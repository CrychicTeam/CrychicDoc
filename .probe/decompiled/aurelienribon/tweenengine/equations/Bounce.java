package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Bounce extends TweenEquation {

    public static final Bounce IN = new Bounce() {

        @Override
        public final float compute(float t) {
            return 1.0F - OUT.compute(1.0F - t);
        }

        public String toString() {
            return "Bounce.IN";
        }
    };

    public static final Bounce OUT = new Bounce() {

        @Override
        public final float compute(float t) {
            if ((double) t < 0.36363636363636365) {
                return 7.5625F * t * t;
            } else if ((double) t < 0.7272727272727273) {
                float var4;
                return 7.5625F * (var4 = t - 0.54545456F) * var4 + 0.75F;
            } else {
                float var2;
                float var3;
                return (double) t < 0.9090909090909091 ? 7.5625F * (var2 = t - 0.8181818F) * var2 + 0.9375F : 7.5625F * (var3 = t - 0.95454544F) * var3 + 0.984375F;
            }
        }

        public String toString() {
            return "Bounce.OUT";
        }
    };

    public static final Bounce INOUT = new Bounce() {

        @Override
        public final float compute(float t) {
            return t < 0.5F ? IN.compute(t * 2.0F) * 0.5F : OUT.compute(t * 2.0F - 1.0F) * 0.5F + 0.5F;
        }

        public String toString() {
            return "Bounce.INOUT";
        }
    };
}