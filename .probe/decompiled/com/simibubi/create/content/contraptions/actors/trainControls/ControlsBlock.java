package com.simibubi.create.content.contraptions.actors.trainControls;

import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.ContraptionWorld;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ControlsBlock extends HorizontalDirectionalBlock implements IWrenchable, ProperWaterloggedBlock {

    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public static final BooleanProperty VIRTUAL = BooleanProperty.create("virtual");

    public ControlsBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(OPEN, false)).m_61124_(WATERLOGGED, false)).m_61124_(VIRTUAL, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(f_54117_, OPEN, WATERLOGGED, VIRTUAL));
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pNeighborPos) {
        this.updateWater(pLevel, pState, pCurrentPos);
        return (BlockState) pState.m_61124_(OPEN, pLevel instanceof ContraptionWorld);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return this.fluidState(pState);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = this.withWater(super.m_5573_(pContext), pContext);
        Direction horizontalDirection = pContext.m_8125_();
        Player player = pContext.m_43723_();
        state = (BlockState) state.m_61124_(f_54117_, horizontalDirection.getOpposite());
        if (player != null && player.m_6144_()) {
            state = (BlockState) state.m_61124_(f_54117_, horizontalDirection);
        }
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return AllShapes.CONTROLS.get((Direction) pState.m_61143_(f_54117_));
    }
}