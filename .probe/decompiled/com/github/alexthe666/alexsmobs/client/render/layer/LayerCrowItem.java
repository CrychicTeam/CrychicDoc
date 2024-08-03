package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelCrow;
import com.github.alexthe666.alexsmobs.client.render.RenderCrow;
import com.github.alexthe666.alexsmobs.entity.EntityCrow;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerCrowItem extends RenderLayer<EntityCrow, ModelCrow> {

    public LayerCrowItem(RenderCrow render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityCrow entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.MAINHAND);
        matrixStackIn.pushPose();
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
        }
        matrixStackIn.pushPose();
        this.translateToHand(matrixStackIn);
        matrixStackIn.translate(0.0F, -0.09F, -0.125F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-2.5F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
        matrixStackIn.scale(0.75F, 0.75F, 0.75F);
        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    protected void translateToHand(PoseStack matrixStack) {
        ((ModelCrow) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelCrow) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelCrow) this.m_117386_()).head.translateAndRotate(matrixStack);
    }
}