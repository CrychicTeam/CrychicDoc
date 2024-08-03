package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Elastic extends TweenEquation {

    private static final float PI = (float) Math.PI;

    public static final Elastic IN = new Elastic() {

        @Override
        public final float compute(float t) {
            float a = this.param_a;
            float p = this.param_p;
            if (t == 0.0F) {
                return 0.0F;
            } else if (t == 1.0F) {
                return 1.0F;
            } else {
                if (!this.setP) {
                    p = 0.3F;
                }
                float s;
                if (this.setA && !(a < 1.0F)) {
                    s = p / (float) (Math.PI * 2) * (float) Math.asin((double) (1.0F / a));
                } else {
                    a = 1.0F;
                    s = p / 4.0F;
                }
                return -(a * (float) Math.pow(2.0, (double) (10.0F * --t)) * (float) Math.sin((double) ((t - s) * (float) (Math.PI * 2) / p)));
            }
        }

        public String toString() {
            return "Elastic.IN";
        }
    };

    public static final Elastic OUT = new Elastic() {

        @Override
        public final float compute(float t) {
            float a = this.param_a;
            float p = this.param_p;
            if (t == 0.0F) {
                return 0.0F;
            } else if (t == 1.0F) {
                return 1.0F;
            } else {
                if (!this.setP) {
                    p = 0.3F;
                }
                float s;
                if (this.setA && !(a < 1.0F)) {
                    s = p / (float) (Math.PI * 2) * (float) Math.asin((double) (1.0F / a));
                } else {
                    a = 1.0F;
                    s = p / 4.0F;
                }
                return a * (float) Math.pow(2.0, (double) (-10.0F * t)) * (float) Math.sin((double) ((t - s) * (float) (Math.PI * 2) / p)) + 1.0F;
            }
        }

        public String toString() {
            return "Elastic.OUT";
        }
    };

    public static final Elastic INOUT = new Elastic() {

        @Override
        public final float compute(float t) {
            float a = this.param_a;
            float p = this.param_p;
            if (t == 0.0F) {
                return 0.0F;
            } else if ((t = t * 2.0F) == 2.0F) {
                return 1.0F;
            } else {
                if (!this.setP) {
                    p = 0.45000002F;
                }
                float s;
                if (this.setA && !(a < 1.0F)) {
                    s = p / (float) (Math.PI * 2) * (float) Math.asin((double) (1.0F / a));
                } else {
                    a = 1.0F;
                    s = p / 4.0F;
                }
                return t < 1.0F ? -0.5F * a * (float) Math.pow(2.0, (double) (10.0F * --t)) * (float) Math.sin((double) ((t - s) * (float) (Math.PI * 2) / p)) : a * (float) Math.pow(2.0, (double) (-10.0F * --t)) * (float) Math.sin((double) ((t - s) * (float) (Math.PI * 2) / p)) * 0.5F + 1.0F;
            }
        }

        public String toString() {
            return "Elastic.INOUT";
        }
    };

    protected float param_a;

    protected float param_p;

    protected boolean setA = false;

    protected boolean setP = false;

    public Elastic a(float a) {
        this.param_a = a;
        this.setA = true;
        return this;
    }

    public Elastic p(float p) {
        this.param_p = p;
        this.setP = true;
        return this;
    }
}