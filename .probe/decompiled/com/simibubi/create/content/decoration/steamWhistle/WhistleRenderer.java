package com.simibubi.create.content.decoration.steamWhistle;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class WhistleRenderer extends SafeBlockEntityRenderer<WhistleBlockEntity> {

    public WhistleRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(WhistleBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = be.m_58900_();
        if (blockState.m_60734_() instanceof WhistleBlock) {
            Direction direction = (Direction) blockState.m_61143_(WhistleBlock.FACING);
            WhistleBlock.WhistleSize size = (WhistleBlock.WhistleSize) blockState.m_61143_(WhistleBlock.SIZE);
            PartialModel mouth = size == WhistleBlock.WhistleSize.LARGE ? AllPartialModels.WHISTLE_MOUTH_LARGE : (size == WhistleBlock.WhistleSize.MEDIUM ? AllPartialModels.WHISTLE_MOUTH_MEDIUM : AllPartialModels.WHISTLE_MOUTH_SMALL);
            float offset = be.animation.getValue(partialTicks);
            if (be.animation.getChaseTarget() > 0.0F && be.animation.getValue() > 0.5F) {
                float wiggleProgress = ((float) AnimationTickHolder.getTicks(be.m_58904_()) + partialTicks) / 8.0F;
                offset = (float) ((double) offset - Math.sin((double) (wiggleProgress * (float) (Math.PI * 2) * (float) (4 - size.ordinal()))) / 16.0);
            }
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) CachedBufferer.partial(mouth, blockState).centre()).rotateY((double) AngleHelper.horizontalAngle(direction))).unCentre()).translate(0.0, (double) (offset * 4.0F / 16.0F), 0.0).light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }
}