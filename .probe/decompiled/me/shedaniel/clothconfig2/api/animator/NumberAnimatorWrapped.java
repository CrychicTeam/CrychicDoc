package me.shedaniel.clothconfig2.api.animator;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class NumberAnimatorWrapped<T extends Number, R extends Number> extends NumberAnimator<T> {

    private final NumberAnimator<R> parent;

    private final Function<R, T> converter;

    NumberAnimatorWrapped(NumberAnimator<R> parent, Function<R, T> converter) {
        this.parent = parent;
        this.converter = converter;
    }

    @Override
    public NumberAnimator<T> setToNumber(Number value, long duration) {
        this.parent.setToNumber(value, duration);
        return this;
    }

    @Override
    public NumberAnimator<T> setTargetNumber(Number value) {
        this.parent.setTargetNumber(value);
        return this;
    }

    public T target() {
        return (T) this.converter.apply(this.parent.target());
    }

    public T value() {
        return (T) this.converter.apply(this.parent.value());
    }

    @Override
    public void update(double delta) {
        this.parent.update(delta);
    }

    public int intValue() {
        return this.parent.intValue();
    }

    public long longValue() {
        return this.parent.longValue();
    }

    public float floatValue() {
        return this.parent.floatValue();
    }

    public double doubleValue() {
        return this.parent.doubleValue();
    }
}