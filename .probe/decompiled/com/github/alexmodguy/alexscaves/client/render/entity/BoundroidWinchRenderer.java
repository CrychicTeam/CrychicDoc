package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.BoundroidWinchModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.BoundroidWinchEntity;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class BoundroidWinchRenderer extends MobRenderer<BoundroidWinchEntity, BoundroidWinchModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/boundroid_winch.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/boundroid_winch_glow.png");

    private static final ResourceLocation TEXTURE_CHAIN = new ResourceLocation("minecraft:textures/block/chain.png");

    public static final int MAX_CHAIN_SEGMENTS = 256;

    public BoundroidWinchRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new BoundroidWinchModel(), 0.3F);
        this.m_115326_(new BoundroidWinchRenderer.LayerGlow());
    }

    protected void scale(BoundroidWinchEntity mob, PoseStack matrixStackIn, float partialTicks) {
    }

    public boolean shouldRender(BoundroidWinchEntity entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        } else {
            Entity weapon = entity.getHead();
            if (weapon != null) {
                Vec3 vec3 = entity.m_20182_();
                Vec3 vec31 = weapon.position();
                return camera.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
            } else {
                return false;
            }
        }
    }

    protected void setupRotations(BoundroidWinchEntity entity, PoseStack poseStack, float ptich, float yaw, float partialTicks) {
        if (m_194453_(entity)) {
            poseStack.translate(0.0F, entity.m_20206_() + 0.1F, 0.0F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }

    public void render(BoundroidWinchEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, source, packedLight);
        Vec3 translateFrom = entity.m_20318_(partialTicks);
        Vec3 modelOffset = ((BoundroidWinchModel) this.f_115290_).getChainPosition(new Vec3(0.0, 0.1F, 0.0));
        Vec3 chainTo = entity.getChainTo(partialTicks).subtract(translateFrom);
        Vec3 chainFrom = entity.getChainFrom(partialTicks).add(modelOffset).subtract(translateFrom).subtract(chainTo);
        VertexConsumer chainBuffer = source.getBuffer(RenderType.entityCutoutNoCull(TEXTURE_CHAIN));
        int overlayHurtEffect = m_115338_(entity, this.m_6931_(entity, partialTicks));
        poseStack.pushPose();
        poseStack.translate(chainTo.x, chainTo.y, chainTo.z);
        renderChain(chainFrom, poseStack, chainBuffer, packedLight, overlayHurtEffect);
        poseStack.popPose();
    }

    private double modifyVecAngle(double dimension) {
        float abs = (float) Math.abs(dimension);
        return Math.signum(dimension) * Mth.clamp(Math.pow((double) abs, 0.1), 0.01 * (double) abs, (double) abs);
    }

    public ResourceLocation getTextureLocation(BoundroidWinchEntity entity) {
        return TEXTURE;
    }

    public static void renderChain(Vec3 to, PoseStack poseStack, VertexConsumer buffer, int packedLightIn, int overlayCoords) {
        double d = to.horizontalDistance();
        float rotY = (float) (Mth.atan2(to.x, to.z) * 180.0F / (float) Math.PI);
        float rotX = (float) (-(Mth.atan2(to.y, d) * 180.0F / (float) Math.PI)) - 90.0F;
        float chainWidth = 0.1875F;
        float chainOffset = chainWidth * -0.5F;
        float chainLength = (float) to.length();
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(rotY));
        poseStack.mulPose(Axis.XP.rotationDegrees(rotX));
        poseStack.translate(0.0F, -chainLength, 0.0F);
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        buffer.vertex(matrix4f, chainOffset, 0.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, chainLength).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        buffer.vertex(matrix4f, chainWidth + chainOffset, 0.0F, 0.0F).color(255, 255, 255, 255).uv(chainWidth, chainLength).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        buffer.vertex(matrix4f, chainWidth + chainOffset, chainLength, 0.0F).color(255, 255, 255, 255).uv(chainWidth, 0.0F).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        buffer.vertex(matrix4f, chainOffset, chainLength, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        float pixelSkip = 0.15625F;
        buffer.vertex(matrix4f, 0.0F, pixelSkip, chainOffset).color(255, 255, 255, 255).uv(chainWidth, chainLength + pixelSkip).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        buffer.vertex(matrix4f, 0.0F, pixelSkip, chainWidth + chainOffset).color(255, 255, 255, 255).uv(chainWidth * 2.0F, chainLength + pixelSkip).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        buffer.vertex(matrix4f, 0.0F, chainLength + pixelSkip, chainWidth + chainOffset).color(255, 255, 255, 255).uv(chainWidth * 2.0F, pixelSkip).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        buffer.vertex(matrix4f, 0.0F, chainLength + pixelSkip, chainOffset).color(255, 255, 255, 255).uv(chainWidth, pixelSkip).overlayCoords(overlayCoords).uv2(packedLightIn).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        poseStack.popPose();
    }

    class LayerGlow extends RenderLayer<BoundroidWinchEntity, BoundroidWinchModel> {

        public LayerGlow() {
            super(BoundroidWinchRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, BoundroidWinchEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(BoundroidWinchRenderer.TEXTURE_GLOW));
            float alpha = (float) (1.0 + Math.sin((double) (ageInTicks * 0.1F + 2.0F))) * 0.1F + 0.5F;
            ((BoundroidWinchModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}