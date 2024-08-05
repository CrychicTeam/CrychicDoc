package net.minecraft.client.particle;

import com.google.common.collect.EvictingQueue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.SpriteLoader;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.slf4j.Logger;

public class ParticleEngine implements PreparableReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final FileToIdConverter PARTICLE_LISTER = FileToIdConverter.json("particles");

    private static final ResourceLocation PARTICLES_ATLAS_INFO = new ResourceLocation("particles");

    private static final int MAX_PARTICLES_PER_LAYER = 16384;

    private static final List<ParticleRenderType> RENDER_ORDER = ImmutableList.of(ParticleRenderType.TERRAIN_SHEET, ParticleRenderType.PARTICLE_SHEET_OPAQUE, ParticleRenderType.PARTICLE_SHEET_LIT, ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT, ParticleRenderType.CUSTOM);

    protected ClientLevel level;

    private final Map<ParticleRenderType, Queue<Particle>> particles = Maps.newIdentityHashMap();

    private final Queue<TrackingEmitter> trackingEmitters = Queues.newArrayDeque();

    private final TextureManager textureManager;

    private final RandomSource random = RandomSource.create();

    private final Int2ObjectMap<ParticleProvider<?>> providers = new Int2ObjectOpenHashMap();

    private final Queue<Particle> particlesToAdd = Queues.newArrayDeque();

    private final Map<ResourceLocation, ParticleEngine.MutableSpriteSet> spriteSets = Maps.newHashMap();

    private final TextureAtlas textureAtlas;

    private final Object2IntOpenHashMap<ParticleGroup> trackedParticleCounts = new Object2IntOpenHashMap();

    public ParticleEngine(ClientLevel clientLevel0, TextureManager textureManager1) {
        this.textureAtlas = new TextureAtlas(TextureAtlas.LOCATION_PARTICLES);
        textureManager1.register(this.textureAtlas.location(), this.textureAtlas);
        this.level = clientLevel0;
        this.textureManager = textureManager1;
        this.registerProviders();
    }

    private void registerProviders() {
        this.register(ParticleTypes.AMBIENT_ENTITY_EFFECT, SpellParticle.AmbientMobProvider::new);
        this.register(ParticleTypes.ANGRY_VILLAGER, HeartParticle.AngryVillagerProvider::new);
        this.register(ParticleTypes.BLOCK_MARKER, new BlockMarker.Provider());
        this.register(ParticleTypes.BLOCK, new TerrainParticle.Provider());
        this.register(ParticleTypes.BUBBLE, BubbleParticle.Provider::new);
        this.register(ParticleTypes.BUBBLE_COLUMN_UP, BubbleColumnUpParticle.Provider::new);
        this.register(ParticleTypes.BUBBLE_POP, BubblePopParticle.Provider::new);
        this.register(ParticleTypes.CAMPFIRE_COSY_SMOKE, CampfireSmokeParticle.CosyProvider::new);
        this.register(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, CampfireSmokeParticle.SignalProvider::new);
        this.register(ParticleTypes.CLOUD, PlayerCloudParticle.Provider::new);
        this.register(ParticleTypes.COMPOSTER, SuspendedTownParticle.ComposterFillProvider::new);
        this.register(ParticleTypes.CRIT, CritParticle.Provider::new);
        this.register(ParticleTypes.CURRENT_DOWN, WaterCurrentDownParticle.Provider::new);
        this.register(ParticleTypes.DAMAGE_INDICATOR, CritParticle.DamageIndicatorProvider::new);
        this.register(ParticleTypes.DRAGON_BREATH, DragonBreathParticle.Provider::new);
        this.register(ParticleTypes.DOLPHIN, SuspendedTownParticle.DolphinSpeedProvider::new);
        this.register(ParticleTypes.DRIPPING_LAVA, DripParticle::m_272109_);
        this.register(ParticleTypes.FALLING_LAVA, DripParticle::m_272026_);
        this.register(ParticleTypes.LANDING_LAVA, DripParticle::m_271885_);
        this.register(ParticleTypes.DRIPPING_WATER, DripParticle::m_272020_);
        this.register(ParticleTypes.FALLING_WATER, DripParticle::m_271915_);
        this.register(ParticleTypes.DUST, DustParticle.Provider::new);
        this.register(ParticleTypes.DUST_COLOR_TRANSITION, DustColorTransitionParticle.Provider::new);
        this.register(ParticleTypes.EFFECT, SpellParticle.Provider::new);
        this.register(ParticleTypes.ELDER_GUARDIAN, new MobAppearanceParticle.Provider());
        this.register(ParticleTypes.ENCHANTED_HIT, CritParticle.MagicProvider::new);
        this.register(ParticleTypes.ENCHANT, EnchantmentTableParticle.Provider::new);
        this.register(ParticleTypes.END_ROD, EndRodParticle.Provider::new);
        this.register(ParticleTypes.ENTITY_EFFECT, SpellParticle.MobProvider::new);
        this.register(ParticleTypes.EXPLOSION_EMITTER, new HugeExplosionSeedParticle.Provider());
        this.register(ParticleTypes.EXPLOSION, HugeExplosionParticle.Provider::new);
        this.register(ParticleTypes.SONIC_BOOM, SonicBoomParticle.Provider::new);
        this.register(ParticleTypes.FALLING_DUST, FallingDustParticle.Provider::new);
        this.register(ParticleTypes.FIREWORK, FireworkParticles.SparkProvider::new);
        this.register(ParticleTypes.FISHING, WakeParticle.Provider::new);
        this.register(ParticleTypes.FLAME, FlameParticle.Provider::new);
        this.register(ParticleTypes.SCULK_SOUL, SoulParticle.EmissiveProvider::new);
        this.register(ParticleTypes.SCULK_CHARGE, SculkChargeParticle.Provider::new);
        this.register(ParticleTypes.SCULK_CHARGE_POP, SculkChargePopParticle.Provider::new);
        this.register(ParticleTypes.SOUL, SoulParticle.Provider::new);
        this.register(ParticleTypes.SOUL_FIRE_FLAME, FlameParticle.Provider::new);
        this.register(ParticleTypes.FLASH, FireworkParticles.FlashProvider::new);
        this.register(ParticleTypes.HAPPY_VILLAGER, SuspendedTownParticle.HappyVillagerProvider::new);
        this.register(ParticleTypes.HEART, HeartParticle.Provider::new);
        this.register(ParticleTypes.INSTANT_EFFECT, SpellParticle.InstantProvider::new);
        this.register(ParticleTypes.ITEM, new BreakingItemParticle.Provider());
        this.register(ParticleTypes.ITEM_SLIME, new BreakingItemParticle.SlimeProvider());
        this.register(ParticleTypes.ITEM_SNOWBALL, new BreakingItemParticle.SnowballProvider());
        this.register(ParticleTypes.LARGE_SMOKE, LargeSmokeParticle.Provider::new);
        this.register(ParticleTypes.LAVA, LavaParticle.Provider::new);
        this.register(ParticleTypes.MYCELIUM, SuspendedTownParticle.Provider::new);
        this.register(ParticleTypes.NAUTILUS, EnchantmentTableParticle.NautilusProvider::new);
        this.register(ParticleTypes.NOTE, NoteParticle.Provider::new);
        this.register(ParticleTypes.POOF, ExplodeParticle.Provider::new);
        this.register(ParticleTypes.PORTAL, PortalParticle.Provider::new);
        this.register(ParticleTypes.RAIN, WaterDropParticle.Provider::new);
        this.register(ParticleTypes.SMOKE, SmokeParticle.Provider::new);
        this.register(ParticleTypes.SNEEZE, PlayerCloudParticle.SneezeProvider::new);
        this.register(ParticleTypes.SNOWFLAKE, SnowflakeParticle.Provider::new);
        this.register(ParticleTypes.SPIT, SpitParticle.Provider::new);
        this.register(ParticleTypes.SWEEP_ATTACK, AttackSweepParticle.Provider::new);
        this.register(ParticleTypes.TOTEM_OF_UNDYING, TotemParticle.Provider::new);
        this.register(ParticleTypes.SQUID_INK, SquidInkParticle.Provider::new);
        this.register(ParticleTypes.UNDERWATER, SuspendedParticle.UnderwaterProvider::new);
        this.register(ParticleTypes.SPLASH, SplashParticle.Provider::new);
        this.register(ParticleTypes.WITCH, SpellParticle.WitchProvider::new);
        this.register(ParticleTypes.DRIPPING_HONEY, DripParticle::m_272107_);
        this.register(ParticleTypes.FALLING_HONEY, DripParticle::m_272030_);
        this.register(ParticleTypes.LANDING_HONEY, DripParticle::m_271744_);
        this.register(ParticleTypes.FALLING_NECTAR, DripParticle::m_272129_);
        this.register(ParticleTypes.FALLING_SPORE_BLOSSOM, DripParticle::m_272261_);
        this.register(ParticleTypes.SPORE_BLOSSOM_AIR, SuspendedParticle.SporeBlossomAirProvider::new);
        this.register(ParticleTypes.ASH, AshParticle.Provider::new);
        this.register(ParticleTypes.CRIMSON_SPORE, SuspendedParticle.CrimsonSporeProvider::new);
        this.register(ParticleTypes.WARPED_SPORE, SuspendedParticle.WarpedSporeProvider::new);
        this.register(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, DripParticle::m_271935_);
        this.register(ParticleTypes.FALLING_OBSIDIAN_TEAR, DripParticle::m_271941_);
        this.register(ParticleTypes.LANDING_OBSIDIAN_TEAR, DripParticle::m_272251_);
        this.register(ParticleTypes.REVERSE_PORTAL, ReversePortalParticle.ReversePortalProvider::new);
        this.register(ParticleTypes.WHITE_ASH, WhiteAshParticle.Provider::new);
        this.register(ParticleTypes.SMALL_FLAME, FlameParticle.SmallFlameProvider::new);
        this.register(ParticleTypes.DRIPPING_DRIPSTONE_WATER, DripParticle::m_272002_);
        this.register(ParticleTypes.FALLING_DRIPSTONE_WATER, DripParticle::m_271993_);
        this.register(ParticleTypes.CHERRY_LEAVES, p_277215_ -> (p_277217_, p_277218_, p_277219_, p_277220_, p_277221_, p_277222_, p_277223_, p_277224_) -> new CherryParticle(p_277218_, p_277219_, p_277220_, p_277221_, p_277215_));
        this.register(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, DripParticle::m_271789_);
        this.register(ParticleTypes.FALLING_DRIPSTONE_LAVA, DripParticle::m_271760_);
        this.register(ParticleTypes.VIBRATION, VibrationSignalParticle.Provider::new);
        this.register(ParticleTypes.GLOW_SQUID_INK, SquidInkParticle.GlowInkProvider::new);
        this.register(ParticleTypes.GLOW, GlowParticle.GlowSquidProvider::new);
        this.register(ParticleTypes.WAX_ON, GlowParticle.WaxOnProvider::new);
        this.register(ParticleTypes.WAX_OFF, GlowParticle.WaxOffProvider::new);
        this.register(ParticleTypes.ELECTRIC_SPARK, GlowParticle.ElectricSparkProvider::new);
        this.register(ParticleTypes.SCRAPE, GlowParticle.ScrapeProvider::new);
        this.register(ParticleTypes.SHRIEK, ShriekParticle.Provider::new);
        this.register(ParticleTypes.EGG_CRACK, SuspendedTownParticle.EggCrackProvider::new);
    }

    private <T extends ParticleOptions> void register(ParticleType<T> particleTypeT0, ParticleProvider<T> particleProviderT1) {
        this.providers.put(BuiltInRegistries.PARTICLE_TYPE.getId(particleTypeT0), particleProviderT1);
    }

    private <T extends ParticleOptions> void register(ParticleType<T> particleTypeT0, ParticleProvider.Sprite<T> particleProviderSpriteT1) {
        this.register(particleTypeT0, p_272320_ -> (p_272323_, p_272324_, p_272325_, p_272326_, p_272327_, p_272328_, p_272329_, p_272330_) -> {
            TextureSheetParticle $$10 = particleProviderSpriteT1.createParticle((T) p_272323_, p_272324_, p_272325_, p_272326_, p_272327_, p_272328_, p_272329_, p_272330_);
            if ($$10 != null) {
                $$10.pickSprite(p_272320_);
            }
            return $$10;
        });
    }

    private <T extends ParticleOptions> void register(ParticleType<T> particleTypeT0, ParticleEngine.SpriteParticleRegistration<T> particleEngineSpriteParticleRegistrationT1) {
        ParticleEngine.MutableSpriteSet $$2 = new ParticleEngine.MutableSpriteSet();
        this.spriteSets.put(BuiltInRegistries.PARTICLE_TYPE.getKey(particleTypeT0), $$2);
        this.providers.put(BuiltInRegistries.PARTICLE_TYPE.getId(particleTypeT0), particleEngineSpriteParticleRegistrationT1.create($$2));
    }

    @Override
    public CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        CompletableFuture<List<ParticleDefinition>> $$6 = CompletableFuture.supplyAsync(() -> PARTICLE_LISTER.listMatchingResources(resourceManager1), executor4).thenCompose(p_247914_ -> {
            List<CompletableFuture<ParticleDefinition>> $$2 = new ArrayList(p_247914_.size());
            p_247914_.forEach((p_247903_, p_247904_) -> {
                ResourceLocation $$4 = PARTICLE_LISTER.fileToId(p_247903_);
                $$2.add(CompletableFuture.supplyAsync(() -> {
                    record ParticleDefinition(ResourceLocation f_244103_, Optional<List<ResourceLocation>> f_243741_) {

                        private final ResourceLocation id;

                        private final Optional<List<ResourceLocation>> sprites;

                        ParticleDefinition(ResourceLocation f_244103_, Optional<List<ResourceLocation>> f_243741_) {
                            this.id = f_244103_;
                            this.sprites = f_243741_;
                        }
                    }
                    return new ParticleDefinition($$4, this.loadParticleDescription($$4, p_247904_));
                }, executor4));
            });
            return Util.sequence($$2);
        });
        CompletableFuture<SpriteLoader.Preparations> $$7 = SpriteLoader.create(this.textureAtlas).loadAndStitch(resourceManager1, PARTICLES_ATLAS_INFO, 0, executor4).thenCompose(SpriteLoader.Preparations::m_246429_);
        return CompletableFuture.allOf($$7, $$6).thenCompose(preparableReloadListenerPreparationBarrier0::m_6769_).thenAcceptAsync(p_247900_ -> {
            this.clearParticles();
            profilerFiller3.startTick();
            profilerFiller3.push("upload");
            SpriteLoader.Preparations $$4 = (SpriteLoader.Preparations) $$7.join();
            this.textureAtlas.upload($$4);
            profilerFiller3.popPush("bindSpriteSets");
            Set<ResourceLocation> $$5 = new HashSet();
            TextureAtlasSprite $$6x = $$4.missing();
            ((List) $$6.join()).forEach(p_247911_ -> {
                Optional<List<ResourceLocation>> $$4x = p_247911_.sprites();
                if (!$$4x.isEmpty()) {
                    List<TextureAtlasSprite> $$5x = new ArrayList();
                    for (ResourceLocation $$6xx : (List) $$4x.get()) {
                        TextureAtlasSprite $$7x = (TextureAtlasSprite) $$4.regions().get($$6xx);
                        if ($$7x == null) {
                            $$5.add($$6xx);
                            $$5x.add($$6x);
                        } else {
                            $$5x.add($$7x);
                        }
                    }
                    if ($$5x.isEmpty()) {
                        $$5x.add($$6x);
                    }
                    ((ParticleEngine.MutableSpriteSet) this.spriteSets.get(p_247911_.id())).rebind($$5x);
                }
            });
            if (!$$5.isEmpty()) {
                LOGGER.warn("Missing particle sprites: {}", $$5.stream().sorted().map(ResourceLocation::toString).collect(Collectors.joining(",")));
            }
            profilerFiller3.pop();
            profilerFiller3.endTick();
        }, executor5);
    }

    public void close() {
        this.textureAtlas.clearTextureData();
    }

    private Optional<List<ResourceLocation>> loadParticleDescription(ResourceLocation resourceLocation0, Resource resource1) {
        if (!this.spriteSets.containsKey(resourceLocation0)) {
            LOGGER.debug("Redundant texture list for particle: {}", resourceLocation0);
            return Optional.empty();
        } else {
            try {
                Reader $$2 = resource1.openAsReader();
                Optional var5;
                try {
                    ParticleDescription $$3 = ParticleDescription.fromJson(GsonHelper.parse($$2));
                    var5 = Optional.of($$3.getTextures());
                } catch (Throwable var7) {
                    if ($$2 != null) {
                        try {
                            $$2.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }
                    throw var7;
                }
                if ($$2 != null) {
                    $$2.close();
                }
                return var5;
            } catch (IOException var8) {
                throw new IllegalStateException("Failed to load description for particle " + resourceLocation0, var8);
            }
        }
    }

    public void createTrackingEmitter(Entity entity0, ParticleOptions particleOptions1) {
        this.trackingEmitters.add(new TrackingEmitter(this.level, entity0, particleOptions1));
    }

    public void createTrackingEmitter(Entity entity0, ParticleOptions particleOptions1, int int2) {
        this.trackingEmitters.add(new TrackingEmitter(this.level, entity0, particleOptions1, int2));
    }

    @Nullable
    public Particle createParticle(ParticleOptions particleOptions0, double double1, double double2, double double3, double double4, double double5, double double6) {
        Particle $$7 = this.makeParticle(particleOptions0, double1, double2, double3, double4, double5, double6);
        if ($$7 != null) {
            this.add($$7);
            return $$7;
        } else {
            return null;
        }
    }

    @Nullable
    private <T extends ParticleOptions> Particle makeParticle(T t0, double double1, double double2, double double3, double double4, double double5, double double6) {
        ParticleProvider<T> $$7 = (ParticleProvider<T>) this.providers.get(BuiltInRegistries.PARTICLE_TYPE.getId(t0.getType()));
        return $$7 == null ? null : $$7.createParticle(t0, this.level, double1, double2, double3, double4, double5, double6);
    }

    public void add(Particle particle0) {
        Optional<ParticleGroup> $$1 = particle0.getParticleGroup();
        if ($$1.isPresent()) {
            if (this.hasSpaceInParticleLimit((ParticleGroup) $$1.get())) {
                this.particlesToAdd.add(particle0);
                this.updateCount((ParticleGroup) $$1.get(), 1);
            }
        } else {
            this.particlesToAdd.add(particle0);
        }
    }

    public void tick() {
        this.particles.forEach((p_288249_, p_288250_) -> {
            this.level.m_46473_().push(p_288249_.toString());
            this.tickParticleList(p_288250_);
            this.level.m_46473_().pop();
        });
        if (!this.trackingEmitters.isEmpty()) {
            List<TrackingEmitter> $$0 = Lists.newArrayList();
            for (TrackingEmitter $$1 : this.trackingEmitters) {
                $$1.tick();
                if (!$$1.m_107276_()) {
                    $$0.add($$1);
                }
            }
            this.trackingEmitters.removeAll($$0);
        }
        Particle $$2;
        if (!this.particlesToAdd.isEmpty()) {
            while (($$2 = (Particle) this.particlesToAdd.poll()) != null) {
                ((Queue) this.particles.computeIfAbsent($$2.getRenderType(), p_107347_ -> EvictingQueue.create(16384))).add($$2);
            }
        }
    }

    private void tickParticleList(Collection<Particle> collectionParticle0) {
        if (!collectionParticle0.isEmpty()) {
            Iterator<Particle> $$1 = collectionParticle0.iterator();
            while ($$1.hasNext()) {
                Particle $$2 = (Particle) $$1.next();
                this.tickParticle($$2);
                if (!$$2.isAlive()) {
                    $$2.getParticleGroup().ifPresent(p_172289_ -> this.updateCount(p_172289_, -1));
                    $$1.remove();
                }
            }
        }
    }

    private void updateCount(ParticleGroup particleGroup0, int int1) {
        this.trackedParticleCounts.addTo(particleGroup0, int1);
    }

    private void tickParticle(Particle particle0) {
        try {
            particle0.tick();
        } catch (Throwable var5) {
            CrashReport $$2 = CrashReport.forThrowable(var5, "Ticking Particle");
            CrashReportCategory $$3 = $$2.addCategory("Particle being ticked");
            $$3.setDetail("Particle", particle0::toString);
            $$3.setDetail("Particle Type", particle0.getRenderType()::toString);
            throw new ReportedException($$2);
        }
    }

    public void render(PoseStack poseStack0, MultiBufferSource.BufferSource multiBufferSourceBufferSource1, LightTexture lightTexture2, Camera camera3, float float4) {
        lightTexture2.turnOnLightLayer();
        RenderSystem.enableDepthTest();
        PoseStack $$5 = RenderSystem.getModelViewStack();
        $$5.pushPose();
        $$5.mulPoseMatrix(poseStack0.last().pose());
        RenderSystem.applyModelViewMatrix();
        for (ParticleRenderType $$6 : RENDER_ORDER) {
            Iterable<Particle> $$7 = (Iterable<Particle>) this.particles.get($$6);
            if ($$7 != null) {
                RenderSystem.setShader(GameRenderer::m_172829_);
                Tesselator $$8 = Tesselator.getInstance();
                BufferBuilder $$9 = $$8.getBuilder();
                $$6.begin($$9, this.textureManager);
                for (Particle $$10 : $$7) {
                    try {
                        $$10.render($$9, camera3, float4);
                    } catch (Throwable var17) {
                        CrashReport $$12 = CrashReport.forThrowable(var17, "Rendering Particle");
                        CrashReportCategory $$13 = $$12.addCategory("Particle being rendered");
                        $$13.setDetail("Particle", $$10::toString);
                        $$13.setDetail("Particle Type", $$6::toString);
                        throw new ReportedException($$12);
                    }
                }
                $$6.end($$8);
            }
        }
        $$5.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        lightTexture2.turnOffLightLayer();
    }

    public void setLevel(@Nullable ClientLevel clientLevel0) {
        this.level = clientLevel0;
        this.clearParticles();
        this.trackingEmitters.clear();
    }

    public void destroy(BlockPos blockPos0, BlockState blockState1) {
        if (!blockState1.m_60795_() && blockState1.m_245147_()) {
            VoxelShape $$2 = blockState1.m_60808_(this.level, blockPos0);
            double $$3 = 0.25;
            $$2.forAllBoxes((p_172273_, p_172274_, p_172275_, p_172276_, p_172277_, p_172278_) -> {
                double $$8 = Math.min(1.0, p_172276_ - p_172273_);
                double $$9 = Math.min(1.0, p_172277_ - p_172274_);
                double $$10 = Math.min(1.0, p_172278_ - p_172275_);
                int $$11 = Math.max(2, Mth.ceil($$8 / 0.25));
                int $$12 = Math.max(2, Mth.ceil($$9 / 0.25));
                int $$13 = Math.max(2, Mth.ceil($$10 / 0.25));
                for (int $$14 = 0; $$14 < $$11; $$14++) {
                    for (int $$15 = 0; $$15 < $$12; $$15++) {
                        for (int $$16 = 0; $$16 < $$13; $$16++) {
                            double $$17 = ((double) $$14 + 0.5) / (double) $$11;
                            double $$18 = ((double) $$15 + 0.5) / (double) $$12;
                            double $$19 = ((double) $$16 + 0.5) / (double) $$13;
                            double $$20 = $$17 * $$8 + p_172273_;
                            double $$21 = $$18 * $$9 + p_172274_;
                            double $$22 = $$19 * $$10 + p_172275_;
                            this.add(new TerrainParticle(this.level, (double) blockPos0.m_123341_() + $$20, (double) blockPos0.m_123342_() + $$21, (double) blockPos0.m_123343_() + $$22, $$17 - 0.5, $$18 - 0.5, $$19 - 0.5, blockState1, blockPos0));
                        }
                    }
                }
            });
        }
    }

    public void crack(BlockPos blockPos0, Direction direction1) {
        BlockState $$2 = this.level.m_8055_(blockPos0);
        if ($$2.m_60799_() != RenderShape.INVISIBLE) {
            int $$3 = blockPos0.m_123341_();
            int $$4 = blockPos0.m_123342_();
            int $$5 = blockPos0.m_123343_();
            float $$6 = 0.1F;
            AABB $$7 = $$2.m_60808_(this.level, blockPos0).bounds();
            double $$8 = (double) $$3 + this.random.nextDouble() * ($$7.maxX - $$7.minX - 0.2F) + 0.1F + $$7.minX;
            double $$9 = (double) $$4 + this.random.nextDouble() * ($$7.maxY - $$7.minY - 0.2F) + 0.1F + $$7.minY;
            double $$10 = (double) $$5 + this.random.nextDouble() * ($$7.maxZ - $$7.minZ - 0.2F) + 0.1F + $$7.minZ;
            if (direction1 == Direction.DOWN) {
                $$9 = (double) $$4 + $$7.minY - 0.1F;
            }
            if (direction1 == Direction.UP) {
                $$9 = (double) $$4 + $$7.maxY + 0.1F;
            }
            if (direction1 == Direction.NORTH) {
                $$10 = (double) $$5 + $$7.minZ - 0.1F;
            }
            if (direction1 == Direction.SOUTH) {
                $$10 = (double) $$5 + $$7.maxZ + 0.1F;
            }
            if (direction1 == Direction.WEST) {
                $$8 = (double) $$3 + $$7.minX - 0.1F;
            }
            if (direction1 == Direction.EAST) {
                $$8 = (double) $$3 + $$7.maxX + 0.1F;
            }
            this.add(new TerrainParticle(this.level, $$8, $$9, $$10, 0.0, 0.0, 0.0, $$2, blockPos0).m_107268_(0.2F).scale(0.6F));
        }
    }

    public String countParticles() {
        return String.valueOf(this.particles.values().stream().mapToInt(Collection::size).sum());
    }

    private boolean hasSpaceInParticleLimit(ParticleGroup particleGroup0) {
        return this.trackedParticleCounts.getInt(particleGroup0) < particleGroup0.getLimit();
    }

    private void clearParticles() {
        this.particles.clear();
        this.particlesToAdd.clear();
        this.trackingEmitters.clear();
        this.trackedParticleCounts.clear();
    }

    static class MutableSpriteSet implements SpriteSet {

        private List<TextureAtlasSprite> sprites;

        @Override
        public TextureAtlasSprite get(int int0, int int1) {
            return (TextureAtlasSprite) this.sprites.get(int0 * (this.sprites.size() - 1) / int1);
        }

        @Override
        public TextureAtlasSprite get(RandomSource randomSource0) {
            return (TextureAtlasSprite) this.sprites.get(randomSource0.nextInt(this.sprites.size()));
        }

        public void rebind(List<TextureAtlasSprite> listTextureAtlasSprite0) {
            this.sprites = ImmutableList.copyOf(listTextureAtlasSprite0);
        }
    }

    @FunctionalInterface
    interface SpriteParticleRegistration<T extends ParticleOptions> {

        ParticleProvider<T> create(SpriteSet var1);
    }
}