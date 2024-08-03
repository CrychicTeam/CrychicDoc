package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.BinBlockEntity;
import java.util.Random;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class BinBlockEntityRenderer implements BlockEntityRenderer<BinBlockEntity> {

    private final ItemRenderer itemRenderer;

    Random random = new Random();

    public BinBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(BinBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null && !blockEntity.inventory.getStackInSlot(0).isEmpty()) {
            ItemStack stack = blockEntity.inventory.getStackInSlot(0);
            int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
            this.random.setSeed((long) seed);
            BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
            int itemCount = (int) Math.ceil((double) stack.getCount() / 4.0);
            for (int i = 0; i < itemCount; i++) {
                float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
                poseStack.pushPose();
                poseStack.translate(0.5, 0.1525 + (double) i * 0.05, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(this.random.nextFloat() * 360.0F));
                poseStack.translate(-0.5, f2 < 0.4F ? 0.036 : 0.0, -0.5);
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                poseStack.translate(0.5 + 0.1 * (double) this.random.nextFloat(), -0.1875 + 0.1 * (double) this.random.nextFloat(), 0.0);
                poseStack.scale(1.5F, 1.5F, 1.5F);
                poseStack.translate(0.0, f2 < 0.4F ? 0.1 : 0.25, 0.0);
                this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
                poseStack.popPose();
            }
        }
    }
}