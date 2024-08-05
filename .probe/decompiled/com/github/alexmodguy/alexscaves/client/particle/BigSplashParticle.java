package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.client.model.SplashModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class BigSplashParticle extends Particle {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/particle/splash.png");

    private static final ResourceLocation TEXTURE_OVERLAY = new ResourceLocation("alexscaves:textures/particle/splash_overlay.png");

    private static final SplashModel MODEL = new SplashModel();

    private float scale;

    private final int waterColor;

    protected BigSplashParticle(ClientLevel level, double x, double y, double z, float scale, int lifetime) {
        super(level, x, y, z);
        this.f_107226_ = 0.0F;
        this.f_107225_ = lifetime;
        this.scale = scale;
        this.waterColor = BiomeColors.getAverageWaterColor(level, BlockPos.containing(x, y, z));
        this.m_107250_(3.0F, 3.0F);
    }

    public boolean shouldCull() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        int k = this.f_107225_ - this.f_107224_;
        if (k < 5) {
            float f = (float) k / 5.0F;
            this.m_107271_(f);
        } else {
            for (int j = 0; (float) j < this.scale * 2.0F + 1.0F; j++) {
                Vec3 sputterFrom = new Vec3((double) ((this.f_107208_.f_46441_.nextFloat() - 0.5F) * 0.1F * this.scale), -0.25, (double) ((this.f_107208_.f_46441_.nextFloat() - 0.5F) * 0.1F * this.scale)).add(this.f_107212_, this.f_107213_, this.f_107214_);
                this.f_107208_.addParticle(ACParticleRegistry.BIG_SPLASH_EFFECT.get(), sputterFrom.x, sputterFrom.y, sputterFrom.z, (double) ((this.f_107208_.f_46441_.nextFloat() - 0.5F) * 0.2F), (double) (0.3F + this.f_107208_.f_46441_.nextFloat() * 0.2F), (double) ((this.f_107208_.f_46441_.nextFloat() - 0.5F) * 0.2F));
            }
        }
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        int packedLight = this.m_6355_(partialTick);
        float colorR = (float) (this.waterColor >> 16 & 0xFF) / 255.0F;
        float colorG = (float) (this.waterColor >> 8 & 0xFF) / 255.0F;
        float colorB = (float) (this.waterColor & 0xFF) / 255.0F;
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(f, f1 - 0.5F, f2);
        posestack.scale(-this.scale, -this.scale, this.scale);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        MODEL.setupAnim(null, 0.0F, (float) this.f_107225_, (float) this.f_107224_ + partialTick, 0.0F, 0.0F);
        VertexConsumer baseConsumer = multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        MODEL.m_7695_(posestack, baseConsumer, packedLight, OverlayTexture.NO_OVERLAY, colorR, colorG, colorB, this.f_107230_);
        VertexConsumer overlayconsumer = multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(TEXTURE_OVERLAY));
        MODEL.m_7695_(posestack, overlayconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, this.f_107230_);
        multibuffersource$buffersource.endBatch();
        posestack.popPose();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            if (xSpeed == 0.0) {
                xSpeed = 1.0;
            }
            int lifetime = 5 + (int) Math.round(ySpeed * 5.0);
            return new BigSplashParticle(worldIn, x, y, z, (float) Math.max(0.5, xSpeed), lifetime);
        }
    }
}