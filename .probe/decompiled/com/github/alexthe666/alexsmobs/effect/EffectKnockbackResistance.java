package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectKnockbackResistance extends MobEffect {

    public EffectKnockbackResistance() {
        super(MobEffectCategory.BENEFICIAL, 8803127);
        this.m_19472_(Attributes.KNOCKBACK_RESISTANCE, "03C3C89D-7037-4B42-869F-B146BCB64D2F", 0.5, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void applyEffectTick(LivingEntity LivingEntityIn, int amplifier) {
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.knockback_resistance";
    }
}