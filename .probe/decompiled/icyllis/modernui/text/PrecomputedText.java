package icyllis.modernui.text;

import icyllis.modernui.annotation.FloatRange;
import icyllis.modernui.annotation.IntRange;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.LineBreakConfig;
import icyllis.modernui.text.style.MetricAffectingSpan;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus.Internal;

public class PrecomputedText implements Spannable {

    @NonNull
    private final SpannableString mText;

    @IntRange(from = 0L)
    private final int mStart;

    @IntRange(from = 0L)
    private final int mEnd;

    @NonNull
    private final PrecomputedText.Params mParams;

    @NonNull
    private final PrecomputedText.ParagraphInfo[] mParagraphInfo;

    public static PrecomputedText create(@NonNull CharSequence text, @NonNull PrecomputedText.Params params) {
        PrecomputedText.ParagraphInfo[] paraInfo = null;
        if (text instanceof PrecomputedText hintPct) {
            PrecomputedText.Params hintParams = hintPct.getParams();
            int checkResult = hintParams.checkResultUsable(params.mPaint, params.mTextDir, params.mLineBreakConfig);
            switch(checkResult) {
                case 0:
                default:
                    break;
                case 1:
                    paraInfo = createMeasuredParagraphsFromPrecomputedText(hintPct, params, true);
                    break;
                case 2:
                    return hintPct;
            }
        }
        if (paraInfo == null) {
            paraInfo = createMeasuredParagraphs(text, params, 0, text.length(), true);
        }
        return new PrecomputedText(text, 0, text.length(), params, paraInfo);
    }

    private static PrecomputedText.ParagraphInfo[] createMeasuredParagraphsFromPrecomputedText(@NonNull PrecomputedText pct, @NonNull PrecomputedText.Params params, boolean computeLayout) {
        ArrayList<PrecomputedText.ParagraphInfo> result = new ArrayList();
        for (int i = 0; i < pct.getParagraphCount(); i++) {
            int paraStart = pct.getParagraphStart(i);
            int paraEnd = pct.getParagraphEnd(i);
            result.add(new PrecomputedText.ParagraphInfo(paraEnd, MeasuredParagraph.buildForStaticLayout(params.getTextPaint(), params.getLineBreakConfig(), pct, paraStart, paraEnd, params.getTextDirection(), computeLayout, null)));
        }
        return (PrecomputedText.ParagraphInfo[]) result.toArray(new PrecomputedText.ParagraphInfo[0]);
    }

    @Internal
    public static PrecomputedText.ParagraphInfo[] createMeasuredParagraphs(@NonNull CharSequence text, @NonNull PrecomputedText.Params params, @IntRange(from = 0L) int start, @IntRange(from = 0L) int end, boolean computeLayout) {
        ArrayList<PrecomputedText.ParagraphInfo> result = new ArrayList();
        Objects.requireNonNull(text);
        Objects.requireNonNull(params);
        int paraStart = start;
        while (paraStart < end) {
            int paraEnd = TextUtils.indexOf(text, '\n', paraStart, end);
            if (paraEnd < 0) {
                paraEnd = end;
            } else {
                paraEnd++;
            }
            result.add(new PrecomputedText.ParagraphInfo(paraEnd, MeasuredParagraph.buildForStaticLayout(params.getTextPaint(), params.getLineBreakConfig(), text, paraStart, paraEnd, params.getTextDirection(), computeLayout, null)));
            paraStart = paraEnd;
        }
        return (PrecomputedText.ParagraphInfo[]) result.toArray(new PrecomputedText.ParagraphInfo[0]);
    }

    private PrecomputedText(@NonNull CharSequence text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull PrecomputedText.Params params, @NonNull PrecomputedText.ParagraphInfo[] paraInfo) {
        this.mText = new SpannableString(text, true);
        this.mStart = start;
        this.mEnd = end;
        this.mParams = params;
        this.mParagraphInfo = paraInfo;
    }

    @Internal
    @NonNull
    public CharSequence getText() {
        return this.mText;
    }

    @Internal
    @IntRange(from = 0L)
    public int getStart() {
        return this.mStart;
    }

    @Internal
    @IntRange(from = 0L)
    public int getEnd() {
        return this.mEnd;
    }

    @NonNull
    public PrecomputedText.Params getParams() {
        return this.mParams;
    }

    @IntRange(from = 0L)
    public int getParagraphCount() {
        return this.mParagraphInfo.length;
    }

    @IntRange(from = 0L)
    public int getParagraphStart(@IntRange(from = 0L) int paraIndex) {
        Objects.checkIndex(paraIndex, this.getParagraphCount());
        return paraIndex == 0 ? this.mStart : this.getParagraphEnd(paraIndex - 1);
    }

    @IntRange(from = 0L)
    public int getParagraphEnd(@IntRange(from = 0L) int paraIndex) {
        Objects.checkIndex(paraIndex, this.getParagraphCount());
        return this.mParagraphInfo[paraIndex].paragraphEnd;
    }

    @Internal
    @NonNull
    public MeasuredParagraph getMeasuredParagraph(@IntRange(from = 0L) int paraIndex) {
        return this.mParagraphInfo[paraIndex].measured;
    }

    @Internal
    @NonNull
    public PrecomputedText.ParagraphInfo[] getParagraphInfo() {
        return this.mParagraphInfo;
    }

    @Internal
    public int checkResultUsable(@IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull TextDirectionHeuristic textDir, @NonNull TextPaint paint, @NonNull LineBreakConfig lbConfig) {
        return this.mStart == start && this.mEnd == end ? this.mParams.checkResultUsable(paint, textDir, lbConfig) : 0;
    }

    @Internal
    public int findParaIndex(@IntRange(from = 0L) int pos) {
        for (int i = 0; i < this.mParagraphInfo.length; i++) {
            if (pos < this.mParagraphInfo[i].paragraphEnd) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException("pos must be less than " + this.mParagraphInfo[this.mParagraphInfo.length - 1].paragraphEnd + ", gave " + pos);
    }

    @FloatRange(from = 0.0)
    public float getWidth(@IntRange(from = 0L) int start, @IntRange(from = 0L) int end) {
        Objects.checkFromToIndex(start, end, this.mText.length());
        if (start == end) {
            return 0.0F;
        } else {
            int paraIndex = this.findParaIndex(start);
            int paraStart = this.getParagraphStart(paraIndex);
            int paraEnd = this.getParagraphEnd(paraIndex);
            if (start >= paraStart && paraEnd >= end) {
                return this.getMeasuredParagraph(paraIndex).getAdvance(start - paraStart, end - paraStart);
            } else {
                throw new IllegalArgumentException("Cannot measured across the paragraph:para: (" + paraStart + ", " + paraEnd + "), request: (" + start + ", " + end + ")");
            }
        }
    }

    public void getFontMetricsInt(@IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull FontMetricsInt outMetrics) {
        Objects.checkFromToIndex(start, end, this.mText.length());
        Objects.requireNonNull(outMetrics);
        if (start == end) {
            this.mParams.getTextPaint().getFontMetricsInt(outMetrics);
        } else {
            int paraIndex = this.findParaIndex(start);
            int paraStart = this.getParagraphStart(paraIndex);
            int paraEnd = this.getParagraphEnd(paraIndex);
            if (start >= paraStart && paraEnd >= end) {
                this.getMeasuredParagraph(paraIndex).getExtent(start - paraStart, end - paraStart, outMetrics);
            } else {
                throw new IllegalArgumentException("Cannot measured across the paragraph:para: (" + paraStart + ", " + paraEnd + "), request: (" + start + ", " + end + ")");
            }
        }
    }

    @Internal
    public int getMemoryUsage() {
        int r = 0;
        for (PrecomputedText.ParagraphInfo info : this.mParagraphInfo) {
            r += info.measured.getMemoryUsage();
        }
        return r;
    }

    @Override
    public void setSpan(@NonNull Object what, int start, int end, int flags) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        } else {
            this.mText.setSpan(what, start, end, flags);
        }
    }

    @Override
    public void removeSpan(@NonNull Object what) {
        if (what instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        } else {
            this.mText.removeSpan(what);
        }
    }

    @NonNull
    @Override
    public <T> List<T> getSpans(int start, int end, @Nullable Class<? extends T> type, @Nullable List<T> dest) {
        return this.mText.getSpans(start, end, type, dest);
    }

    @Override
    public int getSpanStart(@NonNull Object tag) {
        return this.mText.getSpanStart(tag);
    }

    @Override
    public int getSpanEnd(@NonNull Object tag) {
        return this.mText.getSpanEnd(tag);
    }

    @Override
    public int getSpanFlags(@NonNull Object tag) {
        return this.mText.getSpanFlags(tag);
    }

    @Override
    public int nextSpanTransition(int start, int limit, @Nullable Class<?> type) {
        return this.mText.nextSpanTransition(start, limit, type);
    }

    public int length() {
        return this.mText.length();
    }

    public char charAt(int index) {
        return this.mText.charAt(index);
    }

    public CharSequence subSequence(int start, int end) {
        return create(this.mText.subSequence(start, end), this.mParams);
    }

    public String toString() {
        return this.mText.toString();
    }

    @Internal
    public static class ParagraphInfo {

        @IntRange(from = 0L)
        public final int paragraphEnd;

        @NonNull
        public final MeasuredParagraph measured;

        public ParagraphInfo(@IntRange(from = 0L) int paraEnd, @NonNull MeasuredParagraph measured) {
            this.paragraphEnd = paraEnd;
            this.measured = measured;
        }
    }

    public static final class Params {

        @NonNull
        private final TextPaint mPaint;

        @NonNull
        private final TextDirectionHeuristic mTextDir;

        @NonNull
        private final LineBreakConfig mLineBreakConfig;

        @Internal
        public static final int UNUSABLE = 0;

        @Internal
        public static final int NEED_RECOMPUTE = 1;

        @Internal
        public static final int USABLE = 2;

        @Internal
        public Params(@NonNull TextPaint paint, @NonNull LineBreakConfig lineBreakConfig, @NonNull TextDirectionHeuristic textDir) {
            this.mPaint = paint;
            this.mTextDir = textDir;
            this.mLineBreakConfig = lineBreakConfig;
        }

        @NonNull
        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        @NonNull
        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        @NonNull
        public LineBreakConfig getLineBreakConfig() {
            return this.mLineBreakConfig;
        }

        @Internal
        public int checkResultUsable(@NonNull TextPaint paint, @NonNull TextDirectionHeuristic textDir, @NonNull LineBreakConfig lbConfig) {
            if (this.mLineBreakConfig.equals(lbConfig) && this.mPaint.equalsForTextMeasurement(paint)) {
                return this.mTextDir == textDir ? 2 : 1;
            } else {
                return 0;
            }
        }

        public boolean equals(@Nullable Object o) {
            if (o == this) {
                return true;
            } else {
                return o instanceof PrecomputedText.Params param ? this.checkResultUsable(param.mPaint, param.mTextDir, param.mLineBreakConfig) == 2 : false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.mPaint.getInternalPaint(), this.mTextDir, this.mLineBreakConfig });
        }

        public String toString() {
            return "{" + this.mPaint.getInternalPaint() + ", textDir=" + this.mTextDir + ", " + this.mLineBreakConfig + "}";
        }

        public static class Builder {

            @NonNull
            private final TextPaint mPaint;

            private TextDirectionHeuristic mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;

            @NonNull
            private LineBreakConfig mLineBreakConfig = LineBreakConfig.NONE;

            public Builder(@NonNull TextPaint paint) {
                this.mPaint = paint;
            }

            public Builder(@NonNull PrecomputedText.Params params) {
                this.mPaint = params.mPaint;
                this.mTextDir = params.mTextDir;
                this.mLineBreakConfig = params.mLineBreakConfig;
            }

            public PrecomputedText.Params.Builder setTextDirection(@NonNull TextDirectionHeuristic textDir) {
                this.mTextDir = textDir;
                return this;
            }

            @NonNull
            public PrecomputedText.Params.Builder setLineBreakConfig(@NonNull LineBreakConfig lineBreakConfig) {
                this.mLineBreakConfig = lineBreakConfig;
                return this;
            }

            @NonNull
            public PrecomputedText.Params build() {
                return new PrecomputedText.Params(this.mPaint, this.mLineBreakConfig, this.mTextDir);
            }
        }

        @Retention(RetentionPolicy.SOURCE)
        @Internal
        public @interface CheckResultUsableResult {
        }
    }
}