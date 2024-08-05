package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VoidBeingTendrilParticle extends Particle {

    private static final ResourceLocation TENDRIL_TEXTURE = new ResourceLocation("alexscaves", "textures/particle/void_being_cloud_tendril.png");

    private Vec3 tipTarget;

    private double tipX;

    private double tipY;

    private double tipZ;

    private double prevTipX;

    private double prevTipY;

    private double prevTipZ;

    private float animationOffset = 0.0F;

    private int targetId = -1;

    private float size;

    private int seekByTime = 0;

    private float cameraOffsetX = 0.0F;

    private float cameraOffsetY = 0.0F;

    private VoidBeingTendrilParticle(ClientLevel world, double x, double y, double z, double targetId, double seekByTime) {
        super(world, x, y, z);
        this.m_107250_(1.0F, 1.0F);
        this.f_107226_ = 0.0F;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107225_ = 300;
        this.size = 1.0F;
        this.tipTarget = new Vec3((double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F)).normalize().scale(12.0).add(x, y, z);
        this.targetId = (int) targetId;
        this.animationOffset = (float) (Math.PI * (double) this.f_107223_.nextFloat());
        this.tipX = x;
        this.tipY = y;
        this.tipZ = z;
        this.cameraOffsetX = (this.f_107223_.nextFloat() - 0.5F) * 1.3F;
        this.cameraOffsetY = (this.f_107223_.nextFloat() - 0.5F) * 1.3F;
        this.seekByTime = (int) seekByTime;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevTipX = this.tipX;
        this.prevTipY = this.tipY;
        this.prevTipZ = this.tipZ;
        Entity entityTarget = null;
        if (this.targetId != -1 && this.f_107224_ > this.seekByTime) {
            entityTarget = Minecraft.getInstance().level.getEntity(this.targetId);
            if (entityTarget == null) {
                this.targetId = -1;
                this.m_107274_();
            } else {
                this.tipTarget = entityTarget.position().add(0.0, 0.25, 0.0);
            }
        }
        if (this.tipTarget != null) {
            float ageSmooth = (float) this.f_107224_ * 0.3F;
            Vec3 tippening = this.tipTarget.subtract(this.tipX, this.tipY, this.tipZ);
            if (entityTarget == null) {
                tippening = tippening.add((double) ((float) Math.sin((double) (ageSmooth + this.animationOffset))), (double) ((float) Math.cos((double) ageSmooth - (Math.PI / 2) + (double) this.animationOffset)), (double) (-((float) Math.cos((double) (ageSmooth + this.animationOffset)))));
            }
            if (tippening.length() > 1.0) {
                tippening = tippening.normalize();
            }
            this.tipX = this.tipX + tippening.x * 0.2;
            this.tipY = this.tipY + tippening.y * 0.2;
            this.tipZ = this.tipZ + tippening.z * 0.2;
            if (entityTarget == null && this.f_107223_.nextFloat() < 0.1F) {
                this.tipTarget = new Vec3((double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F)).normalize().scale(12.0).add(this.f_107212_, this.f_107213_, this.f_107214_);
            }
        }
        this.f_107215_ *= 0.97;
        this.f_107216_ *= 0.97;
        this.f_107217_ *= 0.97;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 cameraPos = camera.getPosition();
        double x = (double) ((float) Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_));
        double y = (double) ((float) Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_));
        double z = (double) ((float) Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_));
        Vector3f cameraOffset = new Vector3f(this.cameraOffsetX, this.cameraOffsetY, -0.1F);
        Quaternionf quaternion = new Quaternionf(camera.rotation());
        cameraOffset.rotate(quaternion);
        float width = this.targetId == -1 ? 1.5F : 1.5F + (float) this.f_107224_ / (float) this.f_107225_;
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(ACRenderTypes.m_110467_(TENDRIL_TEXTURE));
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(-cameraPos.x + x + (double) cameraOffset.x, -cameraPos.y + y + (double) cameraOffset.y + (double) (0.2F * (float) Math.sin((double) (((float) this.f_107224_ + partialTick + this.animationOffset) * 0.1F))), -cameraPos.z + z + (double) cameraOffset.z);
        int samples = 1;
        Vec3 drawFrom = new Vec3(0.0, 0.0, 0.0);
        Vec3 topAngleVec = new Vec3(0.0, (double) width, 0.0);
        Vec3 bottomAngleVec = new Vec3(0.0, (double) (-width), 0.0);
        int j = this.m_6355_(partialTick);
        float sampleCount = 20.0F;
        while ((float) samples <= sampleCount) {
            float samplesScale = (float) samples / sampleCount;
            float wiggleAmount = (float) Math.sin((double) samplesScale * Math.PI);
            Vec3 drawTo = this.getTendrilPosition(samplesScale, wiggleAmount, new Vec3(cameraOffset), partialTick);
            float u1 = (float) (samples - 1) / sampleCount;
            float u2 = (float) samples / sampleCount;
            float overallAlpha = VoidBeingCloudParticle.getAlphaFromAge(this.f_107224_, this.f_107225_);
            float startA = Math.min(1.0F, (float) samples / (sampleCount - 8.0F)) * overallAlpha;
            float endA = Math.min(1.0F, (float) (samples + 1) / (sampleCount - 8.0F)) * overallAlpha;
            PoseStack.Pose posestack$pose = posestack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            vertexconsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomAngleVec.x, (float) drawFrom.y + (float) bottomAngleVec.y, (float) drawFrom.z + (float) bottomAngleVec.z).color(1.0F, 1.0F, 1.0F, startA).uv(u1, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) drawTo.x + (float) bottomAngleVec.x, (float) drawTo.y + (float) bottomAngleVec.y, (float) drawTo.z + (float) bottomAngleVec.z).color(1.0F, 1.0F, 1.0F, endA).uv(u2, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) drawTo.x + (float) topAngleVec.x, (float) drawTo.y + (float) topAngleVec.y, (float) drawTo.z + (float) topAngleVec.z).color(1.0F, 1.0F, 1.0F, endA).uv(u2, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            vertexconsumer.vertex(matrix4f, (float) drawFrom.x + (float) topAngleVec.x, (float) drawFrom.y + (float) topAngleVec.y, (float) drawFrom.z + (float) topAngleVec.z).color(1.0F, 1.0F, 1.0F, startA).uv(u1, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
            samples++;
            drawFrom = drawTo;
        }
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    private Vec3 getTendrilPosition(float f, float wiggleAmount, Vec3 cameraOffset, float partialTick) {
        float x = (float) Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_);
        float y = (float) Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_);
        float z = (float) Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_);
        float ageSmooth = ((float) this.f_107224_ + partialTick) * (this.targetId != -1 && this.f_107224_ >= 200 ? 1.0F : 0.04F);
        Vec3 wiggleVec = new Vec3((double) ((float) Math.sin((double) (ageSmooth + f + this.animationOffset))), (double) ((float) Math.cos((double) ageSmooth - (Math.PI / 2) + (double) f + (double) this.animationOffset)), (double) (-((float) Math.cos((double) (ageSmooth + f + this.animationOffset))))).scale((double) (wiggleAmount * 0.2F));
        Vec3 lerpOf = new Vec3(this.tipX - this.prevTipX, this.tipY - this.prevTipY, this.tipZ - this.prevTipZ).scale((double) partialTick);
        Vec3 vec31 = new Vec3(this.prevTipX, this.prevTipY, this.prevTipZ).add(lerpOf).subtract(cameraOffset);
        return vec31.subtract((double) x, (double) y, (double) z).scale((double) f).add(wiggleVec);
    }

    private Quaternionf calcCameraAngle(Camera camera, float sampleScale) {
        float followCameraAmount = sampleScale * sampleScale;
        return new Quaternionf(camera.rotation()).rotateX(followCameraAmount * (float) (-Math.PI / 180.0) * camera.getXRot()).rotateY(followCameraAmount * (float) (Math.PI / 180.0) * camera.getYRot());
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoidBeingTendrilParticle(worldIn, x, y, z, xSpeed, ySpeed);
        }
    }
}