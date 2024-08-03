package icyllis.modernui.markdown.core.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.markdown.MarkdownTheme;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.style.LeadingMarginSpan;
import icyllis.modernui.text.style.MetricAffectingSpan;
import icyllis.modernui.text.style.TrailingMarginSpan;

public class CodeBlockSpan extends MetricAffectingSpan implements LeadingMarginSpan, TrailingMarginSpan {

    private final MarkdownTheme mTheme;

    public CodeBlockSpan(MarkdownTheme theme) {
        this.mTheme = theme;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint paint) {
        super.updateDrawState(paint);
        int color = this.mTheme.getCodeBlockTextColor();
        if (color != 0) {
            paint.setColor(color);
        }
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        paint.setTypeface(this.mTheme.getCodeBlockTypeface());
        int textSize = this.mTheme.getCodeBlockTextSize();
        if (textSize > 0) {
            paint.setTextSize((float) textSize);
        } else {
            paint.setTextSize(paint.getTextSize() * 0.875F);
        }
    }

    @Override
    public int getLeadingMargin(boolean first) {
        return this.mTheme.getCodeBlockMargin();
    }

    @Override
    public int getTrailingMargin() {
        return this.mTheme.getCodeBlockMargin();
    }

    @Override
    public void drawLeadingMargin(Canvas c, TextPaint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
    }

    @Override
    public void drawMargin(Canvas c, TextPaint p, int left, int right, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        int color = p.getColor();
        p.setColor(this.mTheme.getCodeBlockBackgroundColor());
        c.drawRect((float) left, (float) top, (float) right, (float) bottom, p);
        p.setColor(color);
    }
}