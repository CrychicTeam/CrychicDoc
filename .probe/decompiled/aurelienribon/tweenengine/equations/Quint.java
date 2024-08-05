package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Quint extends TweenEquation {

    public static final Quint IN = new Quint() {

        @Override
        public final float compute(float t) {
            return t * t * t * t * t;
        }

        public String toString() {
            return "Quint.IN";
        }
    };

    public static final Quint OUT = new Quint() {

        @Override
        public final float compute(float t) {
            return --t * t * t * t * t + 1.0F;
        }

        public String toString() {
            return "Quint.OUT";
        }
    };

    public static final Quint INOUT = new Quint() {

        @Override
        public final float compute(float t) {
            float var2;
            float var3;
            return (var2 = t * 2.0F) < 1.0F ? 0.5F * var2 * var2 * var2 * var2 * var2 : 0.5F * ((var3 = var2 - 2.0F) * var3 * var3 * var3 * var3 + 2.0F);
        }

        public String toString() {
            return "Quint.INOUT";
        }
    };
}