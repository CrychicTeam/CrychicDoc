package com.simibubi.create.content.kinetics.base;

import javax.annotation.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;

@MethodsReturnNonnullByDefault
public abstract class AbstractEncasedShaftBlock extends RotatedPillarKineticBlock {

    public AbstractEncasedShaftBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    public PushReaction getPistonPushReaction(@Nullable BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        if (context.m_43723_() != null && context.m_43723_().m_6144_()) {
            return super.getStateForPlacement(context);
        } else {
            Direction.Axis preferredAxis = getPreferredAxis(context);
            return (BlockState) this.m_49966_().m_61124_(AXIS, preferredAxis == null ? context.getNearestLookingDirection().getAxis() : preferredAxis);
        }
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.m_61143_(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }
}