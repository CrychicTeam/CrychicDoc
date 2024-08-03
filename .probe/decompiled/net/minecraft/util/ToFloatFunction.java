package net.minecraft.util;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.util.function.Function;

public interface ToFloatFunction<C> {

    ToFloatFunction<Float> IDENTITY = createUnlimited(p_216474_ -> p_216474_);

    float apply(C var1);

    float minValue();

    float maxValue();

    static ToFloatFunction<Float> createUnlimited(final Float2FloatFunction floatFloatFunction0) {
        return new ToFloatFunction<Float>() {

            public float apply(Float p_216483_) {
                return (Float) floatFloatFunction0.apply(p_216483_);
            }

            @Override
            public float minValue() {
                return Float.NEGATIVE_INFINITY;
            }

            @Override
            public float maxValue() {
                return Float.POSITIVE_INFINITY;
            }
        };
    }

    default <C2> ToFloatFunction<C2> comap(final Function<C2, C> functionCC0) {
        final ToFloatFunction<C> $$1 = this;
        return new ToFloatFunction<C2>() {

            @Override
            public float apply(C2 p_216496_) {
                return $$1.apply((C) functionCC0.apply(p_216496_));
            }

            @Override
            public float minValue() {
                return $$1.minValue();
            }

            @Override
            public float maxValue() {
                return $$1.maxValue();
            }
        };
    }
}