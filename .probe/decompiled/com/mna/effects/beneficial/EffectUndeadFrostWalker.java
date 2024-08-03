package com.mna.effects.beneficial;

import com.mna.api.entities.DamageHelper;
import com.mna.capabilities.entity.MAPFX;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import com.mna.tools.SummonUtils;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectUndeadFrostWalker extends EffectWithCustomClientParticles implements INoCreeperLingering {

    public EffectUndeadFrostWalker() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.AURA_OF_FROST);
    }

    @Override
    public void applyEffectTick(LivingEntity living, int amplifier) {
        if (!living.m_9236_().isClientSide()) {
            living.m_9236_().m_6443_(LivingEntity.class, living.m_20191_().inflate(6.0), e -> !SummonUtils.isTargetFriendly(e, living) && e.canFreeze() && e != living).stream().map(e -> e).forEach(le -> {
                le.m_146917_(le.m_146888_() + 5 * amplifier);
                le.hurt(DamageHelper.createSourcedType(DamageHelper.FROST, living.m_9236_().registryAccess(), living), (float) amplifier);
            });
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int baseline = 100;
        int targetDuration = baseline / (amplifier + 1);
        return duration % targetDuration == 0;
    }
}