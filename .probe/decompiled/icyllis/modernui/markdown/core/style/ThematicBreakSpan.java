package icyllis.modernui.markdown.core.style;

import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.markdown.MarkdownTheme;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.style.LeadingMarginSpan;

public class ThematicBreakSpan implements LeadingMarginSpan {

    private final MarkdownTheme mTheme;

    public ThematicBreakSpan(MarkdownTheme theme) {
        this.mTheme = theme;
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return 0;
    }

    @Override
    public void drawLeadingMargin(Canvas c, TextPaint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
    }

    @Override
    public void drawMargin(Canvas c, TextPaint p, int left, int right, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        int style = p.getStyle();
        int color = p.getColor();
        p.setStyle(0);
        p.setColor(this.mTheme.getThematicBreakColor());
        float cy = (float) (top + bottom) / 2.0F;
        float mid = p.getTextSize() / 6.0F;
        c.drawRect((float) left, cy - mid, (float) right, cy + mid, p);
        p.setStyle(style);
        p.setColor(color);
    }
}