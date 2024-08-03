package com.github.alexmodguy.alexscaves.client.particle;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.MushroomCloudModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.client.sound.NuclearExplosionSound;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class MushroomCloudParticle extends Particle {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/particle/mushroom_cloud.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/particle/mushroom_cloud_glow.png");

    private static final MushroomCloudModel MODEL = new MushroomCloudModel();

    private static final int BALL_FOR = 10;

    private static final int GLOW_FOR = 20;

    private static final int FADE_SPEED = 10;

    private final float scale;

    private boolean playedRinging;

    private boolean playedExplosion;

    private boolean playedRumble;

    protected MushroomCloudParticle(ClientLevel level, double x, double y, double z, float scale) {
        super(level, x, y, z);
        this.f_107226_ = 0.0F;
        this.f_107225_ = (int) Math.ceil((double) (133.33F * scale));
        this.scale = scale + 0.2F;
        this.m_107250_(3.0F, 3.0F);
    }

    public boolean shouldCull() {
        return false;
    }

    @Override
    public void tick() {
        ClientProxy.renderNukeSkyDarkFor = 70;
        ClientProxy.muteNonNukeSoundsFor = 50;
        boolean large = this.scale > 2.0F;
        if (this.f_107224_ > 10 && !this.playedExplosion) {
            this.playedExplosion = true;
            this.playSound(large ? ACSoundRegistry.LARGE_NUCLEAR_EXPLOSION.get() : ACSoundRegistry.NUCLEAR_EXPLOSION.get(), this.f_107225_ - 20, this.f_107225_, 0.2F, false);
        }
        if (this.f_107224_ < 10) {
            if (!this.playedRinging && AlexsCaves.CLIENT_CONFIG.nuclearBombFlash.get()) {
                this.playedRinging = true;
                this.playSound(ACSoundRegistry.NUCLEAR_EXPLOSION_RINGING.get(), 100, 50, 0.05F, true);
            }
            ClientProxy.renderNukeFlashFor = 16;
        } else if (this.f_107224_ < this.f_107225_ - 10) {
            float life = (float) Math.log((double) (1.0F + (float) (this.f_107224_ - 10) / (float) (this.f_107225_ - 10))) * 2.0F;
            float explosionSpread = (12.0F * life + 4.0F) * this.scale;
            for (int i = 0; (float) i < (float) (1 + this.f_107223_.nextInt(2)) * this.scale; i++) {
                Vec3 from = new Vec3((double) (this.f_107208_.f_46441_.nextFloat() - 0.5F), (double) (this.f_107208_.f_46441_.nextFloat() - 0.5F), (double) (this.f_107208_.f_46441_.nextFloat() - 0.5F)).scale((double) (this.scale * 1.4F)).add(this.f_107212_, this.f_107213_, this.f_107214_);
                Vec3 away = new Vec3((double) (this.f_107208_.f_46441_.nextFloat() - 0.5F), (double) (this.f_107208_.f_46441_.nextFloat() - 0.5F), (double) (this.f_107208_.f_46441_.nextFloat() - 0.5F)).scale(2.34F);
                this.f_107208_.addParticle(ACParticleRegistry.MUSHROOM_CLOUD_SMOKE.get(), from.x, from.y, from.z, away.x, away.y, away.z);
            }
            for (int j = 0; (float) j < this.scale * this.scale; j++) {
                Vec3 explosionBase = new Vec3((double) ((this.f_107208_.f_46441_.nextFloat() - 0.5F) * explosionSpread), (double) ((-0.6F + this.f_107208_.f_46441_.nextFloat() * 0.5F) * explosionSpread * 0.1F), (double) ((this.f_107208_.f_46441_.nextFloat() - 0.5F) * explosionSpread)).add(this.f_107212_, this.f_107213_, this.f_107214_);
                this.f_107208_.addParticle(ACParticleRegistry.MUSHROOM_CLOUD_EXPLOSION.get(), explosionBase.x, explosionBase.y, explosionBase.z, 0.0, 0.0, 0.0);
            }
            if (this.f_107224_ > 10 && !this.playedRumble) {
                this.playedRumble = true;
                this.playSound(ACSoundRegistry.NUCLEAR_EXPLOSION_RUMBLE.get(), this.f_107225_ + 100, this.f_107225_, 0.1F, true);
            }
        }
        super.tick();
    }

    private void playSound(SoundEvent soundEvent, int duration, int fadesAt, float fadeInBy, boolean looping) {
        Minecraft.getInstance().getSoundManager().queueTickingSound(new NuclearExplosionSound(soundEvent, this.f_107212_, this.f_107213_, this.f_107214_, duration, fadesAt, fadeInBy, looping));
    }

    @Override
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTick) {
        Vec3 vec3 = camera.getPosition();
        float f = (float) (Mth.lerp((double) partialTick, this.f_107209_, this.f_107212_) - vec3.x());
        float f1 = (float) (Mth.lerp((double) partialTick, this.f_107210_, this.f_107213_) - vec3.y());
        float f2 = (float) (Mth.lerp((double) partialTick, this.f_107211_, this.f_107214_) - vec3.z());
        PoseStack posestack = new PoseStack();
        posestack.pushPose();
        posestack.translate(f, f1 - 0.5F, f2);
        posestack.scale(-this.scale, -this.scale, this.scale);
        MultiBufferSource.BufferSource multibuffersource$buffersource = Minecraft.getInstance().renderBuffers().bufferSource();
        MODEL.hideFireball(this.f_107224_ >= 10);
        float life = (float) Math.log((double) (1.0F + ((float) (this.f_107224_ - 10) + partialTick) / (float) (this.f_107225_ - 10))) * 2.0F;
        float glowLife = life < 1.0F ? 1.0F - life : 0.0F;
        int left = this.f_107225_ - this.f_107224_;
        float alpha = left <= 10 ? (float) left / 10.0F : 1.0F;
        MODEL.animateParticle((float) this.f_107224_, ACMath.smin(life, 1.0F, 0.5F), partialTick);
        VertexConsumer baseConsumer = multibuffersource$buffersource.getBuffer(RenderType.entityTranslucent(TEXTURE));
        MODEL.m_7695_(posestack, baseConsumer, this.m_6355_(partialTick), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        VertexConsumer glowConsumer1 = multibuffersource$buffersource.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE));
        MODEL.m_7695_(posestack, glowConsumer1, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, alpha);
        VertexConsumer glowConsumer2 = multibuffersource$buffersource.getBuffer(ACRenderTypes.getEyesAlphaEnabled(TEXTURE_GLOW));
        MODEL.m_7695_(posestack, glowConsumer2, 240, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, glowLife * alpha);
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
            return new MushroomCloudParticle(worldIn, x, y, z, (float) Math.max(0.5, xSpeed));
        }
    }
}