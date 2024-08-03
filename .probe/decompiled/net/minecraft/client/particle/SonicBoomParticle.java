package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SonicBoomParticle extends HugeExplosionParticle {

    protected SonicBoomParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, SpriteSet spriteSet5) {
        super(clientLevel0, double1, double2, double3, double4, spriteSet5);
        this.f_107225_ = 16;
        this.f_107663_ = 1.5F;
        this.m_108339_(spriteSet5);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new SonicBoomParticle(clientLevel1, double2, double3, double4, double5, this.sprites);
        }
    }
}