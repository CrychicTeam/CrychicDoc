package com.rekindled.embers.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.rekindled.embers.blockentity.DawnstoneAnvilBlockEntity;
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

public class DawnstoneAnvilBlockEntityRenderer implements BlockEntityRenderer<DawnstoneAnvilBlockEntity> {

    static Random random = new Random();

    private final ItemRenderer itemRenderer;

    public DawnstoneAnvilBlockEntityRenderer(BlockEntityRendererProvider.Context pContext) {
        this.itemRenderer = pContext.getItemRenderer();
    }

    public void render(DawnstoneAnvilBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (blockEntity != null) {
            random.setSeed((long) blockEntity.m_58899_().hashCode());
            if (!blockEntity.inventory.getStackInSlot(0).isEmpty()) {
                poseStack.pushPose();
                ItemStack stack = blockEntity.inventory.getStackInSlot(0);
                int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
                BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
                float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
                poseStack.translate(0.5, (double) (0.25F * f2) + 0.8937, 0.5);
                poseStack.mulPose(Axis.YP.rotation(random.nextFloat((float) (Math.PI * 2))));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
                poseStack.popPose();
            }
            if (!blockEntity.inventory.getStackInSlot(1).isEmpty()) {
                poseStack.pushPose();
                ItemStack stack = blockEntity.inventory.getStackInSlot(1);
                int seed = stack.isEmpty() ? 187 : Item.getId(stack.getItem()) + stack.getDamageValue();
                BakedModel bakedmodel = this.itemRenderer.getModel(stack, blockEntity.m_58904_(), null, seed);
                float f2 = bakedmodel.getTransforms().getTransform(ItemDisplayContext.GROUND).scale.y();
                poseStack.translate(0.5, (double) (0.25F * f2) + 0.9245, 0.5);
                poseStack.mulPose(Axis.YP.rotation(random.nextFloat((float) (Math.PI * 2))));
                poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                this.itemRenderer.render(stack, ItemDisplayContext.GROUND, false, poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY, bakedmodel);
                poseStack.popPose();
            }
        }
    }
}