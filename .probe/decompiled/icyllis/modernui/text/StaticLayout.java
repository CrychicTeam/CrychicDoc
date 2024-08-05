package icyllis.modernui.text;

import icyllis.modernui.ModernUI;
import icyllis.modernui.annotation.IntRange;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.LayoutCache;
import icyllis.modernui.graphics.text.LineBreakConfig;
import icyllis.modernui.graphics.text.LineBreaker;
import icyllis.modernui.text.style.LeadingMarginSpan;
import icyllis.modernui.text.style.LineHeightSpan;
import icyllis.modernui.text.style.TabStopSpan;
import icyllis.modernui.text.style.TrailingMarginSpan;
import icyllis.modernui.util.GrowingArrayUtils;
import icyllis.modernui.util.Pools;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class StaticLayout extends Layout {

    public static final Marker MARKER = MarkerManager.getMarker("StaticLayout");

    private static final Pools.Pool<StaticLayout.Builder> sPool = Pools.newSynchronizedPool(2);

    private static final int COLUMNS_NORMAL = 3;

    private static final int COLUMNS_ELLIPSIZE = 5;

    private static final int START = 0;

    private static final int DIR = 0;

    private static final int TAB = 0;

    private static final int TOP = 1;

    private static final int DESCENT = 2;

    private static final int ELLIPSIS_START = 3;

    private static final int ELLIPSIS_COUNT = 4;

    private static final int START_MASK = 536870911;

    private static final int DIR_SHIFT = 30;

    private static final int TAB_MASK = 536870912;

    private static final int DEFAULT_MAX_LINE_HEIGHT = -1;

    private int mLineCount;

    private int mTopPadding;

    private int mBottomPadding;

    private final int mColumns;

    private int mEllipsizedWidth;

    private boolean mEllipsized;

    private int mMaxLineHeight = -1;

    private int[] mLines;

    private Directions[] mLineDirections;

    private int mMaximumVisibleLineCount = Integer.MAX_VALUE;

    @Nullable
    private int[] mLeftIndents;

    @Nullable
    private int[] mRightIndents;

    @NonNull
    public static StaticLayout.Builder builder(@NonNull CharSequence source, @IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull TextPaint paint, @IntRange(from = 0L) int width) {
        StaticLayout.Builder b = sPool.acquire();
        if (b == null) {
            b = new StaticLayout.Builder();
        }
        b.mText = source;
        b.mStart = start;
        b.mEnd = end;
        b.mPaint = paint;
        b.mWidth = width;
        b.mAlignment = Layout.Alignment.ALIGN_NORMAL;
        b.mTextDir = TextDirectionHeuristics.FIRSTSTRONG_LTR;
        b.mIncludePad = true;
        b.mFallbackLineSpacing = true;
        b.mEllipsizedWidth = width;
        b.mEllipsize = null;
        b.mMaxLines = Integer.MAX_VALUE;
        b.mLineBreakConfig = LineBreakConfig.NONE;
        return b;
    }

    StaticLayout(@Nullable CharSequence text) {
        super(text, null, 0, null);
        this.mColumns = 5;
        this.mLineDirections = new Directions[2];
        this.mLines = new int[2 * this.mColumns];
    }

    private StaticLayout(@NonNull StaticLayout.Builder b) {
        super((CharSequence) (b.mEllipsize == null ? b.mText : (b.mText instanceof Spanned ? new Layout.SpannedEllipsizer(b.mText) : new Layout.Ellipsizer(b.mText))), b.mPaint, b.mWidth, b.mAlignment, b.mTextDir);
        if (b.mEllipsize != null) {
            Layout.Ellipsizer e = (Layout.Ellipsizer) this.getText();
            e.mLayout = this;
            e.mWidth = b.mEllipsizedWidth;
            e.mMethod = b.mEllipsize;
            this.mEllipsizedWidth = b.mEllipsizedWidth;
            this.mColumns = 5;
        } else {
            this.mColumns = 3;
            this.mEllipsizedWidth = b.mWidth;
        }
        this.mLineDirections = new Directions[2];
        this.mLines = new int[2 * this.mColumns];
        this.mMaximumVisibleLineCount = b.mMaxLines;
        this.mLeftIndents = b.mLeftIndents;
        this.mRightIndents = b.mRightIndents;
        this.generate(b, b.mIncludePad, b.mIncludePad);
    }

    void generate(@NonNull StaticLayout.Builder b, boolean includePad, boolean trackPad) {
        CharSequence source = b.mText;
        int bufStart = b.mStart;
        int bufEnd = b.mEnd;
        TextPaint paint = b.mPaint;
        int outerWidth = b.mWidth;
        TextDirectionHeuristic textDir = b.mTextDir;
        boolean fallbackLineSpacing = b.mFallbackLineSpacing;
        float ellipsizedWidth = (float) b.mEllipsizedWidth;
        TextUtils.TruncateAt ellipsize = b.mEllipsize;
        int lineBreakCapacity = 0;
        int[] breaks = null;
        float[] lineWidths = null;
        float[] ascents = null;
        float[] descents = null;
        boolean[] hasTabs = null;
        this.mLineCount = 0;
        this.mEllipsized = false;
        this.mMaxLineHeight = this.mMaximumVisibleLineCount < 1 ? 0 : -1;
        int v = 0;
        FontMetricsInt fm = b.mFontMetricsInt;
        int[] chooseHtv = null;
        int[] indents;
        if (this.mLeftIndents == null && this.mRightIndents == null) {
            indents = null;
        } else {
            int leftLen = this.mLeftIndents == null ? 0 : this.mLeftIndents.length;
            int rightLen = this.mRightIndents == null ? 0 : this.mRightIndents.length;
            int indentsLen = Math.max(leftLen, rightLen);
            indents = new int[indentsLen];
            if (leftLen > 0) {
                System.arraycopy(this.mLeftIndents, 0, indents, 0, leftLen);
            }
            for (int i = 0; i < rightLen; i++) {
                indents[i] += this.mRightIndents[i];
            }
        }
        LineBreaker.ParagraphConstraints constraints = new LineBreaker.ParagraphConstraints();
        PrecomputedText.ParagraphInfo[] paragraphInfo = null;
        Spanned spanned = source instanceof Spanned ? (Spanned) source : null;
        if (source instanceof PrecomputedText precomputed) {
            int checkResult = precomputed.checkResultUsable(bufStart, bufEnd, textDir, paint, b.mLineBreakConfig);
            switch(checkResult) {
                case 0:
                default:
                    break;
                case 1:
                    PrecomputedText.Params newParams = new PrecomputedText.Params.Builder(paint).setTextDirection(textDir).setLineBreakConfig(b.mLineBreakConfig).build();
                    PrecomputedText var56 = PrecomputedText.create(precomputed, newParams);
                    paragraphInfo = var56.getParagraphInfo();
                    break;
                case 2:
                    paragraphInfo = precomputed.getParagraphInfo();
            }
        }
        if (paragraphInfo == null) {
            PrecomputedText.Params param = new PrecomputedText.Params(paint, b.mLineBreakConfig, textDir);
            paragraphInfo = PrecomputedText.createMeasuredParagraphs(source, param, bufStart, bufEnd, false);
        }
        for (int paraIndex = 0; paraIndex < paragraphInfo.length; paraIndex++) {
            int paraStart = paraIndex == 0 ? bufStart : paragraphInfo[paraIndex - 1].paragraphEnd;
            int paraEnd = paragraphInfo[paraIndex].paragraphEnd;
            int firstWidthLineCount = 1;
            int firstWidth = outerWidth;
            int restWidth = outerWidth;
            List<LineHeightSpan> chooseHt = Collections.emptyList();
            if (spanned != null) {
                for (LeadingMarginSpan lms : getParagraphSpans(spanned, paraStart, paraEnd, LeadingMarginSpan.class)) {
                    firstWidth -= lms.getLeadingMargin(true);
                    restWidth -= lms.getLeadingMargin(false);
                    if (lms instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                        firstWidthLineCount = Math.max(firstWidthLineCount, ((LeadingMarginSpan.LeadingMarginSpan2) lms).getLeadingMarginLineCount());
                    }
                }
                for (TrailingMarginSpan tms : getParagraphSpans(spanned, paraStart, paraEnd, TrailingMarginSpan.class)) {
                    int margin = tms.getTrailingMargin();
                    firstWidth -= margin;
                    restWidth -= margin;
                }
                chooseHt = getParagraphSpans(spanned, paraStart, paraEnd, LineHeightSpan.class);
                if (!chooseHt.isEmpty()) {
                    if (chooseHtv == null || chooseHtv.length < chooseHt.size()) {
                        chooseHtv = new int[chooseHt.size()];
                    }
                    for (int i = 0; i < chooseHt.size(); i++) {
                        int o = spanned.getSpanStart(chooseHt.get(i));
                        if (o < paraStart) {
                            chooseHtv[i] = this.getLineTop(this.getLineForOffset(o));
                        } else {
                            chooseHtv[i] = v;
                        }
                    }
                }
            }
            float[] variableTabStops = null;
            if (spanned != null) {
                List<TabStopSpan> spans = getParagraphSpans(spanned, paraStart, paraEnd, TabStopSpan.class);
                if (!spans.isEmpty()) {
                    float[] stops = new float[spans.size()];
                    for (int ix = 0; ix < spans.size(); ix++) {
                        stops[ix] = (float) ((TabStopSpan) spans.get(ix)).getTabStop();
                    }
                    Arrays.sort(stops, 0, stops.length);
                    variableTabStops = stops;
                }
            }
            MeasuredParagraph measuredPara = paragraphInfo[paraIndex].measured;
            int[] spanEndCache = measuredPara.getSpanEndCache().elements();
            int[] fmCache = measuredPara.getFontMetrics().elements();
            constraints.setWidth((float) restWidth);
            constraints.setIndent((float) firstWidth);
            constraints.setTabStops(variableTabStops, 20.0F);
            LineBreaker.Result res = LineBreaker.computeLineBreaks(measuredPara.getMeasuredText(), constraints, indents, this.mLineCount);
            int breakCount = res.getLineCount();
            if (breakCount > lineBreakCapacity) {
                lineBreakCapacity = breakCount;
                breaks = new int[breakCount];
                lineWidths = new float[breakCount];
                ascents = new float[breakCount];
                descents = new float[breakCount];
                hasTabs = new boolean[breakCount];
            }
            for (int ix = 0; ix < breakCount; ix++) {
                breaks[ix] = res.getLineBreakOffset(ix);
                lineWidths[ix] = res.getLineWidth(ix);
                ascents[ix] = res.getLineAscent(ix);
                descents[ix] = res.getLineDescent(ix);
                hasTabs[ix] = res.hasLineTab(ix);
            }
            int remainingLineCount = this.mMaximumVisibleLineCount - this.mLineCount;
            boolean ellipsisMayBeApplied = ellipsize != null && (ellipsize == TextUtils.TruncateAt.END || this.mMaximumVisibleLineCount == 1 && ellipsize != TextUtils.TruncateAt.MARQUEE);
            if (0 < remainingLineCount && remainingLineCount < breakCount && ellipsisMayBeApplied) {
                float width = 0.0F;
                boolean hasTab = false;
                for (int ix = remainingLineCount - 1; ix < breakCount; ix++) {
                    if (ix == breakCount - 1) {
                        width += lineWidths[ix];
                    } else {
                        for (int j = ix == 0 ? 0 : breaks[ix - 1]; j < breaks[ix]; j++) {
                            width += measuredPara.getAdvance(j);
                        }
                    }
                    hasTab |= hasTabs[ix];
                }
                breaks[remainingLineCount - 1] = breaks[breakCount - 1];
                lineWidths[remainingLineCount - 1] = width;
                hasTabs[remainingLineCount - 1] = hasTab;
                breakCount = remainingLineCount;
            }
            int here = paraStart;
            int fmAscent = 0;
            int fmDescent = 0;
            int cacheIndex = 0;
            int breakIndex = 0;
            int spanStart = paraStart;
            while (spanStart < paraEnd) {
                int spanEnd = spanEndCache[cacheIndex];
                fm.ascent = fmCache[cacheIndex * 2];
                fm.descent = fmCache[cacheIndex * 2 + 1];
                cacheIndex++;
                if (fm.ascent < fmAscent) {
                    fmAscent = fm.ascent;
                }
                if (fm.descent > fmDescent) {
                    fmDescent = fm.descent;
                }
                while (breakIndex < breakCount && paraStart + breaks[breakIndex] < spanStart) {
                    breakIndex++;
                }
                while (breakIndex < breakCount && paraStart + breaks[breakIndex] <= spanEnd) {
                    int endPos = paraStart + breaks[breakIndex];
                    boolean moreChars = endPos < bufEnd;
                    int ascent = fallbackLineSpacing ? Math.min(fmAscent, Math.round(ascents[breakIndex])) : fmAscent;
                    int descent = fallbackLineSpacing ? Math.max(fmDescent, Math.round(descents[breakIndex])) : fmDescent;
                    v = this.out(source, here, endPos, ascent, descent, ascent, descent, v, chooseHt, chooseHtv, fm, hasTabs[breakIndex], measuredPara, bufEnd, includePad, trackPad, paraStart, ellipsize, ellipsizedWidth, lineWidths[breakIndex], paint, moreChars);
                    if (endPos < spanEnd) {
                        fmAscent = fm.ascent;
                        fmDescent = fm.descent;
                    } else {
                        fmDescent = 0;
                        fmAscent = 0;
                    }
                    here = endPos;
                    breakIndex++;
                    if (this.mLineCount >= this.mMaximumVisibleLineCount && this.mEllipsized) {
                        return;
                    }
                }
                spanStart = spanEnd;
            }
            if (paraEnd >= bufEnd) {
                assert paraEnd == bufEnd;
                break;
            }
        }
        if ((bufEnd == bufStart || source.charAt(bufEnd - 1) == '\n') && this.mLineCount < this.mMaximumVisibleLineCount) {
            MeasuredParagraph measuredParax = MeasuredParagraph.buildForBidi(source, bufEnd, bufEnd, textDir, null);
            paint.getFontMetricsInt(fm);
            this.out(source, bufEnd, bufEnd, fm.ascent, fm.descent, fm.ascent, fm.descent, v, Collections.emptyList(), null, fm, false, measuredParax, bufEnd, includePad, trackPad, bufStart, ellipsize, ellipsizedWidth, 0.0F, paint, false);
        }
    }

    private int out(CharSequence text, int start, int end, int above, int below, int top, int bottom, int v, List<LineHeightSpan> chooseHt, int[] chooseHtv, FontMetricsInt fm, boolean hasTab, @NonNull MeasuredParagraph measured, int bufEnd, boolean includePad, boolean trackPad, int widthStart, TextUtils.TruncateAt ellipsize, float ellipsisWidth, float textWidth, TextPaint paint, boolean moreChars) {
        int j = this.mLineCount;
        int off = j * this.mColumns;
        int want = off + this.mColumns + 1;
        int dir = measured.getParagraphDir();
        if (want >= this.mLines.length) {
            this.mLines = Arrays.copyOf(this.mLines, GrowingArrayUtils.growSize(want));
        }
        if (j >= this.mLineDirections.length) {
            this.mLineDirections = (Directions[]) Arrays.copyOf(this.mLineDirections, GrowingArrayUtils.growSize(j));
        }
        if (!chooseHt.isEmpty()) {
            fm.ascent = above;
            fm.descent = below;
            for (int i = 0; i < chooseHt.size(); i++) {
                ((LineHeightSpan) chooseHt.get(i)).chooseHeight(text, start, end, chooseHtv[i], v, fm, paint);
            }
            above = fm.ascent;
            below = fm.descent;
        }
        boolean firstLine = j == 0;
        boolean currentLineIsTheLastVisibleOne = j + 1 == this.mMaximumVisibleLineCount;
        if (ellipsize != null) {
            boolean forceEllipsis = moreChars && this.mLineCount + 1 == this.mMaximumVisibleLineCount;
            boolean doEllipsis = (this.mMaximumVisibleLineCount == 1 && moreChars || firstLine && !moreChars) && ellipsize != TextUtils.TruncateAt.MARQUEE || !firstLine && (currentLineIsTheLastVisibleOne || !moreChars) && ellipsize == TextUtils.TruncateAt.END;
            if (doEllipsis) {
                this.calculateEllipsis(start, end, measured, widthStart, ellipsisWidth, ellipsize, j, textWidth, paint, forceEllipsis);
            }
        }
        boolean lastLine;
        if (this.mEllipsized) {
            lastLine = true;
        } else {
            boolean lastCharIsNewLine = widthStart != bufEnd && bufEnd > 0 && text.charAt(bufEnd - 1) == '\n';
            if (end == bufEnd && !lastCharIsNewLine) {
                lastLine = true;
            } else {
                lastLine = start == bufEnd && lastCharIsNewLine;
            }
        }
        if (firstLine) {
            if (trackPad) {
                this.mTopPadding = top - above;
            }
            if (includePad) {
                above = top;
            }
        }
        if (lastLine) {
            if (trackPad) {
                this.mBottomPadding = bottom - below;
            }
            if (includePad) {
                below = bottom;
            }
        }
        int[] lines = this.mLines;
        lines[off + 0] = start;
        lines[off + 1] = v;
        lines[off + 2] = below;
        if (!this.mEllipsized && currentLineIsTheLastVisibleOne) {
            int maxLineBelow = includePad ? bottom : below;
            this.mMaxLineHeight = v + (maxLineBelow - above);
        }
        v += below - above;
        lines[off + this.mColumns + 0] = end;
        lines[off + this.mColumns + 1] = v;
        lines[off + 0] = lines[off + 0] | (hasTab ? 536870912 : 0);
        lines[off + 0] = lines[off + 0] | dir << 30;
        this.mLineDirections[j] = measured.getDirections(start - widthStart, end - widthStart);
        this.mLineCount++;
        return v;
    }

    private void calculateEllipsis(int lineStart, int lineEnd, MeasuredParagraph measured, int widthStart, float avail, TextUtils.TruncateAt where, int line, float textWidth, TextPaint paint, boolean forceEllipsis) {
        avail -= this.getTotalInsets(line);
        if (textWidth <= avail && !forceEllipsis) {
            this.mLines[this.mColumns * line + 3] = 0;
            this.mLines[this.mColumns * line + 4] = 0;
        } else {
            char[] ellipsisChars = TextUtils.getEllipsisChars(where);
            float ellipsisWidth = LayoutCache.getOrCreate(ellipsisChars, 0, ellipsisChars.length, 0, ellipsisChars.length, false, paint.getInternalPaint(), 0).getAdvance();
            int ellipsisStart = 0;
            int ellipsisCount = 0;
            int len = lineEnd - lineStart;
            if (where == TextUtils.TruncateAt.START) {
                if (this.mMaximumVisibleLineCount == 1) {
                    float sum = 0.0F;
                    int i;
                    for (i = len; i > 0; i--) {
                        float w = measured.getAdvance(i - 1 + lineStart - widthStart);
                        if (w + sum + ellipsisWidth > avail) {
                            while (i < len && measured.getAdvance(i + lineStart - widthStart) == 0.0F) {
                                i++;
                            }
                            break;
                        }
                        sum += w;
                    }
                    ellipsisStart = 0;
                    ellipsisCount = i;
                } else {
                    ModernUI.LOGGER.warn(MARKER, "Start Ellipsis only supported with one line");
                }
            } else if (where == TextUtils.TruncateAt.END || where == TextUtils.TruncateAt.MARQUEE) {
                float sum = 0.0F;
                int i;
                for (i = 0; i < len; i++) {
                    float w = measured.getAdvance(i + lineStart - widthStart);
                    if (w + sum + ellipsisWidth > avail) {
                        break;
                    }
                    sum += w;
                }
                ellipsisStart = i;
                ellipsisCount = len - i;
                if (forceEllipsis && ellipsisCount == 0 && len > 0) {
                    ellipsisStart = len - 1;
                    ellipsisCount = 1;
                }
            } else if (this.mMaximumVisibleLineCount == 1) {
                float lsum = 0.0F;
                float rsum = 0.0F;
                float ravail = (avail - ellipsisWidth) / 2.0F;
                int right;
                for (right = len; right > 0; right--) {
                    float w = measured.getAdvance(right - 1 + lineStart - widthStart);
                    if (w + rsum > ravail) {
                        while (right < len && measured.getAdvance(right + lineStart - widthStart) == 0.0F) {
                            right++;
                        }
                        break;
                    }
                    rsum += w;
                }
                float lavail = avail - ellipsisWidth - rsum;
                int left;
                for (left = 0; left < right; left++) {
                    float w = measured.getAdvance(left + lineStart - widthStart);
                    if (w + lsum > lavail) {
                        break;
                    }
                    lsum += w;
                }
                ellipsisStart = left;
                ellipsisCount = right - left;
            } else {
                ModernUI.LOGGER.warn(MARKER, "Middle Ellipsis only supported with one line");
            }
            this.mEllipsized = true;
            this.mLines[this.mColumns * line + 3] = ellipsisStart;
            this.mLines[this.mColumns * line + 4] = ellipsisCount;
        }
    }

    private float getTotalInsets(int line) {
        int totalIndent = 0;
        if (this.mLeftIndents != null) {
            totalIndent = this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
        }
        if (this.mRightIndents != null) {
            totalIndent += this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
        }
        return (float) totalIndent;
    }

    @Override
    public int getLineForVertical(int vertical) {
        int high = this.mLineCount;
        int low = -1;
        int[] lines = this.mLines;
        while (high - low > 1) {
            int guess = high + low >> 1;
            if (lines[this.mColumns * guess + 1] > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        return Math.max(low, 0);
    }

    @Override
    public int getLineCount() {
        return this.mLineCount;
    }

    @Override
    public int getLineTop(int line) {
        return this.mLines[this.mColumns * line + 1];
    }

    @Override
    public int getLineDescent(int line) {
        return this.mLines[this.mColumns * line + 2];
    }

    @Override
    public int getLineStart(int line) {
        return this.mLines[this.mColumns * line + 0] & 536870911;
    }

    @Override
    public int getParagraphDirection(int line) {
        return this.mLines[this.mColumns * line + 0] >> 30;
    }

    @Override
    public boolean getLineContainsTab(int line) {
        return (this.mLines[this.mColumns * line + 0] & 536870912) != 0;
    }

    @Override
    public final Directions getLineDirections(int line) {
        if (line > this.getLineCount()) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            return this.mLineDirections[line];
        }
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
    public int getIndentAdjust(int line, Layout.Alignment align) {
        if (align == Layout.Alignment.ALIGN_LEFT) {
            return this.mLeftIndents == null ? 0 : this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
        } else if (align == Layout.Alignment.ALIGN_RIGHT) {
            return this.mRightIndents == null ? 0 : -this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
        } else if (align == Layout.Alignment.ALIGN_CENTER) {
            int left = 0;
            if (this.mLeftIndents != null) {
                left = this.mLeftIndents[Math.min(line, this.mLeftIndents.length - 1)];
            }
            int right = 0;
            if (this.mRightIndents != null) {
                right = this.mRightIndents[Math.min(line, this.mRightIndents.length - 1)];
            }
            return left - right >> 1;
        } else {
            throw new AssertionError("unhandled alignment " + align);
        }
    }

    @Override
    public int getEllipsisCount(int line) {
        return this.mColumns < 5 ? 0 : this.mLines[this.mColumns * line + 4];
    }

    @Override
    public int getEllipsisStart(int line) {
        return this.mColumns < 5 ? 0 : this.mLines[this.mColumns * line + 3];
    }

    @Override
    public int getEllipsizedWidth() {
        return this.mEllipsizedWidth;
    }

    @Override
    public int getHeight(boolean cap) {
        if (cap && this.mLineCount > this.mMaximumVisibleLineCount && this.mMaxLineHeight == -1) {
            ModernUI.LOGGER.warn(MARKER, "maxLineHeight should not be -1.  maxLines: {} lineCount: {}", this.mMaximumVisibleLineCount, this.mLineCount);
        }
        return cap && this.mLineCount > this.mMaximumVisibleLineCount && this.mMaxLineHeight != -1 ? this.mMaxLineHeight : super.getHeight();
    }

    public static final class Builder {

        private final FontMetricsInt mFontMetricsInt = new FontMetricsInt();

        private CharSequence mText;

        private int mStart;

        private int mEnd;

        private TextPaint mPaint;

        private int mWidth;

        private Layout.Alignment mAlignment;

        private TextDirectionHeuristic mTextDir;

        private boolean mIncludePad;

        private boolean mFallbackLineSpacing;

        private int mEllipsizedWidth;

        @Nullable
        private TextUtils.TruncateAt mEllipsize;

        private int mMaxLines;

        @Nullable
        private int[] mLeftIndents;

        @Nullable
        private int[] mRightIndents;

        private LineBreakConfig mLineBreakConfig = LineBreakConfig.NONE;

        private Builder() {
        }

        private void recycle() {
            this.release();
            StaticLayout.sPool.release(this);
        }

        void release() {
            this.mText = null;
            this.mPaint = null;
            this.mLeftIndents = null;
            this.mRightIndents = null;
        }

        @NonNull
        StaticLayout.Builder setText(@NonNull CharSequence source, int start, int end) {
            this.mText = source;
            this.mStart = start;
            this.mEnd = end;
            return this;
        }

        @NonNull
        StaticLayout.Builder setPaint(@NonNull TextPaint paint) {
            this.mPaint = paint;
            return this;
        }

        @NonNull
        StaticLayout.Builder setWidth(int width) {
            this.mWidth = width;
            if (this.mEllipsize == null) {
                this.mEllipsizedWidth = width;
            }
            return this;
        }

        @NonNull
        public StaticLayout.Builder setAlignment(@NonNull Layout.Alignment alignment) {
            this.mAlignment = alignment;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setTextDirection(@NonNull TextDirectionHeuristic textDir) {
            this.mTextDir = textDir;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setIncludePad(boolean includePad) {
            this.mIncludePad = includePad;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setFallbackLineSpacing(boolean fallbackLineSpacing) {
            this.mFallbackLineSpacing = fallbackLineSpacing;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setEllipsizedWidth(int ellipsizedWidth) {
            this.mEllipsizedWidth = ellipsizedWidth;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setEllipsize(@Nullable TextUtils.TruncateAt ellipsize) {
            this.mEllipsize = ellipsize;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setMaxLines(int maxLines) {
            this.mMaxLines = maxLines;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setIndents(@Nullable int[] leftIndents, @Nullable int[] rightIndents) {
            this.mLeftIndents = leftIndents;
            this.mRightIndents = rightIndents;
            return this;
        }

        @NonNull
        public StaticLayout.Builder setLineBreakConfig(@NonNull LineBreakConfig lineBreakConfig) {
            this.mLineBreakConfig = lineBreakConfig;
            return this;
        }

        @NonNull
        public StaticLayout build() {
            StaticLayout result = new StaticLayout(this);
            this.recycle();
            return result;
        }
    }
}