package com.github.alexmodguy.alexscaves.client.render.blockentity;

import com.github.alexmodguy.alexscaves.client.model.SirenLightModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.block.SirenLightBlock;
import com.github.alexmodguy.alexscaves.server.block.blockentity.SirenLightBlockEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SirenLightBlockRenderer<T extends SirenLightBlockEntity> implements BlockEntityRenderer<T> {

    private static final SirenLightModel MODEL = new SirenLightModel();

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/entity/siren_light.png");

    private static final ResourceLocation COLOR_TEXTURE = new ResourceLocation("alexscaves", "textures/entity/siren_light_color.png");

    public SirenLightBlockRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
    }

    public void render(T light, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        BlockState state = light.m_58900_();
        Direction dir = (Direction) state.m_61143_(SirenLightBlock.FACING);
        if (dir == Direction.UP) {
            poseStack.translate(0.5F, 1.5F, 0.5F);
        } else if (dir == Direction.DOWN) {
            poseStack.translate(0.5F, -0.5F, 0.5F);
        } else if (dir == Direction.NORTH) {
            poseStack.translate(0.5, 0.5, -0.5);
        } else if (dir == Direction.EAST) {
            poseStack.translate(1.5F, 0.5F, 0.5F);
        } else if (dir == Direction.SOUTH) {
            poseStack.translate(0.5, 0.5, 1.5);
        } else if (dir == Direction.WEST) {
            poseStack.translate(-0.5F, 0.5F, 0.5F);
        }
        poseStack.mulPose(dir.getOpposite().getRotation());
        int color = light.getColor();
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        float rotation = light.getSirenRotation(partialTicks);
        MODEL.setupAnim(null, rotation, 0.0F, 0.0F, 0.0F, 0.0F);
        MODEL.m_7695_(poseStack, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
        float f = light.getOnProgress(partialTicks);
        if (f > 0.0F) {
            float length = f * 1.25F;
            float width = f * f * 0.5F;
            poseStack.pushPose();
            poseStack.translate(0.0F, 1.125F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation + 90.0F));
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f1 = posestack$pose.pose();
            Matrix3f matrix3f1 = posestack$pose.normal();
            VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getNucleeperLights());
            shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F, r, g, b);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F, r, g, b);
            shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F, r, g, b);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F, r, g, b);
            Matrix4f matrix4f2 = posestack$pose.pose();
            Matrix3f matrix3f2 = posestack$pose.normal();
            poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
            shineOriginVertex(lightConsumer, matrix4f2, matrix3f2, 0.0F, 0.0F, r, g, b);
            shineLeftCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F, r, g, b);
            shineRightCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F, r, g, b);
            shineLeftCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F, r, g, b);
            poseStack.popPose();
            poseStack.popPose();
        }
        MODEL.m_7695_(poseStack, bufferIn.getBuffer(RenderType.entityTranslucent(COLOR_TEXTURE)), state.m_61143_(SirenLightBlock.POWERED) ? 240 : combinedLightIn, combinedOverlayIn, r, g, b, 1.0F);
        poseStack.popPose();
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset, float r, float g, float b) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(r, g, b, 1.0F).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset, float r, float g, float b) {
        vertexConsumer0.vertex(matrixF1, -ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(r, g, b, 0.0F).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset, float r, float g, float b) {
        vertexConsumer0.vertex(matrixF1, ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(r, g, b, 0.0F).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    @Override
    public int getViewDistance() {
        return 128;
    }
}