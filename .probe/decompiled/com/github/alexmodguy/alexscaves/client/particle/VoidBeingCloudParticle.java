package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.util.UnderzealotSacrifice;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
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
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VoidBeingCloudParticle extends Particle {

    private final int textureSize;

    private static int currentlyUsedTextures = 0;

    private final DynamicTexture dynamicTexture;

    private final RenderType renderType;

    private boolean requiresUpload = true;

    private float size;

    private int id = 0;

    private int targetId;

    private int totalTendrils;

    private int idleSoundTime = 30;

    private boolean spawnedExtras = false;

    public VoidBeingCloudParticle(ClientLevel world, double x, double y, double z, int size, int target, int totalTendrils) {
        super(world, x, y, z);
        this.f_107226_ = 0.0F;
        this.f_107212_ = x;
        this.f_107213_ = y;
        this.f_107214_ = z;
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107225_ = 300;
        this.size = (float) (size + 1);
        this.m_107250_(this.size, this.size);
        this.textureSize = 32 + size * 32;
        this.dynamicTexture = new DynamicTexture(this.textureSize, this.textureSize, true);
        this.id = currentlyUsedTextures;
        ResourceLocation resourcelocation = Minecraft.getInstance().textureManager.register("alexscavesvoid_particle/void_cloud_" + this.id, this.dynamicTexture);
        currentlyUsedTextures++;
        this.renderType = ACRenderTypes.getVoidBeingCloud(resourcelocation);
        this.targetId = target;
        this.totalTendrils = totalTendrils;
    }

    @Override
    public void tick() {
        if (this.f_107224_ <= 0 && !this.spawnedExtras) {
            this.onSpawn();
            this.spawnedExtras = true;
        }
        super.tick();
        this.f_107215_ *= 0.97;
        this.f_107216_ *= 0.97;
        this.f_107217_ *= 0.97;
        this.updateTexture();
        if (this.idleSoundTime-- <= 0) {
            this.idleSoundTime = 80 + this.f_107223_.nextInt(60);
            this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, ACSoundRegistry.DARK_CLOUD_IDLE.get(), SoundSource.BLOCKS, 2.0F, 1.0F, false);
        }
        this.f_107208_.addParticle(ParticleTypes.SMOKE, this.f_107212_, this.f_107213_, this.f_107214_, (double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F), (double) (this.f_107223_.nextFloat() - 0.5F));
        Entity entity = this.f_107208_.getEntity(this.targetId);
        if (entity == null || !(entity instanceof UnderzealotSacrifice)) {
            this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, ACSoundRegistry.DARK_CLOUD_DISAPPEAR.get(), SoundSource.BLOCKS, 2.0F, 1.0F, false);
            this.remove();
        }
        if (this.f_107224_ == this.f_107225_ - 10) {
            this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, ACSoundRegistry.DARK_CLOUD_DISAPPEAR.get(), SoundSource.BLOCKS, 2.0F, 1.0F, false);
        }
    }

    private void onSpawn() {
        int circleOffset = this.f_107223_.nextInt(360);
        int eyes = 3 + this.f_107223_.nextInt(2);
        this.f_107208_.playLocalSound(this.f_107212_, this.f_107213_, this.f_107214_, ACSoundRegistry.DARK_CLOUD_APPEAR.get(), SoundSource.BLOCKS, 2.0F, 1.0F, false);
        for (int j = 0; j < eyes; j++) {
            Vec3 vec3 = new Vec3((double) ((0.5F + this.f_107223_.nextFloat() * 0.7F) * this.size * 1.1F), 0.0, 0.0).yRot((float) ((double) circleOffset + (double) ((float) j / (float) eyes * 180.0F) * (Math.PI / 180.0)));
            this.f_107208_.addParticle(ACParticleRegistry.VOID_BEING_EYE.get(), this.f_107212_, this.f_107213_, this.f_107214_, vec3.x, vec3.z, 0.0);
        }
        for (int j = 0; j < this.totalTendrils; j++) {
            int timeBy = 200 / this.totalTendrils * (j + 1);
            this.f_107208_.addParticle(ACParticleRegistry.VOID_BEING_TENDRIL.get(), this.f_107212_, this.f_107213_, this.f_107214_, (double) this.targetId, (double) timeBy, 0.0);
        }
    }

    private void updateTexture() {
        int center = this.textureSize / 2;
        int black = 0;
        double alphaFadeOut = this.f_107224_ > this.f_107225_ - 10 ? (double) ((float) (this.f_107225_ - this.f_107224_) / 10.0F) : 1.0;
        double radiusSq = (double) ((float) (center * center) * getAlphaFromAge(this.f_107224_, this.f_107225_));
        for (int i = 0; i < this.textureSize; i++) {
            for (int j = 0; j < this.textureSize; j++) {
                double d0 = (double) (center - i);
                double d1 = (double) (center - j);
                double f1 = (double) ((ACMath.sampleNoise3D(i, this.f_107224_, j, 15.0F) + 1.0F) / 2.0F);
                double d2 = d0 * d0 + d1 * d1;
                double alpha = (1.0 - d2 / (radiusSq - f1 * f1 * radiusSq)) * alphaFadeOut;
                if (alpha < 0.0) {
                    this.dynamicTexture.getPixels().setPixelRGBA(j, i, 0);
                } else {
                    this.dynamicTexture.getPixels().setPixelRGBA(j, i, FastColor.ARGB32.color((int) Math.min(alpha * 255.0, 255.0), black, black, black));
                }
            }
        }
        this.dynamicTexture.upload();
    }

    public static float getAlphaFromAge(int age, int lifetime) {
        float alphaFadeIn = (float) Math.min(20, age) / 20.0F;
        float alphaFadeOut = age > lifetime - 10 ? (float) (lifetime - age) / 10.0F : 1.0F;
        return alphaFadeIn * alphaFadeOut;
    }

    @Override
    public void remove() {
        this.f_107220_ = true;
        currentlyUsedTextures--;
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        if (this.requiresUpload) {
            this.updateTexture();
            this.requiresUpload = false;
        }
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
        VertexConsumer vertexConsumer1 = multibuffersource$buffersource.getBuffer(this.renderType);
        PoseStack posestack = new PoseStack();
        PoseStack.Pose posestack$pose = posestack.last();
        Matrix3f matrix3f = posestack$pose.normal();
        float zFightFix = 0.0F;
        Vector3f vector3f1 = new Vector3f(-1.0F, -1.0F, 0.0F);
        vector3f1.rotate(quaternion);
        Vector3f[] avector3f = new Vector3f[] { new Vector3f(-1.0F, -1.0F, zFightFix), new Vector3f(-1.0F, 1.0F, zFightFix), new Vector3f(1.0F, 1.0F, zFightFix), new Vector3f(1.0F, -1.0F, zFightFix) };
        float f4 = this.size;
        for (int i = 0; i < 4; i++) {
            Vector3f vector3f = avector3f[i].add(0.0F, 0.2F * (float) Math.sin((double) (((float) this.f_107224_ + partialTick) * 0.1F)), 0.0F);
            vector3f.rotate(quaternion);
            vector3f.mul(f4);
            vector3f.add(f, f1, f2);
        }
        float f7 = 0.0F;
        float f8 = 1.0F;
        float f5 = 0.0F;
        float f6 = 1.0F;
        int j = 240;
        vertexConsumer1.vertex((double) avector3f[0].x(), (double) avector3f[0].y(), (double) avector3f[0].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f8, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex((double) avector3f[1].x(), (double) avector3f[1].y(), (double) avector3f[1].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f8, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex((double) avector3f[2].x(), (double) avector3f[2].y(), (double) avector3f[2].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f7, f5).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer1.vertex((double) avector3f[3].x(), (double) avector3f[3].y(), (double) avector3f[3].z()).color(this.f_107227_, this.f_107228_, this.f_107229_, this.f_107230_).uv(f7, f6).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(j).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        multibuffersource$buffersource.endBatch();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new VoidBeingCloudParticle(worldIn, x, y, z, (int) xSpeed, (int) ySpeed, (int) zSpeed);
        }
    }
}