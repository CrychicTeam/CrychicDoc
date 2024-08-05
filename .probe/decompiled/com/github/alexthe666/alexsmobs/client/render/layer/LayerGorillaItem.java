package com.github.alexthe666.alexsmobs.client.render.layer;

import com.github.alexthe666.alexsmobs.client.model.ModelGorilla;
import com.github.alexthe666.alexsmobs.client.render.RenderGorilla;
import com.github.alexthe666.alexsmobs.entity.EntityGorilla;
import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class LayerGorillaItem extends RenderLayer<EntityGorilla, ModelGorilla> {

    public LayerGorillaItem(RenderGorilla render) {
        super(render);
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityGorilla entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.MAINHAND);
        String name = entitylivingbaseIn.m_7755_().getString().toLowerCase();
        ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
        if (name.contains("harambe")) {
            ItemStack haloStack = new ItemStack(AMItemRegistry.HALO.get());
            matrixStackIn.pushPose();
            ((ModelGorilla) this.m_117386_()).root.translateAndRotate(matrixStackIn);
            ((ModelGorilla) this.m_117386_()).body.translateAndRotate(matrixStackIn);
            ((ModelGorilla) this.m_117386_()).chest.translateAndRotate(matrixStackIn);
            ((ModelGorilla) this.m_117386_()).head.translateAndRotate(matrixStackIn);
            float f = 0.1F * (float) Math.sin((double) (((float) entitylivingbaseIn.f_19797_ + partialTicks) * 0.1F)) + (entitylivingbaseIn.m_6162_() ? 0.2F : 0.0F);
            matrixStackIn.translate(0.0F, -0.7F - f, -0.2F);
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            matrixStackIn.scale(1.3F, 1.3F, 1.3F);
            renderer.renderItem(entitylivingbaseIn, haloStack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
        matrixStackIn.pushPose();
        if (entitylivingbaseIn.m_6162_()) {
            matrixStackIn.scale(0.35F, 0.35F, 0.35F);
            matrixStackIn.translate(-0.1, 2.0, -1.15);
            this.translateToHand(false, matrixStackIn);
            matrixStackIn.translate(-0.4F, 0.75F, -0.0F);
            matrixStackIn.scale(2.8F, 2.8F, 2.8F);
        } else {
            this.translateToHand(false, matrixStackIn);
            matrixStackIn.translate(-0.4F, 0.75F, -0.0F);
        }
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(-2.5F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
        if (itemstack.getItem() instanceof BlockItem) {
            matrixStackIn.scale(2.0F, 2.0F, 2.0F);
        }
        renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
    }

    protected void translateToHand(boolean left, PoseStack matrixStack) {
        ((ModelGorilla) this.m_117386_()).root.translateAndRotate(matrixStack);
        ((ModelGorilla) this.m_117386_()).body.translateAndRotate(matrixStack);
        ((ModelGorilla) this.m_117386_()).chest.translateAndRotate(matrixStack);
        ((ModelGorilla) this.m_117386_()).leftArm.translateAndRotate(matrixStack);
    }
}