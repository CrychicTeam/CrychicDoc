package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectChill extends SimpleHarmfulEffect implements INoCreeperLingering {

    public EffectChill() {
        this.m_19472_(Attributes.MOVEMENT_SPEED, "7107DE5E-7CE8-4030-940E-514C1F160890", -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int k = 50 >> pAmplifier;
        return k > 0 ? pDuration % k == 0 : true;
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.canFreeze()) {
            pLivingEntity.m_146917_(pLivingEntity.m_146888_() + 5 * pAmplifier);
        }
    }
}