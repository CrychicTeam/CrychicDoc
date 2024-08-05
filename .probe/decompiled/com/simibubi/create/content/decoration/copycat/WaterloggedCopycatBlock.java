package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;

public abstract class WaterloggedCopycatBlock extends CopycatBlock implements ProperWaterloggedBlock {

    public WaterloggedCopycatBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(WATERLOGGED));
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.withWater(super.m_5573_(pContext), pContext);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return pState;
    }
}