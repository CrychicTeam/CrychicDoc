package com.simibubi.create.content.contraptions;

import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

public class StructureTransform {

    public Direction.Axis rotationAxis;

    public BlockPos offset;

    public int angle;

    public Rotation rotation;

    public Mirror mirror;

    private StructureTransform(BlockPos offset, int angle, Direction.Axis axis, Rotation rotation, Mirror mirror) {
        this.offset = offset;
        this.angle = angle;
        this.rotationAxis = axis;
        this.rotation = rotation;
        this.mirror = mirror;
    }

    public StructureTransform(BlockPos offset, Direction.Axis axis, Rotation rotation, Mirror mirror) {
        this(offset, rotation == Rotation.NONE ? 0 : (4 - rotation.ordinal()) * 90, axis, rotation, mirror);
    }

    public StructureTransform(BlockPos offset, float xRotation, float yRotation, float zRotation) {
        this.offset = offset;
        if (xRotation != 0.0F) {
            this.rotationAxis = Direction.Axis.X;
            this.angle = Math.round(xRotation / 90.0F) * 90;
        }
        if (yRotation != 0.0F) {
            this.rotationAxis = Direction.Axis.Y;
            this.angle = Math.round(yRotation / 90.0F) * 90;
        }
        if (zRotation != 0.0F) {
            this.rotationAxis = Direction.Axis.Z;
            this.angle = Math.round(zRotation / 90.0F) * 90;
        }
        this.angle %= 360;
        if (this.angle < -90) {
            this.angle += 360;
        }
        this.rotation = Rotation.NONE;
        if (this.angle == -90 || this.angle == 270) {
            this.rotation = Rotation.CLOCKWISE_90;
        }
        if (this.angle == 90) {
            this.rotation = Rotation.COUNTERCLOCKWISE_90;
        }
        if (this.angle == 180) {
            this.rotation = Rotation.CLOCKWISE_180;
        }
        this.mirror = Mirror.NONE;
    }

    public Vec3 applyWithoutOffsetUncentered(Vec3 localVec) {
        Vec3 vec = localVec;
        if (this.mirror != null) {
            vec = VecHelper.mirror(localVec, this.mirror);
        }
        if (this.rotationAxis != null) {
            vec = VecHelper.rotate(vec, (double) this.angle, this.rotationAxis);
        }
        return vec;
    }

    public Vec3 applyWithoutOffset(Vec3 localVec) {
        Vec3 vec = localVec;
        if (this.mirror != null) {
            vec = VecHelper.mirrorCentered(localVec, this.mirror);
        }
        if (this.rotationAxis != null) {
            vec = VecHelper.rotateCentered(vec, (double) this.angle, this.rotationAxis);
        }
        return vec;
    }

    public Vec3 apply(Vec3 localVec) {
        return this.applyWithoutOffset(localVec).add(Vec3.atLowerCornerOf(this.offset));
    }

    public BlockPos applyWithoutOffset(BlockPos localPos) {
        return BlockPos.containing(this.applyWithoutOffset(VecHelper.getCenterOf(localPos)));
    }

    public BlockPos apply(BlockPos localPos) {
        return this.applyWithoutOffset(localPos).offset(this.offset);
    }

    public void apply(BlockEntity be) {
        if (be instanceof ITransformableBlockEntity) {
            ((ITransformableBlockEntity) be).transform(this);
        }
    }

    public BlockState apply(BlockState state) {
        Block block = state.m_60734_();
        if (block instanceof ITransformableBlock transformable) {
            return transformable.transform(state, this);
        } else {
            if (this.mirror != null) {
                state = state.m_60715_(this.mirror);
            }
            if (this.rotationAxis == Direction.Axis.Y) {
                if (block instanceof BellBlock) {
                    if (state.m_61143_(BlockStateProperties.BELL_ATTACHMENT) == BellAttachType.DOUBLE_WALL) {
                        state = (BlockState) state.m_61124_(BlockStateProperties.BELL_ATTACHMENT, BellAttachType.SINGLE_WALL);
                    }
                    return (BlockState) state.m_61124_(BellBlock.FACING, this.rotation.rotate((Direction) state.m_61143_(BellBlock.FACING)));
                } else {
                    return state.m_60717_(this.rotation);
                }
            } else if (block instanceof FaceAttachedHorizontalDirectionalBlock) {
                DirectionProperty facingProperty = FaceAttachedHorizontalDirectionalBlock.f_54117_;
                EnumProperty<AttachFace> faceProperty = FaceAttachedHorizontalDirectionalBlock.FACE;
                Direction stateFacing = (Direction) state.m_61143_(facingProperty);
                AttachFace stateFace = (AttachFace) state.m_61143_(faceProperty);
                boolean z = this.rotationAxis == Direction.Axis.Z;
                Direction forcedAxis = z ? Direction.WEST : Direction.SOUTH;
                if (stateFacing.getAxis() == this.rotationAxis && stateFace == AttachFace.WALL) {
                    return state;
                } else {
                    for (int i = 0; i < this.rotation.ordinal(); i++) {
                        stateFace = (AttachFace) state.m_61143_(faceProperty);
                        stateFacing = (Direction) state.m_61143_(facingProperty);
                        boolean b = state.m_61143_(faceProperty) == AttachFace.CEILING;
                        state = (BlockState) state.m_61124_(facingProperty, b ? forcedAxis : forcedAxis.getOpposite());
                        if (stateFace != AttachFace.WALL) {
                            state = (BlockState) state.m_61124_(faceProperty, AttachFace.WALL);
                        } else if (stateFacing.getAxisDirection() == (z ? Direction.AxisDirection.NEGATIVE : Direction.AxisDirection.POSITIVE)) {
                            state = (BlockState) state.m_61124_(faceProperty, AttachFace.FLOOR);
                        } else {
                            state = (BlockState) state.m_61124_(faceProperty, AttachFace.CEILING);
                        }
                    }
                    return state;
                }
            } else {
                boolean halfTurn = this.rotation == Rotation.CLOCKWISE_180;
                if (block instanceof StairBlock) {
                    return this.transformStairs(state, halfTurn);
                } else {
                    if (state.m_61138_(BlockStateProperties.FACING)) {
                        state = (BlockState) state.m_61124_(BlockStateProperties.FACING, this.rotateFacing((Direction) state.m_61143_(BlockStateProperties.FACING)));
                    } else if (state.m_61138_(BlockStateProperties.AXIS)) {
                        state = (BlockState) state.m_61124_(BlockStateProperties.AXIS, this.rotateAxis((Direction.Axis) state.m_61143_(BlockStateProperties.AXIS)));
                    } else if (halfTurn) {
                        if (state.m_61138_(BlockStateProperties.HORIZONTAL_FACING)) {
                            Direction stateFacing = (Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING);
                            if (stateFacing.getAxis() == this.rotationAxis) {
                                return state;
                            }
                        }
                        state = state.m_60717_(this.rotation);
                        if (state.m_61138_(SlabBlock.TYPE) && state.m_61143_(SlabBlock.TYPE) != SlabType.DOUBLE) {
                            state = (BlockState) state.m_61124_(SlabBlock.TYPE, state.m_61143_(SlabBlock.TYPE) == SlabType.BOTTOM ? SlabType.TOP : SlabType.BOTTOM);
                        }
                    }
                    return state;
                }
            }
        }
    }

    protected BlockState transformStairs(BlockState state, boolean halfTurn) {
        if (((Direction) state.m_61143_(StairBlock.FACING)).getAxis() != this.rotationAxis) {
            for (int i = 0; i < this.rotation.ordinal(); i++) {
                Direction direction = (Direction) state.m_61143_(StairBlock.FACING);
                Half half = (Half) state.m_61143_(StairBlock.HALF);
                if (direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ^ half == Half.BOTTOM ^ direction.getAxis() == Direction.Axis.Z) {
                    state = (BlockState) state.m_61122_(StairBlock.HALF);
                } else {
                    state = (BlockState) state.m_61124_(StairBlock.FACING, direction.getOpposite());
                }
            }
        } else if (halfTurn) {
            state = (BlockState) state.m_61122_(StairBlock.HALF);
        }
        return state;
    }

    public Direction mirrorFacing(Direction facing) {
        return this.mirror != null ? this.mirror.mirror(facing) : facing;
    }

    public Direction.Axis rotateAxis(Direction.Axis axis) {
        Direction facing = Direction.get(Direction.AxisDirection.POSITIVE, axis);
        return this.rotateFacing(facing).getAxis();
    }

    public Direction rotateFacing(Direction facing) {
        for (int i = 0; i < this.rotation.ordinal(); i++) {
            facing = facing.getClockWise(this.rotationAxis);
        }
        return facing;
    }

    public static StructureTransform fromBuffer(FriendlyByteBuf buffer) {
        BlockPos readBlockPos = buffer.readBlockPos();
        int readAngle = buffer.readInt();
        int axisIndex = buffer.readVarInt();
        int rotationIndex = buffer.readVarInt();
        int mirrorIndex = buffer.readVarInt();
        return new StructureTransform(readBlockPos, readAngle, axisIndex == -1 ? null : Direction.Axis.values()[axisIndex], rotationIndex == -1 ? null : Rotation.values()[rotationIndex], mirrorIndex == -1 ? null : Mirror.values()[mirrorIndex]);
    }

    public void writeToBuffer(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.offset);
        buffer.writeInt(this.angle);
        buffer.writeVarInt(this.rotationAxis == null ? -1 : this.rotationAxis.ordinal());
        buffer.writeVarInt(this.rotation == null ? -1 : this.rotation.ordinal());
        buffer.writeVarInt(this.mirror == null ? -1 : this.mirror.ordinal());
    }
}