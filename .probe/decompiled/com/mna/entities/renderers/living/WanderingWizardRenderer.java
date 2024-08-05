package com.mna.entities.renderers.living;

import com.mna.entities.models.WanderingWizardModel;
import com.mna.entities.renderers.MAGeckoRenderer;
import com.mna.entities.utility.WanderingWizard;
import com.mna.tools.render.MARenderTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.RenderUtils;

public class WanderingWizardRenderer extends MAGeckoRenderer<WanderingWizard> {

    private final Minecraft mc = Minecraft.getInstance();

    public WanderingWizardRenderer(EntityRendererProvider.Context context) {
        super(context, new WanderingWizardModel());
    }

    public void renderRecursively(PoseStack poseStack, WanderingWizard animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (!bone.getName().equals("scroll") || this.animatable.renderScroll()) {
            if (bone.getName().equals("staff_planted") && this.animatable.renderPlantedStaff()) {
                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);
                RenderUtils.translateToPivotPoint(poseStack, bone);
                RenderUtils.rotateMatrixAroundBone(poseStack, bone);
                RenderUtils.scaleMatrixForBone(poseStack, bone);
                poseStack.translate(0.0F, 0.6F, 0.0F);
                poseStack.scale(0.5F, 0.5F, 0.5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(animatable.staff(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, poseStack, bufferSource, this.mc.level, 0);
                poseStack.popPose();
                bufferSource.getBuffer(renderType);
            }
            if (bone.getName().equals("staff") && !this.animatable.renderPlantedStaff()) {
                poseStack.pushPose();
                RenderUtils.translateMatrixToBone(poseStack, bone);
                RenderUtils.translateToPivotPoint(poseStack, bone);
                RenderUtils.rotateMatrixAroundBone(poseStack, bone);
                RenderUtils.scaleMatrixForBone(poseStack, bone);
                poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
                poseStack.scale(0.5F, 0.5F, 0.5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(animatable.staff(), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, packedLight, packedOverlay, poseStack, bufferSource, this.mc.level, 0);
                poseStack.popPose();
                bufferSource.getBuffer(renderType);
            }
            super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        }
    }

    public void render(WanderingWizard entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.f_19797_ < 60) {
            RenderSystem.setShaderTexture(0, MARenderTypes.PORTAL_TEXTURE);
            Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
            Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
            float spawnPct = entityIn.f_19797_ < 20 ? (float) entityIn.f_19797_ / 20.0F : (entityIn.f_19797_ < 40 ? 1.0F : (float) (20 - (entityIn.f_19797_ - 40)) / 20.0F);
            float scaleFactor = 4.0F * spawnPct;
            float portalSpinDegrees = (float) (entityIn.f_19797_ * 3 % 360);
            float verticalOffset = 1.5F;
            int[] colors = new int[] { 68, 13, 112 };
            VertexConsumer vertexBuilder = bufferIn.getBuffer(MARenderTypes.PORTAL_RENDER);
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0F, verticalOffset, 0.0F);
            matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
            matrixStackIn.mulPose(portalRotation);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(portalSpinDegrees));
            matrixStackIn.translate(0.0F, -0.25F, 0.0F);
            PoseStack.Pose matrixstack$entry = matrixStackIn.last();
            Matrix4f renderMatrix = matrixstack$entry.pose();
            Matrix3f normalMatrix = matrixstack$entry.normal();
            float nrmV = (float) Math.cos((double) portalSpinDegrees * Math.PI / 180.0);
            float nrmH = (float) Math.cos((double) (portalSpinDegrees - 90.0F) * Math.PI / 180.0);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, colors);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, colors);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, colors);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, colors);
            matrixStackIn.popPose();
            colors = new int[] { 82, 167, 217 };
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0F, verticalOffset, 0.0F);
            matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
            matrixStackIn.mulPose(portalRotation);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-portalSpinDegrees));
            matrixStackIn.translate(0.0F, -0.25F, 0.001F);
            matrixstack$entry = matrixStackIn.last();
            renderMatrix = matrixstack$entry.pose();
            normalMatrix = matrixstack$entry.normal();
            nrmH = (float) Math.cos((double) (portalSpinDegrees + 90.0F) * Math.PI / 180.0);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, colors);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, colors);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, colors);
            addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, colors);
            matrixStackIn.popPose();
            RenderSystem.setShaderTexture(0, this.m_5478_(entityIn));
        }
        if (entityIn.f_19797_ >= 20) {
            super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        }
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, int[] rgb) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(rgb[0], rgb[1], rgb[2], 230).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }
}