package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.entity.EntityMudBall;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderMudBall extends EntityRenderer<EntityMudBall> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/mud_ball.png");

    public RenderMudBall(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
    }

    public void render(EntityMudBall entityMudBall, float f, float f2, PoseStack poseStack0, MultiBufferSource multiBufferSource1, int int2) {
        poseStack0.pushPose();
        poseStack0.scale(0.7F, 0.7F, 0.7F);
        poseStack0.mulPose(this.f_114476_.cameraOrientation());
        poseStack0.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose $$6 = poseStack0.last();
        Matrix4f $$7 = $$6.pose();
        Matrix3f $$8 = $$6.normal();
        VertexConsumer $$9 = multiBufferSource1.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        vertex($$9, $$7, $$8, int2, 0.0F, 0, 0, 1);
        vertex($$9, $$7, $$8, int2, 1.0F, 0, 1, 1);
        vertex($$9, $$7, $$8, int2, 1.0F, 1, 1, 0);
        vertex($$9, $$7, $$8, int2, 0.0F, 1, 0, 0);
        poseStack0.popPose();
        super.render(entityMudBall, f, f2, poseStack0, multiBufferSource1, int2);
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, int int5, int int6, int int7) {
        vertexConsumer0.vertex(matrixF1, float4 - 0.5F, (float) int5 - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(EntityMudBall mudball) {
        return TEXTURE;
    }
}