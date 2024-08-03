package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;

public abstract class HorizontalAxisKineticBlock extends KineticBlock {

    public static final Property<Direction.Axis> HORIZONTAL_AXIS = BlockStateProperties.HORIZONTAL_AXIS;

    public HorizontalAxisKineticBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HORIZONTAL_AXIS);
        super.m_7926_(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis preferredAxis = getPreferredHorizontalAxis(context);
        return preferredAxis != null ? (BlockState) this.m_49966_().m_61124_(HORIZONTAL_AXIS, preferredAxis) : (BlockState) this.m_49966_().m_61124_(HORIZONTAL_AXIS, context.m_8125_().getClockWise().getAxis());
    }

    public static Direction.Axis getPreferredHorizontalAxis(BlockPlaceContext context) {
        Direction prefferedSide = null;
        for (Direction side : Iterate.horizontalDirections) {
            BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos().relative(side));
            if (blockState.m_60734_() instanceof IRotate && ((IRotate) blockState.m_60734_()).hasShaftTowards(context.m_43725_(), context.getClickedPos().relative(side), blockState, side.getOpposite())) {
                if (prefferedSide != null && prefferedSide.getAxis() != side.getAxis()) {
                    prefferedSide = null;
                    break;
                }
                prefferedSide = side;
            }
        }
        return prefferedSide == null ? null : prefferedSide.getAxis();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(HORIZONTAL_AXIS);
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.m_61143_(HORIZONTAL_AXIS);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(HORIZONTAL_AXIS);
        return (BlockState) state.m_61124_(HORIZONTAL_AXIS, rot.rotate(Direction.get(Direction.AxisDirection.POSITIVE, axis)).getAxis());
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state;
    }
}