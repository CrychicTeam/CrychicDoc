package com.mna.effects.beneficial;

import com.mna.capabilities.entity.MAPFX;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import net.minecraft.world.effect.MobEffectCategory;

public class EffectFireShield extends EffectWithCustomClientParticles implements INoCreeperLingering {

    public EffectFireShield() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.FIRE_SHIELD);
    }
}