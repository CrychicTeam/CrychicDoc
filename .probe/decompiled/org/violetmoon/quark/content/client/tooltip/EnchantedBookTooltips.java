package org.violetmoon.quark.content.client.tooltip;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Either;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.client.module.ImprovedTooltipsModule;
import org.violetmoon.quark.content.tools.item.AncientTomeItem;
import org.violetmoon.quark.content.tools.module.AncientTomesModule;
import org.violetmoon.zeta.client.event.play.ZGatherTooltipComponents;
import org.violetmoon.zeta.module.IDisableable;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class EnchantedBookTooltips {

    private static List<ItemStack> testItems = null;

    private static Multimap<Enchantment, ItemStack> additionalStacks = null;

    public static final String TABLE_ONLY_DISPLAY = "quark:only_show_table_enchantments";

    private static ItemStack BOOK;

    public static void reloaded() {
        additionalStacks = null;
        testItems = null;
    }

    public static void makeTooltip(ZGatherTooltipComponents event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            ItemStack stack = event.getItemStack();
            if (stack.getItem() == Items.ENCHANTED_BOOK || stack.getItem() == AncientTomesModule.ancient_tome) {
                List<Either<FormattedText, TooltipComponent>> tooltip = event.getTooltipElements();
                int tooltipIndex = 0;
                for (EnchantmentInstance ed : getEnchantedBookEnchantments(stack)) {
                    Component match;
                    if (stack.getItem() == Items.ENCHANTED_BOOK) {
                        match = ed.enchantment.getFullname(ed.level);
                    } else {
                        match = AncientTomeItem.getFullTooltipText(ed.enchantment);
                    }
                    while (tooltipIndex < tooltip.size()) {
                        Either<FormattedText, TooltipComponent> elmAt = (Either<FormattedText, TooltipComponent>) tooltip.get(tooltipIndex);
                        if (elmAt.left().isPresent() && ((FormattedText) elmAt.left().get()).equals(match)) {
                            boolean tableOnly = ItemNBTHelper.getBoolean(stack, "quark:only_show_table_enchantments", false);
                            List<ItemStack> items = getItemsForEnchantment(ed.enchantment, tableOnly);
                            int itemCount = items.size();
                            int lines = (int) Math.ceil((double) itemCount / 10.0);
                            int len = 3 + Math.min(10, itemCount) * 9;
                            tooltip.add(tooltipIndex + 1, Either.right(new EnchantedBookTooltips.EnchantedBookComponent(len, lines * 10, ed.enchantment, tableOnly)));
                            break;
                        }
                        tooltipIndex++;
                    }
                }
            }
        }
    }

    private static List<ItemStack> getItemsForEnchantment(Enchantment e, boolean onlyForTable) {
        List<ItemStack> list = new ArrayList();
        for (ItemStack stack : getTestItems()) {
            if ((!(stack.getItem() instanceof IDisableable<?> disableable) || disableable.isEnabled()) && !stack.isEmpty() && e.canEnchant(stack) && (!onlyForTable || e.canApplyAtEnchantingTable(stack) && stack.isEnchantable() && Quark.ZETA.itemExtensions.get(stack).getEnchantmentValueZeta(stack) > 0)) {
                list.add(stack);
            }
        }
        if (onlyForTable) {
            if (BOOK == null) {
                BOOK = new ItemStack(Items.BOOK);
            }
            list.add(BOOK);
        }
        if (getAdditionalStacks().containsKey(e)) {
            list.addAll(getAdditionalStacks().get(e));
        }
        return list;
    }

    private static List<EnchantmentInstance> getEnchantedBookEnchantments(ItemStack stack) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        List<EnchantmentInstance> retList = new ArrayList(enchantments.size());
        for (Enchantment enchantment : enchantments.keySet()) {
            if (enchantment != null) {
                int level = (Integer) enchantments.get(enchantment);
                retList.add(new EnchantmentInstance(enchantment, level));
            }
        }
        return retList;
    }

    private static Multimap<Enchantment, ItemStack> getAdditionalStacks() {
        if (additionalStacks == null) {
            computeAdditionalStacks();
        }
        return additionalStacks;
    }

    public static List<ItemStack> getTestItems() {
        if (testItems == null) {
            computeTestItems();
        }
        return testItems;
    }

    private static void computeTestItems() {
        testItems = ImprovedTooltipsModule.enchantingStacks.stream().map(ResourceLocation::new).map(BuiltInRegistries.ITEM::m_7745_).filter(i -> i != Items.AIR).map(ItemStack::new).toList();
    }

    private static void computeAdditionalStacks() {
        additionalStacks = HashMultimap.create();
        for (String s : ImprovedTooltipsModule.enchantingAdditionalStacks) {
            if (s.contains("=")) {
                String[] tokens = s.split("=");
                String left = tokens[0];
                String right = tokens[1];
                BuiltInRegistries.ENCHANTMENT.getOptional(new ResourceLocation(left)).ifPresent(ench -> {
                    for (String itemId : right.split(",")) {
                        BuiltInRegistries.ITEM.m_6612_(new ResourceLocation(itemId)).ifPresent(item -> additionalStacks.put(ench, new ItemStack(item)));
                    }
                });
            }
        }
    }

    public static record EnchantedBookComponent(int width, int height, Enchantment enchantment, boolean tableOnly) implements ClientTooltipComponent, TooltipComponent {

        @Override
        public void renderImage(@NotNull Font font, int tooltipX, int tooltipY, @NotNull GuiGraphics guiGraphics) {
            PoseStack pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate((float) tooltipX, (float) tooltipY, 0.0F);
            pose.scale(0.5F, 0.5F, 1.0F);
            List<ItemStack> items = EnchantedBookTooltips.getItemsForEnchantment(this.enchantment, this.tableOnly);
            int drawn = 0;
            for (ItemStack testStack : items) {
                guiGraphics.renderItem(testStack, 6 + drawn % 10 * 18, drawn / 10 * 20);
                drawn++;
            }
            pose.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        @Override
        public int getHeight() {
            return this.height;
        }

        @Override
        public int getWidth(@NotNull Font font) {
            return this.width;
        }
    }
}