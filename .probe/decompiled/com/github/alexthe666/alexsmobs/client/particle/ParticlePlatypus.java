package com.github.alexthe666.alexsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticlePlatypus extends SimpleAnimatedParticle {

    private ParticlePlatypus(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.f_107215_ = (double) ((float) motionX);
        this.f_107216_ = (double) ((float) motionY);
        this.f_107217_ = (double) ((float) motionZ);
        this.f_107663_ = this.f_107663_ * (0.2F + this.f_107223_.nextFloat() * 0.6F);
        this.f_107225_ = 3 + this.f_107223_.nextInt(2);
        this.f_107226_ = 0.0F;
        this.m_108339_(sprites);
    }

    @Override
    public int getLightColor(float p_189214_1_) {
        int lvt_2_1_ = super.getLightColor(p_189214_1_);
        int lvt_4_1_ = lvt_2_1_ >> 16 & 0xFF;
        return 240 | lvt_4_1_ << 16;
    }

    @Override
    public void tick() {
        super.tick();
        this.f_107215_ *= 0.8;
        this.f_107216_ *= 0.8;
        this.f_107217_ *= 0.8;
        double lvt_1_1_ = this.f_107212_ - this.f_107209_;
        double lvt_5_1_ = this.f_107214_ - this.f_107211_;
        float lvt_9_1_ = (float) (Mth.atan2(lvt_5_1_, lvt_1_1_) * 180.0F / (float) Math.PI) - 180.0F;
        this.f_107231_ = lvt_9_1_;
        this.f_107204_ = this.f_107231_;
        this.m_108339_(this.f_107644_);
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
            return new ParticlePlatypus(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}