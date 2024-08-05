package com.squoshi.irons_spells_js.spell.school;

import com.squoshi.irons_spells_js.IronsSpellsJSPlugin;
import com.squoshi.irons_spells_js.util.ISSKJSUtils;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import io.redspace.ironsspellbooks.api.spells.SchoolType;
import java.util.Objects;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;

public class SchoolTypeJSBuilder extends BuilderBase<SchoolType> {

    public transient ResourceLocation schoolResource;

    public transient TagKey<Item> focus;

    public transient Component name;

    public transient LazyOptional<Attribute> powerAttribute;

    public transient LazyOptional<Attribute> resistanceAttribute;

    public transient LazyOptional<SoundEvent> defaultCastSound;

    public transient ResourceKey<DamageType> damageType;

    public SchoolTypeJSBuilder(ResourceLocation i) {
        super(i);
        this.schoolResource = i;
    }

    public SchoolTypeJSBuilder setFocus(ResourceLocation focus) {
        this.focus = ItemTags.create(focus);
        return this;
    }

    public SchoolTypeJSBuilder setName(Component name) {
        this.name = name;
        return this;
    }

    public SchoolTypeJSBuilder setPowerAttribute(ISSKJSUtils.AttributeHolder powerAttribute) {
        this.powerAttribute = LazyOptional.of(() -> (Attribute) Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(powerAttribute.getLocation())));
        return this;
    }

    public SchoolTypeJSBuilder setResistanceAttribute(ISSKJSUtils.AttributeHolder resistanceAttribute) {
        this.resistanceAttribute = LazyOptional.of(() -> (Attribute) Objects.requireNonNull(ForgeRegistries.ATTRIBUTES.getValue(resistanceAttribute.getLocation())));
        return this;
    }

    public SchoolTypeJSBuilder setDefaultCastSound(ISSKJSUtils.SoundEventHolder defaultCastSound) {
        this.defaultCastSound = LazyOptional.of(() -> (SoundEvent) Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(defaultCastSound.getLocation())));
        return this;
    }

    public SchoolTypeJSBuilder setDamageType(ISSKJSUtils.DamageTypeHolder damageType) {
        this.damageType = ResourceKey.create(Registries.DAMAGE_TYPE, damageType.getLocation());
        return this;
    }

    @Override
    public RegistryInfo<SchoolType> getRegistryType() {
        return IronsSpellsJSPlugin.SCHOOL_REGISTRY;
    }

    public SchoolType createObject() {
        return new SchoolType(this.schoolResource, this.focus, this.name, this.powerAttribute, this.resistanceAttribute, this.defaultCastSound, this.damageType);
    }
}