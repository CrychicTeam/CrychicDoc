package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Expo extends TweenEquation {

    public static final Expo IN = new Expo() {

        @Override
        public final float compute(float t) {
            return t == 0.0F ? 0.0F : (float) Math.pow(2.0, (double) (10.0F * (t - 1.0F)));
        }

        public String toString() {
            return "Expo.IN";
        }
    };

    public static final Expo OUT = new Expo() {

        @Override
        public final float compute(float t) {
            return t == 1.0F ? 1.0F : -((float) Math.pow(2.0, (double) (-10.0F * t))) + 1.0F;
        }

        public String toString() {
            return "Expo.OUT";
        }
    };

    public static final Expo INOUT = new Expo() {

        @Override
        public final float compute(float t) {
            if (t == 0.0F) {
                return 0.0F;
            } else if (t == 1.0F) {
                return 1.0F;
            } else {
                float var2;
                return (var2 = t * 2.0F) < 1.0F ? 0.5F * (float) Math.pow(2.0, (double) (10.0F * (var2 - 1.0F))) : 0.5F * (-((float) Math.pow(2.0, (double) (-10.0F * --var2))) + 2.0F);
            }
        }

        public String toString() {
            return "Expo.INOUT";
        }
    };
}