package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelMimicOctopus;
import com.github.alexthe666.alexsmobs.entity.EntityMimicOctopus;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderMimicOctopus extends MobRenderer<EntityMimicOctopus, ModelMimicOctopus> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus.png");

    private static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus_overlay.png");

    private static final ResourceLocation TEXTURE_CREEPER = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus_creeper.png");

    private static final ResourceLocation TEXTURE_GUARDIAN = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus_guardian.png");

    private static final ResourceLocation TEXTURE_PUFFERFISH = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus_pufferfish.png");

    private static final ResourceLocation TEXTURE_MIMICUBE = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus_mimicube.png");

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("alexsmobs:textures/entity/mimic_octopus_eyes.png");

    private static final ResourceLocation GUARDIAN_BEAM_TEXTURE = new ResourceLocation("textures/entity/guardian_beam.png");

    private static final RenderType BEAM_RENDER_TYPE = RenderType.entityCutoutNoCull(GUARDIAN_BEAM_TEXTURE);

    public RenderMimicOctopus(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelMimicOctopus(), 0.4F);
        this.m_115326_(new RenderMimicOctopus.OverlayLayer(this));
    }

    private static void vertex(VertexConsumer p_229108_0_, Matrix4f p_229108_1_, Matrix3f p_229108_2_, float p_229108_3_, float p_229108_4_, float p_229108_5_, int p_229108_6_, int p_229108_7_, int p_229108_8_, float p_229108_9_, float p_229108_10_) {
        p_229108_0_.vertex(p_229108_1_, p_229108_3_, p_229108_4_, p_229108_5_).color(p_229108_6_, p_229108_7_, p_229108_8_, 255).uv(p_229108_9_, p_229108_10_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(p_229108_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public void render(EntityMimicOctopus entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        LivingEntity livingentity = entityIn.getGuardianLaser();
        if (livingentity != null) {
            float f = entityIn.getLaserAttackAnimationScale(partialTicks);
            float f1 = (float) entityIn.m_9236_().getGameTime() + partialTicks;
            float f2 = f1 * 0.5F % 1.0F;
            float f3 = entityIn.m_20192_();
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0, (double) f3, 0.0);
            Vec3 vector3d = this.getPosition(livingentity, (double) livingentity.m_20206_() * 0.5, partialTicks);
            Vec3 vector3d1 = this.getPosition(entityIn, (double) f3, partialTicks);
            Vec3 vector3d2 = vector3d.subtract(vector3d1);
            float f4 = (float) (vector3d2.length() + 1.0);
            vector3d2 = vector3d2.normalize();
            float f5 = (float) Math.acos(vector3d2.y);
            float f6 = (float) Math.atan2(vector3d2.z, vector3d2.x);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(((float) (Math.PI / 2) - f6) * (180.0F / (float) Math.PI)));
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(f5 * (180.0F / (float) Math.PI)));
            int i = 1;
            float f7 = f1 * 0.05F * -1.5F;
            float f8 = f * f;
            int j = 64 + (int) (f8 * 191.0F);
            int k = 32 + (int) (f8 * 191.0F);
            int l = 128 - (int) (f8 * 64.0F);
            float f9 = 0.2F;
            float f10 = 0.282F;
            float f11 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float f12 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 4.0)) * 0.282F;
            float f13 = Mth.cos(f7 + (float) (Math.PI / 4)) * 0.282F;
            float f14 = Mth.sin(f7 + (float) (Math.PI / 4)) * 0.282F;
            float f15 = Mth.cos(f7 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float f16 = Mth.sin(f7 + ((float) Math.PI * 5.0F / 4.0F)) * 0.282F;
            float f17 = Mth.cos(f7 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float f18 = Mth.sin(f7 + ((float) Math.PI * 7.0F / 4.0F)) * 0.282F;
            float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
            float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
            float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
            float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
            float f23 = Mth.cos(f7 + (float) (Math.PI / 2)) * 0.2F;
            float f24 = Mth.sin(f7 + (float) (Math.PI / 2)) * 0.2F;
            float f25 = Mth.cos(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float f26 = Mth.sin(f7 + (float) (Math.PI * 3.0 / 2.0)) * 0.2F;
            float f27 = 0.0F;
            float f28 = 0.4999F;
            float f29 = -1.0F + f2;
            float f30 = f4 * 2.5F + f29;
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(BEAM_RENDER_TYPE);
            PoseStack.Pose matrixstack$entry = matrixStackIn.last();
            Matrix4f matrix4f = matrixstack$entry.pose();
            Matrix3f matrix3f = matrixstack$entry.normal();
            vertex(ivertexbuilder, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
            vertex(ivertexbuilder, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
            vertex(ivertexbuilder, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
            float f31 = 0.0F;
            if (entityIn.f_19797_ % 2 == 0) {
                f31 = 0.5F;
            }
            vertex(ivertexbuilder, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
            vertex(ivertexbuilder, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
            vertex(ivertexbuilder, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
            vertex(ivertexbuilder, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void scale(EntityMimicOctopus octo, PoseStack matrixStackIn, float partialTickTime) {
        matrixStackIn.translate(0.0F, -0.02F, 0.0F);
        matrixStackIn.scale(0.9F * octo.m_6134_(), 0.9F * octo.m_6134_(), 0.9F * octo.m_6134_());
    }

    public boolean shouldRender(EntityMimicOctopus livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            if (livingEntityIn.hasGuardianLaser()) {
                LivingEntity livingentity = livingEntityIn.getGuardianLaser();
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

    public ResourceLocation getTextureLocation(EntityMimicOctopus entity) {
        return TEXTURE;
    }

    static class OverlayLayer extends RenderLayer<EntityMimicOctopus, ModelMimicOctopus> {

        public OverlayLayer(RenderMimicOctopus render) {
            super(render);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource buffer, int packedLightIn, EntityMimicOctopus entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float transProgress = entitylivingbaseIn.prevTransProgress + (entitylivingbaseIn.transProgress - entitylivingbaseIn.prevTransProgress) * partialTicks;
            float colorProgress = (entitylivingbaseIn.prevColorShiftProgress + (entitylivingbaseIn.colorShiftProgress - entitylivingbaseIn.prevColorShiftProgress) * partialTicks) * 0.2F;
            float r = 1.0F;
            float g = 1.0F;
            float b = 1.0F;
            float a = 1.0F;
            float startR = 1.0F;
            float startG = 1.0F;
            float startB = 1.0F;
            float startA = 1.0F;
            float finR = 1.0F;
            float finG = 1.0F;
            float finB = 1.0F;
            float finA = 1.0F;
            if (entitylivingbaseIn.getPrevMimicState() == EntityMimicOctopus.MimicState.OVERLAY) {
                if (entitylivingbaseIn.getPrevMimickedBlock() != null) {
                    int j = OctopusColorRegistry.getBlockColor(entitylivingbaseIn.getPrevMimickedBlock());
                    startR = (float) (j >> 16 & 0xFF) / 255.0F;
                    startG = (float) (j >> 8 & 0xFF) / 255.0F;
                    startB = (float) (j & 0xFF) / 255.0F;
                } else {
                    startA = 0.0F;
                }
            }
            if (entitylivingbaseIn.getMimicState() == EntityMimicOctopus.MimicState.OVERLAY) {
                if (entitylivingbaseIn.getMimickedBlock() != null) {
                    int i = OctopusColorRegistry.getBlockColor(entitylivingbaseIn.getMimickedBlock());
                    finR = (float) (i >> 16 & 0xFF) / 255.0F;
                    finG = (float) (i >> 8 & 0xFF) / 255.0F;
                    finB = (float) (i & 0xFF) / 255.0F;
                } else {
                    finA = 0.0F;
                }
                r = startR + (finR - startR) * colorProgress;
                g = startG + (finG - startG) * colorProgress;
                b = startB + (finB - startB) * colorProgress;
                a = startA + (finA - startA) * colorProgress;
            }
            if (a == 1.0F) {
                a *= 0.9F + 0.1F * (float) Math.sin((double) ((float) entitylivingbaseIn.f_19797_ * 0.1F));
            }
            if (entitylivingbaseIn.getPrevMimicState() != null) {
                float alphaPrev = 1.0F - transProgress * 0.2F;
                VertexConsumer prev = buffer.getBuffer(AMRenderTypes.m_110473_(this.getFor(entitylivingbaseIn.getPrevMimicState())));
                if (entitylivingbaseIn.getPrevMimicState() == entitylivingbaseIn.getMimicState()) {
                    alphaPrev *= a;
                }
                ((ModelMimicOctopus) this.m_117386_()).m_7695_(matrixStackIn, prev, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), r, g, b, alphaPrev);
            }
            float alphaCurrent = transProgress * 0.2F;
            VertexConsumer current = buffer.getBuffer(AMRenderTypes.m_110473_(this.getFor(entitylivingbaseIn.getMimicState())));
            ((ModelMimicOctopus) this.m_117386_()).m_7695_(matrixStackIn, current, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), r, g, b, a * alphaCurrent);
            VertexConsumer eyes = buffer.getBuffer(AMRenderTypes.m_110473_(RenderMimicOctopus.TEXTURE_EYES));
            ((ModelMimicOctopus) this.m_117386_()).m_7695_(matrixStackIn, eyes, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }

        public ResourceLocation getFor(EntityMimicOctopus.MimicState state) {
            if (state == EntityMimicOctopus.MimicState.CREEPER) {
                return RenderMimicOctopus.TEXTURE_CREEPER;
            } else if (state == EntityMimicOctopus.MimicState.GUARDIAN) {
                return RenderMimicOctopus.TEXTURE_GUARDIAN;
            } else if (state == EntityMimicOctopus.MimicState.PUFFERFISH) {
                return RenderMimicOctopus.TEXTURE_PUFFERFISH;
            } else {
                return state == EntityMimicOctopus.MimicState.MIMICUBE ? RenderMimicOctopus.TEXTURE_MIMICUBE : RenderMimicOctopus.TEXTURE_OVERLAY;
            }
        }
    }
}