package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectExsanguination extends MobEffect {

    private int lastDuration = -1;

    protected EffectExsanguination() {
        super(MobEffectCategory.HARMFUL, 15552849);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.hurt(entity.m_269291_().magic(), (float) Math.min(amplifier + 1, Math.round((float) this.lastDuration / 20.0F)));
        for (int i = 0; i < 3; i++) {
            entity.m_9236_().addParticle(ParticleTypes.DAMAGE_INDICATOR, entity.m_20208_(1.0), entity.m_20187_(), entity.m_20262_(1.0), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        this.lastDuration = duration;
        return duration > 0 && duration % 20 == 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.exsanguination";
    }
}