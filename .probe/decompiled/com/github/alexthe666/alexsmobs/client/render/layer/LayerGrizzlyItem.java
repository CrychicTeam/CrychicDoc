package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelGrizzlyBear;
import com.github.alexthe666.alexsmobs.client.render.RenderGrizzlyBear;
import com.github.alexthe666.alexsmobs.entity.EntityGrizzlyBear;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerGrizzlyItem extends RenderLayer<EntityGrizzlyBear, ModelGrizzlyBear> {

    public LayerGrizzlyItem(RenderGrizzlyBear renderGrizzlyBear) {
        super(renderGrizzlyBear);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGrizzlyBear entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.MAINHAND);
        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        matrixStackIn.pushPose();
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(0.0, 2.75, 0.125);
            this.translateToHand(false, matrixStackIn);
            matrixStackIn.translate(0.2F, 0.7F, -0.4F);
            matrixStackIn.scale(2.8F, 2.8F, 2.8F);
        } else {
            this.translateToHand(false, matrixStackIn);
            matrixStackIn.translate(0.2F, 0.7F, -0.4F);
        }
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(10.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(100.0F));
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    protected void translateToHand(boolean left, PoseStack matrixStack) {
        ((ModelGrizzlyBear) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelGrizzlyBear) this.m_117386_()).midbody.translateAndRotate(matrixStack);
        ((ModelGrizzlyBear) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelGrizzlyBear) this.m_117386_()).right_arm.translateAndRotate(matrixStack);
    }
}