package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FalloutParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    private float greenOff = 0.0F;

    private float initialColor = 0.0F;

    protected FalloutParticle(ClientLevel level, double x, double y, double z, double xMotion, double yMotion, double zMotion, SpriteSet sprites) {
        super(level, x, y, z, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.f_107226_ = 0.05F;
        this.f_172259_ = true;
        this.sprites = sprites;
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= -0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107215_ += xMotion;
        this.f_107216_ += yMotion;
        this.f_107217_ += zMotion;
        this.initialColor = level.f_46441_.nextFloat() * 0.5F;
        this.f_107227_ = this.initialColor;
        this.f_107228_ = this.initialColor;
        this.f_107229_ = this.initialColor;
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
        float greenAmount = (float) (Math.sin((double) ((float) this.f_107224_ * 0.1F) + (double) this.greenOff * Math.PI) + 1.0) * 0.5F;
        float invGreen = 1.0F - greenAmount;
        this.f_107227_ = this.initialColor * invGreen;
        this.f_107228_ = this.initialColor * invGreen + greenAmount * 0.7F;
        this.f_107229_ = this.initialColor * invGreen;
        super.m_5989_();
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    @Override
    public int getLightColor(float partialTicks) {
        float greenAmount = (float) (Math.sin((double) (((float) this.f_107224_ + partialTicks) * 0.1F) + (double) this.greenOff * Math.PI) + 1.0) * 0.5F;
        greenAmount = Math.max(0.3F, greenAmount);
        int i = super.m_6355_(partialTicks);
        int j = i & 0xFF;
        int k = i >> 16 & 0xFF;
        j += (int) (greenAmount * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FalloutParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}