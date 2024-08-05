package com.mna.effects.harmful;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectAsphyxiate extends MobEffect {

    public EffectAsphyxiate() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.m_20301_(Math.max(entityLivingBaseIn.m_20146_() - (amplifier + 4), 0));
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}