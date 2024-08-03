package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.WaterBoltModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.item.WaterBoltEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class WaterBoltRenderer extends EntityRenderer<WaterBoltEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/entity/deep_one/water_bolt.png");

    private static final ResourceLocation OVERLAY_TEXTURE = new ResourceLocation("alexscaves", "textures/entity/deep_one/water_bolt_overlay.png");

    private static final WaterBoltModel MODEL = new WaterBoltModel();

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/trail.png");

    public WaterBoltRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(WaterBoltEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        int waterColorAt = ((Biome) entityIn.m_9236_().m_204166_(entityIn.m_20183_()).get()).getWaterColor();
        float colorR = (float) (waterColorAt >> 16 & 0xFF) / 255.0F;
        float colorG = (float) (waterColorAt >> 8 & 0xFF) / 255.0F;
        float colorB = (float) (waterColorAt & 0xFF) / 255.0F;
        poseStack.pushPose();
        poseStack.translate(0.0, 0.25, 0.0);
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 180.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        MODEL.setupAnim(entityIn, 0.0F, 0.0F, (float) entityIn.f_19797_ + partialTicks, 0.0F, 0.0F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getBubbledNoCull(TEXTURE));
        MODEL.m_7695_(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, colorR, colorG, colorB, 1.0F);
        VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ACRenderTypes.getBubbledNoCull(OVERLAY_TEXTURE));
        MODEL.m_7695_(poseStack, ivertexbuilder2, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        if (entityIn.hasTrail()) {
            double x = Mth.lerp((double) partialTicks, entityIn.f_19790_, entityIn.m_20185_());
            double y = Mth.lerp((double) partialTicks, entityIn.f_19791_, entityIn.m_20186_());
            double z = Mth.lerp((double) partialTicks, entityIn.f_19792_, entityIn.m_20189_());
            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);
            this.renderTrail(entityIn, partialTicks, poseStack, bufferIn, colorR, colorG, colorB, 0.6F, packedLightIn);
            poseStack.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    private void renderTrail(WaterBoltEntity entityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn) {
        int samples = 0;
        int sampleSize = 10;
        float trailHeight = 0.5F;
        float trailZRot = 0.0F;
        Vec3 topAngleVec = new Vec3(0.0, (double) trailHeight, 0.0).zRot(trailZRot);
        Vec3 bottomAngleVec = new Vec3(0.0, (double) (-trailHeight), 0.0).zRot(trailZRot);
        Vec3 drawFrom = entityIn.getTrailPosition(0, partialTicks);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(ACRenderTypes.getBubbledNoCull(TRAIL_TEXTURE));
        while (samples < sampleSize) {
            Vec3 sample = entityIn.getTrailPosition(samples + 2, partialTicks);
            float u1 = (float) samples / (float) sampleSize;
            float u2 = u1 + 1.0F / (float) sampleSize;
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertexconsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomAngleVec.x, (float) drawFrom.y + (float) bottomAngleVec.y, (float) drawFrom.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) sample.x + (float) bottomAngleVec.x, (float) sample.y + (float) bottomAngleVec.y, (float) sample.z + (float) bottomAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u2, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) sample.x + (float) topAngleVec.x, (float) sample.y + (float) topAngleVec.y, (float) sample.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u2, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) drawFrom.x + (float) topAngleVec.x, (float) drawFrom.y + (float) topAngleVec.y, (float) drawFrom.z + (float) topAngleVec.z).color(trailR, trailG, trailB, trailA).uv(u1, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            samples++;
            drawFrom = sample;
        }
    }

    public ResourceLocation getTextureLocation(WaterBoltEntity entity) {
        return TEXTURE;
    }
}