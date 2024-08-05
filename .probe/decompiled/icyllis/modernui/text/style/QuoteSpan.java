package icyllis.modernui.text.style;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.TextPaint;

public class QuoteSpan implements LeadingMarginSpan {

    private final int mBlockMargin;

    private final int mStripeWidth;

    private final int mColor;

    public QuoteSpan(int blockMargin, int stripeWidth, int color) {
        this.mBlockMargin = blockMargin;
        this.mStripeWidth = stripeWidth;
        this.mColor = color;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return this.mBlockMargin;
    }

    @Override
    public void drawLeadingMargin(Canvas c, TextPaint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        int style = p.getStyle();
        int color = p.getColor();
        p.setStyle(0);
        p.setColor(this.mColor);
        c.drawRect((float) x, (float) top, (float) (x + dir * this.mStripeWidth), (float) bottom, p);
        p.setStyle(style);
        p.setColor(color);
    }
}