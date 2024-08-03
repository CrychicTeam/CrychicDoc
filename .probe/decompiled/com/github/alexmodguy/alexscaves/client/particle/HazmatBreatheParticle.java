package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class HazmatBreatheParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private float greenOff = 0.0F;

    private float initialColor = 0.0F;

    protected HazmatBreatheParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion, SpriteSet sprites, boolean blue) {
        super(level, x, y, z, xMotion, yMotion, zMotion);
        this.f_172258_ = 0.96F;
        this.f_107226_ = -0.02F - 0.03F * level.f_46441_.nextFloat();
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107215_ *= 0.125;
        this.f_107216_ *= 0.125;
        this.f_107217_ *= 0.125;
        float initialColor = level.f_46441_.nextFloat() * 0.25F;
        float greenAmount = level.f_46441_.nextFloat() * 0.5F + 0.5F;
        this.f_107227_ = initialColor;
        this.f_107228_ = greenAmount;
        this.f_107229_ = blue ? greenAmount : initialColor;
        this.f_107663_ = this.f_107663_ * (0.75F + this.f_107223_.nextFloat() * 0.5F);
        this.f_107225_ = (int) (30.0 / ((double) level.f_46441_.nextFloat() * 0.8 + 0.2));
        this.f_107225_ = Math.max(this.f_107225_, 1);
        this.m_108339_(sprites);
        this.greenOff = this.f_107223_.nextFloat();
        this.f_107219_ = false;
    }

    @Override
    public float getQuadSize(float f) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + f) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.m_5989_();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    public static class BlueFactory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public BlueFactory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HazmatBreatheParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, true);
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HazmatBreatheParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet, false);
        }
    }
}