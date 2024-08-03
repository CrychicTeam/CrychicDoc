package com.mna.entities.constructs.movement;

import com.mna.entities.constructs.animated.Construct;
import com.mna.tools.math.MathUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidType;

public class ConstructMoveControl extends MoveControl {

    private final Construct construct;

    private final boolean applyGravity;

    private float inWaterSpeedModifier;

    private int maxTurnX;

    private float outsideWaterSpeedModifier;

    private float maxTurnY;

    public ConstructMoveControl(Construct construct, int pMaxTurnX, int pMaxTurnY, float pInWaterSpeedModifier, float pOutsideWaterSpeedModifier, boolean pApplyGravity) {
        super(construct);
        this.construct = construct;
        this.maxTurnX = pMaxTurnX;
        this.maxTurnY = (float) pMaxTurnY;
        this.inWaterSpeedModifier = pInWaterSpeedModifier;
        this.outsideWaterSpeedModifier = pOutsideWaterSpeedModifier;
        this.applyGravity = pApplyGravity;
    }

    @Override
    public void tick() {
        if (this.construct.isInFluidType()) {
            FluidType fluidType = this.construct.getMaxHeightFluidType();
            if (fluidType != null && this.construct.canSwimInFluidType(fluidType)) {
                this.swimTick();
            } else {
                super.tick();
            }
        } else if (this.construct.canFly()) {
            this.flyTick();
        } else {
            super.tick();
        }
    }

    private void swimTick() {
        if (this.applyGravity && this.f_24974_.m_20069_()) {
            this.f_24974_.m_20256_(this.f_24974_.m_20184_().add(0.0, 0.005, 0.0));
        }
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO && !this.f_24974_.getNavigation().isDone()) {
            double d0 = this.f_24975_ - this.f_24974_.m_20185_();
            double d1 = this.f_24976_ - this.f_24974_.m_20186_();
            double d2 = this.f_24977_ - this.f_24974_.m_20189_();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d3 < 2.5000003E-7F) {
                this.f_24974_.setZza(0.0F);
            } else {
                float f = (float) (Mth.atan2(d2, d0) * 180.0F / (float) Math.PI) - 90.0F;
                this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), f, this.maxTurnY));
                this.f_24974_.f_20883_ = this.f_24974_.m_146908_();
                this.f_24974_.f_20885_ = this.f_24974_.m_146908_();
                float f1 = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED));
                if (this.f_24974_.m_20069_()) {
                    this.f_24974_.setSpeed(f1 * this.inWaterSpeedModifier);
                    double d4 = Math.sqrt(d0 * d0 + d2 * d2);
                    if (Math.abs(d1) > 1.0E-5F || Math.abs(d4) > 1.0E-5F) {
                        float f3 = -((float) (Mth.atan2(d1, d4) * 180.0F / (float) Math.PI));
                        f3 = Mth.clamp(Mth.wrapDegrees(f3), (float) (-this.maxTurnX), (float) this.maxTurnX);
                        this.f_24974_.m_146926_(this.m_24991_(this.f_24974_.m_146909_(), f3, 5.0F));
                    }
                    float f6 = Mth.cos(this.f_24974_.m_146909_() * (float) (Math.PI / 180.0));
                    float f4 = Mth.sin(this.f_24974_.m_146909_() * (float) (Math.PI / 180.0));
                    this.f_24974_.f_20902_ = f6 * f1;
                    this.f_24974_.f_20901_ = -f4 * f1;
                } else {
                    float f5 = Math.abs(Mth.wrapDegrees(this.f_24974_.m_146908_() - f));
                    float f2 = getTurningSpeedFactor(f5);
                    this.f_24974_.setSpeed(f1 * this.outsideWaterSpeedModifier * f2);
                }
            }
        } else {
            this.f_24974_.setSpeed(0.0F);
            this.f_24974_.setXxa(0.0F);
            this.f_24974_.setYya(0.0F);
            this.f_24974_.setZza(0.0F);
        }
    }

    private void flyTick() {
        if (this.f_24981_ == MoveControl.Operation.MOVE_TO) {
            this.f_24981_ = MoveControl.Operation.WAIT;
            Vec3 wanted = new Vec3(this.f_24975_, this.f_24976_, this.f_24977_);
            Vec3 pos = this.construct.m_20182_();
            Vec3 delta = wanted.subtract(pos);
            if (delta.lengthSqr() < 2.5000003E-7F) {
                this.f_24974_.setYya(0.0F);
                this.f_24974_.setZza(0.0F);
                return;
            }
            float angleToWanted = (float) (Mth.atan2(delta.z, delta.x) * 180.0F / (float) Math.PI) - 90.0F;
            this.f_24974_.m_146922_(this.m_24991_(this.f_24974_.m_146908_(), angleToWanted, 90.0F));
            float speed;
            if (this.f_24974_.m_20096_()) {
                speed = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.MOVEMENT_SPEED));
            } else {
                speed = (float) (this.f_24978_ * this.f_24974_.m_21133_(Attributes.FLYING_SPEED));
            }
            this.f_24974_.setSpeed(speed);
            double horizontalDistance = Math.sqrt(delta.x * delta.x + delta.z * delta.z);
            if (Math.abs(delta.y) > 1.0E-5F || Math.abs(horizontalDistance) > 1.0E-5F) {
                float targetYaw = (float) (-(Mth.atan2(delta.y, horizontalDistance) * 180.0F / (float) Math.PI));
                this.f_24974_.m_146926_(this.m_24991_(this.f_24974_.m_146909_(), targetYaw, (float) this.maxTurnX / 4.0F));
                if (Math.abs(this.construct.m_20186_() - (double) this.construct.m_21573_().getTargetPos().m_123342_()) > 0.5) {
                    float verticalSpeedPercent = (float) MathUtils.clamp01(Math.abs(delta.y) / 4.0);
                    float verticalSpeed = (delta.y > 0.0 ? speed : -speed) * verticalSpeedPercent;
                    this.f_24974_.setYya(verticalSpeed);
                    if (!this.construct.m_20072_()) {
                        this.f_24974_.m_20242_(true);
                    } else {
                        this.f_24974_.m_20242_(false);
                    }
                }
            } else if (Math.abs(this.construct.m_20186_() - (double) this.construct.m_21573_().getTargetPos().m_123342_()) < 1.5) {
                BlockPos cBPos = this.construct.m_20183_();
                if (this.construct.m_9236_().getBlockState(cBPos).m_60634_(this.construct.m_9236_(), cBPos, this.construct) || this.construct.m_9236_().getBlockState(cBPos.below()).m_60634_(this.construct.m_9236_(), cBPos.below(), this.construct)) {
                    this.f_24974_.m_20242_(false);
                }
            }
        } else {
            BlockPos cBPos = this.construct.m_20183_();
            if (this.construct.m_9236_().getBlockState(cBPos).m_60634_(this.construct.m_9236_(), cBPos, this.construct) || this.construct.m_9236_().getBlockState(cBPos.below()).m_60634_(this.construct.m_9236_(), cBPos.below(), this.construct)) {
                this.f_24974_.m_20242_(false);
            }
            this.f_24974_.setYya(0.0F);
            this.f_24974_.setZza(0.0F);
        }
    }

    private static float getTurningSpeedFactor(float float0) {
        return 1.0F - Mth.clamp((float0 - 10.0F) / 50.0F, 0.0F, 1.0F);
    }
}