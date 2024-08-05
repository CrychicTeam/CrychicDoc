package com.simibubi.create.foundation.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public interface ProperWaterloggedBlock extends SimpleWaterloggedBlock {

    BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    default FluidState fluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : Fluids.EMPTY.defaultFluidState();
    }

    default void updateWater(LevelAccessor level, BlockState state, BlockPos pos) {
        if ((Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.m_6718_(level));
        }
    }

    default BlockState withWater(BlockState placementState, BlockPlaceContext ctx) {
        return withWater(ctx.m_43725_(), placementState, ctx.getClickedPos());
    }

    static BlockState withWater(LevelAccessor level, BlockState placementState, BlockPos pos) {
        if (placementState == null) {
            return null;
        } else {
            FluidState ifluidstate = level.m_6425_(pos);
            if (placementState.m_60795_()) {
                return ifluidstate.getType() == Fluids.WATER ? ifluidstate.createLegacyBlock() : placementState;
            } else {
                return !(placementState.m_60734_() instanceof SimpleWaterloggedBlock) ? placementState : (BlockState) placementState.m_61124_(BlockStateProperties.WATERLOGGED, ifluidstate.getType() == Fluids.WATER);
            }
        }
    }
}