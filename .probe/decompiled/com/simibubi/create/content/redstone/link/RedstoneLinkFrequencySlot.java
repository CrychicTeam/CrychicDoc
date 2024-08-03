package com.simibubi.create.content.redstone.link;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class RedstoneLinkFrequencySlot extends ValueBoxTransform.Dual {

    Vec3 horizontal = VecHelper.voxelSpace(10.0, 5.5, 2.5);

    Vec3 vertical = VecHelper.voxelSpace(10.0, 2.5, 5.5);

    public RedstoneLinkFrequencySlot(boolean first) {
        super(first);
    }

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction facing = (Direction) state.m_61143_(RedstoneLinkBlock.f_52588_);
        Vec3 location = VecHelper.voxelSpace(8.0, 3.01F, 5.5);
        if (facing.getAxis().isHorizontal()) {
            location = VecHelper.voxelSpace(8.0, 5.5, 3.01F);
            if (this.isFirst()) {
                location = location.add(0.0, 0.3125, 0.0);
            }
            return this.rotateHorizontally(state, location);
        } else {
            if (this.isFirst()) {
                location = location.add(0.0, 0.0, 0.3125);
            }
            return VecHelper.rotateCentered(location, facing == Direction.DOWN ? 180.0 : 0.0, Direction.Axis.X);
        }
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = (Direction) state.m_61143_(RedstoneLinkBlock.f_52588_);
        float yRot = facing.getAxis().isVertical() ? 0.0F : AngleHelper.horizontalAngle(facing) + 180.0F;
        float xRot = facing == Direction.UP ? 90.0F : (facing == Direction.DOWN ? 270.0F : 0.0F);
        ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX((double) xRot);
    }

    @Override
    public float getScale() {
        return 0.4975F;
    }
}