package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.entity.EntityVineLasso;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderVineLasso extends EntityRenderer<EntityVineLasso> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/vine_lasso.png");

    private static final float VINES_COLOR_R = 0.3764706F;

    private static final float VINES_COLOR_G = 0.56078434F;

    private static final float VINES_COLOR_B = 0.24313726F;

    private static final float VINES_COLOR_R2 = 0.6509804F;

    private static final float VINES_COLOR_G2 = 0.7490196F;

    private static final float VINES_COLOR_B2 = 0.38039216F;

    public RenderVineLasso(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(EntityVineLasso entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.25, 0.0);
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 180.0F));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.translate(0.0, -0.1F, 0.0);
        matrixStackIn.pushPose();
        matrixStackIn.scale(0.45F, 0.45F, 0.45F);
        this.renderCircle(matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.popPose();
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        LivingEntity holder = Minecraft.getInstance().player;
        if (holder != null) {
            double d0 = Mth.lerp((double) partialTicks, entityIn.f_19790_, entityIn.m_20185_());
            double d1 = Mth.lerp((double) partialTicks, entityIn.f_19791_, entityIn.m_20186_());
            double d2 = Mth.lerp((double) partialTicks, entityIn.f_19792_, entityIn.m_20189_());
            matrixStackIn.pushPose();
            matrixStackIn.translate(-d0, -d1, -d2);
            renderVine(entityIn, partialTicks, matrixStackIn, bufferIn, holder, holder.getMainArm() != HumanoidArm.LEFT, -0.4F);
            matrixStackIn.popPose();
        }
    }

    private void renderCircle(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        PoseStack.Pose lvt_19_1_ = matrixStackIn.last();
        Matrix4f lvt_20_1_ = lvt_19_1_.pose();
        Matrix3f lvt_21_1_ = lvt_19_1_.normal();
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, -1, 0, -1, 0.0F, 0.0F, 1, 0, 1, packedLightIn);
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, -1, 0, 1, 0.0F, 1.0F, 1, 0, 1, packedLightIn);
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, 1, 0, 1, 1.0F, 1.0F, 1, 0, 1, packedLightIn);
        this.drawVertex(lvt_20_1_, lvt_21_1_, ivertexbuilder, 1, 0, -1, 1.0F, 0.0F, 1, 0, 1, packedLightIn);
        matrixStackIn.popPose();
    }

    public static <E extends Entity> void renderVine(Entity mob, float partialTick, PoseStack poseStack0, MultiBufferSource multiBufferSource1, LivingEntity player, boolean left, float zOffset) {
        poseStack0.pushPose();
        float bodyRot = mob instanceof LivingEntity ? ((LivingEntity) mob).yBodyRot : mob.getYRot();
        float bodyRot0 = mob instanceof LivingEntity ? ((LivingEntity) mob).yBodyRotO : mob.yRotO;
        Vec3 vec3 = player.m_7398_(partialTick);
        double d0 = (double) (Mth.lerp(partialTick, bodyRot, bodyRot0) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
        Vec3 vec31 = new Vec3((double) (left ? -0.05F : 0.05F), (double) mob.getEyeHeight(), (double) zOffset);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp((double) partialTick, mob.xo, mob.getX()) + d1;
        double d4 = Mth.lerp((double) partialTick, mob.yo, mob.getY()) + vec31.y;
        double d5 = Mth.lerp((double) partialTick, mob.zo, mob.getZ()) + d2;
        poseStack0.translate(d3, d4, d5);
        float f = (float) (vec3.x - d3);
        float f1 = (float) (vec3.y - d4);
        float f2 = (float) (vec3.z - d5);
        VertexConsumer vertexconsumer = multiBufferSource1.getBuffer(RenderType.leash());
        Matrix4f matrix4f = poseStack0.last().pose();
        float f4 = (float) (Mth.fastInvSqrt((double) (f * f + f2 * f2)) * 0.025F / 2.0);
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = AMBlockPos.fromVec3(mob.getEyePosition(partialTick));
        BlockPos blockpos1 = AMBlockPos.fromVec3(player.m_20299_(partialTick));
        int i = getVineLightLevel(mob, blockpos);
        int j = mob.level().m_45517_(LightLayer.BLOCK, blockpos1);
        int k = mob.level().m_45517_(LightLayer.SKY, blockpos);
        int l = mob.level().m_45517_(LightLayer.SKY, blockpos1);
        float width = 0.1F;
        for (int i1 = 0; i1 <= 24; i1++) {
            addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, i1, false);
        }
        for (int j1 = 24; j1 >= 0; j1--) {
            addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, j1, true);
        }
        poseStack0.popPose();
    }

    protected static int getVineLightLevel(Entity entity0, BlockPos blockPos1) {
        return entity0.isOnFire() ? 15 : entity0.level().m_45517_(LightLayer.BLOCK, blockPos1);
    }

    private static Vec3 getVinePosition(LivingEntity entity, float float0, boolean left, float shake) {
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

    private static void addVertexPairAlex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3, float float4, int int5, int int6, int int7, int int8, float float9, float float10, float float11, float float12, int int13, boolean boolean14) {
        float f = (float) int13 / 24.0F;
        int i = (int) Mth.lerp(f, (float) int5, (float) int6);
        int j = (int) Mth.lerp(f, (float) int7, (float) int8);
        int k = LightTexture.pack(i, j);
        float f2 = 0.3764706F;
        float f3 = 0.56078434F;
        float f4 = 0.24313726F;
        if (int13 % 2 == (boolean14 ? 1 : 0)) {
            f2 = 0.6509804F;
            f3 = 0.7490196F;
            f4 = 0.38039216F;
        }
        float f5 = float2 * f;
        float f6 = float3 > 0.0F ? float3 * f * f : float3 - float3 * (1.0F - f) * (1.0F - f);
        float f7 = float4 * f;
        vertexConsumer0.vertex(matrixF1, f5 - float11, f6 + float10, f7 + float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        vertexConsumer0.vertex(matrixF1, f5 + float11, f6 + float9 - float10, f7 - float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    public ResourceLocation getTextureLocation(EntityVineLasso entity) {
        return TEXTURE;
    }

    public void drawVertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, VertexConsumer p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_) {
        p_229039_3_.vertex(p_229039_1_, (float) p_229039_4_, (float) p_229039_5_, (float) p_229039_6_).color(255, 255, 255, 255).uv(p_229039_7_, p_229039_8_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(p_229039_2_, (float) p_229039_9_, (float) p_229039_11_, (float) p_229039_10_).endVertex();
    }
}