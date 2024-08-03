package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class ExplodeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    protected ExplodeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3);
        this.f_107226_ = -0.1F;
        this.f_172258_ = 0.9F;
        this.sprites = spriteSet7;
        this.f_107215_ = double4 + (Math.random() * 2.0 - 1.0) * 0.05F;
        this.f_107216_ = double5 + (Math.random() * 2.0 - 1.0) * 0.05F;
        this.f_107217_ = double6 + (Math.random() * 2.0 - 1.0) * 0.05F;
        float $$8 = this.f_107223_.nextFloat() * 0.3F + 0.7F;
        this.f_107227_ = $$8;
        this.f_107228_ = $$8;
        this.f_107229_ = $$8;
        this.f_107663_ = 0.1F * (this.f_107223_.nextFloat() * this.f_107223_.nextFloat() * 6.0F + 1.0F);
        this.f_107225_ = (int) (16.0 / ((double) this.f_107223_.nextFloat() * 0.8 + 0.2)) + 2;
        this.m_108339_(spriteSet7);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.m_108339_(this.sprites);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new ExplodeParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}