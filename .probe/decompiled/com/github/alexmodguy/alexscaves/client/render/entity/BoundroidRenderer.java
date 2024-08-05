package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.BoundroidModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class BoundroidRenderer extends MobRenderer<BoundroidEntity, BoundroidModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/boundroid.png");

    private static final ResourceLocation TEXTURE_SCARED = new ResourceLocation("alexscaves:textures/entity/boundroid_scared.png");

    public BoundroidRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BoundroidModel(), 0.8F);
        this.m_115326_(new BoundroidRenderer.LayerGlow());
    }

    protected void scale(BoundroidEntity mob, PoseStack matrixStackIn, float partialTicks) {
    }

    public ResourceLocation getTextureLocation(BoundroidEntity entity) {
        return entity.isScared() ? TEXTURE_SCARED : TEXTURE;
    }

    protected boolean isShaking(BoundroidEntity entity) {
        return entity.isScared() || super.m_5936_(entity);
    }

    class LayerGlow extends RenderLayer<BoundroidEntity, BoundroidModel> {

        public LayerGlow() {
            super(BoundroidRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, BoundroidEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(BoundroidRenderer.this.getTextureLocation(entitylivingbaseIn)));
            float alpha = (float) (1.0 + Math.sin((double) (ageInTicks * 0.1F + 2.0F))) * 0.1F + 0.5F;
            ((BoundroidModel) this.m_117386_()).hideChains();
            ((BoundroidModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
            ((BoundroidModel) this.m_117386_()).showChains();
        }
    }
}