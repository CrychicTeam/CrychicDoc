package icyllis.modernui.widget;

import icyllis.modernui.animation.AnimationUtils;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import javax.annotation.Nonnull;

public class EdgeEffect {

    private static final int RECEDE_TIME = 600;

    private static final int PULL_TIME = 167;

    private static final int PULL_DECAY_TIME = 2000;

    private static final float MAX_ALPHA = 0.15F;

    private static final float GLOW_ALPHA_START = 0.09F;

    private static final int MIN_VELOCITY = 100;

    private static final int MAX_VELOCITY = 10000;

    private static final float EPSILON = 0.001F;

    private static final double ANGLE = Math.PI / 6;

    private static final float SIN = (float) Math.sin(Math.PI / 6);

    private static final float COS = (float) Math.cos(Math.PI / 6);

    private static final float RADIUS_FACTOR = 0.6F;

    private float mGlowAlpha;

    private float mGlowScaleY;

    private float mDistance;

    private float mGlowAlphaStart;

    private float mGlowAlphaFinish;

    private float mGlowScaleYStart;

    private float mGlowScaleYFinish;

    private long mStartTime;

    private float mDuration;

    private static final int STATE_IDLE = 0;

    private static final int STATE_PULL = 1;

    private static final int STATE_ABSORB = 2;

    private static final int STATE_RECEDE = 3;

    private static final int STATE_PULL_DECAY = 4;

    private static final float PULL_DISTANCE_ALPHA_GLOW_FACTOR = 0.8F;

    private static final int VELOCITY_GLOW_FACTOR = 6;

    private int mState = 0;

    private float mPullDistance;

    private float mWidth;

    private float mHeight;

    private final Paint mPaint = new Paint();

    private float mRadius;

    private float mBaseGlowScale;

    private float mDisplacement = 0.5F;

    private float mTargetDisplacement = 0.5F;

    public EdgeEffect() {
        this.mPaint.setColor(862348902);
        this.mPaint.setStyle(0);
    }

    public void setSize(int width, int height) {
        float r = (float) width * 0.6F / SIN;
        float y = COS * r;
        float h = r - y;
        float or = (float) height * 0.6F / SIN;
        float oy = COS * or;
        float oh = or - oy;
        this.mRadius = r;
        this.mBaseGlowScale = h > 0.0F ? Math.min(oh / h, 1.0F) : 1.0F;
        this.mWidth = (float) width;
        this.mHeight = (float) ((int) Math.min((float) height, h));
    }

    public boolean isFinished() {
        return this.mState == 0;
    }

    public void finish() {
        this.mState = 0;
        this.mDistance = 0.0F;
    }

    public void onPull(float deltaDistance) {
        this.onPull(deltaDistance, 0.5F);
    }

    public void onPull(float deltaDistance, float displacement) {
        long now = AnimationUtils.currentAnimationTimeMillis();
        this.mTargetDisplacement = displacement;
        if (this.mState != 4 || !((float) (now - this.mStartTime) < this.mDuration)) {
            this.mState = 1;
            this.mStartTime = now;
            this.mDuration = 167.0F;
            this.mPullDistance += deltaDistance;
            this.mDistance = Math.max(0.0F, this.mPullDistance);
            if (this.mPullDistance == 0.0F) {
                this.mGlowScaleY = this.mGlowScaleYStart = 0.0F;
                this.mGlowAlpha = this.mGlowAlphaStart = 0.0F;
            } else {
                float absDeltaDis = Math.abs(deltaDistance);
                this.mGlowAlpha = this.mGlowAlphaStart = Math.min(0.15F, this.mGlowAlpha + absDeltaDis * 0.8F);
                float scale = (float) (Math.max(0.0, 1.0 - 1.0 / Math.sqrt((double) (Math.abs(this.mPullDistance) * this.mHeight)) - 0.3) / 0.7);
                this.mGlowScaleY = this.mGlowScaleYStart = scale;
            }
            this.mGlowAlphaFinish = this.mGlowAlpha;
            this.mGlowScaleYFinish = this.mGlowScaleY;
        }
    }

    public float onPullDistance(float deltaDistance, float displacement) {
        float finalDistance = Math.max(0.0F, deltaDistance + this.mDistance);
        float delta = finalDistance - this.mDistance;
        if (delta == 0.0F && this.mDistance == 0.0F) {
            return 0.0F;
        } else {
            if (this.mState != 1 && this.mState != 4) {
                this.mPullDistance = this.mDistance;
                this.mState = 1;
            }
            this.onPull(delta, displacement);
            return delta;
        }
    }

    public float getDistance() {
        return this.mDistance;
    }

    public void onRelease() {
        this.mPullDistance = 0.0F;
        if (this.mState == 1 || this.mState == 4) {
            this.mState = 3;
            this.mGlowAlphaStart = this.mGlowAlpha;
            this.mGlowScaleYStart = this.mGlowScaleY;
            this.mGlowAlphaFinish = 0.0F;
            this.mGlowScaleYFinish = 0.0F;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 600.0F;
        }
    }

    public void onAbsorb(int velocity) {
        this.mState = 2;
        velocity = Math.min(Math.max(100, Math.abs(velocity)), 10000);
        this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
        this.mDuration = 0.15F + (float) velocity * 0.02F;
        this.mGlowAlphaStart = 0.09F;
        this.mGlowScaleYStart = Math.max(this.mGlowScaleY, 0.0F);
        int f = velocity / 100;
        this.mGlowScaleYFinish = Math.min(0.025F + (float) (velocity * f) * 1.5E-4F / 2.0F, 1.0F);
        this.mGlowAlphaFinish = Math.max(this.mGlowAlphaStart, Math.min((float) (velocity * 6) * 1.0E-5F, 0.15F));
        this.mTargetDisplacement = 0.5F;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
    }

    public int getColor() {
        return this.mPaint.getColor();
    }

    public boolean draw(@Nonnull Canvas canvas) {
        this.update();
        canvas.save();
        float centerX = (this.mWidth + this.mHeight) * 0.5F;
        float centerY = this.mHeight - this.mRadius;
        canvas.scale(1.0F, Math.min(this.mGlowScaleY, 1.0F) * this.mBaseGlowScale, centerX, 0.0F);
        float displacement = Math.max(0.0F, Math.min(this.mDisplacement, 1.0F)) - 0.5F;
        float translateX = this.mWidth * displacement / 2.0F;
        canvas.translate(translateX, 0.0F);
        this.mPaint.setAlpha((int) (255.0F * this.mGlowAlpha));
        canvas.drawCircle(centerX, centerY, this.mRadius, this.mPaint);
        canvas.restore();
        boolean oneLastFrame = false;
        if (this.mState == 3 && this.mDistance == 0.0F) {
            this.mState = 0;
            oneLastFrame = true;
        }
        return this.mState != 0 || oneLastFrame;
    }

    public int getMaxHeight() {
        return (int) this.mHeight;
    }

    private void update() {
        long time = AnimationUtils.currentAnimationTimeMillis();
        float t = Math.min((float) (time - this.mStartTime) / this.mDuration, 1.0F);
        float p = TimeInterpolator.DECELERATE.getInterpolation(t);
        this.mGlowAlpha = this.mGlowAlphaStart + (this.mGlowAlphaFinish - this.mGlowAlphaStart) * p;
        this.mGlowScaleY = this.mGlowScaleYStart + (this.mGlowScaleYFinish - this.mGlowScaleYStart) * p;
        if (this.mState != 1) {
            this.mDistance = this.calculateDistanceFromGlowValues(this.mGlowScaleY, this.mGlowAlpha);
        }
        this.mDisplacement = (this.mDisplacement + this.mTargetDisplacement) / 2.0F;
        if (t >= 0.999F) {
            switch(this.mState) {
                case 1:
                    this.mState = 4;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 2000.0F;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0F;
                    this.mGlowScaleYFinish = 0.0F;
                    break;
                case 2:
                    this.mState = 3;
                    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                    this.mDuration = 600.0F;
                    this.mGlowAlphaStart = this.mGlowAlpha;
                    this.mGlowScaleYStart = this.mGlowScaleY;
                    this.mGlowAlphaFinish = 0.0F;
                    this.mGlowScaleYFinish = 0.0F;
                    break;
                case 3:
                    this.mState = 0;
                    break;
                case 4:
                    this.mState = 3;
            }
        }
    }

    private float calculateDistanceFromGlowValues(float scale, float alpha) {
        if (scale >= 1.0F) {
            return 1.0F;
        } else if (scale > 0.0F) {
            float v = 1.4285715F / (this.mGlowScaleY - 1.0F);
            return v * v / this.mHeight;
        } else {
            return alpha / 0.8F;
        }
    }
}