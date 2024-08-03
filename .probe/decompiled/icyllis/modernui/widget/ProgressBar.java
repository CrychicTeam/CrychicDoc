package icyllis.modernui.widget;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.AnimatorListener;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.LayerDrawable;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.FloatProperty;
import icyllis.modernui.view.View;
import java.text.NumberFormat;
import java.util.Locale;

public class ProgressBar extends View {

    private int mMaxWidth;

    private int mMaxHeight;

    private int mProgress;

    private int mSecondaryProgress;

    private int mMin;

    private boolean mMinInitialized;

    private int mMax = 10000;

    private boolean mMaxInitialized;

    private boolean mIndeterminate;

    private boolean mOnlyIndeterminate;

    private boolean mInDrawing;

    private boolean mAttached;

    private boolean mRefreshIsPosted;

    private float mVisualProgress;

    private Drawable mIndeterminateDrawable;

    private Drawable mProgressDrawable;

    private Drawable mCurrentDrawable;

    private ProgressBar.ProgressTintInfo mProgressTintInfo;

    private ObjectAnimator mLastProgressAnimator;

    private NumberFormat mPercentFormat;

    private Locale mCachedLocale;

    protected static final FloatProperty<ProgressBar> VISUAL_PROGRESS = new FloatProperty<ProgressBar>("visual_progress") {

        public void setValue(ProgressBar object, float value) {
            object.setVisualProgress(16908301, value);
        }

        public Float get(ProgressBar object) {
            return object.mVisualProgress;
        }
    };

    public ProgressBar(Context context) {
        super(context);
    }

    public void setMaximumWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
        this.requestLayout();
    }

    public int getMaximumWidth() {
        return this.mMaxWidth;
    }

    public void setMaximumHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
        this.requestLayout();
    }

    public int getMaximumHeight() {
        return this.mMaxHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int dw = 0;
        int dh = 0;
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            dw = MathUtil.clamp(d.getIntrinsicWidth(), this.getMinimumWidth(), this.mMaxWidth);
            dh = MathUtil.clamp(d.getIntrinsicHeight(), this.getMinimumHeight(), this.mMaxHeight);
        }
        this.updateDrawableState();
        dw += this.mPaddingLeft + this.mPaddingRight;
        dh += this.mPaddingTop + this.mPaddingBottom;
        int measuredWidth = resolveSizeAndState(dw, widthMeasureSpec, 0);
        int measuredHeight = resolveSizeAndState(dh, heightMeasureSpec, 0);
        this.setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public boolean isIndeterminate() {
        return this.mIndeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        if ((!this.mOnlyIndeterminate || !this.mIndeterminate) && indeterminate != this.mIndeterminate) {
            this.mIndeterminate = indeterminate;
            if (indeterminate) {
                this.swapCurrentDrawable(this.mIndeterminateDrawable);
                this.startAnimation();
            } else {
                this.swapCurrentDrawable(this.mProgressDrawable);
                this.stopAnimation();
            }
        }
    }

    private void swapCurrentDrawable(Drawable newDrawable) {
        Drawable oldDrawable = this.mCurrentDrawable;
        this.mCurrentDrawable = newDrawable;
        if (oldDrawable != this.mCurrentDrawable) {
            if (oldDrawable != null) {
                oldDrawable.setVisible(false, false);
            }
            if (this.mCurrentDrawable != null) {
                this.mCurrentDrawable.setVisible(this.getWindowVisibility() == 0 && this.isShown(), false);
            }
        }
    }

    public Drawable getIndeterminateDrawable() {
        return this.mIndeterminateDrawable;
    }

    public void setIndeterminateDrawable(Drawable d) {
        if (this.mIndeterminateDrawable != d) {
            if (this.mIndeterminateDrawable != null) {
                this.mIndeterminateDrawable.setCallback(null);
                this.unscheduleDrawable(this.mIndeterminateDrawable);
            }
            this.mIndeterminateDrawable = d;
            if (d != null) {
                d.setCallback(this);
                d.setLayoutDirection(this.getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(this.getDrawableState());
                }
                this.applyIndeterminateTint();
            }
            if (this.mIndeterminate) {
                this.swapCurrentDrawable(d);
                this.postInvalidate();
            }
        }
    }

    public void setIndeterminateTintList(@Nullable ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mIndeterminateTintList = tint;
        this.mProgressTintInfo.mHasIndeterminateTint = true;
        this.applyIndeterminateTint();
    }

    @Nullable
    public ColorStateList getIndeterminateTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mIndeterminateTintList : null;
    }

    public void setIndeterminateTintBlendMode(@Nullable BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mIndeterminateBlendMode = blendMode;
        this.mProgressTintInfo.mHasIndeterminateTintMode = true;
        this.applyIndeterminateTint();
    }

    @Nullable
    public BlendMode getIndeterminateTintBlendMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mIndeterminateBlendMode : null;
    }

    private void applyIndeterminateTint() {
        if (this.mIndeterminateDrawable != null && this.mProgressTintInfo != null) {
            ProgressBar.ProgressTintInfo tintInfo = this.mProgressTintInfo;
            if (tintInfo.mHasIndeterminateTint || tintInfo.mHasIndeterminateTintMode) {
                this.mIndeterminateDrawable = this.mIndeterminateDrawable.mutate();
                if (tintInfo.mHasIndeterminateTint) {
                    this.mIndeterminateDrawable.setTintList(tintInfo.mIndeterminateTintList);
                }
                if (tintInfo.mHasIndeterminateTintMode) {
                    this.mIndeterminateDrawable.setTintBlendMode(tintInfo.mIndeterminateBlendMode);
                }
                if (this.mIndeterminateDrawable.isStateful()) {
                    this.mIndeterminateDrawable.setState(this.getDrawableState());
                }
            }
        }
    }

    public Drawable getProgressDrawable() {
        return this.mProgressDrawable;
    }

    public void setProgressDrawable(Drawable d) {
        if (this.mProgressDrawable != d) {
            if (this.mProgressDrawable != null) {
                this.mProgressDrawable.setCallback(null);
                this.unscheduleDrawable(this.mProgressDrawable);
            }
            this.mProgressDrawable = d;
            if (d != null) {
                d.setCallback(this);
                d.setLayoutDirection(this.getLayoutDirection());
                if (d.isStateful()) {
                    d.setState(this.getDrawableState());
                }
                int drawableHeight = d.getMinimumHeight();
                if (this.mMaxHeight < drawableHeight) {
                    this.mMaxHeight = drawableHeight;
                    this.requestLayout();
                }
                this.applyProgressTints();
            }
            if (!this.mIndeterminate) {
                this.swapCurrentDrawable(d);
                this.postInvalidate();
            }
            this.updateDrawableBounds(this.getWidth(), this.getHeight());
            this.updateDrawableState();
            this.doRefreshProgress(16908301, this.mProgress, false, false, false);
            this.doRefreshProgress(16908303, this.mSecondaryProgress, false, false, false);
        }
    }

    private void applyProgressTints() {
        if (this.mProgressDrawable != null && this.mProgressTintInfo != null) {
            this.applyPrimaryProgressTint();
            this.applyProgressBackgroundTint();
            this.applySecondaryProgressTint();
        }
    }

    private void applyPrimaryProgressTint() {
        if (this.mProgressTintInfo.mHasProgressTint || this.mProgressTintInfo.mHasProgressTintMode) {
            Drawable target = this.getTintTarget(16908301, true);
            if (target != null) {
                if (this.mProgressTintInfo.mHasProgressTint) {
                    target.setTintList(this.mProgressTintInfo.mProgressTintList);
                }
                if (this.mProgressTintInfo.mHasProgressTintMode) {
                    target.setTintBlendMode(this.mProgressTintInfo.mProgressBlendMode);
                }
                if (target.isStateful()) {
                    target.setState(this.getDrawableState());
                }
            }
        }
    }

    private void applyProgressBackgroundTint() {
        if (this.mProgressTintInfo.mHasProgressBackgroundTint || this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
            Drawable target = this.getTintTarget(16908288, false);
            if (target != null) {
                if (this.mProgressTintInfo.mHasProgressBackgroundTint) {
                    target.setTintList(this.mProgressTintInfo.mProgressBackgroundTintList);
                }
                if (this.mProgressTintInfo.mHasProgressBackgroundTintMode) {
                    target.setTintBlendMode(this.mProgressTintInfo.mProgressBackgroundBlendMode);
                }
                if (target.isStateful()) {
                    target.setState(this.getDrawableState());
                }
            }
        }
    }

    private void applySecondaryProgressTint() {
        if (this.mProgressTintInfo.mHasSecondaryProgressTint || this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
            Drawable target = this.getTintTarget(16908303, false);
            if (target != null) {
                if (this.mProgressTintInfo.mHasSecondaryProgressTint) {
                    target.setTintList(this.mProgressTintInfo.mSecondaryProgressTintList);
                }
                if (this.mProgressTintInfo.mHasSecondaryProgressTintMode) {
                    target.setTintBlendMode(this.mProgressTintInfo.mSecondaryProgressBlendMode);
                }
                if (target.isStateful()) {
                    target.setState(this.getDrawableState());
                }
            }
        }
    }

    public void setProgressTintList(@Nullable ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressTintList = tint;
        this.mProgressTintInfo.mHasProgressTint = true;
        if (this.mProgressDrawable != null) {
            this.applyPrimaryProgressTint();
        }
    }

    @Nullable
    public ColorStateList getProgressTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressTintList : null;
    }

    public void setProgressTintBlendMode(@Nullable BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBlendMode = blendMode;
        this.mProgressTintInfo.mHasProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applyPrimaryProgressTint();
        }
    }

    @Nullable
    public BlendMode getProgressTintBlendMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressBlendMode : null;
    }

    public void setProgressBackgroundTintList(@Nullable ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBackgroundTintList = tint;
        this.mProgressTintInfo.mHasProgressBackgroundTint = true;
        if (this.mProgressDrawable != null) {
            this.applyProgressBackgroundTint();
        }
    }

    @Nullable
    public ColorStateList getProgressBackgroundTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressBackgroundTintList : null;
    }

    public void setProgressBackgroundTintBlendMode(@Nullable BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mProgressBackgroundBlendMode = blendMode;
        this.mProgressTintInfo.mHasProgressBackgroundTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applyProgressBackgroundTint();
        }
    }

    @Nullable
    public BlendMode getProgressBackgroundTintBlendMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mProgressBackgroundBlendMode : null;
    }

    public void setSecondaryProgressTintList(@Nullable ColorStateList tint) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mSecondaryProgressTintList = tint;
        this.mProgressTintInfo.mHasSecondaryProgressTint = true;
        if (this.mProgressDrawable != null) {
            this.applySecondaryProgressTint();
        }
    }

    @Nullable
    public ColorStateList getSecondaryProgressTintList() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mSecondaryProgressTintList : null;
    }

    public void setSecondaryProgressTintBlendMode(@Nullable BlendMode blendMode) {
        if (this.mProgressTintInfo == null) {
            this.mProgressTintInfo = new ProgressBar.ProgressTintInfo();
        }
        this.mProgressTintInfo.mSecondaryProgressBlendMode = blendMode;
        this.mProgressTintInfo.mHasSecondaryProgressTintMode = true;
        if (this.mProgressDrawable != null) {
            this.applySecondaryProgressTint();
        }
    }

    @Nullable
    public BlendMode getSecondaryProgressTintBlendMode() {
        return this.mProgressTintInfo != null ? this.mProgressTintInfo.mSecondaryProgressBlendMode : null;
    }

    @Nullable
    private Drawable getTintTarget(int layerId, boolean shouldFallback) {
        Drawable layer = null;
        Drawable d = this.mProgressDrawable;
        if (d != null) {
            this.mProgressDrawable = d.mutate();
            if (d instanceof LayerDrawable) {
                layer = ((LayerDrawable) d).findDrawableByLayerId(layerId);
            }
            if (shouldFallback && layer == null) {
                layer = d;
            }
        }
        return layer;
    }

    public void setProgress(int progress) {
        this.setProgressInternal(progress, false, false);
    }

    public void setProgress(int progress, boolean animate) {
        this.setProgressInternal(progress, false, animate);
    }

    synchronized boolean setProgressInternal(int progress, boolean fromUser, boolean animate) {
        if (this.mIndeterminate) {
            return false;
        } else {
            progress = MathUtil.clamp(progress, this.mMin, this.mMax);
            if (progress == this.mProgress) {
                return false;
            } else {
                this.mProgress = progress;
                this.doRefreshProgress(16908301, this.mProgress, fromUser, true, animate);
                return true;
            }
        }
    }

    public void setSecondaryProgress(int secondaryProgress) {
        if (!this.mIndeterminate) {
            if (secondaryProgress < this.mMin) {
                secondaryProgress = this.mMin;
            }
            if (secondaryProgress > this.mMax) {
                secondaryProgress = this.mMax;
            }
            if (secondaryProgress != this.mSecondaryProgress) {
                this.mSecondaryProgress = secondaryProgress;
                this.doRefreshProgress(16908303, this.mSecondaryProgress, false, true, false);
            }
        }
    }

    public int getSecondaryProgress() {
        return this.mIndeterminate ? 0 : this.mSecondaryProgress;
    }

    public int getProgress() {
        return this.mIndeterminate ? 0 : this.mProgress;
    }

    public final void incrementProgressBy(int diff) {
        this.setProgress(this.mProgress + diff);
    }

    public final void incrementSecondaryProgressBy(int diff) {
        this.setSecondaryProgress(this.mSecondaryProgress + diff);
    }

    public int getMin() {
        return this.mMin;
    }

    public int getMax() {
        return this.mMax;
    }

    public void setMin(int min) {
        if (this.mMaxInitialized && min > this.mMax) {
            min = this.mMax;
        }
        this.mMinInitialized = true;
        if (this.mMaxInitialized && min != this.mMin) {
            this.mMin = min;
            this.postInvalidate();
            if (this.mProgress < min) {
                this.mProgress = min;
            }
            this.doRefreshProgress(16908301, this.mProgress, false, true, false);
        } else {
            this.mMin = min;
        }
    }

    public void setMax(int max) {
        if (this.mMinInitialized && max < this.mMin) {
            max = this.mMin;
        }
        this.mMaxInitialized = true;
        if (this.mMinInitialized && max != this.mMax) {
            this.mMax = max;
            this.postInvalidate();
            if (this.mProgress > max) {
                this.mProgress = max;
            }
            this.doRefreshProgress(16908301, this.mProgress, false, true, false);
        } else {
            this.mMax = max;
        }
    }

    private void setVisualProgress(int id, float progress) {
        this.mVisualProgress = progress;
        Drawable d = this.mCurrentDrawable;
        if (d instanceof LayerDrawable) {
            d = ((LayerDrawable) d).findDrawableByLayerId(id);
            if (d == null) {
                d = this.mCurrentDrawable;
            }
        }
        if (d != null) {
            int level = (int) (progress * 10000.0F + 0.5F);
            d.setLevel(level);
        } else {
            this.invalidate();
        }
        this.onVisualProgressChanged(id, progress);
    }

    void onVisualProgressChanged(int id, float progress) {
    }

    private void doRefreshProgress(int id, int progress, boolean fromUser, boolean callBackToApp, boolean animate) {
        int range = this.mMax - this.mMin;
        float scale = range > 0 ? (float) (progress - this.mMin) / (float) range : 0.0F;
        boolean isPrimary = id == 16908301;
        if (isPrimary && animate) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, VISUAL_PROGRESS, scale);
            animator.setAutoCancel(true);
            animator.setDuration(80L);
            animator.setInterpolator(TimeInterpolator.DECELERATE);
            animator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    ProgressBar.this.mLastProgressAnimator = null;
                }
            });
            animator.start();
            this.mLastProgressAnimator = animator;
        } else {
            if (isPrimary && this.mLastProgressAnimator != null) {
                this.mLastProgressAnimator.cancel();
                this.mLastProgressAnimator = null;
            }
            this.setVisualProgress(id, scale);
        }
        if (isPrimary && callBackToApp) {
            this.onProgressRefresh(scale, fromUser, progress);
        }
    }

    void onProgressRefresh(float scale, boolean fromUser, int progress) {
    }

    void startAnimation() {
    }

    void stopAnimation() {
    }

    @Nullable
    public Drawable getCurrentDrawable() {
        return this.mCurrentDrawable;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == this.mProgressDrawable || who == this.mIndeterminateDrawable || super.verifyDrawable(who);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.jumpToCurrentState();
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.jumpToCurrentState();
        }
    }

    @Override
    public void onResolveDrawables(int layoutDirection) {
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            d.setLayoutDirection(layoutDirection);
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.setLayoutDirection(layoutDirection);
        }
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setLayoutDirection(layoutDirection);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        this.drawTrack(canvas);
    }

    void drawTrack(Canvas canvas) {
        Drawable d = this.mCurrentDrawable;
        if (d != null) {
            int saveCount = canvas.save();
            if (this.isLayoutRtl()) {
                canvas.translate((float) (this.getWidth() - this.mPaddingRight), (float) this.mPaddingTop);
                canvas.scale(-1.0F, 1.0F);
            } else {
                canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
            }
            d.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.updateDrawableBounds(w, h);
    }

    private void updateDrawableBounds(int w, int h) {
        w -= this.mPaddingRight + this.mPaddingLeft;
        h -= this.mPaddingTop + this.mPaddingBottom;
        int right = w;
        int bottom = h;
        int top = 0;
        int left = 0;
        if (this.mIndeterminateDrawable != null) {
            if (this.mOnlyIndeterminate) {
                int intrinsicWidth = this.mIndeterminateDrawable.getIntrinsicWidth();
                int intrinsicHeight = this.mIndeterminateDrawable.getIntrinsicHeight();
                float intrinsicAspect = (float) intrinsicWidth / (float) intrinsicHeight;
                float boundAspect = (float) w / (float) h;
                if (intrinsicAspect != boundAspect) {
                    if (boundAspect > intrinsicAspect) {
                        int width = (int) ((float) h * intrinsicAspect);
                        left = (w - width) / 2;
                        right = left + width;
                    } else {
                        int height = (int) ((float) w * (1.0F / intrinsicAspect));
                        top = (h - height) / 2;
                        bottom = top + height;
                    }
                }
            }
            if (this.isLayoutRtl()) {
                int tempLeft = left;
                left = w - right;
                right = w - tempLeft;
            }
            this.mIndeterminateDrawable.setBounds(left, top, right, bottom);
        }
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setBounds(0, 0, right, bottom);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        this.updateDrawableState();
    }

    private void updateDrawableState() {
        int[] state = this.getDrawableState();
        boolean changed = false;
        Drawable progressDrawable = this.mProgressDrawable;
        if (progressDrawable != null && progressDrawable.isStateful()) {
            changed |= progressDrawable.setState(state);
        }
        Drawable indeterminateDrawable = this.mIndeterminateDrawable;
        if (indeterminateDrawable != null && indeterminateDrawable.isStateful()) {
            changed |= indeterminateDrawable.setState(state);
        }
        if (changed) {
            this.invalidate();
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mProgressDrawable != null) {
            this.mProgressDrawable.setHotspot(x, y);
        }
        if (this.mIndeterminateDrawable != null) {
            this.mIndeterminateDrawable.setHotspot(x, y);
        }
    }

    private static final class ProgressTintInfo {

        ColorStateList mIndeterminateTintList;

        BlendMode mIndeterminateBlendMode;

        boolean mHasIndeterminateTint;

        boolean mHasIndeterminateTintMode;

        ColorStateList mProgressTintList;

        BlendMode mProgressBlendMode;

        boolean mHasProgressTint;

        boolean mHasProgressTintMode;

        ColorStateList mProgressBackgroundTintList;

        BlendMode mProgressBackgroundBlendMode;

        boolean mHasProgressBackgroundTint;

        boolean mHasProgressBackgroundTintMode;

        ColorStateList mSecondaryProgressTintList;

        BlendMode mSecondaryProgressBlendMode;

        boolean mHasSecondaryProgressTint;

        boolean mHasSecondaryProgressTintMode;
    }
}