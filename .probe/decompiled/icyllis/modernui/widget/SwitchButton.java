package icyllis.modernui.widget;

import icyllis.modernui.animation.Animator;
import icyllis.modernui.animation.ColorEvaluator;
import icyllis.modernui.animation.ObjectAnimator;
import icyllis.modernui.animation.TimeInterpolator;
import icyllis.modernui.animation.ValueAnimator;
import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.RectF;
import javax.annotation.Nonnull;

public class SwitchButton extends CompoundButton {

    private final RectF mButtonRect = new RectF();

    private float mThumbPosition;

    private int mInsideColor;

    private float mInsideRadius;

    private int mCheckedColor = -3300456;

    private int mUncheckedColor = -2236963;

    private int mBorderWidth = this.dp(1.5F);

    private final Animator mAnimator;

    public SwitchButton(Context context) {
        super(context);
        this.mInsideColor = this.mUncheckedColor;
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(this);
        animator.setInterpolator(null);
        animator.addUpdateListener(this::onAnimationUpdate);
        this.mAnimator = animator;
    }

    private void onAnimationUpdate(@Nonnull ValueAnimator animator) {
        float fraction = animator.getAnimatedFraction();
        if (!this.isChecked()) {
            fraction = 1.0F - fraction;
            this.mThumbPosition = TimeInterpolator.ACCELERATE.getInterpolation(fraction);
        } else {
            this.mThumbPosition = TimeInterpolator.DECELERATE.getInterpolation(fraction);
        }
        this.mInsideRadius = this.mButtonRect.height() * 0.5F * fraction;
        this.mInsideColor = ColorEvaluator.evaluate(fraction, this.mUncheckedColor, this.mCheckedColor);
        this.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = this.getMeasuredWidth();
        int maxHeight = (int) ((float) measuredWidth / 1.2F);
        if (this.getMeasuredHeight() > maxHeight) {
            this.setMeasuredDimension(measuredWidth, maxHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int thickness = (int) Math.ceil((double) ((float) this.mBorderWidth / 2.0F));
        this.mButtonRect.set(0.0F, 0.0F, (float) (right - left), (float) (bottom - top));
        this.mButtonRect.inset((float) thickness, (float) thickness);
        if (this.isChecked()) {
            this.mInsideRadius = this.mButtonRect.height() * 0.5F;
        }
    }

    @Override
    protected void onDraw(@Nonnull Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = Paint.obtain();
        float buttonRadius = this.mButtonRect.height() * 0.5F;
        float thumbX = this.mButtonRect.left + buttonRadius + (float) this.getThumbOffset();
        float thumbY = this.mButtonRect.top + buttonRadius;
        paint.setColor(this.mInsideColor);
        if (MathUtil.isApproxEqual(this.mInsideRadius, buttonRadius)) {
            paint.setStyle(0);
            canvas.drawRoundRect(this.mButtonRect, buttonRadius, paint);
        } else if (this.mInsideRadius > 0.0F) {
            float thickness = this.mInsideRadius * 0.5F;
            paint.setStyle(0);
            if (this.isLayoutRtl()) {
                canvas.drawCircle(this.mButtonRect.right - buttonRadius, thumbY, buttonRadius - thickness, paint);
                canvas.drawRect(thumbX, this.mButtonRect.top, this.mButtonRect.right - buttonRadius, this.mButtonRect.bottom, paint);
            } else {
                canvas.drawCircle(this.mButtonRect.left + buttonRadius, thumbY, buttonRadius - thickness, paint);
                canvas.drawRect(this.mButtonRect.left + buttonRadius, this.mButtonRect.top, thumbX, this.mButtonRect.bottom, paint);
            }
            paint.setStyle(1);
            paint.setStrokeWidth(this.mInsideRadius);
            canvas.drawRoundRect(this.mButtonRect.left + thickness, this.mButtonRect.top + thickness, this.mButtonRect.right - thickness, this.mButtonRect.bottom - thickness, buttonRadius - thickness, paint);
        }
        paint.setStyle(1);
        paint.setStrokeWidth((float) this.mBorderWidth);
        paint.setColor(this.mUncheckedColor);
        canvas.drawRoundRect(this.mButtonRect, buttonRadius, paint);
        paint.setStyle(0);
        paint.setColor(-1);
        canvas.drawCircle(thumbX, thumbY, buttonRadius, paint);
        paint.recycle();
    }

    @Override
    public void setChecked(boolean checked) {
        boolean oldChecked = this.isChecked();
        super.setChecked(checked);
        if (oldChecked != checked) {
            this.mAnimator.start();
            if (!this.isAttachedToWindow()) {
                this.mAnimator.end();
            }
        }
    }

    public void setCheckedColor(int checkedColor) {
        if (checkedColor != this.mCheckedColor) {
            this.mCheckedColor = checkedColor;
            if (this.isChecked()) {
                this.mInsideColor = checkedColor;
            }
            this.invalidate();
        }
    }

    public void setUncheckedColor(int uncheckedColor) {
        if (uncheckedColor != this.mUncheckedColor) {
            this.mUncheckedColor = uncheckedColor;
            if (!this.isChecked()) {
                this.mInsideColor = uncheckedColor;
            }
            this.invalidate();
        }
    }

    public void setBorderWidth(int borderWidth) {
        if (borderWidth != this.mBorderWidth) {
            this.mBorderWidth = borderWidth;
            this.requestLayout();
        }
    }

    private int getThumbOffset() {
        float thumbPosition;
        if (this.isLayoutRtl()) {
            thumbPosition = 1.0F - this.mThumbPosition;
        } else {
            thumbPosition = this.mThumbPosition;
        }
        return (int) (thumbPosition * (float) this.getThumbScrollRange() + 0.5F);
    }

    private int getThumbScrollRange() {
        return (int) Math.ceil((double) (this.mButtonRect.width() - this.mButtonRect.height()));
    }
}