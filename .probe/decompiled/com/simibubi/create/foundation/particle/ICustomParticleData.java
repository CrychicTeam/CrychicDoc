package com.simibubi.create.foundation.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

public interface ICustomParticleData<T extends ParticleOptions> {

    ParticleOptions.Deserializer<T> getDeserializer();

    Codec<T> getCodec(ParticleType<T> var1);

    default ParticleType<T> createType() {
        return new ParticleType<T>(false, this.getDeserializer()) {

            @Override
            public Codec<T> codec() {
                return ICustomParticleData.this.getCodec(this);
            }
        };
    }

    @OnlyIn(Dist.CLIENT)
    ParticleProvider<T> getFactory();

    @OnlyIn(Dist.CLIENT)
    default void register(ParticleType<T> type, RegisterParticleProvidersEvent event) {
        event.registerSpecial(type, this.getFactory());
    }
}