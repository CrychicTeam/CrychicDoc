package com.simibubi.create.foundation.utility.animation;

import com.simibubi.create.foundation.utility.AngleHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class LerpedFloat {

    protected LerpedFloat.Interpolator interpolator;

    protected float previousValue;

    protected float value;

    protected LerpedFloat.Chaser chaseFunction;

    protected float chaseTarget;

    protected float chaseSpeed;

    protected boolean angularChase;

    protected boolean forcedSync;

    public LerpedFloat(LerpedFloat.Interpolator interpolator) {
        this.interpolator = interpolator;
        this.startWithValue(0.0);
        this.forcedSync = true;
    }

    public static LerpedFloat linear() {
        return new LerpedFloat((p, c, t) -> (float) Mth.lerp(p, c, t));
    }

    public static LerpedFloat angular() {
        LerpedFloat lerpedFloat = new LerpedFloat(AngleHelper::angleLerp);
        lerpedFloat.angularChase = true;
        return lerpedFloat;
    }

    public LerpedFloat startWithValue(double value) {
        float f = (float) value;
        this.previousValue = f;
        this.chaseTarget = f;
        this.value = f;
        return this;
    }

    public LerpedFloat chase(double value, double speed, LerpedFloat.Chaser chaseFunction) {
        this.updateChaseTarget((float) value);
        this.chaseSpeed = (float) speed;
        this.chaseFunction = chaseFunction;
        return this;
    }

    public LerpedFloat disableSmartAngleChasing() {
        this.angularChase = false;
        return this;
    }

    public void updateChaseTarget(float target) {
        if (this.angularChase) {
            target = this.value + AngleHelper.getShortestAngleDiff((double) this.value, (double) target);
        }
        this.chaseTarget = target;
    }

    public boolean updateChaseSpeed(double speed) {
        float prevSpeed = this.chaseSpeed;
        this.chaseSpeed = (float) speed;
        return !Mth.equal((double) prevSpeed, speed);
    }

    public void tickChaser() {
        this.previousValue = this.value;
        if (this.chaseFunction != null) {
            if (Mth.equal((double) this.value, (double) this.chaseTarget)) {
                this.value = this.chaseTarget;
            } else {
                this.value = this.chaseFunction.chase((double) this.value, (double) this.chaseSpeed, (double) this.chaseTarget);
            }
        }
    }

    public void setValueNoUpdate(double value) {
        this.value = (float) value;
    }

    public void setValue(double value) {
        this.previousValue = this.value;
        this.value = (float) value;
    }

    public float getValue() {
        return this.getValue(1.0F);
    }

    public float getValue(float partialTicks) {
        return this.interpolator.interpolate((double) partialTicks, (double) this.previousValue, (double) this.value);
    }

    public boolean settled() {
        return Mth.equal((double) this.previousValue, (double) this.value) && (this.chaseFunction == null || Mth.equal((double) this.value, (double) this.chaseTarget));
    }

    public float getChaseTarget() {
        return this.chaseTarget;
    }

    public void forceNextSync() {
        this.forcedSync = true;
    }

    public CompoundTag writeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        compoundNBT.putFloat("Speed", this.chaseSpeed);
        compoundNBT.putFloat("Target", this.chaseTarget);
        compoundNBT.putFloat("Value", this.value);
        if (this.forcedSync) {
            compoundNBT.putBoolean("Force", true);
        }
        this.forcedSync = false;
        return compoundNBT;
    }

    public void readNBT(CompoundTag compoundNBT, boolean clientPacket) {
        if (!clientPacket || compoundNBT.contains("Force")) {
            this.startWithValue((double) compoundNBT.getFloat("Value"));
        }
        this.readChaser(compoundNBT);
    }

    protected void readChaser(CompoundTag compoundNBT) {
        this.chaseSpeed = compoundNBT.getFloat("Speed");
        this.chaseTarget = compoundNBT.getFloat("Target");
    }

    @FunctionalInterface
    public interface Chaser {

        LerpedFloat.Chaser IDLE = (c, s, t) -> (float) c;

        LerpedFloat.Chaser EXP = exp(Double.MAX_VALUE);

        LerpedFloat.Chaser LINEAR = (c, s, t) -> (float) (c + Mth.clamp(t - c, -s, s));

        static LerpedFloat.Chaser exp(double maxEffectiveSpeed) {
            return (c, s, t) -> (float) (c + Mth.clamp((t - c) * s, -maxEffectiveSpeed, maxEffectiveSpeed));
        }

        float chase(double var1, double var3, double var5);
    }

    @FunctionalInterface
    public interface Interpolator {

        float interpolate(double var1, double var3, double var5);
    }
}