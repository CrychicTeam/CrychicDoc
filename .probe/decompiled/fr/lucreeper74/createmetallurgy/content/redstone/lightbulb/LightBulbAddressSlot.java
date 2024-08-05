package fr.lucreeper74.createmetallurgy.content.redstone.lightbulb;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class LightBulbAddressSlot extends ValueBoxTransform {

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        Direction facing = (Direction) state.m_61143_(LightBulbBlock.f_52588_);
        Vec3 location = VecHelper.voxelSpace(8.0, 11.5, 8.0);
        if (facing.getAxis().isHorizontal()) {
            location = VecHelper.voxelSpace(8.0, 8.0, 11.5);
            return this.rotateHorizontally(state, location);
        } else {
            return VecHelper.rotateCentered(location, facing == Direction.DOWN ? 180.0 : 0.0, Direction.Axis.X);
        }
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        Direction facing = (Direction) state.m_61143_(LightBulbBlock.f_52588_);
        float yRot = facing.getAxis().isVertical() ? 0.0F : AngleHelper.horizontalAngle(facing) + 180.0F;
        float xRot = facing == Direction.UP ? 90.0F : (facing == Direction.DOWN ? 270.0F : 0.0F);
        ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX((double) xRot);
    }
}