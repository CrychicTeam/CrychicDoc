package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.ShapedText;
import icyllis.modernui.text.style.ParagraphStyle;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BoringLayout extends Layout implements TextUtils.EllipsizeCallback {

    private ShapedText mDirect;

    private TextPaint mPaint;

    int mBottom;

    int mDesc;

    private int mTopPadding;

    private int mBottomPadding;

    private float mMax;

    private int mEllipsizedWidth;

    private int mEllipsizedStart;

    private int mEllipsizedCount;

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad) {
        return new BoringLayout(source, paint, outerWidth, align, metrics, includePad);
    }

    public static BoringLayout make(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        return new BoringLayout(source, paint, outerWidth, align, metrics, includePad, ellipsize, ellipsizedWidth);
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad) {
        this.replaceWith(source, paint, outerWidth, align);
        this.mEllipsizedWidth = outerWidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        this.init(source, paint, align, metrics, includePad, true);
        return this;
    }

    public BoringLayout replaceOrMake(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        boolean trust;
        if (ellipsize != null && ellipsize != TextUtils.TruncateAt.MARQUEE) {
            this.replaceWith(TextUtils.ellipsize(source, paint, (float) ellipsizedWidth, ellipsize, true, this), paint, outerWidth, align);
            this.mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        } else {
            this.replaceWith(source, paint, outerWidth, align);
            this.mEllipsizedWidth = outerWidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        }
        this.init(this.getText(), paint, align, metrics, includePad, trust);
        return this;
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad) {
        super(source, paint, outerWidth, align);
        this.mEllipsizedWidth = outerWidth;
        this.mEllipsizedStart = 0;
        this.mEllipsizedCount = 0;
        this.init(source, paint, align, metrics, includePad, true);
    }

    public BoringLayout(CharSequence source, TextPaint paint, int outerWidth, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad, TextUtils.TruncateAt ellipsize, int ellipsizedWidth) {
        super(source, paint, outerWidth, align);
        boolean trust;
        if (ellipsize != null && ellipsize != TextUtils.TruncateAt.MARQUEE) {
            this.replaceWith(TextUtils.ellipsize(source, paint, (float) ellipsizedWidth, ellipsize, true, this), paint, outerWidth, align);
            this.mEllipsizedWidth = ellipsizedWidth;
            trust = false;
        } else {
            this.mEllipsizedWidth = outerWidth;
            this.mEllipsizedStart = 0;
            this.mEllipsizedCount = 0;
            trust = true;
        }
        this.init(this.getText(), paint, align, metrics, includePad, trust);
    }

    void init(CharSequence source, TextPaint paint, Layout.Alignment align, BoringLayout.Metrics metrics, boolean includePad, boolean trustWidth) {
        if (!(source instanceof String) || align != Layout.Alignment.ALIGN_NORMAL && align != Layout.Alignment.ALIGN_LEFT) {
            this.mDirect = null;
        } else {
            String direct = source.toString();
            int len = direct.length();
            this.mDirect = TextShaper.shapeTextRun(direct, 0, len, 0, len, false, paint);
        }
        this.mPaint = paint;
        int spacing = metrics.descent - metrics.ascent;
        this.mDesc = metrics.descent;
        this.mBottom = spacing;
        if (trustWidth) {
            this.mMax = (float) metrics.width;
        } else {
            TextLine line = TextLine.obtain();
            line.set(paint, source, 0, source.length(), 1, Directions.ALL_LEFT_TO_RIGHT, false, null, this.mEllipsizedStart, this.mEllipsizedStart + this.mEllipsizedCount);
            this.mMax = (float) ((int) Math.ceil((double) line.metrics(null)));
            line.recycle();
        }
    }

    public static BoringLayout.Metrics isBoring(CharSequence text, TextPaint paint) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, null);
    }

    public static BoringLayout.Metrics isBoring(CharSequence text, TextPaint paint, BoringLayout.Metrics metrics) {
        return isBoring(text, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR, metrics);
    }

    private static boolean hasAnyInterestingChars(CharSequence text, int textLength) {
        int MAX_BUF_LEN = 500;
        char[] buffer = TextUtils.obtain(500);
        try {
            for (int start = 0; start < textLength; start += 500) {
                int end = Math.min(start + 500, textLength);
                TextUtils.getChars(text, start, end, buffer, 0);
                int len = end - start;
                for (int i = 0; i < len; i++) {
                    char c = buffer[i];
                    if (c == '\n' || c == '\t' || TextUtils.couldAffectRtl(c)) {
                        return true;
                    }
                }
            }
            return false;
        } finally {
            TextUtils.recycle(buffer);
        }
    }

    @Nullable
    public static BoringLayout.Metrics isBoring(@Nonnull CharSequence text, TextPaint paint, TextDirectionHeuristic textDir, BoringLayout.Metrics metrics) {
        int textLength = text.length();
        if (hasAnyInterestingChars(text, textLength)) {
            return null;
        } else if (textDir != null && textDir.isRtl(text, 0, textLength)) {
            return null;
        } else {
            if (text instanceof Spanned sp) {
                List<?> styles = sp.getSpans(0, textLength, ParagraphStyle.class);
                if (!styles.isEmpty()) {
                    return null;
                }
            }
            BoringLayout.Metrics fm = metrics;
            if (metrics == null) {
                fm = new BoringLayout.Metrics();
            } else {
                metrics.reset();
            }
            TextLine line = TextLine.obtain();
            line.set(paint, text, 0, textLength, 1, Directions.ALL_LEFT_TO_RIGHT, false, null, 0, 0);
            fm.width = (int) Math.ceil((double) line.metrics(fm));
            line.recycle();
            return fm;
        }
    }

    @Override
    public int getHeight() {
        return this.mBottom;
    }

    @Override
    public int getLineCount() {
        return 1;
    }

    @Override
    public int getLineTop(int line) {
        return line == 0 ? 0 : this.mBottom;
    }

    @Override
    public int getLineDescent(int line) {
        return this.mDesc;
    }

    @Override
    public int getLineStart(int line) {
        return line == 0 ? 0 : this.getText().length();
    }

    @Override
    public int getParagraphDirection(int line) {
        return 1;
    }

    @Override
    public boolean getLineContainsTab(int line) {
        return false;
    }

    @Override
    public float getLineMax(int line) {
        return this.mMax;
    }

    @Override
    public float getLineWidth(int line) {
        return line == 0 ? this.mMax : 0.0F;
    }

    @Override
    public final Directions getLineDirections(int line) {
        return Directions.ALL_LEFT_TO_RIGHT;
    }

    @Override
    public int getTopPadding() {
        return this.mTopPadding;
    }

    @Override
    public int getBottomPadding() {
        return this.mBottomPadding;
    }

    @Override
    public int getEllipsisCount(int line) {
        return this.mEllipsizedCount;
    }

    @Override
    public int getEllipsisStart(int line) {
        return this.mEllipsizedStart;
    }

    @Override
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @Override
    public void drawText(@NonNull Canvas canvas, int firstLine, int lastLine) {
        if (this.mDirect != null) {
            canvas.drawShapedText(this.mDirect, 0.0F, (float) (this.mBottom - this.mDesc), this.mPaint);
        } else {
            super.drawText(canvas, firstLine, lastLine);
        }
    }

    @Override
    public void ellipsized(int start, int end) {
        this.mEllipsizedStart = start;
        this.mEllipsizedCount = end - start;
    }

    public static class Metrics extends FontMetricsInt {

        public int width;

        @Override
        public void reset() {
            super.reset();
            this.width = 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o == null || this.getClass() != o.getClass()) {
                return false;
            } else if (!super.equals(o)) {
                return false;
            } else {
                BoringLayout.Metrics metrics = (BoringLayout.Metrics) o;
                return this.width == metrics.width;
            }
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            return 31 * result + this.width;
        }

        @Override
        public String toString() {
            return super.toString() + ", width=" + this.width;
        }
    }
}