package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ThinGeothermalVentBlock extends GeothermalVentBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final VoxelShape shape;

    public ThinGeothermalVentBlock(int pixWidth) {
        this.shape = Block.box((double) (8 - pixWidth / 2), 0.0, (double) (8 - pixWidth / 2), (double) (8 + pixWidth / 2), 16.0, (double) (8 + pixWidth / 2));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(SMOKE_TYPE, 0)).m_61124_(SPAWNING_PARTICLES, true));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.shape;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) super.getStateForPlacement(context).m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, SMOKE_TYPE, SPAWNING_PARTICLES);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }
}