package com.almostreliable.ponderjs.mixin;

import com.simibubi.create.foundation.ponder.PonderWorld;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ PonderWorld.class })
public interface PonderWorldAccessor {

    @Invoker(value = "makeParticle", remap = false)
    <T extends ParticleOptions> Particle ponderjs$makeParticle(T var1, double var2, double var4, double var6, double var8, double var10, double var12);
}