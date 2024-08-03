package com.github.alexthe666.alexsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleSimpleHeart extends TextureSheetParticle {

    protected ParticleSimpleHeart(ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
        this.f_107215_ *= 0.01F;
        this.f_107216_ *= 0.01F;
        this.f_107217_ *= 0.01F;
        this.f_107216_ += 0.1;
        this.f_107663_ *= 2.0F;
        this.f_107225_ = 32;
        this.f_107219_ = false;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + scaleFactor) / (float) this.f_107225_ * 16.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107213_ == this.f_107210_) {
                this.f_107215_ *= 1.1;
                this.f_107217_ *= 1.1;
            }
            this.f_107215_ *= 0.86F;
            this.f_107216_ *= 0.86F;
            this.f_107217_ *= 0.86F;
            if (this.f_107218_) {
                this.f_107215_ *= 0.7F;
                this.f_107217_ *= 0.7F;
            }
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            ParticleSimpleHeart heartparticle = new ParticleSimpleHeart(worldIn, x, y, z);
            heartparticle.m_108335_(this.spriteSet);
            return heartparticle;
        }
    }
}