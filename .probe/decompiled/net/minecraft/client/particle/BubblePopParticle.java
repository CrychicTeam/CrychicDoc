package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;

public class BubblePopParticle extends TextureSheetParticle {

    private final SpriteSet sprites;

    BubblePopParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3);
        this.sprites = spriteSet7;
        this.f_107225_ = 4;
        this.f_107226_ = 0.008F;
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
        this.m_108339_(spriteSet7);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.m_108339_(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new BubblePopParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}