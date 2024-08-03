package icyllis.modernui.mc.ui;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.drawable.ShapeDrawable;
import javax.annotation.Nonnull;

@Deprecated
public class RectangleDrawable extends Drawable {

    private int mAlpha = 255;

    @Override
    public void draw(@Nonnull Canvas canvas) {
        Paint paint = Paint.obtain();
        paint.setColor(-2136956768);
        paint.setAlpha(ShapeDrawable.modulateAlpha(paint.getAlpha(), this.mAlpha));
        if (paint.getAlpha() != 0) {
            Rect b = this.getBounds();
            canvas.drawRoundRect((float) b.left, (float) b.top, (float) b.right, (float) b.bottom, 3.0F, paint);
        }
        paint.recycle();
    }

    @Override
    public void setAlpha(int alpha) {
        if (this.mAlpha != alpha) {
            this.mAlpha = alpha;
            this.invalidateSelf();
        }
    }

    @Override
    public int getAlpha() {
        return this.mAlpha;
    }
}