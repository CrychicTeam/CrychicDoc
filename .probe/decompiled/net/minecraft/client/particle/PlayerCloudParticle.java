package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PlayerCloudParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    PlayerCloudParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.96F;
        this.sprites = spriteSet7;
        float $$8 = 2.5F;
        this.f_107215_ *= 0.1F;
        this.f_107216_ *= 0.1F;
        this.f_107217_ *= 0.1F;
        this.f_107215_ += double4;
        this.f_107216_ += double5;
        this.f_107217_ += double6;
        float $$9 = 1.0F - (float) (Math.random() * 0.3F);
        this.f_107227_ = $$9;
        this.f_107228_ = $$9;
        this.f_107229_ = $$9;
        this.f_107663_ *= 1.875F;
        int $$10 = (int) (8.0 / (Math.random() * 0.8 + 0.3));
        this.f_107225_ = (int) Math.max((float) $$10 * 2.5F, 1.0F);
        this.f_107219_ = false;
        this.m_108339_(spriteSet7);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    @Override
    public void tick() {
        super.m_5989_();
        if (!this.f_107220_) {
            this.m_108339_(this.sprites);
            Player $$0 = this.f_107208_.m_45924_(this.f_107212_, this.f_107213_, this.f_107214_, 2.0, false);
            if ($$0 != null) {
                double $$1 = $$0.m_20186_();
                if (this.f_107213_ > $$1) {
                    this.f_107213_ = this.f_107213_ + ($$1 - this.f_107213_) * 0.2;
                    this.f_107216_ = this.f_107216_ + ($$0.m_20184_().y - this.f_107216_) * 0.2;
                    this.m_107264_(this.f_107212_, this.f_107213_, this.f_107214_);
                }
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new PlayerCloudParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }

    public static class SneezeProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public SneezeProvider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            Particle $$8 = new PlayerCloudParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
            $$8.setColor(200.0F, 50.0F, 120.0F);
            $$8.setAlpha(0.4F);
            return $$8;
        }
    }
}