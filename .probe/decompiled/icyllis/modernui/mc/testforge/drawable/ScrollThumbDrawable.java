package icyllis.modernui.mc.testforge.drawable;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.drawable.Drawable;
import javax.annotation.Nonnull;

@Deprecated
public class ScrollThumbDrawable extends Drawable {

    @Override
    public void draw(@Nonnull Canvas canvas) {
        Paint paint = Paint.obtain();
        paint.setStyle(0);
        paint.setRGBA(128, 128, 128, 128);
    }

    @Override
    public int getIntrinsicWidth() {
        return 10;
    }

    @Override
    public int getIntrinsicHeight() {
        return 10;
    }
}