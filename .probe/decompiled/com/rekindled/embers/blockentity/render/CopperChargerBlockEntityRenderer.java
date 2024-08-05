package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.CopperChargerBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class CopperChargerBlockEntityRenderer implements BlockEntityRenderer<CopperChargerBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CopperChargerBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(CopperChargerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null && !blockEntity.inventory.getStackInSlot(0).isEmpty()) {
            poseStack.pushPose();
            ItemStack stack = blockEntity.inventory.getStackInSlot(0);
            int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
            float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
            poseStack.translate(0.5, (double) (0.4F * f2) + 0.13, 0.5);
            if (f2 > 0.4F) {
                poseStack.scale(0.75F, 0.75F, 0.75F);
            }
            poseStack.mulPose(Axis.YP.rotation(((float) blockEntity.angle + partialTick) / 20.0F));
            this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
            poseStack.popPose();
        }
    }

    protected int getRenderAmount(ItemStack pStack) {
        int i = 1;
        if (pStack.getCount() > 48) {
            i = 5;
        } else if (pStack.getCount() > 32) {
            i = 4;
        } else if (pStack.getCount() > 16) {
            i = 3;
        } else if (pStack.getCount() > 1) {
            i = 2;
        }
        return i;
    }
}