package dev.latvian.mods.kubejs.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

public class ParticleTypeBuilder extends BuilderBase<ParticleType<?>> {

    public transient boolean overrideLimiter = false;

    public transient ParticleOptions.Deserializer deserializer;

    public ParticleTypeBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public final RegistryInfo getRegistryType() {
        return RegistryInfo.PARTICLE_TYPE;
    }

    public ParticleType<?> createObject() {
        return (ParticleType<?>) (this.deserializer != null ? new ComplexParticleType(this.overrideLimiter, this.deserializer) : new BasicParticleType(this.overrideLimiter));
    }

    public ParticleTypeBuilder overrideLimiter(boolean o) {
        this.overrideLimiter = o;
        return this;
    }

    public ParticleTypeBuilder deserializer(ParticleOptions.Deserializer d) {
        this.deserializer = d;
        return this;
    }
}