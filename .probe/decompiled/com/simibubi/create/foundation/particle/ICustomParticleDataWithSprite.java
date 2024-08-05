package com.simibubi.create.foundation.particle;

import com.mojang.serialization.Codec;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

public interface ICustomParticleDataWithSprite<T extends ParticleOptions> extends ICustomParticleData<T> {

    @Override
    ParticleOptions.Deserializer<T> getDeserializer();

    @Override
    default ParticleType<T> createType() {
        return new ParticleType<T>(false, this.getDeserializer()) {

            @Override
            public Codec<T> codec() {
                return ICustomParticleDataWithSprite.this.getCodec(this);
            }
        };
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    default ParticleProvider<T> getFactory() {
        throw new IllegalAccessError("This particle type uses a metaFactory!");
    }

    @OnlyIn(Dist.CLIENT)
    ParticleEngine.SpriteParticleRegistration<T> getMetaFactory();

    @OnlyIn(Dist.CLIENT)
    @Override
    default void register(ParticleType<T> type, RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(type, this.getMetaFactory());
    }
}