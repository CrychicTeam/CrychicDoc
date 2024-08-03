package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class DragonBreathParticle extends TextureSheetParticle {

    private static final int COLOR_MIN = 11993298;

    private static final int COLOR_MAX = 14614777;

    private static final float COLOR_MIN_RED = 0.7176471F;

    private static final float COLOR_MIN_GREEN = 0.0F;

    private static final float COLOR_MIN_BLUE = 0.8235294F;

    private static final float COLOR_MAX_RED = 0.8745098F;

    private static final float COLOR_MAX_GREEN = 0.0F;

    private static final float COLOR_MAX_BLUE = 0.9764706F;

    private boolean hasHitGround;

    private final SpriteSet sprites;

    DragonBreathParticle(ClientLevel clientLevel0, double double1, double double2, double double3, double double4, double double5, double double6, SpriteSet spriteSet7) {
        super(clientLevel0, double1, double2, double3);
        this.f_172258_ = 0.96F;
        this.f_107215_ = double4;
        this.f_107216_ = double5;
        this.f_107217_ = double6;
        this.f_107227_ = Mth.nextFloat(this.f_107223_, 0.7176471F, 0.8745098F);
        this.f_107228_ = Mth.nextFloat(this.f_107223_, 0.0F, 0.0F);
        this.f_107229_ = Mth.nextFloat(this.f_107223_, 0.8235294F, 0.9764706F);
        this.f_107663_ *= 0.75F;
        this.f_107225_ = (int) (20.0 / ((double) this.f_107223_.nextFloat() * 0.8 + 0.2));
        this.hasHitGround = false;
        this.f_107219_ = false;
        this.sprites = spriteSet7;
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
            this.m_108339_(this.sprites);
            if (this.f_107218_) {
                this.f_107216_ = 0.0;
                this.hasHitGround = true;
            }
            if (this.hasHitGround) {
                this.f_107216_ += 0.002;
            }
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107213_ == this.f_107210_) {
                this.f_107215_ *= 1.1;
                this.f_107217_ *= 1.1;
            }
            this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
            this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
            if (this.hasHitGround) {
                this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
            }
        }
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

        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet0) {
            this.sprites = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            return new DragonBreathParticle(clientLevel1, double2, double3, double4, double5, double6, double7, this.sprites);
        }
    }
}