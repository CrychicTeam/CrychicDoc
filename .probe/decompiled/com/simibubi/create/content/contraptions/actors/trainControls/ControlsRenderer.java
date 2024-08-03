package com.simibubi.create.content.contraptions.actors.trainControls;

import com.jozufozu.flywheel.core.virtual.VirtualRenderWorld;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ContraptionMatrices;
import com.simibubi.create.content.contraptions.render.ContraptionRenderDispatcher;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;

public class ControlsRenderer {

    public static void render(MovementContext context, VirtualRenderWorld renderWorld, ContraptionMatrices matrices, MultiBufferSource buffer, float equipAnimation, float firstLever, float secondLever) {
        BlockState state = context.state;
        Direction facing = (Direction) state.m_61143_(ControlsBlock.f_54117_);
        SuperByteBuffer cover = CachedBufferer.partial(AllPartialModels.TRAIN_CONTROLS_COVER, state);
        float hAngle = 180.0F + AngleHelper.horizontalAngle(facing);
        PoseStack ms = matrices.getModel();
        ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) cover.transform(ms).centre()).rotateY((double) hAngle)).unCentre()).light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.cutoutMipped()));
        double yOffset = (double) Mth.lerp(equipAnimation * equipAnimation, -0.15F, 0.05F);
        for (boolean first : Iterate.trueAndFalse) {
            float vAngle = Mth.clamp(first ? firstLever * 70.0F - 25.0F : secondLever * 15.0F, -45.0F, 45.0F);
            SuperByteBuffer lever = CachedBufferer.partial(AllPartialModels.TRAIN_CONTROLS_LEVER, state);
            ms.pushPose();
            ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) ((TransformStack) TransformStack.cast(ms).centre()).rotateY((double) hAngle)).translate(0.0, 0.0, 0.25)).rotateX((double) (vAngle - 45.0F))).translate(0.0, yOffset, 0.0)).rotateX(45.0)).unCentre()).translate(0.0, -0.125, -0.1875)).translate(first ? 0.0 : 0.375, 0.0, 0.0);
            lever.transform(ms).light(matrices.getWorld(), ContraptionRenderDispatcher.getContraptionWorldLight(context, renderWorld)).renderInto(matrices.getViewProjection(), buffer.getBuffer(RenderType.solid()));
            ms.popPose();
        }
    }
}