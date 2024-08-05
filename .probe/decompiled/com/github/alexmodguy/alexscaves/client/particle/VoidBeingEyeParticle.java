package com.github.alexmodguy.alexscaves.client.particle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeRenderTypes;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VoidBeingEyeParticle extends Particle {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] { new ResourceLocation("alexscaves", "textures/particle/void_eye_0.png"), new ResourceLocation("alexscaves", "textures/particle/void_eye_1.png"), new ResourceLocation("alexscaves", "textures/particle/void_eye_2.png") };

    private int textureIndex = 0;

    private float prevCameraOffsetX = 0.0F;

    private float prevCameraOffsetY = 0.0F;

    private float cameraOffsetX = 0.0F;

    private float cameraOffsetY = 0.0F;

    private float animationOffset = 0.0F;

    public VoidBeingEyeParticle(ClientLevel world, double x, double y, double z, float cameraOffsetX, float cameraOffsetY) {
        super(world, x, y, z);
        this.textureIndex = this.f_107223_.nextInt(2);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.animationOffset = (float) ((double) this.f_107223_.nextFloat() * Math.PI);
        this.f_107225_ = 300;
        this.f_107226_ = 0.0F;
        this.prevCameraOffsetX = cameraOffsetX;
        this.prevCameraOffsetY = cameraOffsetY;
        this.cameraOffsetX = cameraOffsetX;
        this.cameraOffsetY = cameraOffsetY;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevCameraOffsetX = this.cameraOffsetX;
        this.prevCameraOffsetY = this.cameraOffsetY;
        if (this.f_107224_ > 200) {
            float offsetX = (0.0F - this.cameraOffsetX) / (float) (this.f_107225_ - 200);
            float offsetY = (-5.0F - this.cameraOffsetY) / (float) (this.f_107225_ - 200);
            this.cameraOffsetX += offsetX;
            this.cameraOffsetY += offsetY;
        }
        if (this.f_107224_ > this.f_107225_ - 20) {
            this.cameraOffsetY += -0.25F;
        }
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        this.f_107230_ = VoidBeingCloudParticle.getAlphaFromAge(this.f_107224_, this.f_107225_);
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        Quaternionf quaternion;
        if (this.f_107231_ == 0.0F) {
            quaternion = camera.rotation();
        } else {
            quaternion = new Quaternionf(camera.rotation());
            float f3 = Mth.lerp(partialTick, this.f_107204_, this.f_107231_);
            quaternion.mul(Axis.ZP.rotation(f3));
        }
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer portalStatic = multibuffersource$buffersource.getBuffer(ForgeRenderTypes.getUnlitTranslucent(TEXTURES[this.textureIndex]));
        PoseStack posestack = new PoseStack();
        PoseStack.Pose posestack$pose = posestack.last();
        Matrix3f matrix3f = posestack$pose.normal();
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, -0.05F), new Vector3f(-1.0F, 1.0F, -0.05F), new Vector3f(1.0F, 1.0F, -0.05F), new Vector3f(1.0F, -1.0F, -0.05F) };
        float f4 = 0.5F;
        float offsetX = this.prevCameraOffsetX + (this.cameraOffsetX - this.prevCameraOffsetX) * partialTick;
        float offsetY = this.prevCameraOffsetY + (this.cameraOffsetY - this.prevCameraOffsetY) * partialTick;
        float shakeX = 0.0F;
        float shakeY = 0.0F;
        if (this.f_107224_ > 200) {
            shakeX = 0.3F * (float) Math.sin((double) (((float) this.f_107224_ + partialTick + 3.0F * this.animationOffset) * 0.54F));
            shakeY = 0.3F * (float) (-Math.sin((double) (((float) this.f_107224_ + partialTick + 3.0F * this.animationOffset) * 0.54F + 2.0F)));
        }
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i].add(offsetX + shakeX, offsetY + shakeY + 0.2F * (float) Math.sin((double) (((float) this.f_107224_ + partialTick + this.animationOffset) * 0.1F)), 0.0F);
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0.0F;
        float f8 = 1.0F;
        float f5 = 0.0F;
        float f6 = 1.0F;
        int j = 240;
        portalStatic.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f8, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f8, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f7, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        portalStatic.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f7, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        multibuffersource$buffersource.endBatch();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoidBeingEyeParticle(worldIn, x, y, z, (float) xSpeed, (float) ySpeed);
        }
    }
}