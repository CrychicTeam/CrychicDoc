package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelStraddleboard;
import com.github.alexthe666.alexsmobs.entity.EntityStraddleboard;
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
import org.joml.Quaternionf;

public class RenderStraddleboard extends EntityRenderer<EntityStraddleboard> {

    private static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation("alexsmobs:textures/entity/straddleboard_overlay.png");

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/straddleboard.png");

    private static final ModelStraddleboard BOARD_MODEL = new ModelStraddleboard();

    public RenderStraddleboard(EntityRendererProvider.Context renderManager) {
        super(renderManager);
    }

    public ResourceLocation getTextureLocation(EntityStraddleboard entity) {
        return TEXTURE;
    }

    public void render(EntityStraddleboard entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(new Quaternionf().rotateY((float) Math.PI));
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) + 180.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.pushPose();
        boolean lava = entityIn.m_20160_();
        float f2 = entityIn.getRockingAngle(partialTicks);
        if (!Mth.equal(f2, 0.0F)) {
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(entityIn.getRockingAngle(partialTicks)));
        }
        int k = entityIn.getColor();
        float r = (float) (k >> 16 & 0xFF) / 255.0F;
        float g = (float) (k >> 8 & 0xFF) / 255.0F;
        float b = (float) (k & 0xFF) / 255.0F;
        float boardRot = entityIn.prevBoardRot + partialTicks * (entityIn.getBoardRot() - entityIn.prevBoardRot);
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(boardRot));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        matrixStackIn.translate(0.0F, -1.5F - Math.abs(boardRot * 0.007F) - (lava ? 0.0F : 0.25F), 0.0F);
        BOARD_MODEL.animateBoard(entityIn, (float) entityIn.f_19797_ + partialTicks);
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_OVERLAY));
        BOARD_MODEL.m_7695_(matrixStackIn, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        BOARD_MODEL.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
    }
}