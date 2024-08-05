package com.mna.effects.harmful;

import com.mna.effects.interfaces.IDoubleTapEndEarly;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectMindVision extends MobEffect implements IDoubleTapEndEarly, INoCreeperLingering {

    public EffectMindVision() {
        super(MobEffectCategory.BENEFICIAL, 0);
    }
}