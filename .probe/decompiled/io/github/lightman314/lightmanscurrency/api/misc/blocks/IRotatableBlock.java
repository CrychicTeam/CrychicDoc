package io.github.lightman314.lightmanscurrency.api.misc.blocks;

import io.github.lightman314.lightmanscurrency.util.MathUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.joml.Vector3f;

public interface IRotatableBlock {

    DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    static BlockPos getRightPos(BlockPos pos, Direction facing) {
        return switch(facing) {
            case NORTH ->
                new BlockPos(pos.m_123341_() + 1, pos.m_123342_(), pos.m_123343_());
            case SOUTH ->
                new BlockPos(pos.m_123341_() - 1, pos.m_123342_(), pos.m_123343_());
            case EAST ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() + 1);
            case WEST ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() - 1);
            default ->
                pos;
        };
    }

    static BlockPos getLeftPos(BlockPos pos, Direction facing) {
        return switch(facing) {
            case NORTH ->
                new BlockPos(pos.m_123341_() - 1, pos.m_123342_(), pos.m_123343_());
            case SOUTH ->
                new BlockPos(pos.m_123341_() + 1, pos.m_123342_(), pos.m_123343_());
            case EAST ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() - 1);
            case WEST ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() + 1);
            default ->
                pos;
        };
    }

    static BlockPos getForwardPos(BlockPos pos, Direction facing) {
        return switch(facing) {
            case NORTH ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() - 1);
            case SOUTH ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() + 1);
            case EAST ->
                new BlockPos(pos.m_123341_() + 1, pos.m_123342_(), pos.m_123343_());
            case WEST ->
                new BlockPos(pos.m_123341_() - 1, pos.m_123342_(), pos.m_123343_());
            default ->
                pos;
        };
    }

    static BlockPos getBackwardPos(BlockPos pos, Direction facing) {
        return switch(facing) {
            case NORTH ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() + 1);
            case SOUTH ->
                new BlockPos(pos.m_123341_(), pos.m_123342_(), pos.m_123343_() - 1);
            case EAST ->
                new BlockPos(pos.m_123341_() - 1, pos.m_123342_(), pos.m_123343_());
            case WEST ->
                new BlockPos(pos.m_123341_() + 1, pos.m_123342_(), pos.m_123343_());
            default ->
                pos;
        };
    }

    static Vector3f getRightVect(Direction facing) {
        return switch(facing) {
            case NORTH ->
                new Vector3f(1.0F, 0.0F, 0.0F);
            case SOUTH ->
                new Vector3f(-1.0F, 0.0F, 0.0F);
            case EAST ->
                new Vector3f(0.0F, 0.0F, 1.0F);
            case WEST ->
                new Vector3f(0.0F, 0.0F, -1.0F);
            default ->
                new Vector3f(0.0F, 0.0F, 0.0F);
        };
    }

    static Vector3f getLeftVect(Direction facing) {
        return MathUtil.VectorMult(getRightVect(facing), -1.0F);
    }

    static Vector3f getForwardVect(Direction facing) {
        return switch(facing) {
            case NORTH ->
                new Vector3f(0.0F, 0.0F, -1.0F);
            case SOUTH ->
                new Vector3f(0.0F, 0.0F, 1.0F);
            case EAST ->
                new Vector3f(1.0F, 0.0F, 0.0F);
            case WEST ->
                new Vector3f(-1.0F, 0.0F, 0.0F);
            default ->
                new Vector3f(0.0F, 0.0F, 0.0F);
        };
    }

    static Vector3f getBackwardVect(Direction facing) {
        return MathUtil.VectorMult(getForwardVect(facing), -1.0F);
    }

    static Vector3f getOffsetVect(Direction facing) {
        return switch(facing) {
            case NORTH ->
                new Vector3f(0.0F, 0.0F, 1.0F);
            case SOUTH ->
                new Vector3f(1.0F, 0.0F, 0.0F);
            default ->
                new Vector3f(0.0F, 0.0F, 0.0F);
            case WEST ->
                new Vector3f(1.0F, 0.0F, 1.0F);
        };
    }

    default Direction getFacing(BlockState state) {
        return (Direction) state.m_61143_(FACING);
    }

    static Direction getRelativeSide(Direction facing, Direction side) {
        if (side == null) {
            return null;
        } else if (side.getAxis() == Direction.Axis.Y) {
            return side;
        } else {
            if (facing.getAxis() == Direction.Axis.Z) {
                facing = facing.getOpposite();
            }
            return Direction.from2DDataValue(facing.get2DDataValue() + side.get2DDataValue());
        }
    }

    static Direction getActualSide(Direction facing, Direction relativeSide) {
        if (relativeSide == null) {
            return null;
        } else if (relativeSide.getAxis() == Direction.Axis.Y) {
            return relativeSide;
        } else {
            if (facing.getAxis() == Direction.Axis.Z) {
                facing = facing.getOpposite();
            }
            Direction result = Direction.from2DDataValue(facing.get2DDataValue() - relativeSide.get2DDataValue() + 4);
            return result.getAxis() == Direction.Axis.X ? result.getOpposite() : result;
        }
    }
}