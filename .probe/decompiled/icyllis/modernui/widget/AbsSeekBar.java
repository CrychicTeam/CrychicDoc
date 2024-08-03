package icyllis.modernui.widget;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.BlendMode;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.KeyEvent;
import icyllis.modernui.view.MotionEvent;

public abstract class AbsSeekBar extends ProgressBar {

    private Drawable mThumb;

    private ColorStateList mThumbTintList = null;

    private BlendMode mThumbBlendMode = null;

    private boolean mHasThumbTint = false;

    private boolean mHasThumbBlendMode = false;

    private Drawable mTickMark;

    private ColorStateList mTickMarkTintList = null;

    private BlendMode mTickMarkBlendMode = null;

    private boolean mHasTickMarkTint = false;

    private boolean mHasTickMarkBlendMode = false;

    private int mThumbOffset;

    private boolean mSplitTrack;

    float mTouchProgressOffset;

    boolean mIsUserSeekable = true;

    private int mKeyProgressIncrement = 1;

    private static final int NO_ALPHA = 255;

    private float mDisabledAlpha;

    private int mThumbExclusionMaxSize;

    private int mScaledTouchSlop;

    private float mTouchDownX;

    private boolean mIsDragging;

    private float mTouchThumbOffset = 0.0F;

    private final Rect mThumbRect = new Rect();

    public AbsSeekBar(Context context) {
        super(context);
    }

    public void setThumb(Drawable thumb) {
        boolean needUpdate;
        if (this.mThumb != null && thumb != this.mThumb) {
            this.mThumb.setCallback(null);
            needUpdate = true;
        } else {
            needUpdate = false;
        }
        if (thumb != null) {
            thumb.setCallback(this);
            if (this.canResolveLayoutDirection()) {
                thumb.setLayoutDirection(this.getLayoutDirection());
            }
            this.mThumbOffset = thumb.getIntrinsicWidth() / 2;
            if (needUpdate && (thumb.getIntrinsicWidth() != this.mThumb.getIntrinsicWidth() || thumb.getIntrinsicHeight() != this.mThumb.getIntrinsicHeight())) {
                this.requestLayout();
            }
        }
        this.mThumb = thumb;
        this.applyThumbTint();
        this.invalidate();
        if (needUpdate) {
            this.updateThumbAndTrackPos(this.getWidth(), this.getHeight());
            if (thumb != null && thumb.isStateful()) {
                int[] state = this.getDrawableState();
                thumb.setState(state);
            }
        }
    }

    public Drawable getThumb() {
        return this.mThumb;
    }

    public void setThumbTintList(@Nullable ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        this.applyThumbTint();
    }

    @Nullable
    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintBlendMode(@Nullable BlendMode blendMode) {
        this.mThumbBlendMode = blendMode;
        this.mHasThumbBlendMode = true;
        this.applyThumbTint();
    }

    @Nullable
    public BlendMode getThumbTintBlendMode() {
        return this.mThumbBlendMode;
    }

    private void applyThumbTint() {
        if (this.mThumb != null && (this.mHasThumbTint || this.mHasThumbBlendMode)) {
            this.mThumb = this.mThumb.mutate();
            if (this.mHasThumbTint) {
                this.mThumb.setTintList(this.mThumbTintList);
            }
            if (this.mHasThumbBlendMode) {
                this.mThumb.setTintBlendMode(this.mThumbBlendMode);
            }
            if (this.mThumb.isStateful()) {
                this.mThumb.setState(this.getDrawableState());
            }
        }
    }

    public int getThumbOffset() {
        return this.mThumbOffset;
    }

    public void setThumbOffset(int thumbOffset) {
        this.mThumbOffset = thumbOffset;
        this.invalidate();
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        this.invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public void setTickMark(Drawable tickMark) {
        if (this.mTickMark != null) {
            this.mTickMark.setCallback(null);
        }
        this.mTickMark = tickMark;
        if (tickMark != null) {
            tickMark.setCallback(this);
            tickMark.setLayoutDirection(this.getLayoutDirection());
            if (tickMark.isStateful()) {
                tickMark.setState(this.getDrawableState());
            }
            this.applyTickMarkTint();
        }
        this.invalidate();
    }

    public Drawable getTickMark() {
        return this.mTickMark;
    }

    public void setTickMarkTintList(@Nullable ColorStateList tint) {
        this.mTickMarkTintList = tint;
        this.mHasTickMarkTint = true;
        this.applyTickMarkTint();
    }

    @Nullable
    public ColorStateList getTickMarkTintList() {
        return this.mTickMarkTintList;
    }

    public void setTickMarkTintBlendMode(@Nullable BlendMode blendMode) {
        this.mTickMarkBlendMode = blendMode;
        this.mHasTickMarkBlendMode = true;
        this.applyTickMarkTint();
    }

    @Nullable
    public BlendMode getTickMarkTintBlendMode() {
        return this.mTickMarkBlendMode;
    }

    private void applyTickMarkTint() {
        if (this.mTickMark != null && (this.mHasTickMarkTint || this.mHasTickMarkBlendMode)) {
            this.mTickMark = this.mTickMark.mutate();
            if (this.mHasTickMarkTint) {
                this.mTickMark.setTintList(this.mTickMarkTintList);
            }
            if (this.mHasTickMarkBlendMode) {
                this.mTickMark.setTintBlendMode(this.mTickMarkBlendMode);
            }
            if (this.mTickMark.isStateful()) {
                this.mTickMark.setState(this.getDrawableState());
            }
        }
    }

    public void setKeyProgressIncrement(int increment) {
        this.mKeyProgressIncrement = Math.abs(increment);
    }

    public int getKeyProgressIncrement() {
        return this.mKeyProgressIncrement;
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == this.mThumb || who == this.mTickMark || super.verifyDrawable(who);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mThumb != null) {
            this.mThumb.jumpToCurrentState();
        }
        if (this.mTickMark != null) {
            this.mTickMark.jumpToCurrentState();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable progressDrawable = this.getProgressDrawable();
        if (progressDrawable != null && this.mDisabledAlpha < 1.0F) {
            progressDrawable.setAlpha(this.isEnabled() ? 255 : (int) (255.0F * this.mDisabledAlpha));
        }
        Drawable thumb = this.mThumb;
        if (thumb != null && thumb.isStateful() && thumb.setState(this.getDrawableState())) {
            this.invalidateDrawable(thumb);
        }
        Drawable tickMark = this.mTickMark;
        if (tickMark != null && tickMark.isStateful() && tickMark.setState(this.getDrawableState())) {
            this.invalidateDrawable(tickMark);
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mThumb != null) {
            this.mThumb.setHotspot(x, y);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.updateThumbAndTrackPos(w, h);
    }

    private void updateThumbAndTrackPos(int w, int h) {
        int paddedHeight = h - this.mPaddingTop - this.mPaddingBottom;
        Drawable track = this.getCurrentDrawable();
        Drawable thumb = this.mThumb;
        int trackHeight = Math.min(this.getMaximumHeight(), paddedHeight);
        int thumbHeight = thumb == null ? 0 : thumb.getIntrinsicHeight();
        int trackOffset;
        int thumbOffset;
        if (thumbHeight > trackHeight) {
            int offsetHeight = (paddedHeight - thumbHeight) / 2;
            trackOffset = offsetHeight + (thumbHeight - trackHeight) / 2;
            thumbOffset = offsetHeight;
        } else {
            int offsetHeight = (paddedHeight - trackHeight) / 2;
            trackOffset = offsetHeight;
            thumbOffset = offsetHeight + (trackHeight - thumbHeight) / 2;
        }
        if (track != null) {
            int trackWidth = w - this.mPaddingRight - this.mPaddingLeft;
            track.setBounds(0, trackOffset, trackWidth, trackOffset + trackHeight);
        }
        if (thumb != null) {
            this.setThumbPos(w, thumb, this.getScale(), thumbOffset);
        }
    }

    private float getScale() {
        int min = this.getMin();
        int max = this.getMax();
        int range = max - min;
        return range > 0 ? (float) (this.getProgress() - min) / (float) range : 0.0F;
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int offset) {
        int available = w - this.mPaddingLeft - this.mPaddingRight;
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available -= thumbWidth;
        available += this.mThumbOffset * 2;
        int thumbPos = (int) (scale * (float) available + 0.5F);
        int top;
        int bottom;
        if (offset == Integer.MIN_VALUE) {
            Rect oldBounds = thumb.getBounds();
            top = oldBounds.top;
            bottom = oldBounds.bottom;
        } else {
            top = offset;
            bottom = offset + thumbHeight;
        }
        int left = this.isLayoutRtl() ? available - thumbPos : thumbPos;
        int right = left + thumbWidth;
        Drawable background = this.getBackground();
        if (background != null) {
            int offsetX = this.mPaddingLeft - this.mThumbOffset;
            int offsetY = this.mPaddingTop;
            background.setHotspotBounds(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
        }
        thumb.setBounds(left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = this.getCurrentDrawable();
        int thumbHeight = this.mThumb == null ? 0 : this.mThumb.getIntrinsicHeight();
        int dw = 0;
        int dh = 0;
        if (d != null) {
            dw = MathUtil.clamp(d.getIntrinsicWidth(), this.getMinimumWidth(), this.getMaximumWidth());
            dh = MathUtil.clamp(d.getIntrinsicHeight(), this.getMinimumHeight(), this.getMaximumHeight());
            dh = Math.max(thumbHeight, dh);
        }
        dw += this.mPaddingLeft + this.mPaddingRight;
        dh += this.mPaddingTop + this.mPaddingBottom;
        this.setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0), resolveSizeAndState(dh, heightMeasureSpec, 0));
    }

    @Override
    void onVisualProgressChanged(int id, float scale) {
        super.onVisualProgressChanged(id, scale);
        if (id == 16908301) {
            Drawable thumb = this.mThumb;
            if (thumb != null) {
                this.setThumbPos(this.getWidth(), thumb, scale, Integer.MIN_VALUE);
                this.invalidate();
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        this.drawThumb(canvas);
    }

    @Override
    void drawTrack(Canvas canvas) {
        super.drawTrack(canvas);
        this.drawTickMarks(canvas);
    }

    protected void drawTickMarks(Canvas canvas) {
        if (this.mTickMark != null) {
            int count = this.getMax() - this.getMin();
            if (count > 1) {
                int w = this.mTickMark.getIntrinsicWidth();
                int h = this.mTickMark.getIntrinsicHeight();
                int halfW = w >= 0 ? w / 2 : 1;
                int halfH = h >= 0 ? h / 2 : 1;
                this.mTickMark.setBounds(-halfW, -halfH, halfW, halfH);
                float spacing = (float) (this.getWidth() - this.mPaddingLeft - this.mPaddingRight) / (float) count;
                int saveCount = canvas.save();
                canvas.translate((float) this.mPaddingLeft, (float) this.getHeight() / 2.0F);
                for (int i = 0; i <= count; i++) {
                    this.mTickMark.draw(canvas);
                    canvas.translate(spacing, 0.0F);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }

    void drawThumb(Canvas canvas) {
        if (this.mThumb != null) {
            int saveCount = canvas.save();
            canvas.translate((float) (this.mPaddingLeft - this.mThumbOffset), (float) this.mPaddingTop);
            this.mThumb.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (this.mIsUserSeekable && this.isEnabled()) {
            switch(event.getAction()) {
                case 0:
                    if (this.mThumb != null) {
                        int availableWidth = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
                        this.mTouchThumbOffset = (float) (this.getProgress() - this.getMin()) / (float) (this.getMax() - this.getMin()) - (event.getX() - (float) this.mPaddingLeft) / (float) availableWidth;
                        if (Math.abs(this.mTouchThumbOffset * (float) availableWidth) > (float) this.getThumbOffset()) {
                            this.mTouchThumbOffset = 0.0F;
                        }
                    }
                    if (this.isInScrollingContainer()) {
                        this.mTouchDownX = event.getX();
                    } else {
                        this.startDrag(event);
                    }
                    break;
                case 1:
                    if (this.mIsDragging) {
                        this.trackTouchEvent(event);
                        this.onStopTrackingTouch();
                        this.setPressed(false);
                    } else {
                        this.onStartTrackingTouch();
                        this.trackTouchEvent(event);
                        this.onStopTrackingTouch();
                    }
                    this.invalidate();
                    break;
                case 2:
                    if (this.mIsDragging) {
                        this.trackTouchEvent(event);
                    } else {
                        float x = event.getX();
                        if (Math.abs(x - this.mTouchDownX) > (float) this.mScaledTouchSlop) {
                            this.startDrag(event);
                        }
                    }
                    break;
                case 3:
                    if (this.mIsDragging) {
                        this.onStopTrackingTouch();
                        this.setPressed(false);
                    }
                    this.invalidate();
            }
            return true;
        } else {
            return false;
        }
    }

    private void startDrag(MotionEvent event) {
        this.setPressed(true);
        if (this.mThumb != null) {
            this.invalidate();
        }
        this.onStartTrackingTouch();
        this.trackTouchEvent(event);
        this.attemptClaimDrag();
    }

    private void setHotspot(float x, float y) {
        Drawable bg = this.getBackground();
        if (bg != null) {
            bg.setHotspot(x, y);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());
        int width = this.getWidth();
        int availableWidth = width - this.mPaddingLeft - this.mPaddingRight;
        float progress = 0.0F;
        float scale;
        if (this.isLayoutRtl()) {
            if (x > width - this.mPaddingRight) {
                scale = 0.0F;
            } else if (x < this.mPaddingLeft) {
                scale = 1.0F;
            } else {
                scale = (float) (availableWidth - x + this.mPaddingLeft) / (float) availableWidth + this.mTouchThumbOffset;
                progress = this.mTouchProgressOffset;
            }
        } else if (x < this.mPaddingLeft) {
            scale = 0.0F;
        } else if (x > width - this.mPaddingRight) {
            scale = 1.0F;
        } else {
            scale = (float) (x - this.mPaddingLeft) / (float) availableWidth + this.mTouchThumbOffset;
            progress = this.mTouchProgressOffset;
        }
        int range = this.getMax() - this.getMin();
        progress += scale * (float) range + (float) this.getMin();
        this.setHotspot((float) x, (float) y);
        this.setProgressInternal(Math.round(progress), true, false);
    }

    private void attemptClaimDrag() {
        if (this.getParent() != null) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
        }
    }

    void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    void onKeyChange() {
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (this.isEnabled()) {
            int increment = this.mKeyProgressIncrement;
            switch(keyCode) {
                case 45:
                case 263:
                case 333:
                    increment = -increment;
                case 61:
                case 262:
                case 334:
                    increment = this.isLayoutRtl() ? -increment : increment;
                    if (this.setProgressInternal(this.getProgress() + increment, true, true)) {
                        this.onKeyChange();
                        return true;
                    }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            this.setThumbPos(this.getWidth(), thumb, this.getScale(), Integer.MIN_VALUE);
            this.invalidate();
        }
    }
}