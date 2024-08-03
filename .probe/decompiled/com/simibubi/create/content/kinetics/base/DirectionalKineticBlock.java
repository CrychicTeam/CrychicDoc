package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public abstract class DirectionalKineticBlock extends KineticBlock {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public DirectionalKineticBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.m_7926_(builder);
    }

    public Direction getPreferredFacing(BlockPlaceContext context) {
        Direction prefferedSide = null;
        for (Direction side : Iterate.directions) {
            BlockState blockState = context.m_43725_().getBlockState(context.getClickedPos().relative(side));
            if (blockState.m_60734_() instanceof IRotate && ((IRotate) blockState.m_60734_()).hasShaftTowards(context.m_43725_(), context.getClickedPos().relative(side), blockState, side.getOpposite())) {
                if (prefferedSide != null && prefferedSide.getAxis() != side.getAxis()) {
                    prefferedSide = null;
                    break;
                }
                prefferedSide = side;
            }
        }
        return prefferedSide;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = this.getPreferredFacing(context);
        if (preferred == null || context.m_43723_() != null && context.m_43723_().m_6144_()) {
            Direction nearestLookingDirection = context.getNearestLookingDirection();
            return (BlockState) this.m_49966_().m_61124_(FACING, context.m_43723_() != null && context.m_43723_().m_6144_() ? nearestLookingDirection : nearestLookingDirection.getOpposite());
        } else {
            return (BlockState) this.m_49966_().m_61124_(FACING, preferred.getOpposite());
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }
}