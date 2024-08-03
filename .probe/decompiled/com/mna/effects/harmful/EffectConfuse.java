package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class EffectConfuse extends MobEffect implements INoCreeperLingering {

    public EffectConfuse() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Mob mob) {
            mob.setTarget(null);
            if (Math.random() < 0.5) {
                entityLivingBaseIn.m_9236_().m_6443_(LivingEntity.class, entityLivingBaseIn.m_20191_().inflate(10.0), e -> e.isAlive() && e != entityLivingBaseIn).stream().findFirst().ifPresent(t -> mob.setTarget(t));
            } else if (Math.random() < 0.75) {
                entityLivingBaseIn.m_9236_().m_6443_(Player.class, entityLivingBaseIn.m_20191_().inflate(10.0), e -> e.m_6084_() && e != entityLivingBaseIn).stream().findFirst().ifPresent(t -> mob.setTarget(t));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}