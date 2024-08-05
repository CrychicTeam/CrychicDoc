package org.embeddedt.modernfix.chunk;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class SafeBlockGetter implements BlockGetter {

    private final ServerLevel wrapped;

    private final Thread mainThread;

    public SafeBlockGetter(ServerLevel wrapped) {
        this.wrapped = wrapped;
        this.mainThread = Thread.currentThread();
    }

    public boolean shouldUse() {
        return Thread.currentThread() != this.mainThread;
    }

    @Nullable
    private BlockGetter getChunkSafe(BlockPos pos) {
        BlockGetter access = this.wrapped.getChunkSource().getChunkForLighting(pos.m_123341_() >> 4, pos.m_123343_() >> 4);
        if (!(access instanceof ChunkAccess chunk)) {
            return null;
        } else {
            return !chunk.getStatus().isOrAfter(ChunkStatus.FULL) ? null : chunk;
        }
    }

    @Override
    public int getMaxBuildHeight() {
        return this.wrapped.m_151558_();
    }

    @Override
    public int getMaxLightLevel() {
        return this.wrapped.m_7469_();
    }

    @Override
    public int getMinBuildHeight() {
        return this.wrapped.m_141937_();
    }

    @Override
    public int getHeight() {
        return this.wrapped.m_141928_();
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        BlockGetter g = this.getChunkSafe(pos);
        return g == null ? null : g.getBlockEntity(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        BlockGetter g = this.getChunkSafe(pos);
        return g == null ? Blocks.AIR.defaultBlockState() : g.getBlockState(pos);
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        BlockGetter g = this.getChunkSafe(pos);
        return g == null ? Fluids.EMPTY.defaultFluidState() : g.getFluidState(pos);
    }
}