package com.simibubi.create.content.kinetics.gantry;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlock;
import com.simibubi.create.content.contraptions.gantry.GantryCarriageBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GantryShaftBlockEntity extends KineticBlockEntity {

    public GantryShaftBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    @Override
    protected boolean syncSequenceContext() {
        return true;
    }

    public void checkAttachedCarriageBlocks() {
        if (this.canAssembleOn()) {
            for (Direction d : Iterate.directions) {
                if (d.getAxis() != ((Direction) this.m_58900_().m_61143_(GantryShaftBlock.FACING)).getAxis()) {
                    BlockPos offset = this.f_58858_.relative(d);
                    BlockState pinionState = this.f_58857_.getBlockState(offset);
                    if (AllBlocks.GANTRY_CARRIAGE.has(pinionState) && pinionState.m_61143_(GantryCarriageBlock.FACING) == d) {
                        BlockEntity blockEntity = this.f_58857_.getBlockEntity(offset);
                        if (blockEntity instanceof GantryCarriageBlockEntity) {
                            ((GantryCarriageBlockEntity) blockEntity).queueAssembly();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onSpeedChanged(float previousSpeed) {
        super.onSpeedChanged(previousSpeed);
        this.checkAttachedCarriageBlocks();
    }

    @Override
    public float propagateRotationTo(KineticBlockEntity target, BlockState stateFrom, BlockState stateTo, BlockPos diff, boolean connectedViaAxes, boolean connectedViaCogs) {
        float defaultModifier = super.propagateRotationTo(target, stateFrom, stateTo, diff, connectedViaAxes, connectedViaCogs);
        if (connectedViaAxes) {
            return defaultModifier;
        } else if (!(Boolean) stateFrom.m_61143_(GantryShaftBlock.POWERED)) {
            return defaultModifier;
        } else if (!AllBlocks.GANTRY_CARRIAGE.has(stateTo)) {
            return defaultModifier;
        } else {
            Direction direction = Direction.getNearest((float) diff.m_123341_(), (float) diff.m_123342_(), (float) diff.m_123343_());
            return stateTo.m_61143_(GantryCarriageBlock.FACING) != direction ? defaultModifier : GantryCarriageBlockEntity.getGantryPinionModifier((Direction) stateFrom.m_61143_(GantryShaftBlock.FACING), (Direction) stateTo.m_61143_(GantryCarriageBlock.FACING));
        }
    }

    @Override
    public boolean isCustomConnection(KineticBlockEntity other, BlockState state, BlockState otherState) {
        if (!AllBlocks.GANTRY_CARRIAGE.has(otherState)) {
            return false;
        } else {
            BlockPos diff = other.m_58899_().subtract(this.f_58858_);
            Direction direction = Direction.getNearest((float) diff.m_123341_(), (float) diff.m_123342_(), (float) diff.m_123343_());
            return otherState.m_61143_(GantryCarriageBlock.FACING) == direction;
        }
    }

    public boolean canAssembleOn() {
        BlockState blockState = this.m_58900_();
        if (!AllBlocks.GANTRY_SHAFT.has(blockState)) {
            return false;
        } else if ((Boolean) blockState.m_61143_(GantryShaftBlock.POWERED)) {
            return false;
        } else {
            float speed = this.getPinionMovementSpeed();
            switch((GantryShaftBlock.Part) blockState.m_61143_(GantryShaftBlock.PART)) {
                case END:
                    return speed < 0.0F;
                case MIDDLE:
                    return speed != 0.0F;
                case START:
                    return speed > 0.0F;
                case SINGLE:
                default:
                    return false;
            }
        }
    }

    public float getPinionMovementSpeed() {
        BlockState blockState = this.m_58900_();
        return !AllBlocks.GANTRY_SHAFT.has(blockState) ? 0.0F : Mth.clamp(convertToLinear(-this.getSpeed()), -0.49F, 0.49F);
    }

    @Override
    protected boolean isNoisy() {
        return false;
    }
}