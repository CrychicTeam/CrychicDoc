package icyllis.modernui.animation;

import javax.annotation.Nonnull;

class IntKeyframeSet extends KeyframeSet implements Keyframes.IntKeyframes {

    IntKeyframeSet(@Nonnull Keyframe... keyframes) {
        super(keyframes);
    }

    public IntKeyframeSet copy() {
        int numKeyframes = this.mKeyframes.length;
        Keyframe.IntKeyframe[] newKeyframes = new Keyframe.IntKeyframe[numKeyframes];
        for (int i = 0; i < numKeyframes; i++) {
            newKeyframes[i] = (Keyframe.IntKeyframe) this.mKeyframes[i].copy();
        }
        return new IntKeyframeSet(newKeyframes);
    }

    public Integer getValue(float fraction) {
        return this.getIntValue(fraction);
    }

    @Override
    public int getIntValue(float fraction) {
        Keyframe[] keyframes = this.mKeyframes;
        int length = keyframes.length;
        if (fraction <= 0.0F) {
            Keyframe.IntKeyframe prevKeyframe = (Keyframe.IntKeyframe) keyframes[0];
            Keyframe.IntKeyframe nextKeyframe = (Keyframe.IntKeyframe) keyframes[1];
            int prevValue = prevKeyframe.getIntValue();
            int nextValue = nextKeyframe.getIntValue();
            TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            float prevFraction = prevKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
            return this.mEvaluator == null ? prevValue + (int) (intervalFraction * (float) (nextValue - prevValue)) : this.mEvaluator.evaluate(intervalFraction, prevValue, nextValue);
        } else if (fraction >= 1.0F) {
            Keyframe.IntKeyframe prevKeyframe = (Keyframe.IntKeyframe) keyframes[length - 2];
            Keyframe.IntKeyframe nextKeyframe = (Keyframe.IntKeyframe) keyframes[length - 1];
            int prevValue = prevKeyframe.getIntValue();
            int nextValue = nextKeyframe.getIntValue();
            TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            float prevFraction = prevKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
            return this.mEvaluator == null ? prevValue + (int) (intervalFraction * (float) (nextValue - prevValue)) : this.mEvaluator.evaluate(intervalFraction, prevValue, nextValue);
        } else {
            Keyframe.IntKeyframe prevKeyframe = (Keyframe.IntKeyframe) keyframes[0];
            for (int i = 1; i < length; i++) {
                Keyframe.IntKeyframe nextKeyframe = (Keyframe.IntKeyframe) keyframes[i];
                if (fraction < nextKeyframe.getFraction()) {
                    TimeInterpolator interpolator = nextKeyframe.getInterpolator();
                    float prevFraction = prevKeyframe.getFraction();
                    float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
                    int prevValue = prevKeyframe.getIntValue();
                    int nextValue = nextKeyframe.getIntValue();
                    if (interpolator != null) {
                        intervalFraction = interpolator.getInterpolation(intervalFraction);
                    }
                    return this.mEvaluator == null ? prevValue + (int) (intervalFraction * (float) (nextValue - prevValue)) : this.mEvaluator.evaluate(intervalFraction, prevValue, nextValue);
                }
                prevKeyframe = nextKeyframe;
            }
            return prevKeyframe.getIntValue();
        }
    }
}