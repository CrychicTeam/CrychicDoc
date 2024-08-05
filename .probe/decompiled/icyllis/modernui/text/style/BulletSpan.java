package icyllis.modernui.text.style;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.TextPaint;

public class BulletSpan implements LeadingMarginSpan {

    public static final int STYLE_DISC = 0;

    public static final int STYLE_CIRCLE = 1;

    public static final int STYLE_SQUARE = 2;

    private final int mBlockMargin;

    private final int mBulletWidth;

    private final int mColor;

    private final int mStyle;

    public BulletSpan(int blockMargin, int bulletWidth, int color, int style) {
        this.mBlockMargin = blockMargin;
        this.mBulletWidth = bulletWidth;
        this.mColor = color;
        this.mStyle = style;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return this.mBlockMargin;
    }

    @Override
    public void drawLeadingMargin(Canvas c, TextPaint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (first && ((Spanned) text).getSpanStart(this) == start) {
            int oldStyle = p.getStyle();
            boolean restoreStrokeWidth = false;
            float oldStrokeWidth = 0.0F;
            boolean restoreColor = this.mColor != 0;
            int oldColor = restoreColor ? p.getColor() : 0;
            int width = this.mBlockMargin;
            int height = bottom - top;
            int bulletWidth = Math.min(height, width) / 2;
            if (this.mBulletWidth != 0) {
                bulletWidth = Math.min(this.mBulletWidth, bulletWidth);
            } else {
                bulletWidth /= 2;
            }
            float l;
            float r;
            if (dir > 0) {
                l = (float) x + (float) width / 2.0F;
                r = l + (float) bulletWidth;
            } else {
                r = (float) x - (float) width / 2.0F;
                l = r - (float) bulletWidth;
            }
            float cy = (float) (top + bottom) / 2.0F;
            if (restoreColor) {
                p.setColor(this.mColor);
            }
            float radius = (float) bulletWidth / 2.0F;
            if (this.mStyle != 0 && this.mStyle != 1) {
                p.setStyle(0);
                c.drawRect(l, cy - radius, r, cy + radius, p);
            } else {
                if (this.mStyle == 0) {
                    p.setStyle(0);
                } else {
                    p.setStyle(1);
                    restoreStrokeWidth = true;
                    oldStrokeWidth = p.getStrokeWidth();
                    float sw = Math.max(1.0F, p.getTextSize() / 16.0F);
                    p.setStrokeWidth(sw);
                    radius -= sw / 2.0F;
                }
                c.drawCircle((l + r) / 2.0F, cy, radius, p);
            }
            p.setStyle(oldStyle);
            if (restoreStrokeWidth) {
                p.setStrokeWidth(oldStrokeWidth);
            }
            if (restoreColor) {
                p.setColor(oldColor);
            }
        }
    }
}