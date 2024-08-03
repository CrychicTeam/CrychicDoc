package io.redspace.ironsspellbooks.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SnowflakeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    public SnowflakeParticle(ClientLevel level, double xCoord, double yCoord, double zCoord, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(level, xCoord, yCoord, zCoord, xd, yd, zd);
        this.f_172258_ = 0.9F;
        this.f_107215_ = xd;
        this.f_107216_ = yd;
        this.f_107217_ = zd;
        this.f_107663_ *= 0.65F;
        this.m_6569_(1.0F);
        this.f_107225_ = 50 + (int) (Math.random() * 80.0);
        this.sprites = spriteSet;
        this.f_107226_ = 0.001F;
        this.m_108339_(spriteSet);
        this.f_107227_ = 1.0F;
        this.f_107228_ = 1.0F;
        this.f_107229_ = 1.0F;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ < this.f_107225_ && !(this.f_107230_ <= 0.0F)) {
            this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 5000.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 5000.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
            this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107224_ >= this.f_107225_ - 60 && this.f_107230_ > 0.01F) {
                this.f_107230_ -= 0.015F;
            }
            if (this.f_107224_ > this.f_107225_ / 2 && this.f_107226_ < 0.225F) {
                this.f_107226_ += 0.001F;
            }
        } else {
            this.m_107274_();
        }
        this.m_108339_(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
            return new SnowflakeParticle(level, x, y, z, this.sprites, dx, dy, dz);
        }
    }
}