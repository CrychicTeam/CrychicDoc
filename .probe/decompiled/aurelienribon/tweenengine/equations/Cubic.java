package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Cubic extends TweenEquation {

    public static final Cubic IN = new Cubic() {

        @Override
        public final float compute(float t) {
            return t * t * t;
        }

        public String toString() {
            return "Cubic.IN";
        }
    };

    public static final Cubic OUT = new Cubic() {

        @Override
        public final float compute(float t) {
            return --t * t * t + 1.0F;
        }

        public String toString() {
            return "Cubic.OUT";
        }
    };

    public static final Cubic INOUT = new Cubic() {

        @Override
        public final float compute(float t) {
            float var2;
            float var3;
            return (var2 = t * 2.0F) < 1.0F ? 0.5F * var2 * var2 * var2 : 0.5F * ((var3 = var2 - 2.0F) * var3 * var3 + 2.0F);
        }

        public String toString() {
            return "Cubic.INOUT";
        }
    };
}