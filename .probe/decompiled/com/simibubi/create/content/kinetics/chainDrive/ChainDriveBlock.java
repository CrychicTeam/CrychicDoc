package com.simibubi.create.content.kinetics.chainDrive;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.PushReaction;

public class ChainDriveBlock extends RotatedPillarKineticBlock implements IBE<KineticBlockEntity>, ITransformableBlock {

    public static final Property<ChainDriveBlock.Part> PART = EnumProperty.create("part", ChainDriveBlock.Part.class);

    public static final BooleanProperty CONNECTED_ALONG_FIRST_COORDINATE = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public ChainDriveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(PART, ChainDriveBlock.Part.NONE));
    }

    public boolean shouldCheckWeakPower(BlockState state, SignalGetter level, BlockPos pos, Direction side) {
        return false;
    }

    public PushReaction getPistonPushReaction(BlockState state) {
        return PushReaction.NORMAL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(PART, CONNECTED_ALONG_FIRST_COORDINATE));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction.Axis placedAxis = context.getNearestLookingDirection().getAxis();
        Direction.Axis axis = context.m_43723_() != null && context.m_43723_().m_6144_() ? placedAxis : getPreferredAxis(context);
        if (axis == null) {
            axis = placedAxis;
        }
        BlockState state = (BlockState) this.m_49966_().m_61124_(AXIS, axis);
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() != axis) {
                BlockPos pos = context.getClickedPos();
                BlockPos offset = pos.relative(facing);
                state = this.updateShape(state, facing, context.m_43725_().getBlockState(offset), context.m_43725_(), pos, offset);
            }
        }
        return state;
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction face, BlockState neighbour, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        ChainDriveBlock.Part part = (ChainDriveBlock.Part) stateIn.m_61143_(PART);
        Direction.Axis axis = (Direction.Axis) stateIn.m_61143_(AXIS);
        boolean connectionAlongFirst = (Boolean) stateIn.m_61143_(CONNECTED_ALONG_FIRST_COORDINATE);
        Direction.Axis connectionAxis = connectionAlongFirst ? (axis == Direction.Axis.X ? Direction.Axis.Y : Direction.Axis.X) : (axis == Direction.Axis.Z ? Direction.Axis.Y : Direction.Axis.Z);
        Direction.Axis faceAxis = face.getAxis();
        boolean facingAlongFirst = axis == Direction.Axis.X ? faceAxis.isVertical() : faceAxis == Direction.Axis.X;
        boolean positive = face.getAxisDirection() == Direction.AxisDirection.POSITIVE;
        if (axis == faceAxis) {
            return stateIn;
        } else if (!(neighbour.m_60734_() instanceof ChainDriveBlock)) {
            if (facingAlongFirst != connectionAlongFirst || part == ChainDriveBlock.Part.NONE) {
                return stateIn;
            } else if (part == ChainDriveBlock.Part.MIDDLE) {
                return (BlockState) stateIn.m_61124_(PART, positive ? ChainDriveBlock.Part.END : ChainDriveBlock.Part.START);
            } else {
                return part == ChainDriveBlock.Part.START == positive ? (BlockState) stateIn.m_61124_(PART, ChainDriveBlock.Part.NONE) : stateIn;
            }
        } else {
            ChainDriveBlock.Part otherPart = (ChainDriveBlock.Part) neighbour.m_61143_(PART);
            Direction.Axis otherAxis = (Direction.Axis) neighbour.m_61143_(AXIS);
            boolean otherConnection = (Boolean) neighbour.m_61143_(CONNECTED_ALONG_FIRST_COORDINATE);
            Direction.Axis otherConnectionAxis = otherConnection ? (otherAxis == Direction.Axis.X ? Direction.Axis.Y : Direction.Axis.X) : (otherAxis == Direction.Axis.Z ? Direction.Axis.Y : Direction.Axis.Z);
            if (neighbour.m_61143_(AXIS) == faceAxis) {
                return stateIn;
            } else if (otherPart != ChainDriveBlock.Part.NONE && otherConnectionAxis != faceAxis) {
                return stateIn;
            } else {
                if (part == ChainDriveBlock.Part.NONE) {
                    part = positive ? ChainDriveBlock.Part.START : ChainDriveBlock.Part.END;
                    connectionAlongFirst = axis == Direction.Axis.X ? faceAxis.isVertical() : faceAxis == Direction.Axis.X;
                } else if (connectionAxis != faceAxis) {
                    return stateIn;
                }
                if (part == ChainDriveBlock.Part.START != positive) {
                    part = ChainDriveBlock.Part.MIDDLE;
                }
                return (BlockState) ((BlockState) stateIn.m_61124_(PART, part)).m_61124_(CONNECTED_ALONG_FIRST_COORDINATE, connectionAlongFirst);
            }
        }
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        return originalState.m_61143_(PART) == ChainDriveBlock.Part.NONE ? super.getRotatedBlockState(originalState, targetedFace) : super.getRotatedBlockState(originalState, Direction.get(Direction.AxisDirection.POSITIVE, getConnectionAxis(originalState)));
    }

    @Override
    public BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        Direction.Axis axis = (Direction.Axis) newState.m_61143_(AXIS);
        newState = (BlockState) this.m_49966_().m_61124_(AXIS, axis);
        if (newState.m_61138_(BlockStateProperties.POWERED)) {
            newState = (BlockState) newState.m_61124_(BlockStateProperties.POWERED, context.getLevel().m_276867_(context.getClickedPos()));
        }
        for (Direction facing : Iterate.directions) {
            if (facing.getAxis() != axis) {
                BlockPos pos = context.getClickedPos();
                BlockPos offset = pos.relative(facing);
                newState = this.updateShape(newState, facing, context.getLevel().getBlockState(offset), context.getLevel(), pos, offset);
            }
        }
        return newState;
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face.getAxis() == state.m_61143_(AXIS);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return (Direction.Axis) state.m_61143_(AXIS);
    }

    public static boolean areBlocksConnected(BlockState state, BlockState other, Direction facing) {
        ChainDriveBlock.Part part = (ChainDriveBlock.Part) state.m_61143_(PART);
        Direction.Axis connectionAxis = getConnectionAxis(state);
        Direction.Axis otherConnectionAxis = getConnectionAxis(other);
        if (otherConnectionAxis != connectionAxis) {
            return false;
        } else if (facing.getAxis() != connectionAxis) {
            return false;
        } else {
            return facing.getAxisDirection() != Direction.AxisDirection.POSITIVE || part != ChainDriveBlock.Part.MIDDLE && part != ChainDriveBlock.Part.START ? facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE && (part == ChainDriveBlock.Part.MIDDLE || part == ChainDriveBlock.Part.END) : true;
        }
    }

    protected static Direction.Axis getConnectionAxis(BlockState state) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(AXIS);
        boolean connectionAlongFirst = (Boolean) state.m_61143_(CONNECTED_ALONG_FIRST_COORDINATE);
        return connectionAlongFirst ? (axis == Direction.Axis.X ? Direction.Axis.Y : Direction.Axis.X) : (axis == Direction.Axis.Z ? Direction.Axis.Y : Direction.Axis.Z);
    }

    public static float getRotationSpeedModifier(KineticBlockEntity from, KineticBlockEntity to) {
        float fromMod = 1.0F;
        float toMod = 1.0F;
        if (from instanceof ChainGearshiftBlockEntity) {
            fromMod = ((ChainGearshiftBlockEntity) from).getModifier();
        }
        if (to instanceof ChainGearshiftBlockEntity) {
            toMod = ((ChainGearshiftBlockEntity) to).getModifier();
        }
        return fromMod / toMod;
    }

    @Override
    public Class<KineticBlockEntity> getBlockEntityClass() {
        return KineticBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends KineticBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends KineticBlockEntity>) AllBlockEntityTypes.ENCASED_SHAFT.get();
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return this.rotate(state, rot, Direction.Axis.Y);
    }

    protected BlockState rotate(BlockState pState, Rotation rot, Direction.Axis rotAxis) {
        Direction.Axis connectionAxis = getConnectionAxis(pState);
        Direction direction = Direction.fromAxisAndDirection(connectionAxis, Direction.AxisDirection.POSITIVE);
        Direction normal = Direction.fromAxisAndDirection((Direction.Axis) pState.m_61143_(AXIS), Direction.AxisDirection.POSITIVE);
        for (int i = 0; i < rot.ordinal(); i++) {
            direction = direction.getClockWise(rotAxis);
            normal = normal.getClockWise(rotAxis);
        }
        if (direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
            pState = this.reversePart(pState);
        }
        Direction.Axis newAxis = normal.getAxis();
        Direction.Axis newConnectingDirection = direction.getAxis();
        boolean alongFirst = newAxis == Direction.Axis.X && newConnectingDirection == Direction.Axis.Y || newAxis != Direction.Axis.X && newConnectingDirection == Direction.Axis.X;
        return (BlockState) ((BlockState) pState.m_61124_(AXIS, newAxis)).m_61124_(CONNECTED_ALONG_FIRST_COORDINATE, alongFirst);
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        Direction.Axis connectionAxis = getConnectionAxis(pState);
        return pMirror.mirror(Direction.fromAxisAndDirection(connectionAxis, Direction.AxisDirection.POSITIVE)).getAxisDirection() == Direction.AxisDirection.POSITIVE ? pState : this.reversePart(pState);
    }

    protected BlockState reversePart(BlockState pState) {
        ChainDriveBlock.Part part = (ChainDriveBlock.Part) pState.m_61143_(PART);
        if (part == ChainDriveBlock.Part.START) {
            return (BlockState) pState.m_61124_(PART, ChainDriveBlock.Part.END);
        } else {
            return part == ChainDriveBlock.Part.END ? (BlockState) pState.m_61124_(PART, ChainDriveBlock.Part.START) : pState;
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        return this.rotate(this.mirror(state, transform.mirror), transform.rotation, transform.rotationAxis);
    }

    public static enum Part implements StringRepresentable {

        START, MIDDLE, END, NONE;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}