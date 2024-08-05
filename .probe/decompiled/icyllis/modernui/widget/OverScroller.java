package icyllis.modernui.widget;

import icyllis.modernui.animation.AnimationUtils;
import icyllis.modernui.animation.TimeInterpolator;
import java.util.Objects;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class OverScroller {

    private static final int DEFAULT_DURATION = 200;

    private final OverScroller.SplineScroller mScrollerX = new OverScroller.SplineScroller();

    private final OverScroller.SplineScroller mScrollerY = new OverScroller.SplineScroller();

    private TimeInterpolator mInterpolator;

    private final boolean mFlywheel;

    private boolean mFlingMode;

    public OverScroller() {
        this(null);
    }

    public OverScroller(@Nullable TimeInterpolator interpolator) {
        this(interpolator, true);
    }

    public OverScroller(@Nullable TimeInterpolator interpolator, boolean flywheel) {
        this.mInterpolator = (TimeInterpolator) Objects.requireNonNullElse(interpolator, TimeInterpolator.DECELERATE);
        this.mFlywheel = flywheel;
    }

    void setInterpolator(@Nullable TimeInterpolator interpolator) {
        this.mInterpolator = (TimeInterpolator) Objects.requireNonNullElse(interpolator, TimeInterpolator.VISCOUS_FLUID);
    }

    public final void setFriction(float friction) {
        this.mScrollerX.setFriction(friction);
        this.mScrollerY.setFriction(friction);
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public final void forceFinished(boolean finished) {
        this.mScrollerX.mFinished = this.mScrollerY.mFinished = finished;
    }

    public final int getDuration() {
        return this.mScrollerX.mDuration;
    }

    @Internal
    public void setFinalX(int newX) {
        this.mScrollerX.setFinalPosition(newX);
    }

    @Internal
    public void setFinalY(int newY) {
        this.mScrollerY.setFinalPosition(newY);
    }

    public final int getCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot((double) this.mScrollerX.mCurrVelocity, (double) this.mScrollerY.mCurrVelocity);
    }

    public final int getStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return this.mScrollerY.mStart;
    }

    public final int getFinalX() {
        return this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return this.mScrollerY.mFinal;
    }

    public boolean computeScrollOffset() {
        if (this.isFinished()) {
            return false;
        } else {
            if (this.mFlingMode) {
                if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && this.mScrollerX.continueWhenFinished()) {
                    this.mScrollerX.finish();
                }
                if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && this.mScrollerY.continueWhenFinished()) {
                    this.mScrollerY.finish();
                }
            } else {
                long time = AnimationUtils.currentAnimationTimeMillis();
                long elapsedTime = time - this.mScrollerX.mStartTime;
                int duration = this.mScrollerX.mDuration;
                if (elapsedTime < (long) duration) {
                    float q = this.mInterpolator.getInterpolation((float) elapsedTime / (float) duration);
                    this.mScrollerX.updateScroll(q);
                    this.mScrollerY.updateScroll(q);
                } else {
                    this.abortAnimation();
                }
            }
            return true;
        }
    }

    public void startScroll(int startX, int startY, int dx, int dy) {
        int dis = Math.max(Math.abs(dy), Math.abs(dx));
        int duration;
        if ((double) dis > 160.0) {
            duration = (int) (Math.sqrt((double) dis / 160.0) * 200.0);
        } else {
            duration = 200;
        }
        double d = (double) (AnimationUtils.currentAnimationTimeMillis() - this.mScrollerX.mStartTime);
        if (d < 120.0) {
            duration = (int) ((double) duration * (0.2 * d / 120.0 + 0.8));
        }
        this.startScroll(startX, startY, dx, dy, duration);
    }

    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        this.mFlingMode = false;
        this.mScrollerX.startScroll(startX, dx, duration);
        this.mScrollerY.startScroll(startY, dy, duration);
    }

    public boolean springBack(int startX, int startY, int minX, int maxX, int minY, int maxY) {
        this.mFlingMode = true;
        boolean springbackX = this.mScrollerX.springback(startX, minX, maxX);
        boolean springbackY = this.mScrollerY.springback(startY, minY, maxY);
        return springbackX || springbackY;
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY) {
        this.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY, 0, 0);
    }

    public void fling(int startX, int startY, int velocityX, int velocityY, int minX, int maxX, int minY, int maxY, int overX, int overY) {
        if (this.mFlywheel && !this.isFinished()) {
            float oldVelocityX = this.mScrollerX.mCurrVelocity;
            float oldVelocityY = this.mScrollerY.mCurrVelocity;
            if (Math.signum((float) velocityX) == Math.signum(oldVelocityX) && Math.signum((float) velocityY) == Math.signum(oldVelocityY)) {
                velocityX = (int) ((float) velocityX + oldVelocityX);
                velocityY = (int) ((float) velocityY + oldVelocityY);
            }
        }
        this.mFlingMode = true;
        this.mScrollerX.fling(startX, velocityX, minX, maxX, overX);
        this.mScrollerY.fling(startY, velocityY, minY, maxY, overY);
    }

    public void notifyHorizontalEdgeReached(int startX, int finalX, int overX) {
        this.mScrollerX.notifyEdgeReached(startX, finalX, overX);
    }

    public void notifyVerticalEdgeReached(int startY, int finalY, int overY) {
        this.mScrollerY.notifyEdgeReached(startY, finalY, overY);
    }

    public boolean isOverScrolled() {
        return !this.mScrollerX.mFinished && this.mScrollerX.mState != 0 || !this.mScrollerY.mFinished && this.mScrollerY.mState != 0;
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public int timePassed() {
        long time = AnimationUtils.currentAnimationTimeMillis();
        long startTime = Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime);
        return (int) (time - startTime);
    }

    public boolean isScrollingInDirection(float xvel, float yvel) {
        int dx = this.mScrollerX.mFinal - this.mScrollerX.mStart;
        int dy = this.mScrollerY.mFinal - this.mScrollerY.mStart;
        return !this.isFinished() && Math.signum(xvel) == Math.signum((float) dx) && Math.signum(yvel) == Math.signum((float) dy);
    }

    double getSplineFlingDistance(int velocity) {
        return this.mScrollerY.getSplineFlingDistance(velocity);
    }

    private static class SplineScroller {

        private static final float SCROLL_FRICTION = 0.015F;

        private static final float PHYSICAL_COEFF = 51890.203F;

        private static final float GRAVITY = 2000.0F;

        private static final float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));

        private static final float INFLEXION = 0.35F;

        private static final float START_TENSION = 0.5F;

        private static final float END_TENSION = 1.0F;

        private static final float P1 = 0.175F;

        private static final float P2 = 0.35000002F;

        private static final int NB_SAMPLES = 100;

        private static final float[] SPLINE_POSITION = new float[101];

        private static final float[] SPLINE_TIME = new float[101];

        private static final int SPLINE = 0;

        private static final int CUBIC = 1;

        private static final int BALLISTIC = 2;

        private int mStart;

        private int mCurrentPosition;

        private int mFinal;

        private int mVelocity;

        private float mCurrVelocity;

        private float mDeceleration;

        private long mStartTime;

        private int mDuration;

        private int mSplineDuration;

        private int mSplineDistance;

        private boolean mFinished = true;

        private int mOver;

        private float mFlingFriction = 0.015F;

        private int mState = 0;

        private static float getDeceleration(int velocity) {
            return velocity > 0 ? -2000.0F : 2000.0F;
        }

        void updateScroll(float q) {
            this.mCurrentPosition = this.mStart + Math.round(q * (float) (this.mFinal - this.mStart));
        }

        void setFriction(float friction) {
            this.mFlingFriction = friction;
        }

        private void adjustDuration(int start, int oldFinal, int newFinal) {
            int oldDistance = oldFinal - start;
            int newDistance = newFinal - start;
            float x = Math.abs((float) newDistance / (float) oldDistance);
            int index = (int) (100.0F * x);
            if (index < 100) {
                float x_inf = (float) index / 100.0F;
                float x_sup = (float) (index + 1) / 100.0F;
                float t_inf = SPLINE_TIME[index];
                float t_sup = SPLINE_TIME[index + 1];
                float timeCoef = t_inf + (x - x_inf) / (x_sup - x_inf) * (t_sup - t_inf);
                this.mDuration = (int) ((float) this.mDuration * timeCoef);
            }
        }

        void startScroll(int start, int distance, int duration) {
            this.mFinished = false;
            this.mCurrentPosition = this.mStart = start;
            this.mFinal = start + distance;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = duration;
            this.mDeceleration = 0.0F;
            this.mVelocity = 0;
        }

        void finish() {
            this.mCurrentPosition = this.mFinal;
            this.mCurrVelocity = 0.0F;
            this.mFinished = true;
        }

        void setFinalPosition(int position) {
            this.mFinal = position;
            this.mSplineDistance = this.mFinal - this.mStart;
            this.mFinished = false;
        }

        boolean springback(int start, int min, int max) {
            this.mFinished = true;
            this.mCurrentPosition = this.mStart = this.mFinal = start;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (start < min) {
                this.startSpringback(start, min, 0);
            } else if (start > max) {
                this.startSpringback(start, max, 0);
            }
            return !this.mFinished;
        }

        private void startSpringback(int start, int end, int velocity) {
            this.mFinished = false;
            this.mState = 1;
            this.mCurrentPosition = this.mStart = start;
            this.mFinal = end;
            int delta = start - end;
            this.mDeceleration = getDeceleration(delta);
            this.mVelocity = -delta;
            this.mOver = Math.abs(delta);
            this.mDuration = (int) (1000.0 * Math.sqrt(-2.0 * (double) delta / (double) this.mDeceleration));
        }

        void fling(int start, int velocity, int min, int max, int over) {
            this.mOver = over;
            this.mFinished = false;
            this.mCurrVelocity = (float) (this.mVelocity = velocity);
            this.mDuration = this.mSplineDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mCurrentPosition = this.mStart = start;
            if (start <= max && start >= min) {
                this.mState = 0;
                double totalDistance = 0.0;
                if (velocity != 0) {
                    this.mDuration = this.mSplineDuration = this.getSplineFlingDuration(velocity);
                    totalDistance = this.getSplineFlingDistance(velocity);
                }
                this.mSplineDistance = (int) (totalDistance * (double) Math.signum((float) velocity));
                this.mFinal = start + this.mSplineDistance;
                if (this.mFinal < min) {
                    this.adjustDuration(this.mStart, this.mFinal, min);
                    this.mFinal = min;
                }
                if (this.mFinal > max) {
                    this.adjustDuration(this.mStart, this.mFinal, max);
                    this.mFinal = max;
                }
            } else {
                this.startAfterEdge(start, min, max, velocity);
            }
        }

        private double getSplineDeceleration(int velocity) {
            return Math.log((double) (0.35F * (float) Math.abs(velocity) / (this.mFlingFriction * 51890.203F)));
        }

        private double getSplineFlingDistance(int velocity) {
            double l = this.getSplineDeceleration(velocity);
            double decelMinusOne = (double) DECELERATION_RATE - 1.0;
            return (double) (this.mFlingFriction * 51890.203F) * Math.exp((double) DECELERATION_RATE / decelMinusOne * l);
        }

        private int getSplineFlingDuration(int velocity) {
            double l = this.getSplineDeceleration(velocity);
            double decelMinusOne = (double) DECELERATION_RATE - 1.0;
            return (int) (1000.0 * Math.exp(l / decelMinusOne));
        }

        private void fitOnBounceCurve(int start, int end, int velocity) {
            float durationToApex = (float) (-velocity) / this.mDeceleration;
            float velocitySquared = (float) velocity * (float) velocity;
            float distanceToApex = velocitySquared / 2.0F / Math.abs(this.mDeceleration);
            float distanceToEdge = (float) Math.abs(end - start);
            float totalDuration = (float) Math.sqrt(2.0 * (double) (distanceToApex + distanceToEdge) / (double) Math.abs(this.mDeceleration));
            this.mStartTime -= (long) ((int) (1000.0F * (totalDuration - durationToApex)));
            this.mCurrentPosition = this.mStart = end;
            this.mVelocity = (int) (-this.mDeceleration * totalDuration);
        }

        private void startBounceAfterEdge(int start, int end, int velocity) {
            this.mDeceleration = getDeceleration(velocity == 0 ? start - end : velocity);
            this.fitOnBounceCurve(start, end, velocity);
            this.onEdgeReached();
        }

        private void startAfterEdge(int start, int min, int max, int velocity) {
            if (start > min && start < max) {
                this.mFinished = true;
            } else {
                boolean positive = start > max;
                int edge = positive ? max : min;
                int overDistance = start - edge;
                boolean keepIncreasing = overDistance * velocity >= 0;
                if (keepIncreasing) {
                    this.startBounceAfterEdge(start, edge, velocity);
                } else {
                    double totalDistance = this.getSplineFlingDistance(velocity);
                    if (totalDistance > (double) Math.abs(overDistance)) {
                        this.fling(start, velocity, positive ? min : start, positive ? start : max, this.mOver);
                    } else {
                        this.startSpringback(start, edge, velocity);
                    }
                }
            }
        }

        void notifyEdgeReached(int start, int end, int over) {
            if (this.mState == 0) {
                this.mOver = over;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                this.startAfterEdge(start, end, end, (int) this.mCurrVelocity);
            }
        }

        private void onEdgeReached() {
            float velocitySquared = (float) this.mVelocity * (float) this.mVelocity;
            float distance = velocitySquared / (2.0F * Math.abs(this.mDeceleration));
            float sign = Math.signum((float) this.mVelocity);
            if (distance > (float) this.mOver) {
                this.mDeceleration = -sign * velocitySquared / (2.0F * (float) this.mOver);
                distance = (float) this.mOver;
            }
            this.mOver = (int) distance;
            this.mState = 2;
            this.mFinal = this.mStart + (int) (this.mVelocity > 0 ? distance : -distance);
            this.mDuration = -((int) (1000.0F * (float) this.mVelocity / this.mDeceleration));
        }

        boolean continueWhenFinished() {
            switch(this.mState) {
                case 0:
                    if (this.mDuration >= this.mSplineDuration) {
                        return true;
                    }
                    this.mCurrentPosition = this.mStart = this.mFinal;
                    this.mVelocity = (int) this.mCurrVelocity;
                    this.mDeceleration = getDeceleration(this.mVelocity);
                    this.mStartTime = this.mStartTime + (long) this.mDuration;
                    this.onEdgeReached();
                    break;
                case 1:
                    return true;
                case 2:
                    this.mStartTime = this.mStartTime + (long) this.mDuration;
                    this.startSpringback(this.mFinal, this.mStart, 0);
            }
            this.update();
            return false;
        }

        boolean update() {
            long time = AnimationUtils.currentAnimationTimeMillis();
            long currentTime = time - this.mStartTime;
            if (currentTime == 0L) {
                return this.mDuration > 0;
            } else if (currentTime > (long) this.mDuration) {
                return false;
            } else {
                double distance = 0.0;
                switch(this.mState) {
                    case 0:
                        {
                            float t = (float) currentTime / (float) this.mSplineDuration;
                            int index = (int) (100.0F * t);
                            float distanceCoef = 1.0F;
                            float velocityCoef = 0.0F;
                            if (index < 100) {
                                float t_inf = (float) index / 100.0F;
                                float t_sup = (float) (index + 1) / 100.0F;
                                float d_inf = SPLINE_POSITION[index];
                                float d_sup = SPLINE_POSITION[index + 1];
                                velocityCoef = (d_sup - d_inf) / (t_sup - t_inf);
                                distanceCoef = d_inf + (t - t_inf) * velocityCoef;
                            }
                            distance = (double) (distanceCoef * (float) this.mSplineDistance);
                            this.mCurrVelocity = velocityCoef * (float) this.mSplineDistance / (float) this.mSplineDuration * 1000.0F;
                            break;
                        }
                    case 1:
                        {
                            float t = (float) currentTime / (float) this.mDuration;
                            float t2 = t * t;
                            float sign = Math.signum((float) this.mVelocity);
                            distance = (double) (sign * (float) this.mOver * (3.0F * t2 - 2.0F * t * t2));
                            this.mCurrVelocity = sign * (float) this.mOver * 6.0F * (-t + t2);
                            break;
                        }
                    case 2:
                        {
                            float t = (float) currentTime / 1000.0F;
                            this.mCurrVelocity = (float) this.mVelocity + this.mDeceleration * t;
                            distance = (double) ((float) this.mVelocity * t + this.mDeceleration * t * t / 2.0F);
                        }
                }
                this.mCurrentPosition = this.mStart + (int) Math.round(distance);
                return true;
            }
        }

        static {
            float x_min = 0.0F;
            float y_min = 0.0F;
            label36: for (int i = 0; i < 100; i++) {
                float alpha = (float) i / 100.0F;
                float x_max = 1.0F;
                while (true) {
                    float x = x_min + (x_max - x_min) / 2.0F;
                    float coef = 3.0F * x * (1.0F - x);
                    float tx = coef * ((1.0F - x) * 0.175F + x * 0.35000002F) + x * x * x;
                    if ((double) Math.abs(tx - alpha) < 1.0E-5) {
                        SPLINE_POSITION[i] = coef * ((1.0F - x) * 0.5F + x) + x * x * x;
                        float y_max = 1.0F;
                        while (true) {
                            float y = y_min + (y_max - y_min) / 2.0F;
                            coef = 3.0F * y * (1.0F - y);
                            float dy = coef * ((1.0F - y) * 0.5F + y) + y * y * y;
                            if ((double) Math.abs(dy - alpha) < 1.0E-5) {
                                SPLINE_TIME[i] = coef * ((1.0F - y) * 0.175F + y * 0.35000002F) + y * y * y;
                                continue label36;
                            }
                            if (dy > alpha) {
                                y_max = y;
                            } else {
                                y_min = y;
                            }
                        }
                    }
                    if (tx > alpha) {
                        x_max = x;
                    } else {
                        x_min = x;
                    }
                }
            }
            SPLINE_POSITION[100] = SPLINE_TIME[100] = 1.0F;
        }
    }
}