package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.DeepOneKnightModel;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneKnightEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class DeepOneKnightRenderer extends MobRenderer<DeepOneKnightEntity, DeepOneKnightModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one_knight.png");

    private static final ResourceLocation TEXTURE_NOON = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one_knight_noon.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/deep_one/deep_one_knight_glow.png");

    public DeepOneKnightRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new DeepOneKnightModel(), 0.45F);
        this.m_115326_(new DeepOneKnightRenderer.LayerGlow());
        this.m_115326_(new ItemInHandLayer<>(this, renderManagerIn.getItemInHandRenderer()));
    }

    protected void scale(DeepOneKnightEntity mob, PoseStack matrixStackIn, float partialTicks) {
        if (mob.isSummoned()) {
            matrixStackIn.translate(0.0F, (mob.m_20206_() + 1.0F) * (1.0F - mob.getSummonProgress(partialTicks)), 0.0F);
        }
    }

    public ResourceLocation getTextureLocation(DeepOneKnightEntity entity) {
        return entity.isNoon() ? TEXTURE_NOON : TEXTURE;
    }

    class LayerGlow extends RenderLayer<DeepOneKnightEntity, DeepOneKnightModel> {

        public LayerGlow() {
            super(DeepOneKnightRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, DeepOneKnightEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!entitylivingbaseIn.m_20145_() && !entitylivingbaseIn.isNoon()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.eyes(DeepOneKnightRenderer.TEXTURE_GLOW));
                float alpha = 1.0F;
                ((DeepOneKnightModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, 240, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
            }
        }
    }
}