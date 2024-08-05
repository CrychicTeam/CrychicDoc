package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Circ extends TweenEquation {

    public static final Circ IN = new Circ() {

        @Override
        public final float compute(float t) {
            return (float) (-Math.sqrt((double) (1.0F - t * t))) - 1.0F;
        }

        public String toString() {
            return "Circ.IN";
        }
    };

    public static final Circ OUT = new Circ() {

        @Override
        public final float compute(float t) {
            return (float) Math.sqrt((double) (1.0F - --t * t));
        }

        public String toString() {
            return "Circ.OUT";
        }
    };

    public static final Circ INOUT = new Circ() {

        @Override
        public final float compute(float t) {
            float var2;
            float var3;
            return (var2 = t * 2.0F) < 1.0F ? -0.5F * ((float) Math.sqrt((double) (1.0F - var2 * var2)) - 1.0F) : 0.5F * ((float) Math.sqrt((double) (1.0F - (var3 = var2 - 2.0F) * var3)) + 1.0F);
        }

        public String toString() {
            return "Circ.INOUT";
        }
    };
}