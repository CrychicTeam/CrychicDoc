package com.github.alexmodguy.alexscaves.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class RadgillSplashParticle extends TextureSheetParticle {

    private boolean fallLeft;

    public RadgillSplashParticle(ClientLevel world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(world, x, y, z);
        this.m_108335_(sprites);
        this.f_107225_ = 10 + this.f_107223_.nextInt(10);
        this.f_107663_ = 0.1F + this.f_107223_.nextFloat() * 0.3F;
        this.f_107226_ = 0.1F;
        this.f_107231_ = 0.0F;
        this.fallLeft = this.f_107223_.nextBoolean();
        this.f_107215_ = xSpeed;
        this.f_107216_ = ySpeed;
        this.f_107217_ = zSpeed;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107204_ = this.f_107231_;
        float downRollTarget = (float) Math.PI;
        if (this.fallLeft) {
            if (this.f_107216_ < 0.0 && this.f_107231_ > -downRollTarget) {
                this.f_107231_ = Math.min(this.f_107231_ - downRollTarget * 0.2F, -downRollTarget);
            }
        } else if (this.f_107216_ < 0.0 && this.f_107231_ < downRollTarget) {
            this.f_107231_ = Math.min(this.f_107231_ + downRollTarget * 0.2F, downRollTarget);
        }
        this.f_107231_ += 0.0F;
        this.f_107224_++;
        if (this.f_107224_ < this.f_107225_ && (this.f_107210_ != this.f_107213_ || this.f_107224_ <= 3)) {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
        } else {
            this.m_107274_();
        }
    }

    @Override
    public int getLightColor(float partialTicks) {
        return 240;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new RadgillSplashParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}