package net.minecraft.world.level;

import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

public interface LevelReader extends BlockAndTintGetter, CollisionGetter, SignalGetter, BiomeManager.NoiseBiomeSource {

    @Nullable
    ChunkAccess getChunk(int var1, int var2, ChunkStatus var3, boolean var4);

    @Deprecated
    boolean hasChunk(int var1, int var2);

    int getHeight(Heightmap.Types var1, int var2, int var3);

    int getSkyDarken();

    BiomeManager getBiomeManager();

    default Holder<Biome> getBiome(BlockPos blockPos0) {
        return this.getBiomeManager().getBiome(blockPos0);
    }

    default Stream<BlockState> getBlockStatesIfLoaded(AABB aABB0) {
        int $$1 = Mth.floor(aABB0.minX);
        int $$2 = Mth.floor(aABB0.maxX);
        int $$3 = Mth.floor(aABB0.minY);
        int $$4 = Mth.floor(aABB0.maxY);
        int $$5 = Mth.floor(aABB0.minZ);
        int $$6 = Mth.floor(aABB0.maxZ);
        return this.hasChunksAt($$1, $$3, $$5, $$2, $$4, $$6) ? this.m_45556_(aABB0) : Stream.empty();
    }

    @Override
    default int getBlockTint(BlockPos blockPos0, ColorResolver colorResolver1) {
        return colorResolver1.getColor(this.getBiome(blockPos0).value(), (double) blockPos0.m_123341_(), (double) blockPos0.m_123343_());
    }

    @Override
    default Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        ChunkAccess $$3 = this.getChunk(QuartPos.toSection(int0), QuartPos.toSection(int2), ChunkStatus.BIOMES, false);
        return $$3 != null ? $$3.getNoiseBiome(int0, int1, int2) : this.getUncachedNoiseBiome(int0, int1, int2);
    }

    Holder<Biome> getUncachedNoiseBiome(int var1, int var2, int var3);

    boolean isClientSide();

    @Deprecated
    int getSeaLevel();

    DimensionType dimensionType();

    @Override
    default int getMinBuildHeight() {
        return this.dimensionType().minY();
    }

    @Override
    default int getHeight() {
        return this.dimensionType().height();
    }

    default BlockPos getHeightmapPos(Heightmap.Types heightmapTypes0, BlockPos blockPos1) {
        return new BlockPos(blockPos1.m_123341_(), this.getHeight(heightmapTypes0, blockPos1.m_123341_(), blockPos1.m_123343_()), blockPos1.m_123343_());
    }

    default boolean isEmptyBlock(BlockPos blockPos0) {
        return this.m_8055_(blockPos0).m_60795_();
    }

    default boolean canSeeSkyFromBelowWater(BlockPos blockPos0) {
        if (blockPos0.m_123342_() >= this.getSeaLevel()) {
            return this.m_45527_(blockPos0);
        } else {
            BlockPos $$1 = new BlockPos(blockPos0.m_123341_(), this.getSeaLevel(), blockPos0.m_123343_());
            if (!this.m_45527_($$1)) {
                return false;
            } else {
                for (BlockPos var4 = $$1.below(); var4.m_123342_() > blockPos0.m_123342_(); var4 = var4.below()) {
                    BlockState $$2 = this.m_8055_(var4);
                    if ($$2.m_60739_(this, var4) > 0 && !$$2.m_278721_()) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    default float getPathfindingCostFromLightLevels(BlockPos blockPos0) {
        return this.getLightLevelDependentMagicValue(blockPos0) - 0.5F;
    }

    @Deprecated
    default float getLightLevelDependentMagicValue(BlockPos blockPos0) {
        float $$1 = (float) this.getMaxLocalRawBrightness(blockPos0) / 15.0F;
        float $$2 = $$1 / (4.0F - 3.0F * $$1);
        return Mth.lerp(this.dimensionType().ambientLight(), $$2, 1.0F);
    }

    default ChunkAccess getChunk(BlockPos blockPos0) {
        return this.getChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
    }

    default ChunkAccess getChunk(int int0, int int1) {
        return this.getChunk(int0, int1, ChunkStatus.FULL, true);
    }

    default ChunkAccess getChunk(int int0, int int1, ChunkStatus chunkStatus2) {
        return this.getChunk(int0, int1, chunkStatus2, true);
    }

    @Nullable
    @Override
    default BlockGetter getChunkForCollisions(int int0, int int1) {
        return this.getChunk(int0, int1, ChunkStatus.EMPTY, false);
    }

    default boolean isWaterAt(BlockPos blockPos0) {
        return this.m_6425_(blockPos0).is(FluidTags.WATER);
    }

    default boolean containsAnyLiquid(AABB aABB0) {
        int $$1 = Mth.floor(aABB0.minX);
        int $$2 = Mth.ceil(aABB0.maxX);
        int $$3 = Mth.floor(aABB0.minY);
        int $$4 = Mth.ceil(aABB0.maxY);
        int $$5 = Mth.floor(aABB0.minZ);
        int $$6 = Mth.ceil(aABB0.maxZ);
        BlockPos.MutableBlockPos $$7 = new BlockPos.MutableBlockPos();
        for (int $$8 = $$1; $$8 < $$2; $$8++) {
            for (int $$9 = $$3; $$9 < $$4; $$9++) {
                for (int $$10 = $$5; $$10 < $$6; $$10++) {
                    BlockState $$11 = this.m_8055_($$7.set($$8, $$9, $$10));
                    if (!$$11.m_60819_().isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    default int getMaxLocalRawBrightness(BlockPos blockPos0) {
        return this.getMaxLocalRawBrightness(blockPos0, this.getSkyDarken());
    }

    default int getMaxLocalRawBrightness(BlockPos blockPos0, int int1) {
        return blockPos0.m_123341_() >= -30000000 && blockPos0.m_123343_() >= -30000000 && blockPos0.m_123341_() < 30000000 && blockPos0.m_123343_() < 30000000 ? this.m_45524_(blockPos0, int1) : 15;
    }

    @Deprecated
    default boolean hasChunkAt(int int0, int int1) {
        return this.hasChunk(SectionPos.blockToSectionCoord(int0), SectionPos.blockToSectionCoord(int1));
    }

    @Deprecated
    default boolean hasChunkAt(BlockPos blockPos0) {
        return this.hasChunkAt(blockPos0.m_123341_(), blockPos0.m_123343_());
    }

    @Deprecated
    default boolean hasChunksAt(BlockPos blockPos0, BlockPos blockPos1) {
        return this.hasChunksAt(blockPos0.m_123341_(), blockPos0.m_123342_(), blockPos0.m_123343_(), blockPos1.m_123341_(), blockPos1.m_123342_(), blockPos1.m_123343_());
    }

    @Deprecated
    default boolean hasChunksAt(int int0, int int1, int int2, int int3, int int4, int int5) {
        return int4 >= this.getMinBuildHeight() && int1 < this.m_151558_() ? this.hasChunksAt(int0, int2, int3, int5) : false;
    }

    @Deprecated
    default boolean hasChunksAt(int int0, int int1, int int2, int int3) {
        int $$4 = SectionPos.blockToSectionCoord(int0);
        int $$5 = SectionPos.blockToSectionCoord(int2);
        int $$6 = SectionPos.blockToSectionCoord(int1);
        int $$7 = SectionPos.blockToSectionCoord(int3);
        for (int $$8 = $$4; $$8 <= $$5; $$8++) {
            for (int $$9 = $$6; $$9 <= $$7; $$9++) {
                if (!this.hasChunk($$8, $$9)) {
                    return false;
                }
            }
        }
        return true;
    }

    RegistryAccess registryAccess();

    FeatureFlagSet enabledFeatures();

    default <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> resourceKeyExtendsRegistryExtendsT0) {
        Registry<T> $$1 = this.registryAccess().registryOrThrow(resourceKeyExtendsRegistryExtendsT0);
        return $$1.asLookup().filterFeatures(this.enabledFeatures());
    }
}