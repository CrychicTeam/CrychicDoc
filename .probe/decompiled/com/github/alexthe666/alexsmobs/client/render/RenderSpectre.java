package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelSpectre;
import com.github.alexthe666.alexsmobs.entity.EntitySpectre;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class RenderSpectre extends MobRenderer<EntitySpectre, ModelSpectre> {

    private static final ResourceLocation TEXTURE_BONE = new ResourceLocation("alexsmobs:textures/entity/spectre_bone.png");

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/spectre.png");

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("alexsmobs:textures/entity/spectre_glow.png");

    private static final ResourceLocation TEXTURE_LEAD = new ResourceLocation("alexsmobs:textures/entity/spectre_lead.png");

    public RenderSpectre(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelSpectre(), 0.5F);
        this.m_115326_(new RenderSpectre.SpectreEyesLayer(this));
        this.m_115326_(new RenderSpectre.SpectreMembraneLayer(this));
    }

    protected void scale(EntitySpectre entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.3F, 1.3F, 1.3F);
    }

    protected int getBlockLightLevel(EntitySpectre entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getTextureLocation(EntitySpectre entity) {
        return TEXTURE_BONE;
    }

    public float getAlphaForRender(EntitySpectre entityIn, float partialTicks) {
        return ((float) Math.sin((double) (((float) entityIn.f_19797_ + partialTicks) * 0.1F)) + 1.5F) * 0.1F + 0.5F;
    }

    static class SpectreEyesLayer extends EyesLayer<EntitySpectre, ModelSpectre> {

        public SpectreEyesLayer(RenderSpectre p_i50928_1_) {
            super(p_i50928_1_);
        }

        @Override
        public RenderType renderType() {
            return RenderType.eyes(RenderSpectre.TEXTURE_EYES);
        }
    }

    class SpectreMembraneLayer extends RenderLayer<EntitySpectre, ModelSpectre> {

        public SpectreMembraneLayer(RenderSpectre p_i50928_1_) {
            super(p_i50928_1_);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntitySpectre entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer lvt_11_1_ = bufferIn.getBuffer(this.getRenderType());
            ((ModelSpectre) this.m_117386_()).m_7695_(matrixStackIn, lvt_11_1_, 15728640, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, RenderSpectre.this.getAlphaForRender(entitylivingbaseIn, partialTicks));
            if (entitylivingbaseIn.m_21523_()) {
                VertexConsumer lead = bufferIn.getBuffer(AMRenderTypes.m_110458_(RenderSpectre.TEXTURE_LEAD));
                ((ModelSpectre) this.m_117386_()).m_7695_(matrixStackIn, lead, 15728640, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }

        public RenderType getRenderType() {
            return AMRenderTypes.getSpectreBones(RenderSpectre.TEXTURE);
        }
    }
}