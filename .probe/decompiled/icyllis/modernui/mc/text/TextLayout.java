package icyllis.modernui.mc.text;

import com.mojang.blaze3d.vertex.VertexConsumer;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.font.BakedGlyph;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.util.SparseArray;
import java.util.Arrays;
import java.util.Random;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;

public class TextLayout {

    private static final Random RANDOM = new Random();

    public static final TextLayout EMPTY = new TextLayout(new char[0], new int[0], new float[0], null, new Font[0], new float[0], new int[0], new int[] { 0 }, 0.0F, false, false, 2, -1) {

        @Nonnull
        @Override
        TextLayout get() {
            throw new UnsupportedOperationException();
        }

        @Override
        boolean tick(int lifespan) {
            throw new UnsupportedOperationException();
        }

        @Override
        public float drawText(@Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, float x, float top, int r, int g, int b, int a, boolean isShadow, int preferredMode, boolean polygonOffset, int bgColor, int packedLight) {
            return 0.0F;
        }

        @Override
        public void drawTextOutline(@Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, float x, float top, int r, int g, int b, int a, int packedLight) {
        }
    };

    public static final int STANDARD_BASELINE_OFFSET = 7;

    public static float sBaselineOffset = 7.0F;

    private final char[] mTextBuf;

    private final int[] mGlyphs;

    private transient BakedGlyph[] mBakedGlyphs;

    private transient BakedGlyph[] mBakedGlyphsForSDF;

    private transient SparseArray<BakedGlyph[]> mBakedGlyphsArray;

    private final float[] mPositions;

    private final byte[] mFontIndices;

    private final Font[] mFonts;

    private final float[] mAdvances;

    private final int[] mGlyphFlags;

    private final int[] mLineBoundaries;

    private final float mTotalAdvance;

    private final boolean mHasEffect;

    private final boolean mHasColorEmoji;

    final int mCreatedResLevel;

    final int mComputedFlags;

    private transient int mTimer = 0;

    private TextLayout(@Nonnull TextLayout layout) {
        this.mTextBuf = layout.mTextBuf;
        this.mGlyphs = layout.mGlyphs;
        this.mPositions = layout.mPositions;
        this.mFontIndices = layout.mFontIndices;
        this.mFonts = layout.mFonts;
        this.mAdvances = layout.mAdvances;
        this.mGlyphFlags = layout.mGlyphFlags;
        this.mLineBoundaries = layout.mLineBoundaries;
        this.mTotalAdvance = layout.mTotalAdvance;
        this.mHasEffect = layout.mHasEffect;
        this.mHasColorEmoji = layout.mHasColorEmoji;
        this.mCreatedResLevel = layout.mCreatedResLevel;
        this.mComputedFlags = layout.mComputedFlags;
    }

    TextLayout(@Nonnull char[] textBuf, @Nonnull int[] glyphs, @Nonnull float[] positions, @Nullable byte[] fontIndices, @Nonnull Font[] fonts, @Nullable float[] advances, @Nonnull int[] glyphFlags, @Nullable int[] lineBoundaries, float totalAdvance, boolean hasEffect, boolean hasColorEmoji, int createdResLevel, int computedFlags) {
        this.mTextBuf = textBuf;
        this.mGlyphs = glyphs;
        this.mPositions = positions;
        this.mFontIndices = fontIndices;
        this.mFonts = fonts;
        this.mAdvances = advances;
        this.mGlyphFlags = glyphFlags;
        this.mLineBoundaries = lineBoundaries;
        this.mTotalAdvance = totalAdvance;
        this.mHasEffect = hasEffect;
        this.mHasColorEmoji = hasColorEmoji;
        this.mCreatedResLevel = createdResLevel;
        this.mComputedFlags = computedFlags;
        assert this.mAdvances == null || this.mTextBuf.length == this.mAdvances.length;
        assert this.mGlyphs.length * 2 == this.mPositions.length;
        assert this.mGlyphs.length == this.mGlyphFlags.length;
    }

    @Nonnull
    public static TextLayout makeEmpty() {
        return new TextLayout(EMPTY);
    }

    @Nonnull
    TextLayout get() {
        this.mTimer = 0;
        return this;
    }

    boolean tick(int lifespan) {
        return ++this.mTimer > lifespan;
    }

    @Nonnull
    private BakedGlyph[] prepareGlyphs(int resLevel, int fontSize) {
        TextLayoutEngine engine = TextLayoutEngine.getInstance();
        BakedGlyph[] glyphs = new BakedGlyph[this.mGlyphs.length];
        for (int i = 0; i < glyphs.length; i++) {
            if ((this.mGlyphFlags[i] & 268435456) != 0) {
                glyphs[i] = engine.lookupFastChars(this.getFont(i), resLevel);
            } else {
                glyphs[i] = engine.lookupGlyph(this.getFont(i), fontSize, this.mGlyphs[i]);
            }
        }
        return glyphs;
    }

    @Nonnull
    private BakedGlyph[] getGlyphs(int resLevel) {
        if (resLevel == this.mCreatedResLevel) {
            if (this.mBakedGlyphs == null) {
                int fontSize = TextLayoutProcessor.computeFontSize((float) resLevel);
                this.mBakedGlyphs = this.prepareGlyphs(resLevel, fontSize);
            }
            return this.mBakedGlyphs;
        } else {
            if (this.mBakedGlyphsForSDF == null) {
                int fontSize = TextLayoutProcessor.computeFontSize((float) resLevel);
                this.mBakedGlyphsForSDF = this.prepareGlyphs(resLevel, fontSize);
            }
            return this.mBakedGlyphsForSDF;
        }
    }

    @Nonnull
    private BakedGlyph[] getGlyphsUniformScale(float density) {
        if (this.mBakedGlyphsArray == null) {
            this.mBakedGlyphsArray = new SparseArray<>();
        }
        int fontSize = TextLayoutProcessor.computeFontSize(density);
        BakedGlyph[] glyphs = this.mBakedGlyphsArray.get(fontSize);
        if (glyphs == null) {
            glyphs = this.prepareGlyphs(this.mCreatedResLevel, fontSize);
            this.mBakedGlyphsArray.put(fontSize, glyphs);
        }
        return glyphs;
    }

    public float drawText(@Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, float x, float top, int r, int g, int b, int a, boolean isShadow, int preferredMode, boolean polygonOffset, int bgColor, int packedLight) {
        int startR = r;
        int startG = g;
        int startB = b;
        float density;
        BakedGlyph[] glyphs;
        if (preferredMode == 1) {
            int resLevel = TextLayoutEngine.adjustPixelDensityForSDF(this.mCreatedResLevel);
            glyphs = this.getGlyphs(resLevel);
            density = (float) resLevel;
        } else if (preferredMode == 4) {
            float devS = matrix.m00();
            if (devS == 0.0F) {
                return this.mTotalAdvance;
            }
            density = (float) this.mCreatedResLevel * devS;
            glyphs = this.getGlyphsUniformScale(density);
            preferredMode = 0;
        } else {
            glyphs = this.getGlyphs(this.mCreatedResLevel);
            density = (float) this.mCreatedResLevel;
        }
        float invDensity = 1.0F / density;
        float[] positions = this.mPositions;
        int[] flags = this.mGlyphFlags;
        float baseline = top + sBaselineOffset;
        int prevTexture = -1;
        VertexConsumer builder = null;
        int standardTexture = -1;
        boolean seeThrough = preferredMode == 3;
        int i = 0;
        for (int e = glyphs.length; i < e; i++) {
            BakedGlyph glyph = glyphs[i];
            if (glyph != null) {
                int bits = flags[i];
                float rx = 0.0F;
                boolean fakeItalic = false;
                int ascent = 0;
                net.minecraft.client.gui.Font.DisplayMode compatDisplayMode = null;
                float ry;
                float w;
                float h;
                int effMode;
                int texture;
                if ((bits & 1610612736) != 0) {
                    float scaleFactor = 0.125F;
                    if ((bits & 536870912) != 0) {
                        if (isShadow) {
                            continue;
                        }
                        scaleFactor *= TextLayoutProcessor.sBaseFontSize / 8.0F;
                    }
                    rx = x + positions[i << 1] + (float) glyph.x * scaleFactor;
                    ry = baseline + positions[i << 1 | 1] + (float) glyph.y * scaleFactor;
                    if (isShadow) {
                        rx += 1.0F - ModernTextRenderer.sShadowOffset;
                        ry += 1.0F - ModernTextRenderer.sShadowOffset;
                    }
                    w = (float) glyph.width * scaleFactor;
                    h = (float) glyph.height * scaleFactor;
                    effMode = seeThrough ? preferredMode : 0;
                    if (polygonOffset) {
                        compatDisplayMode = net.minecraft.client.gui.Font.DisplayMode.POLYGON_OFFSET;
                    }
                    if (this.getFont(i) instanceof BitmapFont bitmapFont) {
                        texture = bitmapFont.getCurrentTexture();
                        ascent = bitmapFont.getAscent();
                    } else {
                        texture = TextLayoutEngine.getInstance().getEmojiTexture();
                        ascent = 7;
                    }
                    fakeItalic = (bits & 33554432) != 0;
                } else {
                    label183: {
                        boolean obfuscated = false;
                        if ((bits & 268435456) != 0) {
                            TextLayoutEngine.FastCharSet chars = (TextLayoutEngine.FastCharSet) glyph;
                            int fastIndex = RANDOM.nextInt(chars.glyphs.length);
                            glyph = chars.glyphs[fastIndex];
                            if (fastIndex != 0) {
                                rx += chars.offsets[fastIndex];
                            }
                            obfuscated = true;
                        }
                        if (obfuscated) {
                            Font var68 = this.getFont(i);
                            if (var68 instanceof BitmapFont) {
                                BitmapFont bitmapFont = (BitmapFont) var68;
                                effMode = seeThrough ? preferredMode : 0;
                                if (polygonOffset) {
                                    compatDisplayMode = net.minecraft.client.gui.Font.DisplayMode.POLYGON_OFFSET;
                                }
                                float scaleFactorx = 0.125F;
                                rx += x + positions[i << 1] + (float) glyph.x * scaleFactorx;
                                ry = baseline + positions[i << 1 | 1] + (float) glyph.y * scaleFactorx;
                                if (isShadow) {
                                    rx += 1.0F - ModernTextRenderer.sShadowOffset;
                                    ry += 1.0F - ModernTextRenderer.sShadowOffset;
                                }
                                w = (float) glyph.width * scaleFactorx;
                                h = (float) glyph.height * scaleFactorx;
                                texture = bitmapFont.getCurrentTexture();
                                break label183;
                            }
                        }
                        effMode = preferredMode;
                        rx += x + positions[i << 1] + (float) glyph.x * invDensity;
                        ry = baseline + positions[i << 1 | 1] + (float) glyph.y * invDensity;
                        w = (float) glyph.width * invDensity;
                        h = (float) glyph.height * invDensity;
                        if (standardTexture == -1) {
                            standardTexture = TextLayoutEngine.getInstance().getStandardTexture();
                        }
                        texture = standardTexture;
                    }
                }
                if (effMode == 0 && !TextLayoutEngine.sCurrentInWorldRendering) {
                    rx = (float) ((int) (rx * density + 0.5F)) * invDensity;
                    ry = (float) ((int) (ry * density + 0.5F)) * invDensity;
                }
                if ((bits & -2147483648) != 0) {
                    r = startR;
                    g = startG;
                    b = startB;
                } else {
                    r = bits >> 16 & 0xFF;
                    g = bits >> 8 & 0xFF;
                    b = bits & 0xFF;
                    if (isShadow) {
                        r >>= 2;
                        g >>= 2;
                        b >>= 2;
                    }
                }
                if (builder == null || prevTexture != texture) {
                    prevTexture = texture;
                    builder = source.getBuffer(compatDisplayMode != null ? TextRenderType.getOrCreate(texture, compatDisplayMode) : TextRenderType.getOrCreate(texture, effMode));
                }
                float upSkew = 0.0F;
                float downSkew = 0.0F;
                if (fakeItalic) {
                    upSkew = 0.25F * (float) ascent;
                    downSkew = 0.25F * ((float) ascent - h);
                }
                builder.vertex(matrix, rx + upSkew, ry, 0.0F).color(r, g, b, a).uv(glyph.u1, glyph.v1).uv2(packedLight).endVertex();
                builder.vertex(matrix, rx + downSkew, ry + h, 0.0F).color(r, g, b, a).uv(glyph.u1, glyph.v2).uv2(packedLight).endVertex();
                builder.vertex(matrix, rx + w + downSkew, ry + h, 0.0F).color(r, g, b, a).uv(glyph.u2, glyph.v2).uv2(packedLight).endVertex();
                builder.vertex(matrix, rx + w + upSkew, ry, 0.0F).color(r, g, b, a).uv(glyph.u2, glyph.v1).uv2(packedLight).endVertex();
            }
        }
        builder = null;
        if (this.mHasEffect) {
            builder = source.getBuffer(EffectRenderType.getRenderType(seeThrough));
            i = 0;
            for (int ex = glyphs.length; i < ex; i++) {
                int flag = flags[i];
                if ((flag & 201326592) != 0) {
                    if ((flag & -2147483648) != 0) {
                        r = startR;
                        g = startG;
                        b = startB;
                    } else {
                        r = flag >> 16 & 0xFF;
                        g = flag >> 8 & 0xFF;
                        b = flag & 0xFF;
                        if (isShadow) {
                            r >>= 2;
                            g >>= 2;
                            b >>= 2;
                        }
                    }
                    float rx1 = x + positions[i << 1];
                    float rx2 = x + (i + 1 == ex ? this.mTotalAdvance : positions[i + 1 << 1]);
                    if ((flag & 134217728) != 0) {
                        TextRenderEffect.drawStrikethrough(matrix, builder, rx1, rx2, baseline, r, g, b, a, packedLight);
                    }
                    if ((flag & 67108864) != 0) {
                        TextRenderEffect.drawUnderline(matrix, builder, rx1, rx2, baseline, r, g, b, a, packedLight);
                    }
                }
            }
        }
        if ((bgColor & 0xFF000000) != 0) {
            a = bgColor >>> 24;
            r = bgColor >> 16 & 0xFF;
            g = bgColor >> 8 & 0xFF;
            b = bgColor & 0xFF;
            if (builder == null) {
                builder = source.getBuffer(EffectRenderType.getRenderType(seeThrough));
            }
            builder.vertex(matrix, x - 1.0F, top + 9.0F, 0.01F).color(r, g, b, a).uv(0.0F, 1.0F).uv2(packedLight).endVertex();
            builder.vertex(matrix, x + this.mTotalAdvance + 1.0F, top + 9.0F, 0.01F).color(r, g, b, a).uv(1.0F, 1.0F).uv2(packedLight).endVertex();
            builder.vertex(matrix, x + this.mTotalAdvance + 1.0F, top - 1.0F, 0.01F).color(r, g, b, a).uv(1.0F, 0.0F).uv2(packedLight).endVertex();
            builder.vertex(matrix, x - 1.0F, top - 1.0F, 0.01F).color(r, g, b, a).uv(0.0F, 0.0F).uv2(packedLight).endVertex();
        }
        return this.mTotalAdvance;
    }

    public void drawTextOutline(@Nonnull Matrix4f matrix, @Nonnull MultiBufferSource source, float x, float top, int r, int g, int b, int a, int packedLight) {
        float resLevel = (float) TextLayoutEngine.adjustPixelDensityForSDF(this.mCreatedResLevel);
        BakedGlyph[] glyphs = this.getGlyphs((int) resLevel);
        float[] positions = this.mPositions;
        int[] flags = this.mGlyphFlags;
        float baseline = top + sBaselineOffset;
        int prevTexture = -1;
        VertexConsumer builder = null;
        int standardTexture = -1;
        float sBloat = 1.0F / resLevel;
        int i = 0;
        for (int e = glyphs.length; i < e; i++) {
            BakedGlyph glyph = glyphs[i];
            if (glyph != null) {
                int bits = flags[i];
                float rx = 0.0F;
                if ((bits & 1610612736) == 0) {
                    if ((bits & 268435456) != 0) {
                        TextLayoutEngine.FastCharSet chars = (TextLayoutEngine.FastCharSet) glyph;
                        int fastIndex = RANDOM.nextInt(chars.glyphs.length);
                        glyph = chars.glyphs[fastIndex];
                        if (fastIndex != 0) {
                            rx += chars.offsets[fastIndex];
                        }
                    }
                    rx += x + positions[i << 1] + (float) glyph.x / resLevel;
                    float ry = baseline + positions[i << 1 | 1] + (float) glyph.y / resLevel;
                    float w = (float) glyph.width / resLevel;
                    float h = (float) glyph.height / resLevel;
                    if (standardTexture == -1) {
                        standardTexture = TextLayoutEngine.getInstance().getStandardTexture();
                    }
                    if (builder == null || prevTexture != standardTexture) {
                        prevTexture = standardTexture;
                        builder = source.getBuffer(TextRenderType.getOrCreate(standardTexture, 2));
                    }
                    float uBloat = (glyph.u2 - glyph.u1) / (float) glyph.width;
                    float vBloat = (glyph.v2 - glyph.v1) / (float) glyph.height;
                    builder.vertex(matrix, rx - sBloat, ry - sBloat, 0.001F).color(r, g, b, a).uv(glyph.u1 - uBloat, glyph.v1 - vBloat).uv2(packedLight).endVertex();
                    builder.vertex(matrix, rx - sBloat, ry + h + sBloat, 0.001F).color(r, g, b, a).uv(glyph.u1 - uBloat, glyph.v2 + vBloat).uv2(packedLight).endVertex();
                    builder.vertex(matrix, rx + w + sBloat, ry + h + sBloat, 0.0F).color(r, g, b, a).uv(glyph.u2 + uBloat, glyph.v2 + vBloat).uv2(packedLight).endVertex();
                    builder.vertex(matrix, rx + w + sBloat, ry - sBloat, 0.0F).color(r, g, b, a).uv(glyph.u2 + uBloat, glyph.v1 - vBloat).uv2(packedLight).endVertex();
                }
            }
        }
    }

    @Nonnull
    public char[] getTextBuf() {
        return this.mTextBuf;
    }

    @Nonnull
    public int[] getGlyphs() {
        return this.mGlyphs;
    }

    @Nonnull
    public float[] getPositions() {
        return this.mPositions;
    }

    public float[] getAdvances() {
        return this.mAdvances;
    }

    public Font getFont(int i) {
        return this.mFontIndices != null ? this.mFonts[this.mFontIndices[i] & 255] : this.mFonts[0];
    }

    public int getCharCount() {
        return this.mTextBuf.length;
    }

    @Nonnull
    public int[] getGlyphFlags() {
        return this.mGlyphFlags;
    }

    @Nullable
    public byte[] getFontIndices() {
        return this.mFontIndices;
    }

    public Font[] getFontVector() {
        return this.mFonts;
    }

    public int[] getLineBoundaries() {
        return this.mLineBoundaries;
    }

    public float getTotalAdvance() {
        return this.mTotalAdvance;
    }

    public boolean hasEffect() {
        return this.mHasEffect;
    }

    public boolean hasColorEmoji() {
        return this.mHasColorEmoji;
    }

    public int getMemorySize() {
        int m = 0;
        m += 16 + MathUtil.align8(this.mTextBuf.length << 1);
        m += 16 + MathUtil.align8(this.mGlyphs.length << 2);
        m += 16 + MathUtil.align8(this.mPositions.length << 2);
        if (this.mFontIndices != null) {
            m += 16 + MathUtil.align8(this.mFontIndices.length);
        }
        m += 16 + MathUtil.align8(this.mFonts.length << 2);
        if (this.mAdvances != null) {
            m += 16 + MathUtil.align8(this.mAdvances.length << 2);
        }
        m += 16 + MathUtil.align8(this.mGlyphFlags.length << 2);
        if (this.mLineBoundaries != null) {
            m += 16 + MathUtil.align8(this.mLineBoundaries.length << 2);
        }
        if (this.mBakedGlyphs != null) {
            m += 16 + MathUtil.align8(this.mBakedGlyphs.length << 2);
        }
        if (this.mBakedGlyphsForSDF != null) {
            m += 16 + MathUtil.align8(this.mBakedGlyphsForSDF.length << 2);
        }
        if (this.mBakedGlyphsArray != null) {
            m += (16 + MathUtil.align8(this.mBakedGlyphsArray.valueAt(0).length << 2)) * this.mBakedGlyphsArray.size();
        }
        return m + 64;
    }

    public String toString() {
        return "TextLayout{text=" + toEscapeChars(this.mTextBuf) + ",glyphs=" + this.mGlyphs.length + ",length=" + this.mTextBuf.length + ",positions=" + toPositionString(this.mPositions) + ",advances=" + Arrays.toString(this.mAdvances) + ",charFlags=" + toFlagString(this.mGlyphFlags) + ",lineBoundaries" + Arrays.toString(this.mLineBoundaries) + ",totalAdvance=" + this.mTotalAdvance + ",hasEffect=" + this.mHasEffect + ",hasColorEmoji=" + this.mHasColorEmoji + "}";
    }

    @Nonnull
    private static String toEscapeChars(@Nonnull char[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "";
        } else {
            StringBuilder b = new StringBuilder();
            int i = 0;
            while (true) {
                b.append("\\u");
                String s = Integer.toHexString(a[i]);
                b.append("0".repeat(4 - s.length()));
                b.append(s);
                if (i == iMax) {
                    return b.toString();
                }
                i++;
            }
        }
    }

    @Nonnull
    private static String toPositionString(@Nonnull float[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "[]";
        } else {
            StringBuilder b = new StringBuilder();
            b.append('[');
            int i = 0;
            while (true) {
                b.append('(');
                b.append(a[i++]);
                b.append(',');
                b.append(a[i]);
                b.append(')');
                if (i == iMax) {
                    return b.append(']').toString();
                }
                b.append(", ");
                i++;
            }
        }
    }

    @Nonnull
    private static String toFlagString(@Nonnull int[] a) {
        int iMax = a.length - 1;
        if (iMax == -1) {
            return "[]";
        } else {
            StringBuilder b = new StringBuilder();
            b.append('[');
            int i = 0;
            while (true) {
                b.append("0x");
                b.append(Integer.toHexString(a[i]));
                if (i == iMax) {
                    return b.append(']').toString();
                }
                b.append(" ");
                i++;
            }
        }
    }
}