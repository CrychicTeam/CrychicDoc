package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.HullbreakerModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.entity.PartEntity;

public class HullbreakerRenderer extends MobRenderer<HullbreakerEntity, HullbreakerModel> implements CustomBookEntityRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/hullbreaker.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/hullbreaker_glow.png");

    private boolean sepia;

    public HullbreakerRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new HullbreakerModel(), 2.25F);
        this.m_115326_(new HullbreakerRenderer.LayerGlow());
    }

    public void render(HullbreakerEntity entity, float f1, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light) {
        ((HullbreakerModel) this.f_115290_).straighten = this.sepia;
        super.render(entity, f1, partialTicks, poseStack, source, light);
    }

    @Nullable
    protected RenderType getRenderType(HullbreakerEntity mob, boolean normal, boolean translucent, boolean outline) {
        ResourceLocation resourcelocation = this.getTextureLocation(mob);
        if (translucent) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (normal) {
            return this.sepia ? ACRenderTypes.getBookWidget(resourcelocation, true) : RenderType.entityTranslucent(resourcelocation);
        } else {
            return outline ? RenderType.outline(resourcelocation) : null;
        }
    }

    protected void scale(HullbreakerEntity mob, PoseStack matrixStackIn, float partialTicks) {
    }

    public ResourceLocation getTextureLocation(HullbreakerEntity entity) {
        return TEXTURE;
    }

    public boolean shouldRender(HullbreakerEntity entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        } else {
            for (PartEntity part : entity.getParts()) {
                if (camera.isVisible(part.m_6921_())) {
                    return true;
                }
            }
            return false;
        }
    }

    protected float getFlipDegrees(HullbreakerEntity hullbreakerEntity) {
        return 0.0F;
    }

    @Override
    public void setSepiaFlag(boolean sepiaFlag) {
        this.sepia = sepiaFlag;
    }

    class LayerGlow extends RenderLayer<HullbreakerEntity, HullbreakerModel> {

        public LayerGlow() {
            super(HullbreakerRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, HullbreakerEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(HullbreakerRenderer.TEXTURE_GLOW));
            float alpha = (float) ((Math.sin((double) entitylivingbaseIn.getPulseAmount(partialTicks)) + 1.0) * 0.5);
            ((HullbreakerModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}