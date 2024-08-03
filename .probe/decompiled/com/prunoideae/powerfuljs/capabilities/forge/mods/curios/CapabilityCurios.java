package com.prunoideae.powerfuljs.capabilities.forge.mods.curios;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CapabilityCurios {

    public CapabilityCurios.ItemStackBuilder itemStack() {
        return new CapabilityCurios.ItemStackBuilder();
    }

    public static class AttributeModificationContext {

        private final ItemStack stack;

        private final SlotContext context;

        private final UUID uuid;

        private final Multimap<Attribute, AttributeModifier> modifiers;

        public AttributeModificationContext(ItemStack stack, SlotContext context, UUID uuid, Multimap<Attribute, AttributeModifier> modifiers) {
            this.stack = stack;
            this.context = context;
            this.uuid = uuid;
            this.modifiers = modifiers;
        }

        public ItemStack getStack() {
            return this.stack;
        }

        public SlotContext getContext() {
            return this.context;
        }

        public UUID getUUID() {
            return this.uuid;
        }

        public CapabilityCurios.AttributeModificationContext modify(Attribute attribute, String identifier, double amount, AttributeModifier.Operation operation) {
            this.modifiers.put(attribute, new AttributeModifier(new UUID((long) identifier.hashCode(), (long) identifier.hashCode()), identifier, amount, operation));
            return this;
        }

        public CapabilityCurios.AttributeModificationContext remove(Attribute attribute, String identifier) {
            this.modifiers.get(attribute).removeIf(modifier -> modifier.getName().equals(identifier));
            return this;
        }

        @HideFromJS
        public Multimap<Attribute, AttributeModifier> getModifiers() {
            return this.modifiers;
        }
    }

    @FunctionalInterface
    public interface EquipCallback {

        void changed(ItemStack var1, SlotContext var2, ItemStack var3);
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, ICurio> {

        private BiConsumer<ItemStack, SlotContext> curioTick;

        private CapabilityCurios.EquipCallback onEquip;

        private CapabilityCurios.EquipCallback onUnequip;

        private BiPredicate<ItemStack, SlotContext> canEquip;

        private BiPredicate<ItemStack, SlotContext> canUnequip;

        private CapabilityCurios.ShouldDrop shouldDrop;

        private final Multimap<ResourceLocation, AttributeModifier> modifiers = HashMultimap.create();

        private final Multimap<Attribute, AttributeModifier> attributes = HashMultimap.create();

        private Consumer<CapabilityCurios.AttributeModificationContext> dynamicAttribute = null;

        private boolean attributeInitialized = false;

        public CapabilityCurios.ItemStackBuilder curioTick(BiConsumer<ItemStack, SlotContext> curioTick) {
            this.curioTick = curioTick;
            return this;
        }

        public CapabilityCurios.ItemStackBuilder onEquip(CapabilityCurios.EquipCallback onEquip) {
            this.onEquip = onEquip;
            return this;
        }

        public CapabilityCurios.ItemStackBuilder onUnequip(CapabilityCurios.EquipCallback onUnequip) {
            this.onUnequip = onUnequip;
            return this;
        }

        public CapabilityCurios.ItemStackBuilder canEquip(BiPredicate<ItemStack, SlotContext> canEquip) {
            this.canEquip = canEquip;
            return this;
        }

        public CapabilityCurios.ItemStackBuilder canUnequip(BiPredicate<ItemStack, SlotContext> canUnequip) {
            this.canUnequip = canUnequip;
            return this;
        }

        public CapabilityCurios.ItemStackBuilder getDropRule(CapabilityCurios.ShouldDrop getDropRule) {
            this.shouldDrop = getDropRule;
            return this;
        }

        public CapabilityCurios.ItemStackBuilder modifyAttribute(ResourceLocation attribute, String identifier, double d, AttributeModifier.Operation operation) {
            this.modifiers.put(attribute, new AttributeModifier(new UUID((long) identifier.hashCode(), (long) identifier.hashCode()), identifier, d, operation));
            return this;
        }

        public CapabilityCurios.ItemStackBuilder dynamicAttribute(Consumer<CapabilityCurios.AttributeModificationContext> context) {
            this.dynamicAttribute = context;
            return this;
        }

        public ICurio getCapability(ItemStack instance) {
            return new ICurio() {

                @Override
                public ItemStack getStack() {
                    return instance;
                }

                @Override
                public void curioTick(SlotContext slotContext) {
                    if (ItemStackBuilder.this.curioTick != null) {
                        ItemStackBuilder.this.curioTick.accept(instance, slotContext);
                    } else {
                        ICurio.super.curioTick(slotContext);
                    }
                }

                @Override
                public void onEquip(SlotContext slotContext, ItemStack prevStack) {
                    if (ItemStackBuilder.this.onEquip != null) {
                        ItemStackBuilder.this.onEquip.changed(instance, slotContext, prevStack);
                    } else {
                        ICurio.super.onEquip(slotContext, prevStack);
                    }
                }

                @Override
                public void onUnequip(SlotContext slotContext, ItemStack newStack) {
                    if (ItemStackBuilder.this.onUnequip != null) {
                        ItemStackBuilder.this.onUnequip.changed(instance, slotContext, newStack);
                    } else {
                        ICurio.super.onUnequip(slotContext, newStack);
                    }
                }

                @Override
                public boolean canEquip(SlotContext slotContext) {
                    return ItemStackBuilder.this.canEquip != null ? ItemStackBuilder.this.canEquip.test(instance, slotContext) : ICurio.super.canEquip(slotContext);
                }

                @Override
                public boolean canUnequip(SlotContext slotContext) {
                    return ItemStackBuilder.this.canUnequip != null ? ItemStackBuilder.this.canUnequip.test(instance, slotContext) : ICurio.super.canUnequip(slotContext);
                }

                @Override
                public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid) {
                    if (!ItemStackBuilder.this.attributeInitialized) {
                        for (Entry<ResourceLocation, AttributeModifier> entry : ItemStackBuilder.this.modifiers.entries()) {
                            ResourceLocation key = (ResourceLocation) entry.getKey();
                            AttributeModifier modifier = (AttributeModifier) entry.getValue();
                            ItemStackBuilder.this.attributes.put(RegistryInfo.ATTRIBUTE.getValue(key), modifier);
                        }
                        ItemStackBuilder.this.attributeInitialized = true;
                    }
                    if (ItemStackBuilder.this.dynamicAttribute != null) {
                        Multimap<Attribute, AttributeModifier> attributeCopy = HashMultimap.create(ItemStackBuilder.this.attributes);
                        ItemStackBuilder.this.dynamicAttribute.accept(new CapabilityCurios.AttributeModificationContext(instance, slotContext, uuid, attributeCopy));
                        return attributeCopy;
                    } else {
                        return ItemStackBuilder.this.attributes;
                    }
                }

                @NotNull
                @Override
                public ICurio.DropRule getDropRule(SlotContext slotContext, DamageSource source, int lootingLevel, boolean recentlyHit) {
                    if (ItemStackBuilder.this.shouldDrop != null) {
                        return ItemStackBuilder.this.shouldDrop.test(instance, slotContext, source, lootingLevel, recentlyHit) ? ICurio.DropRule.ALWAYS_DROP : ICurio.DropRule.ALWAYS_KEEP;
                    } else {
                        return ICurio.DropRule.DEFAULT;
                    }
                }

                @Override
                public boolean canEquipFromUse(SlotContext slotContext) {
                    return true;
                }
            };
        }

        public Capability<ICurio> getCapabilityKey() {
            return CuriosCapability.ITEM;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return CuriosCapability.ID_ITEM;
        }
    }

    @FunctionalInterface
    public interface ShouldDrop {

        boolean test(ItemStack var1, SlotContext var2, DamageSource var3, int var4, boolean var5);
    }
}