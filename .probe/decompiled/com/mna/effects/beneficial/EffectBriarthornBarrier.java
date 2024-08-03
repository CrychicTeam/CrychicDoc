package com.mna.effects.beneficial;

import com.mna.capabilities.entity.MAPFX;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectBriarthornBarrier extends EffectWithCustomClientParticles implements INoCreeperLingering {

    public EffectBriarthornBarrier() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.BRIARTHORN_BARRIER);
        this.m_19472_(Attributes.ARMOR, "24602b52-344f-4f69-b316-85dd7a65b8bf", 4.0, AttributeModifier.Operation.ADDITION);
    }
}