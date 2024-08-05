package net.minecraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;

public class CherryParticle extends TextureSheetParticle {

    private static final float ACCELERATION_SCALE = 0.0025F;

    private static final int INITIAL_LIFETIME = 300;

    private static final int CURVE_ENDPOINT_TIME = 300;

    private static final float FALL_ACC = 0.25F;

    private static final float WIND_BIG = 2.0F;

    private float rotSpeed;

    private final float particleRandom;

    private final float spinAcceleration;

    protected CherryParticle(ClientLevel clientLevel0, double double1, double double2, double double3, SpriteSet spriteSet4) {
        super(clientLevel0, double1, double2, double3);
        this.m_108337_(spriteSet4.get(this.f_107223_.nextInt(12), 12));
        this.rotSpeed = (float) Math.toRadians(this.f_107223_.nextBoolean() ? -30.0 : 30.0);
        this.particleRandom = this.f_107223_.nextFloat();
        this.spinAcceleration = (float) Math.toRadians(this.f_107223_.nextBoolean() ? -5.0 : 5.0);
        this.f_107225_ = 300;
        this.f_107226_ = 7.5E-4F;
        float $$5 = this.f_107223_.nextBoolean() ? 0.05F : 0.075F;
        this.f_107663_ = $$5;
        this.m_107250_($$5, $$5);
        this.f_172258_ = 1.0F;
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
        }
        if (!this.f_107220_) {
            float $$0 = (float) (300 - this.f_107225_);
            float $$1 = Math.min($$0 / 300.0F, 1.0F);
            double $$2 = Math.cos(Math.toRadians((double) (this.particleRandom * 60.0F))) * 2.0 * Math.pow((double) $$1, 1.25);
            double $$3 = Math.sin(Math.toRadians((double) (this.particleRandom * 60.0F))) * 2.0 * Math.pow((double) $$1, 1.25);
            this.f_107215_ += $$2 * 0.0025F;
            this.f_107217_ += $$3 * 0.0025F;
            this.f_107216_ = this.f_107216_ - (double) this.f_107226_;
            this.rotSpeed = this.rotSpeed + this.spinAcceleration / 20.0F;
            this.f_107204_ = this.f_107231_;
            this.f_107231_ = this.f_107231_ + this.rotSpeed / 20.0F;
            this.m_6257_(this.f_107215_, this.f_107216_, this.f_107217_);
            if (this.f_107218_ || this.f_107225_ < 299 && (this.f_107215_ == 0.0 || this.f_107217_ == 0.0)) {
                this.m_107274_();
            }
            if (!this.f_107220_) {
                this.f_107215_ = this.f_107215_ * (double) this.f_172258_;
                this.f_107216_ = this.f_107216_ * (double) this.f_172258_;
                this.f_107217_ = this.f_107217_ * (double) this.f_172258_;
            }
        }
    }
}