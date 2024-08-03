package icyllis.modernui.graphics.text;

import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.BidiRun;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.MathUtil;
import it.unimi.dsi.fastutil.bytes.ByteArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.floats.FloatArrays;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntArrays;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.concurrent.Immutable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Immutable
public class ShapedText {

    public static final int BIDI_LTR = 0;

    public static final int BIDI_RTL = 1;

    public static final int BIDI_DEFAULT_LTR = 2;

    public static final int BIDI_DEFAULT_RTL = 3;

    public static final int BIDI_OVERRIDE_LTR = 4;

    public static final int BIDI_OVERRIDE_RTL = 5;

    private final int[] mGlyphs;

    private final float[] mPositions;

    private final byte[] mFontIndices;

    private final Font[] mFonts;

    private final float[] mAdvances;

    private final int mAscent;

    private final int mDescent;

    private final float mAdvance;

    public int getGlyphCount() {
        return this.mGlyphs.length;
    }

    public int[] getGlyphs() {
        return this.mGlyphs;
    }

    public int getGlyph(int i) {
        return this.mGlyphs[i];
    }

    public float[] getPositions() {
        return this.mPositions;
    }

    public float getX(int i) {
        return this.mPositions[i << 1];
    }

    public float getY(int i) {
        return this.mPositions[i << 1 | 1];
    }

    public Font getFont(int i) {
        return this.mFontIndices != null ? this.mFonts[this.mFontIndices[i] & 255] : this.mFonts[0];
    }

    @Internal
    public int getCharCount() {
        return this.mAdvances.length;
    }

    @Internal
    public float[] getAdvances() {
        return this.mAdvances;
    }

    @Internal
    public float getAdvance(int i) {
        return i == this.mAdvances.length ? this.mAdvance : this.mAdvances[i];
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

    @Internal
    public int getMemoryUsage() {
        int m = 48;
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
        return "ShapedText{mGlyphs=" + Arrays.toString(this.mGlyphs) + ", mPositions=" + Arrays.toString(this.mPositions) + ", mFonts=" + Arrays.toString(this.mFonts) + ", mFontIndices=" + Arrays.toString(this.mFontIndices) + ", mAdvances=" + Arrays.toString(this.mAdvances) + ", mAscent=" + this.mAscent + ", mDescent=" + this.mDescent + ", mAdvance=" + this.mAdvance + "}";
    }

    public ShapedText(@NonNull char[] text, int contextStart, int contextLimit, int start, int limit, int bidiFlags, @NonNull FontPaint paint) {
        int length = text.length;
        Objects.checkFromToIndex(contextStart, contextLimit, length);
        if (contextStart > start || contextLimit < limit) {
            throw new IndexOutOfBoundsException(String.format("context range [%d,%d) must be no smaller than layout range [%d,%d)", contextStart, contextLimit, start, limit));
        } else if (bidiFlags >= 0 && bidiFlags <= 7) {
            boolean isOverride = (bidiFlags & 4) != 0;
            if (isOverride || contextStart == 0 && contextLimit == length) {
                int count = limit - start;
                if (count == 0) {
                    this.mAdvances = null;
                    this.mGlyphs = IntArrays.EMPTY_ARRAY;
                    this.mPositions = FloatArrays.EMPTY_ARRAY;
                    this.mFontIndices = null;
                    this.mFonts = null;
                    this.mAscent = 0;
                    this.mDescent = 0;
                    this.mAdvance = 0.0F;
                } else {
                    this.mAdvances = null;
                    FontMetricsInt extent = new FontMetricsInt();
                    ByteArrayList fontIndices = new ByteArrayList(count);
                    IntArrayList glyphs = new IntArrayList(count);
                    FloatArrayList positions = new FloatArrayList(count * 2);
                    ArrayList<Font> fontVec = new ArrayList();
                    HashMap<Font, Byte> fontMap = new HashMap();
                    Function<Font, Byte> nextID = font -> {
                        fontVec.add(font);
                        return (byte) fontMap.size();
                    };
                    Function<Font, Byte> idGet = font -> (Byte) fontMap.computeIfAbsent(font, nextID);
                    float advance = 0.0F;
                    if (isOverride) {
                        boolean isRtl = (bidiFlags & 1) != 0;
                        advance += doLayoutRun(text, contextStart, contextLimit, start, limit, isRtl, paint, start, this.mAdvances, advance, glyphs, positions, fontIndices, idGet, extent, null);
                    } else {
                        byte paraLevel = switch(bidiFlags) {
                            case 0 ->
                                0;
                            case 1 ->
                                1;
                            case 2 ->
                                126;
                            case 3 ->
                                127;
                            default ->
                                throw new AssertionError();
                        };
                        Bidi bidi = new Bidi(length, 0);
                        bidi.setPara(text, paraLevel, null);
                        if (bidi.isRightToLeft()) {
                            advance += doLayoutRun(text, 0, length, start, limit, true, paint, start, this.mAdvances, advance, glyphs, positions, fontIndices, idGet, extent, null);
                        } else if (bidi.isLeftToRight()) {
                            advance += doLayoutRun(text, 0, length, start, limit, false, paint, start, this.mAdvances, advance, glyphs, positions, fontIndices, idGet, extent, null);
                        } else {
                            int runCount = bidi.getRunCount();
                            for (int visualIndex = 0; visualIndex < runCount; visualIndex++) {
                                BidiRun run = bidi.getVisualRun(visualIndex);
                                int runStart = Math.max(run.getStart(), start);
                                int runEnd = Math.min(run.getLimit(), limit);
                                advance += doLayoutRun(text, 0, length, runStart, runEnd, run.isOddRun(), paint, start, this.mAdvances, advance, glyphs, positions, fontIndices, idGet, extent, null);
                            }
                        }
                    }
                    this.mAdvance = advance;
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
                    assert this.mGlyphs.length * 2 == this.mPositions.length;
                    assert this.mFontIndices == null || this.mFontIndices.length == this.mGlyphs.length;
                }
            } else {
                throw new IllegalArgumentException(String.format("text array [0,%d) must be context range [%d,%d) for non-override bidi flags 0x%X", length, contextStart, contextLimit, bidiFlags));
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Internal
    public static float doLayoutRun(char[] text, int contextStart, int contextLimit, int start, int limit, boolean isRtl, FontPaint paint, FontMetricsInt extent, ShapedText.RunConsumer consumer) {
        return doLayoutRun(text, contextStart, contextLimit, start, limit, isRtl, paint, start, null, 0.0F, null, null, null, null, extent, consumer);
    }

    @Internal
    public static float doLayoutRun(char[] text, int contextStart, int contextLimit, int start, int limit, boolean isRtl, FontPaint paint, int layoutStart, float[] advances, float curAdvance, IntArrayList glyphs, FloatArrayList positions, ByteArrayList fontIndices, Function<Font, Byte> idGet, FontMetricsInt extent, ShapedText.RunConsumer consumer) {
        float advance = 0.0F;
        if (isRtl) {
            int pos = limit;
            while (true) {
                int itContextStart = LayoutUtils.getPrevWordBreakForCache(text, contextStart, contextLimit, pos);
                int itContextEnd = LayoutUtils.getNextWordBreakForCache(text, contextStart, contextLimit, pos == 0 ? 0 : pos - 1);
                int itPieceStart = Math.max(itContextStart, start);
                if (itPieceStart == pos) {
                    break;
                }
                advance += doLayoutWord(text, itContextStart, itContextEnd, itPieceStart, pos, true, paint, itPieceStart - layoutStart, advances, curAdvance + advance, glyphs, positions, fontIndices, idGet, extent, consumer);
                pos = itPieceStart;
            }
        } else {
            int pos = start;
            while (true) {
                int itContextStart = LayoutUtils.getPrevWordBreakForCache(text, contextStart, contextLimit, pos == limit ? pos : pos + 1);
                int itContextEnd = LayoutUtils.getNextWordBreakForCache(text, contextStart, contextLimit, pos);
                int itPieceEnd = Math.min(itContextEnd, limit);
                if (pos == itPieceEnd) {
                    break;
                }
                advance += doLayoutWord(text, itContextStart, itContextEnd, pos, itPieceEnd, false, paint, pos - layoutStart, advances, curAdvance + advance, glyphs, positions, fontIndices, idGet, extent, consumer);
                pos = itPieceEnd;
            }
        }
        return advance;
    }

    private static float doLayoutWord(char[] buf, int contextStart, int contextEnd, int start, int end, boolean isRtl, FontPaint paint, int advanceOffset, float[] advances, float curAdvance, IntArrayList glyphs, FloatArrayList positions, ByteArrayList fontIndices, Function<Font, Byte> idGet, FontMetricsInt extent, ShapedText.RunConsumer consumer) {
        LayoutPiece src = LayoutCache.getOrCreate(buf, contextStart, contextEnd, start, end, isRtl, paint, advances != null ? 1 : 0);
        if (consumer == null) {
            for (int i = 0; i < src.getGlyphCount(); i++) {
                fontIndices.add((Byte) idGet.apply(src.getFont(i)));
            }
            glyphs.addElements(glyphs.size(), src.getGlyphs());
            int posStart = positions.size();
            positions.addElements(posStart, src.getPositions());
            int posIndex = posStart;
            for (int posEnd = positions.size(); posIndex < posEnd; posIndex += 2) {
                positions.elements()[posIndex] += curAdvance;
            }
            if (advances != null) {
                float[] srcAdvances = src.getAdvances();
                System.arraycopy(srcAdvances, 0, advances, advanceOffset, srcAdvances.length);
            }
        } else {
            consumer.accept(src, curAdvance);
        }
        if (extent != null) {
            extent.extendBy(src.getAscent(), src.getDescent());
        }
        return src.getAdvance();
    }

    @FunctionalInterface
    public interface RunConsumer {

        void accept(LayoutPiece var1, float var2);
    }
}