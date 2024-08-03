package com.simibubi.create.foundation.utility.worldWrappers;

import java.util.HashMap;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class PlacementSimulationServerWorld extends WrappedServerWorld {

    public HashMap<BlockPos, BlockState> blocksAdded = new HashMap();

    public PlacementSimulationServerWorld(ServerLevel wrapped) {
        super(wrapped);
    }

    public void clear() {
        this.blocksAdded.clear();
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState newState, int flags) {
        this.blocksAdded.put(pos.immutable(), newState);
        return true;
    }

    @Override
    public boolean setBlockAndUpdate(BlockPos pos, BlockState state) {
        return this.setBlock(pos, state, 0);
    }

    @Override
    public boolean isStateAtPosition(BlockPos pos, Predicate<BlockState> condition) {
        return condition.test(this.getBlockState(pos));
    }

    @Override
    public boolean isLoaded(BlockPos pos) {
        return true;
    }

    public boolean isAreaLoaded(BlockPos center, int range) {
        return true;
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return this.blocksAdded.containsKey(pos) ? (BlockState) this.blocksAdded.get(pos) : Blocks.AIR.defaultBlockState();
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.getBlockState(pos).m_60819_();
    }
}