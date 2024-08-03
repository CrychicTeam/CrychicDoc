package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class RotatedPillarKineticBlock extends KineticBlock {

    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public RotatedPillarKineticBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
            case COUNTERCLOCKWISE_90:
            case CLOCKWISE_90:
                switch((Direction.Axis) state.m_61143_(AXIS)) {
                    case X:
                        return (BlockState) state.m_61124_(AXIS, Direction.Axis.Z);
                    case Z:
                        return (BlockState) state.m_61124_(AXIS, Direction.Axis.X);
                    default:
                        return state;
                }
            default:
                return state;
        }
    }

    public static Direction.Axis getPreferredAxis(BlockPlaceContext context) {
        Direction.Axis prefferedAxis = null;
        for (Direction side : Iterate.directions) {
            BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos().relative(side));
            if (blockState.m_60734_() instanceof IRotate && ((IRotate) blockState.m_60734_()).hasShaftTowards(context.m_43725_(), context.getClickedPos().relative(side), blockState, side.getOpposite())) {
                if (prefferedAxis != null && prefferedAxis != side.getAxis()) {
                    prefferedAxis = null;
                    break;
                }
                prefferedAxis = side.getAxis();
            }
        }
        return prefferedAxis;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis preferredAxis = getPreferredAxis(context);
        return preferredAxis == null || context.m_43723_() != null && context.m_43723_().m_6144_() ? (BlockState) this.m_49966_().m_61124_(AXIS, preferredAxis != null && context.m_43723_().m_6144_() ? context.m_43719_().getAxis() : context.getNearestLookingDirection().getAxis()) : (BlockState) this.m_49966_().m_61124_(AXIS, preferredAxis);
    }
}