package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.RadgillModel;
import com.github.alexmodguy.alexscaves.server.entity.living.RadgillEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class RadgillRenderer extends MobRenderer<RadgillEntity, RadgillModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/radgill.png");

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("alexscaves:textures/entity/radgill_eyes.png");

    public RadgillRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new RadgillModel(), 0.25F);
        this.m_115326_(new RadgillRenderer.LayerGlow());
    }

    protected void scale(RadgillEntity mob, PoseStack matrixStackIn, float partialTicks) {
        matrixStackIn.scale(0.9F, 0.9F, 0.9F);
    }

    public ResourceLocation getTextureLocation(RadgillEntity entity) {
        return TEXTURE;
    }

    class LayerGlow extends RenderLayer<RadgillEntity, RadgillModel> {

        public LayerGlow() {
            super(RadgillRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, RadgillEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.eyes(RadgillRenderer.TEXTURE_EYES));
            float alpha = 1.0F;
            ((RadgillModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}