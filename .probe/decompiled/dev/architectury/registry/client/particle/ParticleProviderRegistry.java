package dev.architectury.registry.client.particle;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.registry.client.particle.forge.ParticleProviderRegistryImpl;
import dev.architectury.registry.registries.RegistrySupplier;
import java.util.List;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ParticleProviderRegistry {

    public static <T extends ParticleOptions> void register(RegistrySupplier<? extends ParticleType<T>> supplier, ParticleProvider<T> provider) {
        supplier.listen(it -> register(it, provider));
    }

    public static <T extends ParticleOptions> void register(RegistrySupplier<? extends ParticleType<T>> supplier, ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        supplier.listen(it -> register(it, provider));
    }

    @ExpectPlatform
    @Transformed
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProvider<T> provider) {
        ParticleProviderRegistryImpl.register(type, provider);
    }

    @ExpectPlatform
    @Transformed
    public static <T extends ParticleOptions> void register(ParticleType<T> type, ParticleProviderRegistry.DeferredParticleProvider<T> provider) {
        ParticleProviderRegistryImpl.register(type, provider);
    }

    @FunctionalInterface
    public interface DeferredParticleProvider<T extends ParticleOptions> {

        ParticleProvider<T> create(ParticleProviderRegistry.ExtendedSpriteSet var1);
    }

    public interface ExtendedSpriteSet extends SpriteSet {

        TextureAtlas getAtlas();

        List<TextureAtlasSprite> getSprites();
    }
}