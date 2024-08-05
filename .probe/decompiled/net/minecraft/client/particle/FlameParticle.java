package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class FlameParticle extends RisingParticle {

    FlameParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double double0, double double1, double double2) {
        this.m_107259_(this.m_107277_().move(double0, double1, double2));
        this.m_107275_();
    }

    @Override
    public float getQuadSize(float float0) {
        float $$1 = ((float) this.f_107224_ + float0) / (float) this.f_107225_;
        return this.f_107663_ * (1.0F - $$1 * $$1 * 0.5F);
    }

    @Override
    public int getLightColor(float float0) {
        float $$1 = ((float) this.f_107224_ + float0) / (float) this.f_107225_;
        $$1 = Mth.clamp($$1, 0.0F, 1.0F);
        int $$2 = super.m_6355_(float0);
        int $$3 = $$2 & 0xFF;
        int $$4 = $$2 >> 16 & 0xFF;
        $$3 += (int) ($$1 * 15.0F * 16.0F);
        if ($$3 > 240) {
            $$3 = 240;
        }
        return $$3 | $$4 << 16;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            FlameParticle $$8 = new FlameParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }

    public static class SmallFlameProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public SmallFlameProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            FlameParticle $$8 = new FlameParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            $$8.m_6569_(0.5F);
            return $$8;
        }
    }
}