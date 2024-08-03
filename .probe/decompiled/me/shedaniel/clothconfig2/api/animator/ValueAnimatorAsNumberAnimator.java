package me.shedaniel.clothconfig2.api.animator;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
abstract class ValueAnimatorAsNumberAnimator<T extends Number> extends NumberAnimator<T> {

    private final ValueAnimator<T> animator;

    ValueAnimatorAsNumberAnimator(ValueAnimator<T> animator) {
        this.animator = animator;
    }

    public int intValue() {
        return this.animator.value().intValue();
    }

    public long longValue() {
        return this.animator.value().longValue();
    }

    public float floatValue() {
        return this.animator.value().floatValue();
    }

    public double doubleValue() {
        return this.animator.value().doubleValue();
    }

    public T value() {
        return this.animator.value();
    }

    public T target() {
        return this.animator.target();
    }

    @Override
    public void update(double delta) {
        this.animator.update(delta);
    }
}