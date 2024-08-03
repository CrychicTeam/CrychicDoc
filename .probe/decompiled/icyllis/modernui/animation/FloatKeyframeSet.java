package icyllis.modernui.animation;

import javax.annotation.Nonnull;

class FloatKeyframeSet extends KeyframeSet implements Keyframes.FloatKeyframes {

    FloatKeyframeSet(@Nonnull Keyframe... keyframes) {
        super(keyframes);
    }

    public FloatKeyframeSet copy() {
        int numKeyframes = this.mKeyframes.length;
        Keyframe.FloatKeyframe[] newKeyframes = new Keyframe.FloatKeyframe[numKeyframes];
        for (int i = 0; i < numKeyframes; i++) {
            newKeyframes[i] = (Keyframe.FloatKeyframe) this.mKeyframes[i].copy();
        }
        return new FloatKeyframeSet(newKeyframes);
    }

    public Float getValue(float fraction) {
        return this.getFloatValue(fraction);
    }

    @Override
    public float getFloatValue(float fraction) {
        Keyframe[] keyframes = this.mKeyframes;
        int length = keyframes.length;
        if (fraction <= 0.0F) {
            Keyframe.FloatKeyframe prevKeyframe = (Keyframe.FloatKeyframe) keyframes[0];
            Keyframe.FloatKeyframe nextKeyframe = (Keyframe.FloatKeyframe) keyframes[1];
            float prevValue = prevKeyframe.getFloatValue();
            float nextValue = nextKeyframe.getFloatValue();
            TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            float prevFraction = prevKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
            return this.mEvaluator == null ? prevValue + intervalFraction * (nextValue - prevValue) : this.mEvaluator.evaluate(intervalFraction, prevValue, nextValue);
        } else if (fraction >= 1.0F) {
            Keyframe.FloatKeyframe prevKeyframe = (Keyframe.FloatKeyframe) keyframes[length - 2];
            Keyframe.FloatKeyframe nextKeyframe = (Keyframe.FloatKeyframe) keyframes[length - 1];
            float prevValue = prevKeyframe.getFloatValue();
            float nextValue = nextKeyframe.getFloatValue();
            TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            float prevFraction = prevKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
            return this.mEvaluator == null ? prevValue + intervalFraction * (nextValue - prevValue) : this.mEvaluator.evaluate(intervalFraction, prevValue, nextValue);
        } else {
            Keyframe.FloatKeyframe prevKeyframe = (Keyframe.FloatKeyframe) keyframes[0];
            for (int i = 1; i < length; i++) {
                Keyframe.FloatKeyframe nextKeyframe = (Keyframe.FloatKeyframe) keyframes[i];
                if (fraction < nextKeyframe.getFraction()) {
                    TimeInterpolator interpolator = nextKeyframe.getInterpolator();
                    float prevFraction = prevKeyframe.getFraction();
                    float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
                    float prevValue = prevKeyframe.getFloatValue();
                    float nextValue = nextKeyframe.getFloatValue();
                    if (interpolator != null) {
                        intervalFraction = interpolator.getInterpolation(intervalFraction);
                    }
                    return this.mEvaluator == null ? prevValue + intervalFraction * (nextValue - prevValue) : this.mEvaluator.evaluate(intervalFraction, prevValue, nextValue);
                }
                prevKeyframe = nextKeyframe;
            }
            return prevKeyframe.getFloatValue();
        }
    }
}