package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelMantisShrimp;
import com.github.alexthe666.alexsmobs.client.render.RenderMantisShrimp;
import com.github.alexthe666.alexsmobs.entity.EntityMantisShrimp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerMantisShrimpItem extends RenderLayer<EntityMantisShrimp, ModelMantisShrimp> {

    public LayerMantisShrimpItem(RenderMantisShrimp render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityMantisShrimp entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.MAINHAND);
        matrixStackIn.pushPose();
        boolean left = entitylivingbaseIn.m_21526_();
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
        }
        matrixStackIn.pushPose();
        this.translateToHand(matrixStackIn, left);
        matrixStackIn.translate(left ? 0.075F : -0.075F, 0.45F, -0.125F);
        if (!Minecraft.getInstance().getItemRenderer().getItemModelShaper().getItemModel(itemstack).isGui3d()) {
            matrixStackIn.translate(0.0F, 0.0F, 0.05F);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(left ? -40.0F : 40.0F));
        }
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-2.5F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-180.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    protected void translateToHand(PoseStack matrixStack, boolean left) {
        ((ModelMantisShrimp) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelMantisShrimp) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelMantisShrimp) this.m_117386_()).head.translateAndRotate(matrixStack);
        if (left) {
            ((ModelMantisShrimp) this.m_117386_()).arm_left.translateAndRotate(matrixStack);
            ((ModelMantisShrimp) this.m_117386_()).fist_left.translateAndRotate(matrixStack);
        } else {
            ((ModelMantisShrimp) this.m_117386_()).arm_right.translateAndRotate(matrixStack);
            ((ModelMantisShrimp) this.m_117386_()).fist_right.translateAndRotate(matrixStack);
        }
    }
}