package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.TeletorModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.TeletorEntity;
import com.github.alexthe666.citadel.client.render.LightningBoltData;
import com.github.alexthe666.citadel.client.render.LightningRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class TeletorRenderer extends MobRenderer<TeletorEntity, TeletorModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/teletor.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/teletor_glow.png");

    private static final ResourceLocation TRAIL_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/teletor_trail.png");

    private Map<UUID, LightningRender> lightningRenderMap = new HashMap();

    public TeletorRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new TeletorModel(), 0.5F);
        this.m_115326_(new TeletorRenderer.LayerGlow());
    }

    protected void scale(TeletorEntity mob, PoseStack matrixStackIn, float partialTicks) {
        matrixStackIn.scale(0.9F, 0.9F, 0.9F);
    }

    public boolean shouldRender(TeletorEntity entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        } else {
            Entity weapon = entity.getWeapon();
            if (weapon != null) {
                Vec3 vec3 = entity.m_20182_();
                Vec3 vec31 = weapon.position();
                return camera.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
            } else {
                return false;
            }
        }
    }

    public ResourceLocation getTextureLocation(TeletorEntity entity) {
        return TEXTURE;
    }

    public void render(TeletorEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        double x = Mth.lerp((double) partialTicks, entityIn.f_19790_, entityIn.m_20185_());
        double y = Mth.lerp((double) partialTicks, entityIn.f_19791_, entityIn.m_20186_());
        double z = Mth.lerp((double) partialTicks, entityIn.f_19792_, entityIn.m_20189_());
        float yaw = entityIn.f_20884_ + (entityIn.f_20883_ - entityIn.f_20884_) * partialTicks;
        if (entityIn.hasTrail()) {
            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);
            this.m_7523_(entityIn, poseStack, 0.0F, 180.0F, partialTicks);
            Vec3 headModelPos = ((TeletorModel) this.m_7200_()).translateToHead(new Vec3(0.0, -0.4F, 0.0), yaw).scale(-1.0);
            poseStack.translate(headModelPos.x, headModelPos.y, headModelPos.z);
            this.renderTrail(entityIn, 0, partialTicks, poseStack, bufferIn, 0.2F, 0.2F, 0.8F, 0.8F, 240);
            this.renderTrail(entityIn, 1, partialTicks, poseStack, bufferIn, 0.8F, 0.2F, 0.2F, 0.8F, 240);
            poseStack.popPose();
        }
        Entity weapon = entityIn.getWeapon();
        if (weapon != null && entityIn.m_6084_() && weapon.isAlive()) {
            poseStack.pushPose();
            poseStack.translate(-x, -y, -z);
            this.m_7523_(entityIn, poseStack, 0.0F, 180.0F, partialTicks);
            Vec3 headModelPos = ((TeletorModel) this.m_7200_()).translateToHead(new Vec3(0.0, -0.4F, 0.0), yaw).scale(-1.0);
            Vec3 fromVec1 = entityIn.getHelmetPosition(0).add(headModelPos);
            Vec3 fromVec2 = entityIn.getHelmetPosition(1).add(headModelPos);
            Vec3 toVec = weapon.getPosition(partialTicks).add(0.0, (double) (weapon.getBbHeight() * 0.5F - 0.1F) + Math.sin((double) (((float) weapon.tickCount + partialTicks) * 0.1F)) * 0.1F, 0.0);
            int segCount = Mth.clamp((int) weapon.distanceTo(entityIn) + 2, 3, 30);
            float spreadFactor = Mth.clamp((10.0F - weapon.distanceTo(entityIn)) / 10.0F * 0.2F, 0.01F, 0.2F);
            LightningBoltData.BoltRenderInfo blueBoltData = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.2F, 0.2F, 0.8F, 0.8F), 0.1F);
            LightningBoltData.BoltRenderInfo redBoltData = new LightningBoltData.BoltRenderInfo(0.0F, spreadFactor, 0.0F, 0.0F, new Vector4f(0.8F, 0.2F, 0.2F, 0.8F), 0.1F);
            LightningBoltData bolt1 = new LightningBoltData(blueBoltData, fromVec1, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
            LightningBoltData bolt2 = new LightningBoltData(redBoltData, fromVec2, toVec, segCount).size(0.1F).lifespan(1).spawn(LightningBoltData.SpawnFunction.CONSECUTIVE).fade(LightningBoltData.FadeFunction.NONE);
            LightningRender lightningRender = this.getLightingRender(entityIn.m_20148_());
            lightningRender.update(entityIn, bolt1, partialTicks);
            lightningRender.update(weapon, bolt2, partialTicks);
            lightningRender.render(partialTicks, poseStack, bufferIn);
            poseStack.popPose();
        }
        if (!entityIn.m_6084_() && this.lightningRenderMap.containsKey(entityIn.m_20148_())) {
            this.lightningRenderMap.remove(entityIn.m_20148_());
        }
    }

    private LightningRender getLightingRender(UUID uuid) {
        if (this.lightningRenderMap.get(uuid) == null) {
            this.lightningRenderMap.put(uuid, new LightningRender());
        }
        return (LightningRender) this.lightningRenderMap.get(uuid);
    }

    private void renderTrail(TeletorEntity entityIn, int side, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, float trailR, float trailG, float trailB, float trailA, int packedLightIn) {
        int samples = 0;
        int sampleSize = 10;
        float trailHeight = 0.2F;
        float trailZRot = 0.0F;
        Vec3 topAngleVec = new Vec3(0.0, (double) trailHeight, 0.0).zRot(trailZRot);
        Vec3 bottomAngleVec = new Vec3(0.0, (double) (-trailHeight), 0.0).zRot(trailZRot);
        Vec3 drawFrom = entityIn.getTrailPosition(0, side, partialTicks);
        VertexConsumer vertexconsumer = bufferIn.getBuffer(RenderType.entityTranslucent(TRAIL_TEXTURE));
        while (samples < sampleSize) {
            Vec3 sample = entityIn.getTrailPosition(samples + 2, side, partialTicks);
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

    class LayerGlow extends RenderLayer<TeletorEntity, TeletorModel> {

        public LayerGlow() {
            super(TeletorRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, TeletorEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TeletorRenderer.TEXTURE_GLOW));
            float alpha = (float) (1.0 + Math.sin((double) (ageInTicks * 0.3F))) * 0.1F + 0.8F;
            ((TeletorModel) this.m_117386_()).m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}