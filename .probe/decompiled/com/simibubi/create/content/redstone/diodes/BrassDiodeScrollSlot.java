package com.simibubi.create.content.redstone.diodes;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class BrassDiodeScrollSlot extends ValueBoxTransform {

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        return VecHelper.voxelSpace(8.0, 2.6F, 8.0);
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        float yRot = AngleHelper.horizontalAngle((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)) + 180.0F;
        ((TransformStack) TransformStack.cast(ms).rotateY((double) yRot)).rotateX(90.0);
    }

    @Override
    public int getOverrideColor() {
        return 5841956;
    }
}