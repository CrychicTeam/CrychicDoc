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
import icyllis.modernui.widget.RadioButton;
import javax.annotation.Nonnull;

public class MaterialRadioButton extends RadioButton {

    private static final int[][] ENABLED_CHECKED_STATES = new int[][] { { 16842910, 16842912 }, { 16842910, -16842912 }, StateSet.WILD_CARD };

    private static final int[] COLORS = new int[] { -5579536, -7697782, -10395295 };

    public MaterialRadioButton(Context context) {
        super(context);
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(CHECKED_STATE_SET, new MaterialRadioButton.CheckedDrawable(this));
        drawable.addState(StateSet.WILD_CARD, new MaterialRadioButton.UncheckedDrawable(this));
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
            float cx = r.exactCenterX();
            float cy = r.exactCenterY();
            Paint paint = Paint.obtain();
            paint.setColor(this.mColor);
            paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
            if (paint.getAlpha() != 0) {
                canvas.drawCircle(cx, cy, this.mRadius, paint);
                paint.setStyle(1);
                paint.setStrokeWidth(this.mRadius * 0.5F);
                canvas.drawCircle(cx, cy, this.mRadius * 1.6F, paint);
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
            float cx = r.exactCenterX();
            float cy = r.exactCenterY();
            Paint paint = Paint.obtain();
            paint.setColor(this.mColor);
            paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
            if (paint.getAlpha() != 0) {
                paint.setStyle(1);
                paint.setStrokeWidth(this.mRadius * 0.5F);
                canvas.drawCircle(cx, cy, this.mRadius * 1.6F, paint);
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