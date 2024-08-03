package com.mna.effects.beneficial;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectSpiderClimbing extends MobEffect implements INoCreeperLingering {

    public EffectSpiderClimbing() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }
}