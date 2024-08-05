package fr.lucreeper74.createmetallurgy.content.belt_grinder;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BeltGrinderFilterSlot extends ValueBoxTransform {

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        return VecHelper.rotateCentered(VecHelper.voxelSpace(8.0, 12.5, 5.0), (double) this.angleY(state), Direction.Axis.Y);
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        ((TransformStack) TransformStack.cast(ms).rotateY((double) this.angleY(state))).rotateX(90.0);
    }

    protected float angleY(BlockState state) {
        return AngleHelper.horizontalAngle(((Direction) state.m_61143_(BeltGrinderBlock.HORIZONTAL_FACING)).getOpposite());
    }
}