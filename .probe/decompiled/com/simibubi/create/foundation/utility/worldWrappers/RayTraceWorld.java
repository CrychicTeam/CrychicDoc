package com.simibubi.create.foundation.utility.worldWrappers;

import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

public class RayTraceWorld implements BlockGetter {

    private final LevelAccessor template;

    private final BiFunction<BlockPos, BlockState, BlockState> stateGetter;

    public RayTraceWorld(LevelAccessor template, BiFunction<BlockPos, BlockState, BlockState> stateGetter) {
        this.template = template;
        this.stateGetter = stateGetter;
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos pos) {
        return this.template.m_7702_(pos);
    }

    @Override
    public BlockState getBlockState(BlockPos pos) {
        return (BlockState) this.stateGetter.apply(pos, this.template.m_8055_(pos));
    }

    @Override
    public FluidState getFluidState(BlockPos pos) {
        return this.template.m_6425_(pos);
    }

    @Override
    public int getHeight() {
        return this.template.m_141928_();
    }

    @Override
    public int getMinBuildHeight() {
        return this.template.m_141937_();
    }
}