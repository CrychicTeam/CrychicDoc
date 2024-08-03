package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectWaterWalking extends MobEffect implements INoCreeperLingering {

    public EffectWaterWalking() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }
}