package com.simibubi.create.content.redstone.analogLever;

import com.jozufozu.flywheel.backend.Backend;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.AngleHelper;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class AnalogLeverRenderer extends SafeBlockEntityRenderer<AnalogLeverBlockEntity> {

    public AnalogLeverRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(AnalogLeverBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!Backend.canUseInstancing(be.m_58904_())) {
            BlockState leverState = be.m_58900_();
            float state = be.clientState.getValue(partialTicks);
            VertexConsumer vb = buffer.getBuffer(RenderType.solid());
            SuperByteBuffer handle = CachedBufferer.partial(AllPartialModels.ANALOG_LEVER_HANDLE, leverState);
            float angle = (float) ((double) (state / 15.0F * 90.0F / 180.0F) * Math.PI);
            ((SuperByteBuffer) this.transform(handle, leverState).translate(0.5, 0.0625, 0.5).rotate(Direction.EAST, angle)).translate(-0.5, -0.0625, -0.5);
            handle.light(light).renderInto(ms, vb);
            int color = Color.mixColors(2884352, 13434880, state / 15.0F);
            SuperByteBuffer indicator = this.transform(CachedBufferer.partial(AllPartialModels.ANALOG_LEVER_INDICATOR, leverState), leverState);
            indicator.light(light).color(color).renderInto(ms, vb);
        }
    }

    private SuperByteBuffer transform(SuperByteBuffer buffer, BlockState leverState) {
        AttachFace face = (AttachFace) leverState.m_61143_(AnalogLeverBlock.f_53179_);
        float rX = face == AttachFace.FLOOR ? 0.0F : (face == AttachFace.WALL ? 90.0F : 180.0F);
        float rY = AngleHelper.horizontalAngle((Direction) leverState.m_61143_(AnalogLeverBlock.f_54117_));
        buffer.rotateCentered(Direction.UP, (float) ((double) (rY / 180.0F) * Math.PI));
        buffer.rotateCentered(Direction.EAST, (float) ((double) (rX / 180.0F) * Math.PI));
        return buffer;
    }
}