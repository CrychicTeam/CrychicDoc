package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class TotemParticle extends SimpleAnimatedParticle {

    TotemParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, spriteSet7, 1.25F);
        this.f_172258_ = 0.6F;
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
        this.f_107663_ *= 0.75F;
        this.f_107225_ = 60 + this.f_107223_.nextInt(12);
        this.m_108339_(spriteSet7);
        if (this.f_107223_.nextInt(4) == 0) {
            this.m_107253_(0.6F + this.f_107223_.nextFloat() * 0.2F, 0.6F + this.f_107223_.nextFloat() * 0.3F, this.f_107223_.nextFloat() * 0.2F);
        } else {
            this.m_107253_(0.1F + this.f_107223_.nextFloat() * 0.2F, 0.4F + this.f_107223_.nextFloat() * 0.3F, this.f_107223_.nextFloat() * 0.2F);
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new TotemParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}