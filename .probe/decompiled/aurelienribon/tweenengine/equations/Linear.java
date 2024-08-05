package aurelienribon.tweenengine.equations;

import aurelienribon.tweenengine.TweenEquation;

public abstract class Linear extends TweenEquation {

    public static final Linear INOUT = new Linear() {

        @Override
        public float compute(float t) {
            return t;
        }

        public String toString() {
            return "Linear.INOUT";
        }
    };
}