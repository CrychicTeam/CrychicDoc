package dev.latvian.mods.kubejs.item.custom;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class BasicItemJS extends Item {

    private final ItemBuilder itemBuilder;

    private final Multimap<Attribute, AttributeModifier> attributes;

    private boolean modified = false;

    public BasicItemJS(ItemBuilder p) {
        super(p.createItemProperties());
        this.itemBuilder = p;
        if (p.burnTime > 0) {
            FuelRegistry.register(p.burnTime, this);
        }
        this.attributes = ArrayListMultimap.create();
    }

    public ItemBuilder kjs$getItemBuilder() {
        return this.itemBuilder;
    }

    @Override
    public Component getName(ItemStack itemStack) {
        return this.itemBuilder.displayName != null && this.itemBuilder.formattedDisplayName ? this.itemBuilder.displayName : super.getName(itemStack);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        if (!this.modified) {
            this.itemBuilder.attributes.forEach((r, m) -> this.attributes.put(RegistryInfo.ATTRIBUTE.getValue(r), m));
            this.modified = true;
        }
        return slot == EquipmentSlot.MAINHAND ? this.attributes : super.getDefaultAttributeModifiers(slot);
    }

    public static class Builder extends ItemBuilder {

        public Builder(ResourceLocation i) {
            super(i);
        }

        public Item createObject() {
            return new BasicItemJS(this);
        }
    }
}