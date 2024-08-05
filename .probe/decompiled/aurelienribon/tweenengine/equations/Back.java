package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Back extends TweenEquation {

    public static final Back IN = new Back() {

        @Override
        public final float compute(float t) {
            float s = this.param_s;
            return t * t * ((s + 1.0F) * t - s);
        }

        public String toString() {
            return "Back.IN";
        }
    };

    public static final Back OUT = new Back() {

        @Override
        public final float compute(float t) {
            float s = this.param_s;
            return --t * t * ((s + 1.0F) * t + s) + 1.0F;
        }

        public String toString() {
            return "Back.OUT";
        }
    };

    public static final Back INOUT = new Back() {

        @Override
        public final float compute(float t) {
            float s = this.param_s;
            float var3;
            float var4;
            float var5;
            float var6;
            return (var3 = t * 2.0F) < 1.0F ? 0.5F * var3 * var3 * (((var5 = s * 1.525F) + 1.0F) * var3 - var5) : 0.5F * ((var4 = var3 - 2.0F) * var4 * (((var6 = s * 1.525F) + 1.0F) * var4 + var6) + 2.0F);
        }

        public String toString() {
            return "Back.INOUT";
        }
    };

    protected float param_s = 1.70158F;

    public Back s(float s) {
        this.param_s = s;
        return this;
    }
}