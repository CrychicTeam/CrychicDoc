package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

public class SquidInkParticle extends SimpleAnimatedParticle {

    SquidInkParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, int int7, SpriteSet spriteSet8) {
        super(clientLevel0, double1, double2, double3, spriteSet8, 0.0F);
        this.f_172258_ = 0.92F;
        this.f_107663_ = 0.5F;
        this.m_107271_(1.0F);
        this.m_107253_((float) FastColor.ARGB32.red(int7), (float) FastColor.ARGB32.green(int7), (float) FastColor.ARGB32.blue(int7));
        this.f_107225_ = (int) ((double) (this.f_107663_ * 12.0F) / (Math.random() * 0.8F + 0.2F));
        this.m_108339_(spriteSet8);
        this.f_107219_ = false;
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_107220_) {
            this.m_108339_(this.f_107644_);
            if (this.f_107224_ > this.f_107225_ / 2) {
                this.m_107271_(1.0F - ((float) this.f_107224_ - (float) (this.f_107225_ / 2)) / (float) this.f_107225_);
            }
            if (this.f_107208_.m_8055_(BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_)).m_60795_()) {
                this.f_107216_ -= 0.0074F;
            }
        }
    }

    public static class GlowInkProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public GlowInkProvider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new SquidInkParticle(clientLevel1, double2, double3, double4, double5, double6, double7, FastColor.ARGB32.color(255, 204, 31, 102), this.sprites);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new SquidInkParticle(clientLevel1, double2, double3, double4, double5, double6, double7, FastColor.ARGB32.color(255, 255, 255, 255), this.sprites);
        }
    }
}