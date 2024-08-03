package net.minecraft.server.level;

import com.mojang.logging.LogUtils;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.WorldGenTickAccess;
import org.slf4j.Logger;

public class WorldGenRegion implements WorldGenLevel {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final List<ChunkAccess> cache;

    private final ChunkAccess center;

    private final int size;

    private final ServerLevel level;

    private final long seed;

    private final LevelData levelData;

    private final RandomSource random;

    private final DimensionType dimensionType;

    private final WorldGenTickAccess<Block> blockTicks = new WorldGenTickAccess<>(p_277274_ -> this.m_46865_(p_277274_).getBlockTicks());

    private final WorldGenTickAccess<Fluid> fluidTicks = new WorldGenTickAccess<>(p_277275_ -> this.m_46865_(p_277275_).getFluidTicks());

    private final BiomeManager biomeManager;

    private final ChunkPos firstPos;

    private final ChunkPos lastPos;

    private final StructureManager structureManager;

    private final ChunkStatus generatingStatus;

    private final int writeRadiusCutoff;

    @Nullable
    private Supplier<String> currentlyGenerating;

    private final AtomicLong subTickCount = new AtomicLong();

    private static final ResourceLocation WORLDGEN_REGION_RANDOM = new ResourceLocation("worldgen_region_random");

    public WorldGenRegion(ServerLevel serverLevel0, List<ChunkAccess> listChunkAccess1, ChunkStatus chunkStatus2, int int3) {
        this.generatingStatus = chunkStatus2;
        this.writeRadiusCutoff = int3;
        int $$4 = Mth.floor(Math.sqrt((double) listChunkAccess1.size()));
        if ($$4 * $$4 != listChunkAccess1.size()) {
            throw (IllegalStateException) Util.pauseInIde(new IllegalStateException("Cache size is not a square."));
        } else {
            this.cache = listChunkAccess1;
            this.center = (ChunkAccess) listChunkAccess1.get(listChunkAccess1.size() / 2);
            this.size = $$4;
            this.level = serverLevel0;
            this.seed = serverLevel0.getSeed();
            this.levelData = serverLevel0.m_6106_();
            this.random = serverLevel0.getChunkSource().randomState().getOrCreateRandomFactory(WORLDGEN_REGION_RANDOM).at(this.center.getPos().getWorldPosition());
            this.dimensionType = serverLevel0.m_6042_();
            this.biomeManager = new BiomeManager(this, BiomeManager.obfuscateSeed(this.seed));
            this.firstPos = ((ChunkAccess) listChunkAccess1.get(0)).getPos();
            this.lastPos = ((ChunkAccess) listChunkAccess1.get(listChunkAccess1.size() - 1)).getPos();
            this.structureManager = serverLevel0.structureManager().forWorldGenRegion(this);
        }
    }

    public boolean isOldChunkAround(ChunkPos chunkPos0, int int1) {
        return this.level.getChunkSource().chunkMap.m_223451_(chunkPos0, int1);
    }

    public ChunkPos getCenter() {
        return this.center.getPos();
    }

    @Override
    public void setCurrentlyGenerating(@Nullable Supplier<String> supplierString0) {
        this.currentlyGenerating = supplierString0;
    }

    @Override
    public ChunkAccess getChunk(int int0, int int1) {
        return this.m_46819_(int0, int1, ChunkStatus.EMPTY);
    }

    @Nullable
    @Override
    public ChunkAccess getChunk(int int0, int int1, ChunkStatus chunkStatus2, boolean boolean3) {
        ChunkAccess $$6;
        if (this.hasChunk(int0, int1)) {
            int $$4 = int0 - this.firstPos.x;
            int $$5 = int1 - this.firstPos.z;
            $$6 = (ChunkAccess) this.cache.get($$4 + $$5 * this.size);
            if ($$6.getStatus().isOrAfter(chunkStatus2)) {
                return $$6;
            }
        } else {
            $$6 = null;
        }
        if (!boolean3) {
            return null;
        } else {
            LOGGER.error("Requested chunk : {} {}", int0, int1);
            LOGGER.error("Region bounds : {} {} | {} {}", new Object[] { this.firstPos.x, this.firstPos.z, this.lastPos.x, this.lastPos.z });
            if ($$6 != null) {
                throw (RuntimeException) Util.pauseInIde(new RuntimeException(String.format(Locale.ROOT, "Chunk is not of correct status. Expecting %s, got %s | %s %s", chunkStatus2, $$6.getStatus(), int0, int1)));
            } else {
                throw (RuntimeException) Util.pauseInIde(new RuntimeException(String.format(Locale.ROOT, "We are asking a region for a chunk out of bound | %s %s", int0, int1)));
            }
        }
    }

    @Override
    public boolean hasChunk(int int0, int int1) {
        return int0 >= this.firstPos.x && int0 <= this.lastPos.x && int1 >= this.firstPos.z && int1 <= this.lastPos.z;
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        return this.getChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_())).m_8055_(blockPos0);
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        return this.m_46865_(blockPos0).m_6425_(blockPos0);
    }

    @Nullable
    @Override
    public Player getNearestPlayer(double double0, double double1, double double2, double double3, Predicate<Entity> predicateEntity4) {
        return null;
    }

    @Override
    public int getSkyDarken() {
        return 0;
    }

    @Override
    public BiomeManager getBiomeManager() {
        return this.biomeManager;
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int int0, int int1, int int2) {
        return this.level.getUncachedNoiseBiome(int0, int1, int2);
    }

    @Override
    public float getShade(Direction direction0, boolean boolean1) {
        return 1.0F;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return this.level.m_5518_();
    }

    @Override
    public boolean destroyBlock(BlockPos blockPos0, boolean boolean1, @Nullable Entity entity2, int int3) {
        BlockState $$4 = this.getBlockState(blockPos0);
        if ($$4.m_60795_()) {
            return false;
        } else {
            if (boolean1) {
                BlockEntity $$5 = $$4.m_155947_() ? this.getBlockEntity(blockPos0) : null;
                Block.dropResources($$4, this.level, blockPos0, $$5, entity2, ItemStack.EMPTY);
            }
            return this.setBlock(blockPos0, Blocks.AIR.defaultBlockState(), 3, int3);
        }
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        ChunkAccess $$1 = this.m_46865_(blockPos0);
        BlockEntity $$2 = $$1.m_7702_(blockPos0);
        if ($$2 != null) {
            return $$2;
        } else {
            CompoundTag $$3 = $$1.getBlockEntityNbt(blockPos0);
            BlockState $$4 = $$1.m_8055_(blockPos0);
            if ($$3 != null) {
                if ("DUMMY".equals($$3.getString("id"))) {
                    if (!$$4.m_155947_()) {
                        return null;
                    }
                    $$2 = ((EntityBlock) $$4.m_60734_()).newBlockEntity(blockPos0, $$4);
                } else {
                    $$2 = BlockEntity.loadStatic(blockPos0, $$4, $$3);
                }
                if ($$2 != null) {
                    $$1.setBlockEntity($$2);
                    return $$2;
                }
            }
            if ($$4.m_155947_()) {
                LOGGER.warn("Tried to access a block entity before it was created. {}", blockPos0);
            }
            return null;
        }
    }

    @Override
    public boolean ensureCanWrite(BlockPos blockPos0) {
        int $$1 = SectionPos.blockToSectionCoord(blockPos0.m_123341_());
        int $$2 = SectionPos.blockToSectionCoord(blockPos0.m_123343_());
        ChunkPos $$3 = this.getCenter();
        int $$4 = Math.abs($$3.x - $$1);
        int $$5 = Math.abs($$3.z - $$2);
        if ($$4 <= this.writeRadiusCutoff && $$5 <= this.writeRadiusCutoff) {
            if (this.center.isUpgrading()) {
                LevelHeightAccessor $$6 = this.center.getHeightAccessorForGeneration();
                if (blockPos0.m_123342_() < $$6.getMinBuildHeight() || blockPos0.m_123342_() >= $$6.getMaxBuildHeight()) {
                    return false;
                }
            }
            return true;
        } else {
            Util.logAndPauseIfInIde("Detected setBlock in a far chunk [" + $$1 + ", " + $$2 + "], pos: " + blockPos0 + ", status: " + this.generatingStatus + (this.currentlyGenerating == null ? "" : ", currently generating: " + (String) this.currentlyGenerating.get()));
            return false;
        }
    }

    @Override
    public boolean setBlock(BlockPos blockPos0, BlockState blockState1, int int2, int int3) {
        if (!this.ensureCanWrite(blockPos0)) {
            return false;
        } else {
            ChunkAccess $$4 = this.m_46865_(blockPos0);
            BlockState $$5 = $$4.setBlockState(blockPos0, blockState1, false);
            if ($$5 != null) {
                this.level.onBlockStateChange(blockPos0, $$5, blockState1);
            }
            if (blockState1.m_155947_()) {
                if ($$4.getStatus().getChunkType() == ChunkStatus.ChunkType.LEVELCHUNK) {
                    BlockEntity $$6 = ((EntityBlock) blockState1.m_60734_()).newBlockEntity(blockPos0, blockState1);
                    if ($$6 != null) {
                        $$4.setBlockEntity($$6);
                    } else {
                        $$4.removeBlockEntity(blockPos0);
                    }
                } else {
                    CompoundTag $$7 = new CompoundTag();
                    $$7.putInt("x", blockPos0.m_123341_());
                    $$7.putInt("y", blockPos0.m_123342_());
                    $$7.putInt("z", blockPos0.m_123343_());
                    $$7.putString("id", "DUMMY");
                    $$4.setBlockEntityNbt($$7);
                }
            } else if ($$5 != null && $$5.m_155947_()) {
                $$4.removeBlockEntity(blockPos0);
            }
            if (blockState1.m_60835_(this, blockPos0)) {
                this.markPosForPostprocessing(blockPos0);
            }
            return true;
        }
    }

    private void markPosForPostprocessing(BlockPos blockPos0) {
        this.m_46865_(blockPos0).markPosForPostprocessing(blockPos0);
    }

    @Override
    public boolean addFreshEntity(Entity entity0) {
        int $$1 = SectionPos.blockToSectionCoord(entity0.getBlockX());
        int $$2 = SectionPos.blockToSectionCoord(entity0.getBlockZ());
        this.getChunk($$1, $$2).addEntity(entity0);
        return true;
    }

    @Override
    public boolean removeBlock(BlockPos blockPos0, boolean boolean1) {
        return this.m_7731_(blockPos0, Blocks.AIR.defaultBlockState(), 3);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.level.m_6857_();
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Deprecated
    @Override
    public ServerLevel getLevel() {
        return this.level;
    }

    @Override
    public RegistryAccess registryAccess() {
        return this.level.m_9598_();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return this.level.enabledFeatures();
    }

    @Override
    public LevelData getLevelData() {
        return this.levelData;
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos blockPos0) {
        if (!this.hasChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()))) {
            throw new RuntimeException("We are asking a region for a chunk out of bound");
        } else {
            return new DifficultyInstance(this.level.m_46791_(), this.level.m_46468_(), 0L, this.level.m_46940_());
        }
    }

    @Nullable
    @Override
    public MinecraftServer getServer() {
        return this.level.getServer();
    }

    @Override
    public ChunkSource getChunkSource() {
        return this.level.getChunkSource();
    }

    @Override
    public long getSeed() {
        return this.seed;
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return this.blockTicks;
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return this.fluidTicks;
    }

    @Override
    public int getSeaLevel() {
        return this.level.m_5736_();
    }

    @Override
    public RandomSource getRandom() {
        return this.random;
    }

    @Override
    public int getHeight(Heightmap.Types heightmapTypes0, int int1, int int2) {
        return this.getChunk(SectionPos.blockToSectionCoord(int1), SectionPos.blockToSectionCoord(int2)).getHeight(heightmapTypes0, int1 & 15, int2 & 15) + 1;
    }

    @Override
    public void playSound(@Nullable Player player0, BlockPos blockPos1, SoundEvent soundEvent2, SoundSource soundSource3, float float4, float float5) {
    }

    @Override
    public void addParticle(ParticleOptions particleOptions0, double double1, double double2, double double3, double double4, double double5, double double6) {
    }

    @Override
    public void levelEvent(@Nullable Player player0, int int1, BlockPos blockPos2, int int3) {
    }

    @Override
    public void gameEvent(GameEvent gameEvent0, Vec3 vec1, GameEvent.Context gameEventContext2) {
    }

    @Override
    public DimensionType dimensionType() {
        return this.dimensionType;
    }

    @Override
    public boolean isStateAtPosition(BlockPos blockPos0, Predicate<BlockState> predicateBlockState1) {
        return predicateBlockState1.test(this.getBlockState(blockPos0));
    }

    @Override
    public boolean isFluidAtPosition(BlockPos blockPos0, Predicate<FluidState> predicateFluidState1) {
        return predicateFluidState1.test(this.getFluidState(blockPos0));
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entityTypeTestEntityT0, AABB aABB1, Predicate<? super T> predicateSuperT2) {
        return Collections.emptyList();
    }

    @Override
    public List<Entity> getEntities(@Nullable Entity entity0, AABB aABB1, @Nullable Predicate<? super Entity> predicateSuperEntity2) {
        return Collections.emptyList();
    }

    @Override
    public List<Player> players() {
        return Collections.emptyList();
    }

    @Override
    public int getMinBuildHeight() {
        return this.level.m_141937_();
    }

    @Override
    public int getHeight() {
        return this.level.m_141928_();
    }

    @Override
    public long nextSubTickCount() {
        return this.subTickCount.getAndIncrement();
    }
}