package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;

public class WaterCurrentDownParticle extends TextureSheetParticle {

    private float angle;

    WaterCurrentDownParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3);
        this.f_107225_ = (int) (Math.random() * 60.0) + 30;
        this.f_107219_ = false;
        this.f_107215_ = 0.0;
        this.f_107216_ = -0.05;
        this.f_107217_ = 0.0;
        this.m_107250_(0.02F, 0.02F);
        this.f_107663_ = this.f_107663_ * (this.f_107223_.nextFloat() * 0.6F + 0.2F);
        this.f_107226_ = 0.002F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.f_107209_ = this.f_107212_;
        this.f_107210_ = this.f_107213_;
        this.f_107211_ = this.f_107214_;
        if (this.f_107224_++ >= this.f_107225_) {
            this.m_107274_();
        } else {
            float $$0 = 0.6F;
            this.f_107215_ = this.f_107215_ + (double) (0.6F * Mth.cos(this.angle));
            this.f_107217_ = this.f_107217_ + (double) (0.6F * Mth.sin(this.angle));
            this.f_107215_ *= 0.07;
            this.f_107217_ *= 0.07;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (!this.f_107208_.m_6425_(BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_)).is(FluidTags.WATER) || this.f_107218_) {
                this.m_107274_();
            }
            this.angle += 0.08F;
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            WaterCurrentDownParticle $$8 = new WaterCurrentDownParticle(clientLevel1, double2, double3, double4);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}