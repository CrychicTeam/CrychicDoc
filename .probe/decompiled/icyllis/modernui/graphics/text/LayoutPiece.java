package icyllis.modernui.graphics.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Rect;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import javax.annotation.concurrent.Immutable;

@Immutable
public final class LayoutPiece {

    private final int[] mGlyphs;

    private final float[] mPositions;

    private final byte[] mFontIndices;

    private final Font[] mFonts;

    private final float[] mAdvances;

    private final int mAscent;

    private final int mDescent;

    private final float mAdvance;

    private final int mBoundsX;

    private final int mBoundsY;

    private final int mBoundsWidth;

    private final int mBoundsHeight;

    private final int mNumChars;

    final int mComputeFlags;

    LayoutPiece(@NonNull char[] buf, int contextStart, int contextLimit, int start, int limit, boolean isRtl, @NonNull FontPaint paint, @Nullable LayoutPiece hint, int newFlags) {
        boolean computeAdvances = (newFlags & 1) != 0;
        boolean computeBounds = (newFlags & 2) != 0;
        int count = limit - start;
        if (computeAdvances) {
            assert hint == null || hint.mAdvances == null;
            this.mAdvances = new float[count];
        } else if (hint != null) {
            this.mAdvances = hint.mAdvances;
        } else {
            this.mAdvances = null;
        }
        FontMetricsInt extent = new FontMetricsInt();
        Rect bounds = new Rect();
        ByteArrayList fontIndices;
        IntArrayList glyphs;
        FloatArrayList positions;
        if (hint == null) {
            fontIndices = new ByteArrayList(count);
            glyphs = new IntArrayList(count);
            positions = new FloatArrayList(count * 2);
        } else {
            fontIndices = null;
            glyphs = null;
            positions = null;
        }
        ArrayList<Font> fontVec;
        HashMap<Font, Byte> fontMap;
        Function<Font, Byte> nextID;
        if (hint == null) {
            fontVec = new ArrayList();
            fontMap = new HashMap();
            nextID = fontx -> {
                fontVec.add(fontx);
                return (byte) fontMap.size();
            };
        } else {
            fontVec = null;
            fontMap = null;
            nextID = null;
        }
        int style = paint.getFontStyle();
        float advance = 0.0F;
        List<FontCollection.Run> items = paint.mFont.itemize(buf, start, limit);
        int runIndex;
        int runLimit;
        int runDir;
        if (isRtl) {
            runIndex = items.size() - 1;
            runLimit = -1;
            runDir = -1;
        } else {
            runIndex = 0;
            runLimit = items.size();
            runDir = 1;
        }
        while (runIndex != runLimit) {
            FontCollection.Run run = (FontCollection.Run) items.get(runIndex);
            byte fontIdx = -1;
            int oldGlyphSize = 0;
            Font font = run.getBestFont(buf, style);
            if (hint == null) {
                fontIdx = (Byte) fontMap.computeIfAbsent(font, nextID);
                font.getMetrics(paint, extent);
                oldGlyphSize = glyphs.size();
            }
            float adv = font.doComplexLayout(buf, contextStart, contextLimit, run.start(), run.limit(), isRtl, paint, glyphs, positions, computeAdvances ? this.mAdvances : null, start, computeBounds ? bounds : null, advance, 0.0F);
            if (hint == null) {
                int newGlyphSize = glyphs.size();
                fontIndices.size(newGlyphSize);
                Arrays.fill(fontIndices.elements(), oldGlyphSize, newGlyphSize, fontIdx);
            }
            advance += adv;
            runIndex += runDir;
        }
        if (hint != null) {
            this.mGlyphs = hint.mGlyphs;
            this.mPositions = hint.mPositions;
            this.mFontIndices = hint.mFontIndices;
            this.mFonts = hint.mFonts;
            this.mAscent = hint.mAscent;
            this.mDescent = hint.mDescent;
            this.mAdvance = hint.mAdvance;
            assert this.mAdvance == advance;
            this.mComputeFlags = hint.mComputeFlags | newFlags;
        } else {
            this.mGlyphs = glyphs.toIntArray();
            this.mPositions = positions.toFloatArray();
            if (fontVec.size() > 1) {
                this.mFontIndices = fontIndices.toByteArray();
            } else {
                this.mFontIndices = null;
            }
            this.mFonts = (Font[]) fontVec.toArray(new Font[0]);
            this.mAscent = extent.ascent;
            this.mDescent = extent.descent;
            this.mAdvance = advance;
            this.mComputeFlags = newFlags;
        }
        if (!computeBounds && hint != null) {
            this.mBoundsX = hint.mBoundsX;
            this.mBoundsY = hint.mBoundsY;
            this.mBoundsWidth = hint.mBoundsWidth;
            this.mBoundsHeight = hint.mBoundsHeight;
        } else {
            this.mBoundsX = bounds.x();
            this.mBoundsY = bounds.y();
            this.mBoundsWidth = bounds.width();
            this.mBoundsHeight = bounds.height();
        }
        this.mNumChars = count;
        assert hint == null || hint.mNumChars == count;
        assert this.mGlyphs.length * 2 == this.mPositions.length;
        assert this.mFontIndices == null || this.mFontIndices.length == this.mGlyphs.length;
    }

    public int getGlyphCount() {
        return this.mGlyphs.length;
    }

    public int[] getGlyphs() {
        return this.mGlyphs;
    }

    public float[] getPositions() {
        return this.mPositions;
    }

    public Font getFont(int i) {
        return this.mFontIndices != null ? this.mFonts[this.mFontIndices[i] & 255] : this.mFonts[0];
    }

    public int getCharCount() {
        return this.mNumChars;
    }

    public float[] getAdvances() {
        return this.mAdvances;
    }

    public void getExtent(@NonNull FontMetricsInt extent) {
        extent.extendBy(this.mAscent, this.mDescent);
    }

    public int getAscent() {
        return this.mAscent;
    }

    public int getDescent() {
        return this.mDescent;
    }

    public float getAdvance() {
        return this.mAdvance;
    }

    public int getBoundsX() {
        return this.mBoundsX;
    }

    public int getBoundsY() {
        return this.mBoundsY;
    }

    public int getBoundsWidth() {
        return this.mBoundsWidth;
    }

    public int getBoundsHeight() {
        return this.mBoundsHeight;
    }

    public int getComputeFlags() {
        return this.mComputeFlags;
    }

    public int getMemoryUsage() {
        int m = 64;
        m += 16 + MathUtil.align8(this.mGlyphs.length << 2);
        m += 16 + MathUtil.align8(this.mPositions.length << 2);
        if (this.mFontIndices != null) {
            m += 16 + MathUtil.align8(this.mFontIndices.length);
        }
        m += 16 + MathUtil.align8(this.mFonts.length << 2);
        if (this.mAdvances != null) {
            m += 16 + MathUtil.align8(this.mAdvances.length << 2);
        }
        return m;
    }

    public String toString() {
        return "LayoutPiece{mGlyphs=" + Arrays.toString(this.mGlyphs) + ", mPositions=" + Arrays.toString(this.mPositions) + ", mFonts=" + Arrays.toString(this.mFonts) + ", mFontIndices=" + Arrays.toString(this.mFontIndices) + ", mAdvances=" + Arrays.toString(this.mAdvances) + ", mAscent=" + this.mAscent + ", mDescent=" + this.mDescent + ", mAdvance=" + this.mAdvance + ", mBoundsX=" + this.mBoundsX + ", mBoundsY=" + this.mBoundsY + ", mBoundsWidth=" + this.mBoundsWidth + ", mBoundsHeight=" + this.mBoundsHeight + ", mComputeFlags=0x" + Integer.toHexString(this.mComputeFlags) + "}";
    }
}