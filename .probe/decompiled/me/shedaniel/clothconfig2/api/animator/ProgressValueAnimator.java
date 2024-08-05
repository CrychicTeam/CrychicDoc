package me.shedaniel.clothconfig2.api.animator;

import java.util.function.Function;

public interface ProgressValueAnimator<T> extends ValueAnimator<T> {

    double progress();

    default ProgressValueAnimator<T> setAs(T value) {
        ValueAnimator.super.setAs(value);
        return this;
    }

    ProgressValueAnimator<T> setTo(T var1, long var2);

    ProgressValueAnimator<T> setTarget(T var1);

    static <R> ProgressValueAnimator<R> mapProgress(NumberAnimator<?> parent, Function<Double, R> converter, Function<R, Double> backwardsConverter) {
        return new MappingProgressValueAnimator<>(parent.asDouble(), converter, backwardsConverter);
    }
}