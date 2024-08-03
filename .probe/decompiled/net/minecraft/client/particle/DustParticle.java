package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.DustParticleOptions;

public class DustParticle extends DustParticleBase<DustParticleOptions> {

    protected DustParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, DustParticleOptions dustParticleOptions7, SpriteSet spriteSet8) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6, dustParticleOptions7, spriteSet8);
    }

    public static class Provider implements ParticleProvider<DustParticleOptions> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(DustParticleOptions dustParticleOptions0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new DustParticle(clientLevel1, double2, double3, double4, double5, double6, double7, dustParticleOptions0, this.sprites);
        }
    }
}