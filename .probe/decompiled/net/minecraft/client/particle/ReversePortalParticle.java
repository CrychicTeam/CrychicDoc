package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class ReversePortalParticle extends PortalParticle {

    ReversePortalParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6) {
        super(clientLevel0, double1, double2, double3, double4, double5, double6);
        this.f_107663_ *= 1.5F;
        this.f_107225_ = (int) (Math.random() * 2.0) + 60;
    }

    @Override
    public float getQuadSize(float float0) {
        float $$1 = 1.0F - ((float) this.f_107224_ + float0) / ((float) this.f_107225_ * 1.5F);
        return this.f_107663_ * $$1;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            float $$0 = (float) this.f_107224_ / (float) this.f_107225_;
            this.f_107212_ = this.f_107212_ + this.f_107215_ * (double) $$0;
            this.f_107213_ = this.f_107213_ + this.f_107216_ * (double) $$0;
            this.f_107214_ = this.f_107214_ + this.f_107217_ * (double) $$0;
        }
    }

    public static class ReversePortalProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public ReversePortalProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            ReversePortalParticle $$8 = new ReversePortalParticle(clientLevel1, double2, double3, double4, double5, double6, double7);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}