package icyllis.modernui.markdown.core.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.markdown.MarkdownTheme;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.Typeface;
import icyllis.modernui.text.style.LeadingMarginSpan;
import icyllis.modernui.text.style.LineHeightSpan;
import icyllis.modernui.text.style.MetricAffectingSpan;

public class HeadingSpan extends MetricAffectingSpan implements LeadingMarginSpan, LineHeightSpan {

    private final MarkdownTheme mTheme;

    private final int mLevel;

    public HeadingSpan(MarkdownTheme theme, int level) {
        this.mTheme = theme;
        this.mLevel = level;
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        Typeface typeface = this.mTheme.getHeadingTypeface();
        if (typeface != null) {
            paint.setTypeface(typeface);
        }
        float multiplier = this.mTheme.getHeadingTextSizeMultiplier(this.mLevel);
        paint.setTextSize(paint.getTextSize() * multiplier);
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
        if (this.mLevel == 1 || this.mLevel == 2) {
            int style = p.getStyle();
            int color = p.getColor();
            p.setStyle(0);
            p.setColor(this.mTheme.getHeadingBreakColor());
            float mid = p.getTextSize() / 24.0F;
            float cy = (float) bottom - mid * 3.0F;
            c.drawRect((float) left, cy - mid, (float) right, cy + mid, p);
            p.setStyle(style);
            p.setColor(color);
        }
    }

    @Override
    public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int lineHeight, FontMetricsInt fm, TextPaint paint) {
        fm.descent = (int) ((float) fm.descent + (float) fm.descent * 0.5F);
    }
}