package icyllis.modernui.animation;

import icyllis.modernui.ModernUI;
import javax.annotation.Nonnull;

public class KeyframeSet implements Keyframes {

    final Keyframe[] mKeyframes;

    TypeEvaluator mEvaluator;

    KeyframeSet(@Nonnull Keyframe... keyframes) {
        if (keyframes.length < 2) {
            throw new IllegalArgumentException("Keyframes < 2");
        } else {
            this.mKeyframes = keyframes;
        }
    }

    @Nonnull
    public static IntKeyframeSet ofInt(@Nonnull int... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Length == 0");
        } else {
            int length = values.length;
            Keyframe[] keyframes = new Keyframe.IntKeyframe[Math.max(length, 2)];
            if (length == 1) {
                keyframes[0] = Keyframe.ofInt(0.0F);
                keyframes[1] = Keyframe.ofInt(1.0F, values[0]);
            } else {
                for (int i = 0; i < length; i++) {
                    keyframes[i] = Keyframe.ofInt((float) i / (float) (length - 1), values[i]);
                }
            }
            return new IntKeyframeSet(keyframes);
        }
    }

    @Nonnull
    public static FloatKeyframeSet ofFloat(@Nonnull float... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Length == 0");
        } else {
            boolean badValue = false;
            int length = values.length;
            Keyframe[] keyframes = new Keyframe.FloatKeyframe[Math.max(length, 2)];
            if (length == 1) {
                keyframes[0] = Keyframe.ofFloat(0.0F);
                keyframes[1] = Keyframe.ofFloat(1.0F, values[0]);
                if (Float.isNaN(values[0])) {
                    badValue = true;
                }
            } else {
                for (int i = 0; i < length; i++) {
                    keyframes[i] = Keyframe.ofFloat((float) i / (float) (length - 1), values[i]);
                    if (Float.isNaN(values[i])) {
                        badValue = true;
                    }
                }
            }
            if (badValue) {
                ModernUI.LOGGER.warn(Animator.MARKER, "Bad value (NaN) in float animator");
            }
            return new FloatKeyframeSet(keyframes);
        }
    }

    @Nonnull
    public static Keyframes ofObject(@Nonnull Object... values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Length == 0");
        } else {
            int length = values.length;
            Keyframe[] keyframes = new Keyframe.ObjectKeyframe[Math.max(length, 2)];
            if (length == 1) {
                keyframes[0] = Keyframe.ofObject(0.0F);
                keyframes[1] = Keyframe.ofObject(1.0F, values[0]);
            } else {
                for (int i = 0; i < length; i++) {
                    keyframes[i] = Keyframe.ofObject((float) i / (float) (length - 1), values[i]);
                }
            }
            return new KeyframeSet(keyframes);
        }
    }

    @Nonnull
    public static KeyframeSet ofKeyframe(@Nonnull Keyframe... keyframes) {
        if (keyframes.length < 2) {
            throw new IllegalArgumentException("Keyframes < 2");
        } else {
            boolean hasFloat = false;
            boolean hasInt = false;
            boolean hasOther = false;
            for (Keyframe keyframe : keyframes) {
                if (keyframe instanceof Keyframe.FloatKeyframe) {
                    hasFloat = true;
                } else if (keyframe instanceof Keyframe.IntKeyframe) {
                    hasInt = true;
                } else {
                    hasOther = true;
                }
            }
            if (hasFloat && !hasInt && !hasOther) {
                return new FloatKeyframeSet(keyframes);
            } else {
                return (KeyframeSet) (hasInt && !hasFloat && !hasOther ? new IntKeyframeSet(keyframes) : new KeyframeSet(keyframes));
            }
        }
    }

    @Override
    public void setEvaluator(TypeEvaluator<?> evaluator) {
        this.mEvaluator = evaluator;
    }

    public KeyframeSet copy() {
        Keyframe[] keyframes = this.mKeyframes;
        int length = keyframes.length;
        Keyframe[] newKeyframes = new Keyframe[length];
        for (int i = 0; i < length; i++) {
            newKeyframes[i] = keyframes[i].copy();
        }
        return new KeyframeSet(newKeyframes);
    }

    @Override
    public Object getValue(float fraction) {
        Keyframe[] keyframes = this.mKeyframes;
        int length = keyframes.length;
        if (length == 2) {
            Keyframe nextKeyframe = keyframes[1];
            TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            return this.mEvaluator.evaluate(fraction, keyframes[0].getValue(), nextKeyframe.getValue());
        } else if (fraction <= 0.0F) {
            Keyframe firstKeyframe = keyframes[0];
            Keyframe nextKeyframe = keyframes[1];
            TimeInterpolator interpolator = nextKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            float prevFraction = firstKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
            return this.mEvaluator.evaluate(intervalFraction, firstKeyframe.getValue(), nextKeyframe.getValue());
        } else if (fraction >= 1.0F) {
            Keyframe prevKeyframe = keyframes[length - 2];
            Keyframe lastKeyframe = keyframes[length - 1];
            TimeInterpolator interpolator = lastKeyframe.getInterpolator();
            if (interpolator != null) {
                fraction = interpolator.getInterpolation(fraction);
            }
            float prevFraction = prevKeyframe.getFraction();
            float intervalFraction = (fraction - prevFraction) / (lastKeyframe.getFraction() - prevFraction);
            return this.mEvaluator.evaluate(intervalFraction, prevKeyframe.getValue(), lastKeyframe.getValue());
        } else {
            Keyframe prevKeyframe = keyframes[0];
            for (int i = 1; i < length; i++) {
                Keyframe nextKeyframe = keyframes[i];
                if (fraction < nextKeyframe.getFraction()) {
                    TimeInterpolator interpolator = nextKeyframe.getInterpolator();
                    float prevFraction = prevKeyframe.getFraction();
                    float intervalFraction = (fraction - prevFraction) / (nextKeyframe.getFraction() - prevFraction);
                    if (interpolator != null) {
                        intervalFraction = interpolator.getInterpolation(intervalFraction);
                    }
                    return this.mEvaluator.evaluate(intervalFraction, prevKeyframe.getValue(), nextKeyframe.getValue());
                }
                prevKeyframe = nextKeyframe;
            }
            return prevKeyframe.getValue();
        }
    }

    @Override
    public Keyframe[] getKeyframes() {
        return this.mKeyframes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(" ");
        for (Keyframe keyframe : this.mKeyframes) {
            sb.append(keyframe.getValue()).append("  ");
        }
        return sb.toString();
    }
}