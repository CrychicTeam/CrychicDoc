package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class SmokeParticle extends BaseAshSmokeParticle {

    protected SmokeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, float float7, SpriteSet spriteSet8) {
        super(clientLevel0, double1, double2, double3, 0.1F, 0.1F, 0.1F, double4, double5, double6, float7, spriteSet8, 0.3F, 8, -0.1F, true);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new SmokeParticle(clientLevel1, double2, double3, double4, double5, double6, double7, 1.0F, this.sprites);
        }
    }
}