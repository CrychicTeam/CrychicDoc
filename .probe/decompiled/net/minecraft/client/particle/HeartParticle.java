package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class HeartParticle extends TextureSheetParticle {

    HeartParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_172259_ = true;
        this.f_172258_ = 0.86F;
        this.f_107215_ *= 0.01F;
        this.f_107216_ *= 0.01F;
        this.f_107217_ *= 0.01F;
        this.f_107216_ += 0.1;
        this.f_107663_ *= 1.5F;
        this.f_107225_ = 16;
        this.f_107219_ = false;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    public static class AngryVillagerProvider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public AngryVillagerProvider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            HeartParticle $$8 = new HeartParticle(clientLevel1, double2, double3 + 0.5, double4);
            $$8.m_108335_(this.sprite);
            $$8.m_107253_(1.0F, 1.0F, 1.0F);
            return $$8;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            HeartParticle $$8 = new HeartParticle(clientLevel1, double2, double3, double4);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}