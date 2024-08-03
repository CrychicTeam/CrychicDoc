package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelBunfungus;
import com.github.alexthe666.alexsmobs.entity.EntityBunfungus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderBunfungus extends MobRenderer<EntityBunfungus, ModelBunfungus> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/bunfungus.png");

    private static final ResourceLocation TEXTURE_SLEEPING = new ResourceLocation("alexsmobs:textures/entity/bunfungus_sleeping.png");

    public RenderBunfungus(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelBunfungus(), 0.6F);
        this.m_115326_(new RenderBunfungus.LayerHeldItem(this));
    }

    protected void scale(EntityBunfungus rabbit, PoseStack matrixStackIn, float partialTickTime) {
        float f = (float) rabbit.prevTransformTime + (float) (rabbit.transformsIn() - rabbit.prevTransformTime) * partialTickTime;
        float f1 = (50.0F - f) / 50.0F;
        float f2 = f1 * 0.7F + 0.3F;
        matrixStackIn.scale(f2, f2, f2);
    }

    public ResourceLocation getTextureLocation(EntityBunfungus entity) {
        return entity.isSleeping() ? TEXTURE_SLEEPING : TEXTURE;
    }

    static class LayerHeldItem extends RenderLayer<EntityBunfungus, ModelBunfungus> {

        public LayerHeldItem(RenderBunfungus render) {
            super(render);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityBunfungus entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.MAINHAND);
            matrixStackIn.pushPose();
            if (entitylivingbaseIn.m_6162_()) {
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                matrixStackIn.translate(0.0, 1.5, 0.0);
            }
            matrixStackIn.pushPose();
            this.translateToHand(matrixStackIn);
            matrixStackIn.translate(0.3F, 0.45F, -0.15F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
            matrixStackIn.scale(1.15F, 1.15F, 1.15F);
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }

        protected void translateToHand(PoseStack matrixStack) {
            ((ModelBunfungus) this.m_117386_()).root.translateAndRotate(matrixStack);
            ((ModelBunfungus) this.m_117386_()).body.translateAndRotate(matrixStack);
            ((ModelBunfungus) this.m_117386_()).right_arm.translateAndRotate(matrixStack);
        }
    }
}