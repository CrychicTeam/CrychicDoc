package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.text.LineBreaker;
import icyllis.modernui.text.method.TextKeyListener;
import icyllis.modernui.text.style.AlignmentSpan;
import icyllis.modernui.text.style.LeadingMarginSpan;
import icyllis.modernui.text.style.LineBackgroundSpan;
import icyllis.modernui.text.style.ParagraphStyle;
import icyllis.modernui.text.style.ReplacementSpan;
import icyllis.modernui.text.style.TabStopSpan;
import icyllis.modernui.text.style.TrailingMarginSpan;
import icyllis.modernui.util.GrowingArrayUtils;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Layout {

    public static final int DIR_LEFT_TO_RIGHT = 1;

    public static final int DIR_RIGHT_TO_LEFT = -1;

    public static final float TAB_INCREMENT = 20.0F;

    private CharSequence mText;

    private TextPaint mPaint;

    private int mWidth;

    private Layout.Alignment mAlignment;

    private boolean mSpannedText;

    private final TextDirectionHeuristic mTextDir;

    private SpanSet<LineBackgroundSpan> mLineBackgroundSpans;

    private static final LineBackgroundSpan[] EMPTY_BACKGROUND_SPANS = new LineBackgroundSpan[0];

    protected Layout(CharSequence text, TextPaint paint, int width, Layout.Alignment align) {
        this(text, paint, width, align, TextDirectionHeuristics.FIRSTSTRONG_LTR);
    }

    protected Layout(CharSequence text, TextPaint paint, int width, Layout.Alignment align, TextDirectionHeuristic textDir) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        } else {
            if (paint != null) {
                paint.bgColor = 0;
                paint.baselineShift = 0;
            }
            this.mText = text;
            this.mPaint = paint;
            this.mWidth = width;
            this.mAlignment = align;
            this.mSpannedText = text instanceof Spanned;
            this.mTextDir = textDir;
        }
    }

    void replaceWith(CharSequence text, TextPaint paint, int width, Layout.Alignment align) {
        if (width < 0) {
            throw new IllegalArgumentException("Layout: " + width + " < 0");
        } else {
            this.mText = text;
            this.mPaint = paint;
            this.mWidth = width;
            this.mAlignment = align;
            this.mSpannedText = text instanceof Spanned;
        }
    }

    public void draw(@NonNull Canvas canvas) {
        long range = this.getLineRangeForDraw(canvas);
        if (range >= 0L) {
            int firstLine = (int) (range >>> 32);
            int lastLine = (int) (range & 4294967295L);
            this.drawBackground(canvas, firstLine, lastLine);
            this.drawText(canvas, firstLine, lastLine);
        }
    }

    public final void drawBackground(@NonNull Canvas canvas, int firstLine, int lastLine) {
        if (this.mSpannedText) {
            assert firstLine >= 0 && lastLine >= firstLine;
            if (this.mLineBackgroundSpans == null) {
                this.mLineBackgroundSpans = new SpanSet<>(LineBackgroundSpan.class);
            }
            Spanned buffer = (Spanned) this.mText;
            int textLength = buffer.length();
            this.mLineBackgroundSpans.init(buffer, 0, textLength);
            if (!this.mLineBackgroundSpans.isEmpty()) {
                int previousLineBottom = this.getLineTop(firstLine);
                int previousLineEnd = this.getLineStart(firstLine);
                LineBackgroundSpan[] spans = EMPTY_BACKGROUND_SPANS;
                int spansLength = 0;
                TextPaint paint = this.mPaint;
                int spanEnd = 0;
                int width = this.mWidth;
                for (int i = firstLine; i <= lastLine; i++) {
                    int start = previousLineEnd;
                    int end = this.getLineStart(i + 1);
                    previousLineEnd = end;
                    int ltop = previousLineBottom;
                    int lbottom = this.getLineTop(i + 1);
                    previousLineBottom = lbottom;
                    int lbaseline = lbottom - this.getLineDescent(i);
                    if (end >= spanEnd) {
                        spanEnd = this.mLineBackgroundSpans.getNextTransition(start, textLength);
                        spansLength = 0;
                        if (start != end || start == 0) {
                            for (int j = 0; j < this.mLineBackgroundSpans.size(); j++) {
                                if (this.mLineBackgroundSpans.mSpanStarts[j] < end && this.mLineBackgroundSpans.mSpanEnds[j] > start) {
                                    spans = GrowingArrayUtils.append(spans, spansLength, (LineBackgroundSpan) this.mLineBackgroundSpans.get(j));
                                    spansLength++;
                                }
                            }
                        }
                    }
                    for (int n = 0; n < spansLength; n++) {
                        LineBackgroundSpan lineBackgroundSpan = spans[n];
                        lineBackgroundSpan.drawBackground(canvas, paint, 0, width, ltop, lbaseline, lbottom, buffer, start, end, i);
                    }
                }
            }
            this.mLineBackgroundSpans.recycle();
        }
    }

    public void drawText(@NonNull Canvas canvas, int firstLine, int lastLine) {
        assert firstLine >= 0 && lastLine >= firstLine && lastLine < this.getLineCount();
        int previousLineBottom = this.getLineTop(firstLine);
        int previousLineEnd = this.getLineStart(firstLine);
        List<ParagraphStyle> spans = Collections.emptyList();
        int spanEnd = 0;
        TextPaint paint = TextPaint.obtain();
        paint.set(this.mPaint);
        CharSequence buf = this.mText;
        Layout.Alignment paraAlign = this.mAlignment;
        TabStops tabStops = null;
        boolean tabStopsIsInitialized = false;
        TextLine tl = TextLine.obtain();
        for (int lineNum = firstLine; lineNum <= lastLine; lineNum++) {
            int start = previousLineEnd;
            previousLineEnd = this.getLineStart(lineNum + 1);
            int end = this.getLineVisibleEnd(lineNum, start, previousLineEnd);
            int ltop = previousLineBottom;
            int lbottom = this.getLineTop(lineNum + 1);
            previousLineBottom = lbottom;
            int lbaseline = lbottom - this.getLineDescent(lineNum);
            int dir = this.getParagraphDirection(lineNum);
            int left = 0;
            int right = this.mWidth;
            if (this.mSpannedText) {
                Spanned sp = (Spanned) buf;
                int textLength = buf.length();
                boolean isFirstParaLine = start == 0 || buf.charAt(start - 1) == '\n';
                if (start >= spanEnd && (lineNum == firstLine || isFirstParaLine)) {
                    spanEnd = sp.nextSpanTransition(start, textLength, ParagraphStyle.class);
                    spans = getParagraphSpans(sp, start, spanEnd, ParagraphStyle.class);
                    paraAlign = this.mAlignment;
                    for (int n = spans.size() - 1; n >= 0; n--) {
                        if (spans.get(n) instanceof AlignmentSpan alignment) {
                            paraAlign = alignment.getAlignment();
                            break;
                        }
                    }
                    tabStopsIsInitialized = false;
                }
                boolean useFirstLineMargin = isFirstParaLine;
                for (ParagraphStyle span : spans) {
                    if (span instanceof LeadingMarginSpan.LeadingMarginSpan2 margin) {
                        int count = margin.getLeadingMarginLineCount();
                        int startLine = this.getLineForOffset(sp.getSpanStart(margin));
                        if (lineNum < startLine + count) {
                            useFirstLineMargin = true;
                            break;
                        }
                    }
                }
                for (ParagraphStyle spanx : spans) {
                    if (spanx instanceof LeadingMarginSpan lms) {
                        lms.drawMargin(canvas, paint, left, right, dir, ltop, lbaseline, lbottom, buf, start, end, isFirstParaLine, this);
                        if (dir == -1) {
                            right -= lms.getLeadingMargin(useFirstLineMargin);
                        } else {
                            left += lms.getLeadingMargin(useFirstLineMargin);
                        }
                    }
                    if (spanx instanceof TrailingMarginSpan tms) {
                        if (dir == 1) {
                            right -= tms.getTrailingMargin();
                        } else {
                            left += tms.getTrailingMargin();
                        }
                    }
                }
            }
            boolean hasTab = this.getLineContainsTab(lineNum);
            if (hasTab && !tabStopsIsInitialized) {
                if (tabStops == null) {
                    tabStops = new TabStops(20.0F, spans);
                } else {
                    tabStops.reset(20.0F, spans);
                }
                tabStopsIsInitialized = true;
            }
            Layout.Alignment align = paraAlign;
            if (paraAlign == Layout.Alignment.ALIGN_LEFT) {
                align = dir == 1 ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
            } else if (paraAlign == Layout.Alignment.ALIGN_RIGHT) {
                align = dir == 1 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
            }
            Directions directions = this.getLineDirections(lineNum);
            int ellipsisStart = this.getEllipsisStart(lineNum);
            tl.set(paint, buf, start, end, dir, directions, hasTab, tabStops, ellipsisStart, ellipsisStart + this.getEllipsisCount(lineNum));
            int x;
            if (align == Layout.Alignment.ALIGN_NORMAL) {
                if (dir == 1) {
                    int indentWidth = this.getIndentAdjust(lineNum, Layout.Alignment.ALIGN_LEFT);
                    x = left + indentWidth;
                } else {
                    int indentWidth = -this.getIndentAdjust(lineNum, Layout.Alignment.ALIGN_RIGHT);
                    x = right - indentWidth;
                }
            } else {
                int max = (int) tl.metrics(null);
                if (align == Layout.Alignment.ALIGN_OPPOSITE) {
                    if (dir == 1) {
                        int indentWidth = -this.getIndentAdjust(lineNum, Layout.Alignment.ALIGN_RIGHT);
                        x = right - max - indentWidth;
                    } else {
                        int indentWidth = this.getIndentAdjust(lineNum, Layout.Alignment.ALIGN_LEFT);
                        x = left - max + indentWidth;
                    }
                } else {
                    int indentWidth = this.getIndentAdjust(lineNum, Layout.Alignment.ALIGN_CENTER);
                    max &= -2;
                    x = (right + left - max >> 1) + indentWidth;
                }
            }
            if (directions == Directions.ALL_LEFT_TO_RIGHT && !this.mSpannedText && !hasTab) {
                TextUtils.drawTextRun(canvas, buf, start, end, start, end, (float) x, (float) lbaseline, false, paint);
            } else {
                tl.draw(canvas, (float) x, ltop, lbaseline, lbottom);
            }
        }
        paint.recycle();
        tl.recycle();
    }

    public final long getLineRangeForDraw(@NonNull Canvas canvas) {
        int lineCount = this.getLineCount();
        if (lineCount <= 0) {
            return -1L;
        } else {
            int bottom = this.getLineTop(lineCount);
            if (canvas.quickReject(0.0F, 0.0F, (float) this.mWidth, (float) bottom)) {
                return -1L;
            } else {
                int lineNum = 0;
                int lineTop = 0;
                int firstLine = -1;
                int lastLine = -1;
                do {
                    int lineBottom = this.getLineTop(lineNum + 1);
                    if (firstLine == -1) {
                        if (!canvas.quickReject(0.0F, (float) lineTop, (float) this.mWidth, (float) lineBottom)) {
                            firstLine = lineNum;
                        }
                    } else if (canvas.quickReject(0.0F, (float) lineTop, (float) this.mWidth, (float) lineBottom)) {
                        lastLine = lineNum - 1;
                        break;
                    }
                    lineTop = lineBottom;
                } while (++lineNum < lineCount);
                if (firstLine == -1) {
                    return -1L;
                } else {
                    if (lastLine == -1) {
                        assert lineNum == lineCount;
                        lastLine = lineCount - 1;
                    }
                    assert lastLine >= firstLine;
                    return (long) firstLine << 32 | (long) lastLine;
                }
            }
        }
    }

    public int getLineForVertical(int vertical) {
        int high = this.getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = high + low >> 1;
            if (this.getLineTop(guess) > vertical) {
                high = guess;
            } else {
                low = guess;
            }
        }
        return Math.max(low, 0);
    }

    public int getLineForOffset(int offset) {
        int high = this.getLineCount();
        int low = -1;
        while (high - low > 1) {
            int guess = (high + low) / 2;
            if (this.getLineStart(guess) > offset) {
                high = guess;
            } else {
                low = guess;
            }
        }
        return Math.max(low, 0);
    }

    public final CharSequence getText() {
        return this.mText;
    }

    public final TextPaint getPaint() {
        return this.mPaint;
    }

    public final int getWidth() {
        return this.mWidth;
    }

    public int getEllipsizedWidth() {
        return this.mWidth;
    }

    public final void increaseWidthTo(int wid) {
        if (wid < this.mWidth) {
            throw new RuntimeException("attempted to reduce Layout width");
        } else {
            this.mWidth = wid;
        }
    }

    public int getHeight() {
        return this.getLineTop(this.getLineCount());
    }

    public int getHeight(boolean cap) {
        return this.getHeight();
    }

    public final Layout.Alignment getAlignment() {
        return this.mAlignment;
    }

    public final TextDirectionHeuristic getTextDirectionHeuristic() {
        return this.mTextDir;
    }

    public abstract int getLineCount();

    public int getLineBounds(int line, @Nullable Rect bounds) {
        if (bounds != null) {
            bounds.left = 0;
            bounds.top = this.getLineTop(line);
            bounds.right = this.mWidth;
            bounds.bottom = this.getLineTop(line + 1);
        }
        return this.getLineBaseline(line);
    }

    public abstract int getLineTop(int var1);

    public abstract int getLineDescent(int var1);

    public abstract int getLineStart(int var1);

    public abstract int getParagraphDirection(int var1);

    public abstract boolean getLineContainsTab(int var1);

    public abstract Directions getLineDirections(int var1);

    public abstract int getTopPadding();

    public abstract int getBottomPadding();

    public int getIndentAdjust(int line, Layout.Alignment alignment) {
        return 0;
    }

    public abstract int getEllipsisStart(int var1);

    public abstract int getEllipsisCount(int var1);

    public float getLineMax(int line) {
        float margin = (float) (this.getParagraphLeadingMargin(line) + this.getParagraphTrailingMargin(line));
        float signedExtent = this.getLineExtent(line, false);
        return margin + (signedExtent >= 0.0F ? signedExtent : -signedExtent);
    }

    public float getLineWidth(int line) {
        float margin = (float) (this.getParagraphLeadingMargin(line) + this.getParagraphTrailingMargin(line));
        float signedExtent = this.getLineExtent(line, true);
        return margin + (signedExtent >= 0.0F ? signedExtent : -signedExtent);
    }

    private float getLineExtent(int line, boolean full) {
        int start = this.getLineStart(line);
        int end = full ? this.getLineEnd(line) : this.getLineVisibleEnd(line);
        boolean hasTabs = this.getLineContainsTab(line);
        TabStops tabStops = null;
        if (hasTabs && this.mText instanceof Spanned) {
            List<TabStopSpan> tabs = getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (!tabs.isEmpty()) {
                tabStops = new TabStops(20.0F, tabs);
            }
        }
        Directions directions = this.getLineDirections(line);
        if (directions == null) {
            return 0.0F;
        } else {
            int dir = this.getParagraphDirection(line);
            TextLine tl = TextLine.obtain();
            TextPaint paint = TextPaint.obtain();
            paint.set(this.mPaint);
            tl.set(paint, this.mText, start, end, dir, directions, hasTabs, tabStops, this.getEllipsisStart(line), this.getEllipsisStart(line) + this.getEllipsisCount(line));
            float width = tl.metrics(null);
            tl.recycle();
            paint.recycle();
            return width;
        }
    }

    private float getLineExtent(int line, TabStops tabStops, boolean full) {
        int start = this.getLineStart(line);
        int end = full ? this.getLineEnd(line) : this.getLineVisibleEnd(line);
        boolean hasTabs = this.getLineContainsTab(line);
        Directions directions = this.getLineDirections(line);
        int dir = this.getParagraphDirection(line);
        TextLine tl = TextLine.obtain();
        TextPaint paint = TextPaint.obtain();
        paint.set(this.mPaint);
        tl.set(paint, this.mText, start, end, dir, directions, hasTabs, tabStops, this.getEllipsisStart(line), this.getEllipsisStart(line) + this.getEllipsisCount(line));
        float width = tl.metrics(null);
        tl.recycle();
        paint.recycle();
        return width;
    }

    public final int getLineEnd(int line) {
        return this.getLineStart(line + 1);
    }

    public int getLineVisibleEnd(int line) {
        return this.getLineVisibleEnd(line, this.getLineStart(line), this.getLineStart(line + 1));
    }

    private int getLineVisibleEnd(int line, int start, int end) {
        CharSequence text = this.mText;
        if (line == this.getLineCount() - 1) {
            return end;
        } else {
            while (end > start) {
                char ch = text.charAt(end - 1);
                if (ch == '\n') {
                    return end - 1;
                }
                if (!LineBreaker.isLineEndSpace(ch)) {
                    break;
                }
                end--;
            }
            return end;
        }
    }

    public final int getLineBottom(int line) {
        return this.getLineTop(line + 1);
    }

    public final int getLineBaseline(int line) {
        return this.getLineTop(line + 1) - this.getLineDescent(line);
    }

    public final int getLineAscent(int line) {
        return this.getLineTop(line) - (this.getLineTop(line + 1) - this.getLineDescent(line));
    }

    public final Layout.Alignment getParagraphAlignment(int line) {
        return this.mAlignment;
    }

    public final int getParagraphLeft(int line) {
        int left = 0;
        if (!this.mSpannedText) {
            return left;
        } else {
            int dir = this.getParagraphDirection(line);
            return dir == -1 ? this.getParagraphTrailingMargin(line) : this.getParagraphLeadingMargin(line);
        }
    }

    public final int getParagraphRight(int line) {
        int right = this.mWidth;
        if (!this.mSpannedText) {
            return right;
        } else {
            int dir = this.getParagraphDirection(line);
            return dir == 1 ? right - this.getParagraphTrailingMargin(line) : right - this.getParagraphLeadingMargin(line);
        }
    }

    public boolean primaryIsTrailingPrevious(int offset) {
        int line = this.getLineForOffset(offset);
        int lineStart = this.getLineStart(line);
        int lineEnd = this.getLineEnd(line);
        int[] runs = this.getLineDirections(line).mDirections;
        int levelAt = -1;
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & 67108863);
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if (offset >= start && offset < limit) {
                if (offset > start) {
                    return false;
                }
                levelAt = runs[i + 1] >>> 26 & 63;
                break;
            }
        }
        if (levelAt == -1) {
            levelAt = this.getParagraphDirection(line) == 1 ? 0 : 1;
        }
        int levelBefore = -1;
        if (offset == lineStart) {
            levelBefore = this.getParagraphDirection(line) == 1 ? 0 : 1;
        } else {
            offset--;
            for (int i = 0; i < runs.length; i += 2) {
                int startx = lineStart + runs[i];
                int limitx = startx + (runs[i + 1] & 67108863);
                if (limitx > lineEnd) {
                    limitx = lineEnd;
                }
                if (offset >= startx && offset < limitx) {
                    levelBefore = runs[i + 1] >>> 26 & 63;
                    break;
                }
            }
        }
        return levelBefore < levelAt;
    }

    public boolean[] primaryIsTrailingPreviousAllLineOffsets(int line) {
        int lineStart = this.getLineStart(line);
        int lineEnd = this.getLineEnd(line);
        int[] runs = this.getLineDirections(line).mDirections;
        boolean[] trailing = new boolean[lineEnd - lineStart + 1];
        byte[] level = new byte[lineEnd - lineStart + 1];
        for (int i = 0; i < runs.length; i += 2) {
            int start = lineStart + runs[i];
            int limit = start + (runs[i + 1] & 67108863);
            if (limit > lineEnd) {
                limit = lineEnd;
            }
            if (limit != start) {
                level[limit - lineStart - 1] = (byte) (runs[i + 1] >>> 26 & 63);
            }
        }
        for (int i = 0; i < runs.length; i += 2) {
            int startx = lineStart + runs[i];
            byte currentLevel = (byte) (runs[i + 1] >>> 26 & 63);
            trailing[startx - lineStart] = currentLevel > (startx == lineStart ? (this.getParagraphDirection(line) == 1 ? 0 : 1) : level[startx - lineStart - 1]);
        }
        return trailing;
    }

    public float getPrimaryHorizontal(int offset) {
        return this.getPrimaryHorizontal(offset, false);
    }

    public float getPrimaryHorizontal(int offset, boolean clamped) {
        boolean trailing = this.primaryIsTrailingPrevious(offset);
        return this.getHorizontal(offset, trailing, clamped);
    }

    public float getSecondaryHorizontal(int offset) {
        return this.getSecondaryHorizontal(offset, false);
    }

    public float getSecondaryHorizontal(int offset, boolean clamped) {
        boolean trailing = this.primaryIsTrailingPrevious(offset);
        return this.getHorizontal(offset, !trailing, clamped);
    }

    private float getHorizontal(int offset, boolean primary) {
        return primary ? this.getPrimaryHorizontal(offset) : this.getSecondaryHorizontal(offset);
    }

    private float getHorizontal(int offset, boolean trailing, boolean clamped) {
        int line = this.getLineForOffset(offset);
        return this.getHorizontal(offset, trailing, line, clamped);
    }

    private float getHorizontal(int offset, boolean trailing, int line, boolean clamped) {
        int start = this.getLineStart(line);
        int end = this.getLineEnd(line);
        int dir = this.getParagraphDirection(line);
        boolean hasTab = this.getLineContainsTab(line);
        Directions directions = this.getLineDirections(line);
        TabStops tabStops = null;
        if (hasTab && this.mText instanceof Spanned) {
            List<TabStopSpan> tabs = getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (!tabs.isEmpty()) {
                tabStops = new TabStops(20.0F, tabs);
            }
        }
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTab, tabStops, this.getEllipsisStart(line), this.getEllipsisStart(line) + this.getEllipsisCount(line));
        float wid = tl.measure(offset - start, trailing, null);
        tl.recycle();
        if (clamped && wid > (float) this.mWidth) {
            wid = (float) this.mWidth;
        }
        int left = this.getParagraphLeft(line);
        int right = this.getParagraphRight(line);
        return (float) this.getLineStartPos(line, left, right) + wid;
    }

    private float[] getLineHorizontals(int line, boolean clamped, boolean primary) {
        int start = this.getLineStart(line);
        int end = this.getLineEnd(line);
        int dir = this.getParagraphDirection(line);
        boolean hasTab = this.getLineContainsTab(line);
        Directions directions = this.getLineDirections(line);
        TabStops tabStops = null;
        if (hasTab && this.mText instanceof Spanned) {
            List<TabStopSpan> tabs = getParagraphSpans((Spanned) this.mText, start, end, TabStopSpan.class);
            if (!tabs.isEmpty()) {
                tabStops = new TabStops(20.0F, tabs);
            }
        }
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, start, end, dir, directions, hasTab, tabStops, this.getEllipsisStart(line), this.getEllipsisStart(line) + this.getEllipsisCount(line));
        boolean[] trailings = this.primaryIsTrailingPreviousAllLineOffsets(line);
        if (!primary) {
            for (int offset = 0; offset < trailings.length; offset++) {
                trailings[offset] = !trailings[offset];
            }
        }
        float[] wid = tl.measureAllOffsets(trailings, null);
        tl.recycle();
        if (clamped) {
            for (int offset = 0; offset < wid.length; offset++) {
                if (wid[offset] > (float) this.mWidth) {
                    wid[offset] = (float) this.mWidth;
                }
            }
        }
        int left = this.getParagraphLeft(line);
        int right = this.getParagraphRight(line);
        int lineStartPos = this.getLineStartPos(line, left, right);
        float[] horizontal = new float[end - start + 1];
        for (int offsetx = 0; offsetx < horizontal.length; offsetx++) {
            horizontal[offsetx] = (float) lineStartPos + wid[offsetx];
        }
        return horizontal;
    }

    public int getOffsetForHorizontal(int line, float horiz) {
        return this.getOffsetForHorizontal(line, horiz, true);
    }

    public int getOffsetForHorizontal(int line, float horiz, boolean primary) {
        int lineEndOffset = this.getLineEnd(line);
        int lineStartOffset = this.getLineStart(line);
        Directions dirs = this.getLineDirections(line);
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, lineStartOffset, lineEndOffset, this.getParagraphDirection(line), dirs, false, null, this.getEllipsisStart(line), this.getEllipsisStart(line) + this.getEllipsisCount(line));
        Layout.HorizontalMeasurementProvider horizontal = new Layout.HorizontalMeasurementProvider(line, primary);
        int max;
        if (line == this.getLineCount() - 1) {
            max = lineEndOffset;
        } else {
            max = tl.getOffsetToLeftRightOf(lineEndOffset - lineStartOffset, !this.isRtlCharAt(lineEndOffset - 1)) + lineStartOffset;
        }
        int best = lineStartOffset;
        float bestdist = Math.abs(horizontal.get(lineStartOffset) - horiz);
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = lineStartOffset + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i + 1] & 67108863);
            boolean isRtl = (dirs.mDirections[i + 1] & 67108864) != 0;
            int swap = isRtl ? -1 : 1;
            if (there > max) {
                there = max;
            }
            int high = there - 1 + 1;
            int low = here + 1 - 1;
            while (high - low > 1) {
                int guess = (high + low) / 2;
                int adguess = this.getOffsetAtStartOf(guess);
                if (horizontal.get(adguess) * (float) swap >= horiz * (float) swap) {
                    high = guess;
                } else {
                    low = guess;
                }
            }
            if (low < here + 1) {
                low = here + 1;
            }
            if (low < there) {
                int aft = tl.getOffsetToLeftRightOf(low - lineStartOffset, isRtl) + lineStartOffset;
                low = tl.getOffsetToLeftRightOf(aft - lineStartOffset, !isRtl) + lineStartOffset;
                if (low >= here && low < there) {
                    float dist = Math.abs(horizontal.get(low) - horiz);
                    if (aft < there) {
                        float other = Math.abs(horizontal.get(aft) - horiz);
                        if (other < dist) {
                            dist = other;
                            low = aft;
                        }
                    }
                    if (dist < bestdist) {
                        bestdist = dist;
                        best = low;
                    }
                }
            }
            float distx = Math.abs(horizontal.get(here) - horiz);
            if (distx < bestdist) {
                bestdist = distx;
                best = here;
            }
        }
        float distx = Math.abs(horizontal.get(max) - horiz);
        if (distx <= bestdist) {
            best = max;
        }
        tl.recycle();
        return best;
    }

    private int getLineStartPos(int line, int left, int right) {
        Layout.Alignment align = this.getParagraphAlignment(line);
        int dir = this.getParagraphDirection(line);
        if (align == Layout.Alignment.ALIGN_LEFT) {
            align = dir == 1 ? Layout.Alignment.ALIGN_NORMAL : Layout.Alignment.ALIGN_OPPOSITE;
        } else if (align == Layout.Alignment.ALIGN_RIGHT) {
            align = dir == 1 ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        }
        int x;
        if (align == Layout.Alignment.ALIGN_NORMAL) {
            if (dir == 1) {
                x = left + this.getIndentAdjust(line, Layout.Alignment.ALIGN_LEFT);
            } else {
                x = right + this.getIndentAdjust(line, Layout.Alignment.ALIGN_RIGHT);
            }
        } else {
            TabStops tabStops = null;
            if (this.mSpannedText && this.getLineContainsTab(line)) {
                Spanned spanned = (Spanned) this.mText;
                int start = this.getLineStart(line);
                int spanEnd = spanned.nextSpanTransition(start, spanned.length(), TabStopSpan.class);
                List<TabStopSpan> tabSpans = getParagraphSpans(spanned, start, spanEnd, TabStopSpan.class);
                if (!tabSpans.isEmpty()) {
                    tabStops = new TabStops(20.0F, tabSpans);
                }
            }
            int max = (int) this.getLineExtent(line, tabStops, false);
            if (align == Layout.Alignment.ALIGN_OPPOSITE) {
                if (dir == 1) {
                    x = right - max + this.getIndentAdjust(line, Layout.Alignment.ALIGN_RIGHT);
                } else {
                    x = left - max + this.getIndentAdjust(line, Layout.Alignment.ALIGN_LEFT);
                }
            } else {
                max &= -2;
                x = left + right - max >> 1 + this.getIndentAdjust(line, Layout.Alignment.ALIGN_CENTER);
            }
        }
        return x;
    }

    public boolean isLevelBoundary(int offset) {
        int line = this.getLineForOffset(offset);
        Directions dirs = this.getLineDirections(line);
        if (dirs != Directions.ALL_LEFT_TO_RIGHT && dirs != Directions.ALL_RIGHT_TO_LEFT) {
            int[] runs = dirs.mDirections;
            int lineStart = this.getLineStart(line);
            int lineEnd = this.getLineEnd(line);
            if (offset != lineStart && offset != lineEnd) {
                offset -= lineStart;
                for (int i = 0; i < runs.length; i += 2) {
                    if (offset == runs[i]) {
                        return true;
                    }
                }
                return false;
            } else {
                int paraLevel = this.getParagraphDirection(line) == 1 ? 0 : 1;
                int runIndex = offset == lineStart ? 0 : runs.length - 2;
                return (runs[runIndex + 1] >>> 26 & 63) != paraLevel;
            }
        } else {
            return false;
        }
    }

    public boolean isRtlCharAt(int offset) {
        int line = this.getLineForOffset(offset);
        Directions dirs = this.getLineDirections(line);
        if (dirs == Directions.ALL_LEFT_TO_RIGHT) {
            return false;
        } else if (dirs == Directions.ALL_RIGHT_TO_LEFT) {
            return true;
        } else {
            int[] runs = dirs.mDirections;
            int lineStart = this.getLineStart(line);
            for (int i = 0; i < runs.length; i += 2) {
                int start = lineStart + runs[i];
                int limit = start + (runs[i + 1] & 67108863);
                if (offset >= start && offset < limit) {
                    int level = runs[i + 1] >>> 26 & 63;
                    return (level & 1) != 0;
                }
            }
            return false;
        }
    }

    private int getOffsetAtStartOf(int offset) {
        if (offset == 0) {
            return 0;
        } else {
            CharSequence text = this.mText;
            char c = text.charAt(offset);
            if (c >= '\udc00' && c <= '\udfff') {
                char c1 = text.charAt(offset - 1);
                if (c1 >= '\ud800' && c1 <= '\udbff') {
                    offset--;
                }
            }
            if (this.mSpannedText) {
                for (ReplacementSpan span : ((Spanned) text).getSpans(offset, offset, ReplacementSpan.class)) {
                    int start = ((Spanned) text).getSpanStart(span);
                    int end = ((Spanned) text).getSpanEnd(span);
                    if (start < offset && end > offset) {
                        offset = start;
                    }
                }
            }
            return offset;
        }
    }

    public int getOffsetToLeftOf(int offset) {
        return this.getOffsetToLeftRightOf(offset, true);
    }

    public int getOffsetToRightOf(int offset) {
        return this.getOffsetToLeftRightOf(offset, false);
    }

    private int getOffsetToLeftRightOf(int caret, boolean toLeft) {
        int line = this.getLineForOffset(caret);
        int lineStart = this.getLineStart(line);
        int lineEnd = this.getLineEnd(line);
        int lineDir = this.getParagraphDirection(line);
        boolean lineChanged = false;
        boolean advance = toLeft == (lineDir == -1);
        if (advance) {
            if (caret == lineEnd) {
                if (line >= this.getLineCount() - 1) {
                    return caret;
                }
                lineChanged = true;
                line++;
            }
        } else if (caret == lineStart) {
            if (line <= 0) {
                return caret;
            }
            lineChanged = true;
            line--;
        }
        if (lineChanged) {
            lineStart = this.getLineStart(line);
            lineEnd = this.getLineEnd(line);
            int newDir = this.getParagraphDirection(line);
            if (newDir != lineDir) {
                toLeft = !toLeft;
                lineDir = newDir;
            }
        }
        Directions directions = this.getLineDirections(line);
        TextLine tl = TextLine.obtain();
        tl.set(this.mPaint, this.mText, lineStart, lineEnd, lineDir, directions, false, null, this.getEllipsisStart(line), this.getEllipsisStart(line) + this.getEllipsisCount(line));
        caret = lineStart + tl.getOffsetToLeftRightOf(caret - lineStart, toLeft);
        tl.recycle();
        return caret;
    }

    private int getParagraphLeadingMargin(int line) {
        if (!this.mSpannedText) {
            return 0;
        } else {
            Spanned spanned = (Spanned) this.mText;
            int lineStart = this.getLineStart(line);
            int lineEnd = this.getLineEnd(line);
            int spanEnd = spanned.nextSpanTransition(lineStart, lineEnd, LeadingMarginSpan.class);
            List<LeadingMarginSpan> spans = getParagraphSpans(spanned, lineStart, spanEnd, LeadingMarginSpan.class);
            if (spans.isEmpty()) {
                return 0;
            } else {
                int margin = 0;
                boolean useFirstLineMargin = lineStart == 0 || spanned.charAt(lineStart - 1) == '\n';
                for (LeadingMarginSpan span : spans) {
                    if (span instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                        int spStart = spanned.getSpanStart(span);
                        int spanLine = this.getLineForOffset(spStart);
                        int count = ((LeadingMarginSpan.LeadingMarginSpan2) span).getLeadingMarginLineCount();
                        useFirstLineMargin |= line < spanLine + count;
                    }
                }
                for (LeadingMarginSpan spanx : spans) {
                    margin += spanx.getLeadingMargin(useFirstLineMargin);
                }
                return margin;
            }
        }
    }

    private int getParagraphTrailingMargin(int line) {
        if (!this.mSpannedText) {
            return 0;
        } else {
            Spanned spanned = (Spanned) this.mText;
            int lineStart = this.getLineStart(line);
            int lineEnd = this.getLineEnd(line);
            int spanEnd = spanned.nextSpanTransition(lineStart, lineEnd, TrailingMarginSpan.class);
            List<TrailingMarginSpan> spans = getParagraphSpans(spanned, lineStart, spanEnd, TrailingMarginSpan.class);
            if (spans.isEmpty()) {
                return 0;
            } else {
                int margin = 0;
                for (TrailingMarginSpan span : spans) {
                    margin += span.getTrailingMargin();
                }
                return margin;
            }
        }
    }

    private boolean shouldClampCursor(int line) {
        return switch(this.getParagraphAlignment(line)) {
            case ALIGN_LEFT ->
                true;
            case ALIGN_NORMAL ->
                this.getParagraphDirection(line) > 0;
            default ->
                false;
        };
    }

    public float getLineLeft(int line) {
        int dir = this.getParagraphDirection(line);
        Layout.Alignment align = this.getParagraphAlignment(line);
        if (align == null) {
            align = Layout.Alignment.ALIGN_CENTER;
        }
        switch(switch(align) {
            case ALIGN_NORMAL ->
                dir == -1 ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
            case ALIGN_OPPOSITE ->
                dir == -1 ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
            case ALIGN_CENTER ->
                Layout.Alignment.ALIGN_CENTER;
            case ALIGN_RIGHT ->
                Layout.Alignment.ALIGN_RIGHT;
            default ->
                Layout.Alignment.ALIGN_LEFT;
        }) {
            case ALIGN_CENTER:
                int left = this.getParagraphLeft(line);
                float max = this.getLineMax(line);
                return (float) Math.floor((double) ((float) left + ((float) this.mWidth - max) / 2.0F));
            case ALIGN_RIGHT:
                return (float) this.mWidth - this.getLineMax(line);
            default:
                return 0.0F;
        }
    }

    public float getLineRight(int line) {
        int dir = this.getParagraphDirection(line);
        Layout.Alignment align = this.getParagraphAlignment(line);
        if (align == null) {
            align = Layout.Alignment.ALIGN_CENTER;
        }
        switch(switch(align) {
            case ALIGN_NORMAL ->
                dir == -1 ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
            case ALIGN_OPPOSITE ->
                dir == -1 ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
            case ALIGN_CENTER ->
                Layout.Alignment.ALIGN_CENTER;
            case ALIGN_RIGHT ->
                Layout.Alignment.ALIGN_RIGHT;
            default ->
                Layout.Alignment.ALIGN_LEFT;
        }) {
            case ALIGN_CENTER:
                int right = this.getParagraphRight(line);
                float max = this.getLineMax(line);
                return (float) Math.ceil((double) ((float) right - ((float) this.mWidth - max) / 2.0F));
            case ALIGN_RIGHT:
                return (float) this.mWidth;
            default:
                return this.getLineMax(line);
        }
    }

    public void getCursorPath(int point, @NonNull FloatArrayList dest, @NonNull CharSequence buffer) {
        dest.clear();
        if (point >= 0) {
            int line = this.getLineForOffset(point);
            int top = this.getLineTop(line);
            int bottom = this.getLineBottom(line);
            boolean clamped = this.shouldClampCursor(line);
            float h1 = this.getPrimaryHorizontal(point, clamped) - 0.5F;
            int caps = TextKeyListener.getMetaState(buffer, 1);
            int fn = TextKeyListener.getMetaState(buffer, 4);
            int dist = bottom - top >> 3;
            top += dist;
            bottom -= dist;
            if (caps == 0 && fn == 0) {
                dist = 0;
            } else {
                dist = bottom - top >> 2;
                if (fn != 0) {
                    top += dist;
                }
                if (caps != 0) {
                    bottom -= dist;
                }
            }
            if (h1 < 0.5F) {
                h1 = 0.5F;
            }
            dest.add(h1);
            dest.add((float) top);
            dest.add(h1);
            dest.add((float) bottom);
            if (caps == 1) {
                dest.add(h1);
                dest.add((float) bottom);
                dest.add(h1 - (float) dist * 0.7F);
                dest.add((float) (bottom + dist));
                dest.add(h1 - (float) dist * 0.7F);
                dest.add((float) (bottom + dist) - 0.5F);
                dest.add(h1 + (float) dist * 0.7F);
                dest.add((float) (bottom + dist) - 0.5F);
                dest.add(h1 + (float) dist * 0.7F);
                dest.add((float) (bottom + dist));
                dest.add(h1);
                dest.add((float) bottom);
            }
            if (fn == 1) {
                dest.add(h1);
                dest.add((float) top);
                dest.add(h1 - (float) dist * 0.7F);
                dest.add((float) (top - dist));
                dest.add(h1 - (float) dist * 0.7F);
                dest.add((float) (top - dist) + 0.5F);
                dest.add(h1 + (float) dist * 0.7F);
                dest.add((float) (top - dist) + 0.5F);
                dest.add(h1 + (float) dist * 0.7F);
                dest.add((float) (top - dist));
                dest.add(h1);
                dest.add((float) top);
            }
        }
    }

    private void addSelection(int line, int start, int end, int top, int bottom, FloatArrayList out) {
        int linestart = this.getLineStart(line);
        int lineend = this.getLineEnd(line);
        Directions dirs = this.getLineDirections(line);
        if (lineend > linestart && this.mText.charAt(lineend - 1) == '\n') {
            lineend--;
        }
        for (int i = 0; i < dirs.mDirections.length; i += 2) {
            int here = linestart + dirs.mDirections[i];
            int there = here + (dirs.mDirections[i + 1] & 67108863);
            if (there > lineend) {
                there = lineend;
            }
            if (start <= there && end >= here) {
                int st = Math.max(start, here);
                int en = Math.min(end, there);
                if (st != en) {
                    float h1 = this.getHorizontal(st, false, line, false);
                    float h2 = this.getHorizontal(en, true, line, false);
                    float left = Math.min(h1, h2);
                    float right = Math.max(h1, h2);
                    out.add(left);
                    out.add((float) top);
                    out.add(right);
                    out.add((float) bottom);
                }
            }
        }
    }

    public void getSelectionPath(int start, int end, @NonNull FloatArrayList dest) {
        dest.clear();
        if (start != end) {
            if (end < start) {
                int temp = end;
                end = start;
                start = temp;
            }
            int startline = this.getLineForOffset(start);
            int endline = this.getLineForOffset(end);
            int top = this.getLineTop(startline);
            int bottom = this.getLineBottom(endline);
            if (startline == endline) {
                this.addSelection(startline, start, end, top, bottom, dest);
            } else {
                float width = (float) this.mWidth;
                this.addSelection(startline, start, this.getLineEnd(startline), top, this.getLineBottom(startline), dest);
                if (this.getParagraphDirection(startline) == -1) {
                    dest.add(0.0F);
                    dest.add((float) top);
                    dest.add(this.getLineLeft(startline));
                    dest.add((float) this.getLineBottom(startline));
                } else {
                    dest.add(this.getLineRight(startline));
                    dest.add((float) top);
                    dest.add(width);
                    dest.add((float) this.getLineBottom(startline));
                }
                for (int i = startline + 1; i < endline; i++) {
                    top = this.getLineTop(i);
                    bottom = this.getLineBottom(i);
                    dest.add(0.0F);
                    dest.add((float) top);
                    dest.add(width);
                    dest.add((float) bottom);
                }
                top = this.getLineTop(endline);
                bottom = this.getLineBottom(endline);
                this.addSelection(endline, this.getLineStart(endline), end, top, bottom, dest);
                if (this.getParagraphDirection(endline) == -1) {
                    dest.add(this.getLineRight(endline));
                    dest.add((float) top);
                    dest.add(width);
                    dest.add((float) bottom);
                } else {
                    dest.add(0.0F);
                    dest.add((float) top);
                    dest.add(this.getLineLeft(endline));
                    dest.add((float) bottom);
                }
            }
        }
    }

    public static float getDesiredWidth(CharSequence source, TextPaint paint) {
        return getDesiredWidth(source, 0, source.length(), paint);
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint) {
        return getDesiredWidth(source, start, end, paint, TextDirectionHeuristics.FIRSTSTRONG_LTR);
    }

    public static float getDesiredWidth(CharSequence source, int start, int end, TextPaint paint, TextDirectionHeuristic textDir) {
        return getDesiredWidthWithLimit(source, start, end, paint, textDir, Float.MAX_VALUE);
    }

    public static float getDesiredWidthWithLimit(CharSequence source, int start, int end, TextPaint paint, TextDirectionHeuristic textDir, float upperLimit) {
        float need = 0.0F;
        int i = start;
        while (i <= end) {
            int next = TextUtils.indexOf(source, '\n', i, end);
            if (next < 0) {
                next = end;
            }
            float w = measurePara(paint, source, i, next, textDir);
            if (w > upperLimit) {
                return upperLimit;
            }
            if (w > need) {
                need = w;
            }
            i = ++next;
        }
        return need;
    }

    private static float measurePara(TextPaint paint, CharSequence text, int start, int end, TextDirectionHeuristic textDir) {
        MeasuredParagraph mt = null;
        TextLine tl = TextLine.obtain();
        float var25;
        try {
            mt = MeasuredParagraph.buildForBidi(text, start, end, textDir, null);
            char[] chars = mt.getChars();
            int len = chars.length;
            Directions directions = mt.getDirections(0, len);
            int dir = mt.getParagraphDir();
            boolean hasTabs = false;
            TabStops tabStops = null;
            int margin = 0;
            if (text instanceof Spanned spanned) {
                for (LeadingMarginSpan lms : getParagraphSpans(spanned, start, end, LeadingMarginSpan.class)) {
                    margin += lms.getLeadingMargin(true);
                }
                for (TrailingMarginSpan tms : getParagraphSpans(spanned, start, end, TrailingMarginSpan.class)) {
                    margin += tms.getTrailingMargin();
                }
            }
            for (char c : chars) {
                if (c == '\t') {
                    hasTabs = true;
                    if (text instanceof Spanned spanned) {
                        int spanEnd = spanned.nextSpanTransition(start, end, TabStopSpan.class);
                        List<TabStopSpan> spans = getParagraphSpans(spanned, start, spanEnd, TabStopSpan.class);
                        if (!spans.isEmpty()) {
                            tabStops = new TabStops(20.0F, spans);
                        }
                    }
                    break;
                }
            }
            tl.set(paint, text, start, end, dir, directions, hasTabs, tabStops, 0, 0);
            var25 = (float) margin + Math.abs(tl.metrics(null));
        } finally {
            tl.recycle();
            if (mt != null) {
                mt.recycle();
            }
        }
        return var25;
    }

    @NonNull
    static <T> List<T> getParagraphSpans(@NonNull Spanned text, int start, int end, Class<T> type) {
        if (start == end && start > 0) {
            return Collections.emptyList();
        } else {
            return text instanceof SpannableStringBuilder ? ((SpannableStringBuilder) text).getSpans(start, end, type, false, null) : text.getSpans(start, end, type);
        }
    }

    private void ellipsize(int start, int end, int line, char[] dest, int destoff, TextUtils.TruncateAt method) {
        int ellipsisCount = this.getEllipsisCount(line);
        if (ellipsisCount != 0) {
            int ellipsisStart = this.getEllipsisStart(line);
            int lineStart = this.getLineStart(line);
            String ellipsisString = TextUtils.getEllipsisString(method);
            int ellipsisStringLen = ellipsisString.length();
            boolean useEllipsisString = ellipsisCount >= ellipsisStringLen;
            int min = Math.max(0, start - ellipsisStart - lineStart);
            int max = Math.min(ellipsisCount, end - ellipsisStart - lineStart);
            for (int i = min; i < max; i++) {
                char c;
                if (useEllipsisString && i < ellipsisStringLen) {
                    c = ellipsisString.charAt(i);
                } else {
                    c = '\ufeff';
                }
                int a = i + ellipsisStart + lineStart;
                dest[destoff + a - start] = c;
            }
        }
    }

    public static enum Alignment {

        ALIGN_NORMAL, ALIGN_OPPOSITE, ALIGN_CENTER, ALIGN_LEFT, ALIGN_RIGHT
    }

    static class Ellipsizer implements CharSequence, GetChars {

        CharSequence mText;

        Layout mLayout;

        int mWidth;

        TextUtils.TruncateAt mMethod;

        public Ellipsizer(CharSequence s) {
            this.mText = s;
        }

        public char charAt(int off) {
            char[] buf = TextUtils.obtain(1);
            this.getChars(off, off + 1, buf, 0);
            char ret = buf[0];
            TextUtils.recycle(buf);
            return ret;
        }

        @Override
        public void getChars(int start, int end, char[] dest, int destoff) {
            int line1 = this.mLayout.getLineForOffset(start);
            int line2 = this.mLayout.getLineForOffset(end);
            TextUtils.getChars(this.mText, start, end, dest, destoff);
            for (int i = line1; i <= line2; i++) {
                this.mLayout.ellipsize(start, end, i, dest, destoff, this.mMethod);
            }
        }

        public int length() {
            return this.mText.length();
        }

        public CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            this.getChars(start, end, s, 0);
            return new String(s);
        }

        public String toString() {
            char[] s = new char[this.length()];
            this.getChars(0, this.length(), s, 0);
            return new String(s);
        }
    }

    private class HorizontalMeasurementProvider {

        private final int mLine;

        private final boolean mPrimary;

        private float[] mHorizontals;

        private int mLineStartOffset;

        HorizontalMeasurementProvider(int line, boolean primary) {
            this.mLine = line;
            this.mPrimary = primary;
            this.init();
        }

        private void init() {
            Directions dirs = Layout.this.getLineDirections(this.mLine);
            if (dirs != Directions.ALL_LEFT_TO_RIGHT) {
                this.mHorizontals = Layout.this.getLineHorizontals(this.mLine, false, this.mPrimary);
                this.mLineStartOffset = Layout.this.getLineStart(this.mLine);
            }
        }

        float get(int offset) {
            int index = offset - this.mLineStartOffset;
            return this.mHorizontals != null && index >= 0 && index < this.mHorizontals.length ? this.mHorizontals[index] : Layout.this.getHorizontal(offset, this.mPrimary);
        }
    }

    static class SpannedEllipsizer extends Layout.Ellipsizer implements Spanned {

        private final Spanned mSpanned;

        public SpannedEllipsizer(CharSequence display) {
            super(display);
            this.mSpanned = (Spanned) display;
        }

        @NonNull
        @Override
        public <T> List<T> getSpans(int start, int end, Class<? extends T> type, @Nullable List<T> out) {
            return this.mSpanned.getSpans(start, end, type, out);
        }

        @Override
        public int getSpanStart(@NonNull Object tag) {
            return this.mSpanned.getSpanStart(tag);
        }

        @Override
        public int getSpanEnd(@NonNull Object tag) {
            return this.mSpanned.getSpanEnd(tag);
        }

        @Override
        public int getSpanFlags(@NonNull Object tag) {
            return this.mSpanned.getSpanFlags(tag);
        }

        @Override
        public int nextSpanTransition(int start, int limit, Class<?> type) {
            return this.mSpanned.nextSpanTransition(start, limit, type);
        }

        @Override
        public CharSequence subSequence(int start, int end) {
            char[] s = new char[end - start];
            this.getChars(start, end, s, 0);
            SpannableString ss = new SpannableString(new String(s));
            TextUtils.copySpansFrom(this.mSpanned, start, end, Object.class, ss, 0);
            return ss;
        }
    }
}