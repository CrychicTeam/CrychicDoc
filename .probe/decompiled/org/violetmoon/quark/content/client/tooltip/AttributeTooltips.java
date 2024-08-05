package org.violetmoon.quark.content.client.tooltip;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ClientUtil;
import org.violetmoon.quark.content.client.hax.PseudoAccessorItemStack;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.quark.content.client.resources.AttributeDisplayType;
import org.violetmoon.quark.content.client.resources.AttributeIconEntry;
import org.violetmoon.quark.content.client.resources.AttributeSlot;
import org.violetmoon.zeta.client.event.play.ZGatherTooltipComponents;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class AttributeTooltips {

    public static final ResourceLocation TEXTURE_UPGRADE = new ResourceLocation("quark", "textures/attribute/upgrade.png");

    public static final ResourceLocation TEXTURE_DOWNGRADE = new ResourceLocation("quark", "textures/attribute/downgrade.png");

    private static final Map<ResourceLocation, AttributeIconEntry> attributes = new HashMap();

    public static void receiveAttributes(Map<String, AttributeIconEntry> map) {
        attributes.clear();
        for (Entry<String, AttributeIconEntry> entry : map.entrySet()) {
            attributes.put(new ResourceLocation((String) entry.getKey()), (AttributeIconEntry) entry.getValue());
        }
    }

    @Nullable
    private static AttributeIconEntry getIconForAttribute(Attribute attribute) {
        ResourceLocation loc = BuiltInRegistries.ATTRIBUTE.getKey(attribute);
        return loc != null ? (AttributeIconEntry) attributes.get(loc) : null;
    }

    private static MutableComponent format(Attribute attribute, double value, AttributeDisplayType displayType) {
        switch(displayType) {
            case DIFFERENCE:
                return Component.literal((value > 0.0 ? "+" : "") + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value)).withStyle(value < 0.0 ? ChatFormatting.RED : ChatFormatting.WHITE);
            case PERCENTAGE:
                return Component.literal((value > 0.0 ? "+" : "") + ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value * 100.0) + "%").withStyle(value < 0.0 ? ChatFormatting.RED : ChatFormatting.WHITE);
            case MULTIPLIER:
                AttributeSupplier supplier = DefaultAttributes.getSupplier(EntityType.PLAYER);
                double scaledValue = value / supplier.getBaseValue(attribute);
                return Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(scaledValue) + "x").withStyle(scaledValue < 1.0 ? ChatFormatting.RED : ChatFormatting.WHITE);
            default:
                return Component.literal(ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(value)).withStyle(value < 0.0 ? ChatFormatting.RED : ChatFormatting.WHITE);
        }
    }

    public static void makeTooltip(ZGatherTooltipComponents event) {
        ItemStack stack = event.getItemStack();
        if (!Screen.hasShiftDown()) {
            List<Either<FormattedText, TooltipComponent>> tooltipRaw = event.getTooltipElements();
            Map<AttributeSlot, MutableComponent> attributeTooltips = Maps.newHashMap();
            boolean onlyInvalid = true;
            Multimap<Attribute, AttributeModifier> baseCheck = null;
            boolean allAreSame = true;
            for (AttributeSlot slot : AttributeSlot.values()) {
                if (canShowAttributes(stack, slot)) {
                    Multimap<Attribute, AttributeModifier> slotAttributes = getModifiers(stack, slot);
                    if (baseCheck == null) {
                        baseCheck = slotAttributes;
                    } else if (slot.hasCanonicalSlot() && allAreSame && !slotAttributes.equals(baseCheck)) {
                        allAreSame = false;
                    }
                    if (!slotAttributes.isEmpty() && !slot.hasCanonicalSlot()) {
                        allAreSame = false;
                    }
                    onlyInvalid = extractAttributeValues(stack, attributeTooltips, onlyInvalid, slot, slotAttributes);
                }
            }
            AttributeSlot primarySlot = getPrimarySlot(stack);
            int i = 1;
            for (AttributeSlot slotx : AttributeSlot.values()) {
                if (attributeTooltips.containsKey(slotx)) {
                    int tooltipSlot = slotx == primarySlot ? 1 : i;
                    tooltipRaw.add(tooltipSlot, Either.right(new AttributeTooltips.AttributeComponent(stack, slotx)));
                    i++;
                    if (allAreSame) {
                        break;
                    }
                }
            }
        }
    }

    private static Multimap<Attribute, AttributeModifier> getModifiersOnEquipped(Player player, ItemStack stack, Multimap<Attribute, AttributeModifier> attributes, AttributeSlot slot) {
        if (ImprovedTooltipsModule.showUpgradeStatus && slot.hasCanonicalSlot()) {
            ItemStack equipped = player.getItemBySlot(slot.getCanonicalSlot());
            if (!equipped.equals(stack) && !equipped.isEmpty()) {
                equipped.getTooltipLines(player, TooltipFlag.Default.f_256752_);
                return getModifiers(equipped, slot);
            }
        }
        return ImmutableMultimap.of();
    }

    private static Multimap<Attribute, AttributeModifier> getModifiers(ItemStack stack, AttributeSlot slot) {
        Map<AttributeSlot, Multimap<Attribute, AttributeModifier>> capturedModifiers = ((PseudoAccessorItemStack) stack).quark$getCapturedAttributes();
        if (!capturedModifiers.containsKey(slot)) {
            return ImmutableMultimap.of();
        } else {
            Multimap<Attribute, AttributeModifier> map = (Multimap<Attribute, AttributeModifier>) capturedModifiers.get(slot);
            if (slot == AttributeSlot.MAINHAND) {
                if (!map.containsKey(Attributes.ATTACK_DAMAGE) && (map.containsKey(Attributes.ATTACK_SPEED) || EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED) > 0.0F)) {
                    map.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(Util.NIL_UUID, "-", 0.0, AttributeModifier.Operation.ADDITION));
                }
                if (!map.containsKey(Attributes.ATTACK_SPEED) && map.containsKey(Attributes.ATTACK_DAMAGE)) {
                    map.put(Attributes.ATTACK_SPEED, new AttributeModifier(Util.NIL_UUID, "-", 0.0, AttributeModifier.Operation.ADDITION));
                }
                if (!map.containsKey(Attributes.ATTACK_KNOCKBACK) && Quark.ZETA.itemExtensions.get(stack).getEnchantmentLevelZeta(stack, Enchantments.KNOCKBACK) > 0) {
                    map.put(Attributes.ATTACK_KNOCKBACK, new AttributeModifier(Util.NIL_UUID, "-", 0.0, AttributeModifier.Operation.ADDITION));
                }
            }
            return map;
        }
    }

    private static boolean extractAttributeValues(ItemStack stack, Map<AttributeSlot, MutableComponent> attributeTooltips, boolean onlyInvalid, AttributeSlot slot, Multimap<Attribute, AttributeModifier> slotAttributes) {
        boolean anyInvalid = false;
        for (Attribute attr : slotAttributes.keySet()) {
            AttributeIconEntry entry = getIconForAttribute(attr);
            if (entry != null) {
                onlyInvalid = false;
                Minecraft mc = Minecraft.getInstance();
                double attributeValue = getAttribute(mc.player, slot, stack, slotAttributes, attr);
                if (attributeValue != 0.0) {
                    if (!attributeTooltips.containsKey(slot)) {
                        attributeTooltips.put(slot, Component.literal(""));
                    }
                    ((MutableComponent) attributeTooltips.get(slot)).append(format(attr, attributeValue, (AttributeDisplayType) entry.displayTypes().get(slot)).getString()).append("/");
                }
            } else if (!anyInvalid) {
                anyInvalid = true;
                if (!attributeTooltips.containsKey(slot)) {
                    attributeTooltips.put(slot, Component.literal(""));
                }
                ((MutableComponent) attributeTooltips.get(slot)).append("[+]");
            }
        }
        return onlyInvalid;
    }

    private static int renderAttribute(GuiGraphics guiGraphics, Attribute attribute, AttributeSlot slot, int x, int y, ItemStack stack, Multimap<Attribute, AttributeModifier> slotAttributes, Minecraft mc, boolean forceRenderIfZero, Multimap<Attribute, AttributeModifier> equippedSlotAttributes, @Nullable Set<Attribute> equippedAttrsToRender) {
        AttributeIconEntry entry = getIconForAttribute(attribute);
        if (entry != null) {
            if (equippedAttrsToRender != null) {
                equippedAttrsToRender.remove(attribute);
            }
            double value = getAttribute(mc.player, slot, stack, slotAttributes, attribute);
            if (value != 0.0 || forceRenderIfZero) {
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.blit(entry.texture(), x, y, 0.0F, 0.0F, 9, 9, 9, 9);
                MutableComponent valueStr = format(attribute, value, (AttributeDisplayType) entry.displayTypes().get(slot));
                if (ImprovedTooltipsModule.showUpgradeStatus && slot.hasCanonicalSlot()) {
                    AttributeIconEntry.CompareType compareType = entry.comparison();
                    EquipmentSlot equipSlot = slot.getCanonicalSlot();
                    if (mc.player != null) {
                        ItemStack equipped = mc.player.m_6844_(equipSlot);
                        if (!equipped.equals(stack) && !equipped.isEmpty() && !equippedSlotAttributes.isEmpty()) {
                            double otherValue = getAttribute(mc.player, slot, equipped, equippedSlotAttributes, attribute);
                            ChatFormatting color = compareType.getColor(value, otherValue);
                            if (color != ChatFormatting.WHITE) {
                                int xp = x - 2;
                                int yp = y - 2;
                                if (ImprovedTooltipsModule.animateUpDownArrows && QuarkClient.ticker.total % 20.0F < 10.0F) {
                                    yp++;
                                }
                                guiGraphics.blit(color == ChatFormatting.RED ? TEXTURE_DOWNGRADE : TEXTURE_UPGRADE, xp, yp, 0.0F, 0.0F, 13, 13, 13, 13);
                            }
                            valueStr = valueStr.withStyle(color);
                        }
                    }
                }
                guiGraphics.drawString(mc.font, valueStr, x + 12, y + 1, -1);
                x += mc.font.width(valueStr) + 20;
            }
        }
        return x;
    }

    private static AttributeSlot getPrimarySlot(ItemStack stack) {
        return !(stack.getItem() instanceof PotionItem) && !(stack.getItem() instanceof TippedArrowItem) ? AttributeSlot.fromCanonicalSlot(Mob.m_147233_(stack)) : AttributeSlot.POTION;
    }

    private static boolean canShowAttributes(ItemStack stack, AttributeSlot slot) {
        if (stack.isEmpty()) {
            return false;
        } else {
            return slot == AttributeSlot.POTION ? (ItemNBTHelper.getInt(stack, "HideFlags", 0) & 32) == 0 : (ItemNBTHelper.getInt(stack, "HideFlags", 0) & 2) == 0;
        }
    }

    private static double getAttribute(Player player, AttributeSlot slot, ItemStack stack, Multimap<Attribute, AttributeModifier> map, Attribute key) {
        if (player == null) {
            return 0.0;
        } else {
            Collection<AttributeModifier> collection = map.get(key);
            if (collection.isEmpty()) {
                return 0.0;
            } else {
                double value = 0.0;
                AttributeIconEntry entry = getIconForAttribute(key);
                if (entry == null) {
                    return 0.0;
                } else {
                    AttributeDisplayType displayType = (AttributeDisplayType) entry.displayTypes().get(slot);
                    if (displayType != AttributeDisplayType.PERCENTAGE && (slot != AttributeSlot.POTION || !key.equals(Attributes.ATTACK_DAMAGE))) {
                        AttributeInstance attribute = player.m_21051_(key);
                        if (attribute != null) {
                            value = attribute.getBaseValue();
                        }
                    }
                    for (AttributeModifier modifier : collection) {
                        if (modifier.getOperation() == AttributeModifier.Operation.ADDITION) {
                            value += modifier.getAmount();
                        }
                    }
                    double rawValue = value;
                    for (AttributeModifier modifierx : collection) {
                        if (modifierx.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE) {
                            value += rawValue * modifierx.getAmount();
                        }
                    }
                    for (AttributeModifier modifierxx : collection) {
                        if (modifierxx.getOperation() == AttributeModifier.Operation.MULTIPLY_TOTAL) {
                            value += value * modifierxx.getAmount();
                        }
                    }
                    if (key.equals(Attributes.ATTACK_DAMAGE) && slot == AttributeSlot.MAINHAND) {
                        value += (double) EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED);
                    }
                    if (key.equals(Attributes.ATTACK_KNOCKBACK) && slot == AttributeSlot.MAINHAND) {
                        value += (double) Quark.ZETA.itemExtensions.get(stack).getEnchantmentLevelZeta(stack, Enchantments.KNOCKBACK);
                    }
                    if (displayType == AttributeDisplayType.DIFFERENCE && (slot != AttributeSlot.POTION || !key.equals(Attributes.ATTACK_DAMAGE))) {
                        AttributeInstance attribute = player.m_21051_(key);
                        if (attribute != null) {
                            value -= attribute.getBaseValue();
                        }
                    }
                    return value;
                }
            }
        }
    }

    public static record AttributeComponent(ItemStack stack, AttributeSlot slot) implements ClientTooltipComponent, TooltipComponent {

        @Override
        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
            PoseStack pose = guiGraphics.pose();
            if (!Screen.hasShiftDown()) {
                pose.pushPose();
                pose.translate(0.0F, 0.0F, 500.0F);
                RenderSystem.setShader(GameRenderer::m_172817_);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft mc = Minecraft.getInstance();
                int y = tooltipY - 1;
                AttributeSlot primarySlot = AttributeTooltips.getPrimarySlot(this.stack);
                boolean showSlots = false;
                int x = tooltipX;
                if (AttributeTooltips.canShowAttributes(this.stack, this.slot)) {
                    Multimap<Attribute, AttributeModifier> slotAttributes = AttributeTooltips.getModifiers(this.stack, this.slot);
                    Multimap<Attribute, AttributeModifier> presentOnEquipped = AttributeTooltips.getModifiersOnEquipped(mc.player, this.stack, slotAttributes, this.slot);
                    Set<Attribute> equippedAttrsToRender = new LinkedHashSet(presentOnEquipped.keySet());
                    for (Attribute attr : slotAttributes.keySet()) {
                        if (AttributeTooltips.getIconForAttribute(attr) != null && this.slot != primarySlot) {
                            showSlots = true;
                            break;
                        }
                    }
                    boolean anyToRender = false;
                    for (Attribute attrx : slotAttributes.keySet()) {
                        double value = AttributeTooltips.getAttribute(mc.player, this.slot, this.stack, slotAttributes, attrx);
                        if (value != 0.0) {
                            anyToRender = true;
                            break;
                        }
                    }
                    if (anyToRender) {
                        if (showSlots) {
                            RenderSystem.setShader(GameRenderer::m_172817_);
                            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                            guiGraphics.blit(ClientUtil.GENERAL_ICONS, tooltipX, y, (float) (193 + this.slot.ordinal() * 9), 35.0F, 9, 9, 256, 256);
                            x = tooltipX + 20;
                        }
                        for (Attribute key : slotAttributes.keySet()) {
                            x = AttributeTooltips.renderAttribute(guiGraphics, key, this.slot, x, y, this.stack, slotAttributes, mc, false, presentOnEquipped, equippedAttrsToRender);
                        }
                        for (Attribute key : equippedAttrsToRender) {
                            x = AttributeTooltips.renderAttribute(guiGraphics, key, this.slot, x, y, this.stack, slotAttributes, mc, true, presentOnEquipped, null);
                        }
                        for (Attribute key : slotAttributes.keys()) {
                            if (AttributeTooltips.getIconForAttribute(key) == null) {
                                guiGraphics.drawString(font, "[+]", x + 1, y + 1, 16777045, true);
                                break;
                            }
                        }
                    }
                }
                pose.popPose();
            }
        }

        @Override
        public int getHeight() {
            return 10;
        }

        @Override
        public int getWidth(@NotNull Font font) {
            int width = 0;
            if (AttributeTooltips.canShowAttributes(this.stack, this.slot)) {
                Minecraft mc = Minecraft.getInstance();
                Multimap<Attribute, AttributeModifier> slotAttributes = AttributeTooltips.getModifiers(this.stack, this.slot);
                Multimap<Attribute, AttributeModifier> presentOnEquipped = AttributeTooltips.getModifiersOnEquipped(mc.player, this.stack, slotAttributes, this.slot);
                Set<Attribute> equippedAttrsToRender = new LinkedHashSet(presentOnEquipped.keySet());
                AttributeSlot primarySlot = AttributeTooltips.getPrimarySlot(this.stack);
                boolean showSlots = false;
                for (Attribute attr : slotAttributes.keySet()) {
                    if (AttributeTooltips.getIconForAttribute(attr) != null && this.slot != primarySlot) {
                        showSlots = true;
                        break;
                    }
                }
                boolean anyToRender = false;
                for (Attribute attrx : slotAttributes.keySet()) {
                    double value = AttributeTooltips.getAttribute(mc.player, this.slot, this.stack, slotAttributes, attrx);
                    if (value != 0.0) {
                        anyToRender = true;
                        break;
                    }
                }
                if (anyToRender) {
                    if (showSlots) {
                        width += 20;
                    }
                    for (Attribute key : slotAttributes.keySet()) {
                        AttributeIconEntry icons = AttributeTooltips.getIconForAttribute(key);
                        if (icons != null) {
                            equippedAttrsToRender.remove(key);
                            double value = AttributeTooltips.getAttribute(mc.player, this.slot, this.stack, slotAttributes, key);
                            if (value != 0.0) {
                                MutableComponent valueStr = AttributeTooltips.format(key, value, (AttributeDisplayType) icons.displayTypes().get(this.slot));
                                width += font.width(valueStr) + 20;
                            }
                        }
                    }
                    for (Attribute keyx : equippedAttrsToRender) {
                        AttributeIconEntry icons = AttributeTooltips.getIconForAttribute(keyx);
                        if (icons != null) {
                            double value = AttributeTooltips.getAttribute(mc.player, this.slot, this.stack, slotAttributes, keyx);
                            MutableComponent valueStr = AttributeTooltips.format(keyx, value, (AttributeDisplayType) icons.displayTypes().get(this.slot));
                            width += font.width(valueStr) + 20;
                        }
                    }
                    for (Attribute keyxx : slotAttributes.keys()) {
                        if (AttributeTooltips.getIconForAttribute(keyxx) == null) {
                            width += font.width("[+]") + 8;
                            break;
                        }
                    }
                }
            }
            return width - 8;
        }
    }
}