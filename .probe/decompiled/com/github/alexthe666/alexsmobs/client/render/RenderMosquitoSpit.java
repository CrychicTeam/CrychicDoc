package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.entity.EntityMosquitoSpit;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.LlamaSpit;

public class RenderMosquitoSpit extends EntityRenderer<EntityMosquitoSpit> {

    private static final ResourceLocation SPIT_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/mosquito_spit.png");

    private final LlamaSpitModel<LlamaSpit> model;

    public RenderMosquitoSpit(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new LlamaSpitModel<>(renderManagerIn.bakeLayer(ModelLayers.LLAMA_SPIT));
    }

    public void render(EntityMosquitoSpit entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.15F, 0.0);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.m_103119_(SPIT_TEXTURE));
        this.model.m_7695_(matrixStackIn, ivertexbuilder, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(EntityMosquitoSpit entity) {
        return SPIT_TEXTURE;
    }
}