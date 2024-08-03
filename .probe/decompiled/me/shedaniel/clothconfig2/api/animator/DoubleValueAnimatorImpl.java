package me.shedaniel.clothconfig2.api.animator;

import me.shedaniel.clothconfig2.impl.EasingMethod;
import net.minecraft.Util;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class DoubleValueAnimatorImpl extends NumberAnimator<Double> {

    private double amount;

    private double target;

    private long start;

    private long duration;

    DoubleValueAnimatorImpl() {
    }

    DoubleValueAnimatorImpl(double amount) {
        this.setAs(amount);
    }

    @Override
    public NumberAnimator<Double> setToNumber(Number value, long duration) {
        double doubleValue = value.doubleValue();
        if (this.target != doubleValue) {
            this.set(doubleValue, duration);
        }
        return this;
    }

    @Override
    public NumberAnimator<Double> setTargetNumber(Number value) {
        if (this.duration == 0L) {
            this.setAsNumber(value);
        } else {
            this.target = value.doubleValue();
        }
        return this;
    }

    private void set(double value, long duration) {
        this.target = value;
        this.start = Util.getMillis();
        if (duration > 0L) {
            this.duration = duration;
        } else {
            this.duration = 0L;
            this.amount = this.target;
        }
    }

    @Override
    public void update(double delta) {
        if (this.duration != 0L) {
            if (this.amount < this.target) {
                this.amount = Math.min(ease(this.amount, this.target + (this.target - this.amount), Math.min(((double) Util.getMillis() - (double) this.start) / (double) this.duration * delta * 3.0, 1.0), EasingMethod.EasingMethodImpl.LINEAR), this.target);
            } else if (this.amount > this.target) {
                this.amount = Math.max(ease(this.amount, this.target - (this.amount - this.target), Math.min(((double) Util.getMillis() - (double) this.start) / (double) this.duration * delta * 3.0, 1.0), EasingMethod.EasingMethodImpl.LINEAR), this.target);
            }
        }
    }

    private static double ease(double start, double end, double amount, EasingMethod easingMethod) {
        return start + (end - start) * easingMethod.apply(amount);
    }

    public int intValue() {
        return (int) this.amount;
    }

    public long longValue() {
        return (long) this.amount;
    }

    public float floatValue() {
        return (float) this.amount;
    }

    public double doubleValue() {
        return this.amount;
    }

    public Double target() {
        return this.target;
    }

    public Double value() {
        return this.amount;
    }
}