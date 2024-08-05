package com.simibubi.create.content.kinetics.simpleRelays;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathComputationType;

public abstract class AbstractShaftBlock extends RotatedPillarKineticBlock implements IBE<KineticBlockEntity>, ProperWaterloggedBlock {

    public AbstractShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) super.m_49966_().m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.m_61143_(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return this.fluidState(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(BlockStateProperties.WATERLOGGED));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world, BlockPos pos, BlockPos neighbourPos) {
        this.updateWater(world, state, pos);
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.withWater(super.getStateForPlacement(context), context);
    }
}