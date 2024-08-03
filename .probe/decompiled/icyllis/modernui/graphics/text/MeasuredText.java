package icyllis.modernui.graphics.text;

import icyllis.modernui.annotation.FloatRange;
import icyllis.modernui.annotation.IntRange;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.text.TextPaint;
import icyllis.modernui.util.AlgorithmUtils;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeasuredText {

    private final char[] mTextBuf;

    private final MeasuredText.Run[] mRuns;

    private MeasuredText.Run mLastSeenRun;

    private int mLastSeenRunIndex;

    private MeasuredText(@NonNull char[] textBuf, @NonNull MeasuredText.Run[] runs, boolean computeLayout) {
        this.mTextBuf = textBuf;
        this.mRuns = runs;
        if (runs.length > 0) {
            this.mLastSeenRun = runs[0];
            for (MeasuredText.Run run : runs) {
                run.measure(textBuf, computeLayout);
            }
        } else {
            this.mLastSeenRunIndex = -1;
        }
    }

    @NonNull
    public char[] getTextBuf() {
        return this.mTextBuf;
    }

    @NonNull
    public MeasuredText.Run[] getRuns() {
        return this.mRuns;
    }

    public void getExtent(int start, int end, @NonNull FontMetricsInt extent) {
        if (start < end) {
            MeasuredText.Run run = this.memoizedSearch(start);
            int index = this.mLastSeenRunIndex;
            while (true) {
                if (start < run.mEnd && end > run.mStart) {
                    run.getExtent(this.mTextBuf, Math.max(start, run.mStart), Math.min(end, run.mEnd), extent);
                }
                if (run.mEnd >= end) {
                    return;
                }
                run = this.mRuns[++index];
            }
        }
    }

    public float getAdvance(int pos) {
        MeasuredText.Run run = this.memoizedSearch(pos);
        return run.getAdvance(this.mTextBuf, pos);
    }

    public float getAdvance(int start, int end) {
        if (start >= end) {
            return 0.0F;
        } else {
            float advance = 0.0F;
            MeasuredText.Run run = this.memoizedSearch(start);
            int index = this.mLastSeenRunIndex;
            while (true) {
                if (start < run.mEnd && end > run.mStart) {
                    advance += run.getAdvance(this.mTextBuf, Math.max(start, run.mStart), Math.min(end, run.mEnd));
                }
                if (run.mEnd >= end) {
                    return advance;
                }
                run = this.mRuns[++index];
            }
        }
    }

    @NonNull
    private MeasuredText.Run memoizedSearch(int pos) {
        MeasuredText.Run run = this.mLastSeenRun;
        if (pos >= run.mStart && pos < run.mEnd) {
            return run;
        } else {
            int index = this.mLastSeenRunIndex;
            MeasuredText.Run[] runs = this.mRuns;
            if (index < runs.length - 1) {
                run = runs[index + 1];
                if (pos >= run.mStart && pos < run.mEnd) {
                    this.mLastSeenRun = run;
                    this.mLastSeenRunIndex++;
                    return run;
                }
            }
            if (index > 0) {
                run = runs[index - 1];
                if (pos >= run.mStart && pos < run.mEnd) {
                    this.mLastSeenRun = run;
                    this.mLastSeenRunIndex--;
                    return run;
                }
            }
            int low = 0;
            int high = runs.length - 1;
            while (low <= high) {
                int mid = low + high >>> 1;
                run = runs[mid];
                if (run.mEnd <= pos) {
                    low = mid + 1;
                } else {
                    if (run.mStart <= pos) {
                        this.mLastSeenRun = run;
                        this.mLastSeenRunIndex = mid;
                        return run;
                    }
                    high = mid - 1;
                }
            }
            throw new IndexOutOfBoundsException(-(low + 1));
        }
    }

    @Nullable
    public MeasuredText.Run searchRun(int pos) {
        if (pos >= 0 && pos < this.mTextBuf.length) {
            int low = 0;
            int high = this.mRuns.length - 1;
            while (low <= high) {
                int mid = low + high >>> 1;
                MeasuredText.Run run = this.mRuns[mid];
                if (run.mEnd <= pos) {
                    low = mid + 1;
                } else {
                    if (run.mStart <= pos) {
                        return run;
                    }
                    high = mid - 1;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public int getMemoryUsage() {
        int size = 44;
        for (MeasuredText.Run run : this.mRuns) {
            size += run.getMemoryUsage();
        }
        return size;
    }

    public String toString() {
        return "MeasuredText{" + (String) Arrays.stream(this.mRuns).map(Objects::toString).collect(Collectors.joining("\n")) + "}";
    }

    public static class Builder {

        private final List<MeasuredText.Run> mRuns = new ArrayList();

        @NonNull
        private final char[] mText;

        private boolean mComputeLayout = true;

        private int mCurrentOffset = 0;

        public Builder(@NonNull char[] text) {
            Objects.requireNonNull(text);
            this.mText = text;
        }

        @NonNull
        public MeasuredText.Builder appendStyleRun(@NonNull TextPaint paint, @IntRange(from = 0L) int length, boolean isRtl) {
            this.addStyleRun(paint.createInternalPaint(), null, length, isRtl);
            return this;
        }

        @NonNull
        public MeasuredText.Builder appendStyleRun(@NonNull TextPaint paint, @Nullable LineBreakConfig lineBreakConfig, @IntRange(from = 0L) int length, boolean isRtl) {
            this.addStyleRun(paint.createInternalPaint(), lineBreakConfig, length, isRtl);
            return this;
        }

        public void addStyleRun(@NonNull FontPaint paint, @Nullable LineBreakConfig lineBreakConfig, @IntRange(from = 0L) int length, boolean isRtl) {
            Objects.requireNonNull(paint);
            if (length <= 0) {
                throw new IllegalArgumentException("length can not be negative");
            } else {
                int end = this.mCurrentOffset + length;
                if (end > this.mText.length) {
                    throw new IllegalArgumentException("Style exceeds the text length");
                } else {
                    int lbStyle = lineBreakConfig != null ? lineBreakConfig.getLineBreakStyle() : 0;
                    int lbWordStyle = lineBreakConfig != null ? lineBreakConfig.getLineBreakWordStyle() : 0;
                    this.mRuns.add(new MeasuredText.StyleRun(this.mCurrentOffset, end, paint, lbStyle, lbWordStyle, isRtl));
                    this.mCurrentOffset = end;
                }
            }
        }

        @NonNull
        public MeasuredText.Builder appendReplacementRun(@NonNull TextPaint paint, @IntRange(from = 0L) int length, @FloatRange(from = 0.0) float width) {
            this.addReplacementRun(paint.getTextLocale(), length, width);
            return this;
        }

        public void addReplacementRun(@NonNull Locale locale, @IntRange(from = 0L) int length, @FloatRange(from = 0.0) float width) {
            if (length <= 0) {
                throw new IllegalArgumentException("length can not be negative");
            } else {
                int end = this.mCurrentOffset + length;
                if (end > this.mText.length) {
                    throw new IllegalArgumentException("Replacement exceeds the text length");
                } else {
                    this.mRuns.add(new MeasuredText.ReplacementRun(this.mCurrentOffset, end, width, locale));
                    this.mCurrentOffset = end;
                }
            }
        }

        @NonNull
        public MeasuredText.Builder setComputeLayout(boolean computeLayout) {
            this.mComputeLayout = computeLayout;
            return this;
        }

        @NonNull
        public MeasuredText build() {
            if (this.mCurrentOffset < 0) {
                throw new IllegalStateException("Builder can not be reused.");
            } else if (this.mCurrentOffset != this.mText.length) {
                throw new IllegalStateException("Style info has not been provided for all text.");
            } else {
                this.mCurrentOffset = -1;
                return new MeasuredText(this.mText, (MeasuredText.Run[]) this.mRuns.toArray(new MeasuredText.Run[0]), this.mComputeLayout);
            }
        }
    }

    public static class ReplacementRun extends MeasuredText.Run {

        private final float mWidth;

        private final Locale mLocale;

        private ReplacementRun(int start, int end, float width, Locale locale) {
            super(start, end);
            this.mWidth = width;
            this.mLocale = locale;
        }

        @Override
        public void measure(@NonNull char[] text, boolean computeLayout) {
        }

        @Override
        public void getExtent(@NonNull char[] text, int start, int end, @NonNull FontMetricsInt extent) {
        }

        @Override
        public float getAdvance(char[] text, int pos) {
            assert pos >= this.mStart && pos < this.mEnd;
            return pos == this.mStart ? this.mWidth : 0.0F;
        }

        @Override
        public float getAdvance(char[] text, int start, int end) {
            assert start >= this.mStart && end <= this.mEnd && start < end;
            return start == this.mStart ? this.mWidth : 0.0F;
        }

        @Override
        public boolean isRtl() {
            return false;
        }

        @Override
        public boolean canBreak() {
            return false;
        }

        @NonNull
        @Override
        public Locale getLocale() {
            return this.mLocale;
        }

        @Override
        public int getLineBreakStyle() {
            return 0;
        }

        @Override
        public int getLineBreakWordStyle() {
            return 0;
        }

        @Override
        public int getMemoryUsage() {
            return 32;
        }

        public String toString() {
            return "ReplacementRun{mStart=" + this.mStart + ", mEnd=" + this.mEnd + ", mWidth=" + this.mWidth + ", mLocale=" + this.mLocale + "}";
        }
    }

    public abstract static class Run {

        public final int mStart;

        public final int mEnd;

        private Run(int start, int end) {
            this.mStart = start;
            this.mEnd = end;
        }

        public abstract void measure(@NonNull char[] var1, boolean var2);

        public abstract void getExtent(@NonNull char[] var1, int var2, int var3, @NonNull FontMetricsInt var4);

        public abstract float getAdvance(char[] var1, int var2);

        public abstract float getAdvance(char[] var1, int var2, int var3);

        public abstract boolean isRtl();

        public abstract boolean canBreak();

        @NonNull
        public abstract Locale getLocale();

        public abstract int getLineBreakStyle();

        public abstract int getLineBreakWordStyle();

        public abstract int getMemoryUsage();
    }

    public static class StyleRun extends MeasuredText.Run {

        private final FontPaint mPaint;

        private final int mLineBreakStyle;

        private final int mLineBreakWordStyle;

        private final boolean mIsRtl;

        private int[] mOffsets;

        private LayoutPiece[] mPieces;

        private float mAdvance;

        StyleRun(int start, int end, FontPaint paint, int lineBreakStyle, int lineBreakWordStyle, boolean isRtl) {
            super(start, end);
            this.mPaint = paint;
            this.mLineBreakStyle = lineBreakStyle;
            this.mLineBreakWordStyle = lineBreakWordStyle;
            this.mIsRtl = isRtl;
        }

        @Override
        public void measure(@NonNull char[] text, boolean computeLayout) {
            ArrayList<LayoutPiece> pieces = new ArrayList();
            IntArrayList offsets = new IntArrayList();
            int[] offset = new int[] { this.mIsRtl ? this.mEnd : this.mStart };
            this.mAdvance = ShapedText.doLayoutRun(text, this.mStart, this.mEnd, this.mStart, this.mEnd, this.mIsRtl, this.mPaint, null, (piece, __) -> {
                pieces.add(piece);
                if (this.mIsRtl) {
                    offsets.add(offset[0]);
                    offset[0] -= piece.getCharCount();
                } else {
                    offset[0] += piece.getCharCount();
                    offsets.add(offset[0]);
                }
            });
            assert offset[0] == (this.mIsRtl ? this.mStart : this.mEnd);
            if (this.mIsRtl) {
                Collections.reverse(pieces);
            }
            this.mPieces = (LayoutPiece[]) pieces.toArray(new LayoutPiece[0]);
            this.mOffsets = offsets.toIntArray();
            if (this.mIsRtl) {
                Arrays.sort(this.mOffsets);
            }
        }

        @Override
        public void getExtent(@NonNull char[] text, int start, int end, @NonNull FontMetricsInt extent) {
            int[] offsets = this.mOffsets;
            LayoutPiece[] pieces = this.mPieces;
            int i;
            int itContextStart;
            int itContextEnd;
            if (start < offsets[0]) {
                i = 0;
                itContextStart = this.mStart;
                itContextEnd = offsets[0];
            } else {
                i = AlgorithmUtils.higher(offsets, start);
                itContextStart = offsets[i - 1];
                itContextEnd = i == offsets.length ? this.mEnd : offsets[i];
            }
            while (true) {
                int itPieceStart = Math.max(itContextStart, start);
                int itPieceEnd = Math.min(itContextEnd, end);
                if (itPieceStart == itContextStart && itPieceEnd == itContextEnd) {
                    pieces[i].getExtent(extent);
                } else {
                    LayoutCache.getOrCreate(text, itContextStart, itContextEnd, itPieceStart, itPieceEnd, this.mIsRtl, this.mPaint, 0).getExtent(extent);
                }
                if (itPieceEnd == end) {
                    return;
                }
                itContextStart = itContextEnd;
                itContextEnd = offsets[++i];
            }
        }

        @Override
        public float getAdvance(char[] text, int pos) {
            int[] offsets = this.mOffsets;
            LayoutPiece[] pieces = this.mPieces;
            int i;
            int itContextStart;
            int itContextEnd;
            if (pos < offsets[0]) {
                i = 0;
                itContextStart = this.mStart;
                itContextEnd = offsets[0];
            } else {
                i = AlgorithmUtils.higher(offsets, pos);
                itContextStart = offsets[i - 1];
                itContextEnd = i == offsets.length ? this.mEnd : offsets[i];
            }
            LayoutPiece piece = pieces[i];
            if ((piece.getComputeFlags() & 1) == 0) {
                pieces[i] = piece = LayoutCache.getOrCreate(text, itContextStart, itContextEnd, itContextStart, itContextEnd, this.mIsRtl, this.mPaint, 1);
            }
            return piece.getAdvances()[pos - itContextStart];
        }

        @Override
        public float getAdvance(char[] text, int start, int end) {
            if (start == this.mStart && end == this.mEnd) {
                return this.mAdvance;
            } else {
                int[] offsets = this.mOffsets;
                LayoutPiece[] pieces = this.mPieces;
                int i;
                int itContextStart;
                int itContextEnd;
                if (start < offsets[0]) {
                    i = 0;
                    itContextStart = this.mStart;
                    itContextEnd = offsets[0];
                } else {
                    i = AlgorithmUtils.higher(offsets, start);
                    itContextStart = offsets[i - 1];
                    itContextEnd = i == offsets.length ? this.mEnd : offsets[i];
                }
                float advance = 0.0F;
                while (true) {
                    int itPieceStart = Math.max(itContextStart, start);
                    int itPieceEnd = Math.min(itContextEnd, end);
                    if (itPieceStart == itContextStart && itPieceEnd == itContextEnd) {
                        advance += pieces[i].getAdvance();
                    } else {
                        LayoutPiece piece = pieces[i];
                        if ((piece.getComputeFlags() & 1) == 0) {
                            pieces[i] = piece = LayoutCache.getOrCreate(text, itContextStart, itContextEnd, itContextStart, itContextEnd, this.mIsRtl, this.mPaint, 1);
                        }
                        for (int j = itPieceStart; j < itPieceEnd; j++) {
                            advance += piece.getAdvances()[j - itContextStart];
                        }
                    }
                    if (itPieceEnd == end) {
                        return advance;
                    }
                    itContextStart = itContextEnd;
                    itContextEnd = offsets[++i];
                }
            }
        }

        @Override
        public boolean isRtl() {
            return this.mIsRtl;
        }

        @Override
        public boolean canBreak() {
            return true;
        }

        @NonNull
        @Override
        public Locale getLocale() {
            return this.mPaint.getLocale();
        }

        @Override
        public int getLineBreakStyle() {
            return this.mLineBreakStyle;
        }

        @Override
        public int getLineBreakWordStyle() {
            return this.mLineBreakWordStyle;
        }

        @Override
        public int getMemoryUsage() {
            int size = 80;
            size += this.mOffsets.length << 2;
            for (LayoutPiece piece : this.mPieces) {
                size += piece.getMemoryUsage();
            }
            return size;
        }

        public String toString() {
            return "StyleRun{mPaint=" + this.mPaint + ", mLineBreakStyle=" + this.mLineBreakStyle + ", mLineBreakWordStyle=" + this.mLineBreakWordStyle + ", mIsRtl=" + this.mIsRtl + ", mAdvance=" + this.mAdvance + ", mStart=" + this.mStart + ", mEnd=" + this.mEnd + "}";
        }
    }
}