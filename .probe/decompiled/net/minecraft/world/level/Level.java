package net.minecraft.world.level;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.CrashReportDetail;
import net.minecraft.ReportedException;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.AbortableIterationConsumer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.redstone.CollectingNeighborUpdater;
import net.minecraft.world.level.redstone.NeighborUpdater;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Scoreboard;

public abstract class Level implements LevelAccessor, AutoCloseable {

    public static final Codec<ResourceKey<Level>> RESOURCE_KEY_CODEC = ResourceKey.codec(Registries.DIMENSION);

    public static final ResourceKey<Level> OVERWORLD = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("overworld"));

    public static final ResourceKey<Level> NETHER = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("the_nether"));

    public static final ResourceKey<Level> END = ResourceKey.create(Registries.DIMENSION, new ResourceLocation("the_end"));

    public static final int MAX_LEVEL_SIZE = 30000000;

    public static final int LONG_PARTICLE_CLIP_RANGE = 512;

    public static final int SHORT_PARTICLE_CLIP_RANGE = 32;

    public static final int MAX_BRIGHTNESS = 15;

    public static final int TICKS_PER_DAY = 24000;

    public static final int MAX_ENTITY_SPAWN_Y = 20000000;

    public static final int MIN_ENTITY_SPAWN_Y = -20000000;

    protected final List<TickingBlockEntity> blockEntityTickers = Lists.newArrayList();

    protected final NeighborUpdater neighborUpdater;

    private final List<TickingBlockEntity> pendingBlockEntityTickers = Lists.newArrayList();

    private boolean tickingBlockEntities;

    private final Thread thread;

    private final boolean isDebug;

    private int skyDarken;

    protected int randValue = RandomSource.create().nextInt();

    protected final int addend = 1013904223;

    protected float oRainLevel;

    protected float rainLevel;

    protected float oThunderLevel;

    protected float thunderLevel;

    public final RandomSource random = RandomSource.create();

    @Deprecated
    private final RandomSource threadSafeRandom = RandomSource.createThreadSafe();

    private final ResourceKey<DimensionType> dimensionTypeId;

    private final Holder<DimensionType> dimensionTypeRegistration;

    protected final WritableLevelData levelData;

    private final Supplier<ProfilerFiller> profiler;

    public final boolean isClientSide;

    private final WorldBorder worldBorder;

    private final BiomeManager biomeManager;

    private final ResourceKey<Level> dimension;

    private final RegistryAccess registryAccess;

    private final DamageSources damageSources;

    private long subTickCount;

    protected Level(WritableLevelData writableLevelData0, ResourceKey<Level> resourceKeyLevel1, RegistryAccess registryAccess2, Holder<DimensionType> holderDimensionType3, Supplier<ProfilerFiller> supplierProfilerFiller4, boolean boolean5, boolean boolean6, long long7, int int8) {
        this.profiler = supplierProfilerFiller4;
        this.levelData = writableLevelData0;
        this.dimensionTypeRegistration = holderDimensionType3;
        this.dimensionTypeId = (ResourceKey<DimensionType>) holderDimensionType3.unwrapKey().orElseThrow(() -> new IllegalArgumentException("Dimension must be registered, got " + holderDimensionType3));
        final DimensionType $$9 = holderDimensionType3.value();
        this.dimension = resourceKeyLevel1;
        this.isClientSide = boolean5;
        if ($$9.coordinateScale() != 1.0) {
            this.worldBorder = new WorldBorder() {

                @Override
                public double getCenterX() {
                    return super.getCenterX() / $$9.coordinateScale();
                }

                @Override
                public double getCenterZ() {
                    return super.getCenterZ() / $$9.coordinateScale();
                }
            };
        } else {
            this.worldBorder = new WorldBorder();
        }
        this.thread = Thread.currentThread();
        this.biomeManager = new BiomeManager(this, long7);
        this.isDebug = boolean6;
        this.neighborUpdater = new CollectingNeighborUpdater(this, int8);
        this.registryAccess = registryAccess2;
        this.damageSources = new DamageSources(registryAccess2);
    }

    @Override
    public boolean isClientSide() {
        return this.isClientSide;
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return null;
    }

    public boolean isInWorldBounds(BlockPos blockPos0) {
        return !this.m_151570_(blockPos0) && isInWorldBoundsHorizontal(blockPos0);
    }

    public static boolean isInSpawnableBounds(BlockPos blockPos0) {
        return !isOutsideSpawnableHeight(blockPos0.m_123342_()) && isInWorldBoundsHorizontal(blockPos0);
    }

    private static boolean isInWorldBoundsHorizontal(BlockPos blockPos0) {
        return blockPos0.m_123341_() >= -30000000 && blockPos0.m_123343_() >= -30000000 && blockPos0.m_123341_() < 30000000 && blockPos0.m_123343_() < 30000000;
    }

    private static boolean isOutsideSpawnableHeight(int int0) {
        return int0 < -20000000 || int0 >= 20000000;
    }

    public LevelChunk getChunkAt(BlockPos blockPos0) {
        return this.getChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
    }

    public LevelChunk getChunk(int int0, int int1) {
        return (LevelChunk) this.m_46819_(int0, int1, ChunkStatus.FULL);
    }

    @Nullable
    @Override
    public ChunkAccess getChunk(int int0, int int1, ChunkStatus chunkStatus2, boolean boolean3) {
        ChunkAccess $$4 = this.m_7726_().getChunk(int0, int1, chunkStatus2, boolean3);
        if ($$4 == null && boolean3) {
            throw new IllegalStateException("Should always be able to create a chunk!");
        } else {
            return $$4;
        }
    }

    @Override
    public boolean setBlock(BlockPos blockPos0, BlockState blockState1, int int2) {
        return this.setBlock(blockPos0, blockState1, int2, 512);
    }

    @Override
    public boolean setBlock(BlockPos blockPos0, BlockState blockState1, int int2, int int3) {
        if (this.m_151570_(blockPos0)) {
            return false;
        } else if (!this.isClientSide && this.isDebug()) {
            return false;
        } else {
            LevelChunk $$4 = this.getChunkAt(blockPos0);
            Block $$5 = blockState1.m_60734_();
            BlockState $$6 = $$4.setBlockState(blockPos0, blockState1, (int2 & 64) != 0);
            if ($$6 == null) {
                return false;
            } else {
                BlockState $$7 = this.getBlockState(blockPos0);
                if ($$7 == blockState1) {
                    if ($$6 != $$7) {
                        this.setBlocksDirty(blockPos0, $$6, $$7);
                    }
                    if ((int2 & 2) != 0 && (!this.isClientSide || (int2 & 4) == 0) && (this.isClientSide || $$4.getFullStatus() != null && $$4.getFullStatus().isOrAfter(FullChunkStatus.BLOCK_TICKING))) {
                        this.sendBlockUpdated(blockPos0, $$6, blockState1, int2);
                    }
                    if ((int2 & 1) != 0) {
                        this.m_6289_(blockPos0, $$6.m_60734_());
                        if (!this.isClientSide && blockState1.m_60807_()) {
                            this.updateNeighbourForOutputSignal(blockPos0, $$5);
                        }
                    }
                    if ((int2 & 16) == 0 && int3 > 0) {
                        int $$8 = int2 & -34;
                        $$6.m_60762_(this, blockPos0, $$8, int3 - 1);
                        blockState1.m_60705_(this, blockPos0, $$8, int3 - 1);
                        blockState1.m_60762_(this, blockPos0, $$8, int3 - 1);
                    }
                    this.onBlockStateChange(blockPos0, $$6, $$7);
                }
                return true;
            }
        }
    }

    public void onBlockStateChange(BlockPos blockPos0, BlockState blockState1, BlockState blockState2) {
    }

    @Override
    public boolean removeBlock(BlockPos blockPos0, boolean boolean1) {
        FluidState $$2 = this.getFluidState(blockPos0);
        return this.setBlock(blockPos0, $$2.createLegacyBlock(), 3 | (boolean1 ? 64 : 0));
    }

    @Override
    public boolean destroyBlock(BlockPos blockPos0, boolean boolean1, @Nullable Entity entity2, int int3) {
        BlockState $$4 = this.getBlockState(blockPos0);
        if ($$4.m_60795_()) {
            return false;
        } else {
            FluidState $$5 = this.getFluidState(blockPos0);
            if (!($$4.m_60734_() instanceof BaseFireBlock)) {
                this.m_46796_(2001, blockPos0, Block.getId($$4));
            }
            if (boolean1) {
                BlockEntity $$6 = $$4.m_155947_() ? this.getBlockEntity(blockPos0) : null;
                Block.dropResources($$4, this, blockPos0, $$6, entity2, ItemStack.EMPTY);
            }
            boolean $$7 = this.setBlock(blockPos0, $$5.createLegacyBlock(), 3, int3);
            if ($$7) {
                this.m_220407_(GameEvent.BLOCK_DESTROY, blockPos0, GameEvent.Context.of(entity2, $$4));
            }
            return $$7;
        }
    }

    public void addDestroyBlockEffect(BlockPos blockPos0, BlockState blockState1) {
    }

    public boolean setBlockAndUpdate(BlockPos blockPos0, BlockState blockState1) {
        return this.setBlock(blockPos0, blockState1, 3);
    }

    public abstract void sendBlockUpdated(BlockPos var1, BlockState var2, BlockState var3, int var4);

    public void setBlocksDirty(BlockPos blockPos0, BlockState blockState1, BlockState blockState2) {
    }

    public void updateNeighborsAt(BlockPos blockPos0, Block block1) {
    }

    public void updateNeighborsAtExceptFromFacing(BlockPos blockPos0, Block block1, Direction direction2) {
    }

    public void neighborChanged(BlockPos blockPos0, Block block1, BlockPos blockPos2) {
    }

    public void neighborChanged(BlockState blockState0, BlockPos blockPos1, Block block2, BlockPos blockPos3, boolean boolean4) {
    }

    @Override
    public void neighborShapeChanged(Direction direction0, BlockState blockState1, BlockPos blockPos2, BlockPos blockPos3, int int4, int int5) {
        this.neighborUpdater.shapeUpdate(direction0, blockState1, blockPos2, blockPos3, int4, int5);
    }

    @Override
    public int getHeight(Heightmap.Types heightmapTypes0, int int1, int int2) {
        int $$4;
        if (int1 >= -30000000 && int2 >= -30000000 && int1 < 30000000 && int2 < 30000000) {
            if (this.m_7232_(SectionPos.blockToSectionCoord(int1), SectionPos.blockToSectionCoord(int2))) {
                $$4 = this.getChunk(SectionPos.blockToSectionCoord(int1), SectionPos.blockToSectionCoord(int2)).m_5885_(heightmapTypes0, int1 & 15, int2 & 15) + 1;
            } else {
                $$4 = this.m_141937_();
            }
        } else {
            $$4 = this.getSeaLevel() + 1;
        }
        return $$4;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.m_7726_().getLightEngine();
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        if (this.m_151570_(blockPos0)) {
            return Blocks.VOID_AIR.defaultBlockState();
        } else {
            LevelChunk $$1 = this.getChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
            return $$1.getBlockState(blockPos0);
        }
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        if (this.m_151570_(blockPos0)) {
            return Fluids.EMPTY.defaultFluidState();
        } else {
            LevelChunk $$1 = this.getChunkAt(blockPos0);
            return $$1.getFluidState(blockPos0);
        }
    }

    public boolean isDay() {
        return !this.dimensionType().hasFixedTime() && this.skyDarken < 4;
    }

    public boolean isNight() {
        return !this.dimensionType().hasFixedTime() && !this.isDay();
    }

    public void playSound(@Nullable Entity entity0, BlockPos blockPos1, SoundEvent soundEvent2, SoundSource soundSource3, float float4, float float5) {
        this.playSound(entity0 instanceof Player $$6 ? $$6 : null, blockPos1, soundEvent2, soundSource3, float4, float5);
    }

    @Override
    public void playSound(@Nullable Player player0, BlockPos blockPos1, SoundEvent soundEvent2, SoundSource soundSource3, float float4, float float5) {
        this.playSound(player0, (double) blockPos1.m_123341_() + 0.5, (double) blockPos1.m_123342_() + 0.5, (double) blockPos1.m_123343_() + 0.5, soundEvent2, soundSource3, float4, float5);
    }

    public abstract void playSeededSound(@Nullable Player var1, double var2, double var4, double var6, Holder<SoundEvent> var8, SoundSource var9, float var10, float var11, long var12);

    public void playSeededSound(@Nullable Player player0, double double1, double double2, double double3, SoundEvent soundEvent4, SoundSource soundSource5, float float6, float float7, long long8) {
        this.playSeededSound(player0, double1, double2, double3, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent4), soundSource5, float6, float7, long8);
    }

    public abstract void playSeededSound(@Nullable Player var1, Entity var2, Holder<SoundEvent> var3, SoundSource var4, float var5, float var6, long var7);

    public void playSound(@Nullable Player player0, double double1, double double2, double double3, SoundEvent soundEvent4, SoundSource soundSource5, float float6, float float7) {
        this.playSeededSound(player0, double1, double2, double3, soundEvent4, soundSource5, float6, float7, this.threadSafeRandom.nextLong());
    }

    public void playSound(@Nullable Player player0, Entity entity1, SoundEvent soundEvent2, SoundSource soundSource3, float float4, float float5) {
        this.playSeededSound(player0, entity1, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundEvent2), soundSource3, float4, float5, this.threadSafeRandom.nextLong());
    }

    public void playLocalSound(BlockPos blockPos0, SoundEvent soundEvent1, SoundSource soundSource2, float float3, float float4, boolean boolean5) {
        this.playLocalSound((double) blockPos0.m_123341_() + 0.5, (double) blockPos0.m_123342_() + 0.5, (double) blockPos0.m_123343_() + 0.5, soundEvent1, soundSource2, float3, float4, boolean5);
    }

    public void playLocalSound(double double0, double double1, double double2, SoundEvent soundEvent3, SoundSource soundSource4, float float5, float float6, boolean boolean7) {
    }

    @Override
    public void addParticle(ParticleOptions particleOptions0, double double1, double double2, double double3, double double4, double double5, double double6) {
    }

    public void addParticle(ParticleOptions particleOptions0, boolean boolean1, double double2, double double3, double double4, double double5, double double6, double double7) {
    }

    public void addAlwaysVisibleParticle(ParticleOptions particleOptions0, double double1, double double2, double double3, double double4, double double5, double double6) {
    }

    public void addAlwaysVisibleParticle(ParticleOptions particleOptions0, boolean boolean1, double double2, double double3, double double4, double double5, double double6, double double7) {
    }

    public float getSunAngle(float float0) {
        float $$1 = this.m_46942_(float0);
        return $$1 * (float) (Math.PI * 2);
    }

    public void addBlockEntityTicker(TickingBlockEntity tickingBlockEntity0) {
        (this.tickingBlockEntities ? this.pendingBlockEntityTickers : this.blockEntityTickers).add(tickingBlockEntity0);
    }

    protected void tickBlockEntities() {
        ProfilerFiller $$0 = this.getProfiler();
        $$0.push("blockEntities");
        this.tickingBlockEntities = true;
        if (!this.pendingBlockEntityTickers.isEmpty()) {
            this.blockEntityTickers.addAll(this.pendingBlockEntityTickers);
            this.pendingBlockEntityTickers.clear();
        }
        Iterator<TickingBlockEntity> $$1 = this.blockEntityTickers.iterator();
        while ($$1.hasNext()) {
            TickingBlockEntity $$2 = (TickingBlockEntity) $$1.next();
            if ($$2.isRemoved()) {
                $$1.remove();
            } else if (this.shouldTickBlocksAt($$2.getPos())) {
                $$2.tick();
            }
        }
        this.tickingBlockEntities = false;
        $$0.pop();
    }

    public <T extends Entity> void guardEntityTick(Consumer<T> consumerT0, T t1) {
        try {
            consumerT0.accept(t1);
        } catch (Throwable var6) {
            CrashReport $$3 = CrashReport.forThrowable(var6, "Ticking entity");
            CrashReportCategory $$4 = $$3.addCategory("Entity being ticked");
            t1.fillCrashReportCategory($$4);
            throw new ReportedException($$3);
        }
    }

    public boolean shouldTickDeath(Entity entity0) {
        return true;
    }

    public boolean shouldTickBlocksAt(long long0) {
        return true;
    }

    public boolean shouldTickBlocksAt(BlockPos blockPos0) {
        return this.shouldTickBlocksAt(ChunkPos.asLong(blockPos0));
    }

    public Explosion explode(@Nullable Entity entity0, double double1, double double2, double double3, float float4, Level.ExplosionInteraction levelExplosionInteraction5) {
        return this.explode(entity0, null, null, double1, double2, double3, float4, false, levelExplosionInteraction5);
    }

    public Explosion explode(@Nullable Entity entity0, double double1, double double2, double double3, float float4, boolean boolean5, Level.ExplosionInteraction levelExplosionInteraction6) {
        return this.explode(entity0, null, null, double1, double2, double3, float4, boolean5, levelExplosionInteraction6);
    }

    public Explosion explode(@Nullable Entity entity0, @Nullable DamageSource damageSource1, @Nullable ExplosionDamageCalculator explosionDamageCalculator2, Vec3 vec3, float float4, boolean boolean5, Level.ExplosionInteraction levelExplosionInteraction6) {
        return this.explode(entity0, damageSource1, explosionDamageCalculator2, vec3.x(), vec3.y(), vec3.z(), float4, boolean5, levelExplosionInteraction6);
    }

    public Explosion explode(@Nullable Entity entity0, @Nullable DamageSource damageSource1, @Nullable ExplosionDamageCalculator explosionDamageCalculator2, double double3, double double4, double double5, float float6, boolean boolean7, Level.ExplosionInteraction levelExplosionInteraction8) {
        return this.explode(entity0, damageSource1, explosionDamageCalculator2, double3, double4, double5, float6, boolean7, levelExplosionInteraction8, true);
    }

    public Explosion explode(@Nullable Entity entity0, @Nullable DamageSource damageSource1, @Nullable ExplosionDamageCalculator explosionDamageCalculator2, double double3, double double4, double double5, float float6, boolean boolean7, Level.ExplosionInteraction levelExplosionInteraction8, boolean boolean9) {
        Explosion.BlockInteraction $$10 = switch(levelExplosionInteraction8) {
            case NONE ->
                Explosion.BlockInteraction.KEEP;
            case BLOCK ->
                this.getDestroyType(GameRules.RULE_BLOCK_EXPLOSION_DROP_DECAY);
            case MOB ->
                this.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) ? this.getDestroyType(GameRules.RULE_MOB_EXPLOSION_DROP_DECAY) : Explosion.BlockInteraction.KEEP;
            case TNT ->
                this.getDestroyType(GameRules.RULE_TNT_EXPLOSION_DROP_DECAY);
        };
        Explosion $$11 = new Explosion(this, entity0, damageSource1, explosionDamageCalculator2, double3, double4, double5, float6, boolean7, $$10);
        $$11.explode();
        $$11.finalizeExplosion(boolean9);
        return $$11;
    }

    private Explosion.BlockInteraction getDestroyType(GameRules.Key<GameRules.BooleanValue> gameRulesKeyGameRulesBooleanValue0) {
        return this.getGameRules().getBoolean(gameRulesKeyGameRulesBooleanValue0) ? Explosion.BlockInteraction.DESTROY_WITH_DECAY : Explosion.BlockInteraction.DESTROY;
    }

    public abstract String gatherChunkSourceStats();

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        if (this.m_151570_(blockPos0)) {
            return null;
        } else {
            return !this.isClientSide && Thread.currentThread() != this.thread ? null : this.getChunkAt(blockPos0).getBlockEntity(blockPos0, LevelChunk.EntityCreationType.IMMEDIATE);
        }
    }

    public void setBlockEntity(BlockEntity blockEntity0) {
        BlockPos $$1 = blockEntity0.getBlockPos();
        if (!this.m_151570_($$1)) {
            this.getChunkAt($$1).addAndRegisterBlockEntity(blockEntity0);
        }
    }

    public void removeBlockEntity(BlockPos blockPos0) {
        if (!this.m_151570_(blockPos0)) {
            this.getChunkAt(blockPos0).removeBlockEntity(blockPos0);
        }
    }

    public boolean isLoaded(BlockPos blockPos0) {
        return this.m_151570_(blockPos0) ? false : this.m_7726_().hasChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
    }

    public boolean loadedAndEntityCanStandOnFace(BlockPos blockPos0, Entity entity1, Direction direction2) {
        if (this.m_151570_(blockPos0)) {
            return false;
        } else {
            ChunkAccess $$3 = this.getChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()), ChunkStatus.FULL, false);
            return $$3 == null ? false : $$3.m_8055_(blockPos0).m_60638_(this, blockPos0, entity1, direction2);
        }
    }

    public boolean loadedAndEntityCanStandOn(BlockPos blockPos0, Entity entity1) {
        return this.loadedAndEntityCanStandOnFace(blockPos0, entity1, Direction.UP);
    }

    public void updateSkyBrightness() {
        double $$0 = 1.0 - (double) (this.getRainLevel(1.0F) * 5.0F) / 16.0;
        double $$1 = 1.0 - (double) (this.getThunderLevel(1.0F) * 5.0F) / 16.0;
        double $$2 = 0.5 + 2.0 * Mth.clamp((double) Mth.cos(this.m_46942_(1.0F) * (float) (Math.PI * 2)), -0.25, 0.25);
        this.skyDarken = (int) ((1.0 - $$2 * $$0 * $$1) * 11.0);
    }

    public void setSpawnSettings(boolean boolean0, boolean boolean1) {
        this.m_7726_().setSpawnSettings(boolean0, boolean1);
    }

    public BlockPos getSharedSpawnPos() {
        BlockPos $$0 = new BlockPos(this.levelData.m_6789_(), this.levelData.m_6527_(), this.levelData.m_6526_());
        if (!this.getWorldBorder().isWithinBounds($$0)) {
            $$0 = this.m_5452_(Heightmap.Types.MOTION_BLOCKING, BlockPos.containing(this.getWorldBorder().getCenterX(), 0.0, this.getWorldBorder().getCenterZ()));
        }
        return $$0;
    }

    public float getSharedSpawnAngle() {
        return this.levelData.m_6790_();
    }

    protected void prepareWeather() {
        if (this.levelData.m_6533_()) {
            this.rainLevel = 1.0F;
            if (this.levelData.m_6534_()) {
                this.thunderLevel = 1.0F;
            }
        }
    }

    public void close() throws IOException {
        this.m_7726_().close();
    }

    @Nullable
    @Override
    public BlockGetter getChunkForCollisions(int int0, int int1) {
        return this.getChunk(int0, int1, ChunkStatus.FULL, false);
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity0, AABB aABB1, Predicate<? super Entity> predicateSuperEntity2) {
        this.getProfiler().incrementCounter("getEntities");
        List<Entity> $$3 = Lists.newArrayList();
        this.getEntities().get(aABB1, p_151522_ -> {
            if (p_151522_ != entity0 && predicateSuperEntity2.test(p_151522_)) {
                $$3.add(p_151522_);
            }
            if (p_151522_ instanceof EnderDragon) {
                for (EnderDragonPart $$4 : ((EnderDragon) p_151522_).getSubEntities()) {
                    if (p_151522_ != entity0 && predicateSuperEntity2.test($$4)) {
                        $$3.add($$4);
                    }
                }
            }
        });
        return $$3;
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTestEntityT0, AABB aABB1, Predicate<? super T> predicateSuperT2) {
        List<T> $$3 = Lists.newArrayList();
        this.getEntities(entityTypeTestEntityT0, aABB1, predicateSuperT2, $$3);
        return $$3;
    }

    public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> entityTypeTestEntityT0, AABB aABB1, Predicate<? super T> predicateSuperT2, List<? super T> listSuperT3) {
        this.getEntities(entityTypeTestEntityT0, aABB1, predicateSuperT2, listSuperT3, Integer.MAX_VALUE);
    }

    public <T extends Entity> void getEntities(EntityTypeTest<Entity, T> entityTypeTestEntityT0, AABB aABB1, Predicate<? super T> predicateSuperT2, List<? super T> listSuperT3, int int4) {
        this.getProfiler().incrementCounter("getEntities");
        this.getEntities().get(entityTypeTestEntityT0, aABB1, p_261454_ -> {
            if (predicateSuperT2.test(p_261454_)) {
                listSuperT3.add(p_261454_);
                if (listSuperT3.size() >= int4) {
                    return AbortableIterationConsumer.Continuation.ABORT;
                }
            }
            if (p_261454_ instanceof EnderDragon $$5) {
                for (EnderDragonPart $$6 : $$5.getSubEntities()) {
                    T $$7 = entityTypeTestEntityT0.tryCast($$6);
                    if ($$7 != null && predicateSuperT2.test($$7)) {
                        listSuperT3.add($$7);
                        if (listSuperT3.size() >= int4) {
                            return AbortableIterationConsumer.Continuation.ABORT;
                        }
                    }
                }
            }
            return AbortableIterationConsumer.Continuation.CONTINUE;
        });
    }

    @Nullable
    public abstract Entity getEntity(int var1);

    public void blockEntityChanged(BlockPos blockPos0) {
        if (this.m_46805_(blockPos0)) {
            this.getChunkAt(blockPos0).m_8092_(true);
        }
    }

    @Override
    public int getSeaLevel() {
        return 63;
    }

    public void disconnect() {
    }

    public long getGameTime() {
        return this.levelData.m_6793_();
    }

    public long getDayTime() {
        return this.levelData.m_6792_();
    }

    public boolean mayInteract(Player player0, BlockPos blockPos1) {
        return true;
    }

    public void broadcastEntityEvent(Entity entity0, byte byte1) {
    }

    public void broadcastDamageEvent(Entity entity0, DamageSource damageSource1) {
    }

    public void blockEvent(BlockPos blockPos0, Block block1, int int2, int int3) {
        this.getBlockState(blockPos0).m_60677_(this, blockPos0, int2, int3);
    }

    @Override
    public LevelData getLevelData() {
        return this.levelData;
    }

    public GameRules getGameRules() {
        return this.levelData.m_5470_();
    }

    public float getThunderLevel(float float0) {
        return Mth.lerp(float0, this.oThunderLevel, this.thunderLevel) * this.getRainLevel(float0);
    }

    public void setThunderLevel(float float0) {
        float $$1 = Mth.clamp(float0, 0.0F, 1.0F);
        this.oThunderLevel = $$1;
        this.thunderLevel = $$1;
    }

    public float getRainLevel(float float0) {
        return Mth.lerp(float0, this.oRainLevel, this.rainLevel);
    }

    public void setRainLevel(float float0) {
        float $$1 = Mth.clamp(float0, 0.0F, 1.0F);
        this.oRainLevel = $$1;
        this.rainLevel = $$1;
    }

    public boolean isThundering() {
        return this.dimensionType().hasSkyLight() && !this.dimensionType().hasCeiling() ? (double) this.getThunderLevel(1.0F) > 0.9 : false;
    }

    public boolean isRaining() {
        return (double) this.getRainLevel(1.0F) > 0.2;
    }

    public boolean isRainingAt(BlockPos blockPos0) {
        if (!this.isRaining()) {
            return false;
        } else if (!this.m_45527_(blockPos0)) {
            return false;
        } else if (this.m_5452_(Heightmap.Types.MOTION_BLOCKING, blockPos0).m_123342_() > blockPos0.m_123342_()) {
            return false;
        } else {
            Biome $$1 = (Biome) this.m_204166_(blockPos0).value();
            return $$1.getPrecipitationAt(blockPos0) == Biome.Precipitation.RAIN;
        }
    }

    @Nullable
    public abstract MapItemSavedData getMapData(String var1);

    public abstract void setMapData(String var1, MapItemSavedData var2);

    public abstract int getFreeMapId();

    public void globalLevelEvent(int int0, BlockPos blockPos1, int int2) {
    }

    public CrashReportCategory fillReportDetails(CrashReport crashReport0) {
        CrashReportCategory $$1 = crashReport0.addCategory("Affected level", 1);
        $$1.setDetail("All players", (CrashReportDetail<String>) (() -> this.m_6907_().size() + " total; " + this.m_6907_()));
        $$1.setDetail("Chunk stats", this.m_7726_()::m_6754_);
        $$1.setDetail("Level dimension", (CrashReportDetail<String>) (() -> this.dimension().location().toString()));
        try {
            this.levelData.m_142471_($$1, this);
        } catch (Throwable var4) {
            $$1.setDetailError("Level Data Unobtainable", var4);
        }
        return $$1;
    }

    public abstract void destroyBlockProgress(int var1, BlockPos var2, int var3);

    public void createFireworks(double double0, double double1, double double2, double double3, double double4, double double5, @Nullable CompoundTag compoundTag6) {
    }

    public abstract Scoreboard getScoreboard();

    public void updateNeighbourForOutputSignal(BlockPos blockPos0, Block block1) {
        for (Direction $$2 : Direction.Plane.HORIZONTAL) {
            BlockPos $$3 = blockPos0.relative($$2);
            if (this.m_46805_($$3)) {
                BlockState $$4 = this.getBlockState($$3);
                if ($$4.m_60713_(Blocks.COMPARATOR)) {
                    this.neighborChanged($$4, $$3, block1, blockPos0, false);
                } else if ($$4.m_60796_(this, $$3)) {
                    $$3 = $$3.relative($$2);
                    $$4 = this.getBlockState($$3);
                    if ($$4.m_60713_(Blocks.COMPARATOR)) {
                        this.neighborChanged($$4, $$3, block1, blockPos0, false);
                    }
                }
            }
        }
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos blockPos0) {
        long $$1 = 0L;
        float $$2 = 0.0F;
        if (this.m_46805_(blockPos0)) {
            $$2 = this.m_46940_();
            $$1 = this.getChunkAt(blockPos0).m_6319_();
        }
        return new DifficultyInstance(this.m_46791_(), this.getDayTime(), $$1, $$2);
    }

    @Override
    public int getSkyDarken() {
        return this.skyDarken;
    }

    public void setSkyFlashTime(int int0) {
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.worldBorder;
    }

    public void sendPacketToServer(Packet<?> packet0) {
        throw new UnsupportedOperationException("Can't send packets to server unless you're on the client.");
    }

    @Override
    public DimensionType dimensionType() {
        return this.dimensionTypeRegistration.value();
    }

    public ResourceKey<DimensionType> dimensionTypeId() {
        return this.dimensionTypeId;
    }

    public Holder<DimensionType> dimensionTypeRegistration() {
        return this.dimensionTypeRegistration;
    }

    public ResourceKey<Level> dimension() {
        return this.dimension;
    }

    @Override
    public RandomSource getRandom() {
        return this.random;
    }

    @Override
    public boolean isStateAtPosition(BlockPos blockPos0, Predicate<BlockState> predicateBlockState1) {
        return predicateBlockState1.test(this.getBlockState(blockPos0));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos blockPos0, Predicate<FluidState> predicateFluidState1) {
        return predicateFluidState1.test(this.getFluidState(blockPos0));
    }

    public abstract RecipeManager getRecipeManager();

    public BlockPos getBlockRandomPos(int int0, int int1, int int2, int int3) {
        this.randValue = this.randValue * 3 + 1013904223;
        int $$4 = this.randValue >> 2;
        return new BlockPos(int0 + ($$4 & 15), int1 + ($$4 >> 16 & int3), int2 + ($$4 >> 8 & 15));
    }

    public boolean noSave() {
        return false;
    }

    public ProfilerFiller getProfiler() {
        return (ProfilerFiller) this.profiler.get();
    }

    public Supplier<ProfilerFiller> getProfilerSupplier() {
        return this.profiler;
    }

    @Override
    public BiomeManager getBiomeManager() {
        return this.biomeManager;
    }

    public final boolean isDebug() {
        return this.isDebug;
    }

    protected abstract LevelEntityGetter<Entity> getEntities();

    @Override
    public long nextSubTickCount() {
        return this.subTickCount++;
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }

    public DamageSources damageSources() {
        return this.damageSources;
    }

    public static enum ExplosionInteraction {

        NONE, BLOCK, MOB, TNT
    }
}