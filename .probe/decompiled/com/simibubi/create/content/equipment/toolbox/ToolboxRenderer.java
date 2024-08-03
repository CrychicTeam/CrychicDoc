package com.simibubi.create.content.equipment.toolbox;

import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;
import com.simibubi.create.foundation.render.SuperByteBuffer;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class ToolboxRenderer extends SmartBlockEntityRenderer<ToolboxBlockEntity> {

    public ToolboxRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    protected void renderSafe(ToolboxBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BlockState blockState = blockEntity.m_58900_();
        Direction facing = ((Direction) blockState.m_61143_(ToolboxBlock.f_54117_)).getOpposite();
        SuperByteBuffer lid = CachedBufferer.partial((PartialModel) AllPartialModels.TOOLBOX_LIDS.get(blockEntity.getColor()), blockState);
        SuperByteBuffer drawer = CachedBufferer.partial(AllPartialModels.TOOLBOX_DRAWER, blockState);
        float lidAngle = blockEntity.lid.getValue(partialTicks);
        float drawerOffset = blockEntity.drawers.getValue(partialTicks);
        VertexConsumer builder = buffer.getBuffer(RenderType.cutoutMipped());
        ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) lid.centre()).rotateY((double) (-facing.toYRot()))).unCentre()).translate(0.0, 0.375, 0.75).rotateX((double) (135.0F * lidAngle))).translate(0.0, -0.375, -0.75).light(light).renderInto(ms, builder);
        for (int offset : Iterate.zeroAndOne) {
            ((SuperByteBuffer) ((SuperByteBuffer) ((SuperByteBuffer) drawer.centre()).rotateY((double) (-facing.toYRot()))).unCentre()).translate(0.0, (double) ((float) (offset * 1) / 8.0F), (double) (-drawerOffset * 0.175F * (float) (2 - offset))).light(light).renderInto(ms, builder);
        }
    }
}