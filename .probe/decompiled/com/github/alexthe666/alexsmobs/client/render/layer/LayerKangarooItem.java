package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelKangaroo;
import com.github.alexthe666.alexsmobs.client.render.RenderKangaroo;
import com.github.alexthe666.alexsmobs.entity.EntityKangaroo;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerKangarooItem extends RenderLayer<EntityKangaroo, ModelKangaroo> {

    public LayerKangarooItem(RenderKangaroo render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityKangaroo entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.getItemBySlot(EquipmentSlot.MAINHAND);
        matrixStackIn.pushPose();
        boolean left = entitylivingbaseIn.m_21526_();
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.scale(0.5F, 0.5F, 0.5F);
            matrixStackIn.translate(0.0, 1.5, 0.0);
        }
        matrixStackIn.pushPose();
        this.translateToHand(matrixStackIn, left);
        matrixStackIn.translate(0.0F, 0.75F, -0.125F);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-110.0F));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStackIn.scale(0.8F, 0.8F, 0.8F);
        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        renderer.renderItem(entitylivingbaseIn, itemstack, left ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }

    protected void translateToHand(PoseStack matrixStack, boolean left) {
        ((ModelKangaroo) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelKangaroo) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelKangaroo) this.m_117386_()).chest.translateAndRotate(matrixStack);
        if (left) {
            ((ModelKangaroo) this.m_117386_()).arm_left.translateAndRotate(matrixStack);
        } else {
            ((ModelKangaroo) this.m_117386_()).arm_right.translateAndRotate(matrixStack);
        }
    }
}