package me.shedaniel.clothconfig2.api.animator;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class MappingProgressValueAnimator<R> implements ProgressValueAnimator<R> {

    private final ValueAnimator<Double> parent;

    private final Function<Double, R> converter;

    private final Function<R, Double> backwardsConverter;

    MappingProgressValueAnimator(ValueAnimator<Double> parent, Function<Double, R> converter, Function<R, Double> backwardsConverter) {
        this.parent = parent;
        this.converter = converter;
        this.backwardsConverter = backwardsConverter;
    }

    @Override
    public ProgressValueAnimator<R> setTo(R value, long duration) {
        this.parent.setTo((Double) this.backwardsConverter.apply(value), duration);
        return this;
    }

    @Override
    public ProgressValueAnimator<R> setTarget(R target) {
        this.parent.setTarget((Double) this.backwardsConverter.apply(target));
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

    @Override
    public double progress() {
        return this.parent.value() / 100.0;
    }
}