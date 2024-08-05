package icyllis.modernui.text;

import com.ibm.icu.text.Bidi;
import icyllis.modernui.annotation.IntRange;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.FontPaint;
import icyllis.modernui.graphics.text.LineBreakConfig;
import icyllis.modernui.graphics.text.MeasuredText;
import icyllis.modernui.text.style.MetricAffectingSpan;
import icyllis.modernui.text.style.ReplacementSpan;
import icyllis.modernui.util.Pools;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.concurrent.NotThreadSafe;

@NotThreadSafe
public class MeasuredParagraph {

    private static final Pools.Pool<MeasuredParagraph> sPool = Pools.newSynchronizedPool(1);

    @Nullable
    private Spanned mSpanned;

    private int mTextStart;

    private char[] mCopiedBuffer;

    private int mParaDir;

    @Nullable
    private byte[] mLevels;

    @NonNull
    private final IntArrayList mSpanEndCache = new IntArrayList();

    @NonNull
    private final IntArrayList mFontMetrics = new IntArrayList();

    @Nullable
    private MeasuredText mMeasuredText;

    @NonNull
    private final FontMetricsInt mCachedFm = new FontMetricsInt();

    private MeasuredParagraph() {
    }

    public void release() {
        this.reset();
        this.mSpanEndCache.trim();
        this.mFontMetrics.trim();
    }

    private void reset() {
        this.mSpanned = null;
        this.mCopiedBuffer = null;
        this.mLevels = null;
        this.mSpanEndCache.clear();
        this.mFontMetrics.clear();
        this.mMeasuredText = null;
    }

    public int getTextStart() {
        return this.mTextStart;
    }

    public int getTextLength() {
        return this.mCopiedBuffer.length;
    }

    @NonNull
    public char[] getChars() {
        return this.mCopiedBuffer;
    }

    public int getParagraphDir() {
        return this.mParaDir;
    }

    @NonNull
    public Directions getDirections(int start, int end) {
        if (start != end && this.mLevels != null) {
            int baseLevel = this.mParaDir == 1 ? 0 : 1;
            byte[] levels = this.mLevels;
            int curLevel = levels[start];
            int minLevel = curLevel;
            int runCount = 1;
            for (int i = start + 1; i < end; i++) {
                int level = levels[i];
                if (level != curLevel) {
                    curLevel = level;
                    runCount++;
                }
            }
            int visLen = end - start;
            if ((curLevel & 1) != (baseLevel & 1)) {
                while (--visLen >= 0) {
                    char ch = this.mCopiedBuffer[start + visLen];
                    if (ch == '\n') {
                        visLen--;
                        break;
                    }
                    if (ch != ' ' && ch != '\t') {
                        break;
                    }
                }
                if (++visLen != end - start) {
                    runCount++;
                }
            }
            if (runCount != 1 || minLevel != baseLevel) {
                int[] ld = new int[runCount * 2];
                int maxLevel = minLevel;
                int levelBits = minLevel << 26;
                int n = 1;
                int prev = start;
                curLevel = minLevel;
                int ix = start;
                for (int e = start + visLen; ix < e; ix++) {
                    int level = levels[ix];
                    if (level != curLevel) {
                        curLevel = level;
                        if (level > maxLevel) {
                            maxLevel = level;
                        } else if (level < minLevel) {
                            minLevel = level;
                        }
                        ld[n++] = ix - prev | levelBits;
                        ld[n++] = ix - start;
                        levelBits = level << 26;
                        prev = ix;
                    }
                }
                ld[n] = start + visLen - prev | levelBits;
                if (visLen < end - start) {
                    n++;
                    ld[n] = visLen;
                    n++;
                    ld[n] = end - start - visLen | baseLevel << 26;
                }
                boolean swap;
                if ((minLevel & 1) == baseLevel) {
                    minLevel++;
                    swap = maxLevel > minLevel;
                } else {
                    swap = runCount > 1;
                }
                if (swap) {
                    for (int level = maxLevel - 1; level >= minLevel; level--) {
                        for (int ixx = 0; ixx < ld.length; ixx += 2) {
                            if (levels[ld[ixx]] >= level) {
                                int ex = ixx + 2;
                                while (ex < ld.length && levels[ld[ex]] >= level) {
                                    ex += 2;
                                }
                                int low = ixx;
                                for (int hi = ex - 2; low < hi; hi -= 2) {
                                    int x = ld[low];
                                    ld[low] = ld[hi];
                                    ld[hi] = x;
                                    x = ld[low + 1];
                                    ld[low + 1] = ld[hi + 1];
                                    ld[hi + 1] = x;
                                    low += 2;
                                }
                                ixx = ex + 2;
                            }
                        }
                    }
                }
                return new Directions(ld);
            } else {
                return (minLevel & 1) != 0 ? Directions.ALL_RIGHT_TO_LEFT : Directions.ALL_LEFT_TO_RIGHT;
            }
        } else {
            return Directions.ALL_LEFT_TO_RIGHT;
        }
    }

    @NonNull
    public IntArrayList getSpanEndCache() {
        return this.mSpanEndCache;
    }

    @NonNull
    public IntArrayList getFontMetrics() {
        return this.mFontMetrics;
    }

    @Nullable
    public MeasuredText getMeasuredText() {
        return this.mMeasuredText;
    }

    public float getAdvance(int offset) {
        return this.mMeasuredText == null ? 0.0F : this.mMeasuredText.getAdvance(offset);
    }

    public float getAdvance(int start, int end) {
        return this.mMeasuredText == null ? 0.0F : this.mMeasuredText.getAdvance(start, end);
    }

    public void getExtent(@IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull FontMetricsInt fmi) {
        if (this.mMeasuredText != null) {
            this.mMeasuredText.getExtent(start, end, fmi);
        }
    }

    @NonNull
    private static MeasuredParagraph obtain() {
        MeasuredParagraph c = sPool.acquire();
        return c == null ? new MeasuredParagraph() : c;
    }

    @NonNull
    public static MeasuredParagraph buildForBidi(@NonNull CharSequence text, int start, int end, @NonNull TextDirectionHeuristic textDir, @Nullable MeasuredParagraph recycle) {
        if ((start | end | end - start | text.length() - end) < 0) {
            throw new IllegalArgumentException();
        } else {
            MeasuredParagraph c = recycle == null ? obtain() : recycle;
            c.resetAndAnalyzeBidi(text, start, end, textDir);
            return c;
        }
    }

    @NonNull
    public static MeasuredParagraph buildForStaticLayout(@NonNull TextPaint paint, @Nullable LineBreakConfig lineBreakConfig, @NonNull CharSequence text, @IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull TextDirectionHeuristic textDir, boolean fullLayout, @Nullable MeasuredParagraph recycle) {
        if ((start | end | end - start | text.length() - end) < 0) {
            throw new IllegalArgumentException();
        } else {
            MeasuredParagraph c = recycle == null ? obtain() : recycle;
            c.resetAndAnalyzeBidi(text, start, end, textDir);
            if (end > start) {
                MeasuredText.Builder builder = new MeasuredText.Builder(c.mCopiedBuffer).setComputeLayout(fullLayout);
                if (c.mSpanned == null) {
                    c.applyMetricsAffectingSpan(paint, lineBreakConfig, Collections.emptyList(), start, end, builder);
                    c.mSpanEndCache.add(end);
                } else {
                    int spanStart = start;
                    while (spanStart < end) {
                        int spanEnd = c.mSpanned.nextSpanTransition(spanStart, end, MetricAffectingSpan.class);
                        List<MetricAffectingSpan> spans = c.mSpanned.getSpans(spanStart, spanEnd, MetricAffectingSpan.class);
                        spans = TextUtils.removeEmptySpans(spans, c.mSpanned);
                        c.applyMetricsAffectingSpan(paint, lineBreakConfig, spans, spanStart, spanEnd, builder);
                        c.mSpanEndCache.add(spanEnd);
                        spanStart = spanEnd;
                    }
                }
                c.mMeasuredText = builder.build();
            }
            return c;
        }
    }

    private void resetAndAnalyzeBidi(@NonNull CharSequence text, int start, int end, @NonNull TextDirectionHeuristic dir) {
        this.reset();
        this.mSpanned = text instanceof Spanned ? (Spanned) text : null;
        this.mTextStart = start;
        int length = end - start;
        if (this.mCopiedBuffer == null || this.mCopiedBuffer.length != length) {
            this.mCopiedBuffer = new char[length];
        }
        TextUtils.getChars(text, start, end, this.mCopiedBuffer, 0);
        if (this.mSpanned != null) {
            for (ReplacementSpan span : this.mSpanned.getSpans(start, end, ReplacementSpan.class)) {
                int startInPara = this.mSpanned.getSpanStart(span) - start;
                int endInPara = this.mSpanned.getSpanEnd(span) - start;
                if (startInPara < 0) {
                    startInPara = 0;
                }
                if (endInPara > length) {
                    endInPara = length;
                }
                Arrays.fill(this.mCopiedBuffer, startInPara, endInPara, '￼');
            }
        }
        if ((dir == TextDirectionHeuristics.LTR || dir == TextDirectionHeuristics.FIRSTSTRONG_LTR || dir == TextDirectionHeuristics.ANYRTL_LTR) && !Bidi.requiresBidi(this.mCopiedBuffer, 0, length)) {
            this.mLevels = null;
            this.mParaDir = 1;
        } else {
            byte paraLevel;
            if (dir == TextDirectionHeuristics.LTR) {
                paraLevel = 0;
            } else if (dir == TextDirectionHeuristics.RTL) {
                paraLevel = 1;
            } else if (dir == TextDirectionHeuristics.FIRSTSTRONG_LTR) {
                paraLevel = 126;
            } else if (dir == TextDirectionHeuristics.FIRSTSTRONG_RTL) {
                paraLevel = 127;
            } else {
                boolean isRtl = dir.isRtl(this.mCopiedBuffer, 0, length);
                paraLevel = (byte) (isRtl ? 1 : 0);
            }
            Bidi bidi = new Bidi(length, 0);
            bidi.setPara(this.mCopiedBuffer, paraLevel, null);
            this.mLevels = bidi.getLevels();
            this.mParaDir = (bidi.getParaLevel() & 1) == 0 ? 1 : -1;
        }
    }

    private void applyMetricsAffectingSpan(@NonNull TextPaint paint, @Nullable LineBreakConfig lineBreakConfig, @NonNull List<MetricAffectingSpan> spans, @IntRange(from = 0L) int start, @IntRange(from = 0L) int end, @NonNull MeasuredText.Builder builder) {
        assert start != end;
        TextPaint tp = TextPaint.obtain();
        tp.set(paint);
        tp.baselineShift = 0;
        ReplacementSpan replacement = null;
        for (MetricAffectingSpan span : spans) {
            if (span instanceof ReplacementSpan) {
                replacement = (ReplacementSpan) span;
            } else {
                span.updateMeasureState(tp);
            }
        }
        tp.getFontMetricsInt(this.mCachedFm);
        if (replacement != null) {
            float width = (float) replacement.getSize(tp, this.mSpanned, start + this.mTextStart, end + this.mTextStart, this.mCachedFm);
            builder.addReplacementRun(tp.getTextLocale(), end - start, width);
        } else {
            int offset = this.mTextStart;
            FontPaint base = tp.createInternalPaint();
            this.applyStyleRun(base, start - offset, end - offset, lineBreakConfig, builder);
        }
        if (tp.baselineShift < 0) {
            this.mCachedFm.ascent = this.mCachedFm.ascent + tp.baselineShift;
        } else {
            this.mCachedFm.descent = this.mCachedFm.descent + tp.baselineShift;
        }
        this.mFontMetrics.add(this.mCachedFm.ascent);
        this.mFontMetrics.add(this.mCachedFm.descent);
        tp.recycle();
    }

    private void applyStyleRun(@NonNull FontPaint paint, int start, int end, @Nullable LineBreakConfig config, @NonNull MeasuredText.Builder builder) {
        if (this.mLevels == null) {
            builder.addStyleRun(paint, config, end - start, false);
        } else {
            byte level = this.mLevels[start];
            int levelStart = start;
            int levelEnd = start + 1;
            while (true) {
                if (levelEnd == end || this.mLevels[levelEnd] != level) {
                    boolean isRtl = (level & 1) != 0;
                    builder.addStyleRun(paint, config, levelEnd - levelStart, isRtl);
                    if (levelEnd == end) {
                        break;
                    }
                    levelStart = levelEnd;
                    level = this.mLevels[levelEnd];
                }
                levelEnd++;
            }
        }
    }

    int breakText(int limit, boolean forwards, float width) {
        MeasuredText mt = this.mMeasuredText;
        assert mt != null;
        if (forwards) {
            int i;
            for (i = 0; i < limit; i++) {
                width -= mt.getAdvance(i);
                if (width < 0.0F) {
                    break;
                }
            }
            while (i > 0 && this.mCopiedBuffer[i - 1] == ' ') {
                i--;
            }
            return i;
        } else {
            int i;
            for (i = limit - 1; i >= 0; i--) {
                width -= mt.getAdvance(i);
                if (width < 0.0F) {
                    break;
                }
            }
            while (i < limit - 1 && (this.mCopiedBuffer[i + 1] == ' ' || mt.getAdvance(i + 1) == 0.0F)) {
                i++;
            }
            return limit - i - 1;
        }
    }

    public void recycle() {
        this.release();
        sPool.release(this);
    }

    public int getMemoryUsage() {
        return MathUtil.align8(32 + (this.mCopiedBuffer == null ? 0 : 16 + (this.mCopiedBuffer.length << 1)) + 4 + (this.mLevels == null ? 0 : 16 + this.mLevels.length) + 16 + (this.mSpanEndCache.size() << 2) + 16 + (this.mFontMetrics.size() << 2) + 8) + (this.mMeasuredText == null ? 0 : this.mMeasuredText.getMemoryUsage());
    }
}