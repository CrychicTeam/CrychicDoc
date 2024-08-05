package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class WhiteAshParticle extends BaseAshSmokeParticle {

    private static final int COLOR_RGB24 = 12235202;

    protected WhiteAshParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, float float7, SpriteSet spriteSet8) {
        super(clientLevel0, double1, double2, double3, 0.1F, -0.1F, 0.1F, double4, double5, double6, float7, spriteSet8, 0.0F, 20, 0.0125F, false);
        this.f_107227_ = 0.7294118F;
        this.f_107228_ = 0.69411767F;
        this.f_107229_ = 0.7607843F;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            RandomSource $$8 = clientLevel1.f_46441_;
            double $$9 = (double) $$8.nextFloat() * -1.9 * (double) $$8.nextFloat() * 0.1;
            double $$10 = (double) $$8.nextFloat() * -0.5 * (double) $$8.nextFloat() * 0.1 * 5.0;
            double $$11 = (double) $$8.nextFloat() * -1.9 * (double) $$8.nextFloat() * 0.1;
            return new WhiteAshParticle(clientLevel1, double2, double3, double4, $$9, $$10, $$11, 1.0F, this.sprites);
        }
    }
}