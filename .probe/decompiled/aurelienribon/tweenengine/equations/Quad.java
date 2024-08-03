package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Quad extends TweenEquation {

    public static final Quad IN = new Quad() {

        @Override
        public final float compute(float t) {
            return t * t;
        }

        public String toString() {
            return "Quad.IN";
        }
    };

    public static final Quad OUT = new Quad() {

        @Override
        public final float compute(float t) {
            return -t * (t - 2.0F);
        }

        public String toString() {
            return "Quad.OUT";
        }
    };

    public static final Quad INOUT = new Quad() {

        @Override
        public final float compute(float t) {
            float var2;
            return (var2 = t * 2.0F) < 1.0F ? 0.5F * var2 * var2 : -0.5F * (--var2 * (var2 - 2.0F) - 1.0F);
        }

        public String toString() {
            return "Quad.INOUT";
        }
    };
}