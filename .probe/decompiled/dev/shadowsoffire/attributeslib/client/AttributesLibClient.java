package dev.shadowsoffire.attributeslib.client;

import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.mojang.datafixers.util.Pair;
import dev.shadowsoffire.attributeslib.ALConfig;
import dev.shadowsoffire.attributeslib.AttributesLib;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import dev.shadowsoffire.attributeslib.api.AttributeHelper;
import dev.shadowsoffire.attributeslib.api.IFormattableAttribute;
import dev.shadowsoffire.attributeslib.api.client.AddAttributeTooltipsEvent;
import dev.shadowsoffire.attributeslib.api.client.GatherEffectScreenTooltipsEvent;
import dev.shadowsoffire.attributeslib.api.client.GatherSkippedAttributeTooltipsEvent;
import dev.shadowsoffire.attributeslib.util.IFlying;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.CritParticle;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class AttributesLibClient {

    private static final UUID FAKE_MERGED_UUID = UUID.fromString("a6b0ac71-e435-416e-a991-7623eaa129a4");

    @SubscribeEvent
    public void updateClientFlyStateOnRespawn(ClientPlayerNetworkEvent.Clone e) {
        if (e.getOldPlayer().m_150110_().flying) {
            ((IFlying) e.getNewPlayer()).markFlying();
        }
    }

    @SubscribeEvent
    public static void clientReload(RegisterClientReloadListenersEvent e) {
        e.registerReloadListener(ALConfig.makeReloader());
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent e) {
        if (ModList.get().isLoaded("curios")) {
            MinecraftForge.EVENT_BUS.register(new CuriosClientCompat());
        }
    }

    @SubscribeEvent
    public static void particleFactories(RegisterParticleProvidersEvent e) {
        e.registerSprite(ALObjects.Particles.APOTH_CRIT.get(), AttributesLibClient.ApothCritParticle::new);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void tooltips(ItemTooltipEvent e) {
        ItemStack stack = e.getItemStack();
        List<Component> list = e.getToolTip();
        int markIdx1 = -1;
        int markIdx2 = -1;
        for (int i = 0; i < list.size(); i++) {
            ComponentContents var8 = ((Component) list.get(i)).getContents();
            if (var8 instanceof LiteralContents) {
                LiteralContents tc = (LiteralContents) var8;
                if ("APOTH_REMOVE_MARKER".equals(tc.text())) {
                    markIdx1 = i;
                }
                if ("APOTH_REMOVE_MARKER_2".equals(tc.text())) {
                    markIdx2 = i;
                    break;
                }
            }
        }
        if (markIdx1 != -1 && markIdx2 != -1) {
            ListIterator<Component> it = list.listIterator(markIdx1);
            for (int ix = markIdx1; ix < markIdx2 + 1; ix++) {
                it.next();
                it.remove();
            }
            int flags = getHideFlags(stack);
            if (shouldShowInTooltip(flags, ItemStack.TooltipPart.MODIFIERS)) {
                applyModifierTooltips(e.getEntity(), stack, it::add, e.getFlags());
            }
            MinecraftForge.EVENT_BUS.post(new AddAttributeTooltipsEvent(stack, e.getEntity(), list, it, e.getFlags()));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void addAttribComponent(ScreenEvent.Init.Post e) {
        if (ALConfig.enableAttributesGui && e.getScreen() instanceof InventoryScreen scn) {
            AttributesGui atrComp = new AttributesGui(scn);
            e.addListener(atrComp);
            e.addListener(atrComp.toggleBtn);
            e.addListener(atrComp.hideUnchangedBtn);
            if (AttributesGui.wasOpen || AttributesGui.swappedFromCurios) {
                atrComp.toggleVisibility();
            }
            AttributesGui.swappedFromCurios = false;
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void effectGuiTooltips(GatherEffectScreenTooltipsEvent e) {
        List<Component> tooltips = e.getTooltip();
        MobEffectInstance effectInst = e.getEffectInstance();
        MobEffect effect = effectInst.getEffect();
        MutableComponent name = (MutableComponent) tooltips.get(0);
        Component duration = (Component) tooltips.remove(1);
        Component var14 = Component.translatable("(%s)", duration).withStyle(ChatFormatting.WHITE);
        name.append(" ").append(var14);
        if (AttributesLib.getTooltipFlag().isAdvanced()) {
            name.append(" ").append(Component.translatable("[%s]", BuiltInRegistries.MOB_EFFECT.getKey(effect)).withStyle(ChatFormatting.GRAY));
        }
        String key = effect.getDescriptionId() + ".desc";
        if (I18n.exists(key)) {
            tooltips.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
        } else if (AttributesLib.getTooltipFlag().isAdvanced() && effect.getAttributeModifiers().isEmpty()) {
            tooltips.add(Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
        }
        List<Pair<Attribute, AttributeModifier>> list = Lists.newArrayList();
        Map<Attribute, AttributeModifier> map = effect.getAttributeModifiers();
        if (!map.isEmpty()) {
            for (Entry<Attribute, AttributeModifier> entry : map.entrySet()) {
                AttributeModifier attributemodifier = (AttributeModifier) entry.getValue();
                AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), effect.getAttributeModifierValue(effectInst.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                list.add(new Pair((Attribute) entry.getKey(), attributemodifier1));
            }
        }
        if (!list.isEmpty()) {
            for (Pair<Attribute, AttributeModifier> pair : list) {
                tooltips.add(IFormattableAttribute.toComponent((Attribute) pair.getFirst(), (AttributeModifier) pair.getSecond(), AttributesLib.getTooltipFlag()));
            }
        }
    }

    @SubscribeEvent
    public void potionTooltips(ItemTooltipEvent e) {
        if (ALConfig.enablePotionTooltips) {
            ItemStack stack = e.getItemStack();
            List<Component> tooltips = e.getToolTip();
            if (stack.getItem() instanceof PotionItem) {
                List<MobEffectInstance> effects = PotionUtils.getMobEffects(stack);
                if (effects.size() == 1 && tooltips.size() >= 2) {
                    MobEffect effect = ((MobEffectInstance) effects.get(0)).getEffect();
                    String key = effect.getDescriptionId() + ".desc";
                    if (I18n.exists(key)) {
                        tooltips.add(2, Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY));
                    } else if (e.getFlags().isAdvanced() && effect.getAttributeModifiers().isEmpty()) {
                        tooltips.add(2, Component.translatable(key).withStyle(ChatFormatting.DARK_GRAY, ChatFormatting.ITALIC));
                    }
                }
            }
        }
    }

    public static Multimap<Attribute, AttributeModifier> getSortedModifiers(ItemStack stack, EquipmentSlot slot) {
        Multimap<Attribute, AttributeModifier> unsorted = stack.getAttributeModifiers(slot);
        Multimap<Attribute, AttributeModifier> map = AttributeHelper.sortedMap();
        for (Entry<Attribute, AttributeModifier> ent : unsorted.entries()) {
            if (ent.getKey() != null && ent.getValue() != null) {
                map.put((Attribute) ent.getKey(), (AttributeModifier) ent.getValue());
            } else {
                AttributesLib.LOGGER.debug("Detected broken attribute modifier entry on item {}.  Attr={}, Modif={}", stack, ent.getKey(), ent.getValue());
            }
        }
        return map;
    }

    public static void apothCrit(int entityId) {
        Entity entity = Minecraft.getInstance().level.getEntity(entityId);
        if (entity != null) {
            Minecraft.getInstance().particleEngine.createTrackingEmitter(entity, ALObjects.Particles.APOTH_CRIT.get());
        }
    }

    private static boolean shouldShowInTooltip(int pHideFlags, ItemStack.TooltipPart pPart) {
        return (pHideFlags & pPart.getMask()) == 0;
    }

    private static int getHideFlags(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains("HideFlags", 99) ? stack.getTag().getInt("HideFlags") : stack.getItem().getDefaultTooltipHideFlags(stack);
    }

    private static void applyModifierTooltips(@Nullable Player player, ItemStack stack, Consumer<Component> tooltip, TooltipFlag flag) {
        Multimap<Attribute, AttributeModifier> mainhand = getSortedModifiers(stack, EquipmentSlot.MAINHAND);
        Multimap<Attribute, AttributeModifier> offhand = getSortedModifiers(stack, EquipmentSlot.OFFHAND);
        Multimap<Attribute, AttributeModifier> dualHand = AttributeHelper.sortedMap();
        for (Attribute atr : mainhand.keys()) {
            Collection<AttributeModifier> modifMh = mainhand.get(atr);
            Collection<AttributeModifier> modifOh = offhand.get(atr);
            modifMh.stream().filter(a1 -> modifOh.stream().anyMatch(a2 -> a1.getId().equals(a2.getId()))).forEach(modif -> dualHand.put(atr, modif));
        }
        dualHand.values().forEach(m -> {
            mainhand.values().remove(m);
            offhand.values().removeIf(m1 -> m1.getId().equals(m.getId()));
        });
        Set<UUID> skips = new HashSet();
        MinecraftForge.EVENT_BUS.post(new GatherSkippedAttributeTooltipsEvent(stack, player, skips, flag));
        applyTextFor(player, stack, tooltip, dualHand, "both_hands", skips, flag);
        applyTextFor(player, stack, tooltip, mainhand, EquipmentSlot.MAINHAND.getName(), skips, flag);
        applyTextFor(player, stack, tooltip, offhand, EquipmentSlot.OFFHAND.getName(), skips, flag);
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.ordinal() >= 2) {
                Multimap<Attribute, AttributeModifier> modifiers = getSortedModifiers(stack, slot);
                applyTextFor(player, stack, tooltip, modifiers, slot.getName(), skips, flag);
            }
        }
    }

    private static MutableComponent padded(String padding, Component comp) {
        return Component.literal(padding).append(comp);
    }

    private static MutableComponent list() {
        return AttributeHelper.list();
    }

    private static void applyTextFor(@Nullable Player player, ItemStack stack, Consumer<Component> tooltip, Multimap<Attribute, AttributeModifier> modifierMap, String group, Set<UUID> skips, TooltipFlag flag) {
        if (!modifierMap.isEmpty()) {
            modifierMap.values().removeIf(m -> skips.contains(m.getId()));
            tooltip.accept(Component.empty());
            tooltip.accept(Component.translatable("item.modifiers." + group).withStyle(ChatFormatting.GRAY));
            if (modifierMap.isEmpty()) {
                return;
            }
            Map<Attribute, AttributesLibClient.BaseModifier> baseModifs = new IdentityHashMap();
            modifierMap.forEach((attrx, modif) -> {
                if (modif.getId().equals(((IFormattableAttribute) attrx).getBaseUUID())) {
                    baseModifs.put(attrx, new AttributesLibClient.BaseModifier(modif, new ArrayList()));
                }
            });
            modifierMap.forEach((attrx, modif) -> {
                AttributesLibClient.BaseModifier basex = (AttributesLibClient.BaseModifier) baseModifs.get(attrx);
                if (basex != null && basex.base != modif) {
                    basex.children.add(modif);
                }
            });
            for (Entry<Attribute, AttributesLibClient.BaseModifier> entry : baseModifs.entrySet()) {
                Attribute attr = (Attribute) entry.getKey();
                AttributesLibClient.BaseModifier baseModif = (AttributesLibClient.BaseModifier) entry.getValue();
                double entityBase = player == null ? 0.0 : player.m_21172_(attr);
                double base = baseModif.base.getAmount() + entityBase;
                double rawBase = base;
                double amt = base;
                double baseBonus = ((IFormattableAttribute) attr).getBonusBaseValue(stack);
                for (AttributeModifier modif : baseModif.children) {
                    if (modif.getOperation() == AttributeModifier.Operation.ADDITION) {
                        base = amt += modif.getAmount();
                    } else if (modif.getOperation() == AttributeModifier.Operation.MULTIPLY_BASE) {
                        amt += modif.getAmount() * base;
                    } else {
                        amt *= 1.0 + modif.getAmount();
                    }
                }
                amt += baseBonus;
                boolean isMerged = !baseModif.children.isEmpty() || baseBonus != 0.0;
                MutableComponent text = IFormattableAttribute.toBaseComponent(attr, amt, entityBase, isMerged, flag);
                tooltip.accept(padded(" ", text).withStyle(isMerged ? ChatFormatting.GOLD : ChatFormatting.DARK_GREEN));
                if (Screen.hasShiftDown() && isMerged) {
                    text = IFormattableAttribute.toBaseComponent(attr, rawBase, entityBase, false, flag);
                    tooltip.accept(list().append(text.withStyle(ChatFormatting.DARK_GREEN)));
                    for (AttributeModifier modifier : baseModif.children) {
                        tooltip.accept(list().append(IFormattableAttribute.toComponent(attr, modifier, flag)));
                    }
                    if (baseBonus > 0.0) {
                        ((IFormattableAttribute) attr).addBonusTooltips(stack, tooltip, flag);
                    }
                }
            }
            for (Attribute attr : modifierMap.keySet()) {
                if (!baseModifs.containsKey(attr)) {
                    Collection<AttributeModifier> modifs = modifierMap.get(attr);
                    if (modifs.size() > 1) {
                        double[] sums = new double[3];
                        boolean[] merged = new boolean[3];
                        Map<AttributeModifier.Operation, List<AttributeModifier>> shiftExpands = new HashMap();
                        for (AttributeModifier modifier : modifs) {
                            if (modifier.getAmount() != 0.0) {
                                if (sums[modifier.getOperation().ordinal()] != 0.0) {
                                    merged[modifier.getOperation().ordinal()] = true;
                                }
                                sums[modifier.getOperation().ordinal()] += modifier.getAmount();
                                ((List) shiftExpands.computeIfAbsent(modifier.getOperation(), k -> new LinkedList())).add(modifier);
                            }
                        }
                        for (AttributeModifier.Operation op : AttributeModifier.Operation.values()) {
                            int i = op.ordinal();
                            if (sums[i] != 0.0) {
                                if (merged[i]) {
                                    TextColor color = sums[i] < 0.0 ? TextColor.fromRgb(16331057) : TextColor.fromRgb(8026873);
                                    if (sums[i] < 0.0) {
                                        sums[i] *= -1.0;
                                    }
                                    AttributeModifier fakeModif = new AttributeModifier(FAKE_MERGED_UUID, (Supplier<String>) (() -> "attributeslib:merged"), sums[i], op);
                                    MutableComponent comp = IFormattableAttribute.toComponent(attr, fakeModif, flag);
                                    tooltip.accept(comp.withStyle(comp.getStyle().withColor(color)));
                                    if (merged[i] && Screen.hasShiftDown()) {
                                        ((List) shiftExpands.get(AttributeModifier.Operation.fromValue(i))).forEach(modifx -> tooltip.accept(list().append(IFormattableAttribute.toComponent(attr, modifx, flag))));
                                    }
                                } else {
                                    AttributeModifier fakeModif = new AttributeModifier(FAKE_MERGED_UUID, (Supplier<String>) (() -> "attributeslib:merged"), sums[i], op);
                                    tooltip.accept(IFormattableAttribute.toComponent(attr, fakeModif, flag));
                                }
                            }
                        }
                    } else {
                        modifs.forEach(m -> {
                            if (m.getAmount() != 0.0) {
                                tooltip.accept(IFormattableAttribute.toComponent(attr, m, flag));
                            }
                        });
                    }
                }
            }
        }
    }

    public static class ApothCritParticle extends CritParticle {

        public ApothCritParticle(SimpleParticleType type, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            super(pLevel, pX, pY, pZ, pXSpeed, pYSpeed, pZSpeed);
            this.f_107229_ = 1.0F;
            this.f_107227_ = 0.3F;
            this.f_107228_ = 0.8F;
        }
    }

    private static record BaseModifier(AttributeModifier base, List<AttributeModifier> children) {
    }
}