package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SimpleBeneficialEffect extends MobEffect implements INoCreeperLingering {

    public SimpleBeneficialEffect() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }
}