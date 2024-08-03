package com.mna.effects.beneficial;

import com.mna.capabilities.entity.MAPFX;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectCircleOfPower extends EffectWithCustomClientParticles implements INoCreeperLingering {

    public EffectCircleOfPower() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.CIRCLE_OF_POWER);
    }
}