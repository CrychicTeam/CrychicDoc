package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelFart;
import com.github.alexthe666.alexsmobs.entity.EntityFart;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class RenderFart extends EntityRenderer<EntityFart> {

    private static final ResourceLocation FART_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/fart.png");

    private static final ModelFart MODEL = new ModelFart();

    public RenderFart(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(EntityFart entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float f = Math.min((float) entityIn.f_19797_ + partialTicks, 30.0F) / 30.0F;
        float alpha = 1.0F - f;
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.15F, 0.0);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 180.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityTranslucent(FART_TEXTURE));
        MODEL.setupAnim(entityIn, 0.0F, 0.0F, partialTicks, 0.0F, 0.0F);
        MODEL.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(EntityFart entity) {
        return FART_TEXTURE;
    }
}