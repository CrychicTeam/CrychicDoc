package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.gameevent.GameEvent;

public class EffectLacerate extends SimpleHarmfulEffect implements INoCreeperLingering {

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int k = 100 >> pAmplifier;
        return k > 0 ? pDuration % k == 0 : true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.setHealth(pLivingEntity.getHealth() - 1.0F);
        pLivingEntity.m_146852_(GameEvent.ENTITY_DAMAGE, (Entity) null);
    }
}