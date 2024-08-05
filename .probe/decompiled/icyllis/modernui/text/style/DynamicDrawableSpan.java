package icyllis.modernui.text.style;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.drawable.Drawable;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.text.TextPaint;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class DynamicDrawableSpan extends ReplacementSpan {

    public static final int ALIGN_BOTTOM = 0;

    public static final int ALIGN_BASELINE = 1;

    public static final int ALIGN_CENTER = 2;

    protected final int mVerticalAlignment;

    private WeakReference<Drawable> mDrawableRef;

    public DynamicDrawableSpan() {
        this.mVerticalAlignment = 0;
    }

    protected DynamicDrawableSpan(int verticalAlignment) {
        this.mVerticalAlignment = verticalAlignment;
    }

    public int getVerticalAlignment() {
        return this.mVerticalAlignment;
    }

    public abstract Drawable getDrawable();

    @Override
    public int getSize(@Nonnull TextPaint paint, CharSequence text, int start, int end, @Nullable FontMetricsInt fm) {
        Drawable d = this.getCachedDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            fm.ascent = -rect.bottom;
            fm.descent = 0;
        }
        return rect.right;
    }

    @Override
    public void draw(@Nonnull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @Nonnull TextPaint paint) {
        Drawable b = this.getCachedDrawable();
        canvas.save();
        int transY = bottom - b.getBounds().bottom;
        if (this.mVerticalAlignment == 1) {
            transY -= paint.getFontMetricsInt().descent;
        } else if (this.mVerticalAlignment == 2) {
            transY = top + (bottom - top) / 2 - b.getBounds().height() / 2;
        }
        canvas.translate(x, (float) transY);
        b.draw(canvas);
        canvas.restore();
    }

    private Drawable getCachedDrawable() {
        WeakReference<Drawable> wr = this.mDrawableRef;
        Drawable d = null;
        if (wr != null) {
            d = (Drawable) wr.get();
        }
        if (d == null) {
            d = this.getDrawable();
            this.mDrawableRef = new WeakReference(d);
        }
        return d;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AlignmentType {
    }
}