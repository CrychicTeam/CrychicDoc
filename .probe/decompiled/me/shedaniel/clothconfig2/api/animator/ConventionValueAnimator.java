package me.shedaniel.clothconfig2.api.animator;

import java.util.Objects;
import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class ConventionValueAnimator<T> implements ValueAnimator<T> {

    private final ValueAnimator<T> parent;

    private final Supplier<T> convention;

    private final long duration;

    ConventionValueAnimator(ValueAnimator<T> parent, Supplier<T> convention, long duration) {
        this.parent = parent;
        this.convention = convention;
        this.duration = duration;
        this.setAs((T) convention.get());
    }

    @Override
    public ValueAnimator<T> setTo(T value, long duration) {
        return this.parent.setTo(value, duration);
    }

    @Override
    public ValueAnimator<T> setTarget(T target) {
        return this.parent.setTarget(target);
    }

    @Override
    public T target() {
        return (T) this.convention.get();
    }

    @Override
    public T value() {
        return this.parent.value();
    }

    @Override
    public void update(double delta) {
        this.parent.update(delta);
        T target = this.target();
        if (!Objects.equals(this.parent.target(), target)) {
            this.setTo(target, this.duration);
        }
    }
}