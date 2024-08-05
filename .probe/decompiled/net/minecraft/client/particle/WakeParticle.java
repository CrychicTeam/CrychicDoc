package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class WakeParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    WakeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.sprites = spriteSet7;
        this.f_107215_ *= 0.3F;
        this.f_107216_ = Math.random() * 0.2F + 0.1F;
        this.f_107217_ *= 0.3F;
        this.m_107250_(0.01F, 0.01F);
        this.f_107225_ = (int) (8.0 / (Math.random() * 0.8 + 0.2));
        this.m_108339_(spriteSet7);
        this.f_107226_ = 0.0F;
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        int $$0 = 60 - this.f_107225_;
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        } else {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ *= 0.98F;
            this.f_107216_ *= 0.98F;
            this.f_107217_ *= 0.98F;
            float $$1 = (float) $$0 * 0.001F;
            this.m_107250_($$1, $$1);
            this.m_108337_(this.sprites.get($$0 % 4, 4));
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new WakeParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}