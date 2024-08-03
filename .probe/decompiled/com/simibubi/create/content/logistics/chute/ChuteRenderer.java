package com.simibubi.create.content.logistics.chute;

import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;

public class ChuteRenderer extends SafeBlockEntityRenderer<ChuteBlockEntity> {

    public ChuteRenderer(BlockEntityRendererProvider.Context context) {
    }

    protected void renderSafe(ChuteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (!be.item.isEmpty()) {
            BlockState blockState = be.m_58900_();
            if (blockState.m_61143_(ChuteBlock.FACING) == Direction.DOWN) {
                if (blockState.m_61143_(ChuteBlock.SHAPE) == ChuteBlock.Shape.WINDOW || be.bottomPullDistance != 0.0F && !(be.itemPosition.getValue(partialTicks) > 0.5F)) {
                    renderItem(be, partialTicks, ms, buffer, light, overlay);
                }
            }
        }
    }

    public static void renderItem(ChuteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        TransformStack msr = TransformStack.cast(ms);
        ms.pushPose();
        msr.centre();
        float itemScale = 0.5F;
        float itemPosition = be.itemPosition.getValue(partialTicks);
        ms.translate(0.0, -0.5 + (double) itemPosition, 0.0);
        ms.scale(itemScale, itemScale, itemScale);
        msr.rotateX((double) (itemPosition * 180.0F));
        msr.rotateY((double) (itemPosition * 180.0F));
        itemRenderer.renderStatic(be.item, ItemDisplayContext.FIXED, light, overlay, ms, buffer, be.m_58904_(), 0);
        ms.popPose();
    }
}