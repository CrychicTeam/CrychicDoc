package com.simibubi.create.content.kinetics.deployer;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class DeployerFilterSlot extends ValueBoxTransform.Sided {

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction facing = (Direction) state.m_61143_(DeployerBlock.FACING);
        Vec3 vec = VecHelper.voxelSpace(8.0, 8.0, 15.5);
        vec = VecHelper.rotateCentered(vec, (double) AngleHelper.horizontalAngle(this.getSide()), Direction.Axis.Y);
        vec = VecHelper.rotateCentered(vec, (double) AngleHelper.verticalAngle(this.getSide()), Direction.Axis.X);
        return vec.subtract(Vec3.atLowerCornerOf(facing.getNormal()).scale(0.125));
    }

    @Override
    protected boolean isSideActive(BlockState state, Direction direction) {
        Direction facing = (Direction) state.m_61143_(DeployerBlock.FACING);
        return direction.getAxis() == facing.getAxis() ? false : ((DeployerBlock) state.m_60734_()).getRotationAxis(state) != direction.getAxis();
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = this.getSide();
        float xRot = facing == Direction.UP ? 90.0F : (facing == Direction.DOWN ? 270.0F : 0.0F);
        float yRot = AngleHelper.horizontalAngle(facing) + 180.0F;
        if (facing.getAxis() == Direction.Axis.Y) {
            TransformStack.cast(ms).rotateY((double) (180.0F + AngleHelper.horizontalAngle((Direction) state.m_61143_(DeployerBlock.FACING))));
        }
        ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX((double) xRot);
    }

    @Override
    protected Vec3 getSouthLocation() {
        return Vec3.ZERO;
    }
}