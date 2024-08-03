package net.minecraft.core.particles;

import com.mojang.serialization.Codec;

public abstract class ParticleType<T extends ParticleOptions> {

    private final boolean overrideLimiter;

    private final ParticleOptions.Deserializer<T> deserializer;

    protected ParticleType(boolean boolean0, ParticleOptions.Deserializer<T> particleOptionsDeserializerT1) {
        this.overrideLimiter = boolean0;
        this.deserializer = particleOptionsDeserializerT1;
    }

    public boolean getOverrideLimiter() {
        return this.overrideLimiter;
    }

    public ParticleOptions.Deserializer<T> getDeserializer() {
        return this.deserializer;
    }

    public abstract Codec<T> codec();
}