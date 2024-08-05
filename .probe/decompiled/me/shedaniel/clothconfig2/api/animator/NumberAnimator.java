package me.shedaniel.clothconfig2.api.animator;

import java.util.function.Supplier;

public abstract class NumberAnimator<T extends Number> extends Number implements ValueAnimator<T> {

    public NumberAnimator<Double> asDouble() {
        return new NumberAnimatorWrapped<>(this, Number::doubleValue);
    }

    public NumberAnimator<Float> asFloat() {
        return new NumberAnimatorWrapped<>(this, Number::floatValue);
    }

    public NumberAnimator<Integer> asInt() {
        return new NumberAnimatorWrapped<>(this, d -> (int) Math.round(d.doubleValue()));
    }

    public NumberAnimator<Long> asLong() {
        return new NumberAnimatorWrapped<>(this, d -> Math.round(d.doubleValue()));
    }

    public NumberAnimator<T> setAs(T value) {
        ValueAnimator.super.setAs(value);
        return this;
    }

    public NumberAnimator<T> setAs(int value) {
        this.setAsNumber(value);
        return this;
    }

    public NumberAnimator<T> setAs(long value) {
        this.setAsNumber(value);
        return this;
    }

    public NumberAnimator<T> setAs(float value) {
        this.setAsNumber(value);
        return this;
    }

    public NumberAnimator<T> setAs(double value) {
        this.setAsNumber(value);
        return this;
    }

    public NumberAnimator<T> setTo(T value, long duration) {
        this.setToNumber(value, duration);
        return this;
    }

    public NumberAnimator<T> setTo(int value, long duration) {
        this.setToNumber(value, duration);
        return this;
    }

    public NumberAnimator<T> setTo(long value, long duration) {
        this.setToNumber(value, duration);
        return this;
    }

    public NumberAnimator<T> setTo(float value, long duration) {
        this.setToNumber(value, duration);
        return this;
    }

    public NumberAnimator<T> setTo(double value, long duration) {
        this.setToNumber(value, duration);
        return this;
    }

    public NumberAnimator<T> setAsNumber(Number value) {
        return this.setToNumber(value, -1L);
    }

    public abstract NumberAnimator<T> setToNumber(Number var1, long var2);

    public ValueAnimator<T> setTarget(T target) {
        this.setTargetNumber(target);
        return this;
    }

    public NumberAnimator<T> setTarget(int value) {
        this.setTargetNumber(value);
        return this;
    }

    public NumberAnimator<T> setTarget(long value) {
        this.setTargetNumber(value);
        return this;
    }

    public NumberAnimator<T> setTarget(float value) {
        this.setTargetNumber(value);
        return this;
    }

    public NumberAnimator<T> setTarget(double value) {
        this.setTargetNumber(value);
        return this;
    }

    public abstract NumberAnimator<T> setTargetNumber(Number var1);

    public NumberAnimator<T> withConvention(Supplier<T> convention, long duration) {
        ValueAnimator<T> parentConvention = ValueAnimator.super.withConvention(convention, duration);
        return new ValueAnimatorAsNumberAnimator<T>(parentConvention) {

            @Override
            public NumberAnimator<T> setToNumber(Number value, long duration) {
                return NumberAnimator.this.setToNumber(value, duration);
            }

            @Override
            public NumberAnimator<T> setTargetNumber(Number value) {
                return NumberAnimator.this.setTargetNumber(value);
            }
        };
    }
}