package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class CampfireSmokeParticle extends TextureSheetParticle {

    CampfireSmokeParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, boolean boolean7) {
        super(clientLevel0, double1, double2, double3);
        this.m_6569_(3.0F);
        this.m_107250_(0.25F, 0.25F);
        if (boolean7) {
            this.f_107225_ = this.f_107223_.nextInt(50) + 280;
        } else {
            this.f_107225_ = this.f_107223_.nextInt(50) + 80;
        }
        this.f_107226_ = 3.0E-6F;
        this.f_107215_ = double4;
        this.f_107216_ = double5 + (double) (this.f_107223_.nextFloat() / 500.0F);
        this.f_107217_ = double6;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ < this.f_107225_ && !(this.f_107230_ <= 0.0F)) {
            this.f_107215_ = this.f_107215_ + (double) (this.f_107223_.nextFloat() / 5000.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107217_ = this.f_107217_ + (double) (this.f_107223_.nextFloat() / 5000.0F * (float) (this.f_107223_.nextBoolean() ? 1 : -1));
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107224_ >= this.f_107225_ - 60 && this.f_107230_ > 0.01F) {
                this.f_107230_ -= 0.015F;
            }
        } else {
            this.m_107274_();
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class CosyProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public CosyProvider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            CampfireSmokeParticle $$8 = new CampfireSmokeParticle(clientLevel1, double2, double3, double4, double5, double6, double7, false);
            $$8.m_107271_(0.9F);
            $$8.m_108335_(this.sprites);
            return $$8;
        }
    }

    public static class SignalProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public SignalProvider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            CampfireSmokeParticle $$8 = new CampfireSmokeParticle(clientLevel1, double2, double3, double4, double5, double6, double7, true);
            $$8.m_107271_(0.95F);
            $$8.m_108335_(this.sprites);
            return $$8;
        }
    }
}