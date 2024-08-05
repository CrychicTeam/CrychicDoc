package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class EffectOiled extends MobEffect {

    public EffectOiled() {
        super(MobEffectCategory.BENEFICIAL, 16771228);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.m_20071_()) {
            if (!entity.m_6144_()) {
                entity.m_20256_(entity.m_20184_().add(0.0, 0.1, 0.0));
            } else {
                entity.f_19789_ = 0.0F;
            }
            if (!entity.m_20096_()) {
                Vec3 vector3d = entity.m_20184_();
                entity.m_20256_(vector3d.multiply(1.0, 0.9, 1.0));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.oiled";
    }
}