package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public abstract class AbstractTrailParticle extends Particle {

    private Vec3[] trailPositions = new Vec3[64];

    private int trailPointer = -1;

    protected float trailR = 1.0F;

    protected float trailG = 1.0F;

    protected float trailB = 1.0F;

    protected float trailA = 1.0F;

    public AbstractTrailParticle(ClientLevel world, double x, double y, double z, double xd, double yd, double zd) {
        super(world, x, y, z);
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
    }

    @Override
    public void tick() {
        this.tickTrail();
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107215_ *= 0.99;
        this.f_107216_ *= 0.99;
        this.f_107217_ *= 0.99;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
        }
    }

    public void tickTrail() {
        Vec3 currentPosition = new Vec3(this.f_107212_, this.f_107213_, this.f_107214_);
        if (this.trailPointer == -1) {
            for (int i = 0; i < this.trailPositions.length; i++) {
                this.trailPositions[i] = currentPosition;
            }
        }
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        this.trailPositions[this.trailPointer] = currentPosition;
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTick) {
        if (this.trailPointer > -1) {
            MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer vertexconsumer = multibuffersource$buffersource.getBuffer(ACRenderTypes.m_110467_(this.getTrailTexture()));
            Vec3 cameraPos = camera.getPosition();
            double x = (double) ((float) Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_));
            double y = (double) ((float) Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_));
            double z = (double) ((float) Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_));
            PoseStack posestack = new PoseStack();
            posestack.pushPose();
            posestack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
            int samples = 0;
            Vec3 drawFrom = new Vec3(x, y, z);
            float zRot = this.getTrailRot(camera);
            Vec3 topAngleVec = new Vec3(0.0, (double) (this.getTrailHeight() / 2.0F), 0.0).zRot(zRot);
            Vec3 bottomAngleVec = new Vec3(0.0, (double) (this.getTrailHeight() / -2.0F), 0.0).zRot(zRot);
            int j = this.m_6355_(partialTick);
            while (samples < this.sampleCount()) {
                Vec3 sample = this.getTrailPosition(samples * this.sampleStep(), partialTick);
                float u1 = (float) samples / (float) this.sampleCount();
                float u2 = u1 + 1.0F / (float) this.sampleCount();
                PoseStack.Pose posestack$pose = posestack.last();
                Matrix4f matrix4f = posestack$pose.pose();
                Matrix3f matrix3f = posestack$pose.normal();
                vertexconsumer.vertex(matrix4f, (float) drawFrom.x + (float) bottomAngleVec.x, (float) drawFrom.y + (float) bottomAngleVec.y, (float) drawFrom.z + (float) bottomAngleVec.z).color(this.trailR, this.trailG, this.trailB, this.trailA).uv(u1, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float) sample.x + (float) bottomAngleVec.x, (float) sample.y + (float) bottomAngleVec.y, (float) sample.z + (float) bottomAngleVec.z).color(this.trailR, this.trailG, this.trailB, this.trailA).uv(u2, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float) sample.x + (float) topAngleVec.x, (float) sample.y + (float) topAngleVec.y, (float) sample.z + (float) topAngleVec.z).color(this.trailR, this.trailG, this.trailB, this.trailA).uv(u2, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                vertexconsumer.vertex(matrix4f, (float) drawFrom.x + (float) topAngleVec.x, (float) drawFrom.y + (float) topAngleVec.y, (float) drawFrom.z + (float) topAngleVec.z).color(this.trailR, this.trailG, this.trailB, this.trailA).uv(u1, 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
                samples++;
                drawFrom = sample;
            }
            multibuffersource$buffersource.endBatch();
            posestack.popPose();
        }
    }

    public float getTrailRot(Camera camera) {
        return (float) (-Math.PI / 180.0) * camera.getXRot();
    }

    public abstract float getTrailHeight();

    public abstract ResourceLocation getTrailTexture();

    public int sampleCount() {
        return 20;
    }

    public int sampleStep() {
        return 1;
    }

    public Vec3 getTrailPosition(int pointer, float partialTick) {
        if (this.f_107220_) {
            partialTick = 1.0F;
        }
        int i = this.trailPointer - pointer & 63;
        int j = this.trailPointer - pointer - 1 & 63;
        Vec3 d0 = this.trailPositions[j];
        Vec3 d1 = this.trailPositions[i].subtract(d0);
        return d0.add(d1.scale((double) partialTick));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}