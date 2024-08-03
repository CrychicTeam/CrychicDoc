package com.simibubi.create.foundation.block.connected;

import com.simibubi.create.content.decoration.copycat.CopycatBlock;
import com.simibubi.create.content.decoration.palettes.ConnectedPillarBlock;
import com.simibubi.create.content.decoration.palettes.LayeredBlock;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class RotatedPillarCTBehaviour extends HorizontalCTBehaviour {

    public RotatedPillarCTBehaviour(CTSpriteShiftEntry layerShift, CTSpriteShiftEntry topShift) {
        super(layerShift, topShift);
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face, Direction primaryOffset, Direction secondaryOffset) {
        if (other.m_60734_() != state.m_60734_()) {
            return false;
        } else {
            Direction.Axis stateAxis = (Direction.Axis) state.m_61143_(LayeredBlock.f_55923_);
            if (other.m_61143_(LayeredBlock.f_55923_) != stateAxis) {
                return false;
            } else if (this.isBeingBlocked(state, reader, pos, otherPos, face)) {
                return false;
            } else if (reader.m_8055_(pos).m_60734_() instanceof CopycatBlock) {
                return true;
            } else if (reader.m_8055_(otherPos).m_60734_() instanceof CopycatBlock) {
                return true;
            } else if (primaryOffset != null && primaryOffset.getAxis() != stateAxis && !ConnectedPillarBlock.getConnection(state, primaryOffset)) {
                return false;
            } else {
                if (secondaryOffset != null && secondaryOffset.getAxis() != stateAxis) {
                    if (!ConnectedPillarBlock.getConnection(state, secondaryOffset)) {
                        return false;
                    }
                    if (!ConnectedPillarBlock.getConnection(other, secondaryOffset.getOpposite())) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    @Override
    protected boolean isBeingBlocked(BlockState state, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        return state.m_61143_(LayeredBlock.f_55923_) == face.getAxis() && super.isBeingBlocked(state, reader, pos, otherPos, face);
    }

    @Override
    protected boolean reverseUVs(BlockState state, Direction face) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(LayeredBlock.f_55923_);
        if (axis == Direction.Axis.X) {
            return face.getAxisDirection() == Direction.AxisDirection.NEGATIVE && face.getAxis() != Direction.Axis.X;
        } else {
            return axis != Direction.Axis.Z ? super.reverseUVs(state, face) : face != Direction.NORTH && face.getAxisDirection() != Direction.AxisDirection.POSITIVE;
        }
    }

    @Override
    protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
        return super.reverseUVsHorizontally(state, face);
    }

    @Override
    protected boolean reverseUVsVertically(BlockState state, Direction face) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(LayeredBlock.f_55923_);
        if (axis == Direction.Axis.X && face == Direction.NORTH) {
            return false;
        } else {
            return axis == Direction.Axis.Z && face == Direction.WEST ? false : super.reverseUVsVertically(state, face);
        }
    }

    @Override
    protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(LayeredBlock.f_55923_);
        if (axis == Direction.Axis.Y) {
            return super.getUpDirection(reader, pos, state, face);
        } else {
            boolean alongX = axis == Direction.Axis.X;
            if (face.getAxis().isVertical() && alongX) {
                return super.getUpDirection(reader, pos, state, face).getClockWise();
            } else {
                return face.getAxis() != axis && !face.getAxis().isVertical() ? Direction.fromAxisAndDirection(axis, alongX ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE) : super.getUpDirection(reader, pos, state, face);
            }
        }
    }

    @Override
    protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(LayeredBlock.f_55923_);
        if (axis == Direction.Axis.Y) {
            return super.getRightDirection(reader, pos, state, face);
        } else if (face.getAxis().isVertical() && axis == Direction.Axis.X) {
            return super.getRightDirection(reader, pos, state, face).getClockWise();
        } else {
            return face.getAxis() != axis && !face.getAxis().isVertical() ? Direction.fromAxisAndDirection(Direction.Axis.Y, face.getAxisDirection()) : super.getRightDirection(reader, pos, state, face);
        }
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, TextureAtlasSprite sprite) {
        return super.getShift(state, direction.getAxis() == state.m_61143_(LayeredBlock.f_55923_) ? Direction.UP : Direction.SOUTH, sprite);
    }
}