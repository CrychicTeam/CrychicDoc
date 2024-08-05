package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectFear extends MobEffect {

    protected EffectFear() {
        super(MobEffectCategory.NEUTRAL, 7632119);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -1.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.m_20184_().y > 0.0 && !entity.m_20072_()) {
            entity.m_20256_(entity.m_20184_().multiply(1.0, 0.0, 1.0));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.fear";
    }
}