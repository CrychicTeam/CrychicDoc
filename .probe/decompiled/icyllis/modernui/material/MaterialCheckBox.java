package icyllis.modernui.material;

import icyllis.modernui.core.Context;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import icyllis.modernui.graphics.drawable.StateListDrawable;
import icyllis.modernui.util.ColorStateList;
import icyllis.modernui.util.StateSet;
import icyllis.modernui.view.View;
import icyllis.modernui.widget.CheckBox;
import javax.annotation.Nonnull;

public class MaterialCheckBox extends CheckBox {

    private static final int[][] ENABLED_CHECKED_STATES = new int[][] { { 16842910, 16842912 }, { 16842910, -16842912 }, StateSet.WILD_CARD };

    private static final int[] COLORS = new int[] { -5579536, -7697782, -10395295 };

    public MaterialCheckBox(Context context) {
        super(context);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(CHECKED_STATE_SET, new MaterialCheckBox.CheckedDrawable(this));
        drawable.addState(StateSet.WILD_CARD, new MaterialCheckBox.UncheckedDrawable(this));
        drawable.setEnterFadeDuration(300);
        drawable.setExitFadeDuration(300);
        this.setButtonDrawable(drawable);
        this.setButtonTintList(new ColorStateList(ENABLED_CHECKED_STATES, COLORS));
    }

    private static class CheckedDrawable extends MaterialDrawable {

        private final float mRadius;

        CheckedDrawable(View view) {
            this.mRadius = (float) view.dp(4.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Rect r = this.getBounds();
            Paint paint = Paint.obtain();
            paint.setColor(this.mColor);
            paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
            int alpha = paint.getAlpha();
            if (alpha != 0) {
                float inner = this.mRadius * 0.5F;
                paint.setStyle(1);
                paint.setStrokeWidth(this.mRadius * 0.75F);
                canvas.drawRoundRect((float) r.left + inner, (float) r.top + inner, (float) r.right - inner, (float) r.bottom - inner, this.mRadius, paint);
                if (alpha != 255) {
                    paint.setAlpha(255);
                    canvas.saveLayer((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, alpha);
                }
                paint.setStyle(0);
                canvas.drawLine(this.mRadius * 1.5F, this.mRadius * 3.5F, this.mRadius * 2.5F, this.mRadius * 4.5F, this.mRadius * 0.75F, paint);
                canvas.drawLine(this.mRadius * 2.5F, this.mRadius * 4.5F, this.mRadius * 4.5F, this.mRadius * 2.0F, this.mRadius * 0.75F, paint);
                if (alpha != 255) {
                    canvas.restore();
                }
            }
            paint.recycle();
        }

        @Override
        public int getIntrinsicWidth() {
            return (int) (this.mRadius * 6.0F);
        }

        @Override
        public int getIntrinsicHeight() {
            return (int) (this.mRadius * 6.0F);
        }
    }

    private static class UncheckedDrawable extends MaterialDrawable {

        private final float mRadius;

        UncheckedDrawable(View view) {
            this.mRadius = (float) view.dp(4.0F);
        }

        @Override
        public void draw(@Nonnull Canvas canvas) {
            Rect r = this.getBounds();
            Paint paint = Paint.obtain();
            paint.setColor(this.mColor);
            paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
            if (paint.getAlpha() != 0) {
                float inner = this.mRadius * 0.5F;
                paint.setStyle(1);
                paint.setStrokeWidth(this.mRadius * 0.75F);
                canvas.drawRoundRect((float) r.left + inner, (float) r.top + inner, (float) r.right - inner, (float) r.bottom - inner, this.mRadius, paint);
            }
            paint.recycle();
        }

        @Override
        public int getIntrinsicWidth() {
            return (int) (this.mRadius * 6.0F);
        }

        @Override
        public int getIntrinsicHeight() {
            return (int) (this.mRadius * 6.0F);
        }
    }
}