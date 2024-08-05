package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class LargeSmokeParticle extends SmokeParticle {

    protected LargeSmokeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6, 2.5F, spriteSet7);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new LargeSmokeParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}