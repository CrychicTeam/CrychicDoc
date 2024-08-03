package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelSeal;
import com.github.alexthe666.alexsmobs.client.render.RenderSeal;
import com.github.alexthe666.alexsmobs.entity.EntitySeal;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerSealItem extends RenderLayer<EntitySeal, ModelSeal> {

    public LayerSealItem(RenderSeal render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntitySeal entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.m_21205_();
        matrixStackIn.pushPose();
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
        }
        matrixStackIn.pushPose();
        this.translateToHand(matrixStackIn);
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.translate(0.0, 0.1F, -0.6);
        }
        matrixStackIn.translate(-0.1F, 0.15F, -0.6F);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-45.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    protected void translateToHand(PoseStack matrixStack) {
        ((ModelSeal) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelSeal) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelSeal) this.m_117386_()).head.translateAndRotate(matrixStack);
    }
}