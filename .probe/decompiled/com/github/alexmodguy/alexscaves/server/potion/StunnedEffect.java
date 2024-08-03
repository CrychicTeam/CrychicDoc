package com.github.alexmodguy.alexscaves.server.potion;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class StunnedEffect extends MobEffect {

    protected StunnedEffect() {
        super(MobEffectCategory.HARMFUL, 16776133);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160892", -1.0, AttributeModifier.Operation.MULTIPLY_BASE);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.m_20184_().y > 0.0) {
            entity.m_20256_(entity.m_20184_().multiply(1.0, 0.1, 1.0));
        }
        if (entity.m_9236_().random.nextFloat() < entity.m_20205_() * 0.12F) {
            entity.m_9236_().addParticle(ACParticleRegistry.STUN_STAR.get(), entity.m_20185_(), entity.m_20188_(), entity.m_20189_(), (double) entity.m_19879_(), (double) (entity.m_9236_().random.nextFloat() * 360.0F), 0.0);
        }
        if (entity instanceof Mob mob) {
            entity.m_146926_(30.0F);
            entity.f_19860_ = 30.0F;
            if (!mob.m_9236_().isClientSide) {
                mob.goalSelector.setControlFlag(Goal.Flag.MOVE, false);
                mob.goalSelector.setControlFlag(Goal.Flag.JUMP, false);
                mob.goalSelector.setControlFlag(Goal.Flag.LOOK, false);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }
}