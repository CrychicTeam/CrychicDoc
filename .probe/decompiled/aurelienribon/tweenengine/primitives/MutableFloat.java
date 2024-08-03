package aurelienribon.tweenengine.primitives;

import aurelienribon.tweenengine.TweenAccessor;

public class MutableFloat extends Number implements TweenAccessor<MutableFloat> {

    private float value;

    public MutableFloat(float value) {
        this.value = value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public int getValues(MutableFloat target, int tweenType, float[] returnValues) {
        returnValues[0] = target.value;
        return 1;
    }

    public void setValues(MutableFloat target, int tweenType, float[] newValues) {
        target.value = newValues[0];
    }
}