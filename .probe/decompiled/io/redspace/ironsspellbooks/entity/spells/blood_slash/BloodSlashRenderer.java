package io.redspace.ironsspellbooks.entity.spells.blood_slash;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BloodSlashRenderer extends EntityRenderer<BloodSlashProjectile> {

    private static ResourceLocation TEXTURE = IronsSpellbooks.id("textures/entity/blood_slash/blood_slash_large.png");

    private static ResourceLocation[] TEXTURES = new ResourceLocation[] { new ResourceLocation("textures/particle/sweep_0.png"), new ResourceLocation("textures/particle/sweep_1.png"), new ResourceLocation("textures/particle/sweep_2.png"), new ResourceLocation("textures/particle/sweep_3.png"), new ResourceLocation("textures/particle/sweep_4.png"), new ResourceLocation("textures/particle/sweep_5.png"), new ResourceLocation("textures/particle/sweep_6.png"), new ResourceLocation("textures/particle/sweep_7.png") };

    public BloodSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(BloodSlashProjectile entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.f_19859_, entity.m_146908_())));
        poseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(partialTicks, entity.f_19860_, entity.m_146909_())));
        entity.animationTime++;
        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (entity.animationSeed % 30 - 15) * (float) Math.sin((double) entity.animationTime * 0.015)));
        float oldWith = (float) entity.oldBB.getXsize();
        float width = entity.m_20205_();
        width = oldWith + (width - oldWith) * Math.min(partialTicks, 1.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(-15.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-10.0F));
        this.drawSlash(pose, entity, bufferSource, light, width, 4);
        poseStack.mulPose(Axis.YP.rotationDegrees(30.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(20.0F));
        this.drawSlash(pose, entity, bufferSource, light, width, 0);
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(PoseStack.Pose pose, BloodSlashProjectile entity, MultiBufferSource bufferSource, int light, float width, int offset) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity, offset)));
        float halfWidth = width * 0.5F;
        consumer.vertex(poseMatrix, -halfWidth, -0.1F, -halfWidth).color(90, 0, 10, 255).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -0.1F, -halfWidth).color(90, 0, 10, 255).uv(1.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, halfWidth, -0.1F, halfWidth).color(90, 0, 10, 255).uv(1.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, -0.1F, halfWidth).color(90, 0, 10, 255).uv(0.0F, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(BloodSlashProjectile entity) {
        int frame = entity.animationTime / 4 % TEXTURES.length;
        return TEXTURES[frame];
    }

    private ResourceLocation getTextureLocation(BloodSlashProjectile entity, int offset) {
        int frame = (entity.animationTime / 6 + offset) % TEXTURES.length;
        return TEXTURES[frame];
    }
}