package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import com.github.alexthe666.citadel.server.world.WorldChunkUtil;
import com.mojang.datafixers.util.Either;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ChunkCache implements LevelReader {

    private final DimensionType dimType;

    protected int chunkX;

    protected int chunkZ;

    protected LevelChunk[][] chunkArray;

    protected boolean empty;

    protected Level world;

    private final int minBuildHeight;

    private final int maxBuildHeight;

    public ChunkCache(Level worldIn, BlockPos posFromIn, BlockPos posToIn, int subIn, DimensionType type) {
        this.world = worldIn;
        this.chunkX = posFromIn.m_123341_() - subIn >> 4;
        this.chunkZ = posFromIn.m_123343_() - subIn >> 4;
        int i = posToIn.m_123341_() + subIn >> 4;
        int j = posToIn.m_123343_() + subIn >> 4;
        this.chunkArray = new LevelChunk[i - this.chunkX + 1][j - this.chunkZ + 1];
        this.empty = true;
        for (int k = this.chunkX; k <= i; k++) {
            for (int l = this.chunkZ; l <= j; l++) {
                if (WorldChunkUtil.isEntityChunkLoaded(this.world, new ChunkPos(k, l))) {
                    ChunkSource holder = worldIn.m_7726_();
                    if (holder instanceof ServerChunkCache) {
                        ServerChunkCache serverChunkCache = (ServerChunkCache) holder;
                        ChunkHolder holderx = serverChunkCache.chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(k, l));
                        if (holderx != null) {
                            this.chunkArray[k - this.chunkX][l - this.chunkZ] = (LevelChunk) ((Either) holderx.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left().orElse(null);
                        }
                    }
                }
            }
        }
        this.dimType = type;
        this.minBuildHeight = worldIn.m_141937_();
        this.maxBuildHeight = worldIn.m_151558_();
    }

    public boolean isEmpty() {
        return this.empty;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(@NotNull BlockPos pos) {
        return this.getTileEntity(pos, LevelChunk.EntityCreationType.CHECK);
    }

    @Nullable
    public BlockEntity getTileEntity(BlockPos pos, LevelChunk.EntityCreationType createType) {
        int i = (pos.m_123341_() >> 4) - this.chunkX;
        int j = (pos.m_123343_() >> 4) - this.chunkZ;
        return !this.withinBounds(i, j) ? null : this.chunkArray[i][j].getBlockEntity(pos, createType);
    }

    @Override
    public int getMinBuildHeight() {
        return this.minBuildHeight;
    }

    @Override
    public int getMaxBuildHeight() {
        return this.maxBuildHeight;
    }

    @NotNull
    @Override
    public BlockState getBlockState(BlockPos pos) {
        if (pos.m_123342_() >= this.getMinBuildHeight() && pos.m_123342_() < this.getMaxBuildHeight()) {
            int i = (pos.m_123341_() >> 4) - this.chunkX;
            int j = (pos.m_123343_() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                LevelChunk chunk = this.chunkArray[i][j];
                if (chunk != null) {
                    return chunk.getBlockState(pos);
                }
            }
        }
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        if (pos.m_123342_() >= this.getMinBuildHeight() && pos.m_123342_() < this.getMaxBuildHeight()) {
            int i = (pos.m_123341_() >> 4) - this.chunkX;
            int j = (pos.m_123343_() >> 4) - this.chunkZ;
            if (i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length) {
                LevelChunk chunk = this.chunkArray[i][j];
                if (chunk != null) {
                    return chunk.getFluidState(pos);
                }
            }
        }
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int x, int y, int z) {
        return null;
    }

    @Override
    public boolean isEmptyBlock(BlockPos pos) {
        BlockState state = this.getBlockState(pos);
        return state.m_60795_();
    }

    @Nullable
    @Override
    public ChunkAccess getChunk(int x, int z, ChunkStatus requiredStatus, boolean nonnull) {
        int i = x - this.chunkX;
        int j = z - this.chunkZ;
        return i >= 0 && i < this.chunkArray.length && j >= 0 && j < this.chunkArray[i].length ? this.chunkArray[i][j] : null;
    }

    @Override
    public boolean hasChunk(int chunkX, int chunkZ) {
        return false;
    }

    @Override
    public BlockPos getHeightmapPos(Heightmap.Types heightmapType, BlockPos pos) {
        return null;
    }

    @Override
    public int getHeight(Heightmap.Types heightmapType, int x, int z) {
        return 0;
    }

    @Override
    public int getSkyDarken() {
        return 0;
    }

    @Override
    public BiomeManager getBiomeManager() {
        return null;
    }

    @Override
    public WorldBorder getWorldBorder() {
        return null;
    }

    @Override
    public boolean isUnobstructed(@Nullable Entity entityIn, VoxelShape shape) {
        return false;
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@org.jetbrains.annotations.Nullable Entity entity0, AABB aABB1) {
        return null;
    }

    @Override
    public int getDirectSignal(BlockPos pos, Direction direction) {
        return this.getBlockState(pos).m_60775_(this, pos, direction);
    }

    @Override
    public RegistryAccess registryAccess() {
        return RegistryAccess.EMPTY;
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return FeatureFlagSet.of();
    }

    @Override
    public boolean isClientSide() {
        return false;
    }

    @Override
    public int getSeaLevel() {
        return 0;
    }

    @NotNull
    @Override
    public DimensionType dimensionType() {
        return this.dimType;
    }

    private boolean withinBounds(int x, int z) {
        return x >= 0 && x < this.chunkArray.length && z >= 0 && z < this.chunkArray[x].length && this.chunkArray[x][z] != null;
    }

    @Override
    public float getShade(Direction direction, boolean b) {
        return 0.0F;
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return null;
    }
}