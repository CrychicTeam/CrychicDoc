package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class NoteParticle extends TextureSheetParticle {

    NoteParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_172258_ = 0.66F;
        this.f_172259_ = true;
        this.f_107215_ *= 0.01F;
        this.f_107216_ *= 0.01F;
        this.f_107217_ *= 0.01F;
        this.f_107216_ += 0.2;
        this.f_107227_ = Math.max(0.0F, Mth.sin(((float) double4 + 0.0F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
        this.f_107228_ = Math.max(0.0F, Mth.sin(((float) double4 + 0.33333334F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
        this.f_107229_ = Math.max(0.0F, Mth.sin(((float) double4 + 0.6666667F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
        this.f_107663_ *= 1.5F;
        this.f_107225_ = 6;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float float0) {
        return this.f_107663_ * Mth.clamp(((float) this.f_107224_ + float0) / (float) this.f_107225_ * 32.0F, 0.0F, 1.0F);
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            NoteParticle $$8 = new NoteParticle(clientLevel1, double2, double3, double4, double5);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}