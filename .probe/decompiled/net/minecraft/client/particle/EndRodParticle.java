package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class EndRodParticle extends SimpleAnimatedParticle {

    EndRodParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, spriteSet7, 0.0125F);
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
        this.f_107663_ *= 0.75F;
        this.f_107225_ = 60 + this.f_107223_.nextInt(12);
        this.m_107659_(15916745);
        this.m_108339_(spriteSet7);
    }

    @Override
    public void move(double double0, double double1, double double2) {
        this.m_107259_(this.m_107277_().move(double0, double1, double2));
        this.m_107275_();
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new EndRodParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}