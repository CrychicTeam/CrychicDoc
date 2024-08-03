package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectOrcaMight extends MobEffect {

    public EffectOrcaMight() {
        super(MobEffectCategory.BENEFICIAL, 4868690);
        this.m_19472_(Attributes.ATTACK_SPEED, "03C3C89D-7037-4B42-869F-B146BCB64D3A", 3.0, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.orcas_might";
    }
}