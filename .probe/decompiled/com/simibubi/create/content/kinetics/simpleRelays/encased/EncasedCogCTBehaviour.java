package com.simibubi.create.content.kinetics.simpleRelays.encased;

import com.simibubi.create.content.decoration.encasing.EncasedCTBehaviour;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EncasedCogCTBehaviour extends EncasedCTBehaviour {

    private Couple<CTSpriteShiftEntry> sideShifts;

    private boolean large;

    public EncasedCogCTBehaviour(CTSpriteShiftEntry shift) {
        this(shift, null);
    }

    public EncasedCogCTBehaviour(CTSpriteShiftEntry shift, Couple<CTSpriteShiftEntry> sideShifts) {
        super(shift);
        this.large = sideShifts == null;
        this.sideShifts = sideShifts;
    }

    @Override
    public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS);
        if (!this.large && axis != face.getAxis()) {
            if (other.m_60734_() == state.m_60734_() && other.m_61143_(RotatedPillarKineticBlock.AXIS) == state.m_61143_(RotatedPillarKineticBlock.AXIS)) {
                return true;
            } else {
                BlockState blockState = reader.m_8055_(otherPos.relative(face));
                return !ICogWheel.isLargeCog(blockState) ? false : ((IRotate) blockState.m_60734_()).getRotationAxis(blockState) == axis;
            }
        } else {
            return super.connectsTo(state, other, reader, pos, otherPos, face);
        }
    }

    @Override
    protected boolean reverseUVs(BlockState state, Direction face) {
        return ((Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS)).isHorizontal() && face.getAxis().isHorizontal() && face.getAxisDirection() == Direction.AxisDirection.POSITIVE;
    }

    @Override
    protected boolean reverseUVsVertically(BlockState state, Direction face) {
        return !this.large && state.m_61143_(RotatedPillarKineticBlock.AXIS) == Direction.Axis.X && face.getAxis() == Direction.Axis.Z ? face != Direction.SOUTH : super.reverseUVsVertically(state, face);
    }

    @Override
    protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
        if (this.large) {
            return super.reverseUVsHorizontally(state, face);
        } else if (((Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS)).isVertical() && face.getAxis().isHorizontal()) {
            return true;
        } else {
            return state.m_61143_(RotatedPillarKineticBlock.AXIS) == Direction.Axis.Z && face == Direction.DOWN ? true : super.reverseUVsHorizontally(state, face);
        }
    }

    @Override
    public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarKineticBlock.AXIS);
        if (!this.large && axis != direction.getAxis()) {
            return this.sideShifts.get(axis == Direction.Axis.X || axis == Direction.Axis.Z && direction.getAxis() == Direction.Axis.X);
        } else {
            return axis == direction.getAxis() && state.m_61143_(direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? EncasedCogwheelBlock.TOP_SHAFT : EncasedCogwheelBlock.BOTTOM_SHAFT) ? null : super.getShift(state, direction, sprite);
        }
    }
}