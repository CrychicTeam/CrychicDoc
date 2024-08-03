package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelSquidGrapple;
import com.github.alexthe666.alexsmobs.entity.EntitySquidGrapple;
import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
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

public class RenderSquidGrapple extends EntityRenderer<EntitySquidGrapple> {

    private static final ResourceLocation SQUID_TEXTURE = new ResourceLocation("alexsmobs:textures/entity/giant_squid.png");

    private static final ModelSquidGrapple SQUID_MODEL = new ModelSquidGrapple();

    private static final float TENTACLES_COLOR_R = 0.70980394F;

    private static final float TENTACLES_COLOR_G = 0.34117648F;

    private static final float TENTACLES_COLOR_B = 0.33333334F;

    private static final float TENTACLES_COLOR_R2 = 0.7490196F;

    private static final float TENTACLES_COLOR_G2 = 0.38431373F;

    private static final float TENTACLES_COLOR_B2 = 0.34901962F;

    public RenderSquidGrapple(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
    }

    private static void addVertexPairAlex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, float float2, float float3, float float4, int int5, int int6, int int7, int int8, float float9, float float10, float float11, float float12, int int13, boolean boolean14) {
        float f = (float) int13 / 24.0F;
        int i = (int) Mth.lerp(f, (float) int5, (float) int6);
        int j = (int) Mth.lerp(f, (float) int7, (float) int8);
        int k = LightTexture.pack(i, j);
        float f2 = 0.70980394F;
        float f3 = 0.34117648F;
        float f4 = 0.33333334F;
        if (int13 % 2 == (boolean14 ? 1 : 0)) {
            f2 = 0.7490196F;
            f3 = 0.38431373F;
            f4 = 0.34901962F;
        }
        float f5 = float2 * f;
        float f6 = float3 > 0.0F ? float3 * f * f : float3 - float3 * (1.0F - f) * (1.0F - f);
        float f7 = float4 * f;
        vertexConsumer0.vertex(matrixF1, f5 - float11, f6 + float10, f7 + float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        vertexConsumer0.vertex(matrixF1, f5 + float11, f6 + float9 - float10, f7 - float12).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    public static <E extends Entity> void renderTentacle(Entity mob, float partialTick, PoseStack poseStack0, MultiBufferSource multiBufferSource1, LivingEntity player, boolean left, float zOffset) {
        poseStack0.pushPose();
        float bodyRot = mob instanceof LivingEntity ? ((LivingEntity) mob).yBodyRot : mob.getYRot();
        float bodyRot0 = mob instanceof LivingEntity ? ((LivingEntity) mob).yBodyRotO : mob.yRotO;
        Vec3 vec3 = player.m_7398_(partialTick);
        double d0 = (double) (Mth.lerp(partialTick, bodyRot, bodyRot0) * (float) (Math.PI / 180.0)) + (Math.PI / 2);
        Vec3 vec31 = new Vec3(0.0, 0.0, 0.0);
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
        int i = getTentacleLightLevel(mob, blockpos);
        int j = mob.level().m_45517_(LightLayer.BLOCK, blockpos1);
        int k = mob.level().m_45517_(LightLayer.SKY, blockpos);
        int l = mob.level().m_45517_(LightLayer.SKY, blockpos1);
        float width = 0.2F;
        for (int i1 = 0; i1 <= 24; i1++) {
            addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, i1, false);
        }
        for (int j1 = 24; j1 >= 0; j1--) {
            addVertexPairAlex(vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, width, width, f5, f6, j1, true);
        }
        poseStack0.popPose();
    }

    protected static int getTentacleLightLevel(Entity entity0, BlockPos blockPos1) {
        return entity0.isOnFire() ? 15 : entity0.level().m_45517_(LightLayer.BLOCK, blockPos1);
    }

    public boolean shouldRender(EntitySquidGrapple grapple, Frustum f, double d1, double d2, double d3) {
        return super.shouldRender(grapple, f, d1, d2, d3) || grapple.getOwner() != null && (f.isVisible(grapple.getOwner().getBoundingBox()) || grapple.getOwner() == Minecraft.getInstance().player);
    }

    public void render(EntitySquidGrapple entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YN.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_())));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F + Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.translate(0.0F, -1.5F, -0.25F);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entityIn)));
        SQUID_MODEL.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        if (entityIn.getOwner() instanceof LivingEntity holder) {
            double d0 = Mth.lerp((double) partialTicks, entityIn.f_19790_, entityIn.m_20185_());
            double d1 = Mth.lerp((double) partialTicks, entityIn.f_19791_, entityIn.m_20186_());
            double d2 = Mth.lerp((double) partialTicks, entityIn.f_19792_, entityIn.m_20189_());
            matrixStackIn.pushPose();
            matrixStackIn.translate(-d0, -d1, -d2);
            renderTentacle(entityIn, partialTicks, matrixStackIn, bufferIn, holder, holder.getMainArm() != HumanoidArm.LEFT, -0.1F);
            matrixStackIn.popPose();
        }
    }

    public ResourceLocation getTextureLocation(EntitySquidGrapple entity) {
        return SQUID_TEXTURE;
    }

    public void drawVertex(Matrix4f p_229039_1_, Matrix3f p_229039_2_, VertexConsumer p_229039_3_, int p_229039_4_, int p_229039_5_, int p_229039_6_, float p_229039_7_, float p_229039_8_, int p_229039_9_, int p_229039_10_, int p_229039_11_, int p_229039_12_) {
        p_229039_3_.vertex(p_229039_1_, (float) p_229039_4_, (float) p_229039_5_, (float) p_229039_6_).color(255, 255, 255, 255).uv(p_229039_7_, p_229039_8_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229039_12_).normal(p_229039_2_, (float) p_229039_9_, (float) p_229039_11_, (float) p_229039_10_).endVertex();
    }
}