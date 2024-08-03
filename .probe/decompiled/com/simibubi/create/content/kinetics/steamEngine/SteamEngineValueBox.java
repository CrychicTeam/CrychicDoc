package com.simibubi.create.content.kinetics.steamEngine;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Pointing;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SteamEngineValueBox extends ValueBoxTransform.Sided {

    @Override
    protected boolean isSideActive(BlockState state, Direction side) {
        Direction engineFacing = SteamEngineBlock.getFacing(state);
        if (engineFacing.getAxis() == side.getAxis()) {
            return false;
        } else {
            float roll = 0.0F;
            for (Pointing p : Pointing.values()) {
                if (p.getCombinedDirection(engineFacing) == side) {
                    roll = (float) p.getXRotation();
                }
            }
            if (engineFacing == Direction.UP) {
                roll += 180.0F;
            }
            boolean recessed = roll % 180.0F == 0.0F;
            if (engineFacing.getAxis() == Direction.Axis.Y) {
                recessed ^= ((Direction) state.m_61143_(SteamEngineBlock.f_54117_)).getAxis() == Direction.Axis.X;
            }
            return !recessed;
        }
    }

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction side = this.getSide();
        Direction engineFacing = SteamEngineBlock.getFacing(state);
        float roll = 0.0F;
        for (Pointing p : Pointing.values()) {
            if (p.getCombinedDirection(engineFacing) == side) {
                roll = (float) p.getXRotation();
            }
        }
        if (engineFacing == Direction.UP) {
            roll += 180.0F;
        }
        float horizontalAngle = AngleHelper.horizontalAngle(engineFacing);
        float verticalAngle = AngleHelper.verticalAngle(engineFacing);
        Vec3 local = VecHelper.voxelSpace(8.0, 14.5, 9.0);
        local = VecHelper.rotateCentered(local, (double) roll, Direction.Axis.Z);
        local = VecHelper.rotateCentered(local, (double) horizontalAngle, Direction.Axis.Y);
        return VecHelper.rotateCentered(local, (double) verticalAngle, Direction.Axis.X);
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = SteamEngineBlock.getFacing(state);
        if (facing.getAxis() == Direction.Axis.Y) {
            super.rotate(state, ms);
        } else {
            float roll = 0.0F;
            for (Pointing p : Pointing.values()) {
                if (p.getCombinedDirection(facing) == this.getSide()) {
                    roll = (float) p.getXRotation();
                }
            }
            float yRot = AngleHelper.horizontalAngle(facing) + (float) (facing == Direction.DOWN ? 180 : 0);
            ((TransformStack) ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX(facing == Direction.DOWN ? -90.0 : 90.0)).rotateY((double) roll);
        }
    }

    @Override
    protected Vec3 getSouthLocation() {
        return Vec3.ZERO;
    }
}