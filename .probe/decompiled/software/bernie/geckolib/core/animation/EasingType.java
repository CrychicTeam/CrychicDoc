package software.bernie.geckolib.core.animation;

import com.eliotlash.mclib.math.IValue;
import com.eliotlash.mclib.utils.Interpolations;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import software.bernie.geckolib.core.keyframe.AnimationPoint;

@FunctionalInterface
public interface EasingType {

    Map<String, EasingType> EASING_TYPES = new ConcurrentHashMap(64);

    EasingType LINEAR = register("linear", register("none", value -> easeIn(EasingType::linear)));

    EasingType STEP = register("step", value -> easeIn(step(value)));

    EasingType EASE_IN_SINE = register("easeinsine", value -> easeIn(EasingType::sine));

    EasingType EASE_OUT_SINE = register("easeoutsine", value -> easeOut(EasingType::sine));

    EasingType EASE_IN_OUT_SINE = register("easeinoutsine", value -> easeInOut(EasingType::sine));

    EasingType EASE_IN_QUAD = register("easeinquad", value -> easeIn(EasingType::quadratic));

    EasingType EASE_OUT_QUAD = register("easeoutquad", value -> easeOut(EasingType::quadratic));

    EasingType EASE_IN_OUT_QUAD = register("easeinoutquad", value -> easeInOut(EasingType::quadratic));

    EasingType EASE_IN_CUBIC = register("easeincubic", value -> easeIn(EasingType::cubic));

    EasingType EASE_OUT_CUBIC = register("easeoutcubic", value -> easeOut(EasingType::cubic));

    EasingType EASE_IN_OUT_CUBIC = register("easeinoutcubic", value -> easeInOut(EasingType::cubic));

    EasingType EASE_IN_QUART = register("easeinquart", value -> easeIn(pow(4.0)));

    EasingType EASE_OUT_QUART = register("easeoutquart", value -> easeOut(pow(4.0)));

    EasingType EASE_IN_OUT_QUART = register("easeinoutquart", value -> easeInOut(pow(4.0)));

    EasingType EASE_IN_QUINT = register("easeinquint", value -> easeIn(pow(4.0)));

    EasingType EASE_OUT_QUINT = register("easeoutquint", value -> easeOut(pow(5.0)));

    EasingType EASE_IN_OUT_QUINT = register("easeinoutquint", value -> easeInOut(pow(5.0)));

    EasingType EASE_IN_EXPO = register("easeinexpo", value -> easeIn(EasingType::exp));

    EasingType EASE_OUT_EXPO = register("easeoutexpo", value -> easeOut(EasingType::exp));

    EasingType EASE_IN_OUT_EXPO = register("easeinoutexpo", value -> easeInOut(EasingType::exp));

    EasingType EASE_IN_CIRC = register("easeincirc", value -> easeIn(EasingType::circle));

    EasingType EASE_OUT_CIRC = register("easeoutcirc", value -> easeOut(EasingType::circle));

    EasingType EASE_IN_OUT_CIRC = register("easeinoutcirc", value -> easeInOut(EasingType::circle));

    EasingType EASE_IN_BACK = register("easeinback", value -> easeIn(back(value)));

    EasingType EASE_OUT_BACK = register("easeoutback", value -> easeOut(back(value)));

    EasingType EASE_IN_OUT_BACK = register("easeinoutback", value -> easeInOut(back(value)));

    EasingType EASE_IN_ELASTIC = register("easeinelastic", value -> easeIn(elastic(value)));

    EasingType EASE_OUT_ELASTIC = register("easeoutelastic", value -> easeOut(elastic(value)));

    EasingType EASE_IN_OUT_ELASTIC = register("easeinoutelastic", value -> easeInOut(elastic(value)));

    EasingType EASE_IN_BOUNCE = register("easeinbounce", value -> easeIn(bounce(value)));

    EasingType EASE_OUT_BOUNCE = register("easeoutbounce", value -> easeOut(bounce(value)));

    EasingType EASE_IN_OUT_BOUNCE = register("easeinoutbounce", value -> easeInOut(bounce(value)));

    EasingType CATMULLROM = register("catmullrom", value -> easeInOut(EasingType::catmullRom));

    Double2DoubleFunction buildTransformer(Double var1);

    static double lerpWithOverride(AnimationPoint animationPoint, EasingType override) {
        EasingType easingType = override;
        if (override == null) {
            easingType = animationPoint.keyFrame() == null ? LINEAR : animationPoint.keyFrame().easingType();
        }
        return easingType.apply(animationPoint);
    }

    default double apply(AnimationPoint animationPoint) {
        Double easingVariable = null;
        if (animationPoint.keyFrame() != null && animationPoint.keyFrame().easingArgs().size() > 0) {
            easingVariable = ((IValue) animationPoint.keyFrame().easingArgs().get(0)).get();
        }
        return this.apply(animationPoint, easingVariable, animationPoint.currentTick() / animationPoint.transitionLength());
    }

    default double apply(AnimationPoint animationPoint, Double easingValue, double lerpValue) {
        return animationPoint.currentTick() >= animationPoint.transitionLength() ? (double) ((float) animationPoint.animationEndValue()) : Interpolations.lerp(animationPoint.animationStartValue(), animationPoint.animationEndValue(), (Double) this.buildTransformer(easingValue).apply(lerpValue));
    }

    static EasingType register(String name, EasingType easingType) {
        EASING_TYPES.putIfAbsent(name, easingType);
        return easingType;
    }

    static EasingType fromJson(JsonElement json) {
        if (json instanceof JsonPrimitive primitive && primitive.isString()) {
            return fromString(primitive.getAsString().toLowerCase(Locale.ROOT));
        }
        return LINEAR;
    }

    static EasingType fromString(String name) {
        return (EasingType) EASING_TYPES.getOrDefault(name, LINEAR);
    }

    static Double2DoubleFunction linear(Double2DoubleFunction function) {
        return function;
    }

    static double catmullRom(double n) {
        return 0.5 * (2.0 * (n + 1.0) + (n + 2.0 - n) * 1.0 + (2.0 * n - 5.0 * (n + 1.0) + 4.0 * (n + 2.0) - (n + 3.0)) * 1.0 + (3.0 * (n + 1.0) - n - 3.0 * (n + 2.0) + n + 3.0) * 1.0);
    }

    static Double2DoubleFunction easeIn(Double2DoubleFunction function) {
        return function;
    }

    static Double2DoubleFunction easeOut(Double2DoubleFunction function) {
        return time -> 1.0 - (Double) function.apply(1.0 - time);
    }

    static Double2DoubleFunction easeInOut(Double2DoubleFunction function) {
        return time -> time < 0.5 ? (Double) function.apply(time * 2.0) / 2.0 : 1.0 - (Double) function.apply((1.0 - time) * 2.0) / 2.0;
    }

    static Double2DoubleFunction stepPositive(Double2DoubleFunction function) {
        return n -> n > 0.0 ? 1.0 : 0.0;
    }

    static Double2DoubleFunction stepNonNegative(Double2DoubleFunction function) {
        return n -> n >= 0.0 ? 1.0 : 0.0;
    }

    static double linear(double n) {
        return n;
    }

    static double quadratic(double n) {
        return n * n;
    }

    static double cubic(double n) {
        return n * n * n;
    }

    static double sine(double n) {
        return 1.0 - Math.cos(n * Math.PI / 2.0);
    }

    static double circle(double n) {
        return 1.0 - Math.sqrt(1.0 - n * n);
    }

    static double exp(double n) {
        return Math.pow(2.0, 10.0 * (n - 1.0));
    }

    static Double2DoubleFunction elastic(Double n) {
        double n2 = n == null ? 1.0 : n;
        return t -> 1.0 - Math.pow(Math.cos(t * Math.PI / 2.0), 3.0) * Math.cos(t * n2 * Math.PI);
    }

    static Double2DoubleFunction bounce(Double n) {
        double n2 = n == null ? 0.5 : n;
        Double2DoubleFunction one = x -> 7.5625 * x * x;
        Double2DoubleFunction two = x -> 30.25 * n2 * Math.pow(x - 0.54545456F, 2.0) + 1.0 - n2;
        Double2DoubleFunction three = x -> 121.0 * n2 * n2 * Math.pow(x - 0.8181818F, 2.0) + 1.0 - n2 * n2;
        Double2DoubleFunction four = x -> 484.0 * n2 * n2 * n2 * Math.pow(x - 0.95454544F, 2.0) + 1.0 - n2 * n2 * n2;
        return t -> Math.min(Math.min((Double) one.apply(t), (Double) two.apply(t)), Math.min((Double) three.apply(t), (Double) four.apply(t)));
    }

    static Double2DoubleFunction back(Double n) {
        double n2 = n == null ? 1.70158 : n * 1.70158;
        return t -> t * t * ((n2 + 1.0) * t - n2);
    }

    static Double2DoubleFunction pow(double n) {
        return t -> Math.pow(t, n);
    }

    static Double2DoubleFunction step(Double n) {
        double n2 = n == null ? 2.0 : n;
        if (n2 < 2.0) {
            throw new IllegalArgumentException("Steps must be >= 2, got: " + n2);
        } else {
            int steps = (int) n2;
            return t -> {
                double result = 0.0;
                if (t < 0.0) {
                    return result;
                } else {
                    double stepLength = 1.0 / (double) steps;
                    if (t > (result = (double) (steps - 1) * stepLength)) {
                        return result;
                    } else {
                        int leftBorderIndex = 0;
                        int rightBorderIndex = steps - 1;
                        while (rightBorderIndex - leftBorderIndex != 1) {
                            int testIndex = leftBorderIndex + (rightBorderIndex - leftBorderIndex) / 2;
                            if (t >= (double) testIndex * stepLength) {
                                leftBorderIndex = testIndex;
                            } else {
                                rightBorderIndex = testIndex;
                            }
                        }
                        return (double) leftBorderIndex * stepLength;
                    }
                }
            };
        }
    }
}