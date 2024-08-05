package com.simibubi.create.content.kinetics.simpleRelays;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class BracketedKineticBlockEntityRenderer extends KineticBlockEntityRenderer<BracketedKineticBlockEntity> {

    public BracketedKineticBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(BracketedKineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            if (!AllBlocks.LARGE_COGWHEEL.has(be.m_58900_())) {
                super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
            } else {
                Direction.Axis axis = getRotationAxisOf(be);
                Direction facing = Direction.fromAxisAndDirection(axis, Direction.AxisDirection.POSITIVE);
                renderRotatingBuffer(be, CachedBufferer.partialFacingVertical(AllPartialModels.SHAFTLESS_LARGE_COGWHEEL, be.m_58900_(), facing), ms, buffer.getBuffer(RenderType.solid()), light);
                float angle = getAngleForLargeCogShaft(be, axis);
                SuperByteBuffer shaft = CachedBufferer.partialFacingVertical(AllPartialModels.COGWHEEL_SHAFT, be.m_58900_(), facing);
                kineticRotationTransform(shaft, be, axis, angle, light);
                shaft.renderInto(ms, buffer.getBuffer(RenderType.solid()));
            }
        }
    }

    public static float getAngleForLargeCogShaft(SimpleKineticBlockEntity be, Direction.Axis axis) {
        BlockPos pos = be.m_58899_();
        float offset = getShaftAngleOffset(axis, pos);
        float time = AnimationTickHolder.getRenderTime(be.m_58904_());
        return (time * be.getSpeed() * 3.0F / 10.0F + offset) % 360.0F / 180.0F * (float) Math.PI;
    }

    public static float getShaftAngleOffset(Direction.Axis axis, BlockPos pos) {
        float offset = 0.0F;
        double d = (double) (((axis == Direction.Axis.X ? 0 : pos.m_123341_()) + (axis == Direction.Axis.Y ? 0 : pos.m_123342_()) + (axis == Direction.Axis.Z ? 0 : pos.m_123343_())) % 2);
        if (d == 0.0) {
            offset = 22.5F;
        }
        return offset;
    }
}