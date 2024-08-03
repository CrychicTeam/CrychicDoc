package com.mna.effects.beneficial;

import com.mna.effects.EffectInit;
import net.minecraft.world.entity.LivingEntity;

public class EffectSoar extends SimpleBeneficialEffect {

    public static boolean canSoar(LivingEntity entity) {
        return entity.hasEffect(EffectInit.SOAR.get());
    }
}