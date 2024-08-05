package com.simibubi.create.content.redstone.displayLink;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.RenderTypes;
import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class DisplayLinkRenderer extends SafeBlockEntityRenderer<DisplayLinkBlockEntity> {

    public DisplayLinkRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(DisplayLinkBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        float glow = be.glow.getValue(partialTicks);
        if (!(glow < 0.125F)) {
            glow = (float) (1.0 - 2.0 * Math.pow((double) (glow - 0.75F), 2.0));
            glow = Mth.clamp(glow, -1.0F, 1.0F);
            int color = (int) (200.0F * glow);
            BlockState blockState = be.m_58900_();
            TransformStack msr = TransformStack.cast(ms);
            Direction face = (Direction) blockState.m_61145_(DisplayLinkBlock.f_52588_).orElse(Direction.UP);
            if (face.getAxis().isHorizontal()) {
                face = face.getOpposite();
            }
            ms.pushPose();
            ((TransformStack) ((TransformStack) ((TransformStack) msr.centre()).rotateY((double) AngleHelper.horizontalAngle(face))).rotateX((double) (-AngleHelper.verticalAngle(face) - 90.0F))).unCentre();
            CachedBufferer.partial(AllPartialModels.DISPLAY_LINK_TUBE, blockState).light(15728880).renderInto(ms, buffer.getBuffer(RenderType.translucent()));
            CachedBufferer.partial(AllPartialModels.DISPLAY_LINK_GLOW, blockState).light(15728880).color(color, color, color, 255).disableDiffuse().renderInto(ms, buffer.getBuffer(RenderTypes.getAdditive()));
            ms.popPose();
        }
    }
}