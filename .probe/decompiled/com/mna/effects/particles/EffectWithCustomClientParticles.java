package com.mna.effects.particles;

import com.mna.capabilities.entity.MAPFX;
import com.mna.capabilities.entity.MAPFXProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectWithCustomClientParticles extends MobEffect {

    private MAPFX.Flag[] __flags;

    protected EffectWithCustomClientParticles(MobEffectCategory typeIn, int liquidColorIn, MAPFX.Flag... flags) {
        super(typeIn, liquidColorIn);
        this.__flags = flags;
    }

    public void setFlags(LivingEntity entityLivingBaseIn, boolean on) {
        entityLivingBaseIn.getCapability(MAPFXProvider.MAPFX).ifPresent(p -> {
            for (MAPFX.Flag __flag : this.__flags) {
                p.setFlag(entityLivingBaseIn, __flag, on);
            }
        });
    }
}