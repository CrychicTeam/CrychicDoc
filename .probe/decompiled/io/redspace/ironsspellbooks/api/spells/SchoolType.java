package io.redspace.ironsspellbooks.api.spells;

import io.redspace.ironsspellbooks.api.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.joml.Vector3f;

public class SchoolType {

    final ResourceLocation id;

    final TagKey<Item> focus;

    final Component displayName;

    final Style displayStyle;

    final LazyOptional<Attribute> powerAttribute;

    final LazyOptional<Attribute> resistanceAttribute;

    final LazyOptional<SoundEvent> defaultCastSound;

    final ResourceKey<DamageType> damageType;

    public SchoolType(ResourceLocation id, TagKey<Item> focus, Component displayName, LazyOptional<Attribute> powerAttribute, LazyOptional<Attribute> resistanceAttribute, LazyOptional<SoundEvent> defaultCastSound, ResourceKey<DamageType> damageType) {
        this.id = id;
        this.focus = focus;
        this.displayName = displayName;
        this.displayStyle = displayName.getStyle();
        this.powerAttribute = powerAttribute;
        this.resistanceAttribute = resistanceAttribute;
        this.defaultCastSound = defaultCastSound;
        this.damageType = damageType;
    }

    public double getResistanceFor(LivingEntity livingEntity) {
        Attribute resistanceAttribute = this.resistanceAttribute.orElse(null);
        return resistanceAttribute != null ? livingEntity.getAttributeValue(resistanceAttribute) : 1.0;
    }

    public double getPowerFor(LivingEntity livingEntity) {
        Attribute powerAttribute = this.powerAttribute.orElse(null);
        return powerAttribute != null ? livingEntity.getAttributeValue(powerAttribute) : 1.0;
    }

    public SoundEvent getCastSound() {
        return (SoundEvent) this.defaultCastSound.resolve().get();
    }

    public ResourceKey<DamageType> getDamageType() {
        return this.damageType;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public Component getDisplayName() {
        return this.displayName;
    }

    public boolean isFocus(ItemStack itemStack) {
        return itemStack.is(this.focus);
    }

    public Vector3f getTargetingColor() {
        return Utils.deconstructRGB(this.displayStyle.getColor().getValue());
    }
}