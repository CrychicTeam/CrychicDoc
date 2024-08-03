package io.redspace.ironsspellbooks.entity.spells.black_hole;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.entity.spells.icicle.IcicleRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BlackHoleRenderer extends EntityRenderer<BlackHole> {

    private static final ResourceLocation CENTER_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/black_hole.png");

    private static final ResourceLocation BEAM_TEXTURE = IronsSpellbooks.id("textures/entity/black_hole/beam.png");

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    public BlackHoleRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    public void render(BlackHole entity, float pEntityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int pPackedLight) {
        poseStack.pushPose();
        poseStack.translate(0.0, entity.m_20191_().getYsize() / 2.0, 0.0);
        float entityScale = entity.m_20205_() * 0.025F;
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        poseStack.scale(0.5F * entityScale, 0.5F * entityScale, 0.5F * entityScale);
        poseStack.mulPose(this.f_114476_.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        poseStack.translate(5.0F, 0.0F, 0.0F);
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityTranslucent(CENTER_TEXTURE));
        consumer.vertex(poseMatrix, 0.0F, -8.0F, -8.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, 0.0F, 8.0F, -8.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, 0.0F, 8.0F, 8.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, 0.0F, -8.0F, 8.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        poseStack.popPose();
        poseStack.pushPose();
        poseStack.translate(0.0, entity.m_20191_().getYsize() / 2.0, 0.0);
        float animationProgress = ((float) entity.f_19797_ + partialTicks) / 200.0F;
        float fadeProgress = 0.5F;
        RandomSource randomSource = RandomSource.create(432L);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.energySwirl(BEAM_TEXTURE, 0.0F, 0.0F));
        float segments = Math.min(animationProgress, 0.8F);
        for (int i = 0; (float) i < (segments + segments * segments) / 2.0F * 60.0F; i++) {
            poseStack.mulPose(Axis.XP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.XP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(randomSource.nextFloat() * 360.0F));
            poseStack.mulPose(Axis.ZP.rotationDegrees(randomSource.nextFloat() * 360.0F + animationProgress * 90.0F));
            float size1 = (randomSource.nextFloat() * 10.0F + 5.0F + fadeProgress * 5.0F) * entityScale * 0.4F;
            Matrix4f matrix = poseStack.last().pose();
            Matrix3f normalMatrix2 = poseStack.last().normal();
            int alpha = (int) (255.0F * (1.0F - fadeProgress));
            drawTriangle(vertexConsumer, matrix, normalMatrix2, size1);
        }
        poseStack.popPose();
        super.render(entity, pEntityYaw, partialTicks, poseStack, bufferSource, pPackedLight);
    }

    public ResourceLocation getTextureLocation(BlackHole pEntity) {
        return IcicleRenderer.TEXTURE;
    }

    private static void vertex01(VertexConsumer vertexConsumer0, Matrix4f matrixF1, int int2) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(255, 255, 255, int2).endVertex();
    }

    private static void vertex2(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3) {
        vertexConsumer0.vertex(matrixF1, -HALF_SQRT_3 * float3, float2, -0.5F * float3).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3) {
        vertexConsumer0.vertex(matrixF1, HALF_SQRT_3 * float3, float2, -0.5F * float3).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3) {
        vertexConsumer0.vertex(matrixF1, 0.0F, float2, 1.0F * float3).color(255, 0, 255, 0).endVertex();
    }

    private static void drawTriangle(VertexConsumer consumer, Matrix4f poseMatrix, Matrix3f normalMatrix, float size) {
        consumer.vertex(poseMatrix, 0.0F, 0.0F, 0.0F).color(255, 0, 255, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, 0.0F, 3.0F * size, -1.0F * size).color(0, 0, 0, 0).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, 0.0F, 3.0F * size, 1.0F * size).color(0, 0, 0, 0).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, 0.0F, 0.0F, 0.0F).color(255, 0, 255, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }
}