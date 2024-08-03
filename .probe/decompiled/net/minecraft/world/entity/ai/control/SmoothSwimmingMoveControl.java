package net.minecraft.world.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SmoothSwimmingMoveControl extends MoveControl {

    private static final float FULL_SPEED_TURN_THRESHOLD = 10.0F;

    private static final float STOP_TURN_THRESHOLD = 60.0F;

    private final int maxTurnX;

    private final int maxTurnY;

    private final float inWaterSpeedModifier;

    private final float outsideWaterSpeedModifier;

    private final boolean applyGravity;

    public SmoothSwimmingMoveControl(Mob mob0, int int1, int int2, float float3, float float4, boolean boolean5) {
        super(mob0);
        this.maxTurnX = int1;
        this.maxTurnY = int2;
        this.inWaterSpeedModifier = float3;
        this.outsideWaterSpeedModifier = float4;
        this.applyGravity = boolean5;
    }

    @Override
    public void tick() {
        if (this.applyGravity && this.f_24974_.m_20069_()) {
            this.f_24974_.m_20256_(this.f_24974_.m_20184_().add(0.0, 0.005, 0.0));
        }
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.f_24974_.getNavigation().isDone()) {
            double $$0 = this.f_24975_ - this.f_24974_.m_20185_();
            double $$1 = this.f_24976_ - this.f_24974_.m_20186_();
            double $$2 = this.f_24977_ - this.f_24974_.m_20189_();
            double $$3 = $$0 * $$0 + $$1 * $$1 + $$2 * $$2;
            if ($$3 < 2.5000003E-7F) {
                this.f_24974_.setZza(0.0F);
            } else {
                float $$4 = (float) (Mth.atan2($$2, $$0) * 180.0F / (float) Math.PI) - 90.0F;
                this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), $$4, (float) this.maxTurnY));
                this.f_24974_.f_20883_ = this.f_24974_.m_146908_();
                this.f_24974_.f_20885_ = this.f_24974_.m_146908_();
                float $$5 = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED));
                if (this.f_24974_.m_20069_()) {
                    this.f_24974_.setSpeed($$5 * this.inWaterSpeedModifier);
                    double $$6 = Math.sqrt($$0 * $$0 + $$2 * $$2);
                    if (Math.abs($$1) > 1.0E-5F || Math.abs($$6) > 1.0E-5F) {
                        float $$7 = -((float) (Mth.atan2($$1, $$6) * 180.0F / (float) Math.PI));
                        $$7 = Mth.clamp(Mth.wrapDegrees($$7), (float) (-this.maxTurnX), (float) this.maxTurnX);
                        this.f_24974_.m_146926_(this.m_24991_(this.f_24974_.m_146909_(), $$7, 5.0F));
                    }
                    float $$8 = Mth.cos(this.f_24974_.m_146909_() * (float) (Math.PI / 180.0));
                    float $$9 = Mth.sin(this.f_24974_.m_146909_() * (float) (Math.PI / 180.0));
                    this.f_24974_.f_20902_ = $$8 * $$5;
                    this.f_24974_.f_20901_ = -$$9 * $$5;
                } else {
                    float $$10 = Math.abs(Mth.wrapDegrees(this.f_24974_.m_146908_() - $$4));
                    float $$11 = getTurningSpeedFactor($$10);
                    this.f_24974_.setSpeed($$5 * this.outsideWaterSpeedModifier * $$11);
                }
            }
        } else {
            this.f_24974_.setSpeed(0.0F);
            this.f_24974_.setXxa(0.0F);
            this.f_24974_.setYya(0.0F);
            this.f_24974_.setZza(0.0F);
        }
    }

    private static float getTurningSpeedFactor(float float0) {
        return 1.0F - Mth.clamp((float0 - 10.0F) / 50.0F, 0.0F, 1.0F);
    }
}