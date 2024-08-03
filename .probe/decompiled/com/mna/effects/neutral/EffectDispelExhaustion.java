package com.mna.effects.neutral;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectDispelExhaustion extends MobEffect implements INoCreeperLingering {

    public EffectDispelExhaustion() {
        super(MobEffectCategory.NEUTRAL, 0);
    }
}