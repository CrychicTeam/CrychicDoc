package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelFarseer;
import com.github.alexthe666.alexsmobs.entity.EntityFarseer;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event.Result;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RenderFarseer extends MobRenderer<EntityFarseer, ModelFarseer> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/farseer/farseer.png");

    private static final ResourceLocation TEXTURE_ANGRY = new ResourceLocation("alexsmobs:textures/entity/farseer/farseer_angry.png");

    private static final ResourceLocation TEXTURE_CLAWS = new ResourceLocation("alexsmobs:textures/entity/farseer/farseer_claws.png");

    private static final ResourceLocation TEXTURE_EYE = new ResourceLocation("alexsmobs:textures/entity/farseer/farseer_eye.png");

    private static final ResourceLocation TEXTURE_SCARS = new ResourceLocation("alexsmobs:textures/entity/farseer/farseer_scars.png");

    private static final ResourceLocation[] PORTAL_TEXTURES = new ResourceLocation[] { new ResourceLocation("alexsmobs:textures/entity/farseer/portal_0.png"), new ResourceLocation("alexsmobs:textures/entity/farseer/portal_1.png"), new ResourceLocation("alexsmobs:textures/entity/farseer/portal_2.png"), new ResourceLocation("alexsmobs:textures/entity/farseer/portal_3.png") };

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    private static final ModelFarseer EYE_MODEL = new ModelFarseer(0.1F);

    private static final ModelFarseer SCARS_MODEL = new ModelFarseer(0.05F);

    private static final ModelFarseer AFTERIMAGE_MODEL = new ModelFarseer(0.05F);

    public RenderFarseer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelFarseer(0.0F), 0.9F);
        this.m_115326_(new RenderFarseer.LayerOverlay());
    }

    public boolean shouldRender(EntityFarseer livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (livingEntityIn.hasLaser()) {
                LivingEntity livingentity = livingEntityIn.getLaserTarget();
                if (livingentity != null) {
                    Vec3 vector3d = this.getPosition(livingentity, (double) livingentity.m_20206_() * 0.5, 1.0F);
                    Vec3 vector3d1 = this.getPosition(livingEntityIn, (double) livingEntityIn.m_20192_(), 1.0F);
                    return camera.isVisible(new AABB(vector3d1.x, vector3d1.y, vector3d1.z, vector3d.x, vector3d.y, vector3d.z));
                }
            }
            return false;
        }
    }

    private Vec3 getPosition(LivingEntity entityLivingBaseIn, double p_177110_2_, float p_177110_4_) {
        double d0 = Mth.lerp((double) p_177110_4_, entityLivingBaseIn.f_19790_, entityLivingBaseIn.m_20185_());
        double d1 = Mth.lerp((double) p_177110_4_, entityLivingBaseIn.f_19791_, entityLivingBaseIn.m_20186_()) + p_177110_2_;
        double d2 = Mth.lerp((double) p_177110_4_, entityLivingBaseIn.f_19792_, entityLivingBaseIn.m_20189_());
        return new Vec3(d0, d1, d2);
    }

    public void render(EntityFarseer entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (!MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre<>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn))) {
            LivingEntity laserTarget = entityIn.getLaserTarget();
            float faceCameraAmount = entityIn.getFacingCameraAmount(partialTicks);
            Quaternionf camera = this.f_114476_.cameraOrientation();
            matrixStackIn.pushPose();
            ((ModelFarseer) this.f_115290_).f_102608_ = this.m_115342_(entityIn, partialTicks);
            boolean shouldSit = entityIn.m_20159_() && entityIn.m_20202_() != null && entityIn.m_20202_().shouldRiderSit();
            ((ModelFarseer) this.f_115290_).f_102609_ = shouldSit;
            ((ModelFarseer) this.f_115290_).f_102610_ = entityIn.m_6162_();
            float f = Mth.rotLerp(partialTicks, entityIn.f_20884_, entityIn.f_20883_);
            float f1 = Mth.rotLerp(partialTicks, entityIn.f_20886_, entityIn.f_20885_);
            float f2 = f1 - f;
            if (shouldSit && entityIn.m_20202_() instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity) entityIn.m_20202_();
                f = Mth.rotLerp(partialTicks, livingentity.yBodyRotO, livingentity.yBodyRot);
                f2 = f1 - f;
                float f3 = Mth.wrapDegrees(f2);
                if (f3 < -85.0F) {
                    f3 = -85.0F;
                }
                if (f3 >= 85.0F) {
                    f3 = 85.0F;
                }
                f = f1 - f3;
                if (f3 * f3 > 2500.0F) {
                    f += f3 * 0.2F;
                }
                f2 = f1 - f;
            }
            float f6 = Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_());
            if (entityIn.m_20089_() == Pose.SLEEPING) {
                Direction direction = entityIn.m_21259_();
                if (direction != null) {
                    float f4 = entityIn.m_20236_(Pose.STANDING) - 0.1F;
                    matrixStackIn.translate((double) ((float) (-direction.getStepX()) * f4), 0.0, (double) ((float) (-direction.getStepZ()) * f4));
                }
            }
            float f7 = this.m_6930_(entityIn, partialTicks);
            if (faceCameraAmount != 0.0F) {
                matrixStackIn.mulPose(camera);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
            }
            this.setupRotations(entityIn, matrixStackIn, f7, f, partialTicks);
            matrixStackIn.scale(-1.0F, -1.0F, 1.0F);
            this.m_7546_(entityIn, matrixStackIn, partialTicks);
            matrixStackIn.translate(0.0, -1.501F, 0.0);
            float f8 = 0.0F;
            float f5 = 0.0F;
            if (!shouldSit && entityIn.m_6084_()) {
                f8 = entityIn.f_267362_.position(partialTicks);
                f5 = entityIn.f_267362_.position() - entityIn.f_267362_.speed() * (1.0F - partialTicks);
                if (entityIn.m_6162_()) {
                    f5 *= 3.0F;
                }
                if (f8 > 1.0F) {
                    f8 = 1.0F;
                }
            }
            ((ModelFarseer) this.f_115290_).m_6839_(entityIn, f5, f8, partialTicks);
            ((ModelFarseer) this.f_115290_).setupAnim(entityIn, f5, f8, f7, f2, f6);
            Minecraft minecraft = Minecraft.getInstance();
            boolean flag = this.m_5933_(entityIn);
            boolean flag1 = !flag && !entityIn.m_20177_(minecraft.player);
            boolean flag2 = minecraft.shouldEntityAppearGlowing(entityIn);
            RenderType rendertype = this.getRenderType(entityIn, flag, flag1, flag2);
            EYE_MODEL.setupAnim(entityIn, f5, f8, f7, f2, f6);
            SCARS_MODEL.setupAnim(entityIn, f5, f8, f7, f2, f6);
            AFTERIMAGE_MODEL.setupAnim(entityIn, f5, f8, f7, f2, f6);
            if (rendertype != null) {
                float portalLevel = entityIn.getFarseerOpacity(partialTicks);
                this.f_114477_ = 0.9F * portalLevel;
                int i = m_115338_(entityIn, this.m_6931_(entityIn, partialTicks));
                this.renderFarseerModel(matrixStackIn, bufferIn, rendertype, partialTicks, packedLightIn, i, flag1 ? 0.15F : Mth.clamp(portalLevel, 0.0F, 1.0F), entityIn);
            }
            if (!entityIn.m_5833_()) {
                for (RenderLayer layerrenderer : this.f_115291_) {
                    layerrenderer.render(matrixStackIn, bufferIn, packedLightIn, entityIn, f5, f8, partialTicks, f7, f2, f6);
                }
            }
            matrixStackIn.popPose();
            RenderNameTagEvent renderNameplateEvent = new RenderNameTagEvent(entityIn, entityIn.m_5446_(), this, matrixStackIn, bufferIn, packedLightIn, partialTicks);
            MinecraftForge.EVENT_BUS.post(renderNameplateEvent);
            if (renderNameplateEvent.getResult() != Result.DENY && (renderNameplateEvent.getResult() == Result.ALLOW || this.m_6512_(entityIn))) {
                this.m_7649_(entityIn, renderNameplateEvent.getContent(), matrixStackIn, bufferIn, packedLightIn);
            }
            MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post<>(entityIn, this, partialTicks, matrixStackIn, bufferIn, packedLightIn));
            if (entityIn.getAnimation() == EntityFarseer.ANIMATION_EMERGE) {
                matrixStackIn.pushPose();
                matrixStackIn.scale(3.0F, 3.0F, 3.0F);
                matrixStackIn.mulPose(camera);
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
                PoseStack.Pose posestack$pose = matrixStackIn.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();
                int portalTexture = Mth.clamp(entityIn.getPortalFrame(), 0, PORTAL_TEXTURES.length - 1);
                VertexConsumer portalStatic = AMRenderTypes.createMergedVertexConsumer(bufferIn.getBuffer(AMRenderTypes.STATIC_PORTAL), bufferIn.getBuffer(RenderType.entityTranslucent(PORTAL_TEXTURES[portalTexture])));
                float portalAlpha = entityIn.getPortalOpacity(partialTicks);
                portalVertex(portalStatic, matrix4f, matrix3f, packedLightIn, 0.0F, 0, 0, 1, portalAlpha);
                portalVertex(portalStatic, matrix4f, matrix3f, packedLightIn, 1.0F, 0, 1, 1, portalAlpha);
                portalVertex(portalStatic, matrix4f, matrix3f, packedLightIn, 1.0F, 1, 1, 0, portalAlpha);
                portalVertex(portalStatic, matrix4f, matrix3f, packedLightIn, 0.0F, 1, 0, 0, portalAlpha);
                matrixStackIn.popPose();
            }
            if (entityIn.hasLaser() && laserTarget != null && !laserTarget.m_213877_()) {
                float laserProgress = (entityIn.prevLaserLvl + ((float) entityIn.getLaserAttackLvl() - entityIn.prevLaserLvl) * partialTicks) / 10.0F;
                float laserHeight = entityIn.m_20192_();
                Vec3 angryShake = Vec3.ZERO;
                double d0 = Mth.lerp((double) partialTicks, laserTarget.f_19854_, laserTarget.m_20185_()) - Mth.lerp((double) partialTicks, entityIn.f_19854_, entityIn.m_20185_()) - angryShake.x;
                double d1 = Mth.lerp((double) partialTicks, laserTarget.f_19855_, laserTarget.m_20186_()) + (double) laserTarget.m_20192_() - Mth.lerp((double) partialTicks, entityIn.f_19855_, entityIn.m_20186_()) - angryShake.y - (double) laserHeight;
                double d2 = Mth.lerp((double) partialTicks, laserTarget.f_19856_, laserTarget.m_20189_()) - Mth.lerp((double) partialTicks, entityIn.f_19856_, entityIn.m_20189_()) - angryShake.z;
                double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                float laserY = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                float laserX = (float) (-(Mth.atan2(d1, d4) * 180.0F / (float) Math.PI));
                VertexConsumer beamStatic = bufferIn.getBuffer(AMRenderTypes.getFarseerBeam());
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.0F, laserHeight, 0.0F);
                matrixStackIn.mulPose(Axis.YN.rotationDegrees(laserY));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(laserX));
                matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
                float length = entityIn.getLaserDistance() * laserProgress;
                float width = (1.5F - laserProgress) * 2.0F;
                float speed = 1.0F + laserProgress * laserProgress * 5.0F;
                PoseStack.Pose posestack$pose = matrixStackIn.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();
                int j = 255;
                long systemTime = Util.getMillis() * 7L;
                float u = (float) (systemTime % 30000L) / 30000.0F;
                float v = (float) Math.floor((double) ((float) (systemTime % 3000L) / 3000.0F * 4.0F)) * 0.25F + (float) Math.sin((double) ((float) systemTime / 30000.0F)) * 0.05F + (float) (systemTime % 20000L) / 20000.0F * speed;
                laserOriginVertex(beamStatic, matrix4f, matrix3f, j, u, v);
                laserLeftCornerVertex(beamStatic, matrix4f, matrix3f, length, width, u, v);
                laserRightCornerVertex(beamStatic, matrix4f, matrix3f, length, width, u, v);
                laserLeftCornerVertex(beamStatic, matrix4f, matrix3f, length, width, u, v);
                matrixStackIn.popPose();
            }
        }
    }

    private void renderFarseerModel(PoseStack matrixStackIn, MultiBufferSource source, RenderType defRenderType, float partialTicks, int packedLightIn, int overlayColors, float alphaIn, EntityFarseer entityIn) {
        if (entityIn.hasLaser()) {
            VertexConsumer staticyInsides = AMRenderTypes.createMergedVertexConsumer(source.getBuffer(AMRenderTypes.STATIC_ENTITY), source.getBuffer(RenderType.entityTranslucent(TEXTURE_EYE)));
            EYE_MODEL.m_7695_(matrixStackIn, staticyInsides, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
        float hurt = (float) Math.max(entityIn.f_20916_, entityIn.f_20919_);
        float defAlpha = alphaIn * 0.2F;
        float afterimageSpeed = 0.3F;
        if (hurt > 0.0F) {
            afterimageSpeed = Math.min(hurt / 20.0F, 1.0F) + 0.3F;
            VertexConsumer staticyScars = AMRenderTypes.createMergedVertexConsumer(source.getBuffer(AMRenderTypes.STATIC_ENTITY), source.getBuffer(RenderType.entityTranslucent(TEXTURE_SCARS)));
            SCARS_MODEL.m_7695_(matrixStackIn, staticyScars, packedLightIn, overlayColors, 1.0F, 1.0F, 1.0F, 0.3F);
        }
        ((ModelFarseer) this.f_115290_).m_7695_(matrixStackIn, source.getBuffer(defRenderType), packedLightIn, overlayColors, 1.0F, 1.0F, 1.0F, alphaIn);
        matrixStackIn.pushPose();
        matrixStackIn.popPose();
        AFTERIMAGE_MODEL.eye.showModel = false;
        RenderType afterimage = RenderType.entityTranslucentEmissive(this.getTextureLocation(entityIn));
        Vec3 colorOffset = entityIn.getLatencyOffsetVec(10, partialTicks).scale(-0.2F).add(entityIn.angryShakeVec.scale(0.3F));
        Vec3 redOffset = colorOffset.add(entityIn.calculateAfterimagePos(partialTicks, false, afterimageSpeed));
        Vec3 blueOffset = colorOffset.add(entityIn.calculateAfterimagePos(partialTicks, true, afterimageSpeed));
        float scale = (float) Mth.clamp(colorOffset.length() * 0.1F, 0.0, 1.0);
        float angryProgress = entityIn.prevAngryProgress + (entityIn.angryProgress - entityIn.prevAngryProgress) * partialTicks;
        float afterimageAlpha1 = defAlpha * Math.max(((float) Math.sin((double) (((float) entityIn.f_19797_ + partialTicks) * 0.2F)) + 1.0F) * 0.3F, angryProgress * 0.2F);
        float afterimageAlpha2 = defAlpha * Math.max(((float) Math.cos((double) (((float) entityIn.f_19797_ + partialTicks) * 0.2F)) + 1.0F) * 0.3F, angryProgress * 0.2F);
        matrixStackIn.pushPose();
        matrixStackIn.scale(scale + 1.0F, scale + 1.0F, scale + 1.0F);
        matrixStackIn.pushPose();
        matrixStackIn.translate(redOffset.x, redOffset.y, redOffset.z);
        AFTERIMAGE_MODEL.m_7695_(matrixStackIn, source.getBuffer(afterimage), 240, overlayColors, 1.0F, 0.0F, 0.0F, afterimageAlpha1);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        matrixStackIn.translate(blueOffset.x, blueOffset.y, blueOffset.z);
        AFTERIMAGE_MODEL.m_7695_(matrixStackIn, source.getBuffer(afterimage), 240, overlayColors, 0.0F, 0.0F, 1.0F, afterimageAlpha2);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
        AFTERIMAGE_MODEL.eye.showModel = true;
    }

    private static void laserOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(255, 255, 255, 255).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void laserLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -HALF_SQRT_3 * float4, float3, 0.0F).color(255, 255, 255, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void laserRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, HALF_SQRT_3 * float4, float3, 0.0F).color(255, 255, 255, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void portalVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, int int5, int int6, int int7, float alpha) {
        vertexConsumer0.vertex(matrixF1, float4 - 0.5F, (float) int5 - 0.25F, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    protected void setupRotations(EntityFarseer farseer, PoseStack matrixStackIn, float f1, float f2, float f3) {
        float invCameraAmount = 1.0F - farseer.getFacingCameraAmount(Minecraft.getInstance().getFrameTime());
        if (this.m_5936_(farseer)) {
            f2 += (float) (Math.cos((double) farseer.f_19797_ * 3.25) * Math.PI * 0.4F);
        }
        if (!farseer.m_217003_(Pose.SLEEPING)) {
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F - f2 * invCameraAmount));
        }
        if (farseer.f_20919_ > 0) {
            float f = ((float) farseer.f_20919_ + f3 - 1.0F) / 20.0F * 1.6F;
            f = Mth.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(f * this.m_6441_(farseer) * invCameraAmount));
        } else if (m_194453_(farseer)) {
            matrixStackIn.translate(0.0, (double) (farseer.m_20206_() + 0.1F), 0.0);
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    @Nullable
    protected RenderType getRenderType(EntityFarseer farseer, boolean normal, boolean invis, boolean outline) {
        ResourceLocation resourcelocation = this.getTextureLocation(farseer);
        if (invis || farseer.getAnimation() == EntityFarseer.ANIMATION_EMERGE) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (normal) {
            return ((ModelFarseer) this.f_115290_).m_103119_(resourcelocation);
        } else {
            return outline ? RenderType.outline(resourcelocation) : null;
        }
    }

    public ResourceLocation getTextureLocation(EntityFarseer entity) {
        return entity.isAngry() ? TEXTURE_ANGRY : TEXTURE;
    }

    class LayerOverlay extends RenderLayer<EntityFarseer, ModelFarseer> {

        public LayerOverlay() {
            super(RenderFarseer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityFarseer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (entitylivingbaseIn.getAnimation() == EntityFarseer.ANIMATION_EMERGE) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(RenderFarseer.TEXTURE_CLAWS));
                ((ModelFarseer) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}