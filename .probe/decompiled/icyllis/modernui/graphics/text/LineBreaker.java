package icyllis.modernui.graphics.text;

import com.ibm.icu.text.BreakIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.ULocale.Builder;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.text.TabStops;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.text.CharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LineBreaker {

    private static final int NOWHERE = -1;

    private static final BreakIterator sBreaker = BreakIterator.getLineInstance(Locale.ROOT);

    @Nonnull
    private final char[] mTextBuf;

    @Nonnull
    private final MeasuredText mMeasuredText;

    @Nonnull
    private final LineBreaker.LineWidth mLineWidthLimits;

    @Nonnull
    private final TabStops mTabStops;

    private int mLineNum = 0;

    private float mLineWidth = 0.0F;

    private float mCharsAdvance = 0.0F;

    private float mLineWidthLimit;

    private int mPrevBoundaryOffset = -1;

    private float mLineWidthAtPrevBoundary = 0.0F;

    private float mCharsAdvanceAtPrevBoundary = 0.0F;

    private final List<LineBreaker.BreakPoint> mBreakPoints = new ArrayList();

    public static boolean isLineEndSpace(char c) {
        return c == '\n' || c == ' ' || c == '\t' || c == 5760 || 8192 <= c && c <= 8202 && c != 8199 || c == 8287 || c == 12288;
    }

    public LineBreaker(@Nonnull char[] textBuf, @Nonnull MeasuredText measuredText, @Nonnull LineBreaker.LineWidth lineWidthLimits, @Nonnull TabStops tabStops) {
        this.mTextBuf = textBuf;
        this.mMeasuredText = measuredText;
        this.mLineWidthLimits = lineWidthLimits;
        this.mTabStops = tabStops;
        this.mLineWidthLimit = lineWidthLimits.getAt(0);
    }

    @Nonnull
    public static LineBreaker.Result computeLineBreaks(@Nullable MeasuredText measuredText, @Nonnull LineBreaker.ParagraphConstraints constraints, @Nullable int[] indents, int lineNumber) {
        if (measuredText != null && measuredText.getTextBuf().length != 0) {
            LineBreaker.DefaultLineWidth lineWidth = new LineBreaker.DefaultLineWidth(constraints.mFirstWidth, constraints.mWidth, indents, lineNumber);
            TabStops tabStops = new TabStops(constraints.mVariableTabStops, constraints.mDefaultTabStop);
            LineBreaker breaker = new LineBreaker(measuredText.getTextBuf(), measuredText, lineWidth, tabStops);
            breaker.process();
            return breaker.getResult();
        } else {
            return new LineBreaker.Result();
        }
    }

    @NonNull
    public static ULocale getLocaleWithLineBreakOption(@NonNull Locale locale, int lbStyle, int lbWordStyle) {
        if (lbStyle == 0 && lbWordStyle == 0) {
            return ULocale.forLocale(locale);
        } else {
            Builder var10000 = new Builder().setLocale(ULocale.forLocale(locale));
            String var10002 = switch(lbStyle) {
                case 1 ->
                    "loose";
                case 2 ->
                    "normal";
                case 3 ->
                    "strict";
                default ->
                    null;
            };
            return var10000.setUnicodeLocaleKeyword("lb", var10002).setUnicodeLocaleKeyword("lw", switch(lbWordStyle) {
                case 1 ->
                    "phrase";
                case 2 ->
                    "normal";
                case 3 ->
                    "breakall";
                case 4 ->
                    "keepall";
                default ->
                    null;
            }).build();
        }
    }

    private void process() {
        BreakIterator breaker = sBreaker;
        CharacterIterator iterator = new CharArrayIterator(this.mTextBuf);
        Locale locale = null;
        int lbStyle = 0;
        int lbWordStyle = 0;
        int nextLineBoundary = 0;
        for (MeasuredText.Run run : this.mMeasuredText.getRuns()) {
            Locale newLocale = run.getLocale();
            if (locale != newLocale || lbStyle != run.getLineBreakStyle() || lbWordStyle != run.getLineBreakWordStyle()) {
                locale = newLocale;
                lbStyle = run.getLineBreakStyle();
                lbWordStyle = run.getLineBreakWordStyle();
                breaker = BreakIterator.getLineInstance(getLocaleWithLineBreakOption(newLocale, lbStyle, lbWordStyle));
                breaker.setText(iterator);
                nextLineBoundary = breaker.following(run.mStart);
            }
            for (int i = run.mStart; i < run.mEnd; i++) {
                this.updateLineWidth(this.mTextBuf[i], this.mMeasuredText.getAdvance(i));
                if (i + 1 == nextLineBoundary) {
                    if (run.canBreak() || nextLineBoundary == run.mEnd) {
                        this.processLineBreak(i + 1);
                    }
                    nextLineBoundary = breaker.next();
                    if (nextLineBoundary == -1) {
                        nextLineBoundary = this.mTextBuf.length;
                    }
                }
            }
        }
        if (this.getPrevLineBreakOffset() != this.mTextBuf.length && this.mPrevBoundaryOffset != -1) {
            this.breakLineAt(this.mPrevBoundaryOffset, this.mLineWidth, 0.0F, 0.0F);
        }
    }

    private void processLineBreak(int offset) {
        while (this.mLineWidth > this.mLineWidthLimit) {
            int start = this.getPrevLineBreakOffset();
            if (!this.tryLineBreak() && this.doLineBreakWithGraphemeBounds(start, offset)) {
                return;
            }
        }
        if (this.mPrevBoundaryOffset != -1) {
        }
        this.mPrevBoundaryOffset = offset;
        this.mLineWidthAtPrevBoundary = this.mLineWidth;
        this.mCharsAdvanceAtPrevBoundary = this.mCharsAdvance;
    }

    private boolean tryLineBreak() {
        if (this.mPrevBoundaryOffset == -1) {
            return false;
        } else {
            this.breakLineAt(this.mPrevBoundaryOffset, this.mLineWidthAtPrevBoundary, this.mLineWidth - this.mCharsAdvanceAtPrevBoundary, this.mCharsAdvance - this.mCharsAdvanceAtPrevBoundary);
            return true;
        }
    }

    private boolean doLineBreakWithGraphemeBounds(int start, int end) {
        float width = this.mMeasuredText.getAdvance(start);
        for (int i = start + 1; i < end; i++) {
            float w = this.mMeasuredText.getAdvance(i);
            if (w != 0.0F) {
                if (width + w > this.mLineWidthLimit) {
                    this.breakLineAt(i, width, this.mLineWidth - width, this.mCharsAdvance - width);
                    return false;
                }
                width += w;
            }
        }
        this.breakLineAt(end, this.mLineWidth, 0.0F, 0.0F);
        return true;
    }

    private void breakLineAt(int offset, float lineWidth, float remainingNextLineWidth, float remainingNextCharsAdvance) {
        this.mBreakPoints.add(new LineBreaker.BreakPoint(offset, lineWidth));
        this.mLineWidthLimit = this.mLineWidthLimits.getAt(++this.mLineNum);
        this.mLineWidth = remainingNextLineWidth;
        this.mCharsAdvance = remainingNextCharsAdvance;
        this.mPrevBoundaryOffset = -1;
        this.mLineWidthAtPrevBoundary = 0.0F;
        this.mCharsAdvanceAtPrevBoundary = 0.0F;
    }

    private void updateLineWidth(char c, float adv) {
        if (c == '\t') {
            this.mCharsAdvance = this.mTabStops.nextTab(this.mCharsAdvance);
            this.mLineWidth = this.mCharsAdvance;
        } else {
            this.mCharsAdvance += adv;
            if (!isLineEndSpace(c)) {
                this.mLineWidth = this.mCharsAdvance;
            }
        }
    }

    private int getPrevLineBreakOffset() {
        return this.mBreakPoints.isEmpty() ? 0 : ((LineBreaker.BreakPoint) this.mBreakPoints.get(this.mBreakPoints.size() - 1)).mOffset;
    }

    @Nonnull
    private LineBreaker.Result getResult() {
        int prevBreakOffset = 0;
        int size = this.mBreakPoints.size();
        int[] ascents = new int[size];
        int[] descents = new int[size];
        FontMetricsInt fm = new FontMetricsInt();
        for (int i = 0; i < size; i++) {
            LineBreaker.BreakPoint breakPoint = (LineBreaker.BreakPoint) this.mBreakPoints.get(i);
            for (int j = prevBreakOffset; j < breakPoint.mOffset; j++) {
                if (this.mTextBuf[j] == '\t') {
                    breakPoint.mHasTabChar = true;
                    break;
                }
            }
            fm.reset();
            this.mMeasuredText.getExtent(prevBreakOffset, breakPoint.mOffset, fm);
            ascents[i] = fm.ascent;
            descents[i] = fm.descent;
            prevBreakOffset = breakPoint.mOffset;
        }
        return new LineBreaker.Result((LineBreaker.BreakPoint[]) this.mBreakPoints.toArray(new LineBreaker.BreakPoint[0]), ascents, descents);
    }

    private static final class BreakPoint {

        private final int mOffset;

        private final float mLineWidth;

        private boolean mHasTabChar = false;

        public BreakPoint(int offset, float lineWidth) {
            this.mOffset = offset;
            this.mLineWidth = lineWidth;
        }
    }

    private static class DefaultLineWidth implements LineBreaker.LineWidth {

        private final float mFirstWidth;

        private final float mRestWidth;

        @Nullable
        private final int[] mIndents;

        private final int mOffset;

        public DefaultLineWidth(float firstWidth, float restWidth, @Nullable int[] indents, int offset) {
            this.mFirstWidth = firstWidth;
            this.mRestWidth = restWidth;
            this.mIndents = indents;
            this.mOffset = offset;
        }

        @Override
        public float getAt(int line) {
            float width = line < 1 ? this.mFirstWidth : this.mRestWidth;
            return Math.max(0.0F, width - this.getIndent(this.mIndents, line));
        }

        @Override
        public float getMin() {
            float minWidth = Math.min(this.getAt(0), this.getAt(1));
            if (this.mIndents != null) {
                int end = this.mIndents.length - this.mOffset;
                for (int line = 1; line < end; line++) {
                    minWidth = Math.min(minWidth, this.getAt(line));
                }
            }
            return minWidth;
        }

        private float getIndent(@Nullable int[] indents, int line) {
            if (indents != null && indents.length != 0) {
                int index = line + this.mOffset;
                return index < indents.length ? (float) indents[index] : (float) indents[indents.length - 1];
            } else {
                return 0.0F;
            }
        }
    }

    public interface LineWidth {

        default float getAt(int line) {
            return 0.0F;
        }

        default float getMin() {
            return 0.0F;
        }
    }

    public static class ParagraphConstraints {

        private float mWidth = 0.0F;

        private float mFirstWidth = 0.0F;

        @Nullable
        private float[] mVariableTabStops = null;

        private float mDefaultTabStop = 0.0F;

        public void setWidth(float width) {
            this.mWidth = width;
        }

        public void setIndent(float firstWidth) {
            this.mFirstWidth = firstWidth;
        }

        public void setTabStops(@Nullable float[] tabStops, float defaultTabStop) {
            this.mVariableTabStops = tabStops;
            this.mDefaultTabStop = defaultTabStop;
        }

        public float getWidth() {
            return this.mWidth;
        }

        public float getFirstWidth() {
            return this.mFirstWidth;
        }

        @Nullable
        public float[] getTabStops() {
            return this.mVariableTabStops;
        }

        public float getDefaultTabStop() {
            return this.mDefaultTabStop;
        }
    }

    public static class Result {

        private static final LineBreaker.BreakPoint[] EMPTY_ARRAY = new LineBreaker.BreakPoint[0];

        @Nonnull
        private final LineBreaker.BreakPoint[] mBreakPoints;

        private final int[] mAscents;

        private final int[] mDescents;

        private Result() {
            this.mBreakPoints = EMPTY_ARRAY;
            this.mAscents = IntArrays.EMPTY_ARRAY;
            this.mDescents = IntArrays.EMPTY_ARRAY;
        }

        private Result(@Nonnull LineBreaker.BreakPoint[] breakPoints, int[] ascents, int[] descents) {
            this.mBreakPoints = breakPoints;
            this.mAscents = ascents;
            this.mDescents = descents;
        }

        public int getLineCount() {
            return this.mBreakPoints.length;
        }

        public int getLineBreakOffset(int lineIndex) {
            return this.mBreakPoints[lineIndex].mOffset;
        }

        public float getLineWidth(int lineIndex) {
            return this.mBreakPoints[lineIndex].mLineWidth;
        }

        public float getLineAscent(int lineIndex) {
            return (float) this.mAscents[lineIndex];
        }

        public float getLineDescent(int lineIndex) {
            return (float) this.mDescents[lineIndex];
        }

        public boolean hasLineTab(int lineIndex) {
            return this.mBreakPoints[lineIndex].mHasTabChar;
        }
    }
}