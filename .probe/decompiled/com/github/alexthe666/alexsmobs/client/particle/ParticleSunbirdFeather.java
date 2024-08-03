package com.github.alexthe666.alexsmobs.client.particle;

import com.github.alexthe666.alexsmobs.entity.util.Maths;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class ParticleSunbirdFeather extends SimpleAnimatedParticle {

    private float initialRoll = 0.0F;

    private ParticleSunbirdFeather(ClientLevel world, double x, double y, double z, double xd, double yd, double zd, SpriteSet sprites) {
        super(world, x, y, z, sprites, 0.0F);
        this.f_172258_ = 0.96F;
        this.f_172259_ = true;
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.f_107663_ = 0.15F + this.f_107223_.nextFloat() * 0.2F;
        this.f_107225_ = 20 + this.f_107223_.nextInt(20);
        this.f_107226_ = 0.02F;
        this.m_108335_(sprites);
        float f = Mth.sqrt((float) (xd * xd + zd * zd));
        float f1 = -((float) Mth.atan2(yd, (double) f)) + Maths.rad(135.0);
        this.f_107231_ = f1 * 2.0F;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        this.f_107215_ *= 0.8;
        this.f_107216_ *= 0.8;
        this.f_107217_ *= 0.8;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.f_107204_ = this.f_107231_;
            if (!this.f_107218_) {
                this.f_107231_ = this.f_107231_ + 0.0F + (float) Math.sin((double) ((float) this.f_107224_ * 0.3F)) * 0.5F * ((float) this.f_107224_ / (float) this.f_107225_);
            }
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
        }
    }

    @Override
    public int getLightColor(float p_189214_1_) {
        int lvt_2_1_ = super.getLightColor(p_189214_1_);
        int lvt_4_1_ = lvt_2_1_ >> 16 & 0xFF;
        return 240 | lvt_4_1_ << 16;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleSunbirdFeather(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, this.spriteSet);
        }
    }
}