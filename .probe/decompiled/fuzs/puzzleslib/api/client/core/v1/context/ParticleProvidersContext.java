package fuzs.puzzleslib.api.client.core.v1.context;

import fuzs.puzzleslib.api.client.particle.v1.ClientParticleTypes;
import fuzs.puzzleslib.impl.client.particle.ClientParticleTypesImpl;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;

public interface ParticleProvidersContext {

    <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> var1, ParticleProvider<T> var2);

    <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> var1, ParticleProvider.Sprite<T> var2);

    @Deprecated(forRemoval = true)
    default <T extends ParticleOptions> void registerParticleFactory(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> particleFactory) {
        this.registerParticleProvider(particleType, particleFactory);
    }

    <T extends ParticleOptions> void registerParticleProvider(ParticleType<T> var1, ParticleEngine.SpriteParticleRegistration<T> var2);

    default <T extends ParticleOptions> void registerClientParticleProvider(ResourceLocation identifier, ParticleProvider<T> particleProvider) {
        ((ClientParticleTypesImpl) ClientParticleTypes.INSTANCE).getParticleTypesManager(identifier.getNamespace()).register(identifier, particleProvider);
    }

    default <T extends ParticleOptions> void registerClientParticleProvider(ResourceLocation identifier, ParticleProvider.Sprite<T> particleProvider) {
        ((ClientParticleTypesImpl) ClientParticleTypes.INSTANCE).getParticleTypesManager(identifier.getNamespace()).register(identifier, particleProvider);
    }

    default <T extends ParticleOptions> void registerClientParticleProvider(ResourceLocation identifier, ParticleEngine.SpriteParticleRegistration<T> particleFactory) {
        ((ClientParticleTypesImpl) ClientParticleTypes.INSTANCE).getParticleTypesManager(identifier.getNamespace()).register(identifier, particleFactory);
    }
}