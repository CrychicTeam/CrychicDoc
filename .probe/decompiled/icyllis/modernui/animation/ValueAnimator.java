package icyllis.modernui.animation;

import icyllis.modernui.annotation.CallSuper;
import icyllis.modernui.core.Looper;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class ValueAnimator extends Animator implements AnimationHandler.FrameCallback {

    @Internal
    public static volatile float sDurationScale = 1.0F;

    public static final int RESTART = 1;

    public static final int REVERSE = 2;

    public static final int INFINITE = -1;

    private boolean mRunning = false;

    private boolean mStarted = false;

    private boolean mResumed = false;

    private boolean mReversing;

    private boolean mStartListenersCalled = false;

    boolean mInitialized = false;

    private boolean mAnimationEndRequested = false;

    boolean mStartTimeCommitted;

    long mStartTime = -1L;

    private long mPauseTime;

    float mSeekFraction = -1.0F;

    private long mDuration = 300L;

    private long mStartDelay = 0L;

    private int mRepeatCount = 0;

    private int mRepeatMode = 1;

    private boolean mSelfPulse = true;

    private boolean mSuppressSelfPulseRequested = false;

    private float mOverallFraction = 0.0F;

    private float mCurrentFraction = 0.0F;

    private long mLastFrameTime = -1L;

    @Nonnull
    private TimeInterpolator mInterpolator = TimeInterpolator.ACCELERATE_DECELERATE;

    @Nullable
    ArrayList<ValueAnimator.AnimatorUpdateListener> mUpdateListeners;

    PropertyValuesHolder[] mValues;

    @Nonnull
    public static ValueAnimator ofInt(@Nonnull int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setValues(PropertyValuesHolder.ofInt(values));
        return anim;
    }

    @Nonnull
    public static ValueAnimator ofArgb(@Nonnull int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setValues(PropertyValuesHolder.ofInt(values));
        anim.setEvaluator(ColorEvaluator.getInstance());
        return anim;
    }

    @Nonnull
    public static ValueAnimator ofFloat(@Nonnull float... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setValues(PropertyValuesHolder.ofFloat(values));
        return anim;
    }

    @Nonnull
    @SafeVarargs
    public static <V> ValueAnimator ofObject(@Nonnull TypeEvaluator<V> evaluator, @Nonnull V... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setValues(PropertyValuesHolder.ofObject(evaluator, values));
        return anim;
    }

    public void setValues(@Nonnull PropertyValuesHolder... values) {
        this.mValues = values;
        this.mInitialized = false;
    }

    @Nonnull
    public PropertyValuesHolder[] getValues() {
        return this.mValues;
    }

    void initAnimation() {
        if (!this.mInitialized) {
            for (PropertyValuesHolder value : this.mValues) {
                value.init();
            }
            this.mInitialized = true;
        }
    }

    public ValueAnimator setDuration(long duration) {
        if (duration < 0L) {
            duration = 0L;
        }
        this.mDuration = duration;
        return this;
    }

    private long getScaledDuration() {
        return (long) ((float) this.mDuration * sDurationScale);
    }

    @Override
    public long getDuration() {
        return this.mDuration;
    }

    @Override
    public long getTotalDuration() {
        return this.mRepeatCount == -1 ? -1L : this.mStartDelay + this.mDuration * (long) (this.mRepeatCount + 1);
    }

    public void setCurrentPlayTime(long playTime) {
        float fraction = this.mDuration > 0L ? (float) playTime / (float) this.mDuration : 1.0F;
        this.setCurrentFraction(fraction);
    }

    public void setCurrentFraction(float fraction) {
        this.initAnimation();
        fraction = this.clampFraction(fraction);
        this.mStartTimeCommitted = true;
        if (this.isPulsingInternal()) {
            long seekTime = (long) ((float) this.getScaledDuration() * fraction);
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStartTime = currentTime - seekTime;
        } else {
            this.mSeekFraction = fraction;
        }
        this.mOverallFraction = fraction;
        float currentIterationFraction = this.getCurrentIterationFraction(fraction, this.mReversing);
        this.animateValue(currentIterationFraction);
    }

    private int getCurrentIteration(float fraction) {
        fraction = this.clampFraction(fraction);
        double iteration = Math.floor((double) fraction);
        if ((double) fraction == iteration && fraction > 0.0F) {
            iteration--;
        }
        return (int) iteration;
    }

    private float getCurrentIterationFraction(float fraction, boolean inReverse) {
        fraction = this.clampFraction(fraction);
        int iteration = this.getCurrentIteration(fraction);
        float currentFraction = fraction - (float) iteration;
        return this.shouldPlayBackward(iteration, inReverse) ? 1.0F - currentFraction : currentFraction;
    }

    private float clampFraction(float fraction) {
        if (fraction < 0.0F) {
            fraction = 0.0F;
        } else if (this.mRepeatCount != -1) {
            fraction = Math.min(fraction, (float) (this.mRepeatCount + 1));
        }
        return fraction;
    }

    private boolean shouldPlayBackward(int iteration, boolean inReverse) {
        if (iteration > 0 && this.mRepeatMode == 2 && (iteration < this.mRepeatCount + 1 || this.mRepeatCount == -1)) {
            return inReverse ? iteration % 2 == 0 : iteration % 2 != 0;
        } else {
            return inReverse;
        }
    }

    public long getCurrentPlayTime() {
        if (this.mInitialized && (this.mStarted || !(this.mSeekFraction < 0.0F))) {
            if (this.mSeekFraction >= 0.0F) {
                return (long) ((float) this.mDuration * this.mSeekFraction);
            } else {
                float durationScale = sDurationScale;
                if (durationScale == 0.0F) {
                    durationScale = 1.0F;
                }
                return (long) ((float) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime) / durationScale);
            }
        } else {
            return 0L;
        }
    }

    @Override
    public void setStartDelay(long startDelay) {
        if (startDelay < 0L) {
            startDelay = 0L;
        }
        this.mStartDelay = startDelay;
    }

    @Override
    public long getStartDelay() {
        return this.mStartDelay;
    }

    public Object getAnimatedValue() {
        return this.mValues != null && this.mValues.length > 0 ? this.mValues[0].getAnimatedValue() : null;
    }

    public void setRepeatCount(int value) {
        this.mRepeatCount = value;
    }

    public int getRepeatCount() {
        return this.mRepeatCount;
    }

    public void setRepeatMode(int value) {
        this.mRepeatMode = value;
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public void addUpdateListener(@Nonnull ValueAnimator.AnimatorUpdateListener listener) {
        if (this.mUpdateListeners == null) {
            this.mUpdateListeners = new ArrayList();
        }
        this.mUpdateListeners.add(listener);
    }

    public void removeAllUpdateListeners() {
        if (this.mUpdateListeners != null) {
            this.mUpdateListeners.clear();
            this.mUpdateListeners = null;
        }
    }

    public void removeUpdateListener(@Nonnull ValueAnimator.AnimatorUpdateListener listener) {
        if (this.mUpdateListeners != null) {
            this.mUpdateListeners.remove(listener);
            if (this.mUpdateListeners.isEmpty()) {
                this.mUpdateListeners = null;
            }
        }
    }

    @Override
    public void setInterpolator(@Nullable TimeInterpolator value) {
        this.mInterpolator = (TimeInterpolator) Objects.requireNonNullElse(value, TimeInterpolator.LINEAR);
    }

    @Nonnull
    @Override
    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setEvaluator(TypeEvaluator<?> value) {
        if (value != null && this.mValues != null && this.mValues.length > 0) {
            this.mValues[0].setEvaluator(value);
        }
    }

    private void notifyStartListeners() {
        if (this.mListeners != null && !this.mStartListenersCalled) {
            for (AnimatorListener l : this.mListeners) {
                l.onAnimationStart(this, this.mReversing);
            }
        }
        this.mStartListenersCalled = true;
    }

    private void start(boolean playBackwards) {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else {
            this.mReversing = playBackwards;
            this.mSelfPulse = !this.mSuppressSelfPulseRequested;
            if (playBackwards && this.mSeekFraction != -1.0F && this.mSeekFraction != 0.0F) {
                if (this.mRepeatCount == -1) {
                    float fraction = (float) ((double) this.mSeekFraction - Math.floor((double) this.mSeekFraction));
                    this.mSeekFraction = 1.0F - fraction;
                } else {
                    this.mSeekFraction = (float) (1 + this.mRepeatCount) - this.mSeekFraction;
                }
            }
            this.mStarted = true;
            this.mPaused = false;
            this.mRunning = false;
            this.mAnimationEndRequested = false;
            this.mLastFrameTime = -1L;
            this.mStartTime = -1L;
            this.addAnimationCallback();
            if (this.mStartDelay == 0L || this.mSeekFraction >= 0.0F || this.mReversing) {
                this.startAnimation();
                if (this.mSeekFraction == -1.0F) {
                    this.setCurrentPlayTime(0L);
                } else {
                    this.setCurrentFraction(this.mSeekFraction);
                }
            }
        }
    }

    @Override
    void startWithoutPulsing(boolean inReverse) {
        this.mSuppressSelfPulseRequested = true;
        if (inReverse) {
            this.reverse();
        } else {
            this.start();
        }
        this.mSuppressSelfPulseRequested = false;
    }

    @Override
    public void start() {
        this.start(false);
    }

    @Override
    public void cancel() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else if (!this.mAnimationEndRequested) {
            if ((this.mStarted || this.mRunning) && this.mListeners != null) {
                if (!this.mRunning) {
                    this.notifyStartListeners();
                }
                for (AnimatorListener l : this.mListeners) {
                    l.onAnimationCancel(this);
                }
            }
            this.endAnimation();
        }
    }

    @Override
    public void end() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be run on Looper threads");
        } else {
            if (!this.mRunning) {
                this.startAnimation();
                this.mStarted = true;
            } else if (!this.mInitialized) {
                this.initAnimation();
            }
            this.animateValue(this.shouldPlayBackward(this.mRepeatCount, this.mReversing) ? 0.0F : 1.0F);
            this.endAnimation();
        }
    }

    @Override
    public void resume() {
        if (Looper.myLooper() == null) {
            throw new RuntimeException("Animators may only be resumed from the same thread that the animator was started on");
        } else {
            if (this.mPaused && !this.mResumed) {
                this.mResumed = true;
                if (this.mPauseTime > 0L) {
                    this.addAnimationCallback();
                }
            }
            super.resume();
        }
    }

    @Override
    public void pause() {
        boolean previouslyPaused = this.mPaused;
        super.pause();
        if (!previouslyPaused && this.mPaused) {
            this.mPauseTime = -1L;
            this.mResumed = false;
        }
    }

    @Override
    public boolean isRunning() {
        return this.mRunning;
    }

    @Override
    public boolean isStarted() {
        return this.mStarted;
    }

    @Override
    public void reverse() {
        if (this.isPulsingInternal()) {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            long currentPlayTime = currentTime - this.mStartTime;
            long timeLeft = this.getScaledDuration() - currentPlayTime;
            this.mStartTime = currentTime - timeLeft;
            this.mStartTimeCommitted = true;
            this.mReversing = !this.mReversing;
        } else if (this.mStarted) {
            this.mReversing = !this.mReversing;
            this.end();
        } else {
            this.start(true);
        }
    }

    @Override
    public boolean canReverse() {
        return true;
    }

    private void endAnimation() {
        if (!this.mAnimationEndRequested) {
            this.removeAnimationCallback();
            this.mAnimationEndRequested = true;
            this.mPaused = false;
            boolean notify = (this.mStarted || this.mRunning) && this.mListeners != null;
            if (notify && !this.mRunning) {
                this.notifyStartListeners();
            }
            this.mRunning = false;
            this.mStarted = false;
            this.mStartListenersCalled = false;
            this.mLastFrameTime = -1L;
            this.mStartTime = -1L;
            if (notify && this.mListeners != null) {
                for (AnimatorListener l : this.mListeners) {
                    l.onAnimationEnd(this, this.mReversing);
                }
            }
            this.mReversing = false;
        }
    }

    private void startAnimation() {
        this.mAnimationEndRequested = false;
        this.initAnimation();
        this.mRunning = true;
        if (this.mSeekFraction >= 0.0F) {
            this.mOverallFraction = this.mSeekFraction;
        } else {
            this.mOverallFraction = 0.0F;
        }
        if (this.mListeners != null) {
            this.notifyStartListeners();
        }
    }

    private boolean isPulsingInternal() {
        return this.mLastFrameTime >= 0L;
    }

    public void commitAnimationFrame(long frameTime) {
        if (!this.mStartTimeCommitted) {
            this.mStartTimeCommitted = true;
            long adjustment = frameTime - this.mLastFrameTime;
            if (adjustment > 0L) {
                this.mStartTime += adjustment;
            }
        }
    }

    boolean animateBasedOnTime(long currentTime) {
        boolean done = false;
        if (this.mRunning) {
            long scaledDuration = this.getScaledDuration();
            float fraction = scaledDuration > 0L ? (float) (currentTime - this.mStartTime) / (float) scaledDuration : 1.0F;
            float lastFraction = this.mOverallFraction;
            boolean newIteration = (int) fraction > (int) lastFraction;
            boolean lastIterationFinished = fraction >= (float) (this.mRepeatCount + 1) && this.mRepeatCount != -1;
            if (scaledDuration == 0L) {
                done = true;
            } else if (newIteration && !lastIterationFinished) {
                if (this.mListeners != null) {
                    for (AnimatorListener listener : this.mListeners) {
                        listener.onAnimationRepeat(this);
                    }
                }
            } else if (lastIterationFinished) {
                done = true;
            }
            this.mOverallFraction = this.clampFraction(fraction);
            float currentIterationFraction = this.getCurrentIterationFraction(this.mOverallFraction, this.mReversing);
            this.animateValue(currentIterationFraction);
        }
        return done;
    }

    @Override
    void animateBasedOnPlayTime(long currentPlayTime, long lastPlayTime, boolean inReverse) {
        if (currentPlayTime >= 0L && lastPlayTime >= 0L) {
            this.initAnimation();
            if (this.mRepeatCount > 0) {
                int iteration = (int) (currentPlayTime / this.mDuration);
                int lastIteration = (int) (lastPlayTime / this.mDuration);
                iteration = Math.min(iteration, this.mRepeatCount);
                lastIteration = Math.min(lastIteration, this.mRepeatCount);
                if (iteration != lastIteration && this.mListeners != null) {
                    for (AnimatorListener listener : this.mListeners) {
                        listener.onAnimationRepeat(this);
                    }
                }
            }
            if (this.mRepeatCount != -1 && currentPlayTime >= (long) (this.mRepeatCount + 1) * this.mDuration) {
                this.skipToEndValue(inReverse);
            } else {
                float fraction = (float) currentPlayTime / (float) this.mDuration;
                fraction = this.getCurrentIterationFraction(fraction, inReverse);
                this.animateValue(fraction);
            }
        } else {
            throw new UnsupportedOperationException("Error: Play time should never be negative.");
        }
    }

    @Override
    void skipToEndValue(boolean inReverse) {
        this.initAnimation();
        float endFraction = inReverse ? 0.0F : 1.0F;
        if (this.mRepeatCount % 2 == 1 && this.mRepeatMode == 2) {
            endFraction = 0.0F;
        }
        this.animateValue(endFraction);
    }

    @Override
    boolean isInitialized() {
        return this.mInitialized;
    }

    @Override
    public final boolean doAnimationFrame(long frameTime) {
        if (this.mStartTime < 0L) {
            this.mStartTime = this.mReversing ? frameTime : frameTime + (long) ((float) this.mStartDelay * sDurationScale);
        }
        if (this.mPaused) {
            this.mPauseTime = frameTime;
            this.removeAnimationCallback();
            return false;
        } else {
            if (this.mResumed) {
                this.mResumed = false;
                if (this.mPauseTime > 0L) {
                    this.mStartTime = this.mStartTime + (frameTime - this.mPauseTime);
                }
            }
            if (!this.mRunning) {
                if (this.mStartTime > frameTime && this.mSeekFraction == -1.0F) {
                    return false;
                }
                this.mRunning = true;
                this.startAnimation();
            }
            if (this.mLastFrameTime < 0L) {
                if (this.mSeekFraction >= 0.0F) {
                    long seekTime = (long) ((float) this.getScaledDuration() * this.mSeekFraction);
                    this.mStartTime = frameTime - seekTime;
                    this.mSeekFraction = -1.0F;
                }
                this.mStartTimeCommitted = false;
            }
            this.mLastFrameTime = frameTime;
            long currentTime = Math.max(frameTime, this.mStartTime);
            boolean finished = this.animateBasedOnTime(currentTime);
            if (finished) {
                this.endAnimation();
            }
            return finished;
        }
    }

    @Override
    boolean pulseAnimationFrame(long frameTime) {
        return this.mSelfPulse ? false : this.doAnimationFrame(frameTime);
    }

    private void removeAnimationCallback() {
        if (this.mSelfPulse) {
            AnimationHandler.getInstance().removeCallback(this);
        }
    }

    private void addAnimationCallback() {
        if (this.mSelfPulse) {
            AnimationHandler.getInstance().addFrameCallback(this, 0L);
        }
    }

    public float getAnimatedFraction() {
        return this.mCurrentFraction;
    }

    @CallSuper
    void animateValue(float fraction) {
        fraction = this.mInterpolator.getInterpolation(fraction);
        this.mCurrentFraction = fraction;
        for (PropertyValuesHolder value : this.mValues) {
            value.calculateValue(fraction);
        }
        if (this.mUpdateListeners != null) {
            for (ValueAnimator.AnimatorUpdateListener l : this.mUpdateListeners) {
                l.onAnimationUpdate(this);
            }
        }
    }

    public ValueAnimator clone() {
        ValueAnimator anim = (ValueAnimator) super.clone();
        if (this.mUpdateListeners != null) {
            anim.mUpdateListeners = new ArrayList(this.mUpdateListeners);
        }
        anim.mSeekFraction = -1.0F;
        anim.mReversing = false;
        anim.mInitialized = false;
        anim.mStarted = false;
        anim.mRunning = false;
        anim.mPaused = false;
        anim.mResumed = false;
        anim.mStartListenersCalled = false;
        anim.mStartTime = -1L;
        anim.mStartTimeCommitted = false;
        anim.mAnimationEndRequested = false;
        anim.mPauseTime = -1L;
        anim.mLastFrameTime = -1L;
        anim.mOverallFraction = 0.0F;
        anim.mCurrentFraction = 0.0F;
        anim.mSelfPulse = true;
        anim.mSuppressSelfPulseRequested = false;
        PropertyValuesHolder[] oldValues = this.mValues;
        if (oldValues != null) {
            int numValues = oldValues.length;
            anim.mValues = new PropertyValuesHolder[numValues];
            for (int i = 0; i < numValues; i++) {
                anim.mValues[i] = oldValues[i].clone();
            }
        }
        return anim;
    }

    @FunctionalInterface
    public interface AnimatorUpdateListener {

        void onAnimationUpdate(@Nonnull ValueAnimator var1);
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface RepeatMode {
    }
}