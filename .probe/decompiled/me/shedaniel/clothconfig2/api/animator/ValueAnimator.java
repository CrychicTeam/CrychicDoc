package me.shedaniel.clothconfig2.api.animator;

import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.math.Color;
import me.shedaniel.math.Dimension;
import me.shedaniel.math.FloatingDimension;
import me.shedaniel.math.FloatingPoint;
import me.shedaniel.math.FloatingRectangle;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface ValueAnimator<T> extends ValueProvider<T> {

    static long typicalTransitionTime() {
        return 700L;
    }

    static NumberAnimator<Double> ofDouble() {
        return new DoubleValueAnimatorImpl(0.0);
    }

    static NumberAnimator<Float> ofFloat() {
        return ofDouble().asFloat();
    }

    static NumberAnimator<Integer> ofInt() {
        return ofDouble().asInt();
    }

    static NumberAnimator<Long> ofLong() {
        return ofDouble().asLong();
    }

    static ProgressValueAnimator<Boolean> ofBoolean() {
        return ofBoolean(50.0);
    }

    static ProgressValueAnimator<Boolean> ofBoolean(double switchPoint) {
        return ProgressValueAnimator.mapProgress(ofDouble(), d -> d > switchPoint / 100.0, b -> b ? 100.0 : 0.0);
    }

    static ValueAnimator<Color> ofColor() {
        return RecordValueAnimator.of(ofInt(), ofInt(), ofInt(), ofInt(), Color::ofRGBA, (color, r, g, b, a) -> {
            r.set(color.getRed());
            g.set(color.getGreen());
            b.set(color.getBlue());
            a.set(color.getAlpha());
        });
    }

    static ValueAnimator<Rectangle> ofRectangle() {
        return RecordValueAnimator.of(ofInt(), ofInt(), ofInt(), ofInt(), Rectangle::new, (rectangle, x, y, width, height) -> {
            x.set(rectangle.x);
            y.set(rectangle.y);
            width.set(rectangle.width);
            height.set(rectangle.height);
        });
    }

    static ValueAnimator<Dimension> ofDimension() {
        return RecordValueAnimator.of(ofInt(), ofInt(), Dimension::new, (dimension, width, height) -> {
            width.set(dimension.width);
            height.set(dimension.height);
        });
    }

    static ValueAnimator<Point> ofPoint() {
        return RecordValueAnimator.of(ofInt(), ofInt(), Point::new, (dimension, width, height) -> {
            width.set(dimension.x);
            height.set(dimension.y);
        });
    }

    static ValueAnimator<FloatingRectangle> ofFloatingRectangle() {
        return RecordValueAnimator.of(ofDouble(), ofDouble(), ofDouble(), ofDouble(), FloatingRectangle::new, (rectangle, x, y, width, height) -> {
            x.set(rectangle.x);
            y.set(rectangle.y);
            width.set(rectangle.width);
            height.set(rectangle.height);
        });
    }

    static ValueAnimator<FloatingDimension> ofFloatingDimension() {
        return RecordValueAnimator.of(ofDouble(), ofDouble(), FloatingDimension::new, (dimension, width, height) -> {
            width.set(dimension.width);
            height.set(dimension.height);
        });
    }

    static ValueAnimator<FloatingPoint> ofFloatingPoint() {
        return RecordValueAnimator.of(ofDouble(), ofDouble(), FloatingPoint::new, (dimension, width, height) -> {
            width.set(dimension.x);
            height.set(dimension.y);
        });
    }

    static NumberAnimator<Double> ofDouble(double initialValue) {
        return new DoubleValueAnimatorImpl(initialValue);
    }

    static NumberAnimator<Float> ofFloat(float initialValue) {
        return ofFloat().setAs(initialValue);
    }

    static NumberAnimator<Integer> ofInt(int initialValue) {
        return ofInt().setAs(initialValue);
    }

    static NumberAnimator<Long> ofLong(long initialValue) {
        return ofLong().setAs(initialValue);
    }

    static ProgressValueAnimator<Boolean> ofBoolean(boolean initialValue) {
        return ofBoolean().setAs(initialValue);
    }

    static ProgressValueAnimator<Boolean> ofBoolean(double switchPoint, boolean initialValue) {
        return ofBoolean(switchPoint).setAs(initialValue);
    }

    static ValueAnimator<Color> ofColor(Color initialValue) {
        return ofColor().setAs(initialValue);
    }

    static ValueAnimator<Rectangle> ofRectangle(Rectangle initialValue) {
        return ofRectangle().setAs(initialValue);
    }

    static ValueAnimator<Dimension> ofDimension(Dimension initialValue) {
        return ofDimension().setAs(initialValue);
    }

    @Deprecated
    @ScheduledForRemoval
    static ValueAnimator<Point> ofDimension(Point initialValue) {
        return ofPoint().setAs(initialValue);
    }

    static ValueAnimator<Point> ofPoint(Point initialValue) {
        return ofPoint().setAs(initialValue);
    }

    static ValueAnimator<FloatingRectangle> ofFloatingRectangle(FloatingRectangle initialValue) {
        return ofFloatingRectangle().setAs(initialValue);
    }

    static ValueAnimator<FloatingDimension> ofFloatingDimension(FloatingDimension initialValue) {
        return ofFloatingDimension().setAs(initialValue);
    }

    @Deprecated
    @ScheduledForRemoval
    static ValueAnimator<FloatingPoint> ofFloatingDimension(FloatingPoint initialValue) {
        return ofFloatingPoint().setAs(initialValue);
    }

    static ValueAnimator<FloatingPoint> ofFloatingPoint(FloatingPoint initialValue) {
        return ofFloatingPoint().setAs(initialValue);
    }

    default <R> ValueAnimator<R> map(Function<T, R> converter, Function<R, T> backwardsConverter) {
        return new MappingValueAnimator<>(this, converter, backwardsConverter);
    }

    default ValueAnimator<T> setAs(T value) {
        return this.setTo(value, -1L);
    }

    ValueAnimator<T> setTo(T var1, long var2);

    ValueAnimator<T> setTarget(T var1);

    default ValueAnimator<T> withConvention(Supplier<T> convention, long duration) {
        return new ConventionValueAnimator<>(this, convention, duration);
    }

    @Override
    default void completeImmediately() {
        this.setAs(this.target());
    }
}