package icyllis.modernui.widget;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Image;
import icyllis.modernui.graphics.Matrix;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ImageDrawable;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.view.MeasureSpec;
import icyllis.modernui.view.View;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImageView extends View {

    private final Matrix mMatrix = new Matrix();

    private ImageView.ScaleType mScaleType = ImageView.ScaleType.FIT_CENTER;

    private boolean mAdjustViewBounds = false;

    private int mMaxWidth = Integer.MAX_VALUE;

    private int mMaxHeight = Integer.MAX_VALUE;

    private int mImageAlpha = 255;

    private boolean mHasImageAlpha = false;

    private Drawable mDrawable = null;

    private ImageDrawable mRecycleImageDrawable = null;

    private ColorStateList mDrawableTintList = null;

    private boolean mHasDrawableTint = false;

    private int[] mState = null;

    private boolean mMergeState = false;

    private int mLevel = 0;

    private int mDrawableWidth;

    private int mDrawableHeight;

    private Matrix mDrawMatrix = null;

    private boolean mCropToPadding = false;

    private int mBaseline = -1;

    private boolean mBaselineAlignBottom = false;

    public ImageView(Context context) {
        super(context);
    }

    @Override
    protected boolean verifyDrawable(@Nonnull Drawable dr) {
        return this.mDrawable == dr || super.verifyDrawable(dr);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mDrawable != null) {
            this.mDrawable.jumpToCurrentState();
        }
    }

    @Override
    public void invalidateDrawable(@Nonnull Drawable dr) {
        if (dr == this.mDrawable) {
            int w = dr.getIntrinsicWidth();
            int h = dr.getIntrinsicHeight();
            if (w != this.mDrawableWidth || h != this.mDrawableHeight) {
                this.mDrawableWidth = w;
                this.mDrawableHeight = h;
                this.configureBounds();
            }
            this.invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    @Override
    public boolean hasOverlappingRendering() {
        return this.getBackground() != null && this.getBackground().getCurrent() != null;
    }

    public boolean getAdjustViewBounds() {
        return this.mAdjustViewBounds;
    }

    public void setAdjustViewBounds(boolean adjustViewBounds) {
        this.mAdjustViewBounds = adjustViewBounds;
        if (adjustViewBounds) {
            this.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }
    }

    public int getMaxWidth() {
        return this.mMaxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public int getMaxHeight() {
        return this.mMaxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }

    @Nullable
    public Drawable getDrawable() {
        if (this.mDrawable == this.mRecycleImageDrawable) {
            this.mRecycleImageDrawable = null;
        }
        return this.mDrawable;
    }

    public void setImageDrawable(@Nullable Drawable drawable) {
        if (this.mDrawable != drawable) {
            int oldWidth = this.mDrawableWidth;
            int oldHeight = this.mDrawableHeight;
            this.updateDrawable(drawable);
            if (oldWidth != this.mDrawableWidth || oldHeight != this.mDrawableHeight) {
                this.requestLayout();
            }
            this.invalidate();
        }
    }

    public void setImageTintList(@Nullable ColorStateList tint) {
        this.mDrawableTintList = tint;
        this.mHasDrawableTint = true;
        this.applyImageTint();
    }

    @Nullable
    public ColorStateList getImageTintList() {
        return this.mDrawableTintList;
    }

    private void applyImageTint() {
        if (this.mDrawable != null && this.mHasDrawableTint) {
            this.mDrawable = this.mDrawable.mutate();
            if (this.mHasDrawableTint) {
                this.mDrawable.setTintList(this.mDrawableTintList);
            }
            if (this.mDrawable.isStateful()) {
                this.mDrawable.setState(this.getDrawableState());
            }
        }
    }

    public void setImage(@Nullable Image image) {
        this.mDrawable = null;
        if (this.mRecycleImageDrawable == null) {
            this.mRecycleImageDrawable = new ImageDrawable(image);
        } else {
            this.mRecycleImageDrawable.setImage(image);
        }
        this.setImageDrawable(this.mRecycleImageDrawable);
    }

    public void setImageState(int[] state, boolean merge) {
        this.mState = state;
        this.mMergeState = merge;
        if (this.mDrawable != null) {
            this.refreshDrawableState();
            this.resizeFromDrawable();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        this.resizeFromDrawable();
    }

    public void setImageLevel(int level) {
        this.mLevel = level;
        if (this.mDrawable != null) {
            this.mDrawable.setLevel(level);
            this.resizeFromDrawable();
        }
    }

    public void setScaleType(@Nonnull ImageView.ScaleType scaleType) {
        if (this.mScaleType != scaleType) {
            this.mScaleType = scaleType;
            this.requestLayout();
            this.invalidate();
        }
    }

    @Nonnull
    public ImageView.ScaleType getScaleType() {
        return this.mScaleType;
    }

    public void setImageMatrix(@Nullable Matrix matrix) {
        if (matrix != null && matrix.isIdentity()) {
            matrix = null;
        }
        if (matrix == null && !this.mMatrix.isIdentity() || matrix != null && !this.mMatrix.equals(matrix)) {
            if (matrix == null) {
                this.mMatrix.setIdentity();
            } else {
                this.mMatrix.set(matrix);
            }
            this.configureBounds();
            this.invalidate();
        }
    }

    public boolean getCropToPadding() {
        return this.mCropToPadding;
    }

    public void setCropToPadding(boolean cropToPadding) {
        if (this.mCropToPadding != cropToPadding) {
            this.mCropToPadding = cropToPadding;
            this.requestLayout();
            this.invalidate();
        }
    }

    @Nonnull
    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        if (this.mState == null) {
            return super.onCreateDrawableState(extraSpace);
        } else {
            return !this.mMergeState ? this.mState : mergeDrawableStates(super.onCreateDrawableState(extraSpace + this.mState.length), this.mState);
        }
    }

    private void updateDrawable(@Nullable Drawable d) {
        if (d != this.mRecycleImageDrawable && this.mRecycleImageDrawable != null) {
            this.mRecycleImageDrawable.setImage(null);
        }
        boolean sameDrawable = false;
        if (this.mDrawable != null) {
            sameDrawable = this.mDrawable == d;
            this.mDrawable.setCallback(null);
            this.unscheduleDrawable(this.mDrawable);
            if (!sameDrawable && this.isAttachedToWindow()) {
                this.mDrawable.setVisible(false, false);
            }
        }
        this.mDrawable = d;
        if (d != null) {
            d.setCallback(this);
            d.setLayoutDirection(this.getLayoutDirection());
            if (d.isStateful()) {
                d.setState(this.getDrawableState());
            }
            if (!sameDrawable) {
                boolean visible = this.isAttachedToWindow() && this.getWindowVisibility() == 0 && this.isShown();
                d.setVisible(visible, true);
            }
            d.setLevel(this.mLevel);
            this.mDrawableWidth = d.getIntrinsicWidth();
            this.mDrawableHeight = d.getIntrinsicHeight();
            this.applyImageTint();
            this.applyAlpha();
            this.configureBounds();
        } else {
            this.mDrawableWidth = this.mDrawableHeight = -1;
        }
    }

    private void resizeFromDrawable() {
        Drawable d = this.mDrawable;
        if (d != null) {
            int w = d.getIntrinsicWidth();
            if (w < 0) {
                w = this.mDrawableWidth;
            }
            int h = d.getIntrinsicHeight();
            if (h < 0) {
                h = this.mDrawableHeight;
            }
            if (w != this.mDrawableWidth || h != this.mDrawableHeight) {
                this.mDrawableWidth = w;
                this.mDrawableHeight = h;
                this.requestLayout();
            }
        }
    }

    @Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (this.mDrawable != null) {
            this.mDrawable.setLayoutDirection(layoutDirection);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float desiredAspect = 0.0F;
        boolean resizeWidth = false;
        boolean resizeHeight = false;
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int w;
        int h;
        if (this.mDrawable == null) {
            this.mDrawableWidth = -1;
            this.mDrawableHeight = -1;
            h = 0;
            w = 0;
        } else {
            w = this.mDrawableWidth;
            h = this.mDrawableHeight;
            if (w <= 0) {
                w = 1;
            }
            if (h <= 0) {
                h = 1;
            }
            if (this.mAdjustViewBounds) {
                resizeWidth = widthSpecMode != 1073741824;
                resizeHeight = heightSpecMode != 1073741824;
                desiredAspect = (float) w / (float) h;
            }
        }
        int pleft = this.mPaddingLeft;
        int pright = this.mPaddingRight;
        int ptop = this.mPaddingTop;
        int pbottom = this.mPaddingBottom;
        int widthSize;
        int heightSize;
        if (!resizeWidth && !resizeHeight) {
            w += pleft + pright;
            h += ptop + pbottom;
            w = Math.max(w, this.getSuggestedMinimumWidth());
            h = Math.max(h, this.getSuggestedMinimumHeight());
            widthSize = resolveSizeAndState(w, widthMeasureSpec, 0);
            heightSize = resolveSizeAndState(h, heightMeasureSpec, 0);
        } else {
            widthSize = this.resolveAdjustedSize(w + pleft + pright, this.mMaxWidth, widthMeasureSpec);
            heightSize = this.resolveAdjustedSize(h + ptop + pbottom, this.mMaxHeight, heightMeasureSpec);
            if (desiredAspect != 0.0F) {
                float actualAspect = (float) (widthSize - pleft - pright) / (float) (heightSize - ptop - pbottom);
                if ((double) Math.abs(actualAspect - desiredAspect) > 1.0E-7) {
                    boolean done = false;
                    if (resizeWidth) {
                        int newWidth = (int) (desiredAspect * (float) (heightSize - ptop - pbottom)) + pleft + pright;
                        if (!resizeHeight) {
                            widthSize = this.resolveAdjustedSize(newWidth, this.mMaxWidth, widthMeasureSpec);
                        }
                        if (newWidth <= widthSize) {
                            widthSize = newWidth;
                            done = true;
                        }
                    }
                    if (!done && resizeHeight) {
                        int newHeight = (int) ((float) (widthSize - pleft - pright) / desiredAspect) + ptop + pbottom;
                        if (!resizeWidth) {
                            heightSize = this.resolveAdjustedSize(newHeight, this.mMaxHeight, heightMeasureSpec);
                        }
                        if (newHeight <= heightSize) {
                            heightSize = newHeight;
                        }
                    }
                }
            }
        }
        this.setMeasuredDimension(widthSize, heightSize);
    }

    private int resolveAdjustedSize(int desiredSize, int maxSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch(specMode) {
            case Integer.MIN_VALUE:
                result = Math.min(Math.min(desiredSize, specSize), maxSize);
                break;
            case 0:
                result = Math.min(desiredSize, maxSize);
                break;
            case 1073741824:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int width, int height, int prevWidth, int prevHeight) {
        super.onSizeChanged(width, height, prevWidth, prevHeight);
        this.configureBounds();
    }

    private void configureBounds() {
        if (this.mDrawable != null && this.isAttachedToWindow()) {
            int dwidth = this.mDrawableWidth;
            int dheight = this.mDrawableHeight;
            int vwidth = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
            int vheight = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
            boolean fits = (dwidth < 0 || vwidth == dwidth) && (dheight < 0 || vheight == dheight);
            if (dwidth > 0 && dheight > 0 && ImageView.ScaleType.FIT_XY != this.mScaleType) {
                this.mDrawable.setBounds(0, 0, dwidth, dheight);
                if (ImageView.ScaleType.MATRIX == this.mScaleType) {
                    if (this.mMatrix.isIdentity()) {
                        this.mDrawMatrix = null;
                    } else {
                        this.mDrawMatrix = this.mMatrix;
                    }
                } else if (fits) {
                    this.mDrawMatrix = null;
                } else if (ImageView.ScaleType.CENTER == this.mScaleType) {
                    this.mDrawMatrix = this.mMatrix;
                    this.mDrawMatrix.setTranslate((float) Math.round((float) (vwidth - dwidth) * 0.5F), (float) Math.round((float) (vheight - dheight) * 0.5F));
                } else if (ImageView.ScaleType.CENTER_CROP == this.mScaleType) {
                    this.mDrawMatrix = this.mMatrix;
                    float dx = 0.0F;
                    float dy = 0.0F;
                    float scale;
                    if (dwidth * vheight > vwidth * dheight) {
                        scale = (float) vheight / (float) dheight;
                        dx = ((float) vwidth - (float) dwidth * scale) * 0.5F;
                    } else {
                        scale = (float) vwidth / (float) dwidth;
                        dy = ((float) vheight - (float) dheight * scale) * 0.5F;
                    }
                    this.mDrawMatrix.setTranslate((float) Math.round(dx), (float) Math.round(dy));
                    this.mDrawMatrix.preScale(scale, scale);
                } else if (ImageView.ScaleType.CENTER_INSIDE == this.mScaleType) {
                    this.mDrawMatrix = this.mMatrix;
                    float scale;
                    if (dwidth <= vwidth && dheight <= vheight) {
                        scale = 1.0F;
                    } else {
                        scale = Math.min((float) vwidth / (float) dwidth, (float) vheight / (float) dheight);
                    }
                    float dx = (float) Math.round(((float) vwidth - (float) dwidth * scale) * 0.5F);
                    float dy = (float) Math.round(((float) vheight - (float) dheight * scale) * 0.5F);
                    this.mDrawMatrix.setTranslate(dx, dy);
                    this.mDrawMatrix.preScale(scale, scale);
                } else {
                    this.mDrawMatrix = this.mMatrix;
                    float tx = 0.0F;
                    float sx = (float) vwidth / (float) dwidth;
                    float ty = 0.0F;
                    float sy = (float) vheight / (float) dheight;
                    boolean xLarger = false;
                    if (this.mScaleType != ImageView.ScaleType.FIT_XY) {
                        if (sx > sy) {
                            xLarger = true;
                            sx = sy;
                        } else {
                            sy = sx;
                        }
                    }
                    if (this.mScaleType == ImageView.ScaleType.FIT_CENTER || this.mScaleType == ImageView.ScaleType.FIT_END) {
                        float diff;
                        if (xLarger) {
                            diff = (float) vwidth - (float) dwidth * sy;
                        } else {
                            diff = (float) vheight - (float) dheight * sy;
                        }
                        if (this.mScaleType == ImageView.ScaleType.FIT_CENTER) {
                            diff *= 0.5F;
                        }
                        if (xLarger) {
                            tx += diff;
                        } else {
                            ty += diff;
                        }
                    }
                    this.mDrawMatrix.setTranslate(tx, ty);
                    this.mDrawMatrix.preScale(sx, sy);
                }
            } else {
                this.mDrawable.setBounds(0, 0, vwidth, vheight);
                this.mDrawMatrix = null;
            }
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable drawable = this.mDrawable;
        if (drawable != null && drawable.isStateful() && drawable.setState(this.getDrawableState())) {
            this.invalidateDrawable(drawable);
        }
    }

    @Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mDrawable != null) {
            this.mDrawable.setHotspot(x, y);
        }
    }

    public void animateTransform(@Nullable Matrix matrix) {
        if (this.mDrawable != null) {
            if (matrix == null) {
                int vwidth = this.getWidth() - this.mPaddingLeft - this.mPaddingRight;
                int vheight = this.getHeight() - this.mPaddingTop - this.mPaddingBottom;
                this.mDrawable.setBounds(0, 0, vwidth, vheight);
                this.mDrawMatrix = null;
            } else {
                this.mDrawable.setBounds(0, 0, this.mDrawableWidth, this.mDrawableHeight);
                if (this.mDrawMatrix == null) {
                    this.mDrawMatrix = new Matrix();
                }
                this.mDrawMatrix.set(matrix);
            }
            this.invalidate();
        }
    }

    @Override
    protected void onDraw(@Nonnull Canvas canvas) {
        super.onDraw(canvas);
        if (this.mDrawable != null) {
            if (this.mDrawableWidth != 0 && this.mDrawableHeight != 0) {
                if (this.mDrawMatrix == null && this.mPaddingTop == 0 && this.mPaddingLeft == 0) {
                    this.mDrawable.draw(canvas);
                } else {
                    int saveCount = canvas.getSaveCount();
                    canvas.save();
                    if (this.mCropToPadding) {
                        int scrollX = this.mScrollX;
                        int scrollY = this.mScrollY;
                        canvas.clipRect((float) (scrollX + this.mPaddingLeft), (float) (scrollY + this.mPaddingTop), (float) (scrollX + this.getWidth() - this.mPaddingRight), (float) (scrollY + this.getHeight() - this.mPaddingBottom));
                    }
                    canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
                    if (this.mDrawMatrix != null) {
                        canvas.concat(this.mDrawMatrix);
                    }
                    this.mDrawable.draw(canvas);
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    @Override
    public int getBaseline() {
        return this.mBaselineAlignBottom ? this.getMeasuredHeight() : this.mBaseline;
    }

    public void setBaseline(int baseline) {
        if (this.mBaseline != baseline) {
            this.mBaseline = baseline;
            this.requestLayout();
        }
    }

    public void setBaselineAlignBottom(boolean aligned) {
        if (this.mBaselineAlignBottom != aligned) {
            this.mBaselineAlignBottom = aligned;
            this.requestLayout();
        }
    }

    public boolean getBaselineAlignBottom() {
        return this.mBaselineAlignBottom;
    }

    public int getImageAlpha() {
        return this.mImageAlpha;
    }

    public void setImageAlpha(int alpha) {
        alpha += alpha >> 7;
        if (this.mImageAlpha != alpha) {
            this.mImageAlpha = alpha;
            this.mHasImageAlpha = true;
            this.applyAlpha();
            this.invalidate();
        }
    }

    private void applyAlpha() {
        if (this.mDrawable != null && this.mHasImageAlpha) {
            this.mDrawable = this.mDrawable.mutate();
            this.mDrawable.setAlpha(this.mImageAlpha);
        }
    }

    @Override
    public void onVisibilityAggregated(boolean isVisible) {
        super.onVisibilityAggregated(isVisible);
        if (this.mDrawable != null) {
            this.mDrawable.setVisible(isVisible, false);
        }
    }

    public static enum ScaleType {

        MATRIX,
        FIT_XY,
        FIT_START,
        FIT_CENTER,
        FIT_END,
        CENTER,
        CENTER_CROP,
        CENTER_INSIDE
    }
}