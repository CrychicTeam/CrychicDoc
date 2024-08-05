package icyllis.modernui.animation;

public interface Keyframes {

    void setEvaluator(TypeEvaluator<?> var1);

    Object getValue(float var1);

    Keyframe[] getKeyframes();

    Keyframes copy();

    public interface FloatKeyframes extends Keyframes {

        float getFloatValue(float var1);
    }

    public interface IntKeyframes extends Keyframes {

        int getIntValue(float var1);
    }
}