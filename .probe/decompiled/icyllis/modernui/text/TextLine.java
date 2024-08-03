package icyllis.modernui.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Canvas;
import icyllis.modernui.graphics.Paint;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.LayoutPiece;
import icyllis.modernui.graphics.text.ShapedText;
import icyllis.modernui.text.style.CharacterStyle;
import icyllis.modernui.text.style.MetricAffectingSpan;
import icyllis.modernui.text.style.ReplacementSpan;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import java.util.ArrayList;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class TextLine {

    private static final Pools.Pool<TextLine> sPool = Pools.newSynchronizedPool(3);

    private static final char TAB_CHAR = '\t';

    private TextPaint mPaint;

    private CharSequence mText;

    private int mStart;

    private int mLen;

    private int mDir;

    private Directions mDirections;

    private boolean mHasTabs;

    private TabStops mTabs;

    private char[] mChars;

    private boolean mCharsValid;

    private Spanned mSpanned;

    private PrecomputedText mComputed;

    private int mEllipsisStart;

    private int mEllipsisEnd;

    private final TextPaint mWorkPaint = new TextPaint();

    private final TextPaint mActivePaint = new TextPaint();

    private final SpanSet<MetricAffectingSpan> mMetricAffectingSpanSpanSet = new SpanSet<>(MetricAffectingSpan.class);

    private final SpanSet<CharacterStyle> mCharacterStyleSpanSet = new SpanSet<>(CharacterStyle.class);

    private final SpanSet<ReplacementSpan> mReplacementSpanSpanSet = new SpanSet<>(ReplacementSpan.class);

    private final ArrayList<LayoutPiece> mCachedPieces = new ArrayList();

    private final FloatArrayList mCachedXOffsets = new FloatArrayList();

    private final FontMetricsInt mCachedFontExtent = new FontMetricsInt();

    private final ShapedText.RunConsumer mBuildCachedPieces = (piece, offsetX) -> {
        this.mCachedPieces.add(piece);
        this.mCachedXOffsets.add(offsetX);
    };

    private TextLine() {
    }

    @NonNull
    public static TextLine obtain() {
        TextLine tl = sPool.acquire();
        if (tl == null) {
            tl = new TextLine();
        }
        return tl;
    }

    public void recycle() {
        this.mText = null;
        this.mPaint = null;
        this.mDirections = null;
        this.mSpanned = null;
        this.mTabs = null;
        this.mComputed = null;
        this.mMetricAffectingSpanSpanSet.recycle();
        this.mCharacterStyleSpanSet.recycle();
        this.mReplacementSpanSpanSet.recycle();
        sPool.release(this);
    }

    public void set(@NonNull TextPaint paint, @NonNull CharSequence text, int start, int limit, int dir, @NonNull Directions directions, boolean hasTabs, @Nullable TabStops tabStops, int ellipsisStart, int ellipsisEnd) {
        this.mPaint = paint;
        this.mText = text;
        this.mStart = start;
        this.mLen = limit - start;
        this.mDir = dir;
        this.mDirections = directions;
        this.mHasTabs = hasTabs;
        this.mSpanned = null;
        if (text instanceof Spanned) {
            this.mSpanned = (Spanned) text;
            this.mCharsValid = this.mReplacementSpanSpanSet.init(this.mSpanned, start, limit);
        } else {
            this.mCharsValid = false;
        }
        this.mComputed = null;
        if (text instanceof PrecomputedText) {
            this.mComputed = (PrecomputedText) text;
            if (!this.mComputed.getParams().getTextPaint().equalsForTextMeasurement(paint)) {
                this.mComputed = null;
            }
        }
        if (this.mCharsValid) {
            if (this.mChars == null || this.mChars.length < this.mLen) {
                this.mChars = new char[this.mLen];
            }
            TextUtils.getChars(text, start, limit, this.mChars, 0);
            char[] chars = this.mChars;
            int i = start;
            while (i < limit) {
                int inext = this.mReplacementSpanSpanSet.getNextTransition(i, limit);
                if (this.mReplacementSpanSpanSet.hasSpansIntersecting(i, inext) && (i - start >= ellipsisEnd || inext - start <= ellipsisStart)) {
                    chars[i - start] = '￼';
                    int j = i - start + 1;
                    for (int e = inext - start; j < e; j++) {
                        chars[j] = '\ufeff';
                    }
                }
                i = inext;
            }
        }
        this.mTabs = tabStops;
        if (ellipsisStart != ellipsisEnd) {
            this.mEllipsisStart = ellipsisStart;
            this.mEllipsisEnd = ellipsisEnd;
        } else {
            this.mEllipsisStart = this.mEllipsisEnd = 0;
        }
    }

    private char charAt(int i) {
        return this.mCharsValid ? this.mChars[i] : this.mText.charAt(i + this.mStart);
    }

    public void draw(@NonNull Canvas canvas, float x, int top, int y, int bottom) {
        float h = 0.0F;
        int runCount = this.mDirections.getRunCount();
        for (int runIndex = 0; runIndex < runCount; runIndex++) {
            int runStart = this.mDirections.getRunStart(runIndex);
            if (runStart >= this.mLen) {
                break;
            }
            int runLimit = Math.min(runStart + this.mDirections.getRunLength(runIndex), this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            int segStart = runStart;
            for (int j = this.mHasTabs ? runStart : runLimit; j <= runLimit; j++) {
                if (j == runLimit || this.charAt(j) == '\t') {
                    h += this.drawRun(canvas, segStart, j, runIsRtl, x + h, top, y, bottom, runIndex != runCount - 1 || j != this.mLen);
                    if (j != runLimit) {
                        h = (float) this.mDir * this.nextTab(h * (float) this.mDir);
                    }
                    segStart = j + 1;
                }
            }
        }
    }

    public float metrics(@Nullable FontMetricsInt fmi) {
        return this.measure(this.mLen, false, fmi);
    }

    public float measure(int offset, boolean trailing, @Nullable FontMetricsInt fmi) {
        if (offset > this.mLen) {
            throw new IndexOutOfBoundsException("offset(" + offset + ") should be less than line limit(" + this.mLen + ")");
        } else {
            int target = trailing ? offset - 1 : offset;
            if (target < 0) {
                return 0.0F;
            } else {
                float h = 0.0F;
                for (int runIndex = 0; runIndex < this.mDirections.getRunCount(); runIndex++) {
                    int runStart = this.mDirections.getRunStart(runIndex);
                    if (runStart > this.mLen) {
                        break;
                    }
                    int runLimit = Math.min(runStart + this.mDirections.getRunLength(runIndex), this.mLen);
                    boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
                    int segStart = runStart;
                    for (int j = this.mHasTabs ? runStart : runLimit; j <= runLimit; j++) {
                        if (j == runLimit || this.charAt(j) == '\t') {
                            boolean targetIsInThisSegment = target >= segStart && target < j;
                            boolean sameDirection = this.mDir == -1 == runIsRtl;
                            if (targetIsInThisSegment && sameDirection) {
                                return h + this.measureRun(segStart, offset, j, runIsRtl, fmi);
                            }
                            float segmentWidth = this.measureRun(segStart, j, j, runIsRtl, fmi);
                            h += sameDirection ? segmentWidth : -segmentWidth;
                            if (targetIsInThisSegment) {
                                return h + this.measureRun(segStart, offset, j, runIsRtl, null);
                            }
                            if (j != runLimit) {
                                if (offset == j) {
                                    return h;
                                }
                                h = (float) this.mDir * this.nextTab(h * (float) this.mDir);
                                if (target == j) {
                                    return h;
                                }
                            }
                            segStart = j + 1;
                        }
                    }
                }
                return h;
            }
        }
    }

    public float[] measureAllOffsets(boolean[] trailing, FontMetricsInt fmi) {
        float[] measurement = new float[this.mLen + 1];
        int[] target = new int[this.mLen + 1];
        for (int offset = 0; offset < target.length; offset++) {
            target[offset] = trailing[offset] ? offset - 1 : offset;
        }
        if (target[0] < 0) {
            measurement[0] = 0.0F;
        }
        float h = 0.0F;
        for (int runIndex = 0; runIndex < this.mDirections.getRunCount(); runIndex++) {
            int runStart = this.mDirections.getRunStart(runIndex);
            if (runStart > this.mLen) {
                break;
            }
            int runLimit = Math.min(runStart + this.mDirections.getRunLength(runIndex), this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            int segStart = runStart;
            for (int j = this.mHasTabs ? runStart : runLimit; j <= runLimit; j++) {
                if (j == runLimit || this.charAt(j) == '\t') {
                    float oldh = h;
                    boolean advance = this.mDir == -1 == runIsRtl;
                    float w = this.measureRun(segStart, j, j, runIsRtl, fmi);
                    h += advance ? w : -w;
                    float baseh = advance ? oldh : h;
                    FontMetricsInt crtfmi = advance ? fmi : null;
                    for (int offset = segStart; offset <= j && offset <= this.mLen; offset++) {
                        if (target[offset] >= segStart && target[offset] < j) {
                            measurement[offset] = baseh + this.measureRun(segStart, offset, j, runIsRtl, crtfmi);
                        }
                    }
                    if (j != runLimit) {
                        if (target[j] == j) {
                            measurement[j] = h;
                        }
                        h = (float) this.mDir * this.nextTab(h * (float) this.mDir);
                        if (target[j + 1] == j) {
                            measurement[j + 1] = h;
                        }
                    }
                    segStart = j + 1;
                }
            }
        }
        if (target[this.mLen] == this.mLen) {
            measurement[this.mLen] = h;
        }
        return measurement;
    }

    void shape(@NonNull TextShaper.GlyphsConsumer consumer) {
        float horizontal = 0.0F;
        float x = 0.0F;
        int runCount = this.mDirections.getRunCount();
        for (int runIndex = 0; runIndex < runCount; runIndex++) {
            int runStart = this.mDirections.getRunStart(runIndex);
            if (runStart > this.mLen) {
                break;
            }
            int runLimit = Math.min(runStart + this.mDirections.getRunLength(runIndex), this.mLen);
            boolean runIsRtl = this.mDirections.isRunRtl(runIndex);
            int segStart = runStart;
            for (int j = this.mHasTabs ? runStart : runLimit; j <= runLimit; j++) {
                if (j == runLimit || this.charAt(j) == '\t') {
                    horizontal += this.shapeRun(consumer, segStart, j, runIsRtl, x + horizontal, runIndex != runCount - 1 || j != this.mLen);
                    if (j != runLimit) {
                        horizontal = (float) this.mDir * this.nextTab(horizontal * (float) this.mDir);
                    }
                    segStart = j + 1;
                }
            }
        }
    }

    public int getOffsetToLeftRightOf(int cursor, boolean toLeft) {
        int lineStart = 0;
        int lineEnd = this.mLen;
        boolean paraIsRtl = this.mDir == -1;
        int[] runs = this.mDirections.mDirections;
        int runLevel = 0;
        int runStart = lineStart;
        int runLimit = lineEnd;
        int newCaret = -1;
        boolean trailing = false;
        int runIndex;
        if (cursor == lineStart) {
            runIndex = -2;
        } else if (cursor == lineEnd) {
            runIndex = runs.length;
        } else {
            label155: for (runIndex = 0; runIndex < runs.length; runIndex += 2) {
                runStart = lineStart + runs[runIndex];
                if (cursor >= runStart) {
                    runLimit = runStart + (runs[runIndex + 1] & 67108863);
                    if (runLimit > lineEnd) {
                        runLimit = lineEnd;
                    }
                    if (cursor < runLimit) {
                        runLevel = runs[runIndex + 1] >>> 26 & 63;
                        if (cursor != runStart) {
                            break;
                        }
                        int pos = cursor - 1;
                        for (int prevRunIndex = 0; prevRunIndex < runs.length; prevRunIndex += 2) {
                            int prevRunStart = lineStart + runs[prevRunIndex];
                            if (pos >= prevRunStart) {
                                int prevRunLimit = prevRunStart + (runs[prevRunIndex + 1] & 67108863);
                                if (prevRunLimit > lineEnd) {
                                    prevRunLimit = lineEnd;
                                }
                                if (pos < prevRunLimit) {
                                    int prevRunLevel = runs[prevRunIndex + 1] >>> 26 & 63;
                                    if (prevRunLevel < runLevel) {
                                        runIndex = prevRunIndex;
                                        runLevel = prevRunLevel;
                                        runStart = prevRunStart;
                                        runLimit = prevRunLimit;
                                        trailing = true;
                                        break label155;
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
            }
            if (runIndex != runs.length) {
                boolean runIsRtl = (runLevel & 1) != 0;
                boolean advance = toLeft == runIsRtl;
                if (cursor != (advance ? runLimit : runStart) || advance != trailing) {
                    newCaret = this.getOffsetBeforeAfter(runIndex, runStart, runLimit, runIsRtl, cursor, advance);
                    if (newCaret != (advance ? runLimit : runStart)) {
                        return newCaret;
                    }
                }
            }
        }
        while (true) {
            boolean advance = toLeft == paraIsRtl;
            int otherRunIndex = runIndex + (advance ? 2 : -2);
            if (otherRunIndex >= 0 && otherRunIndex < runs.length) {
                int otherRunStart = lineStart + runs[otherRunIndex];
                int otherRunLimit = otherRunStart + (runs[otherRunIndex + 1] & 67108863);
                if (otherRunLimit > lineEnd) {
                    otherRunLimit = lineEnd;
                }
                int otherRunLevel = runs[otherRunIndex + 1] >>> 26 & 63;
                boolean otherRunIsRtl = (otherRunLevel & 1) != 0;
                advance = toLeft == otherRunIsRtl;
                if (newCaret == -1) {
                    newCaret = this.getOffsetBeforeAfter(otherRunIndex, otherRunStart, otherRunLimit, otherRunIsRtl, advance ? otherRunStart : otherRunLimit, advance);
                    if (newCaret != (advance ? otherRunLimit : otherRunStart)) {
                        break;
                    }
                    runIndex = otherRunIndex;
                    runLevel = otherRunLevel;
                    continue;
                }
                if (otherRunLevel < runLevel) {
                    newCaret = advance ? otherRunStart : otherRunLimit;
                }
                break;
            }
            if (newCaret == -1) {
                newCaret = advance ? this.mLen + 1 : -1;
            } else if (newCaret <= lineEnd) {
                newCaret = advance ? lineEnd : lineStart;
            }
            break;
        }
        return newCaret;
    }

    private int getOffsetBeforeAfter(int runIndex, int runStart, int runLimit, boolean runIsRtl, int offset, boolean after) {
        if (runIndex >= 0 && offset != (after ? this.mLen : 0)) {
            TextPaint wp = this.mWorkPaint;
            wp.set(this.mPaint);
            int spanStart = runStart;
            int spanLimit;
            if (this.mSpanned == null) {
                spanLimit = runLimit;
            } else {
                int target = after ? offset + 1 : offset;
                int limit = this.mStart + runLimit;
                if (!this.mMetricAffectingSpanSpanSet.init(this.mSpanned, this.mStart + runStart, limit)) {
                    spanLimit = runLimit;
                } else {
                    while (true) {
                        spanLimit = this.mMetricAffectingSpanSpanSet.getNextTransition(this.mStart + spanStart, limit) - this.mStart;
                        if (spanLimit >= target) {
                            ReplacementSpan replacement = null;
                            for (int j = 0; j < this.mMetricAffectingSpanSpanSet.size(); j++) {
                                MetricAffectingSpan span = (MetricAffectingSpan) this.mMetricAffectingSpanSpanSet.get(j);
                                if (this.mMetricAffectingSpanSpanSet.mSpanStarts[j] < this.mStart + spanLimit && this.mMetricAffectingSpanSpanSet.mSpanEnds[j] > this.mStart + spanStart) {
                                    if (span instanceof ReplacementSpan) {
                                        replacement = (ReplacementSpan) span;
                                    } else {
                                        span.updateMeasureState(wp);
                                    }
                                }
                            }
                            if (replacement != null) {
                                return after ? spanLimit : spanStart;
                            }
                            break;
                        }
                        spanStart = spanLimit;
                    }
                }
            }
            int cursorOpt = after ? 0 : 2;
            return this.mCharsValid ? wp.getTextRunCursor(this.mChars, spanStart, spanLimit - spanStart, offset, cursorOpt) : wp.getTextRunCursor(this.mText, this.mStart + spanStart, this.mStart + spanLimit, this.mStart + offset, cursorOpt) - this.mStart;
        } else {
            CharSequence text = this.mText;
            offset += this.mStart;
            if (after) {
                int len = text.length();
                if (offset == len || offset == len - 1) {
                    return len - this.mStart;
                }
                char c = text.charAt(offset);
                if (c >= '\ud800' && c <= '\udbff') {
                    char c1 = text.charAt(offset + 1);
                    if (c1 >= '\udc00' && c1 <= '\udfff') {
                        offset += 2;
                    } else {
                        offset++;
                    }
                } else {
                    offset++;
                }
                if (this.mSpanned != null) {
                    this.mReplacementSpanSpanSet.init(this.mSpanned, offset, offset);
                    for (int i = 0; i < this.mReplacementSpanSpanSet.size(); i++) {
                        int start = this.mReplacementSpanSpanSet.mSpanStarts[i];
                        int end = this.mReplacementSpanSpanSet.mSpanEnds[i];
                        if (start < offset && end > offset) {
                            offset = end;
                        }
                    }
                }
            } else {
                if (offset == 0 || offset == 1) {
                    return -this.mStart;
                }
                char cx = text.charAt(offset - 1);
                if (cx >= '\udc00' && cx <= '\udfff') {
                    char c1 = text.charAt(offset - 2);
                    if (c1 >= '\ud800' && c1 <= '\udbff') {
                        offset -= 2;
                    } else {
                        offset--;
                    }
                } else {
                    offset--;
                }
                if (this.mSpanned != null) {
                    this.mReplacementSpanSpanSet.init(this.mSpanned, offset, offset);
                    for (int ix = 0; ix < this.mReplacementSpanSpanSet.size(); ix++) {
                        int start = this.mReplacementSpanSpanSet.mSpanStarts[ix];
                        int end = this.mReplacementSpanSpanSet.mSpanEnds[ix];
                        if (start < offset && end > offset) {
                            offset = start;
                        }
                    }
                }
            }
            return offset - this.mStart;
        }
    }

    private float drawRun(@NonNull Canvas c, int start, int limit, boolean runIsRtl, float x, int top, int y, int bottom, boolean needWidth) {
        if (this.mDir == 1 == runIsRtl) {
            float w = -this.measureRun(start, limit, limit, runIsRtl, null);
            this.handleRun(start, limit, limit, runIsRtl, c, null, x + w, top, y, bottom, null, false);
            return w;
        } else {
            return this.handleRun(start, limit, limit, runIsRtl, c, null, x, top, y, bottom, null, needWidth);
        }
    }

    private float measureRun(int start, int offset, int limit, boolean runIsRtl, @Nullable FontMetricsInt fmi) {
        return this.handleRun(start, offset, limit, runIsRtl, null, null, 0.0F, 0, 0, 0, fmi, true);
    }

    private float shapeRun(@NonNull TextShaper.GlyphsConsumer consumer, int start, int limit, boolean runIsRtl, float x, boolean needWidth) {
        if (this.mDir == 1 == runIsRtl) {
            float w = -this.measureRun(start, limit, limit, runIsRtl, null);
            this.handleRun(start, limit, limit, runIsRtl, null, consumer, x + w, 0, 0, 0, null, false);
            return w;
        } else {
            return this.handleRun(start, limit, limit, runIsRtl, null, consumer, x, 0, 0, 0, null, needWidth);
        }
    }

    private float handleRun(int start, int measureLimit, int limit, boolean runIsRtl, @Nullable Canvas c, @Nullable TextShaper.GlyphsConsumer consumer, float x, int top, int y, int bottom, @Nullable FontMetricsInt fmi, boolean needWidth) {
        if (measureLimit >= start && measureLimit <= limit) {
            if (start == measureLimit) {
                if (fmi != null) {
                    TextPaint wp = this.mWorkPaint;
                    wp.set(this.mPaint);
                    expandMetricsFromPaint(fmi, wp);
                }
                return 0.0F;
            } else {
                boolean needsSpanMeasurement;
                if (this.mSpanned == null) {
                    needsSpanMeasurement = false;
                } else {
                    needsSpanMeasurement = this.mMetricAffectingSpanSpanSet.init(this.mSpanned, this.mStart + start, this.mStart + limit) | this.mCharacterStyleSpanSet.init(this.mSpanned, this.mStart + start, this.mStart + limit);
                }
                if (!needsSpanMeasurement) {
                    TextPaint wp = this.mWorkPaint;
                    wp.set(this.mPaint);
                    if (fmi != null) {
                        expandMetricsFromPaint(fmi, wp);
                    }
                    return this.handleText(wp, start, limit, start, limit, runIsRtl, c, consumer, x, top, y, bottom, needWidth, measureLimit, 0);
                } else {
                    float originalX = x;
                    int i = start;
                    while (i < measureLimit) {
                        TextPaint wp = this.mWorkPaint;
                        wp.set(this.mPaint);
                        int inext = this.mMetricAffectingSpanSpanSet.getNextTransition(this.mStart + i, this.mStart + limit) - this.mStart;
                        int mlimit = Math.min(inext, measureLimit);
                        ReplacementSpan replacement = null;
                        for (int j = 0; j < this.mMetricAffectingSpanSpanSet.size(); j++) {
                            if (this.mMetricAffectingSpanSpanSet.mSpanStarts[j] < this.mStart + mlimit && this.mMetricAffectingSpanSpanSet.mSpanEnds[j] > this.mStart + i) {
                                MetricAffectingSpan span = (MetricAffectingSpan) this.mMetricAffectingSpanSpanSet.get(j);
                                if (span instanceof ReplacementSpan) {
                                    boolean insideEllipsis = this.mStart + this.mEllipsisStart <= this.mMetricAffectingSpanSpanSet.mSpanStarts[j] && this.mMetricAffectingSpanSpanSet.mSpanEnds[j] <= this.mStart + this.mEllipsisEnd;
                                    replacement = insideEllipsis ? null : (ReplacementSpan) span;
                                } else {
                                    span.updateDrawState(wp);
                                }
                            }
                        }
                        if (replacement != null) {
                            x += this.handleReplacement(replacement, wp, i, mlimit, runIsRtl, c, x, top, y, bottom, fmi, needWidth || mlimit < measureLimit);
                        } else {
                            if (fmi != null) {
                                expandMetricsFromPaint(fmi, wp);
                            }
                            TextPaint activePaint = this.mActivePaint;
                            int jx = i;
                            while (jx < mlimit) {
                                int jnext = this.mCharacterStyleSpanSet.getNextTransition(this.mStart + jx, this.mStart + inext) - this.mStart;
                                int offset = Math.min(jnext, mlimit);
                                activePaint.set(this.mPaint);
                                for (int k = 0; k < this.mCharacterStyleSpanSet.size(); k++) {
                                    if (this.mCharacterStyleSpanSet.mSpanStarts[k] < this.mStart + offset && this.mCharacterStyleSpanSet.mSpanEnds[k] > this.mStart + jx) {
                                        CharacterStyle span = (CharacterStyle) this.mCharacterStyleSpanSet.get(k);
                                        span.updateDrawState(activePaint);
                                    }
                                }
                                int flags = activePaint.getFontFlags() & 3072;
                                activePaint.setFontFlags(activePaint.getFontFlags() & -3073);
                                x += this.handleText(activePaint, jx, jnext, i, inext, runIsRtl, c, consumer, x, top, y, bottom, needWidth || jnext < measureLimit, offset, flags);
                                jx = jnext;
                            }
                        }
                        i = inext;
                    }
                    return x - originalX;
                }
            }
        } else {
            throw new IndexOutOfBoundsException("measureLimit (" + measureLimit + ") is out of start (" + start + ") and limit (" + limit + ") bounds");
        }
    }

    private float handleText(@NonNull TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, @Nullable Canvas c, @Nullable TextShaper.GlyphsConsumer consumer, float x, int top, int y, int bottom, boolean needWidth, int offset, int flags) {
        if (end == start) {
            return 0.0F;
        } else if (consumer != null) {
            assert c == null;
            return this.shapeTextRun(consumer, wp, start, end, contextStart, contextEnd, runIsRtl, x);
        } else {
            float totalWidth = 0.0F;
            if (c != null || needWidth) {
                this.mCachedFontExtent.reset();
                if (this.mCharsValid) {
                    totalWidth = ShapedText.doLayoutRun(this.mChars, contextStart, contextEnd, start, offset, runIsRtl, wp.getInternalPaint(), this.mCachedFontExtent, this.mBuildCachedPieces);
                } else {
                    int delta = this.mStart;
                    int len = contextEnd - contextStart;
                    char[] buf = TextUtils.obtain(len);
                    TextUtils.getChars(this.mText, contextStart + delta, contextEnd + delta, buf, 0);
                    totalWidth = ShapedText.doLayoutRun(buf, 0, len, start - contextStart, offset - contextStart, runIsRtl, wp.getInternalPaint(), this.mCachedFontExtent, this.mBuildCachedPieces);
                    TextUtils.recycle(buf);
                }
            }
            if (c != null) {
                float leftX;
                float rightX;
                if (runIsRtl) {
                    leftX = x - totalWidth;
                    rightX = x;
                } else {
                    leftX = x;
                    rightX = x + totalWidth;
                }
                Paint paint = null;
                if (wp.bgColor != 0) {
                    paint = Paint.obtain();
                    paint.setColor(wp.bgColor);
                    paint.setStyle(0);
                    c.drawRect(leftX, (float) top, rightX, (float) bottom, paint);
                }
                ArrayList<LayoutPiece> pieces = this.mCachedPieces;
                FloatArrayList xOffsets = this.mCachedXOffsets;
                assert pieces.size() == xOffsets.size();
                int i = 0;
                for (int count = pieces.size(); i < count; i++) {
                    TextUtils.drawTextRun(c, (LayoutPiece) pieces.get(i), leftX + xOffsets.getFloat(i), (float) (y + wp.baselineShift), wp);
                }
                if (flags != 0) {
                    if (paint == null) {
                        paint = Paint.obtain();
                    } else {
                        paint.reset();
                    }
                    if ((flags & 1024) != 0) {
                        float thickness = (float) wp.getFontSize() / 18.0F;
                        float strokeTop = (float) y + (float) wp.getFontSize() * 0.11111111F - thickness * 0.5F + (float) wp.baselineShift;
                        paint.setColor(wp.getColor());
                        c.drawRect(leftX, strokeTop, rightX, strokeTop + thickness, paint);
                    }
                    if ((flags & 2048) != 0) {
                        float thickness = (float) wp.getFontSize() / 18.0F;
                        float strokeTop = (float) y - (float) wp.getFontSize() * 0.33333334F - thickness * 0.5F + (float) wp.baselineShift;
                        paint.setColor(wp.getColor());
                        c.drawRect(leftX, strokeTop, rightX, strokeTop + thickness, paint);
                    }
                }
                if (paint != null) {
                    paint.recycle();
                }
            }
            this.mCachedPieces.clear();
            this.mCachedXOffsets.clear();
            return runIsRtl ? -totalWidth : totalWidth;
        }
    }

    private float shapeTextRun(TextShaper.GlyphsConsumer consumer, TextPaint paint, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, float x) {
        int count = end - start;
        int contextCount = contextEnd - contextStart;
        ShapedText glyphs;
        if (this.mCharsValid) {
            glyphs = TextShaper.shapeTextRun(this.mChars, start, count, contextStart, contextCount, runIsRtl, paint);
        } else {
            glyphs = TextShaper.shapeTextRun(this.mText, this.mStart + start, count, this.mStart + contextStart, contextCount, runIsRtl, paint);
        }
        float totalWidth = glyphs.getAdvance();
        if (runIsRtl) {
            x -= totalWidth;
        }
        consumer.accept(start, count, glyphs, paint, x, 0.0F);
        return runIsRtl ? -totalWidth : totalWidth;
    }

    private float handleReplacement(@NonNull ReplacementSpan replacement, @NonNull TextPaint wp, int start, int limit, boolean runIsRtl, @Nullable Canvas canvas, float x, int top, int y, int bottom, @Nullable FontMetricsInt fmi, boolean needWidth) {
        float ret = 0.0F;
        int textStart = this.mStart + start;
        int textLimit = this.mStart + limit;
        if (needWidth || canvas != null && runIsRtl) {
            int previousAscent = 0;
            int previousDescent = 0;
            boolean needUpdateMetrics = fmi != null;
            if (needUpdateMetrics) {
                previousAscent = fmi.ascent;
                previousDescent = fmi.descent;
            }
            ret = (float) replacement.getSize(wp, this.mText, textStart, textLimit, fmi);
            if (needUpdateMetrics) {
                fmi.extendBy(previousAscent, previousDescent);
            }
        }
        if (canvas != null) {
            if (runIsRtl) {
                x -= ret;
            }
            replacement.draw(canvas, this.mText, textStart, textLimit, x, top, y, bottom, wp);
        }
        return runIsRtl ? -ret : ret;
    }

    public float nextTab(float h) {
        return this.mTabs != null ? this.mTabs.nextTab(h) : TabStops.nextDefaultStop(h, 20.0F);
    }

    private static void expandMetricsFromPaint(@NonNull FontMetricsInt fmi, @NonNull TextPaint wp) {
        int previousAscent = fmi.ascent;
        int previousDescent = fmi.descent;
        int previousLeading = fmi.leading;
        wp.getFontMetricsInt(fmi);
        fmi.extendBy(previousAscent, previousDescent);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }

    private void expandMetricsFromPaint(@NonNull TextPaint wp, int start, int end, int contextStart, int contextEnd, boolean runIsRtl, @NonNull FontMetricsInt fmi) {
        int previousAscent = fmi.ascent;
        int previousDescent = fmi.descent;
        int previousLeading = fmi.leading;
        if (this.mComputed == null) {
            wp.getFontMetricsInt(fmi);
        } else {
            this.mComputed.getFontMetricsInt(this.mStart + start, this.mStart + end, fmi);
        }
        fmi.extendBy(previousAscent, previousDescent);
        fmi.leading = Math.max(fmi.leading, previousLeading);
    }
}