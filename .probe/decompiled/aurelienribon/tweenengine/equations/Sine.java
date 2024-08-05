package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Sine extends TweenEquation {

    private static final float PI = (float) Math.PI;

    public static final Sine IN = new Sine() {

        @Override
        public final float compute(float t) {
            return (float) (-Math.cos((double) (t * (float) (Math.PI / 2)))) + 1.0F;
        }

        public String toString() {
            return "Sine.IN";
        }
    };

    public static final Sine OUT = new Sine() {

        @Override
        public final float compute(float t) {
            return (float) Math.sin((double) (t * (float) (Math.PI / 2)));
        }

        public String toString() {
            return "Sine.OUT";
        }
    };

    public static final Sine INOUT = new Sine() {

        @Override
        public final float compute(float t) {
            return -0.5F * ((float) Math.cos((double) ((float) Math.PI * t)) - 1.0F);
        }

        public String toString() {
            return "Sine.INOUT";
        }
    };
}