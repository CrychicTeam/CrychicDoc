package icyllis.modernui.animation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Keyframe {

    boolean mHasValue;

    boolean mValueWasSetOnStart;

    float mFraction;

    @Nullable
    private TimeInterpolator mInterpolator;

    @Nonnull
    public static Keyframe ofInt(float fraction, int value) {
        return new Keyframe.IntKeyframe(fraction, value);
    }

    @Nonnull
    public static Keyframe ofInt(float fraction) {
        return new Keyframe.IntKeyframe(fraction);
    }

    @Nonnull
    public static Keyframe ofFloat(float fraction, float value) {
        return new Keyframe.FloatKeyframe(fraction, value);
    }

    @Nonnull
    public static Keyframe ofFloat(float fraction) {
        return new Keyframe.FloatKeyframe(fraction);
    }

    @Nonnull
    public static Keyframe ofObject(float fraction, Object value) {
        return new Keyframe.ObjectKeyframe(fraction, value);
    }

    @Nonnull
    public static Keyframe ofObject(float fraction) {
        return new Keyframe.ObjectKeyframe(fraction, null);
    }

    public boolean hasValue() {
        return this.mHasValue;
    }

    public abstract Object getValue();

    public abstract void setValue(Object var1);

    public float getFraction() {
        return this.mFraction;
    }

    public void setFraction(float fraction) {
        this.mFraction = fraction;
    }

    @Nullable
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setInterpolator(@Nullable TimeInterpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    @Nonnull
    public abstract Keyframe copy();

    static final class FloatKeyframe extends Keyframe {

        float mValue;

        FloatKeyframe(float fraction, float value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mHasValue = true;
        }

        FloatKeyframe(float fraction) {
            this.mFraction = fraction;
        }

        public float getFloatValue() {
            return this.mValue;
        }

        public Float getValue() {
            return this.mValue;
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Float) {
                this.mValue = (Float) value;
                this.mHasValue = true;
            }
        }

        @Nonnull
        public Keyframe.FloatKeyframe copy() {
            Keyframe.FloatKeyframe kfClone = this.mHasValue ? new Keyframe.FloatKeyframe(this.getFraction(), this.mValue) : new Keyframe.FloatKeyframe(this.getFraction());
            kfClone.setInterpolator(this.getInterpolator());
            kfClone.mValueWasSetOnStart = this.mValueWasSetOnStart;
            return kfClone;
        }
    }

    static final class IntKeyframe extends Keyframe {

        int mValue;

        IntKeyframe(float fraction, int value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mHasValue = true;
        }

        IntKeyframe(float fraction) {
            this.mFraction = fraction;
        }

        public int getIntValue() {
            return this.mValue;
        }

        public Integer getValue() {
            return this.mValue;
        }

        @Override
        public void setValue(Object value) {
            if (value instanceof Integer) {
                this.mValue = (Integer) value;
                this.mHasValue = true;
            }
        }

        @Nonnull
        public Keyframe.IntKeyframe copy() {
            Keyframe.IntKeyframe kfClone = this.mHasValue ? new Keyframe.IntKeyframe(this.getFraction(), this.mValue) : new Keyframe.IntKeyframe(this.getFraction());
            kfClone.setInterpolator(this.getInterpolator());
            kfClone.mValueWasSetOnStart = this.mValueWasSetOnStart;
            return kfClone;
        }
    }

    static final class ObjectKeyframe extends Keyframe {

        Object mValue;

        ObjectKeyframe(float fraction, Object value) {
            this.mFraction = fraction;
            this.mValue = value;
            this.mHasValue = value != null;
        }

        @Override
        public Object getValue() {
            return this.mValue;
        }

        @Override
        public void setValue(Object value) {
            this.mValue = value;
            this.mHasValue = value != null;
        }

        @Nonnull
        public Keyframe.ObjectKeyframe copy() {
            Keyframe.ObjectKeyframe kfClone = new Keyframe.ObjectKeyframe(this.getFraction(), this.hasValue() ? this.mValue : null);
            kfClone.mValueWasSetOnStart = this.mValueWasSetOnStart;
            kfClone.setInterpolator(this.getInterpolator());
            return kfClone;
        }
    }
}