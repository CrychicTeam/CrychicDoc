package fuzs.puzzleslib.impl.client.particle;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDescription;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.Nullable;

public final class ClientParticleTypesManager implements PreparableReloadListener {

    private static final FileToIdConverter PARTICLE_LISTER = FileToIdConverter.json("particles");

    private static final ResourceLocation PARTICLES_ATLAS_INFO = new ResourceLocation("particles");

    private final Map<ResourceLocation, ParticleProvider<?>> providers = Maps.newHashMap();

    private final Map<ResourceLocation, ClientParticleTypesManager.MutableSpriteSet> spriteSets = Maps.newHashMap();

    private final Minecraft minecraft = Minecraft.getInstance();

    private boolean hasBeenReloaded;

    public <T extends ParticleOptions> void register(ResourceLocation identifier, ParticleProvider<T> particleFactory) {
        this.providers.put(identifier, particleFactory);
    }

    public <T extends ParticleOptions> void register(ResourceLocation identifier, ParticleProvider.Sprite<T> sprite) {
        this.register(identifier, spriteSet -> (particleOptions, clientLevel, x, y, z, xd, yd, zd) -> {
            TextureSheetParticle textureSheetParticle = sprite.createParticle((T) particleOptions, clientLevel, x, y, z, xd, yd, zd);
            if (textureSheetParticle != null) {
                textureSheetParticle.pickSprite(spriteSet);
            }
            return textureSheetParticle;
        });
    }

    public <T extends ParticleOptions> void register(ResourceLocation identifier, ParticleEngine.SpriteParticleRegistration<T> particleMetaFactory) {
        ClientParticleTypesManager.MutableSpriteSet mutableSpriteSet = new ClientParticleTypesManager.MutableSpriteSet();
        this.spriteSets.put(identifier, mutableSpriteSet);
        this.providers.put(identifier, particleMetaFactory.create(mutableSpriteSet));
    }

    @Nullable
    public Particle createParticle(ResourceLocation identifier, ParticleOptions particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Particle particle = this.makeParticle(identifier, particleData, x, y, z, xSpeed, ySpeed, zSpeed);
        if (particle != null) {
            this.minecraft.particleEngine.add(particle);
            return particle;
        } else {
            return null;
        }
    }

    @Nullable
    private <T extends ParticleOptions> Particle makeParticle(ResourceLocation identifier, T particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        if (!this.hasBeenReloaded) {
            ContentRegistrationFlags.throwForFlag(ContentRegistrationFlags.CLIENT_PARTICLE_TYPES);
        }
        ParticleProvider<T> particleProvider = (ParticleProvider<T>) this.providers.get(identifier);
        return particleProvider == null ? null : particleProvider.createParticle(particleData, this.minecraft.level, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
        this.hasBeenReloaded = false;
        CompletableFuture<List<ParticleDefinition>> completablefuture = CompletableFuture.supplyAsync(() -> PARTICLE_LISTER.listMatchingResources(resourceManager), backgroundExecutor).thenCompose(resourceMap -> {
            List<CompletableFuture<ParticleDefinition>> list = new ArrayList(resourceMap.size());
            resourceMap.forEach((p_247903_, p_247904_) -> {
                ResourceLocation resourcelocation = PARTICLE_LISTER.fileToId(p_247903_);
                list.add(CompletableFuture.supplyAsync(() -> {
                    record ParticleDefinition(ResourceLocation id, Optional<List<ResourceLocation>> sprites) {
                    }
                    return new ParticleDefinition(resourcelocation, this.loadParticleDescription(resourcelocation, p_247904_));
                }, backgroundExecutor));
            });
            return Util.sequence(list);
        });
        TextureAtlas textureAtlas = (TextureAtlas) Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES, MissingTextureAtlasSprite.getTexture());
        CompletableFuture<SpriteLoader.Preparations> completablefuture1 = SpriteLoader.create(textureAtlas).loadAndStitch(resourceManager, PARTICLES_ATLAS_INFO, 0, backgroundExecutor).thenCompose(SpriteLoader.Preparations::m_246429_);
        return CompletableFuture.allOf(completablefuture1, completablefuture).thenCompose(preparationBarrier::m_6769_).thenAcceptAsync($ -> {
            reloadProfiler.startTick();
            reloadProfiler.push("upload");
            SpriteLoader.Preparations spriteloader$preparations = (SpriteLoader.Preparations) completablefuture1.join();
            textureAtlas.upload(spriteloader$preparations);
            reloadProfiler.popPush("bindSpriteSets");
            Set<ResourceLocation> set = new HashSet();
            TextureAtlasSprite textureatlassprite = spriteloader$preparations.missing();
            ((List) completablefuture.join()).forEach(particleDefinition -> {
                Optional<List<ResourceLocation>> optional = particleDefinition.sprites();
                if (!optional.isEmpty()) {
                    List<TextureAtlasSprite> list = new ArrayList();
                    for (ResourceLocation resourcelocation : (List) optional.get()) {
                        TextureAtlasSprite textureatlassprite1 = (TextureAtlasSprite) spriteloader$preparations.regions().get(resourcelocation);
                        if (textureatlassprite1 == null) {
                            set.add(resourcelocation);
                            list.add(textureatlassprite);
                        } else {
                            list.add(textureatlassprite1);
                        }
                    }
                    if (list.isEmpty()) {
                        list.add(textureatlassprite);
                    }
                    ((ClientParticleTypesManager.MutableSpriteSet) this.spriteSets.get(particleDefinition.id())).rebind(list);
                }
            });
            if (!set.isEmpty()) {
                PuzzlesLib.LOGGER.warn("Missing particle sprites: {}", set.stream().sorted().map(ResourceLocation::toString).collect(Collectors.joining(",")));
            }
            reloadProfiler.pop();
            reloadProfiler.endTick();
            this.hasBeenReloaded = true;
        }, gameExecutor);
    }

    private Optional<List<ResourceLocation>> loadParticleDescription(ResourceLocation registryName, Resource resource) {
        if (!this.spriteSets.containsKey(registryName)) {
            return Optional.empty();
        } else {
            try {
                Reader reader = resource.openAsReader();
                Optional<List<ResourceLocation>> optional;
                try {
                    ParticleDescription particleDescription = ParticleDescription.fromJson(GsonHelper.parse(reader));
                    optional = Optional.of(particleDescription.getTextures());
                } catch (Throwable var8) {
                    try {
                        reader.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                    throw var8;
                }
                reader.close();
                return optional;
            } catch (IOException var9) {
                throw new IllegalStateException("Failed to load description for particle " + registryName, var9);
            }
        }
    }

    private static class MutableSpriteSet implements SpriteSet {

        private List<TextureAtlasSprite> sprites;

        @Override
        public TextureAtlasSprite get(int age, int lifetime) {
            return (TextureAtlasSprite) this.sprites.get(age * (this.sprites.size() - 1) / lifetime);
        }

        @Override
        public TextureAtlasSprite get(RandomSource random) {
            return (TextureAtlasSprite) this.sprites.get(random.nextInt(this.sprites.size()));
        }

        public void rebind(List<TextureAtlasSprite> sprites) {
            this.sprites = ImmutableList.copyOf(sprites);
        }
    }
}