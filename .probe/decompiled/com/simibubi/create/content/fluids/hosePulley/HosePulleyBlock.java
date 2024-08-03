package com.simibubi.create.content.fluids.hosePulley;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.fluids.pipes.FluidPipeBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class HosePulleyBlock extends HorizontalKineticBlock implements IBE<HosePulleyBlockEntity> {

    public HosePulleyBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise().getAxis();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferredHorizontalFacing = this.getPreferredHorizontalFacing(context);
        return (BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, preferredHorizontalFacing != null ? preferredHorizontalFacing.getCounterClockWise() : context.m_8125_().getOpposite());
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise() == face;
    }

    public static boolean hasPipeTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getCounterClockWise() == face;
    }

    @Override
    public Direction getPreferredHorizontalFacing(BlockPlaceContext context) {
        Direction fromParent = super.getPreferredHorizontalFacing(context);
        if (fromParent != null) {
            return fromParent;
        } else {
            Direction prefferedSide = null;
            for (Direction facing : Iterate.horizontalDirections) {
                BlockPos pos = context.getClickedPos().relative(facing);
                BlockState blockState = context.m_43725_().getBlockState(pos);
                if (FluidPipeBlock.canConnectTo(context.m_43725_(), pos, blockState, facing)) {
                    if (prefferedSide != null && prefferedSide.getAxis() != facing.getAxis()) {
                        prefferedSide = null;
                        break;
                    }
                    prefferedSide = facing;
                }
            }
            return prefferedSide == null ? null : prefferedSide.getOpposite();
        }
    }

    @Override
    public Class<HosePulleyBlockEntity> getBlockEntityClass() {
        return HosePulleyBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends HosePulleyBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends HosePulleyBlockEntity>) AllBlockEntityTypes.HOSE_PULLEY.get();
    }
}