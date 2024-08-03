package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.blockentity.ItemTransferBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ItemTransferBlockEntityRenderer implements BlockEntityRenderer<ItemTransferBlockEntity> {

    private final ItemRenderer itemRenderer;

    public ItemTransferBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(ItemTransferBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null && !blockEntity.filterItem.isEmpty()) {
            poseStack.pushPose();
            ItemStack stack = blockEntity.filterItem;
            int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
            float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
            poseStack.translate(0.5, (double) (0.4F * f2) + 0.21, 0.5);
            if (f2 > 0.4F) {
                poseStack.scale(0.75F, 0.75F, 0.75F);
            }
            poseStack.mulPose(Axis.YP.rotation(((float) EmbersClientEvents.ticks + partialTick) / 20.0F));
            this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
            poseStack.popPose();
        }
    }
}