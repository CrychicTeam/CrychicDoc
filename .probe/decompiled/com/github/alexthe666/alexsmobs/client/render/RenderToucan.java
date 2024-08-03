package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelToucan;
import com.github.alexthe666.alexsmobs.entity.EntityToucan;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RenderToucan extends MobRenderer<EntityToucan, ModelToucan> {

    private static final ResourceLocation TEXTURE_0 = new ResourceLocation("alexsmobs:textures/entity/toucan/toucan_0.png");

    private static final ResourceLocation TEXTURE_1 = new ResourceLocation("alexsmobs:textures/entity/toucan/toucan_1.png");

    private static final ResourceLocation TEXTURE_2 = new ResourceLocation("alexsmobs:textures/entity/toucan/toucan_2.png");

    private static final ResourceLocation TEXTURE_3 = new ResourceLocation("alexsmobs:textures/entity/toucan/toucan_3.png");

    private static final ResourceLocation TEXTURE_GOLDEN = new ResourceLocation("alexsmobs:textures/entity/toucan/toucan_gold.png");

    private static final ResourceLocation TEXTURE_SAM = new ResourceLocation("alexsmobs:textures/entity/toucan/toucan_sam.png");

    public RenderToucan(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelToucan(), 0.2F);
        this.m_115326_(new RenderToucan.LayerGlint(this));
        this.m_115326_(new RenderToucan.LayerHeldItem(this));
    }

    protected void scale(EntityToucan entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.9F, 0.9F, 0.9F);
    }

    public ResourceLocation getTextureLocation(EntityToucan entity) {
        if (entity.isSam()) {
            return TEXTURE_SAM;
        } else if (entity.isGolden()) {
            return TEXTURE_GOLDEN;
        } else {
            switch(entity.getVariant()) {
                case 1:
                    return TEXTURE_1;
                case 2:
                    return TEXTURE_2;
                case 3:
                    return TEXTURE_3;
                default:
                    return TEXTURE_0;
            }
        }
    }

    static class LayerGlint extends RenderLayer<EntityToucan, ModelToucan> {

        public LayerGlint(RenderToucan render) {
            super(render);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityToucan entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.isEnchanted()) {
                VertexConsumer vertexconsumer = ItemRenderer.getArmorFoilBuffer(bufferIn, RenderType.armorCutoutNoCull(RenderToucan.TEXTURE_GOLDEN), false, true);
                ((ModelToucan) this.m_117386_()).renderToBuffer(matrixStackIn, vertexconsumer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }

    static class LayerHeldItem extends RenderLayer<EntityToucan, ModelToucan> {

        public LayerHeldItem(RenderToucan render) {
            super(render);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityToucan entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack itemstack = entitylivingbaseIn.m_6844_(EquipmentSlot.MAINHAND);
            matrixStackIn.pushPose();
            if (entitylivingbaseIn.m_6162_()) {
                matrixStackIn.scale(0.5F, 0.5F, 0.5F);
                matrixStackIn.translate(0.0, 1.5, 0.0);
            }
            matrixStackIn.pushPose();
            this.translateToHand(matrixStackIn);
            matrixStackIn.translate(-0.07F, -0.1F, -0.25F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(-45.0F));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
            ItemInHandRenderer renderer = Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer();
            renderer.renderItem(entitylivingbaseIn, itemstack, ItemDisplayContext.GROUND, false, matrixStackIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
            matrixStackIn.popPose();
        }

        protected void translateToHand(PoseStack matrixStack) {
            ((ModelToucan) this.m_117386_()).root.translateAndRotate(matrixStack);
            ((ModelToucan) this.m_117386_()).body.translateAndRotate(matrixStack);
            ((ModelToucan) this.m_117386_()).head.translateAndRotate(matrixStack);
        }
    }
}