package icyllis.modernui.markdown.core.style;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.text.ShapedText;
import icyllis.modernui.markdown.MarkdownTheme;
import icyllis.modernui.text.Layout;
import icyllis.modernui.text.Spanned;
import icyllis.modernui.text.TextDirectionHeuristic;
import icyllis.modernui.text.TextDirectionHeuristics;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.text.TextShaper;
import icyllis.modernui.text.style.LeadingMarginSpan;
import icyllis.modernui.widget.TextView;
import java.util.List;

public class OrderedListItemSpan implements LeadingMarginSpan {

    private final MarkdownTheme mTheme;

    private final String mNumber;

    private ShapedText mShapedNumber;

    public static void measure(@NonNull TextView textView, @NonNull Spanned markdown) {
        List<OrderedListItemSpan> spans = markdown.getSpans(0, markdown.length(), OrderedListItemSpan.class);
        TextDirectionHeuristic dir = textView.getTextDirectionHeuristic();
        TextPaint paint = textView.getPaint();
        for (OrderedListItemSpan span : spans) {
            span.shape(dir, paint);
        }
    }

    public OrderedListItemSpan(MarkdownTheme theme, String number) {
        this.mTheme = theme;
        this.mNumber = number;
    }

    private void shape(@NonNull TextDirectionHeuristic dir, @NonNull TextPaint paint) {
        this.mShapedNumber = TextShaper.shapeText(this.mNumber, 0, this.mNumber.length(), dir, paint);
    }

    @Override
    public int getLeadingMargin(boolean first) {
        int margin = this.mTheme.getListItemMargin();
        if (this.mShapedNumber != null) {
            int adv = Math.round(this.mShapedNumber.getAdvance());
            if (adv > margin) {
                int mid = (margin + 1) / 2;
                return (int) Math.ceil((double) ((float) adv / (float) mid)) * mid;
            }
        }
        return margin;
    }

    @Override
    public void drawLeadingMargin(Canvas c, TextPaint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
        if (first && ((Spanned) text).getSpanStart(this) == start) {
            if (this.mShapedNumber == null) {
                this.shape(TextDirectionHeuristics.FIRSTSTRONG_LTR, p);
            }
            int width = this.getLeadingMargin(false);
            if (dir > 0) {
                x = (int) ((float) x + ((float) width - this.mShapedNumber.getAdvance()));
            } else {
                x -= width;
            }
            int oldColor = 0;
            int newColor = this.mTheme.getListItemColor();
            if (newColor != 0) {
                oldColor = p.getColor();
                p.setColor(newColor);
            }
            c.drawShapedText(this.mShapedNumber, (float) x, (float) baseline, p);
            if (newColor != 0) {
                p.setColor(oldColor);
            }
        }
    }
}