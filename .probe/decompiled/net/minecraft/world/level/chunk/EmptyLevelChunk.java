package net.minecraft.world.level.chunk;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class EmptyLevelChunk extends LevelChunk {

    private final Holder<Biome> biome;

    public EmptyLevelChunk(Level level0, ChunkPos chunkPos1, Holder<Biome> holderBiome2) {
        super(level0, chunkPos1);
        this.biome = holderBiome2;
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        return Blocks.VOID_AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos blockPos0, BlockState blockState1, boolean boolean2) {
        return null;
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        return Fluids.EMPTY.defaultFluidState();
    }

    @Override
    public int getLightEmission(BlockPos blockPos0) {
        return 0;
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0, LevelChunk.EntityCreationType levelChunkEntityCreationType1) {
        return null;
    }

    @Override
    public void addAndRegisterBlockEntity(BlockEntity blockEntity0) {
    }

    @Override
    public void setBlockEntity(BlockEntity blockEntity0) {
    }

    @Override
    public void removeBlockEntity(BlockPos blockPos0) {
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean isYSpaceEmpty(int int0, int int1) {
        return true;
    }

    @Override
    public FullChunkStatus getFullStatus() {
        return FullChunkStatus.FULL;
    }

    @Override
    public Holder<Biome> getNoiseBiome(int int0, int int1, int int2) {
        return this.biome;
    }
}