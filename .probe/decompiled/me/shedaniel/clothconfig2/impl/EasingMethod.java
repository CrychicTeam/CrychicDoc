package me.shedaniel.clothconfig2.impl;

import java.util.function.Function;

public interface EasingMethod {

    double apply(double var1);

    public static enum EasingMethodImpl implements EasingMethod {

        NONE(v -> 1.0),
        LINEAR(v -> v),
        EXPO(v -> v == 1.0 ? 1.0 : 1.0 * (-Math.pow(2.0, -10.0 * v) + 1.0)),
        QUAD(v -> {
            Double var1;
            return -1.0 * var1 = v / 1.0 * (var1 - 2.0);
        }),
        QUART(v -> {
            Double var1;
            return v == 1.0 ? 1.0 : 1.0 * -1.0 * (var1 = v - 1.0 * var1 * var1 * var1 - 1.0);
        }),
        SINE(v -> Math.sin(v * (Math.PI / 2))),
        CUBIC(v -> {
            Double var1;
            return var1 = v - 1.0 * var1 * var1 + 1.0;
        }),
        QUINTIC(v -> {
            Double var1;
            return var1 = v - 1.0 * var1 * var1 * var1 * var1 + 1.0;
        }),
        CIRC(v -> {
            Double var1;
            return Math.sqrt(1.0 - var1 = v - 1.0 * var1);
        });

        private final Function<Double, Double> function;

        private EasingMethodImpl(Function<Double, Double> function) {
            this.function = function;
        }

        @Override
        public double apply(double v) {
            return (Double) this.function.apply(v);
        }

        public String toString() {
            return this.name();
        }
    }
}