package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;

public class LavaParticle extends TextureSheetParticle {

    LavaParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_107226_ = 0.75F;
        this.f_172258_ = 0.999F;
        this.f_107215_ *= 0.8F;
        this.f_107216_ *= 0.8F;
        this.f_107217_ *= 0.8F;
        this.f_107216_ = (double) (this.f_107223_.nextFloat() * 0.4F + 0.05F);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 2.0F + 0.2F);
        this.f_107225_ = (int) (16.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float float0) {
        int $$1 = super.m_6355_(float0);
        int $$2 = 240;
        int $$3 = $$1 >> 16 & 0xFF;
        return 240 | $$3 << 16;
    }

    @Override
    public float getQuadSize(float float0) {
        float $$1 = ((float) this.f_107224_ + float0) / (float) this.f_107225_;
        return this.f_107663_ * (1.0F - $$1 * $$1);
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (!this.f_107220_) {
            float $$0 = (float) this.f_107224_ / (float) this.f_107225_;
            if (this.f_107223_.nextFloat() > $$0) {
                this.f_107208_.addParticle(ParticleTypes.SMOKE, this.f_107212_, this.f_107213_, this.f_107214_, this.f_107215_, this.f_107216_, this.f_107217_);
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            LavaParticle $$8 = new LavaParticle(clientLevel1, double2, double3, double4);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}