package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelAlligatorSnappingTurtle;
import com.github.alexthe666.alexsmobs.entity.EntityAlligatorSnappingTurtle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderAlligatorSnappingTurtle extends MobRenderer<EntityAlligatorSnappingTurtle, ModelAlligatorSnappingTurtle> {

    private static final ResourceLocation TEXTURE_MOSS = new ResourceLocation("alexsmobs:textures/entity/alligator_snapping_turtle_moss.png");

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/alligator_snapping_turtle.png");

    public RenderAlligatorSnappingTurtle(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelAlligatorSnappingTurtle(), 0.75F);
        this.m_115326_(new RenderAlligatorSnappingTurtle.AlligatorSnappingTurtleMossLayer(this));
    }

    protected void scale(EntityAlligatorSnappingTurtle entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
        float d = entitylivingbaseIn.getTurtleScale() < 0.01F ? 1.0F : entitylivingbaseIn.getTurtleScale();
        matrixStackIn.scale(d, d, d);
    }

    public ResourceLocation getTextureLocation(EntityAlligatorSnappingTurtle entity) {
        return TEXTURE;
    }

    static class AlligatorSnappingTurtleMossLayer extends RenderLayer<EntityAlligatorSnappingTurtle, ModelAlligatorSnappingTurtle> {

        public AlligatorSnappingTurtleMossLayer(RenderAlligatorSnappingTurtle p_i50928_1_) {
            super(p_i50928_1_);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityAlligatorSnappingTurtle entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.getMoss() > 0) {
                float mossAlpha = 0.15F * (float) Mth.clamp(entitylivingbaseIn.getMoss(), 0, 10);
                VertexConsumer mossbuffer = bufferIn.getBuffer(AMRenderTypes.m_110473_(RenderAlligatorSnappingTurtle.TEXTURE_MOSS));
                ((ModelAlligatorSnappingTurtle) this.m_117386_()).renderToBuffer(matrixStackIn, mossbuffer, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, Math.min(1.0F, mossAlpha));
            }
        }
    }
}