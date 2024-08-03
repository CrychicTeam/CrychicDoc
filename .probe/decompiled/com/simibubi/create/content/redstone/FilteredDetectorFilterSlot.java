package com.simibubi.create.content.redstone;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FilteredDetectorFilterSlot extends ValueBoxTransform.Sided {

    private boolean hasSlotAtBottom;

    public FilteredDetectorFilterSlot(boolean hasSlotAtBottom) {
        this.hasSlotAtBottom = hasSlotAtBottom;
    }

    @Override
    protected boolean isSideActive(BlockState state, Direction direction) {
        Direction targetDirection = DirectedDirectionalBlock.getTargetDirection(state);
        if (direction == targetDirection) {
            return false;
        } else if (targetDirection.getOpposite() == direction) {
            return true;
        } else if (targetDirection.getAxis() == Direction.Axis.Y) {
            if (targetDirection == Direction.UP) {
                direction = direction.getOpposite();
            }
            return !this.hasSlotAtBottom ? direction == state.m_61143_(DirectedDirectionalBlock.f_54117_) : direction.getAxis() == ((Direction) state.m_61143_(DirectedDirectionalBlock.f_54117_)).getClockWise().getAxis();
        } else {
            return direction == Direction.UP || direction == Direction.DOWN && this.hasSlotAtBottom;
        }
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        super.rotate(state, ms);
        Direction facing = (Direction) state.m_61143_(DirectedDirectionalBlock.f_54117_);
        if (facing.getAxis() != Direction.Axis.Y) {
            if (this.getSide() == Direction.UP) {
                TransformStack.cast(ms).rotateZ((double) (-AngleHelper.horizontalAngle(facing) + 180.0F));
            }
        }
    }

    @Override
    protected Vec3 getSouthLocation() {
        return VecHelper.voxelSpace(8.0, 8.0, 15.5);
    }
}