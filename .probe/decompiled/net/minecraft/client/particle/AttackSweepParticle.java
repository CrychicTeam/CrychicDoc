package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class AttackSweepParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    AttackSweepParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, SpriteSet spriteSet5) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.sprites = spriteSet5;
        this.f_107225_ = 4;
        float $$6 = this.f_107223_.nextFloat() * 0.6F + 0.4F;
        this.f_107227_ = $$6;
        this.f_107228_ = $$6;
        this.f_107229_ = $$6;
        this.f_107663_ = 1.0F - (float) double4 * 0.5F;
        this.m_108339_(spriteSet5);
    }

    @Override
    public int getLightColor(float float0) {
        return 15728880;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_108339_(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new AttackSweepParticle(clientLevel1, double2, double3, double4, double5, this.sprites);
        }
    }
}