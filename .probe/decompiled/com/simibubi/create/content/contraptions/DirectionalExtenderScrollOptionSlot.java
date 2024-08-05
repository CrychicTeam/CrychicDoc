package com.simibubi.create.content.contraptions;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.behaviour.CenteredSideValueBoxTransform;
import com.simibubi.create.foundation.utility.AngleHelper;
import java.util.function.BiPredicate;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class DirectionalExtenderScrollOptionSlot extends CenteredSideValueBoxTransform {

    public DirectionalExtenderScrollOptionSlot(BiPredicate<BlockState, Direction> allowedDirections) {
        super(allowedDirections);
    }

    @Override
    public Vec3 getLocalOffset(BlockState state) {
        return super.getLocalOffset(state).add(Vec3.atLowerCornerOf(((Direction) state.m_61143_(BlockStateProperties.FACING)).getNormal()).scale(-0.125));
    }

    @Override
    public void rotate(BlockState state, PoseStack ms) {
        if (!this.getSide().getAxis().isHorizontal()) {
            TransformStack.cast(ms).rotateY((double) (AngleHelper.horizontalAngle((Direction) state.m_61143_(BlockStateProperties.FACING)) + 180.0F));
        }
        super.rotate(state, ms);
    }
}