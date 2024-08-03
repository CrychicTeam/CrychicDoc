package com.simibubi.create.content.logistics.funnel;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class FunnelFilterSlotPositioning extends ValueBoxTransform.Sided {

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction side = this.getSide();
        float horizontalAngle = AngleHelper.horizontalAngle(side);
        Direction funnelFacing = FunnelBlock.getFunnelFacing(state);
        float stateAngle = AngleHelper.horizontalAngle(funnelFacing);
        if (state.m_60734_() instanceof BeltFunnelBlock) {
            switch((BeltFunnelBlock.Shape) state.m_61143_(BeltFunnelBlock.SHAPE)) {
                case EXTENDED:
                    return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, 15.5, 13.0), (double) stateAngle, Direction.Axis.Y);
                case PULLING:
                case PUSHING:
                    return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, 12.0, 8.675F), (double) horizontalAngle, Direction.Axis.Y);
                case RETRACTED:
                default:
                    return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, 13.0, 7.5), (double) horizontalAngle, Direction.Axis.Y);
            }
        } else if (!funnelFacing.getAxis().isHorizontal()) {
            Vec3 southLocation = VecHelper.voxelSpace(8.0, funnelFacing == Direction.DOWN ? 14.0 : 2.0, 15.5);
            return VecHelper.rotateCentered(southLocation, (double) horizontalAngle, Direction.Axis.Y);
        } else {
            return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, 12.2, 8.55F), (double) horizontalAngle, Direction.Axis.Y);
        }
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = FunnelBlock.getFunnelFacing(state);
        if (facing.getAxis().isVertical()) {
            super.rotate(state, ms);
        } else {
            boolean isBeltFunnel = state.m_60734_() instanceof BeltFunnelBlock;
            if (isBeltFunnel && state.m_61143_(BeltFunnelBlock.SHAPE) != BeltFunnelBlock.Shape.EXTENDED) {
                BeltFunnelBlock.Shape shape = (BeltFunnelBlock.Shape) state.m_61143_(BeltFunnelBlock.SHAPE);
                super.rotate(state, ms);
                if (shape == BeltFunnelBlock.Shape.PULLING || shape == BeltFunnelBlock.Shape.PUSHING) {
                    TransformStack.cast(ms).rotateX(-22.5);
                }
            } else if (state.m_60734_() instanceof FunnelBlock) {
                super.rotate(state, ms);
                TransformStack.cast(ms).rotateX(-22.5);
            } else {
                float yRot = AngleHelper.horizontalAngle(AbstractFunnelBlock.getFunnelFacing(state)) + (float) (facing == Direction.DOWN ? 180 : 0);
                ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX(facing == Direction.DOWN ? -90.0 : 90.0);
            }
        }
    }

    @Override
    protected boolean isSideActive(BlockState state, Direction direction) {
        Direction facing = FunnelBlock.getFunnelFacing(state);
        if (facing == null) {
            return false;
        } else if (facing.getAxis().isVertical()) {
            return direction.getAxis().isHorizontal();
        } else {
            return state.m_60734_() instanceof BeltFunnelBlock && state.m_61143_(BeltFunnelBlock.SHAPE) == BeltFunnelBlock.Shape.EXTENDED ? direction == Direction.UP : direction == facing;
        }
    }

    @Override
    protected Vec3 getSouthLocation() {
        return Vec3.ZERO;
    }
}