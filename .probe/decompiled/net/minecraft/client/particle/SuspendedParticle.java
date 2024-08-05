package net.minecraft.client.particle;

import java.util.Optional;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleGroup;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class SuspendedParticle extends TextureSheetParticle {

    SuspendedParticle(ClientLevel clientLevel0, SpriteSet spriteSet1, double double2, double double3, double double4) {
        super(clientLevel0, double2, double3 - 0.125, double4);
        this.m_107250_(0.01F, 0.01F);
        this.m_108335_(spriteSet1);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 0.6F + 0.2F);
        this.f_107225_ = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = false;
        this.f_172258_ = 1.0F;
        this.f_107226_ = 0.0F;
    }

    SuspendedParticle(ClientLevel clientLevel0, SpriteSet spriteSet1, double double2, double double3, double double4, double double5, double double6, double double7) {
        super(clientLevel0, double2, double3 - 0.125, double4, double5, double6, double7);
        this.m_107250_(0.01F, 0.01F);
        this.m_108335_(spriteSet1);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 0.6F + 0.6F);
        this.f_107225_ = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        this.f_107219_ = false;
        this.f_172258_ = 1.0F;
        this.f_107226_ = 0.0F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class CrimsonSporeProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public CrimsonSporeProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            RandomSource $$8 = clientLevel1.f_46441_;
            double $$9 = $$8.nextGaussian() * 1.0E-6F;
            double $$10 = $$8.nextGaussian() * 1.0E-4F;
            double $$11 = $$8.nextGaussian() * 1.0E-6F;
            SuspendedParticle $$12 = new SuspendedParticle(clientLevel1, this.sprite, double2, double3, double4, $$9, $$10, $$11);
            $$12.m_107253_(0.9F, 0.4F, 0.5F);
            return $$12;
        }
    }

    public static class SporeBlossomAirProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public SporeBlossomAirProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedParticle $$8 = new SuspendedParticle(clientLevel1, this.sprite, double2, double3, double4, 0.0, -0.8F, 0.0) {

                @Override
                public Optional<ParticleGroup> getParticleGroup() {
                    return Optional.of(ParticleGroup.SPORE_BLOSSOM);
                }
            };
            $$8.f_107225_ = Mth.randomBetweenInclusive(clientLevel1.f_46441_, 500, 1000);
            $$8.f_107226_ = 0.01F;
            $$8.m_107253_(0.32F, 0.5F, 0.22F);
            return $$8;
        }
    }

    public static class UnderwaterProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public UnderwaterProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            SuspendedParticle $$8 = new SuspendedParticle(clientLevel1, this.sprite, double2, double3, double4);
            $$8.m_107253_(0.4F, 0.4F, 0.7F);
            return $$8;
        }
    }

    public static class WarpedSporeProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public WarpedSporeProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            double $$8 = (double) clientLevel1.f_46441_.nextFloat() * -1.9 * (double) clientLevel1.f_46441_.nextFloat() * 0.1;
            SuspendedParticle $$9 = new SuspendedParticle(clientLevel1, this.sprite, double2, double3, double4, 0.0, $$8, 0.0);
            $$9.m_107253_(0.1F, 0.1F, 0.3F);
            $$9.m_107250_(0.001F, 0.001F);
            return $$9;
        }
    }
}