package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelLaviathan;
import com.github.alexthe666.alexsmobs.entity.EntityLaviathan;
import com.github.alexthe666.alexsmobs.entity.EntityLaviathanPart;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class RenderLaviathan extends MobRenderer<EntityLaviathan, ModelLaviathan> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/laviathan.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexsmobs:textures/entity/laviathan_glow.png");

    private static final ResourceLocation TEXTURE_OBSIDIAN = new ResourceLocation("alexsmobs:textures/entity/laviathan_obsidian.png");

    private static final ResourceLocation TEXTURE_GEAR = new ResourceLocation("alexsmobs:textures/entity/laviathan_gear.png");

    private static final ResourceLocation TEXTURE_HELMET = new ResourceLocation("alexsmobs:textures/entity/laviathan_helmet.png");

    private static final float REINS_COLOR_R = 0.38431373F;

    private static final float REINS_COLOR_G = 0.3019608F;

    private static final float REINS_COLOR_B = 0.20392157F;

    private static final float REINS_COLOR_R2 = 0.22745098F;

    private static final float REINS_COLOR_G2 = 0.15686275F;

    private static final float REINS_COLOR_B2 = 0.13333334F;

    public static boolean renderWithoutShaking = false;

    public RenderLaviathan(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelLaviathan(), 4.0F);
        this.m_115326_(new RenderLaviathan.LayerOverlays(this));
    }

    private static void addVertexPairAlex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3, float float4, int int5, int int6, int int7, int int8, float float9, float float10, float float11, float float12, int int13, boolean boolean14) {
        float f = (float) int13 / 24.0F;
        int i = (int) Mth.lerp(f, (float) int5, (float) int6);
        int j = (int) Mth.lerp(f, (float) int7, (float) int8);
        int k = LightTexture.pack(i, j);
        float f2 = 0.38431373F;
        float f3 = 0.3019608F;
        float f4 = 0.20392157F;
        if (int13 % 2 == (boolean14 ? 1 : 0)) {
            f2 = 0.22745098F;
            f3 = 0.15686275F;
            f4 = 0.13333334F;
        }
        float f5 = float2 * f;
        float f6 = float3 > 0.0F ? float3 * f * f : float3 - float3 * (1.0F - f) * (1.0F - f);
        float f7 = float4 * f;
        vertexConsumer0.vertex(matrixF1, f5 - float11, f6 + float10, f7 + float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        vertexConsumer0.vertex(matrixF1, f5 + float11, f6 + float9 - float10, f7 - float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    public boolean shouldRender(EntityLaviathan livingEntityIn, Frustum camera, double camX, double camY, double camZ) {
        if (super.shouldRender(livingEntityIn, camera, camX, camY, camZ)) {
            return true;
        } else {
            for (EntityLaviathanPart part : livingEntityIn.allParts) {
                if (camera.isVisible(part.m_20191_())) {
                    return true;
                }
            }
            return false;
        }
    }

    public void render(EntityLaviathan mob, float float0, float partialTick, PoseStack ms, MultiBufferSource multiBufferSource1, int int2) {
        super.render(mob, float0, partialTick, ms, multiBufferSource1, int2);
        Entity entity = mob.getControllingPassenger();
        if (entity != null) {
            double d0 = Mth.lerp((double) partialTick, mob.f_19790_, mob.m_20185_());
            double d1 = Mth.lerp((double) partialTick, mob.f_19791_, mob.m_20186_());
            double d2 = Mth.lerp((double) partialTick, mob.f_19792_, mob.m_20189_());
            ms.pushPose();
            ms.translate(-d0, -d1, -d2);
            this.renderRein(mob, partialTick, ms, multiBufferSource1, entity, true);
            this.renderRein(mob, partialTick, ms, multiBufferSource1, entity, false);
            ms.popPose();
        }
    }

    protected void scale(EntityLaviathan entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    protected boolean isShaking(EntityLaviathan entity) {
        return entity.m_20071_() && !entity.isObsidian() && !renderWithoutShaking;
    }

    public ResourceLocation getTextureLocation(EntityLaviathan entity) {
        return entity.isObsidian() ? TEXTURE_OBSIDIAN : TEXTURE;
    }

    private float getHeadShakeForReins(EntityLaviathan mob, float partialTick) {
        float hh1 = mob.prevHeadHeight;
        float hh2 = mob.getHeadHeight();
        float rawHeadHeight = (hh1 + (hh2 - hh1) * partialTick) / 3.0F;
        float clampedNeckRot = Mth.clamp(-rawHeadHeight, -1.0F, 1.0F);
        float headStillProgress = 1.0F - Math.abs(clampedNeckRot);
        float swim = Mth.lerp(partialTick, mob.prevSwimProgress, mob.swimProgress);
        float limbSwingAmount = mob.f_267362_.speed(partialTick);
        float swing = mob.f_267362_.position() + partialTick;
        float swingAmount = limbSwingAmount * swim * 0.2F * headStillProgress;
        float swimSpeed = mob.swimProgress >= 5.0F ? 0.3F : 0.9F;
        float swimDegree = 0.5F + swim * 0.05F;
        float boxOffset = (float) (-Math.PI * 7.0 / 2.0);
        float moveScale = 1.0F;
        return 1.3F * Mth.cos(swing * swimSpeed * moveScale + boxOffset * 2.0F) * swingAmount * swimDegree * moveScale;
    }

    private float getHeadBobForReins(EntityLaviathan mob, float partialTick) {
        float swing = (float) mob.f_19797_ + partialTick;
        float swingAmount = 1.0F;
        float idleSpeed = 0.04F;
        float idleDegree = 0.3F;
        float boxOffset = (float) (Math.PI * 3.0 / 2.0);
        float moveScale = 1.0F;
        return 0.8F * Mth.cos(swing * idleSpeed * moveScale + boxOffset * 2.0F) * swingAmount * idleDegree * moveScale;
    }

    private <E extends Entity> void renderRein(EntityLaviathan mob, float partialTick, PoseStack poseStack0, MultiBufferSource multiBufferSource1, E rider, boolean left) {
        poseStack0.pushPose();
        Entity head = mob.headPart;
        if (head != null) {
            float limbSwingAmount = mob.f_267362_.speed(partialTick);
            float shake = this.getHeadShakeForReins(mob, partialTick);
            float headYaw = Math.abs(mob.getHeadYaw(partialTick)) / 50.0F;
            float headPitch = 1.0F - Math.abs((mob.prevHeadHeight + (mob.getHeadHeight() - mob.prevHeadHeight) * partialTick) / 3.0F);
            float yawAdd = (1.0F - headYaw) * 0.4F * (1.0F - limbSwingAmount * 0.7F) - headPitch * 0.2F;
            Vec3 vec3 = rider instanceof LivingEntity ? this.getReinPosition((LivingEntity) rider, partialTick, left, shake) : rider.getRopeHoldPosition(partialTick);
            double d0 = (double) (Mth.lerp(partialTick, mob.f_20883_, mob.f_20884_) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
            Vec3 vec31 = new Vec3((double) ((left ? -0.05F - yawAdd : 0.05F + yawAdd) + shake), (double) (0.45F - headYaw * 0.2F + this.getHeadBobForReins(mob, partialTick)), 0.1F);
            double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
            double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
            double d3 = Mth.lerp((double) partialTick, head.xo, head.getX()) + d1;
            double d4 = Mth.lerp((double) partialTick, head.yo, head.getY()) + vec31.y;
            double d5 = Mth.lerp((double) partialTick, head.zo, head.getZ()) + d2;
            poseStack0.translate(d3, d4, d5);
            float f = (float) (vec3.x - d3);
            float f1 = (float) (vec3.y - d4);
            float f2 = (float) (vec3.z - d5);
            VertexConsumer vertexconsumer = multiBufferSource1.getBuffer(RenderType.leash());
            Matrix4f matrix4f = poseStack0.last().pose();
            float f4 = (float) (Mth.fastInvSqrt((double) (f * f + f2 * f2)) * 0.025F / 2.0);
            float f5 = f2 * f4;
            float f6 = f * f4;
            BlockPos blockpos = AMBlockPos.fromVec3(mob.m_20299_(partialTick));
            BlockPos blockpos1 = AMBlockPos.fromVec3(rider.getEyePosition(partialTick));
            int i = this.m_6086_(mob, blockpos);
            int j = mob.m_9236_().m_45517_(LightLayer.BLOCK, blockpos1);
            int k = mob.m_9236_().m_45517_(LightLayer.SKY, blockpos);
            int l = mob.m_9236_().m_45517_(LightLayer.SKY, blockpos1);
            float width = 0.05F;
            for (int i1 = 0; i1 <= 24; i1++) {
                addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, i1, false);
            }
            for (int j1 = 24; j1 >= 0; j1--) {
                addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, j1, true);
            }
            poseStack0.popPose();
        }
    }

    private Vec3 getReinPosition(LivingEntity entity, float float0, boolean left, float shake) {
        double d0 = 0.4 * (left ? -1.0 : 1.0) - 0.0;
        float f = Mth.lerp(float0 * 0.5F, entity.m_146909_(), entity.f_19860_) * (float) (Math.PI / 180.0);
        float f1 = Mth.lerp(float0, entity.yBodyRotO, entity.yBodyRot) * (float) (Math.PI / 180.0);
        if (entity.isFallFlying() || entity.isAutoSpinAttack()) {
            Vec3 vec3 = entity.m_20252_(float0);
            Vec3 vec31 = entity.m_20184_();
            double d1 = vec31.horizontalDistanceSqr();
            double d2 = vec3.horizontalDistanceSqr();
            float f2;
            if (d1 > 0.0 && d2 > 0.0) {
                double d3 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d1 * d2);
                double d4 = vec31.x * vec3.z - vec31.z * vec3.x;
                f2 = (float) (Math.signum(d4) * Math.acos(d3));
            } else {
                f2 = 0.0F;
            }
            return entity.m_20318_(float0).add(new Vec3(d0, -0.11, 0.85).zRot(-f2).xRot(-f).yRot(-f1));
        } else if (entity.isVisuallySwimming()) {
            return entity.m_20318_(float0).add(new Vec3(d0, 0.3, -0.34).xRot(-f).yRot(-f1));
        } else {
            double d5 = entity.m_20191_().getYsize() - 1.0;
            double d6 = entity.m_6047_() ? -0.2 : 0.07;
            return entity.m_20318_(float0).add(new Vec3(d0, d5, d6).yRot(-f1));
        }
    }

    static class LayerOverlays extends RenderLayer<EntityLaviathan, ModelLaviathan> {

        public LayerOverlays(RenderLaviathan render) {
            super(render);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntityLaviathan laviathan, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            if (!laviathan.isObsidian()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.eyes(RenderLaviathan.TEXTURE_GLOW));
                ((ModelLaviathan) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            if (laviathan.hasBodyGear()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(RenderLaviathan.TEXTURE_GEAR));
                ((ModelLaviathan) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
            if (laviathan.hasHeadGear()) {
                VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(RenderLaviathan.TEXTURE_HELMET));
                ((ModelLaviathan) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}