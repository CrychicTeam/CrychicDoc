package com.github.alexthe666.alexsmobs.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleDna extends SimpleAnimatedParticle {

    private ParticleDna(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.f_107215_ = (double) ((float) motionX);
        this.f_107216_ = (double) ((float) motionY);
        this.f_107217_ = (double) ((float) motionZ);
        this.f_107663_ = this.f_107663_ * (1.5F + this.f_107223_.nextFloat() * 0.6F);
        this.f_107225_ = 15 + this.f_107223_.nextInt(15);
        this.f_107226_ = 0.1F;
        int color = 15916745;
        float lvt_18_1_ = (float) (color >> 16 & 0xFF) / 255.0F;
        float lvt_19_1_ = (float) (color >> 8 & 0xFF) / 255.0F;
        float lvt_20_1_ = (float) (color & 0xFF) / 255.0F;
        this.m_107253_(lvt_18_1_, lvt_19_1_, lvt_20_1_);
        this.m_108339_(sprites);
        this.f_107230_ = 0.0F;
        this.f_107231_ = (float) ((Math.PI / 2) * (double) this.f_107223_.nextFloat());
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
        this.f_107204_ = this.f_107231_;
        this.f_107215_ = this.f_107215_ + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 0.05F);
        this.f_107216_ = this.f_107216_ + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 0.05F);
        this.f_107217_ = this.f_107217_ + (double) ((this.f_107223_.nextFloat() - this.f_107223_.nextFloat()) * 0.05F);
        float subAlpha = 1.0F;
        if (this.f_107224_ > 5) {
            subAlpha = 1.0F - (float) (this.f_107224_ - 5) / (float) (this.m_107273_() - 5);
        }
        this.f_107230_ = subAlpha;
        this.m_108339_(this.f_107644_);
        this.f_107231_ = this.f_107231_ + (float) Math.random() * 0.9424779F * this.f_107230_;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleDna(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}