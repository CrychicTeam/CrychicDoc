package com.squoshi.irons_spells_js.compat.entityjs.entity.builder;

import com.squoshi.irons_spells_js.compat.entityjs.entity.SpellCastingMobJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SpellCastingMobJSBuilder extends SpellCastingMobBuilder<SpellCastingMobJS> {

    public SpellCastingMobJSBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public EntityType.EntityFactory<SpellCastingMobJS> factory() {
        return (type, level) -> new SpellCastingMobJS(this, type, level);
    }

    @Override
    public AttributeSupplier.Builder getAttributeBuilder() {
        return SpellCastingMobJS.m_21552_().add(Attributes.MAX_HEALTH).add(Attributes.FOLLOW_RANGE).add(Attributes.ATTACK_DAMAGE).add(Attributes.ARMOR).add(Attributes.ARMOR_TOUGHNESS).add(Attributes.ATTACK_SPEED).add(Attributes.ATTACK_KNOCKBACK).add(Attributes.LUCK).add(Attributes.MOVEMENT_SPEED);
    }
}