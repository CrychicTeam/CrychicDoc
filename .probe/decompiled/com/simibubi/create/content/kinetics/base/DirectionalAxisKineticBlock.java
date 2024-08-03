package com.simibubi.create.content.kinetics.base;

import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class DirectionalAxisKineticBlock extends DirectionalKineticBlock implements ITransformableBlock {

    public static final BooleanProperty AXIS_ALONG_FIRST_COORDINATE = BooleanProperty.create("axis_along_first");

    public DirectionalAxisKineticBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS_ALONG_FIRST_COORDINATE);
        super.createBlockStateDefinition(builder);
    }

    protected Direction getFacingForPlacement(BlockPlaceContext context) {
        Direction facing = context.getNearestLookingDirection().getOpposite();
        if (context.m_43723_() != null && context.m_43723_().m_6144_()) {
            facing = facing.getOpposite();
        }
        return facing;
    }

    protected boolean getAxisAlignmentForPlacement(BlockPlaceContext context) {
        return context.m_8125_().getAxis() == Direction.Axis.X;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = this.getFacingForPlacement(context);
        BlockPos pos = context.getClickedPos();
        Level world = context.m_43725_();
        boolean alongFirst = false;
        Direction.Axis faceAxis = facing.getAxis();
        if (faceAxis.isHorizontal()) {
            alongFirst = faceAxis == Direction.Axis.Z;
            Direction positivePerpendicular = faceAxis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
            boolean shaftAbove = this.prefersConnectionTo(world, pos, Direction.UP, true);
            boolean shaftBelow = this.prefersConnectionTo(world, pos, Direction.DOWN, true);
            boolean preferLeft = this.prefersConnectionTo(world, pos, positivePerpendicular, false);
            boolean preferRight = this.prefersConnectionTo(world, pos, positivePerpendicular.getOpposite(), false);
            if (shaftAbove || shaftBelow || preferLeft || preferRight) {
                alongFirst = faceAxis == Direction.Axis.X;
            }
        }
        if (faceAxis.isVertical()) {
            alongFirst = this.getAxisAlignmentForPlacement(context);
            Direction prefferedSide = null;
            for (Direction side : Iterate.horizontalDirections) {
                if (this.prefersConnectionTo(world, pos, side, true) || this.prefersConnectionTo(world, pos, side.getClockWise(), false)) {
                    if (prefferedSide != null && prefferedSide.getAxis() != side.getAxis()) {
                        prefferedSide = null;
                        break;
                    }
                    prefferedSide = side;
                }
            }
            if (prefferedSide != null) {
                alongFirst = prefferedSide.getAxis() == Direction.Axis.X;
            }
        }
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, facing)).m_61124_(AXIS_ALONG_FIRST_COORDINATE, alongFirst);
    }

    protected boolean prefersConnectionTo(LevelReader reader, BlockPos pos, Direction facing, boolean shaftAxis) {
        if (!shaftAxis) {
            return false;
        } else {
            BlockPos neighbourPos = pos.relative(facing);
            BlockState blockState = reader.m_8055_(neighbourPos);
            Block block = blockState.m_60734_();
            return block instanceof IRotate && ((IRotate) block).hasShaftTowards(reader, neighbourPos, blockState, facing.getOpposite());
        }
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        Direction.Axis pistonAxis = ((Direction) state.m_61143_(FACING)).getAxis();
        boolean alongFirst = (Boolean) state.m_61143_(AXIS_ALONG_FIRST_COORDINATE);
        if (pistonAxis == Direction.Axis.X) {
            return alongFirst ? Direction.Axis.Y : Direction.Axis.Z;
        } else if (pistonAxis == Direction.Axis.Y) {
            return alongFirst ? Direction.Axis.X : Direction.Axis.Z;
        } else if (pistonAxis == Direction.Axis.Z) {
            return alongFirst ? Direction.Axis.X : Direction.Axis.Y;
        } else {
            throw new IllegalStateException("Unknown axis??");
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (rot.ordinal() % 2 == 1) {
            state = (BlockState) state.m_61122_(AXIS_ALONG_FIRST_COORDINATE);
        }
        return super.rotate(state, rot);
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = this.m_6943_(state, transform.mirror);
        }
        if (transform.rotationAxis == Direction.Axis.Y) {
            return this.rotate(state, transform.rotation);
        } else {
            Direction newFacing = transform.rotateFacing((Direction) state.m_61143_(FACING));
            if (transform.rotationAxis == newFacing.getAxis() && transform.rotation.ordinal() % 2 == 1) {
                state = (BlockState) state.m_61122_(AXIS_ALONG_FIRST_COORDINATE);
            }
            return (BlockState) state.m_61124_(FACING, newFacing);
        }
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == this.getRotationAxis(state);
    }
}