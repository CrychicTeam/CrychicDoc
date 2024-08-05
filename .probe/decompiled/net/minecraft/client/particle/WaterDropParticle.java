package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.SimpleParticleType;

public class WaterDropParticle extends TextureSheetParticle {

    protected WaterDropParticle(ClientLevel clientLevel0, double double1, double double2, double double3) {
        super(clientLevel0, double1, double2, double3, 0.0, 0.0, 0.0);
        this.f_107215_ *= 0.3F;
        this.f_107216_ = Math.random() * 0.2F + 0.1F;
        this.f_107217_ *= 0.3F;
        this.m_107250_(0.01F, 0.01F);
        this.f_107226_ = 0.06F;
        this.f_107225_ = (int) (8.0 / (Math.random() * 0.8 + 0.2));
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
        if (this.f_107225_-- <= 0) {
            this.m_107274_();
        } else {
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            this.f_107215_ *= 0.98F;
            this.f_107216_ *= 0.98F;
            this.f_107217_ *= 0.98F;
            if (this.f_107218_) {
                if (Math.random() < 0.5) {
                    this.m_107274_();
                }
                this.f_107215_ *= 0.7F;
                this.f_107217_ *= 0.7F;
            }
            BlockPos $$0 = BlockPos.containing(this.f_107212_, this.f_107213_, this.f_107214_);
            double $$1 = Math.max(this.f_107208_.m_8055_($$0).m_60812_(this.f_107208_, $$0).max(Direction.Axis.Y, this.f_107212_ - (double) $$0.m_123341_(), this.f_107214_ - (double) $$0.m_123343_()), (double) this.f_107208_.m_6425_($$0).getHeight(this.f_107208_, $$0));
            if ($$1 > 0.0 && this.f_107213_ < (double) $$0.m_123342_() + $$1) {
                this.m_107274_();
            }
        }
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet spriteSet0) {
            this.sprite = spriteSet0;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType0, ClientLevel clientLevel1, double double2, double double3, double double4, double double5, double double6, double double7) {
            WaterDropParticle $$8 = new WaterDropParticle(clientLevel1, double2, double3, double4);
            $$8.m_108335_(this.sprite);
            return $$8;
        }
    }
}