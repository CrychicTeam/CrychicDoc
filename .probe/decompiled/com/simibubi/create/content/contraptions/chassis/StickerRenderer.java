package com.simibubi.create.content.contraptions.chassis;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.AnimationTickHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class StickerRenderer extends SafeBlockEntityRenderer<StickerBlockEntity> {

    public StickerRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(StickerBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState state = be.m_58900_();
            SuperByteBuffer head = CachedBufferer.partial(AllPartialModels.STICKER_HEAD, state);
            float offset = be.piston.getValue(AnimationTickHolder.getPartialTicks(be.m_58904_()));
            if (be.m_58904_() != Minecraft.getInstance().level && !be.isVirtual()) {
                offset = state.m_61143_(StickerBlock.EXTENDED) ? 1.0F : 0.0F;
            }
            Direction facing = (Direction) state.m_61143_(StickerBlock.f_52588_);
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) head.nudge(be.hashCode())).centre()).rotateY((double) AngleHelper.horizontalAngle(facing))).rotateX((double) (AngleHelper.verticalAngle(facing) + 90.0F))).unCentre()).translate(0.0, (double) (offset * offset * 4.0F / 16.0F), 0.0);
            head.light(light).renderInto(ms, buffer.getBuffer(RenderType.solid()));
        }
    }
}