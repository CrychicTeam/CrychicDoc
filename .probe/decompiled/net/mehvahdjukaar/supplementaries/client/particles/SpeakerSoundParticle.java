package net.mehvahdjukaar.supplementaries.client.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class SpeakerSoundParticle extends TextureSheetParticle {

    protected SpeakerSoundParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0, 0.0, 0.0);
        this.f_107215_ = 0.0;
        this.f_107216_ = 0.0;
        this.f_107217_ = 0.0;
        this.f_107227_ = Math.max(0.0F, Mth.sin(((float) xSpeedIn + 0.0F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
        this.f_107228_ = Math.max(0.0F, Mth.sin(((float) xSpeedIn + 0.33333334F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
        this.f_107229_ = Math.max(0.0F, Mth.sin(((float) xSpeedIn + 0.6666667F) * (float) (Math.PI * 2)) * 0.65F + 0.35F);
        this.f_107663_ *= 1.5F;
        this.f_107225_ = 10;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getQuadSize(float scaleFactor) {
        float i = 0.2F;
        return this.f_107663_ * (Mth.sin((float) (Math.PI * 3) * ((float) this.f_107224_ / (float) this.f_107225_)) * i + 1.0F - i / 2.0F);
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107213_ == this.f_107210_) {
                this.f_107215_ *= 1.1;
                this.f_107217_ *= 1.1;
            }
            this.f_107215_ *= 0.66;
            this.f_107216_ *= 0.66;
            this.f_107217_ *= 0.66;
            if (this.f_107218_) {
                this.f_107215_ *= 0.7;
                this.f_107217_ *= 0.7;
            }
        }
    }

    public static class Factory implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet spriteSet;

        public Factory(SpriteSet sprite) {
            this.spriteSet = sprite;
        }

        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            SpeakerSoundParticle op = new SpeakerSoundParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed);
            op.m_108335_(this.spriteSet);
            return op;
        }
    }
}