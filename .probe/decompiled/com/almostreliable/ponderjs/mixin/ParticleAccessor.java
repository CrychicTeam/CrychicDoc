package com.almostreliable.ponderjs.mixin;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ Particle.class })
public interface ParticleAccessor {

    @Accessor("hasPhysics")
    void ponderjs$setHasPhysics(boolean var1);

    @Accessor("gravity")
    void ponderjs$setGravity(float var1);

    @Accessor("stoppedByCollision")
    void ponderjs$setStoppedByCollision(boolean var1);

    @Accessor("roll")
    void ponderjs$setRoll(float var1);

    @Accessor("friction")
    void ponderjs$setFriction(float var1);

    @Accessor("alpha")
    void ponderjs$setAlpha(float var1);

    @Accessor("lifetime")
    void ponderjs$setLifetime(int var1);
}