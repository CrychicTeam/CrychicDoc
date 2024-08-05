package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectLavaVision extends MobEffect {

    public EffectLavaVision() {
        super(MobEffectCategory.BENEFICIAL, 16738816);
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
        return "alexsmobs.potion.lava_vision";
    }
}