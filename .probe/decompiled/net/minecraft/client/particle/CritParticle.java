package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class CritParticle extends TextureSheetParticle {

    CritParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.7F;
        this.f_107226_ = 0.5F;
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107215_ += double4 * 0.4;
        this.f_107216_ += double5 * 0.4;
        this.f_107217_ += double6 * 0.4;
        float $$7 = (float) (Math.random() * 0.3F + 0.6F);
        this.f_107227_ = $$7;
        this.f_107228_ = $$7;
        this.f_107229_ = $$7;
        this.f_107663_ *= 0.75F;
        this.f_107225_ = Math.max((int) (6.0 / (Math.random() * 0.8 + 0.6)), 1);
        this.f_107219_ = false;
        this.tick();
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.m_5989_();
        this.f_107228_ *= 0.96F;
        this.f_107229_ *= 0.9F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class DamageIndicatorProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public DamageIndicatorProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            CritParticle $$8 = new CritParticle(clientLevel1, double2, double3, double4, double5, double6 + 1.0, double7);
            $$8.m_107257_(20);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }

    public static class MagicProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public MagicProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            CritParticle $$8 = new CritParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.f_107227_ *= 0.3F;
            $$8.f_107228_ *= 0.8F;
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            CritParticle $$8 = new CritParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}