package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelMimicube;
import com.github.alexthe666.alexsmobs.client.render.RenderMimicube;
import com.github.alexthe666.alexsmobs.entity.EntityMimicube;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

public class LayerMimicubeHeldItem extends RenderLayer<EntityMimicube, ModelMimicube> {

    public LayerMimicubeHeldItem(RenderMimicube render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityMimicube entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemRight = entitylivingbaseIn.m_21205_();
        ItemStack itemLeft = entitylivingbaseIn.m_21206_();
        float rightSwap = Mth.lerp(partialTicks, entitylivingbaseIn.prevRightSwapProgress, entitylivingbaseIn.rightSwapProgress) * 0.2F;
        float leftSwap = Mth.lerp(partialTicks, entitylivingbaseIn.prevLeftSwapProgress, entitylivingbaseIn.leftSwapProgress) * 0.2F;
        float attackprogress = Mth.lerp(partialTicks, entitylivingbaseIn.prevAttackProgress, entitylivingbaseIn.attackProgress);
        double bob1 = Math.cos((double) (ageInTicks * 0.1F)) * 0.1F + 0.1F;
        double bob2 = Math.sin((double) (ageInTicks * 0.1F)) * 0.1F + 0.1F;
        if (!itemRight.isEmpty()) {
            matrixStackIn.pushPose();
            this.translateToHand(false, matrixStackIn);
            matrixStackIn.translate(-0.5, 0.1F - bob1, -0.1F);
            matrixStackIn.scale(0.9F * (1.0F - rightSwap), 0.9F * (1.0F - rightSwap), 0.9F * (1.0F - rightSwap));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            if (itemRight.getItem() instanceof ShieldItem) {
                matrixStackIn.translate(-0.1F, 0.0F, -0.4F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-10.0F));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(360.0F * rightSwap));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-40.0F * attackprogress));
            Minecraft.getInstance().getItemRenderer().renderStatic(itemRight, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, rightSwap > 0.0F ? (int) (-100.0F * rightSwap) : packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), matrixStackIn, bufferIn, entitylivingbaseIn.m_9236_(), 0);
            matrixStackIn.popPose();
        }
        if (!itemLeft.isEmpty()) {
            matrixStackIn.pushPose();
            this.translateToHand(false, matrixStackIn);
            matrixStackIn.translate(0.45F, 0.1F - bob2, -0.1F);
            matrixStackIn.scale(0.9F * (1.0F - leftSwap), 0.9F * (1.0F - leftSwap), 0.9F * (1.0F - leftSwap));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            int clampedLight = (int) Math.floor((double) ((float) packedLightIn * (1.0F - leftSwap)));
            if (itemLeft.getItem() instanceof ShieldItem) {
                matrixStackIn.translate(-0.2F, 0.0F, -0.4F);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            }
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(10.0F));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(360.0F * leftSwap));
            Minecraft.getInstance().getItemRenderer().renderStatic(itemLeft, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, leftSwap > 0.0F ? (int) (-100.0F * leftSwap) : packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), matrixStackIn, bufferIn, entitylivingbaseIn.m_9236_(), 0);
            matrixStackIn.popPose();
        }
    }

    protected void translateToHand(boolean left, PoseStack matrixStack) {
        ((ModelMimicube) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelMimicube) this.m_117386_()).innerbody.translateAndRotate(matrixStack);
    }
}