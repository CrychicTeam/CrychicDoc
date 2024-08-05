package net.minecraft.client.renderer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import com.google.gson.JsonSyntaxException;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.logging.LogUtils;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap.Entry;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.CloudStatus;
import net.minecraft.client.GraphicsStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.PrioritizeChunkUpdates;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderRegionCache;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SculkChargeParticleOptions;
import net.minecraft.core.particles.ShriekParticleOption;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.SculkShriekerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector4f;
import org.slf4j.Logger;

public class LevelRenderer implements ResourceManagerReloadListener, AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final int CHUNK_SIZE = 16;

    private static final int HALF_CHUNK_SIZE = 8;

    private static final float SKY_DISC_RADIUS = 512.0F;

    private static final int MINIMUM_ADVANCED_CULLING_DISTANCE = 60;

    private static final double CEILED_SECTION_DIAGONAL = Math.ceil(Math.sqrt(3.0) * 16.0);

    private static final int MIN_FOG_DISTANCE = 32;

    private static final int RAIN_RADIUS = 10;

    private static final int RAIN_DIAMETER = 21;

    private static final int TRANSPARENT_SORT_COUNT = 15;

    private static final int HALF_A_SECOND_IN_MILLIS = 500;

    private static final ResourceLocation MOON_LOCATION = new ResourceLocation("textures/environment/moon_phases.png");

    private static final ResourceLocation SUN_LOCATION = new ResourceLocation("textures/environment/sun.png");

    private static final ResourceLocation CLOUDS_LOCATION = new ResourceLocation("textures/environment/clouds.png");

    private static final ResourceLocation END_SKY_LOCATION = new ResourceLocation("textures/environment/end_sky.png");

    private static final ResourceLocation FORCEFIELD_LOCATION = new ResourceLocation("textures/misc/forcefield.png");

    private static final ResourceLocation RAIN_LOCATION = new ResourceLocation("textures/environment/rain.png");

    private static final ResourceLocation SNOW_LOCATION = new ResourceLocation("textures/environment/snow.png");

    public static final Direction[] DIRECTIONS = Direction.values();

    private final Minecraft minecraft;

    private final EntityRenderDispatcher entityRenderDispatcher;

    private final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    private final RenderBuffers renderBuffers;

    @Nullable
    private ClientLevel level;

    private final BlockingQueue<ChunkRenderDispatcher.RenderChunk> recentlyCompiledChunks = new LinkedBlockingQueue();

    private final AtomicReference<LevelRenderer.RenderChunkStorage> renderChunkStorage = new AtomicReference();

    private final ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum = new ObjectArrayList(10000);

    private final Set<BlockEntity> globalBlockEntities = Sets.newHashSet();

    @Nullable
    private Future<?> lastFullRenderChunkUpdate;

    @Nullable
    private ViewArea viewArea;

    @Nullable
    private VertexBuffer starBuffer;

    @Nullable
    private VertexBuffer skyBuffer;

    @Nullable
    private VertexBuffer darkBuffer;

    private boolean generateClouds = true;

    @Nullable
    private VertexBuffer cloudBuffer;

    private final RunningTrimmedMean frameTimes = new RunningTrimmedMean(100);

    private int ticks;

    private final Int2ObjectMap<BlockDestructionProgress> destroyingBlocks = new Int2ObjectOpenHashMap();

    private final Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress = new Long2ObjectOpenHashMap();

    private final Map<BlockPos, SoundInstance> playingRecords = Maps.newHashMap();

    @Nullable
    private RenderTarget entityTarget;

    @Nullable
    private PostChain entityEffect;

    @Nullable
    private RenderTarget translucentTarget;

    @Nullable
    private RenderTarget itemEntityTarget;

    @Nullable
    private RenderTarget particlesTarget;

    @Nullable
    private RenderTarget weatherTarget;

    @Nullable
    private RenderTarget cloudsTarget;

    @Nullable
    private PostChain transparencyChain;

    private double lastCameraX = Double.MIN_VALUE;

    private double lastCameraY = Double.MIN_VALUE;

    private double lastCameraZ = Double.MIN_VALUE;

    private int lastCameraChunkX = Integer.MIN_VALUE;

    private int lastCameraChunkY = Integer.MIN_VALUE;

    private int lastCameraChunkZ = Integer.MIN_VALUE;

    private double prevCamX = Double.MIN_VALUE;

    private double prevCamY = Double.MIN_VALUE;

    private double prevCamZ = Double.MIN_VALUE;

    private double prevCamRotX = Double.MIN_VALUE;

    private double prevCamRotY = Double.MIN_VALUE;

    private int prevCloudX = Integer.MIN_VALUE;

    private int prevCloudY = Integer.MIN_VALUE;

    private int prevCloudZ = Integer.MIN_VALUE;

    private Vec3 prevCloudColor = Vec3.ZERO;

    @Nullable
    private CloudStatus prevCloudsType;

    @Nullable
    private ChunkRenderDispatcher chunkRenderDispatcher;

    private int lastViewDistance = -1;

    private int renderedEntities;

    private int culledEntities;

    private Frustum cullingFrustum;

    private boolean captureFrustum;

    @Nullable
    private Frustum capturedFrustum;

    private final Vector4f[] frustumPoints = new Vector4f[8];

    private final Vector3d frustumPos = new Vector3d(0.0, 0.0, 0.0);

    private double xTransparentOld;

    private double yTransparentOld;

    private double zTransparentOld;

    private boolean needsFullRenderChunkUpdate = true;

    private final AtomicLong nextFullUpdateMillis = new AtomicLong(0L);

    private final AtomicBoolean needsFrustumUpdate = new AtomicBoolean(false);

    private int rainSoundTime;

    private final float[] rainSizeX = new float[1024];

    private final float[] rainSizeZ = new float[1024];

    public LevelRenderer(Minecraft minecraft0, EntityRenderDispatcher entityRenderDispatcher1, BlockEntityRenderDispatcher blockEntityRenderDispatcher2, RenderBuffers renderBuffers3) {
        this.minecraft = minecraft0;
        this.entityRenderDispatcher = entityRenderDispatcher1;
        this.blockEntityRenderDispatcher = blockEntityRenderDispatcher2;
        this.renderBuffers = renderBuffers3;
        for (int $$4 = 0; $$4 < 32; $$4++) {
            for (int $$5 = 0; $$5 < 32; $$5++) {
                float $$6 = (float) ($$5 - 16);
                float $$7 = (float) ($$4 - 16);
                float $$8 = Mth.sqrt($$6 * $$6 + $$7 * $$7);
                this.rainSizeX[$$4 << 5 | $$5] = -$$7 / $$8;
                this.rainSizeZ[$$4 << 5 | $$5] = $$6 / $$8;
            }
        }
        this.createStars();
        this.createLightSky();
        this.createDarkSky();
    }

    private void renderSnowAndRain(LightTexture lightTexture0, float float1, double double2, double double3, double double4) {
        float $$5 = this.minecraft.level.m_46722_(float1);
        if (!($$5 <= 0.0F)) {
            lightTexture0.turnOnLightLayer();
            Level $$6 = this.minecraft.level;
            int $$7 = Mth.floor(double2);
            int $$8 = Mth.floor(double3);
            int $$9 = Mth.floor(double4);
            Tesselator $$10 = Tesselator.getInstance();
            BufferBuilder $$11 = $$10.getBuilder();
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            int $$12 = 5;
            if (Minecraft.useFancyGraphics()) {
                $$12 = 10;
            }
            RenderSystem.depthMask(Minecraft.useShaderTransparency());
            int $$13 = -1;
            float $$14 = (float) this.ticks + float1;
            RenderSystem.setShader(GameRenderer::m_172829_);
            BlockPos.MutableBlockPos $$15 = new BlockPos.MutableBlockPos();
            for (int $$16 = $$9 - $$12; $$16 <= $$9 + $$12; $$16++) {
                for (int $$17 = $$7 - $$12; $$17 <= $$7 + $$12; $$17++) {
                    int $$18 = ($$16 - $$9 + 16) * 32 + $$17 - $$7 + 16;
                    double $$19 = (double) this.rainSizeX[$$18] * 0.5;
                    double $$20 = (double) this.rainSizeZ[$$18] * 0.5;
                    $$15.set((double) $$17, double3, (double) $$16);
                    Biome $$21 = (Biome) $$6.m_204166_($$15).value();
                    if ($$21.hasPrecipitation()) {
                        int $$22 = $$6.getHeight(Heightmap.Types.MOTION_BLOCKING, $$17, $$16);
                        int $$23 = $$8 - $$12;
                        int $$24 = $$8 + $$12;
                        if ($$23 < $$22) {
                            $$23 = $$22;
                        }
                        if ($$24 < $$22) {
                            $$24 = $$22;
                        }
                        int $$25 = $$22;
                        if ($$22 < $$8) {
                            $$25 = $$8;
                        }
                        if ($$23 != $$24) {
                            RandomSource $$26 = RandomSource.create((long) ($$17 * $$17 * 3121 + $$17 * 45238971 ^ $$16 * $$16 * 418711 + $$16 * 13761));
                            $$15.set($$17, $$23, $$16);
                            Biome.Precipitation $$27 = $$21.getPrecipitationAt($$15);
                            if ($$27 == Biome.Precipitation.RAIN) {
                                if ($$13 != 0) {
                                    if ($$13 >= 0) {
                                        $$10.end();
                                    }
                                    $$13 = 0;
                                    RenderSystem.setShaderTexture(0, RAIN_LOCATION);
                                    $$11.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }
                                int $$28 = this.ticks + $$17 * $$17 * 3121 + $$17 * 45238971 + $$16 * $$16 * 418711 + $$16 * 13761 & 31;
                                float $$29 = -((float) $$28 + float1) / 32.0F * (3.0F + $$26.nextFloat());
                                double $$30 = (double) $$17 + 0.5 - double2;
                                double $$31 = (double) $$16 + 0.5 - double4;
                                float $$32 = (float) Math.sqrt($$30 * $$30 + $$31 * $$31) / (float) $$12;
                                float $$33 = ((1.0F - $$32 * $$32) * 0.5F + 0.5F) * $$5;
                                $$15.set($$17, $$25, $$16);
                                int $$34 = getLightColor($$6, $$15);
                                $$11.m_5483_((double) $$17 - double2 - $$19 + 0.5, (double) $$24 - double3, (double) $$16 - double4 - $$20 + 0.5).uv(0.0F, (float) $$23 * 0.25F + $$29).color(1.0F, 1.0F, 1.0F, $$33).uv2($$34).endVertex();
                                $$11.m_5483_((double) $$17 - double2 + $$19 + 0.5, (double) $$24 - double3, (double) $$16 - double4 + $$20 + 0.5).uv(1.0F, (float) $$23 * 0.25F + $$29).color(1.0F, 1.0F, 1.0F, $$33).uv2($$34).endVertex();
                                $$11.m_5483_((double) $$17 - double2 + $$19 + 0.5, (double) $$23 - double3, (double) $$16 - double4 + $$20 + 0.5).uv(1.0F, (float) $$24 * 0.25F + $$29).color(1.0F, 1.0F, 1.0F, $$33).uv2($$34).endVertex();
                                $$11.m_5483_((double) $$17 - double2 - $$19 + 0.5, (double) $$23 - double3, (double) $$16 - double4 - $$20 + 0.5).uv(0.0F, (float) $$24 * 0.25F + $$29).color(1.0F, 1.0F, 1.0F, $$33).uv2($$34).endVertex();
                            } else if ($$27 == Biome.Precipitation.SNOW) {
                                if ($$13 != 1) {
                                    if ($$13 >= 0) {
                                        $$10.end();
                                    }
                                    $$13 = 1;
                                    RenderSystem.setShaderTexture(0, SNOW_LOCATION);
                                    $$11.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
                                }
                                float $$35 = -((float) (this.ticks & 511) + float1) / 512.0F;
                                float $$36 = (float) ($$26.nextDouble() + (double) $$14 * 0.01 * (double) ((float) $$26.nextGaussian()));
                                float $$37 = (float) ($$26.nextDouble() + (double) ($$14 * (float) $$26.nextGaussian()) * 0.001);
                                double $$38 = (double) $$17 + 0.5 - double2;
                                double $$39 = (double) $$16 + 0.5 - double4;
                                float $$40 = (float) Math.sqrt($$38 * $$38 + $$39 * $$39) / (float) $$12;
                                float $$41 = ((1.0F - $$40 * $$40) * 0.3F + 0.5F) * $$5;
                                $$15.set($$17, $$25, $$16);
                                int $$42 = getLightColor($$6, $$15);
                                int $$43 = $$42 >> 16 & 65535;
                                int $$44 = $$42 & 65535;
                                int $$45 = ($$43 * 3 + 240) / 4;
                                int $$46 = ($$44 * 3 + 240) / 4;
                                $$11.m_5483_((double) $$17 - double2 - $$19 + 0.5, (double) $$24 - double3, (double) $$16 - double4 - $$20 + 0.5).uv(0.0F + $$36, (float) $$23 * 0.25F + $$35 + $$37).color(1.0F, 1.0F, 1.0F, $$41).uv2($$46, $$45).endVertex();
                                $$11.m_5483_((double) $$17 - double2 + $$19 + 0.5, (double) $$24 - double3, (double) $$16 - double4 + $$20 + 0.5).uv(1.0F + $$36, (float) $$23 * 0.25F + $$35 + $$37).color(1.0F, 1.0F, 1.0F, $$41).uv2($$46, $$45).endVertex();
                                $$11.m_5483_((double) $$17 - double2 + $$19 + 0.5, (double) $$23 - double3, (double) $$16 - double4 + $$20 + 0.5).uv(1.0F + $$36, (float) $$24 * 0.25F + $$35 + $$37).color(1.0F, 1.0F, 1.0F, $$41).uv2($$46, $$45).endVertex();
                                $$11.m_5483_((double) $$17 - double2 - $$19 + 0.5, (double) $$23 - double3, (double) $$16 - double4 - $$20 + 0.5).uv(0.0F + $$36, (float) $$24 * 0.25F + $$35 + $$37).color(1.0F, 1.0F, 1.0F, $$41).uv2($$46, $$45).endVertex();
                            }
                        }
                    }
                }
            }
            if ($$13 >= 0) {
                $$10.end();
            }
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            lightTexture0.turnOffLightLayer();
        }
    }

    public void tickRain(Camera camera0) {
        float $$1 = this.minecraft.level.m_46722_(1.0F) / (Minecraft.useFancyGraphics() ? 1.0F : 2.0F);
        if (!($$1 <= 0.0F)) {
            RandomSource $$2 = RandomSource.create((long) this.ticks * 312987231L);
            LevelReader $$3 = this.minecraft.level;
            BlockPos $$4 = BlockPos.containing(camera0.getPosition());
            BlockPos $$5 = null;
            int $$6 = (int) (100.0F * $$1 * $$1) / (this.minecraft.options.particles().get() == ParticleStatus.DECREASED ? 2 : 1);
            for (int $$7 = 0; $$7 < $$6; $$7++) {
                int $$8 = $$2.nextInt(21) - 10;
                int $$9 = $$2.nextInt(21) - 10;
                BlockPos $$10 = $$3.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, $$4.offset($$8, 0, $$9));
                if ($$10.m_123342_() > $$3.getMinBuildHeight() && $$10.m_123342_() <= $$4.m_123342_() + 10 && $$10.m_123342_() >= $$4.m_123342_() - 10) {
                    Biome $$11 = $$3.getBiome($$10).value();
                    if ($$11.getPrecipitationAt($$10) == Biome.Precipitation.RAIN) {
                        $$5 = $$10.below();
                        if (this.minecraft.options.particles().get() == ParticleStatus.MINIMAL) {
                            break;
                        }
                        double $$12 = $$2.nextDouble();
                        double $$13 = $$2.nextDouble();
                        BlockState $$14 = $$3.m_8055_($$5);
                        FluidState $$15 = $$3.m_6425_($$5);
                        VoxelShape $$16 = $$14.m_60812_($$3, $$5);
                        double $$17 = $$16.max(Direction.Axis.Y, $$12, $$13);
                        double $$18 = (double) $$15.getHeight($$3, $$5);
                        double $$19 = Math.max($$17, $$18);
                        ParticleOptions $$20 = !$$15.is(FluidTags.LAVA) && !$$14.m_60713_(Blocks.MAGMA_BLOCK) && !CampfireBlock.isLitCampfire($$14) ? ParticleTypes.RAIN : ParticleTypes.SMOKE;
                        this.minecraft.level.addParticle($$20, (double) $$5.m_123341_() + $$12, (double) $$5.m_123342_() + $$19, (double) $$5.m_123343_() + $$13, 0.0, 0.0, 0.0);
                    }
                }
            }
            if ($$5 != null && $$2.nextInt(3) < this.rainSoundTime++) {
                this.rainSoundTime = 0;
                if ($$5.m_123342_() > $$4.m_123342_() + 1 && $$3.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING, $$4).m_123342_() > Mth.floor((float) $$4.m_123342_())) {
                    this.minecraft.level.m_245747_($$5, SoundEvents.WEATHER_RAIN_ABOVE, SoundSource.WEATHER, 0.1F, 0.5F, false);
                } else {
                    this.minecraft.level.m_245747_($$5, SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.2F, 1.0F, false);
                }
            }
        }
    }

    public void close() {
        if (this.entityEffect != null) {
            this.entityEffect.close();
        }
        if (this.transparencyChain != null) {
            this.transparencyChain.close();
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager0) {
        this.initOutline();
        if (Minecraft.useShaderTransparency()) {
            this.initTransparency();
        }
    }

    public void initOutline() {
        if (this.entityEffect != null) {
            this.entityEffect.close();
        }
        ResourceLocation $$0 = new ResourceLocation("shaders/post/entity_outline.json");
        try {
            this.entityEffect = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), $$0);
            this.entityEffect.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            this.entityTarget = this.entityEffect.getTempTarget("final");
        } catch (IOException var3) {
            LOGGER.warn("Failed to load shader: {}", $$0, var3);
            this.entityEffect = null;
            this.entityTarget = null;
        } catch (JsonSyntaxException var4) {
            LOGGER.warn("Failed to parse shader: {}", $$0, var4);
            this.entityEffect = null;
            this.entityTarget = null;
        }
    }

    private void initTransparency() {
        this.deinitTransparency();
        ResourceLocation $$0 = new ResourceLocation("shaders/post/transparency.json");
        try {
            PostChain $$1 = new PostChain(this.minecraft.getTextureManager(), this.minecraft.getResourceManager(), this.minecraft.getMainRenderTarget(), $$0);
            $$1.resize(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
            RenderTarget $$2 = $$1.getTempTarget("translucent");
            RenderTarget $$3 = $$1.getTempTarget("itemEntity");
            RenderTarget $$4 = $$1.getTempTarget("particles");
            RenderTarget $$5 = $$1.getTempTarget("weather");
            RenderTarget $$6 = $$1.getTempTarget("clouds");
            this.transparencyChain = $$1;
            this.translucentTarget = $$2;
            this.itemEntityTarget = $$3;
            this.particlesTarget = $$4;
            this.weatherTarget = $$5;
            this.cloudsTarget = $$6;
        } catch (Exception var8) {
            String $$8 = var8 instanceof JsonSyntaxException ? "parse" : "load";
            String $$9 = "Failed to " + $$8 + " shader: " + $$0;
            LevelRenderer.TransparencyShaderException $$10 = new LevelRenderer.TransparencyShaderException($$9, var8);
            if (this.minecraft.getResourcePackRepository().getSelectedIds().size() > 1) {
                Component $$11 = (Component) this.minecraft.getResourceManager().listPacks().findFirst().map(p_234256_ -> Component.literal(p_234256_.packId())).orElse(null);
                this.minecraft.options.graphicsMode().set(GraphicsStatus.FANCY);
                this.minecraft.clearResourcePacksOnError($$10, $$11);
            } else {
                CrashReport $$12 = this.minecraft.fillReport(new CrashReport($$9, $$10));
                this.minecraft.options.graphicsMode().set(GraphicsStatus.FANCY);
                this.minecraft.options.save();
                LOGGER.error(LogUtils.FATAL_MARKER, $$9, $$10);
                this.minecraft.emergencySave();
                Minecraft.crash($$12);
            }
        }
    }

    private void deinitTransparency() {
        if (this.transparencyChain != null) {
            this.transparencyChain.close();
            this.translucentTarget.destroyBuffers();
            this.itemEntityTarget.destroyBuffers();
            this.particlesTarget.destroyBuffers();
            this.weatherTarget.destroyBuffers();
            this.cloudsTarget.destroyBuffers();
            this.transparencyChain = null;
            this.translucentTarget = null;
            this.itemEntityTarget = null;
            this.particlesTarget = null;
            this.weatherTarget = null;
            this.cloudsTarget = null;
        }
    }

    public void doEntityOutline() {
        if (this.shouldShowEntityOutlines()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            this.entityTarget.blitToScreen(this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight(), false);
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }

    protected boolean shouldShowEntityOutlines() {
        return !this.minecraft.gameRenderer.isPanoramicMode() && this.entityTarget != null && this.entityEffect != null && this.minecraft.player != null;
    }

    private void createDarkSky() {
        Tesselator $$0 = Tesselator.getInstance();
        BufferBuilder $$1 = $$0.getBuilder();
        if (this.darkBuffer != null) {
            this.darkBuffer.close();
        }
        this.darkBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer $$2 = buildSkyDisc($$1, -16.0F);
        this.darkBuffer.bind();
        this.darkBuffer.upload($$2);
        VertexBuffer.unbind();
    }

    private void createLightSky() {
        Tesselator $$0 = Tesselator.getInstance();
        BufferBuilder $$1 = $$0.getBuilder();
        if (this.skyBuffer != null) {
            this.skyBuffer.close();
        }
        this.skyBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer $$2 = buildSkyDisc($$1, 16.0F);
        this.skyBuffer.bind();
        this.skyBuffer.upload($$2);
        VertexBuffer.unbind();
    }

    private static BufferBuilder.RenderedBuffer buildSkyDisc(BufferBuilder bufferBuilder0, float float1) {
        float $$2 = Math.signum(float1) * 512.0F;
        float $$3 = 512.0F;
        RenderSystem.setShader(GameRenderer::m_172808_);
        bufferBuilder0.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION);
        bufferBuilder0.m_5483_(0.0, (double) float1, 0.0).endVertex();
        for (int $$4 = -180; $$4 <= 180; $$4 += 45) {
            bufferBuilder0.m_5483_((double) ($$2 * Mth.cos((float) $$4 * (float) (Math.PI / 180.0))), (double) float1, (double) (512.0F * Mth.sin((float) $$4 * (float) (Math.PI / 180.0)))).endVertex();
        }
        return bufferBuilder0.end();
    }

    private void createStars() {
        Tesselator $$0 = Tesselator.getInstance();
        BufferBuilder $$1 = $$0.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172808_);
        if (this.starBuffer != null) {
            this.starBuffer.close();
        }
        this.starBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        BufferBuilder.RenderedBuffer $$2 = this.drawStars($$1);
        this.starBuffer.bind();
        this.starBuffer.upload($$2);
        VertexBuffer.unbind();
    }

    private BufferBuilder.RenderedBuffer drawStars(BufferBuilder bufferBuilder0) {
        RandomSource $$1 = RandomSource.create(10842L);
        bufferBuilder0.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);
        for (int $$2 = 0; $$2 < 1500; $$2++) {
            double $$3 = (double) ($$1.nextFloat() * 2.0F - 1.0F);
            double $$4 = (double) ($$1.nextFloat() * 2.0F - 1.0F);
            double $$5 = (double) ($$1.nextFloat() * 2.0F - 1.0F);
            double $$6 = (double) (0.15F + $$1.nextFloat() * 0.1F);
            double $$7 = $$3 * $$3 + $$4 * $$4 + $$5 * $$5;
            if ($$7 < 1.0 && $$7 > 0.01) {
                $$7 = 1.0 / Math.sqrt($$7);
                $$3 *= $$7;
                $$4 *= $$7;
                $$5 *= $$7;
                double $$8 = $$3 * 100.0;
                double $$9 = $$4 * 100.0;
                double $$10 = $$5 * 100.0;
                double $$11 = Math.atan2($$3, $$5);
                double $$12 = Math.sin($$11);
                double $$13 = Math.cos($$11);
                double $$14 = Math.atan2(Math.sqrt($$3 * $$3 + $$5 * $$5), $$4);
                double $$15 = Math.sin($$14);
                double $$16 = Math.cos($$14);
                double $$17 = $$1.nextDouble() * Math.PI * 2.0;
                double $$18 = Math.sin($$17);
                double $$19 = Math.cos($$17);
                for (int $$20 = 0; $$20 < 4; $$20++) {
                    double $$21 = 0.0;
                    double $$22 = (double) (($$20 & 2) - 1) * $$6;
                    double $$23 = (double) (($$20 + 1 & 2) - 1) * $$6;
                    double $$24 = 0.0;
                    double $$25 = $$22 * $$19 - $$23 * $$18;
                    double $$26 = $$23 * $$19 + $$22 * $$18;
                    double $$28 = $$25 * $$15 + 0.0 * $$16;
                    double $$29 = 0.0 * $$15 - $$25 * $$16;
                    double $$30 = $$29 * $$12 - $$26 * $$13;
                    double $$32 = $$26 * $$12 + $$29 * $$13;
                    bufferBuilder0.m_5483_($$8 + $$30, $$9 + $$28, $$10 + $$32).endVertex();
                }
            }
        }
        return bufferBuilder0.end();
    }

    public void setLevel(@Nullable ClientLevel clientLevel0) {
        this.lastCameraX = Double.MIN_VALUE;
        this.lastCameraY = Double.MIN_VALUE;
        this.lastCameraZ = Double.MIN_VALUE;
        this.lastCameraChunkX = Integer.MIN_VALUE;
        this.lastCameraChunkY = Integer.MIN_VALUE;
        this.lastCameraChunkZ = Integer.MIN_VALUE;
        this.entityRenderDispatcher.setLevel(clientLevel0);
        this.level = clientLevel0;
        if (clientLevel0 != null) {
            this.allChanged();
        } else {
            if (this.viewArea != null) {
                this.viewArea.releaseAllBuffers();
                this.viewArea = null;
            }
            if (this.chunkRenderDispatcher != null) {
                this.chunkRenderDispatcher.dispose();
            }
            this.chunkRenderDispatcher = null;
            this.globalBlockEntities.clear();
            this.renderChunkStorage.set(null);
            this.renderChunksInFrustum.clear();
        }
    }

    public void graphicsChanged() {
        if (Minecraft.useShaderTransparency()) {
            this.initTransparency();
        } else {
            this.deinitTransparency();
        }
    }

    public void allChanged() {
        if (this.level != null) {
            this.graphicsChanged();
            this.level.clearTintCaches();
            if (this.chunkRenderDispatcher == null) {
                this.chunkRenderDispatcher = new ChunkRenderDispatcher(this.level, this, Util.backgroundExecutor(), this.minecraft.is64Bit(), this.renderBuffers.fixedBufferPack());
            } else {
                this.chunkRenderDispatcher.setLevel(this.level);
            }
            this.needsFullRenderChunkUpdate = true;
            this.generateClouds = true;
            this.recentlyCompiledChunks.clear();
            ItemBlockRenderTypes.setFancy(Minecraft.useFancyGraphics());
            this.lastViewDistance = this.minecraft.options.getEffectiveRenderDistance();
            if (this.viewArea != null) {
                this.viewArea.releaseAllBuffers();
            }
            this.chunkRenderDispatcher.blockUntilClear();
            synchronized (this.globalBlockEntities) {
                this.globalBlockEntities.clear();
            }
            this.viewArea = new ViewArea(this.chunkRenderDispatcher, this.level, this.minecraft.options.getEffectiveRenderDistance(), this);
            if (this.lastFullRenderChunkUpdate != null) {
                try {
                    this.lastFullRenderChunkUpdate.get();
                    this.lastFullRenderChunkUpdate = null;
                } catch (Exception var3) {
                    LOGGER.warn("Full update failed", var3);
                }
            }
            this.renderChunkStorage.set(new LevelRenderer.RenderChunkStorage(this.viewArea.chunks.length));
            this.renderChunksInFrustum.clear();
            Entity $$1 = this.minecraft.getCameraEntity();
            if ($$1 != null) {
                this.viewArea.repositionCamera($$1.getX(), $$1.getZ());
            }
        }
    }

    public void resize(int int0, int int1) {
        this.needsUpdate();
        if (this.entityEffect != null) {
            this.entityEffect.resize(int0, int1);
        }
        if (this.transparencyChain != null) {
            this.transparencyChain.resize(int0, int1);
        }
    }

    public String getChunkStatistics() {
        int $$0 = this.viewArea.chunks.length;
        int $$1 = this.countRenderedChunks();
        return String.format(Locale.ROOT, "C: %d/%d %sD: %d, %s", $$1, $$0, this.minecraft.smartCull ? "(s) " : "", this.lastViewDistance, this.chunkRenderDispatcher == null ? "null" : this.chunkRenderDispatcher.getStats());
    }

    public ChunkRenderDispatcher getChunkRenderDispatcher() {
        return this.chunkRenderDispatcher;
    }

    public double getTotalChunks() {
        return (double) this.viewArea.chunks.length;
    }

    public double getLastViewDistance() {
        return (double) this.lastViewDistance;
    }

    public int countRenderedChunks() {
        int $$0 = 0;
        ObjectListIterator var2 = this.renderChunksInFrustum.iterator();
        while (var2.hasNext()) {
            LevelRenderer.RenderChunkInfo $$1 = (LevelRenderer.RenderChunkInfo) var2.next();
            if (!$$1.chunk.getCompiledChunk().hasNoRenderableLayers()) {
                $$0++;
            }
        }
        return $$0;
    }

    public String getEntityStatistics() {
        return "E: " + this.renderedEntities + "/" + this.level.getEntityCount() + ", B: " + this.culledEntities + ", SD: " + this.level.getServerSimulationDistance();
    }

    private void setupRender(Camera camera0, Frustum frustum1, boolean boolean2, boolean boolean3) {
        Vec3 $$4 = camera0.getPosition();
        if (this.minecraft.options.getEffectiveRenderDistance() != this.lastViewDistance) {
            this.allChanged();
        }
        this.level.m_46473_().push("camera");
        double $$5 = this.minecraft.player.m_20185_();
        double $$6 = this.minecraft.player.m_20186_();
        double $$7 = this.minecraft.player.m_20189_();
        int $$8 = SectionPos.posToSectionCoord($$5);
        int $$9 = SectionPos.posToSectionCoord($$6);
        int $$10 = SectionPos.posToSectionCoord($$7);
        if (this.lastCameraChunkX != $$8 || this.lastCameraChunkY != $$9 || this.lastCameraChunkZ != $$10) {
            this.lastCameraX = $$5;
            this.lastCameraY = $$6;
            this.lastCameraZ = $$7;
            this.lastCameraChunkX = $$8;
            this.lastCameraChunkY = $$9;
            this.lastCameraChunkZ = $$10;
            this.viewArea.repositionCamera($$5, $$7);
        }
        this.chunkRenderDispatcher.setCamera($$4);
        this.level.m_46473_().popPush("cull");
        this.minecraft.getProfiler().popPush("culling");
        BlockPos $$11 = camera0.getBlockPosition();
        double $$12 = Math.floor($$4.x / 8.0);
        double $$13 = Math.floor($$4.y / 8.0);
        double $$14 = Math.floor($$4.z / 8.0);
        this.needsFullRenderChunkUpdate = this.needsFullRenderChunkUpdate || $$12 != this.prevCamX || $$13 != this.prevCamY || $$14 != this.prevCamZ;
        this.nextFullUpdateMillis.updateAndGet(p_234309_ -> {
            if (p_234309_ > 0L && System.currentTimeMillis() > p_234309_) {
                this.needsFullRenderChunkUpdate = true;
                return 0L;
            } else {
                return p_234309_;
            }
        });
        this.prevCamX = $$12;
        this.prevCamY = $$13;
        this.prevCamZ = $$14;
        this.minecraft.getProfiler().popPush("update");
        boolean $$15 = this.minecraft.smartCull;
        if (boolean3 && this.level.m_8055_($$11).m_60804_(this.level, $$11)) {
            $$15 = false;
        }
        if (!boolean2) {
            if (this.needsFullRenderChunkUpdate && (this.lastFullRenderChunkUpdate == null || this.lastFullRenderChunkUpdate.isDone())) {
                this.minecraft.getProfiler().push("full_update_schedule");
                this.needsFullRenderChunkUpdate = false;
                boolean $$16 = $$15;
                this.lastFullRenderChunkUpdate = Util.backgroundExecutor().submit(() -> {
                    Queue<LevelRenderer.RenderChunkInfo> $$3 = Queues.newArrayDeque();
                    this.initializeQueueForFullUpdate(camera0, $$3);
                    LevelRenderer.RenderChunkStorage $$4x = new LevelRenderer.RenderChunkStorage(this.viewArea.chunks.length);
                    this.updateRenderChunks($$4x.renderChunks, $$4x.renderInfoMap, $$4, $$3, $$16);
                    this.renderChunkStorage.set($$4x);
                    this.needsFrustumUpdate.set(true);
                });
                this.minecraft.getProfiler().pop();
            }
            LevelRenderer.RenderChunkStorage $$17 = (LevelRenderer.RenderChunkStorage) this.renderChunkStorage.get();
            if (!this.recentlyCompiledChunks.isEmpty()) {
                this.minecraft.getProfiler().push("partial_update");
                Queue<LevelRenderer.RenderChunkInfo> $$18 = Queues.newArrayDeque();
                while (!this.recentlyCompiledChunks.isEmpty()) {
                    ChunkRenderDispatcher.RenderChunk $$19 = (ChunkRenderDispatcher.RenderChunk) this.recentlyCompiledChunks.poll();
                    LevelRenderer.RenderChunkInfo $$20 = $$17.renderInfoMap.get($$19);
                    if ($$20 != null && $$20.chunk == $$19) {
                        $$18.add($$20);
                    }
                }
                this.updateRenderChunks($$17.renderChunks, $$17.renderInfoMap, $$4, $$18, $$15);
                this.needsFrustumUpdate.set(true);
                this.minecraft.getProfiler().pop();
            }
            double $$21 = Math.floor((double) (camera0.getXRot() / 2.0F));
            double $$22 = Math.floor((double) (camera0.getYRot() / 2.0F));
            if (this.needsFrustumUpdate.compareAndSet(true, false) || $$21 != this.prevCamRotX || $$22 != this.prevCamRotY) {
                this.applyFrustum(new Frustum(frustum1).offsetToFullyIncludeCameraCube(8));
                this.prevCamRotX = $$21;
                this.prevCamRotY = $$22;
            }
        }
        this.minecraft.getProfiler().pop();
    }

    private void applyFrustum(Frustum frustum0) {
        if (!Minecraft.getInstance().m_18695_()) {
            throw new IllegalStateException("applyFrustum called from wrong thread: " + Thread.currentThread().getName());
        } else {
            this.minecraft.getProfiler().push("apply_frustum");
            this.renderChunksInFrustum.clear();
            for (LevelRenderer.RenderChunkInfo $$1 : ((LevelRenderer.RenderChunkStorage) this.renderChunkStorage.get()).renderChunks) {
                if (frustum0.isVisible($$1.chunk.getBoundingBox())) {
                    this.renderChunksInFrustum.add($$1);
                }
            }
            this.minecraft.getProfiler().pop();
        }
    }

    private void initializeQueueForFullUpdate(Camera camera0, Queue<LevelRenderer.RenderChunkInfo> queueLevelRendererRenderChunkInfo1) {
        int $$2 = 16;
        Vec3 $$3 = camera0.getPosition();
        BlockPos $$4 = camera0.getBlockPosition();
        ChunkRenderDispatcher.RenderChunk $$5 = this.viewArea.getRenderChunkAt($$4);
        if ($$5 == null) {
            boolean $$6 = $$4.m_123342_() > this.level.m_141937_();
            int $$7 = $$6 ? this.level.m_151558_() - 8 : this.level.m_141937_() + 8;
            int $$8 = Mth.floor($$3.x / 16.0) * 16;
            int $$9 = Mth.floor($$3.z / 16.0) * 16;
            List<LevelRenderer.RenderChunkInfo> $$10 = Lists.newArrayList();
            for (int $$11 = -this.lastViewDistance; $$11 <= this.lastViewDistance; $$11++) {
                for (int $$12 = -this.lastViewDistance; $$12 <= this.lastViewDistance; $$12++) {
                    ChunkRenderDispatcher.RenderChunk $$13 = this.viewArea.getRenderChunkAt(new BlockPos($$8 + SectionPos.sectionToBlockCoord($$11, 8), $$7, $$9 + SectionPos.sectionToBlockCoord($$12, 8)));
                    if ($$13 != null) {
                        $$10.add(new LevelRenderer.RenderChunkInfo($$13, null, 0));
                    }
                }
            }
            $$10.sort(Comparator.comparingDouble(p_234303_ -> $$4.m_123331_(p_234303_.chunk.getOrigin().offset(8, 8, 8))));
            queueLevelRendererRenderChunkInfo1.addAll($$10);
        } else {
            queueLevelRendererRenderChunkInfo1.add(new LevelRenderer.RenderChunkInfo($$5, null, 0));
        }
    }

    public void addRecentlyCompiledChunk(ChunkRenderDispatcher.RenderChunk chunkRenderDispatcherRenderChunk0) {
        this.recentlyCompiledChunks.add(chunkRenderDispatcherRenderChunk0);
    }

    private void updateRenderChunks(LinkedHashSet<LevelRenderer.RenderChunkInfo> linkedHashSetLevelRendererRenderChunkInfo0, LevelRenderer.RenderInfoMap levelRendererRenderInfoMap1, Vec3 vec2, Queue<LevelRenderer.RenderChunkInfo> queueLevelRendererRenderChunkInfo3, boolean boolean4) {
        int $$5 = 16;
        BlockPos $$6 = new BlockPos(Mth.floor(vec2.x / 16.0) * 16, Mth.floor(vec2.y / 16.0) * 16, Mth.floor(vec2.z / 16.0) * 16);
        BlockPos $$7 = $$6.offset(8, 8, 8);
        Entity.setViewScale(Mth.clamp((double) this.minecraft.options.getEffectiveRenderDistance() / 8.0, 1.0, 2.5) * this.minecraft.options.entityDistanceScaling().get());
        while (!queueLevelRendererRenderChunkInfo3.isEmpty()) {
            LevelRenderer.RenderChunkInfo $$8 = (LevelRenderer.RenderChunkInfo) queueLevelRendererRenderChunkInfo3.poll();
            ChunkRenderDispatcher.RenderChunk $$9 = $$8.chunk;
            linkedHashSetLevelRendererRenderChunkInfo0.add($$8);
            boolean $$10 = Math.abs($$9.getOrigin().m_123341_() - $$6.m_123341_()) > 60 || Math.abs($$9.getOrigin().m_123342_() - $$6.m_123342_()) > 60 || Math.abs($$9.getOrigin().m_123343_() - $$6.m_123343_()) > 60;
            for (Direction $$11 : DIRECTIONS) {
                ChunkRenderDispatcher.RenderChunk $$12 = this.getRelativeFrom($$6, $$9, $$11);
                if ($$12 != null && (!boolean4 || !$$8.hasDirection($$11.getOpposite()))) {
                    if (boolean4 && $$8.hasSourceDirections()) {
                        ChunkRenderDispatcher.CompiledChunk $$13 = $$9.getCompiledChunk();
                        boolean $$14 = false;
                        for (int $$15 = 0; $$15 < DIRECTIONS.length; $$15++) {
                            if ($$8.hasSourceDirection($$15) && $$13.facesCanSeeEachother(DIRECTIONS[$$15].getOpposite(), $$11)) {
                                $$14 = true;
                                break;
                            }
                        }
                        if (!$$14) {
                            continue;
                        }
                    }
                    if (boolean4 && $$10) {
                        BlockPos $$16 = $$12.getOrigin();
                        BlockPos $$17 = $$16.offset(($$11.getAxis() == Direction.Axis.X ? $$7.m_123341_() <= $$16.m_123341_() : $$7.m_123341_() >= $$16.m_123341_()) ? 0 : 16, ($$11.getAxis() == Direction.Axis.Y ? $$7.m_123342_() <= $$16.m_123342_() : $$7.m_123342_() >= $$16.m_123342_()) ? 0 : 16, ($$11.getAxis() == Direction.Axis.Z ? $$7.m_123343_() <= $$16.m_123343_() : $$7.m_123343_() >= $$16.m_123343_()) ? 0 : 16);
                        Vec3 $$18 = new Vec3((double) $$17.m_123341_(), (double) $$17.m_123342_(), (double) $$17.m_123343_());
                        Vec3 $$19 = vec2.subtract($$18).normalize().scale(CEILED_SECTION_DIAGONAL);
                        boolean $$20 = true;
                        while (vec2.subtract($$18).lengthSqr() > 3600.0) {
                            $$18 = $$18.add($$19);
                            if ($$18.y > (double) this.level.m_151558_() || $$18.y < (double) this.level.m_141937_()) {
                                break;
                            }
                            ChunkRenderDispatcher.RenderChunk $$21 = this.viewArea.getRenderChunkAt(BlockPos.containing($$18.x, $$18.y, $$18.z));
                            if ($$21 == null || levelRendererRenderInfoMap1.get($$21) == null) {
                                $$20 = false;
                                break;
                            }
                        }
                        if (!$$20) {
                            continue;
                        }
                    }
                    LevelRenderer.RenderChunkInfo $$22 = levelRendererRenderInfoMap1.get($$12);
                    if ($$22 != null) {
                        $$22.addSourceDirection($$11);
                    } else if (!$$12.hasAllNeighbors()) {
                        if (!this.closeToBorder($$6, $$9)) {
                            this.nextFullUpdateMillis.set(System.currentTimeMillis() + 500L);
                        }
                    } else {
                        LevelRenderer.RenderChunkInfo $$23 = new LevelRenderer.RenderChunkInfo($$12, $$11, $$8.step + 1);
                        $$23.setDirections($$8.directions, $$11);
                        queueLevelRendererRenderChunkInfo3.add($$23);
                        levelRendererRenderInfoMap1.put($$12, $$23);
                    }
                }
            }
        }
    }

    @Nullable
    private ChunkRenderDispatcher.RenderChunk getRelativeFrom(BlockPos blockPos0, ChunkRenderDispatcher.RenderChunk chunkRenderDispatcherRenderChunk1, Direction direction2) {
        BlockPos $$3 = chunkRenderDispatcherRenderChunk1.getRelativeOrigin(direction2);
        if (Mth.abs(blockPos0.m_123341_() - $$3.m_123341_()) > this.lastViewDistance * 16) {
            return null;
        } else if (Mth.abs(blockPos0.m_123342_() - $$3.m_123342_()) > this.lastViewDistance * 16 || $$3.m_123342_() < this.level.m_141937_() || $$3.m_123342_() >= this.level.m_151558_()) {
            return null;
        } else {
            return Mth.abs(blockPos0.m_123343_() - $$3.m_123343_()) > this.lastViewDistance * 16 ? null : this.viewArea.getRenderChunkAt($$3);
        }
    }

    private boolean closeToBorder(BlockPos blockPos0, ChunkRenderDispatcher.RenderChunk chunkRenderDispatcherRenderChunk1) {
        int $$2 = SectionPos.blockToSectionCoord(blockPos0.m_123341_());
        int $$3 = SectionPos.blockToSectionCoord(blockPos0.m_123343_());
        BlockPos $$4 = chunkRenderDispatcherRenderChunk1.getOrigin();
        int $$5 = SectionPos.blockToSectionCoord($$4.m_123341_());
        int $$6 = SectionPos.blockToSectionCoord($$4.m_123343_());
        return !ChunkMap.isChunkInRange($$5, $$6, $$2, $$3, this.lastViewDistance - 3);
    }

    private void captureFrustum(Matrix4f matrixF0, Matrix4f matrixF1, double double2, double double3, double double4, Frustum frustum5) {
        this.capturedFrustum = frustum5;
        Matrix4f $$6 = new Matrix4f(matrixF1);
        $$6.mul(matrixF0);
        $$6.invert();
        this.frustumPos.x = double2;
        this.frustumPos.y = double3;
        this.frustumPos.z = double4;
        this.frustumPoints[0] = new Vector4f(-1.0F, -1.0F, -1.0F, 1.0F);
        this.frustumPoints[1] = new Vector4f(1.0F, -1.0F, -1.0F, 1.0F);
        this.frustumPoints[2] = new Vector4f(1.0F, 1.0F, -1.0F, 1.0F);
        this.frustumPoints[3] = new Vector4f(-1.0F, 1.0F, -1.0F, 1.0F);
        this.frustumPoints[4] = new Vector4f(-1.0F, -1.0F, 1.0F, 1.0F);
        this.frustumPoints[5] = new Vector4f(1.0F, -1.0F, 1.0F, 1.0F);
        this.frustumPoints[6] = new Vector4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.frustumPoints[7] = new Vector4f(-1.0F, 1.0F, 1.0F, 1.0F);
        for (int $$7 = 0; $$7 < 8; $$7++) {
            $$6.transform(this.frustumPoints[$$7]);
            this.frustumPoints[$$7].div(this.frustumPoints[$$7].w());
        }
    }

    public void prepareCullFrustum(PoseStack poseStack0, Vec3 vec1, Matrix4f matrixF2) {
        Matrix4f $$3 = poseStack0.last().pose();
        double $$4 = vec1.x();
        double $$5 = vec1.y();
        double $$6 = vec1.z();
        this.cullingFrustum = new Frustum($$3, matrixF2);
        this.cullingFrustum.prepare($$4, $$5, $$6);
    }

    public void renderLevel(PoseStack poseStack0, float float1, long long2, boolean boolean3, Camera camera4, GameRenderer gameRenderer5, LightTexture lightTexture6, Matrix4f matrixF7) {
        RenderSystem.setShaderGameTime(this.level.m_46467_(), float1);
        this.blockEntityRenderDispatcher.prepare(this.level, camera4, this.minecraft.hitResult);
        this.entityRenderDispatcher.prepare(this.level, camera4, this.minecraft.crosshairPickEntity);
        ProfilerFiller $$8 = this.level.m_46473_();
        $$8.popPush("light_update_queue");
        this.level.pollLightUpdates();
        $$8.popPush("light_updates");
        this.level.getChunkSource().getLightEngine().runLightUpdates();
        Vec3 $$9 = camera4.getPosition();
        double $$10 = $$9.x();
        double $$11 = $$9.y();
        double $$12 = $$9.z();
        Matrix4f $$13 = poseStack0.last().pose();
        $$8.popPush("culling");
        boolean $$14 = this.capturedFrustum != null;
        Frustum $$15;
        if ($$14) {
            $$15 = this.capturedFrustum;
            $$15.prepare(this.frustumPos.x, this.frustumPos.y, this.frustumPos.z);
        } else {
            $$15 = this.cullingFrustum;
        }
        this.minecraft.getProfiler().popPush("captureFrustum");
        if (this.captureFrustum) {
            this.captureFrustum($$13, matrixF7, $$9.x, $$9.y, $$9.z, $$14 ? new Frustum($$13, matrixF7) : $$15);
            this.captureFrustum = false;
        }
        $$8.popPush("clear");
        FogRenderer.setupColor(camera4, float1, this.minecraft.level, this.minecraft.options.getEffectiveRenderDistance(), gameRenderer5.getDarkenWorldAmount(float1));
        FogRenderer.levelFogColor();
        RenderSystem.clear(16640, Minecraft.ON_OSX);
        float $$17 = gameRenderer5.getRenderDistance();
        boolean $$18 = this.minecraft.level.effects().isFoggyAt(Mth.floor($$10), Mth.floor($$11)) || this.minecraft.gui.getBossOverlay().shouldCreateWorldFog();
        $$8.popPush("sky");
        RenderSystem.setShader(GameRenderer::m_172808_);
        this.renderSky(poseStack0, matrixF7, float1, camera4, $$18, () -> FogRenderer.setupFog(camera4, FogRenderer.FogMode.FOG_SKY, $$17, $$18, float1));
        $$8.popPush("fog");
        FogRenderer.setupFog(camera4, FogRenderer.FogMode.FOG_TERRAIN, Math.max($$17, 32.0F), $$18, float1);
        $$8.popPush("terrain_setup");
        this.setupRender(camera4, $$15, $$14, this.minecraft.player.m_5833_());
        $$8.popPush("compilechunks");
        this.compileChunks(camera4);
        $$8.popPush("terrain");
        this.renderChunkLayer(RenderType.solid(), poseStack0, $$10, $$11, $$12, matrixF7);
        this.renderChunkLayer(RenderType.cutoutMipped(), poseStack0, $$10, $$11, $$12, matrixF7);
        this.renderChunkLayer(RenderType.cutout(), poseStack0, $$10, $$11, $$12, matrixF7);
        if (this.level.effects().constantAmbientLight()) {
            Lighting.setupNetherLevel(poseStack0.last().pose());
        } else {
            Lighting.setupLevel(poseStack0.last().pose());
        }
        $$8.popPush("entities");
        this.renderedEntities = 0;
        this.culledEntities = 0;
        if (this.itemEntityTarget != null) {
            this.itemEntityTarget.clear(Minecraft.ON_OSX);
            this.itemEntityTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        if (this.weatherTarget != null) {
            this.weatherTarget.clear(Minecraft.ON_OSX);
        }
        if (this.shouldShowEntityOutlines()) {
            this.entityTarget.clear(Minecraft.ON_OSX);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        boolean $$19 = false;
        MultiBufferSource.BufferSource $$20 = this.renderBuffers.bufferSource();
        for (Entity $$21 : this.level.entitiesForRendering()) {
            if (this.entityRenderDispatcher.shouldRender($$21, $$15, $$10, $$11, $$12) || $$21.hasIndirectPassenger(this.minecraft.player)) {
                BlockPos $$22 = $$21.blockPosition();
                if ((this.level.m_151562_($$22.m_123342_()) || this.isChunkCompiled($$22)) && ($$21 != camera4.getEntity() || camera4.isDetached() || camera4.getEntity() instanceof LivingEntity && ((LivingEntity) camera4.getEntity()).isSleeping()) && (!($$21 instanceof LocalPlayer) || camera4.getEntity() == $$21)) {
                    this.renderedEntities++;
                    if ($$21.tickCount == 0) {
                        $$21.xOld = $$21.getX();
                        $$21.yOld = $$21.getY();
                        $$21.zOld = $$21.getZ();
                    }
                    MultiBufferSource $$24;
                    if (this.shouldShowEntityOutlines() && this.minecraft.shouldEntityAppearGlowing($$21)) {
                        $$19 = true;
                        OutlineBufferSource $$23 = this.renderBuffers.outlineBufferSource();
                        $$24 = $$23;
                        int $$25 = $$21.getTeamColor();
                        $$23.setColor(FastColor.ARGB32.red($$25), FastColor.ARGB32.green($$25), FastColor.ARGB32.blue($$25), 255);
                    } else {
                        $$24 = $$20;
                    }
                    this.renderEntity($$21, $$10, $$11, $$12, float1, poseStack0, $$24);
                }
            }
        }
        $$20.endLastBatch();
        this.checkPoseStack(poseStack0);
        $$20.endBatch(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
        $$20.endBatch(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
        $$20.endBatch(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
        $$20.endBatch(RenderType.entitySmoothCutout(TextureAtlas.LOCATION_BLOCKS));
        $$8.popPush("blockentities");
        ObjectListIterator var39 = this.renderChunksInFrustum.iterator();
        while (var39.hasNext()) {
            LevelRenderer.RenderChunkInfo $$27 = (LevelRenderer.RenderChunkInfo) var39.next();
            List<BlockEntity> $$28 = $$27.chunk.getCompiledChunk().getRenderableBlockEntities();
            if (!$$28.isEmpty()) {
                for (BlockEntity $$29 : $$28) {
                    BlockPos $$30 = $$29.getBlockPos();
                    MultiBufferSource $$31 = $$20;
                    poseStack0.pushPose();
                    poseStack0.translate((double) $$30.m_123341_() - $$10, (double) $$30.m_123342_() - $$11, (double) $$30.m_123343_() - $$12);
                    SortedSet<BlockDestructionProgress> $$32 = (SortedSet<BlockDestructionProgress>) this.destructionProgress.get($$30.asLong());
                    if ($$32 != null && !$$32.isEmpty()) {
                        int $$33 = ((BlockDestructionProgress) $$32.last()).getProgress();
                        if ($$33 >= 0) {
                            PoseStack.Pose $$34 = poseStack0.last();
                            VertexConsumer $$35 = new SheetedDecalTextureGenerator(this.renderBuffers.crumblingBufferSource().getBuffer((RenderType) ModelBakery.DESTROY_TYPES.get($$33)), $$34.pose(), $$34.normal(), 1.0F);
                            $$31 = p_234298_ -> {
                                VertexConsumer $$3 = $$20.getBuffer(p_234298_);
                                return p_234298_.affectsCrumbling() ? VertexMultiConsumer.create($$35, $$3) : $$3;
                            };
                        }
                    }
                    this.blockEntityRenderDispatcher.render($$29, float1, poseStack0, $$31);
                    poseStack0.popPose();
                }
            }
        }
        synchronized (this.globalBlockEntities) {
            for (BlockEntity $$36 : this.globalBlockEntities) {
                BlockPos $$37 = $$36.getBlockPos();
                poseStack0.pushPose();
                poseStack0.translate((double) $$37.m_123341_() - $$10, (double) $$37.m_123342_() - $$11, (double) $$37.m_123343_() - $$12);
                this.blockEntityRenderDispatcher.render($$36, float1, poseStack0, $$20);
                poseStack0.popPose();
            }
        }
        this.checkPoseStack(poseStack0);
        $$20.endBatch(RenderType.solid());
        $$20.endBatch(RenderType.endPortal());
        $$20.endBatch(RenderType.endGateway());
        $$20.endBatch(Sheets.solidBlockSheet());
        $$20.endBatch(Sheets.cutoutBlockSheet());
        $$20.endBatch(Sheets.bedSheet());
        $$20.endBatch(Sheets.shulkerBoxSheet());
        $$20.endBatch(Sheets.signSheet());
        $$20.endBatch(Sheets.hangingSignSheet());
        $$20.endBatch(Sheets.chestSheet());
        this.renderBuffers.outlineBufferSource().endOutlineBatch();
        if ($$19) {
            this.entityEffect.process(float1);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        }
        $$8.popPush("destroyProgress");
        ObjectIterator var41 = this.destructionProgress.long2ObjectEntrySet().iterator();
        while (var41.hasNext()) {
            Entry<SortedSet<BlockDestructionProgress>> $$38 = (Entry<SortedSet<BlockDestructionProgress>>) var41.next();
            BlockPos $$39 = BlockPos.of($$38.getLongKey());
            double $$40 = (double) $$39.m_123341_() - $$10;
            double $$41 = (double) $$39.m_123342_() - $$11;
            double $$42 = (double) $$39.m_123343_() - $$12;
            if (!($$40 * $$40 + $$41 * $$41 + $$42 * $$42 > 1024.0)) {
                SortedSet<BlockDestructionProgress> $$43 = (SortedSet<BlockDestructionProgress>) $$38.getValue();
                if ($$43 != null && !$$43.isEmpty()) {
                    int $$44 = ((BlockDestructionProgress) $$43.last()).getProgress();
                    poseStack0.pushPose();
                    poseStack0.translate((double) $$39.m_123341_() - $$10, (double) $$39.m_123342_() - $$11, (double) $$39.m_123343_() - $$12);
                    PoseStack.Pose $$45 = poseStack0.last();
                    VertexConsumer $$46 = new SheetedDecalTextureGenerator(this.renderBuffers.crumblingBufferSource().getBuffer((RenderType) ModelBakery.DESTROY_TYPES.get($$44)), $$45.pose(), $$45.normal(), 1.0F);
                    this.minecraft.getBlockRenderer().renderBreakingTexture(this.level.m_8055_($$39), $$39, this.level, poseStack0, $$46);
                    poseStack0.popPose();
                }
            }
        }
        this.checkPoseStack(poseStack0);
        HitResult $$47 = this.minecraft.hitResult;
        if (boolean3 && $$47 != null && $$47.getType() == HitResult.Type.BLOCK) {
            $$8.popPush("outline");
            BlockPos $$48 = ((BlockHitResult) $$47).getBlockPos();
            BlockState $$49 = this.level.m_8055_($$48);
            if (!$$49.m_60795_() && this.level.m_6857_().isWithinBounds($$48)) {
                VertexConsumer $$50 = $$20.getBuffer(RenderType.lines());
                this.renderHitOutline(poseStack0, $$50, camera4.getEntity(), $$10, $$11, $$12, $$48, $$49);
            }
        }
        this.minecraft.debugRenderer.render(poseStack0, $$20, $$10, $$11, $$12);
        $$20.endLastBatch();
        PoseStack $$51 = RenderSystem.getModelViewStack();
        RenderSystem.applyModelViewMatrix();
        $$20.endBatch(Sheets.translucentCullBlockSheet());
        $$20.endBatch(Sheets.bannerSheet());
        $$20.endBatch(Sheets.shieldSheet());
        $$20.endBatch(RenderType.armorGlint());
        $$20.endBatch(RenderType.armorEntityGlint());
        $$20.endBatch(RenderType.glint());
        $$20.endBatch(RenderType.glintDirect());
        $$20.endBatch(RenderType.glintTranslucent());
        $$20.endBatch(RenderType.entityGlint());
        $$20.endBatch(RenderType.entityGlintDirect());
        $$20.endBatch(RenderType.waterMask());
        this.renderBuffers.crumblingBufferSource().endBatch();
        if (this.transparencyChain != null) {
            $$20.endBatch(RenderType.lines());
            $$20.endBatch();
            this.translucentTarget.clear(Minecraft.ON_OSX);
            this.translucentTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            $$8.popPush("translucent");
            this.renderChunkLayer(RenderType.translucent(), poseStack0, $$10, $$11, $$12, matrixF7);
            $$8.popPush("string");
            this.renderChunkLayer(RenderType.tripwire(), poseStack0, $$10, $$11, $$12, matrixF7);
            this.particlesTarget.clear(Minecraft.ON_OSX);
            this.particlesTarget.copyDepthFrom(this.minecraft.getMainRenderTarget());
            RenderStateShard.PARTICLES_TARGET.m_110185_();
            $$8.popPush("particles");
            this.minecraft.particleEngine.render(poseStack0, $$20, lightTexture6, camera4, float1);
            RenderStateShard.PARTICLES_TARGET.m_110188_();
        } else {
            $$8.popPush("translucent");
            if (this.translucentTarget != null) {
                this.translucentTarget.clear(Minecraft.ON_OSX);
            }
            this.renderChunkLayer(RenderType.translucent(), poseStack0, $$10, $$11, $$12, matrixF7);
            $$20.endBatch(RenderType.lines());
            $$20.endBatch();
            $$8.popPush("string");
            this.renderChunkLayer(RenderType.tripwire(), poseStack0, $$10, $$11, $$12, matrixF7);
            $$8.popPush("particles");
            this.minecraft.particleEngine.render(poseStack0, $$20, lightTexture6, camera4, float1);
        }
        $$51.pushPose();
        $$51.mulPoseMatrix(poseStack0.last().pose());
        RenderSystem.applyModelViewMatrix();
        if (this.minecraft.options.getCloudsType() != CloudStatus.OFF) {
            if (this.transparencyChain != null) {
                this.cloudsTarget.clear(Minecraft.ON_OSX);
                RenderStateShard.CLOUDS_TARGET.m_110185_();
                $$8.popPush("clouds");
                this.renderClouds(poseStack0, matrixF7, float1, $$10, $$11, $$12);
                RenderStateShard.CLOUDS_TARGET.m_110188_();
            } else {
                $$8.popPush("clouds");
                RenderSystem.setShader(GameRenderer::m_172838_);
                this.renderClouds(poseStack0, matrixF7, float1, $$10, $$11, $$12);
            }
        }
        if (this.transparencyChain != null) {
            RenderStateShard.WEATHER_TARGET.m_110185_();
            $$8.popPush("weather");
            this.renderSnowAndRain(lightTexture6, float1, $$10, $$11, $$12);
            this.renderWorldBorder(camera4);
            RenderStateShard.WEATHER_TARGET.m_110188_();
            this.transparencyChain.process(float1);
            this.minecraft.getMainRenderTarget().bindWrite(false);
        } else {
            RenderSystem.depthMask(false);
            $$8.popPush("weather");
            this.renderSnowAndRain(lightTexture6, float1, $$10, $$11, $$12);
            this.renderWorldBorder(camera4);
            RenderSystem.depthMask(true);
        }
        $$51.popPose();
        RenderSystem.applyModelViewMatrix();
        this.renderDebug(poseStack0, $$20, camera4);
        $$20.endLastBatch();
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
        FogRenderer.setupNoFog();
    }

    private void checkPoseStack(PoseStack poseStack0) {
        if (!poseStack0.clear()) {
            throw new IllegalStateException("Pose stack not empty");
        }
    }

    private void renderEntity(Entity entity0, double double1, double double2, double double3, float float4, PoseStack poseStack5, MultiBufferSource multiBufferSource6) {
        double $$7 = Mth.lerp((double) float4, entity0.xOld, entity0.getX());
        double $$8 = Mth.lerp((double) float4, entity0.yOld, entity0.getY());
        double $$9 = Mth.lerp((double) float4, entity0.zOld, entity0.getZ());
        float $$10 = Mth.lerp(float4, entity0.yRotO, entity0.getYRot());
        this.entityRenderDispatcher.render(entity0, $$7 - double1, $$8 - double2, $$9 - double3, $$10, float4, poseStack5, multiBufferSource6, this.entityRenderDispatcher.getPackedLightCoords(entity0, float4));
    }

    private void renderChunkLayer(RenderType renderType0, PoseStack poseStack1, double double2, double double3, double double4, Matrix4f matrixF5) {
        RenderSystem.assertOnRenderThread();
        renderType0.m_110185_();
        if (renderType0 == RenderType.translucent()) {
            this.minecraft.getProfiler().push("translucent_sort");
            double $$6 = double2 - this.xTransparentOld;
            double $$7 = double3 - this.yTransparentOld;
            double $$8 = double4 - this.zTransparentOld;
            if ($$6 * $$6 + $$7 * $$7 + $$8 * $$8 > 1.0) {
                int $$9 = SectionPos.posToSectionCoord(double2);
                int $$10 = SectionPos.posToSectionCoord(double3);
                int $$11 = SectionPos.posToSectionCoord(double4);
                boolean $$12 = $$9 != SectionPos.posToSectionCoord(this.xTransparentOld) || $$11 != SectionPos.posToSectionCoord(this.zTransparentOld) || $$10 != SectionPos.posToSectionCoord(this.yTransparentOld);
                this.xTransparentOld = double2;
                this.yTransparentOld = double3;
                this.zTransparentOld = double4;
                int $$13 = 0;
                ObjectListIterator var21 = this.renderChunksInFrustum.iterator();
                while (var21.hasNext()) {
                    LevelRenderer.RenderChunkInfo $$14 = (LevelRenderer.RenderChunkInfo) var21.next();
                    if ($$13 < 15 && ($$12 || $$14.isAxisAlignedWith($$9, $$10, $$11)) && $$14.chunk.resortTransparency(renderType0, this.chunkRenderDispatcher)) {
                        $$13++;
                    }
                }
            }
            this.minecraft.getProfiler().pop();
        }
        this.minecraft.getProfiler().push("filterempty");
        this.minecraft.getProfiler().popPush((Supplier<String>) (() -> "render_" + renderType0));
        boolean $$15 = renderType0 != RenderType.translucent();
        ObjectListIterator<LevelRenderer.RenderChunkInfo> $$16 = this.renderChunksInFrustum.listIterator($$15 ? 0 : this.renderChunksInFrustum.size());
        ShaderInstance $$17 = RenderSystem.getShader();
        for (int $$18 = 0; $$18 < 12; $$18++) {
            int $$19 = RenderSystem.getShaderTexture($$18);
            $$17.setSampler("Sampler" + $$18, $$19);
        }
        if ($$17.MODEL_VIEW_MATRIX != null) {
            $$17.MODEL_VIEW_MATRIX.set(poseStack1.last().pose());
        }
        if ($$17.PROJECTION_MATRIX != null) {
            $$17.PROJECTION_MATRIX.set(matrixF5);
        }
        if ($$17.COLOR_MODULATOR != null) {
            $$17.COLOR_MODULATOR.set(RenderSystem.getShaderColor());
        }
        if ($$17.GLINT_ALPHA != null) {
            $$17.GLINT_ALPHA.set(RenderSystem.getShaderGlintAlpha());
        }
        if ($$17.FOG_START != null) {
            $$17.FOG_START.set(RenderSystem.getShaderFogStart());
        }
        if ($$17.FOG_END != null) {
            $$17.FOG_END.set(RenderSystem.getShaderFogEnd());
        }
        if ($$17.FOG_COLOR != null) {
            $$17.FOG_COLOR.set(RenderSystem.getShaderFogColor());
        }
        if ($$17.FOG_SHAPE != null) {
            $$17.FOG_SHAPE.set(RenderSystem.getShaderFogShape().getIndex());
        }
        if ($$17.TEXTURE_MATRIX != null) {
            $$17.TEXTURE_MATRIX.set(RenderSystem.getTextureMatrix());
        }
        if ($$17.GAME_TIME != null) {
            $$17.GAME_TIME.set(RenderSystem.getShaderGameTime());
        }
        RenderSystem.setupShaderLights($$17);
        $$17.apply();
        Uniform $$20 = $$17.CHUNK_OFFSET;
        while ($$15 ? $$16.hasNext() : $$16.hasPrevious()) {
            LevelRenderer.RenderChunkInfo $$21 = $$15 ? (LevelRenderer.RenderChunkInfo) $$16.next() : (LevelRenderer.RenderChunkInfo) $$16.previous();
            ChunkRenderDispatcher.RenderChunk $$22 = $$21.chunk;
            if (!$$22.getCompiledChunk().isEmpty(renderType0)) {
                VertexBuffer $$23 = $$22.getBuffer(renderType0);
                BlockPos $$24 = $$22.getOrigin();
                if ($$20 != null) {
                    $$20.set((float) ((double) $$24.m_123341_() - double2), (float) ((double) $$24.m_123342_() - double3), (float) ((double) $$24.m_123343_() - double4));
                    $$20.upload();
                }
                $$23.bind();
                $$23.draw();
            }
        }
        if ($$20 != null) {
            $$20.set(0.0F, 0.0F, 0.0F);
        }
        $$17.clear();
        VertexBuffer.unbind();
        this.minecraft.getProfiler().pop();
        renderType0.m_110188_();
    }

    private void renderDebug(PoseStack poseStack0, MultiBufferSource multiBufferSource1, Camera camera2) {
        if (this.minecraft.chunkPath || this.minecraft.chunkVisibility) {
            double $$3 = camera2.getPosition().x();
            double $$4 = camera2.getPosition().y();
            double $$5 = camera2.getPosition().z();
            ObjectListIterator var10 = this.renderChunksInFrustum.iterator();
            while (var10.hasNext()) {
                LevelRenderer.RenderChunkInfo $$6 = (LevelRenderer.RenderChunkInfo) var10.next();
                ChunkRenderDispatcher.RenderChunk $$7 = $$6.chunk;
                BlockPos $$8 = $$7.getOrigin();
                poseStack0.pushPose();
                poseStack0.translate((double) $$8.m_123341_() - $$3, (double) $$8.m_123342_() - $$4, (double) $$8.m_123343_() - $$5);
                Matrix4f $$9 = poseStack0.last().pose();
                if (this.minecraft.chunkPath) {
                    VertexConsumer $$10 = multiBufferSource1.getBuffer(RenderType.lines());
                    int $$11 = $$6.step == 0 ? 0 : Mth.hsvToRgb((float) $$6.step / 50.0F, 0.9F, 0.9F);
                    int $$12 = $$11 >> 16 & 0xFF;
                    int $$13 = $$11 >> 8 & 0xFF;
                    int $$14 = $$11 & 0xFF;
                    for (int $$15 = 0; $$15 < DIRECTIONS.length; $$15++) {
                        if ($$6.hasSourceDirection($$15)) {
                            Direction $$16 = DIRECTIONS[$$15];
                            $$10.vertex($$9, 8.0F, 8.0F, 8.0F).color($$12, $$13, $$14, 255).normal((float) $$16.getStepX(), (float) $$16.getStepY(), (float) $$16.getStepZ()).endVertex();
                            $$10.vertex($$9, (float) (8 - 16 * $$16.getStepX()), (float) (8 - 16 * $$16.getStepY()), (float) (8 - 16 * $$16.getStepZ())).color($$12, $$13, $$14, 255).normal((float) $$16.getStepX(), (float) $$16.getStepY(), (float) $$16.getStepZ()).endVertex();
                        }
                    }
                }
                if (this.minecraft.chunkVisibility && !$$7.getCompiledChunk().hasNoRenderableLayers()) {
                    VertexConsumer $$17 = multiBufferSource1.getBuffer(RenderType.lines());
                    int $$18 = 0;
                    for (Direction $$19 : DIRECTIONS) {
                        for (Direction $$20 : DIRECTIONS) {
                            boolean $$21 = $$7.getCompiledChunk().facesCanSeeEachother($$19, $$20);
                            if (!$$21) {
                                $$18++;
                                $$17.vertex($$9, (float) (8 + 8 * $$19.getStepX()), (float) (8 + 8 * $$19.getStepY()), (float) (8 + 8 * $$19.getStepZ())).color(255, 0, 0, 255).normal((float) $$19.getStepX(), (float) $$19.getStepY(), (float) $$19.getStepZ()).endVertex();
                                $$17.vertex($$9, (float) (8 + 8 * $$20.getStepX()), (float) (8 + 8 * $$20.getStepY()), (float) (8 + 8 * $$20.getStepZ())).color(255, 0, 0, 255).normal((float) $$20.getStepX(), (float) $$20.getStepY(), (float) $$20.getStepZ()).endVertex();
                            }
                        }
                    }
                    if ($$18 > 0) {
                        VertexConsumer $$22 = multiBufferSource1.getBuffer(RenderType.debugQuads());
                        float $$23 = 0.5F;
                        float $$24 = 0.2F;
                        $$22.vertex($$9, 0.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 0.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 15.5F, 0.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 15.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 15.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                        $$22.vertex($$9, 0.5F, 0.5F, 15.5F).color(0.9F, 0.9F, 0.0F, 0.2F).endVertex();
                    }
                }
                poseStack0.popPose();
            }
        }
        if (this.capturedFrustum != null) {
            poseStack0.pushPose();
            poseStack0.translate((float) (this.frustumPos.x - camera2.getPosition().x), (float) (this.frustumPos.y - camera2.getPosition().y), (float) (this.frustumPos.z - camera2.getPosition().z));
            Matrix4f $$25 = poseStack0.last().pose();
            VertexConsumer $$26 = multiBufferSource1.getBuffer(RenderType.debugQuads());
            this.addFrustumQuad($$26, $$25, 0, 1, 2, 3, 0, 1, 1);
            this.addFrustumQuad($$26, $$25, 4, 5, 6, 7, 1, 0, 0);
            this.addFrustumQuad($$26, $$25, 0, 1, 5, 4, 1, 1, 0);
            this.addFrustumQuad($$26, $$25, 2, 3, 7, 6, 0, 0, 1);
            this.addFrustumQuad($$26, $$25, 0, 4, 7, 3, 0, 1, 0);
            this.addFrustumQuad($$26, $$25, 1, 5, 6, 2, 1, 0, 1);
            VertexConsumer $$27 = multiBufferSource1.getBuffer(RenderType.lines());
            this.addFrustumVertex($$27, $$25, 0);
            this.addFrustumVertex($$27, $$25, 1);
            this.addFrustumVertex($$27, $$25, 1);
            this.addFrustumVertex($$27, $$25, 2);
            this.addFrustumVertex($$27, $$25, 2);
            this.addFrustumVertex($$27, $$25, 3);
            this.addFrustumVertex($$27, $$25, 3);
            this.addFrustumVertex($$27, $$25, 0);
            this.addFrustumVertex($$27, $$25, 4);
            this.addFrustumVertex($$27, $$25, 5);
            this.addFrustumVertex($$27, $$25, 5);
            this.addFrustumVertex($$27, $$25, 6);
            this.addFrustumVertex($$27, $$25, 6);
            this.addFrustumVertex($$27, $$25, 7);
            this.addFrustumVertex($$27, $$25, 7);
            this.addFrustumVertex($$27, $$25, 4);
            this.addFrustumVertex($$27, $$25, 0);
            this.addFrustumVertex($$27, $$25, 4);
            this.addFrustumVertex($$27, $$25, 1);
            this.addFrustumVertex($$27, $$25, 5);
            this.addFrustumVertex($$27, $$25, 2);
            this.addFrustumVertex($$27, $$25, 6);
            this.addFrustumVertex($$27, $$25, 3);
            this.addFrustumVertex($$27, $$25, 7);
            poseStack0.popPose();
        }
    }

    private void addFrustumVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, int int2) {
        vertexConsumer0.vertex(matrixF1, this.frustumPoints[int2].x(), this.frustumPoints[int2].y(), this.frustumPoints[int2].z()).color(0, 0, 0, 255).normal(0.0F, 0.0F, -1.0F).endVertex();
    }

    private void addFrustumQuad(VertexConsumer vertexConsumer0, Matrix4f matrixF1, int int2, int int3, int int4, int int5, int int6, int int7, int int8) {
        float $$9 = 0.25F;
        vertexConsumer0.vertex(matrixF1, this.frustumPoints[int2].x(), this.frustumPoints[int2].y(), this.frustumPoints[int2].z()).color((float) int6, (float) int7, (float) int8, 0.25F).endVertex();
        vertexConsumer0.vertex(matrixF1, this.frustumPoints[int3].x(), this.frustumPoints[int3].y(), this.frustumPoints[int3].z()).color((float) int6, (float) int7, (float) int8, 0.25F).endVertex();
        vertexConsumer0.vertex(matrixF1, this.frustumPoints[int4].x(), this.frustumPoints[int4].y(), this.frustumPoints[int4].z()).color((float) int6, (float) int7, (float) int8, 0.25F).endVertex();
        vertexConsumer0.vertex(matrixF1, this.frustumPoints[int5].x(), this.frustumPoints[int5].y(), this.frustumPoints[int5].z()).color((float) int6, (float) int7, (float) int8, 0.25F).endVertex();
    }

    public void captureFrustum() {
        this.captureFrustum = true;
    }

    public void killFrustum() {
        this.capturedFrustum = null;
    }

    public void tick() {
        this.ticks++;
        if (this.ticks % 20 == 0) {
            Iterator<BlockDestructionProgress> $$0 = this.destroyingBlocks.values().iterator();
            while ($$0.hasNext()) {
                BlockDestructionProgress $$1 = (BlockDestructionProgress) $$0.next();
                int $$2 = $$1.getUpdatedRenderTick();
                if (this.ticks - $$2 > 400) {
                    $$0.remove();
                    this.removeProgress($$1);
                }
            }
        }
    }

    private void removeProgress(BlockDestructionProgress blockDestructionProgress0) {
        long $$1 = blockDestructionProgress0.getPos().asLong();
        Set<BlockDestructionProgress> $$2 = (Set<BlockDestructionProgress>) this.destructionProgress.get($$1);
        $$2.remove(blockDestructionProgress0);
        if ($$2.isEmpty()) {
            this.destructionProgress.remove($$1);
        }
    }

    private void renderEndSky(PoseStack poseStack0) {
        RenderSystem.enableBlend();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::m_172820_);
        RenderSystem.setShaderTexture(0, END_SKY_LOCATION);
        Tesselator $$1 = Tesselator.getInstance();
        BufferBuilder $$2 = $$1.getBuilder();
        for (int $$3 = 0; $$3 < 6; $$3++) {
            poseStack0.pushPose();
            if ($$3 == 1) {
                poseStack0.mulPose(Axis.XP.rotationDegrees(90.0F));
            }
            if ($$3 == 2) {
                poseStack0.mulPose(Axis.XP.rotationDegrees(-90.0F));
            }
            if ($$3 == 3) {
                poseStack0.mulPose(Axis.XP.rotationDegrees(180.0F));
            }
            if ($$3 == 4) {
                poseStack0.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
            if ($$3 == 5) {
                poseStack0.mulPose(Axis.ZP.rotationDegrees(-90.0F));
            }
            Matrix4f $$4 = poseStack0.last().pose();
            $$2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            $$2.m_252986_($$4, -100.0F, -100.0F, -100.0F).uv(0.0F, 0.0F).color(40, 40, 40, 255).endVertex();
            $$2.m_252986_($$4, -100.0F, -100.0F, 100.0F).uv(0.0F, 16.0F).color(40, 40, 40, 255).endVertex();
            $$2.m_252986_($$4, 100.0F, -100.0F, 100.0F).uv(16.0F, 16.0F).color(40, 40, 40, 255).endVertex();
            $$2.m_252986_($$4, 100.0F, -100.0F, -100.0F).uv(16.0F, 0.0F).color(40, 40, 40, 255).endVertex();
            $$1.end();
            poseStack0.popPose();
        }
        RenderSystem.depthMask(true);
        RenderSystem.disableBlend();
    }

    public void renderSky(PoseStack poseStack0, Matrix4f matrixF1, float float2, Camera camera3, boolean boolean4, Runnable runnable5) {
        runnable5.run();
        if (!boolean4) {
            FogType $$6 = camera3.getFluidInCamera();
            if ($$6 != FogType.POWDER_SNOW && $$6 != FogType.LAVA && !this.doesMobEffectBlockSky(camera3)) {
                if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.END) {
                    this.renderEndSky(poseStack0);
                } else if (this.minecraft.level.effects().skyType() == DimensionSpecialEffects.SkyType.NORMAL) {
                    Vec3 $$7 = this.level.getSkyColor(this.minecraft.gameRenderer.getMainCamera().getPosition(), float2);
                    float $$8 = (float) $$7.x;
                    float $$9 = (float) $$7.y;
                    float $$10 = (float) $$7.z;
                    FogRenderer.levelFogColor();
                    BufferBuilder $$11 = Tesselator.getInstance().getBuilder();
                    RenderSystem.depthMask(false);
                    RenderSystem.setShaderColor($$8, $$9, $$10, 1.0F);
                    ShaderInstance $$12 = RenderSystem.getShader();
                    this.skyBuffer.bind();
                    this.skyBuffer.drawWithShader(poseStack0.last().pose(), matrixF1, $$12);
                    VertexBuffer.unbind();
                    RenderSystem.enableBlend();
                    float[] $$13 = this.level.effects().getSunriseColor(this.level.m_46942_(float2), float2);
                    if ($$13 != null) {
                        RenderSystem.setShader(GameRenderer::m_172811_);
                        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                        poseStack0.pushPose();
                        poseStack0.mulPose(Axis.XP.rotationDegrees(90.0F));
                        float $$14 = Mth.sin(this.level.m_46490_(float2)) < 0.0F ? 180.0F : 0.0F;
                        poseStack0.mulPose(Axis.ZP.rotationDegrees($$14));
                        poseStack0.mulPose(Axis.ZP.rotationDegrees(90.0F));
                        float $$15 = $$13[0];
                        float $$16 = $$13[1];
                        float $$17 = $$13[2];
                        Matrix4f $$18 = poseStack0.last().pose();
                        $$11.begin(VertexFormat.Mode.TRIANGLE_FAN, DefaultVertexFormat.POSITION_COLOR);
                        $$11.m_252986_($$18, 0.0F, 100.0F, 0.0F).color($$15, $$16, $$17, $$13[3]).endVertex();
                        int $$19 = 16;
                        for (int $$20 = 0; $$20 <= 16; $$20++) {
                            float $$21 = (float) $$20 * (float) (Math.PI * 2) / 16.0F;
                            float $$22 = Mth.sin($$21);
                            float $$23 = Mth.cos($$21);
                            $$11.m_252986_($$18, $$22 * 120.0F, $$23 * 120.0F, -$$23 * 40.0F * $$13[3]).color($$13[0], $$13[1], $$13[2], 0.0F).endVertex();
                        }
                        BufferUploader.drawWithShader($$11.end());
                        poseStack0.popPose();
                    }
                    RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
                    poseStack0.pushPose();
                    float $$24 = 1.0F - this.level.m_46722_(float2);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, $$24);
                    poseStack0.mulPose(Axis.YP.rotationDegrees(-90.0F));
                    poseStack0.mulPose(Axis.XP.rotationDegrees(this.level.m_46942_(float2) * 360.0F));
                    Matrix4f $$25 = poseStack0.last().pose();
                    float $$26 = 30.0F;
                    RenderSystem.setShader(GameRenderer::m_172817_);
                    RenderSystem.setShaderTexture(0, SUN_LOCATION);
                    $$11.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    $$11.m_252986_($$25, -$$26, 100.0F, -$$26).uv(0.0F, 0.0F).endVertex();
                    $$11.m_252986_($$25, $$26, 100.0F, -$$26).uv(1.0F, 0.0F).endVertex();
                    $$11.m_252986_($$25, $$26, 100.0F, $$26).uv(1.0F, 1.0F).endVertex();
                    $$11.m_252986_($$25, -$$26, 100.0F, $$26).uv(0.0F, 1.0F).endVertex();
                    BufferUploader.drawWithShader($$11.end());
                    $$26 = 20.0F;
                    RenderSystem.setShaderTexture(0, MOON_LOCATION);
                    int $$27 = this.level.m_46941_();
                    int $$28 = $$27 % 4;
                    int $$29 = $$27 / 4 % 2;
                    float $$30 = (float) ($$28 + 0) / 4.0F;
                    float $$31 = (float) ($$29 + 0) / 2.0F;
                    float $$32 = (float) ($$28 + 1) / 4.0F;
                    float $$33 = (float) ($$29 + 1) / 2.0F;
                    $$11.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
                    $$11.m_252986_($$25, -$$26, -100.0F, $$26).uv($$32, $$33).endVertex();
                    $$11.m_252986_($$25, $$26, -100.0F, $$26).uv($$30, $$33).endVertex();
                    $$11.m_252986_($$25, $$26, -100.0F, -$$26).uv($$30, $$31).endVertex();
                    $$11.m_252986_($$25, -$$26, -100.0F, -$$26).uv($$32, $$31).endVertex();
                    BufferUploader.drawWithShader($$11.end());
                    float $$34 = this.level.getStarBrightness(float2) * $$24;
                    if ($$34 > 0.0F) {
                        RenderSystem.setShaderColor($$34, $$34, $$34, $$34);
                        FogRenderer.setupNoFog();
                        this.starBuffer.bind();
                        this.starBuffer.drawWithShader(poseStack0.last().pose(), matrixF1, GameRenderer.getPositionShader());
                        VertexBuffer.unbind();
                        runnable5.run();
                    }
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.disableBlend();
                    RenderSystem.defaultBlendFunc();
                    poseStack0.popPose();
                    RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 1.0F);
                    double $$35 = this.minecraft.player.m_20299_(float2).y - this.level.getLevelData().getHorizonHeight(this.level);
                    if ($$35 < 0.0) {
                        poseStack0.pushPose();
                        poseStack0.translate(0.0F, 12.0F, 0.0F);
                        this.darkBuffer.bind();
                        this.darkBuffer.drawWithShader(poseStack0.last().pose(), matrixF1, $$12);
                        VertexBuffer.unbind();
                        poseStack0.popPose();
                    }
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                    RenderSystem.depthMask(true);
                }
            }
        }
    }

    private boolean doesMobEffectBlockSky(Camera camera0) {
        return !(camera0.getEntity() instanceof LivingEntity $$1) ? false : $$1.hasEffect(MobEffects.BLINDNESS) || $$1.hasEffect(MobEffects.DARKNESS);
    }

    public void renderClouds(PoseStack poseStack0, Matrix4f matrixF1, float float2, double double3, double double4, double double5) {
        float $$6 = this.level.effects().getCloudHeight();
        if (!Float.isNaN($$6)) {
            RenderSystem.disableCull();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.depthMask(true);
            float $$7 = 12.0F;
            float $$8 = 4.0F;
            double $$9 = 2.0E-4;
            double $$10 = (double) (((float) this.ticks + float2) * 0.03F);
            double $$11 = (double3 + $$10) / 12.0;
            double $$12 = (double) ($$6 - (float) double4 + 0.33F);
            double $$13 = double5 / 12.0 + 0.33F;
            $$11 -= (double) (Mth.floor($$11 / 2048.0) * 2048);
            $$13 -= (double) (Mth.floor($$13 / 2048.0) * 2048);
            float $$14 = (float) ($$11 - (double) Mth.floor($$11));
            float $$15 = (float) ($$12 / 4.0 - (double) Mth.floor($$12 / 4.0)) * 4.0F;
            float $$16 = (float) ($$13 - (double) Mth.floor($$13));
            Vec3 $$17 = this.level.getCloudColor(float2);
            int $$18 = (int) Math.floor($$11);
            int $$19 = (int) Math.floor($$12 / 4.0);
            int $$20 = (int) Math.floor($$13);
            if ($$18 != this.prevCloudX || $$19 != this.prevCloudY || $$20 != this.prevCloudZ || this.minecraft.options.getCloudsType() != this.prevCloudsType || this.prevCloudColor.distanceToSqr($$17) > 2.0E-4) {
                this.prevCloudX = $$18;
                this.prevCloudY = $$19;
                this.prevCloudZ = $$20;
                this.prevCloudColor = $$17;
                this.prevCloudsType = this.minecraft.options.getCloudsType();
                this.generateClouds = true;
            }
            if (this.generateClouds) {
                this.generateClouds = false;
                BufferBuilder $$21 = Tesselator.getInstance().getBuilder();
                if (this.cloudBuffer != null) {
                    this.cloudBuffer.close();
                }
                this.cloudBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
                BufferBuilder.RenderedBuffer $$22 = this.buildClouds($$21, $$11, $$12, $$13, $$17);
                this.cloudBuffer.bind();
                this.cloudBuffer.upload($$22);
                VertexBuffer.unbind();
            }
            RenderSystem.setShader(GameRenderer::m_172838_);
            RenderSystem.setShaderTexture(0, CLOUDS_LOCATION);
            FogRenderer.levelFogColor();
            poseStack0.pushPose();
            poseStack0.scale(12.0F, 1.0F, 12.0F);
            poseStack0.translate(-$$14, $$15, -$$16);
            if (this.cloudBuffer != null) {
                this.cloudBuffer.bind();
                int $$23 = this.prevCloudsType == CloudStatus.FANCY ? 0 : 1;
                for (int $$24 = $$23; $$24 < 2; $$24++) {
                    if ($$24 == 0) {
                        RenderSystem.colorMask(false, false, false, false);
                    } else {
                        RenderSystem.colorMask(true, true, true, true);
                    }
                    ShaderInstance $$25 = RenderSystem.getShader();
                    this.cloudBuffer.drawWithShader(poseStack0.last().pose(), matrixF1, $$25);
                }
                VertexBuffer.unbind();
            }
            poseStack0.popPose();
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }

    private BufferBuilder.RenderedBuffer buildClouds(BufferBuilder bufferBuilder0, double double1, double double2, double double3, Vec3 vec4) {
        float $$5 = 4.0F;
        float $$6 = 0.00390625F;
        int $$7 = 8;
        int $$8 = 4;
        float $$9 = 9.765625E-4F;
        float $$10 = (float) Mth.floor(double1) * 0.00390625F;
        float $$11 = (float) Mth.floor(double3) * 0.00390625F;
        float $$12 = (float) vec4.x;
        float $$13 = (float) vec4.y;
        float $$14 = (float) vec4.z;
        float $$15 = $$12 * 0.9F;
        float $$16 = $$13 * 0.9F;
        float $$17 = $$14 * 0.9F;
        float $$18 = $$12 * 0.7F;
        float $$19 = $$13 * 0.7F;
        float $$20 = $$14 * 0.7F;
        float $$21 = $$12 * 0.8F;
        float $$22 = $$13 * 0.8F;
        float $$23 = $$14 * 0.8F;
        RenderSystem.setShader(GameRenderer::m_172838_);
        bufferBuilder0.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL);
        float $$24 = (float) Math.floor(double2 / 4.0) * 4.0F;
        if (this.prevCloudsType == CloudStatus.FANCY) {
            for (int $$25 = -3; $$25 <= 4; $$25++) {
                for (int $$26 = -3; $$26 <= 4; $$26++) {
                    float $$27 = (float) ($$25 * 8);
                    float $$28 = (float) ($$26 * 8);
                    if ($$24 > -5.0F) {
                        bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 0.0F), (double) ($$28 + 8.0F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$18, $$19, $$20, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 0.0F), (double) ($$28 + 8.0F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$18, $$19, $$20, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 0.0F), (double) ($$28 + 0.0F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$18, $$19, $$20, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                        bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 0.0F), (double) ($$28 + 0.0F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$18, $$19, $$20, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    }
                    if ($$24 <= 5.0F) {
                        bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 4.0F - 9.765625E-4F), (double) ($$28 + 8.0F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 4.0F - 9.765625E-4F), (double) ($$28 + 8.0F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 4.0F - 9.765625E-4F), (double) ($$28 + 0.0F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                        bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 4.0F - 9.765625E-4F), (double) ($$28 + 0.0F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, 1.0F, 0.0F).endVertex();
                    }
                    if ($$25 > -1) {
                        for (int $$29 = 0; $$29 < 8; $$29++) {
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$29 + 0.0F), (double) ($$24 + 0.0F), (double) ($$28 + 8.0F)).uv(($$27 + (float) $$29 + 0.5F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$29 + 0.0F), (double) ($$24 + 4.0F), (double) ($$28 + 8.0F)).uv(($$27 + (float) $$29 + 0.5F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$29 + 0.0F), (double) ($$24 + 4.0F), (double) ($$28 + 0.0F)).uv(($$27 + (float) $$29 + 0.5F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$29 + 0.0F), (double) ($$24 + 0.0F), (double) ($$28 + 0.0F)).uv(($$27 + (float) $$29 + 0.5F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(-1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }
                    if ($$25 <= 1) {
                        for (int $$30 = 0; $$30 < 8; $$30++) {
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$30 + 1.0F - 9.765625E-4F), (double) ($$24 + 0.0F), (double) ($$28 + 8.0F)).uv(($$27 + (float) $$30 + 0.5F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$30 + 1.0F - 9.765625E-4F), (double) ($$24 + 4.0F), (double) ($$28 + 8.0F)).uv(($$27 + (float) $$30 + 0.5F) * 0.00390625F + $$10, ($$28 + 8.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$30 + 1.0F - 9.765625E-4F), (double) ($$24 + 4.0F), (double) ($$28 + 0.0F)).uv(($$27 + (float) $$30 + 0.5F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + (float) $$30 + 1.0F - 9.765625E-4F), (double) ($$24 + 0.0F), (double) ($$28 + 0.0F)).uv(($$27 + (float) $$30 + 0.5F) * 0.00390625F + $$10, ($$28 + 0.0F) * 0.00390625F + $$11).color($$15, $$16, $$17, 0.8F).normal(1.0F, 0.0F, 0.0F).endVertex();
                        }
                    }
                    if ($$26 > -1) {
                        for (int $$31 = 0; $$31 < 8; $$31++) {
                            bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 4.0F), (double) ($$28 + (float) $$31 + 0.0F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + (float) $$31 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 4.0F), (double) ($$28 + (float) $$31 + 0.0F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + (float) $$31 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 0.0F), (double) ($$28 + (float) $$31 + 0.0F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + (float) $$31 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 0.0F), (double) ($$28 + (float) $$31 + 0.0F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + (float) $$31 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, -1.0F).endVertex();
                        }
                    }
                    if ($$26 <= 1) {
                        for (int $$32 = 0; $$32 < 8; $$32++) {
                            bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 4.0F), (double) ($$28 + (float) $$32 + 1.0F - 9.765625E-4F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + (float) $$32 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 4.0F), (double) ($$28 + (float) $$32 + 1.0F - 9.765625E-4F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + (float) $$32 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + 8.0F), (double) ($$24 + 0.0F), (double) ($$28 + (float) $$32 + 1.0F - 9.765625E-4F)).uv(($$27 + 8.0F) * 0.00390625F + $$10, ($$28 + (float) $$32 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                            bufferBuilder0.m_5483_((double) ($$27 + 0.0F), (double) ($$24 + 0.0F), (double) ($$28 + (float) $$32 + 1.0F - 9.765625E-4F)).uv(($$27 + 0.0F) * 0.00390625F + $$10, ($$28 + (float) $$32 + 0.5F) * 0.00390625F + $$11).color($$21, $$22, $$23, 0.8F).normal(0.0F, 0.0F, 1.0F).endVertex();
                        }
                    }
                }
            }
        } else {
            int $$33 = 1;
            int $$34 = 32;
            for (int $$35 = -32; $$35 < 32; $$35 += 32) {
                for (int $$36 = -32; $$36 < 32; $$36 += 32) {
                    bufferBuilder0.m_5483_((double) ($$35 + 0), (double) $$24, (double) ($$36 + 32)).uv((float) ($$35 + 0) * 0.00390625F + $$10, (float) ($$36 + 32) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferBuilder0.m_5483_((double) ($$35 + 32), (double) $$24, (double) ($$36 + 32)).uv((float) ($$35 + 32) * 0.00390625F + $$10, (float) ($$36 + 32) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferBuilder0.m_5483_((double) ($$35 + 32), (double) $$24, (double) ($$36 + 0)).uv((float) ($$35 + 32) * 0.00390625F + $$10, (float) ($$36 + 0) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                    bufferBuilder0.m_5483_((double) ($$35 + 0), (double) $$24, (double) ($$36 + 0)).uv((float) ($$35 + 0) * 0.00390625F + $$10, (float) ($$36 + 0) * 0.00390625F + $$11).color($$12, $$13, $$14, 0.8F).normal(0.0F, -1.0F, 0.0F).endVertex();
                }
            }
        }
        return bufferBuilder0.end();
    }

    private void compileChunks(Camera camera0) {
        this.minecraft.getProfiler().push("populate_chunks_to_compile");
        LevelLightEngine $$1 = this.level.m_5518_();
        RenderRegionCache $$2 = new RenderRegionCache();
        BlockPos $$3 = camera0.getBlockPosition();
        List<ChunkRenderDispatcher.RenderChunk> $$4 = Lists.newArrayList();
        ObjectListIterator var6 = this.renderChunksInFrustum.iterator();
        while (var6.hasNext()) {
            LevelRenderer.RenderChunkInfo $$5 = (LevelRenderer.RenderChunkInfo) var6.next();
            ChunkRenderDispatcher.RenderChunk $$6 = $$5.chunk;
            SectionPos $$7 = SectionPos.of($$6.getOrigin());
            if ($$6.isDirty() && $$1.lightOnInSection($$7)) {
                boolean $$8 = false;
                if (this.minecraft.options.prioritizeChunkUpdates().get() == PrioritizeChunkUpdates.NEARBY) {
                    BlockPos $$9 = $$6.getOrigin().offset(8, 8, 8);
                    $$8 = $$9.m_123331_($$3) < 768.0 || $$6.isDirtyFromPlayer();
                } else if (this.minecraft.options.prioritizeChunkUpdates().get() == PrioritizeChunkUpdates.PLAYER_AFFECTED) {
                    $$8 = $$6.isDirtyFromPlayer();
                }
                if ($$8) {
                    this.minecraft.getProfiler().push("build_near_sync");
                    this.chunkRenderDispatcher.rebuildChunkSync($$6, $$2);
                    $$6.setNotDirty();
                    this.minecraft.getProfiler().pop();
                } else {
                    $$4.add($$6);
                }
            }
        }
        this.minecraft.getProfiler().popPush("upload");
        this.chunkRenderDispatcher.uploadAllPendingUploads();
        this.minecraft.getProfiler().popPush("schedule_async_compile");
        for (ChunkRenderDispatcher.RenderChunk $$10 : $$4) {
            $$10.rebuildChunkAsync(this.chunkRenderDispatcher, $$2);
            $$10.setNotDirty();
        }
        this.minecraft.getProfiler().pop();
    }

    private void renderWorldBorder(Camera camera0) {
        BufferBuilder $$1 = Tesselator.getInstance().getBuilder();
        WorldBorder $$2 = this.level.m_6857_();
        double $$3 = (double) (this.minecraft.options.getEffectiveRenderDistance() * 16);
        if (!(camera0.getPosition().x < $$2.getMaxX() - $$3) || !(camera0.getPosition().x > $$2.getMinX() + $$3) || !(camera0.getPosition().z < $$2.getMaxZ() - $$3) || !(camera0.getPosition().z > $$2.getMinZ() + $$3)) {
            double $$4 = 1.0 - $$2.getDistanceToBorder(camera0.getPosition().x, camera0.getPosition().z) / $$3;
            $$4 = Math.pow($$4, 4.0);
            $$4 = Mth.clamp($$4, 0.0, 1.0);
            double $$5 = camera0.getPosition().x;
            double $$6 = camera0.getPosition().z;
            double $$7 = (double) this.minecraft.gameRenderer.getDepthFar();
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            RenderSystem.setShaderTexture(0, FORCEFIELD_LOCATION);
            RenderSystem.depthMask(Minecraft.useShaderTransparency());
            PoseStack $$8 = RenderSystem.getModelViewStack();
            $$8.pushPose();
            RenderSystem.applyModelViewMatrix();
            int $$9 = $$2.getStatus().getColor();
            float $$10 = (float) ($$9 >> 16 & 0xFF) / 255.0F;
            float $$11 = (float) ($$9 >> 8 & 0xFF) / 255.0F;
            float $$12 = (float) ($$9 & 0xFF) / 255.0F;
            RenderSystem.setShaderColor($$10, $$11, $$12, (float) $$4);
            RenderSystem.setShader(GameRenderer::m_172817_);
            RenderSystem.polygonOffset(-3.0F, -3.0F);
            RenderSystem.enablePolygonOffset();
            RenderSystem.disableCull();
            float $$13 = (float) (Util.getMillis() % 3000L) / 3000.0F;
            float $$14 = (float) (-Mth.frac(camera0.getPosition().y * 0.5));
            float $$15 = $$14 + (float) $$7;
            $$1.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
            double $$16 = Math.max((double) Mth.floor($$6 - $$3), $$2.getMinZ());
            double $$17 = Math.min((double) Mth.ceil($$6 + $$3), $$2.getMaxZ());
            float $$18 = (float) (Mth.floor($$16) & 1) * 0.5F;
            if ($$5 > $$2.getMaxX() - $$3) {
                float $$19 = $$18;
                for (double $$20 = $$16; $$20 < $$17; $$19 += 0.5F) {
                    double $$21 = Math.min(1.0, $$17 - $$20);
                    float $$22 = (float) $$21 * 0.5F;
                    $$1.m_5483_($$2.getMaxX() - $$5, -$$7, $$20 - $$6).uv($$13 - $$19, $$13 + $$15).endVertex();
                    $$1.m_5483_($$2.getMaxX() - $$5, -$$7, $$20 + $$21 - $$6).uv($$13 - ($$22 + $$19), $$13 + $$15).endVertex();
                    $$1.m_5483_($$2.getMaxX() - $$5, $$7, $$20 + $$21 - $$6).uv($$13 - ($$22 + $$19), $$13 + $$14).endVertex();
                    $$1.m_5483_($$2.getMaxX() - $$5, $$7, $$20 - $$6).uv($$13 - $$19, $$13 + $$14).endVertex();
                    $$20++;
                }
            }
            if ($$5 < $$2.getMinX() + $$3) {
                float $$23 = $$18;
                for (double $$24 = $$16; $$24 < $$17; $$23 += 0.5F) {
                    double $$25 = Math.min(1.0, $$17 - $$24);
                    float $$26 = (float) $$25 * 0.5F;
                    $$1.m_5483_($$2.getMinX() - $$5, -$$7, $$24 - $$6).uv($$13 + $$23, $$13 + $$15).endVertex();
                    $$1.m_5483_($$2.getMinX() - $$5, -$$7, $$24 + $$25 - $$6).uv($$13 + $$26 + $$23, $$13 + $$15).endVertex();
                    $$1.m_5483_($$2.getMinX() - $$5, $$7, $$24 + $$25 - $$6).uv($$13 + $$26 + $$23, $$13 + $$14).endVertex();
                    $$1.m_5483_($$2.getMinX() - $$5, $$7, $$24 - $$6).uv($$13 + $$23, $$13 + $$14).endVertex();
                    $$24++;
                }
            }
            $$16 = Math.max((double) Mth.floor($$5 - $$3), $$2.getMinX());
            $$17 = Math.min((double) Mth.ceil($$5 + $$3), $$2.getMaxX());
            $$18 = (float) (Mth.floor($$16) & 1) * 0.5F;
            if ($$6 > $$2.getMaxZ() - $$3) {
                float $$27 = $$18;
                for (double $$28 = $$16; $$28 < $$17; $$27 += 0.5F) {
                    double $$29 = Math.min(1.0, $$17 - $$28);
                    float $$30 = (float) $$29 * 0.5F;
                    $$1.m_5483_($$28 - $$5, -$$7, $$2.getMaxZ() - $$6).uv($$13 + $$27, $$13 + $$15).endVertex();
                    $$1.m_5483_($$28 + $$29 - $$5, -$$7, $$2.getMaxZ() - $$6).uv($$13 + $$30 + $$27, $$13 + $$15).endVertex();
                    $$1.m_5483_($$28 + $$29 - $$5, $$7, $$2.getMaxZ() - $$6).uv($$13 + $$30 + $$27, $$13 + $$14).endVertex();
                    $$1.m_5483_($$28 - $$5, $$7, $$2.getMaxZ() - $$6).uv($$13 + $$27, $$13 + $$14).endVertex();
                    $$28++;
                }
            }
            if ($$6 < $$2.getMinZ() + $$3) {
                float $$31 = $$18;
                for (double $$32 = $$16; $$32 < $$17; $$31 += 0.5F) {
                    double $$33 = Math.min(1.0, $$17 - $$32);
                    float $$34 = (float) $$33 * 0.5F;
                    $$1.m_5483_($$32 - $$5, -$$7, $$2.getMinZ() - $$6).uv($$13 - $$31, $$13 + $$15).endVertex();
                    $$1.m_5483_($$32 + $$33 - $$5, -$$7, $$2.getMinZ() - $$6).uv($$13 - ($$34 + $$31), $$13 + $$15).endVertex();
                    $$1.m_5483_($$32 + $$33 - $$5, $$7, $$2.getMinZ() - $$6).uv($$13 - ($$34 + $$31), $$13 + $$14).endVertex();
                    $$1.m_5483_($$32 - $$5, $$7, $$2.getMinZ() - $$6).uv($$13 - $$31, $$13 + $$14).endVertex();
                    $$32++;
                }
            }
            BufferUploader.drawWithShader($$1.end());
            RenderSystem.enableCull();
            RenderSystem.polygonOffset(0.0F, 0.0F);
            RenderSystem.disablePolygonOffset();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
            $$8.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.depthMask(true);
        }
    }

    private void renderHitOutline(PoseStack poseStack0, VertexConsumer vertexConsumer1, Entity entity2, double double3, double double4, double double5, BlockPos blockPos6, BlockState blockState7) {
        renderShape(poseStack0, vertexConsumer1, blockState7.m_60651_(this.level, blockPos6, CollisionContext.of(entity2)), (double) blockPos6.m_123341_() - double3, (double) blockPos6.m_123342_() - double4, (double) blockPos6.m_123343_() - double5, 0.0F, 0.0F, 0.0F, 0.4F);
    }

    private static Vec3 mixColor(float float0) {
        float $$1 = 5.99999F;
        int $$2 = (int) (Mth.clamp(float0, 0.0F, 1.0F) * 5.99999F);
        float $$3 = float0 * 5.99999F - (float) $$2;
        return switch($$2) {
            case 0 ->
                new Vec3(1.0, (double) $$3, 0.0);
            case 1 ->
                new Vec3((double) (1.0F - $$3), 1.0, 0.0);
            case 2 ->
                new Vec3(0.0, 1.0, (double) $$3);
            case 3 ->
                new Vec3(0.0, 1.0 - (double) $$3, 1.0);
            case 4 ->
                new Vec3((double) $$3, 0.0, 1.0);
            case 5 ->
                new Vec3(1.0, 0.0, 1.0 - (double) $$3);
            default ->
                throw new IllegalStateException("Unexpected value: " + $$2);
        };
    }

    private static Vec3 shiftHue(float float0, float float1, float float2, float float3) {
        Vec3 $$4 = mixColor(float3).scale((double) float0);
        Vec3 $$5 = mixColor((float3 + 0.33333334F) % 1.0F).scale((double) float1);
        Vec3 $$6 = mixColor((float3 + 0.6666667F) % 1.0F).scale((double) float2);
        Vec3 $$7 = $$4.add($$5).add($$6);
        double $$8 = Math.max(Math.max(1.0, $$7.x), Math.max($$7.y, $$7.z));
        return new Vec3($$7.x / $$8, $$7.y / $$8, $$7.z / $$8);
    }

    public static void renderVoxelShape(PoseStack poseStack0, VertexConsumer vertexConsumer1, VoxelShape voxelShape2, double double3, double double4, double double5, float float6, float float7, float float8, float float9, boolean boolean10) {
        List<AABB> $$11 = voxelShape2.toAabbs();
        if (!$$11.isEmpty()) {
            int $$12 = boolean10 ? $$11.size() : $$11.size() * 8;
            renderShape(poseStack0, vertexConsumer1, Shapes.create((AABB) $$11.get(0)), double3, double4, double5, float6, float7, float8, float9);
            for (int $$13 = 1; $$13 < $$11.size(); $$13++) {
                AABB $$14 = (AABB) $$11.get($$13);
                float $$15 = (float) $$13 / (float) $$12;
                Vec3 $$16 = shiftHue(float6, float7, float8, $$15);
                renderShape(poseStack0, vertexConsumer1, Shapes.create($$14), double3, double4, double5, (float) $$16.x, (float) $$16.y, (float) $$16.z, float9);
            }
        }
    }

    private static void renderShape(PoseStack poseStack0, VertexConsumer vertexConsumer1, VoxelShape voxelShape2, double double3, double double4, double double5, float float6, float float7, float float8, float float9) {
        PoseStack.Pose $$10 = poseStack0.last();
        voxelShape2.forAllEdges((p_234280_, p_234281_, p_234282_, p_234283_, p_234284_, p_234285_) -> {
            float $$15 = (float) (p_234283_ - p_234280_);
            float $$16 = (float) (p_234284_ - p_234281_);
            float $$17 = (float) (p_234285_ - p_234282_);
            float $$18 = Mth.sqrt($$15 * $$15 + $$16 * $$16 + $$17 * $$17);
            $$15 /= $$18;
            $$16 /= $$18;
            $$17 /= $$18;
            vertexConsumer1.vertex($$10.pose(), (float) (p_234280_ + double3), (float) (p_234281_ + double4), (float) (p_234282_ + double5)).color(float6, float7, float8, float9).normal($$10.normal(), $$15, $$16, $$17).endVertex();
            vertexConsumer1.vertex($$10.pose(), (float) (p_234283_ + double3), (float) (p_234284_ + double4), (float) (p_234285_ + double5)).color(float6, float7, float8, float9).normal($$10.normal(), $$15, $$16, $$17).endVertex();
        });
    }

    public static void renderLineBox(VertexConsumer vertexConsumer0, double double1, double double2, double double3, double double4, double double5, double double6, float float7, float float8, float float9, float float10) {
        renderLineBox(new PoseStack(), vertexConsumer0, double1, double2, double3, double4, double5, double6, float7, float8, float9, float10, float7, float8, float9);
    }

    public static void renderLineBox(PoseStack poseStack0, VertexConsumer vertexConsumer1, AABB aABB2, float float3, float float4, float float5, float float6) {
        renderLineBox(poseStack0, vertexConsumer1, aABB2.minX, aABB2.minY, aABB2.minZ, aABB2.maxX, aABB2.maxY, aABB2.maxZ, float3, float4, float5, float6, float3, float4, float5);
    }

    public static void renderLineBox(PoseStack poseStack0, VertexConsumer vertexConsumer1, double double2, double double3, double double4, double double5, double double6, double double7, float float8, float float9, float float10, float float11) {
        renderLineBox(poseStack0, vertexConsumer1, double2, double3, double4, double5, double6, double7, float8, float9, float10, float11, float8, float9, float10);
    }

    public static void renderLineBox(PoseStack poseStack0, VertexConsumer vertexConsumer1, double double2, double double3, double double4, double double5, double double6, double double7, float float8, float float9, float float10, float float11, float float12, float float13, float float14) {
        Matrix4f $$15 = poseStack0.last().pose();
        Matrix3f $$16 = poseStack0.last().normal();
        float $$17 = (float) double2;
        float $$18 = (float) double3;
        float $$19 = (float) double4;
        float $$20 = (float) double5;
        float $$21 = (float) double6;
        float $$22 = (float) double7;
        vertexConsumer1.vertex($$15, $$17, $$18, $$19).color(float8, float13, float14, float11).normal($$16, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$18, $$19).color(float8, float13, float14, float11).normal($$16, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$18, $$19).color(float12, float9, float14, float11).normal($$16, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$21, $$19).color(float12, float9, float14, float11).normal($$16, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$18, $$19).color(float12, float13, float10, float11).normal($$16, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$18, $$22).color(float12, float13, float10, float11).normal($$16, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$18, $$19).color(float8, float9, float10, float11).normal($$16, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$21, $$19).color(float8, float9, float10, float11).normal($$16, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$21, $$19).color(float8, float9, float10, float11).normal($$16, -1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$21, $$19).color(float8, float9, float10, float11).normal($$16, -1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$21, $$19).color(float8, float9, float10, float11).normal($$16, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$21, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$21, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$18, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$18, $$22).color(float8, float9, float10, float11).normal($$16, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$18, $$22).color(float8, float9, float10, float11).normal($$16, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$18, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, 0.0F, -1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$18, $$19).color(float8, float9, float10, float11).normal($$16, 0.0F, 0.0F, -1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$17, $$21, $$22).color(float8, float9, float10, float11).normal($$16, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$21, $$22).color(float8, float9, float10, float11).normal($$16, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$18, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$21, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$21, $$19).color(float8, float9, float10, float11).normal($$16, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer1.vertex($$15, $$20, $$21, $$22).color(float8, float9, float10, float11).normal($$16, 0.0F, 0.0F, 1.0F).endVertex();
    }

    public static void addChainedFilledBoxVertices(PoseStack poseStack0, VertexConsumer vertexConsumer1, double double2, double double3, double double4, double double5, double double6, double double7, float float8, float float9, float float10, float float11) {
        addChainedFilledBoxVertices(poseStack0, vertexConsumer1, (float) double2, (float) double3, (float) double4, (float) double5, (float) double6, (float) double7, float8, float9, float10, float11);
    }

    public static void addChainedFilledBoxVertices(PoseStack poseStack0, VertexConsumer vertexConsumer1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, float float9, float float10, float float11) {
        Matrix4f $$12 = poseStack0.last().pose();
        vertexConsumer1.vertex($$12, float2, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float3, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float2, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float4).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float7).color(float8, float9, float10, float11).endVertex();
        vertexConsumer1.vertex($$12, float5, float6, float7).color(float8, float9, float10, float11).endVertex();
    }

    public void blockChanged(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, BlockState blockState3, int int4) {
        this.setBlockDirty(blockPos1, (int4 & 8) != 0);
    }

    private void setBlockDirty(BlockPos blockPos0, boolean boolean1) {
        for (int $$2 = blockPos0.m_123343_() - 1; $$2 <= blockPos0.m_123343_() + 1; $$2++) {
            for (int $$3 = blockPos0.m_123341_() - 1; $$3 <= blockPos0.m_123341_() + 1; $$3++) {
                for (int $$4 = blockPos0.m_123342_() - 1; $$4 <= blockPos0.m_123342_() + 1; $$4++) {
                    this.setSectionDirty(SectionPos.blockToSectionCoord($$3), SectionPos.blockToSectionCoord($$4), SectionPos.blockToSectionCoord($$2), boolean1);
                }
            }
        }
    }

    public void setBlocksDirty(int int0, int int1, int int2, int int3, int int4, int int5) {
        for (int $$6 = int2 - 1; $$6 <= int5 + 1; $$6++) {
            for (int $$7 = int0 - 1; $$7 <= int3 + 1; $$7++) {
                for (int $$8 = int1 - 1; $$8 <= int4 + 1; $$8++) {
                    this.setSectionDirty(SectionPos.blockToSectionCoord($$7), SectionPos.blockToSectionCoord($$8), SectionPos.blockToSectionCoord($$6));
                }
            }
        }
    }

    public void setBlockDirty(BlockPos blockPos0, BlockState blockState1, BlockState blockState2) {
        if (this.minecraft.getModelManager().requiresRender(blockState1, blockState2)) {
            this.setBlocksDirty(blockPos0.m_123341_(), blockPos0.m_123342_(), blockPos0.m_123343_(), blockPos0.m_123341_(), blockPos0.m_123342_(), blockPos0.m_123343_());
        }
    }

    public void setSectionDirtyWithNeighbors(int int0, int int1, int int2) {
        for (int $$3 = int2 - 1; $$3 <= int2 + 1; $$3++) {
            for (int $$4 = int0 - 1; $$4 <= int0 + 1; $$4++) {
                for (int $$5 = int1 - 1; $$5 <= int1 + 1; $$5++) {
                    this.setSectionDirty($$4, $$5, $$3);
                }
            }
        }
    }

    public void setSectionDirty(int int0, int int1, int int2) {
        this.setSectionDirty(int0, int1, int2, false);
    }

    private void setSectionDirty(int int0, int int1, int int2, boolean boolean3) {
        this.viewArea.setDirty(int0, int1, int2, boolean3);
    }

    public void playStreamingMusic(@Nullable SoundEvent soundEvent0, BlockPos blockPos1) {
        SoundInstance $$2 = (SoundInstance) this.playingRecords.get(blockPos1);
        if ($$2 != null) {
            this.minecraft.getSoundManager().stop($$2);
            this.playingRecords.remove(blockPos1);
        }
        if (soundEvent0 != null) {
            RecordItem $$3 = RecordItem.getBySound(soundEvent0);
            if ($$3 != null) {
                this.minecraft.gui.setNowPlaying($$3.getDisplayName());
            }
            SoundInstance var5 = SimpleSoundInstance.forRecord(soundEvent0, Vec3.atCenterOf(blockPos1));
            this.playingRecords.put(blockPos1, var5);
            this.minecraft.getSoundManager().play(var5);
        }
        this.notifyNearbyEntities(this.level, blockPos1, soundEvent0 != null);
    }

    private void notifyNearbyEntities(Level level0, BlockPos blockPos1, boolean boolean2) {
        for (LivingEntity $$4 : level0.m_45976_(LivingEntity.class, new AABB(blockPos1).inflate(3.0))) {
            $$4.setRecordPlayingNearby(blockPos1, boolean2);
        }
    }

    public void addParticle(ParticleOptions particleOptions0, boolean boolean1, double double2, double double3, double double4, double double5, double double6, double double7) {
        this.addParticle(particleOptions0, boolean1, false, double2, double3, double4, double5, double6, double7);
    }

    public void addParticle(ParticleOptions particleOptions0, boolean boolean1, boolean boolean2, double double3, double double4, double double5, double double6, double double7, double double8) {
        try {
            this.addParticleInternal(particleOptions0, boolean1, boolean2, double3, double4, double5, double6, double7, double8);
        } catch (Throwable var19) {
            CrashReport $$10 = CrashReport.forThrowable(var19, "Exception while adding particle");
            CrashReportCategory $$11 = $$10.addCategory("Particle being added");
            $$11.setDetail("ID", BuiltInRegistries.PARTICLE_TYPE.getKey(particleOptions0.getType()));
            $$11.setDetail("Parameters", particleOptions0.writeToString());
            $$11.setDetail("Position", (CrashReportDetail<String>) (() -> CrashReportCategory.formatLocation(this.level, double3, double4, double5)));
            throw new ReportedException($$10);
        }
    }

    private <T extends ParticleOptions> void addParticle(T t0, double double1, double double2, double double3, double double4, double double5, double double6) {
        this.addParticle(t0, t0.getType().getOverrideLimiter(), double1, double2, double3, double4, double5, double6);
    }

    @Nullable
    private Particle addParticleInternal(ParticleOptions particleOptions0, boolean boolean1, double double2, double double3, double double4, double double5, double double6, double double7) {
        return this.addParticleInternal(particleOptions0, boolean1, false, double2, double3, double4, double5, double6, double7);
    }

    @Nullable
    private Particle addParticleInternal(ParticleOptions particleOptions0, boolean boolean1, boolean boolean2, double double3, double double4, double double5, double double6, double double7, double double8) {
        Camera $$9 = this.minecraft.gameRenderer.getMainCamera();
        ParticleStatus $$10 = this.calculateParticleLevel(boolean2);
        if (boolean1) {
            return this.minecraft.particleEngine.createParticle(particleOptions0, double3, double4, double5, double6, double7, double8);
        } else if ($$9.getPosition().distanceToSqr(double3, double4, double5) > 1024.0) {
            return null;
        } else {
            return $$10 == ParticleStatus.MINIMAL ? null : this.minecraft.particleEngine.createParticle(particleOptions0, double3, double4, double5, double6, double7, double8);
        }
    }

    private ParticleStatus calculateParticleLevel(boolean boolean0) {
        ParticleStatus $$1 = this.minecraft.options.particles().get();
        if (boolean0 && $$1 == ParticleStatus.MINIMAL && this.level.f_46441_.nextInt(10) == 0) {
            $$1 = ParticleStatus.DECREASED;
        }
        if ($$1 == ParticleStatus.DECREASED && this.level.f_46441_.nextInt(3) == 0) {
            $$1 = ParticleStatus.MINIMAL;
        }
        return $$1;
    }

    public void clear() {
    }

    public void globalLevelEvent(int int0, BlockPos blockPos1, int int2) {
        switch(int0) {
            case 1023:
            case 1028:
            case 1038:
                Camera $$3 = this.minecraft.gameRenderer.getMainCamera();
                if ($$3.isInitialized()) {
                    double $$4 = (double) blockPos1.m_123341_() - $$3.getPosition().x;
                    double $$5 = (double) blockPos1.m_123342_() - $$3.getPosition().y;
                    double $$6 = (double) blockPos1.m_123343_() - $$3.getPosition().z;
                    double $$7 = Math.sqrt($$4 * $$4 + $$5 * $$5 + $$6 * $$6);
                    double $$8 = $$3.getPosition().x;
                    double $$9 = $$3.getPosition().y;
                    double $$10 = $$3.getPosition().z;
                    if ($$7 > 0.0) {
                        $$8 += $$4 / $$7 * 2.0;
                        $$9 += $$5 / $$7 * 2.0;
                        $$10 += $$6 / $$7 * 2.0;
                    }
                    if (int0 == 1023) {
                        this.level.playLocalSound($$8, $$9, $$10, SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F, false);
                    } else if (int0 == 1038) {
                        this.level.playLocalSound($$8, $$9, $$10, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.0F, 1.0F, false);
                    } else {
                        this.level.playLocalSound($$8, $$9, $$10, SoundEvents.ENDER_DRAGON_DEATH, SoundSource.HOSTILE, 5.0F, 1.0F, false);
                    }
                }
        }
    }

    public void levelEvent(int int0, BlockPos blockPos1, int int2) {
        RandomSource $$3 = this.level.f_46441_;
        switch(int0) {
            case 1000:
                this.level.m_245747_(blockPos1, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 1001:
                this.level.m_245747_(blockPos1, SoundEvents.DISPENSER_FAIL, SoundSource.BLOCKS, 1.0F, 1.2F, false);
                break;
            case 1002:
                this.level.m_245747_(blockPos1, SoundEvents.DISPENSER_LAUNCH, SoundSource.BLOCKS, 1.0F, 1.2F, false);
                break;
            case 1003:
                this.level.m_245747_(blockPos1, SoundEvents.ENDER_EYE_LAUNCH, SoundSource.NEUTRAL, 1.0F, 1.2F, false);
                break;
            case 1004:
                this.level.m_245747_(blockPos1, SoundEvents.FIREWORK_ROCKET_SHOOT, SoundSource.NEUTRAL, 1.0F, 1.2F, false);
                break;
            case 1009:
                if (int2 == 0) {
                    this.level.m_245747_(blockPos1, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + ($$3.nextFloat() - $$3.nextFloat()) * 0.8F, false);
                } else if (int2 == 1) {
                    this.level.m_245747_(blockPos1, SoundEvents.GENERIC_EXTINGUISH_FIRE, SoundSource.BLOCKS, 0.7F, 1.6F + ($$3.nextFloat() - $$3.nextFloat()) * 0.4F, false);
                }
                break;
            case 1010:
                if (Item.byId(int2) instanceof RecordItem $$87) {
                    this.playStreamingMusic($$87.getSound(), blockPos1);
                }
                break;
            case 1011:
                this.playStreamingMusic(null, blockPos1);
                break;
            case 1015:
                this.level.m_245747_(blockPos1, SoundEvents.GHAST_WARN, SoundSource.HOSTILE, 10.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1016:
                this.level.m_245747_(blockPos1, SoundEvents.GHAST_SHOOT, SoundSource.HOSTILE, 10.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1017:
                this.level.m_245747_(blockPos1, SoundEvents.ENDER_DRAGON_SHOOT, SoundSource.HOSTILE, 10.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1018:
                this.level.m_245747_(blockPos1, SoundEvents.BLAZE_SHOOT, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1019:
                this.level.m_245747_(blockPos1, SoundEvents.ZOMBIE_ATTACK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1020:
                this.level.m_245747_(blockPos1, SoundEvents.ZOMBIE_ATTACK_IRON_DOOR, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1021:
                this.level.m_245747_(blockPos1, SoundEvents.ZOMBIE_BREAK_WOODEN_DOOR, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1022:
                this.level.m_245747_(blockPos1, SoundEvents.WITHER_BREAK_BLOCK, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1024:
                this.level.m_245747_(blockPos1, SoundEvents.WITHER_SHOOT, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1025:
                this.level.m_245747_(blockPos1, SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 0.05F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1026:
                this.level.m_245747_(blockPos1, SoundEvents.ZOMBIE_INFECT, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1027:
                this.level.m_245747_(blockPos1, SoundEvents.ZOMBIE_VILLAGER_CONVERTED, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1029:
                this.level.m_245747_(blockPos1, SoundEvents.ANVIL_DESTROY, SoundSource.BLOCKS, 1.0F, $$3.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1030:
                this.level.m_245747_(blockPos1, SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, $$3.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1031:
                this.level.m_245747_(blockPos1, SoundEvents.ANVIL_LAND, SoundSource.BLOCKS, 0.3F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1032:
                this.minecraft.getSoundManager().play(SimpleSoundInstance.forLocalAmbience(SoundEvents.PORTAL_TRAVEL, $$3.nextFloat() * 0.4F + 0.8F, 0.25F));
                break;
            case 1033:
                this.level.m_245747_(blockPos1, SoundEvents.CHORUS_FLOWER_GROW, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 1034:
                this.level.m_245747_(blockPos1, SoundEvents.CHORUS_FLOWER_DEATH, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 1035:
                this.level.m_245747_(blockPos1, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 1039:
                this.level.m_245747_(blockPos1, SoundEvents.PHANTOM_BITE, SoundSource.HOSTILE, 0.3F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1040:
                this.level.m_245747_(blockPos1, SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1041:
                this.level.m_245747_(blockPos1, SoundEvents.HUSK_CONVERTED_TO_ZOMBIE, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1042:
                this.level.m_245747_(blockPos1, SoundEvents.GRINDSTONE_USE, SoundSource.BLOCKS, 1.0F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1043:
                this.level.m_245747_(blockPos1, SoundEvents.BOOK_PAGE_TURN, SoundSource.BLOCKS, 1.0F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1044:
                this.level.m_245747_(blockPos1, SoundEvents.SMITHING_TABLE_USE, SoundSource.BLOCKS, 1.0F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1045:
                this.level.m_245747_(blockPos1, SoundEvents.POINTED_DRIPSTONE_LAND, SoundSource.BLOCKS, 2.0F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1046:
                this.level.m_245747_(blockPos1, SoundEvents.POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1047:
                this.level.m_245747_(blockPos1, SoundEvents.POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON, SoundSource.BLOCKS, 2.0F, this.level.f_46441_.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 1048:
                this.level.m_245747_(blockPos1, SoundEvents.SKELETON_CONVERTED_TO_STRAY, SoundSource.HOSTILE, 2.0F, ($$3.nextFloat() - $$3.nextFloat()) * 0.2F + 1.0F, false);
                break;
            case 1500:
                ComposterBlock.handleFill(this.level, blockPos1, int2 > 0);
                break;
            case 1501:
                this.level.m_245747_(blockPos1, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + ($$3.nextFloat() - $$3.nextFloat()) * 0.8F, false);
                for (int $$70 = 0; $$70 < 8; $$70++) {
                    this.level.addParticle(ParticleTypes.LARGE_SMOKE, (double) blockPos1.m_123341_() + $$3.nextDouble(), (double) blockPos1.m_123342_() + 1.2, (double) blockPos1.m_123343_() + $$3.nextDouble(), 0.0, 0.0, 0.0);
                }
                break;
            case 1502:
                this.level.m_245747_(blockPos1, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.BLOCKS, 0.5F, 2.6F + ($$3.nextFloat() - $$3.nextFloat()) * 0.8F, false);
                for (int $$71 = 0; $$71 < 5; $$71++) {
                    double $$72 = (double) blockPos1.m_123341_() + $$3.nextDouble() * 0.6 + 0.2;
                    double $$73 = (double) blockPos1.m_123342_() + $$3.nextDouble() * 0.6 + 0.2;
                    double $$74 = (double) blockPos1.m_123343_() + $$3.nextDouble() * 0.6 + 0.2;
                    this.level.addParticle(ParticleTypes.SMOKE, $$72, $$73, $$74, 0.0, 0.0, 0.0);
                }
                break;
            case 1503:
                this.level.m_245747_(blockPos1, SoundEvents.END_PORTAL_FRAME_FILL, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                for (int $$75 = 0; $$75 < 16; $$75++) {
                    double $$76 = (double) blockPos1.m_123341_() + (5.0 + $$3.nextDouble() * 6.0) / 16.0;
                    double $$77 = (double) blockPos1.m_123342_() + 0.8125;
                    double $$78 = (double) blockPos1.m_123343_() + (5.0 + $$3.nextDouble() * 6.0) / 16.0;
                    this.level.addParticle(ParticleTypes.SMOKE, $$76, $$77, $$78, 0.0, 0.0, 0.0);
                }
                break;
            case 1504:
                PointedDripstoneBlock.spawnDripParticle(this.level, blockPos1, this.level.m_8055_(blockPos1));
                break;
            case 1505:
                BoneMealItem.addGrowthParticles(this.level, blockPos1, int2);
                this.level.m_245747_(blockPos1, SoundEvents.BONE_MEAL_USE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 2000:
                Direction $$4 = Direction.from3DDataValue(int2);
                int $$5 = $$4.getStepX();
                int $$6 = $$4.getStepY();
                int $$7 = $$4.getStepZ();
                double $$8 = (double) blockPos1.m_123341_() + (double) $$5 * 0.6 + 0.5;
                double $$9 = (double) blockPos1.m_123342_() + (double) $$6 * 0.6 + 0.5;
                double $$10 = (double) blockPos1.m_123343_() + (double) $$7 * 0.6 + 0.5;
                for (int $$11 = 0; $$11 < 10; $$11++) {
                    double $$12 = $$3.nextDouble() * 0.2 + 0.01;
                    double $$13 = $$8 + (double) $$5 * 0.01 + ($$3.nextDouble() - 0.5) * (double) $$7 * 0.5;
                    double $$14 = $$9 + (double) $$6 * 0.01 + ($$3.nextDouble() - 0.5) * (double) $$6 * 0.5;
                    double $$15 = $$10 + (double) $$7 * 0.01 + ($$3.nextDouble() - 0.5) * (double) $$5 * 0.5;
                    double $$16 = (double) $$5 * $$12 + $$3.nextGaussian() * 0.01;
                    double $$17 = (double) $$6 * $$12 + $$3.nextGaussian() * 0.01;
                    double $$18 = (double) $$7 * $$12 + $$3.nextGaussian() * 0.01;
                    this.addParticle(ParticleTypes.SMOKE, $$13, $$14, $$15, $$16, $$17, $$18);
                }
                break;
            case 2001:
                BlockState $$38 = Block.stateById(int2);
                if (!$$38.m_60795_()) {
                    SoundType $$39 = $$38.m_60827_();
                    this.level.m_245747_(blockPos1, $$39.getBreakSound(), SoundSource.BLOCKS, ($$39.getVolume() + 1.0F) / 2.0F, $$39.getPitch() * 0.8F, false);
                }
                this.level.addDestroyBlockEffect(blockPos1, $$38);
                break;
            case 2002:
            case 2007:
                Vec3 $$24 = Vec3.atBottomCenterOf(blockPos1);
                for (int $$25 = 0; $$25 < 8; $$25++) {
                    this.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.SPLASH_POTION)), $$24.x, $$24.y, $$24.z, $$3.nextGaussian() * 0.15, $$3.nextDouble() * 0.2, $$3.nextGaussian() * 0.15);
                }
                float $$26 = (float) (int2 >> 16 & 0xFF) / 255.0F;
                float $$27 = (float) (int2 >> 8 & 0xFF) / 255.0F;
                float $$28 = (float) (int2 >> 0 & 0xFF) / 255.0F;
                ParticleOptions $$29 = int0 == 2007 ? ParticleTypes.INSTANT_EFFECT : ParticleTypes.EFFECT;
                for (int $$30 = 0; $$30 < 100; $$30++) {
                    double $$31 = $$3.nextDouble() * 4.0;
                    double $$32 = $$3.nextDouble() * Math.PI * 2.0;
                    double $$33 = Math.cos($$32) * $$31;
                    double $$34 = 0.01 + $$3.nextDouble() * 0.5;
                    double $$35 = Math.sin($$32) * $$31;
                    Particle $$36 = this.addParticleInternal($$29, $$29.getType().getOverrideLimiter(), $$24.x + $$33 * 0.1, $$24.y + 0.3, $$24.z + $$35 * 0.1, $$33, $$34, $$35);
                    if ($$36 != null) {
                        float $$37 = 0.75F + $$3.nextFloat() * 0.25F;
                        $$36.setColor($$26 * $$37, $$27 * $$37, $$28 * $$37);
                        $$36.setPower((float) $$31);
                    }
                }
                this.level.m_245747_(blockPos1, SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, $$3.nextFloat() * 0.1F + 0.9F, false);
                break;
            case 2003:
                double $$19 = (double) blockPos1.m_123341_() + 0.5;
                double $$20 = (double) blockPos1.m_123342_();
                double $$21 = (double) blockPos1.m_123343_() + 0.5;
                for (int $$22 = 0; $$22 < 8; $$22++) {
                    this.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.ENDER_EYE)), $$19, $$20, $$21, $$3.nextGaussian() * 0.15, $$3.nextDouble() * 0.2, $$3.nextGaussian() * 0.15);
                }
                for (double $$23 = 0.0; $$23 < Math.PI * 2; $$23 += Math.PI / 20) {
                    this.addParticle(ParticleTypes.PORTAL, $$19 + Math.cos($$23) * 5.0, $$20 - 0.4, $$21 + Math.sin($$23) * 5.0, Math.cos($$23) * -5.0, 0.0, Math.sin($$23) * -5.0);
                    this.addParticle(ParticleTypes.PORTAL, $$19 + Math.cos($$23) * 5.0, $$20 - 0.4, $$21 + Math.sin($$23) * 5.0, Math.cos($$23) * -7.0, 0.0, Math.sin($$23) * -7.0);
                }
                break;
            case 2004:
                for (int $$42 = 0; $$42 < 20; $$42++) {
                    double $$43 = (double) blockPos1.m_123341_() + 0.5 + ($$3.nextDouble() - 0.5) * 2.0;
                    double $$44 = (double) blockPos1.m_123342_() + 0.5 + ($$3.nextDouble() - 0.5) * 2.0;
                    double $$45 = (double) blockPos1.m_123343_() + 0.5 + ($$3.nextDouble() - 0.5) * 2.0;
                    this.level.addParticle(ParticleTypes.SMOKE, $$43, $$44, $$45, 0.0, 0.0, 0.0);
                    this.level.addParticle(ParticleTypes.FLAME, $$43, $$44, $$45, 0.0, 0.0, 0.0);
                }
                break;
            case 2005:
                BoneMealItem.addGrowthParticles(this.level, blockPos1, int2);
                break;
            case 2006:
                for (int $$79 = 0; $$79 < 200; $$79++) {
                    float $$80 = $$3.nextFloat() * 4.0F;
                    float $$81 = $$3.nextFloat() * (float) (Math.PI * 2);
                    double $$82 = (double) (Mth.cos($$81) * $$80);
                    double $$83 = 0.01 + $$3.nextDouble() * 0.5;
                    double $$84 = (double) (Mth.sin($$81) * $$80);
                    Particle $$85 = this.addParticleInternal(ParticleTypes.DRAGON_BREATH, false, (double) blockPos1.m_123341_() + $$82 * 0.1, (double) blockPos1.m_123342_() + 0.3, (double) blockPos1.m_123343_() + $$84 * 0.1, $$82, $$83, $$84);
                    if ($$85 != null) {
                        $$85.setPower($$80);
                    }
                }
                if (int2 == 1) {
                    this.level.m_245747_(blockPos1, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0F, $$3.nextFloat() * 0.1F + 0.9F, false);
                }
                break;
            case 2008:
                this.level.addParticle(ParticleTypes.EXPLOSION, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, 0.0, 0.0, 0.0);
                break;
            case 2009:
                for (int $$86 = 0; $$86 < 8; $$86++) {
                    this.level.addParticle(ParticleTypes.CLOUD, (double) blockPos1.m_123341_() + $$3.nextDouble(), (double) blockPos1.m_123342_() + 1.2, (double) blockPos1.m_123343_() + $$3.nextDouble(), 0.0, 0.0, 0.0);
                }
                break;
            case 3000:
                this.level.addParticle(ParticleTypes.EXPLOSION_EMITTER, true, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, 0.0, 0.0, 0.0);
                this.level.m_245747_(blockPos1, SoundEvents.END_GATEWAY_SPAWN, SoundSource.BLOCKS, 10.0F, (1.0F + (this.level.f_46441_.nextFloat() - this.level.f_46441_.nextFloat()) * 0.2F) * 0.7F, false);
                break;
            case 3001:
                this.level.m_245747_(blockPos1, SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 64.0F, 0.8F + this.level.f_46441_.nextFloat() * 0.3F, false);
                break;
            case 3002:
                if (int2 >= 0 && int2 < Direction.Axis.VALUES.length) {
                    ParticleUtils.spawnParticlesAlongAxis(Direction.Axis.VALUES[int2], this.level, blockPos1, 0.125, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(10, 19));
                } else {
                    ParticleUtils.spawnParticlesOnBlockFaces(this.level, blockPos1, ParticleTypes.ELECTRIC_SPARK, UniformInt.of(3, 5));
                }
                break;
            case 3003:
                ParticleUtils.spawnParticlesOnBlockFaces(this.level, blockPos1, ParticleTypes.WAX_ON, UniformInt.of(3, 5));
                this.level.m_245747_(blockPos1, SoundEvents.HONEYCOMB_WAX_ON, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                break;
            case 3004:
                ParticleUtils.spawnParticlesOnBlockFaces(this.level, blockPos1, ParticleTypes.WAX_OFF, UniformInt.of(3, 5));
                break;
            case 3005:
                ParticleUtils.spawnParticlesOnBlockFaces(this.level, blockPos1, ParticleTypes.SCRAPE, UniformInt.of(3, 5));
                break;
            case 3006:
                int $$46 = int2 >> 6;
                if ($$46 > 0) {
                    if ($$3.nextFloat() < 0.3F + (float) $$46 * 0.1F) {
                        float $$47 = 0.15F + 0.02F * (float) $$46 * (float) $$46 * $$3.nextFloat();
                        float $$48 = 0.4F + 0.3F * (float) $$46 * $$3.nextFloat();
                        this.level.m_245747_(blockPos1, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, $$47, $$48, false);
                    }
                    byte $$49 = (byte) (int2 & 63);
                    IntProvider $$50 = UniformInt.of(0, $$46);
                    float $$51 = 0.005F;
                    Supplier<Vec3> $$52 = () -> new Vec3(Mth.nextDouble($$3, -0.005F, 0.005F), Mth.nextDouble($$3, -0.005F, 0.005F), Mth.nextDouble($$3, -0.005F, 0.005F));
                    if ($$49 == 0) {
                        for (Direction $$53 : Direction.values()) {
                            float $$54 = $$53 == Direction.DOWN ? (float) Math.PI : 0.0F;
                            double $$55 = $$53.getAxis() == Direction.Axis.Y ? 0.65 : 0.57;
                            ParticleUtils.spawnParticlesOnBlockFace(this.level, blockPos1, new SculkChargeParticleOptions($$54), $$50, $$53, $$52, $$55);
                        }
                    } else {
                        for (Direction $$56 : MultifaceBlock.unpack($$49)) {
                            float $$57 = $$56 == Direction.UP ? (float) Math.PI : 0.0F;
                            double $$58 = 0.35;
                            ParticleUtils.spawnParticlesOnBlockFace(this.level, blockPos1, new SculkChargeParticleOptions($$57), $$50, $$56, $$52, 0.35);
                        }
                    }
                } else {
                    this.level.m_245747_(blockPos1, SoundEvents.SCULK_BLOCK_CHARGE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
                    boolean $$59 = this.level.m_8055_(blockPos1).m_60838_(this.level, blockPos1);
                    int $$60 = $$59 ? 40 : 20;
                    float $$61 = $$59 ? 0.45F : 0.25F;
                    float $$62 = 0.07F;
                    for (int $$63 = 0; $$63 < $$60; $$63++) {
                        float $$64 = 2.0F * $$3.nextFloat() - 1.0F;
                        float $$65 = 2.0F * $$3.nextFloat() - 1.0F;
                        float $$66 = 2.0F * $$3.nextFloat() - 1.0F;
                        this.level.addParticle(ParticleTypes.SCULK_CHARGE_POP, (double) blockPos1.m_123341_() + 0.5 + (double) ($$64 * $$61), (double) blockPos1.m_123342_() + 0.5 + (double) ($$65 * $$61), (double) blockPos1.m_123343_() + 0.5 + (double) ($$66 * $$61), (double) ($$64 * 0.07F), (double) ($$65 * 0.07F), (double) ($$66 * 0.07F));
                    }
                }
                break;
            case 3007:
                for (int $$67 = 0; $$67 < 10; $$67++) {
                    this.level.addParticle(new ShriekParticleOption($$67 * 5), false, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + SculkShriekerBlock.TOP_Y, (double) blockPos1.m_123343_() + 0.5, 0.0, 0.0, 0.0);
                }
                BlockState $$68 = this.level.m_8055_(blockPos1);
                boolean $$69 = $$68.m_61138_(BlockStateProperties.WATERLOGGED) && (Boolean) $$68.m_61143_(BlockStateProperties.WATERLOGGED);
                if (!$$69) {
                    this.level.playLocalSound((double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + SculkShriekerBlock.TOP_Y, (double) blockPos1.m_123343_() + 0.5, SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.BLOCKS, 2.0F, 0.6F + this.level.f_46441_.nextFloat() * 0.4F, false);
                }
                break;
            case 3008:
                BlockState $$40 = Block.stateById(int2);
                if ($$40.m_60734_() instanceof BrushableBlock $$41) {
                    this.level.m_245747_(blockPos1, $$41.getBrushCompletedSound(), SoundSource.PLAYERS, 1.0F, 1.0F, false);
                }
                this.level.addDestroyBlockEffect(blockPos1, $$40);
                break;
            case 3009:
                ParticleUtils.spawnParticlesOnBlockFaces(this.level, blockPos1, ParticleTypes.EGG_CRACK, UniformInt.of(3, 6));
        }
    }

    public void destroyBlockProgress(int int0, BlockPos blockPos1, int int2) {
        if (int2 >= 0 && int2 < 10) {
            BlockDestructionProgress $$4 = (BlockDestructionProgress) this.destroyingBlocks.get(int0);
            if ($$4 != null) {
                this.removeProgress($$4);
            }
            if ($$4 == null || $$4.getPos().m_123341_() != blockPos1.m_123341_() || $$4.getPos().m_123342_() != blockPos1.m_123342_() || $$4.getPos().m_123343_() != blockPos1.m_123343_()) {
                $$4 = new BlockDestructionProgress(int0, blockPos1);
                this.destroyingBlocks.put(int0, $$4);
            }
            $$4.setProgress(int2);
            $$4.updateTick(this.ticks);
            ((SortedSet) this.destructionProgress.computeIfAbsent($$4.getPos().asLong(), p_234254_ -> Sets.newTreeSet())).add($$4);
        } else {
            BlockDestructionProgress $$3 = (BlockDestructionProgress) this.destroyingBlocks.remove(int0);
            if ($$3 != null) {
                this.removeProgress($$3);
            }
        }
    }

    public boolean hasRenderedAllChunks() {
        return this.chunkRenderDispatcher.isQueueEmpty();
    }

    public void needsUpdate() {
        this.needsFullRenderChunkUpdate = true;
        this.generateClouds = true;
    }

    public void updateGlobalBlockEntities(Collection<BlockEntity> collectionBlockEntity0, Collection<BlockEntity> collectionBlockEntity1) {
        synchronized (this.globalBlockEntities) {
            this.globalBlockEntities.removeAll(collectionBlockEntity0);
            this.globalBlockEntities.addAll(collectionBlockEntity1);
        }
    }

    public static int getLightColor(BlockAndTintGetter blockAndTintGetter0, BlockPos blockPos1) {
        return getLightColor(blockAndTintGetter0, blockAndTintGetter0.m_8055_(blockPos1), blockPos1);
    }

    public static int getLightColor(BlockAndTintGetter blockAndTintGetter0, BlockState blockState1, BlockPos blockPos2) {
        if (blockState1.m_60788_(blockAndTintGetter0, blockPos2)) {
            return 15728880;
        } else {
            int $$3 = blockAndTintGetter0.getBrightness(LightLayer.SKY, blockPos2);
            int $$4 = blockAndTintGetter0.getBrightness(LightLayer.BLOCK, blockPos2);
            int $$5 = blockState1.m_60791_();
            if ($$4 < $$5) {
                $$4 = $$5;
            }
            return $$3 << 20 | $$4 << 4;
        }
    }

    public boolean isChunkCompiled(BlockPos blockPos0) {
        ChunkRenderDispatcher.RenderChunk $$1 = this.viewArea.getRenderChunkAt(blockPos0);
        return $$1 != null && $$1.compiled.get() != ChunkRenderDispatcher.CompiledChunk.UNCOMPILED;
    }

    @Nullable
    public RenderTarget entityTarget() {
        return this.entityTarget;
    }

    @Nullable
    public RenderTarget getTranslucentTarget() {
        return this.translucentTarget;
    }

    @Nullable
    public RenderTarget getItemEntityTarget() {
        return this.itemEntityTarget;
    }

    @Nullable
    public RenderTarget getParticlesTarget() {
        return this.particlesTarget;
    }

    @Nullable
    public RenderTarget getWeatherTarget() {
        return this.weatherTarget;
    }

    @Nullable
    public RenderTarget getCloudsTarget() {
        return this.cloudsTarget;
    }

    static class RenderChunkInfo {

        final ChunkRenderDispatcher.RenderChunk chunk;

        private byte sourceDirections;

        byte directions;

        final int step;

        RenderChunkInfo(ChunkRenderDispatcher.RenderChunk chunkRenderDispatcherRenderChunk0, @Nullable Direction direction1, int int2) {
            this.chunk = chunkRenderDispatcherRenderChunk0;
            if (direction1 != null) {
                this.addSourceDirection(direction1);
            }
            this.step = int2;
        }

        public void setDirections(byte byte0, Direction direction1) {
            this.directions = (byte) (this.directions | byte0 | 1 << direction1.ordinal());
        }

        public boolean hasDirection(Direction direction0) {
            return (this.directions & 1 << direction0.ordinal()) > 0;
        }

        public void addSourceDirection(Direction direction0) {
            this.sourceDirections = (byte) (this.sourceDirections | this.sourceDirections | 1 << direction0.ordinal());
        }

        public boolean hasSourceDirection(int int0) {
            return (this.sourceDirections & 1 << int0) > 0;
        }

        public boolean hasSourceDirections() {
            return this.sourceDirections != 0;
        }

        public boolean isAxisAlignedWith(int int0, int int1, int int2) {
            BlockPos $$3 = this.chunk.getOrigin();
            return int0 == $$3.m_123341_() / 16 || int2 == $$3.m_123343_() / 16 || int1 == $$3.m_123342_() / 16;
        }

        public int hashCode() {
            return this.chunk.getOrigin().hashCode();
        }

        public boolean equals(Object object0) {
            return !(object0 instanceof LevelRenderer.RenderChunkInfo $$1) ? false : this.chunk.getOrigin().equals($$1.chunk.getOrigin());
        }
    }

    static class RenderChunkStorage {

        public final LevelRenderer.RenderInfoMap renderInfoMap;

        public final LinkedHashSet<LevelRenderer.RenderChunkInfo> renderChunks;

        public RenderChunkStorage(int int0) {
            this.renderInfoMap = new LevelRenderer.RenderInfoMap(int0);
            this.renderChunks = new LinkedHashSet(int0);
        }
    }

    static class RenderInfoMap {

        private final LevelRenderer.RenderChunkInfo[] infos;

        RenderInfoMap(int int0) {
            this.infos = new LevelRenderer.RenderChunkInfo[int0];
        }

        public void put(ChunkRenderDispatcher.RenderChunk chunkRenderDispatcherRenderChunk0, LevelRenderer.RenderChunkInfo levelRendererRenderChunkInfo1) {
            this.infos[chunkRenderDispatcherRenderChunk0.index] = levelRendererRenderChunkInfo1;
        }

        @Nullable
        public LevelRenderer.RenderChunkInfo get(ChunkRenderDispatcher.RenderChunk chunkRenderDispatcherRenderChunk0) {
            int $$1 = chunkRenderDispatcherRenderChunk0.index;
            return $$1 >= 0 && $$1 < this.infos.length ? this.infos[$$1] : null;
        }
    }

    public static class TransparencyShaderException extends RuntimeException {

        public TransparencyShaderException(String string0, Throwable throwable1) {
            super(string0, throwable1);
        }
    }
}