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

public class ParticleWormPortal extends SimpleAnimatedParticle {

    private ParticleWormPortal(ClientLevel world, double x, double y, double z, double motionX, double motionY, double motionZ, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.f_107215_ = (double) ((float) motionX);
        this.f_107216_ = (double) ((float) motionY);
        this.f_107217_ = (double) ((float) motionZ);
        this.f_107663_ = 0.35F;
        this.f_107225_ = 10 + this.f_107223_.nextInt(12);
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
        this.f_107204_ = this.f_107231_;
        this.f_107215_ *= 0.8;
        this.f_107216_ *= 0.8;
        this.f_107217_ *= 0.8;
        this.f_107231_ += 0.25F;
        this.m_108339_(this.f_107644_);
        this.f_107663_ = 0.35F * (1.0F - (float) this.f_107224_ / (float) this.f_107225_);
        this.m_107271_(1.0F - (float) this.f_107224_ / (float) this.f_107225_);
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
            return new ParticleWormPortal(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}