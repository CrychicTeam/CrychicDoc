package com.simibubi.create.content.kinetics.transmission;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;

public class SplitShaftRenderer extends KineticBlockEntityRenderer<SplitShaftBlockEntity> {

    public SplitShaftRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(SplitShaftBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            Block block = be.m_58900_().m_60734_();
            Direction.Axis boxAxis = ((IRotate) block).getRotationAxis(be.m_58900_());
            BlockPos pos = be.m_58899_();
            float time = AnimationTickHolder.getRenderTime(be.m_58904_());
            for (Direction direction : Iterate.directions) {
                Direction.Axis axis = direction.getAxis();
                if (boxAxis == axis) {
                    float offset = getRotationOffsetForPosition(be, pos, axis);
                    float angle = time * be.getSpeed() * 3.0F / 10.0F % 360.0F;
                    float modifier = be.getRotationSpeedModifier(direction);
                    angle *= modifier;
                    angle += offset;
                    angle = angle / 180.0F * (float) Math.PI;
                    SuperByteBuffer superByteBuffer = CachedBufferer.partialFacing(AllPartialModels.SHAFT_HALF, be.m_58900_(), direction);
                    kineticRotationTransform(superByteBuffer, be, axis, angle, light);
                    superByteBuffer.renderInto(ms, buffer.getBuffer(RenderType.solid()));
                }
            }
        }
    }
}