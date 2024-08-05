package me.shedaniel.clothconfig2.api.animator;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class MappingValueAnimator<T, R> implements ValueAnimator<R> {

    private final ValueAnimator<T> parent;

    private final Function<T, R> converter;

    private final Function<R, T> backwardsConverter;

    MappingValueAnimator(ValueAnimator<T> parent, Function<T, R> converter, Function<R, T> backwardsConverter) {
        this.parent = parent;
        this.converter = converter;
        this.backwardsConverter = backwardsConverter;
    }

    @Override
    public ValueAnimator<R> setTo(R value, long duration) {
        this.parent.setTo((T) this.backwardsConverter.apply(value), duration);
        return this;
    }

    @Override
    public ValueAnimator<R> setTarget(R target) {
        this.parent.setTarget((T) this.backwardsConverter.apply(target));
        return this;
    }

    @Override
    public R target() {
        return (R) this.converter.apply(this.parent.target());
    }

    @Override
    public R value() {
        return (R) this.converter.apply(this.parent.value());
    }

    @Override
    public void update(double delta) {
        this.parent.update(delta);
    }
}