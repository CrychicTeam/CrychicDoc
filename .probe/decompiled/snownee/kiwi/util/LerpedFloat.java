package snownee.kiwi.util;

import net.minecraft.util.Mth;

public class LerpedFloat {

    protected LerpedFloat.Interpolator interpolator;

    protected float previousValue;

    protected float value;

    protected LerpedFloat.Chaser chaseFunction;

    protected float chaseTarget;

    protected float chaseSpeed;

    protected boolean forcedSync;

    public LerpedFloat(LerpedFloat.Interpolator interpolator) {
        this.interpolator = interpolator;
        this.startWithValue(0.0);
        this.forcedSync = true;
    }

    public static LerpedFloat linear() {
        return new LerpedFloat((p, c, t) -> (float) Mth.lerp(p, c, t));
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

    public void updateChaseTarget(float target) {
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