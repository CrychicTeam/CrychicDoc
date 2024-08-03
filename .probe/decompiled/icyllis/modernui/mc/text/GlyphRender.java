package icyllis.modernui.mc.text;

import com.mojang.blaze3d.vertex.VertexConsumer;
import icyllis.modernui.graphics.font.BakedGlyph;
import java.util.Random;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.joml.Matrix4f;

@Deprecated
public abstract class GlyphRender {

    public static final int USE_INPUT_COLOR = Integer.MIN_VALUE;

    public static final int COLOR_NO_CHANGE = 1073741824;

    public int mStringIndex;

    public float mOffsetX;

    private final float mAdvance;

    public int mFlags = 1073741824;

    public GlyphRender(int stripIndex, float offsetX, float advance, int decoration) {
        this.mStringIndex = stripIndex;
        this.mOffsetX = offsetX;
        this.mAdvance = advance;
        this.mFlags |= decoration;
    }

    public final void drawEffect(@Nonnull VertexConsumer builder, float x, float y, int r, int g, int b, int a) {
        if ((this.mFlags & 201326592) != 0) {
            x += this.mOffsetX;
            if ((this.mFlags & 67108864) != 0) {
                TextRenderEffect.drawUnderline(builder, x, x + this.mAdvance, y, r, g, b, a);
            }
            if ((this.mFlags & 134217728) != 0) {
                TextRenderEffect.drawStrikethrough(builder, x, x + this.mAdvance, y, r, g, b, a);
            }
        }
    }

    public final void drawEffect(@Nonnull Matrix4f matrix, @Nonnull VertexConsumer builder, float x, float y, int r, int g, int b, int a, int light) {
        if ((this.mFlags & 201326592) != 0) {
            x += this.mOffsetX;
            if ((this.mFlags & 67108864) != 0) {
                TextRenderEffect.drawUnderline(matrix, builder, x, x + this.mAdvance, y, r, g, b, a, light);
            }
            if ((this.mFlags & 134217728) != 0) {
                TextRenderEffect.drawStrikethrough(matrix, builder, x, x + this.mAdvance, y, r, g, b, a, light);
            }
        }
    }

    public float getAdvance() {
        return this.mAdvance;
    }

    public String toString() {
        return "BaseGlyphRender{stringIndex=" + this.mStringIndex + ", offsetX=" + this.mOffsetX + ", advance=" + this.mAdvance + ", flags=0x" + Integer.toHexString(this.mFlags) + "}";
    }

    @Deprecated
    static class DigitGlyphRender extends GlyphRender {

        @Nonnull
        private final Entry<BakedGlyph[], float[]> mDigits;

        public DigitGlyphRender(int stripIndex, float offsetX, float advance, int decoration, @Nonnull Entry<BakedGlyph[], float[]> digits) {
            super(stripIndex, offsetX, advance, decoration);
            this.mDigits = digits;
        }
    }

    @Deprecated
    static class RandomGlyphRender extends GlyphRender {

        private static final Random RANDOM = new Random();

        @Nonnull
        private final Entry<BakedGlyph[], float[]> mGlyphs;

        public RandomGlyphRender(int stripIndex, float offsetX, float advance, int decoration, @Nonnull Entry<BakedGlyph[], float[]> glyphs) {
            super(stripIndex, advance, offsetX, decoration);
            this.mGlyphs = glyphs;
        }
    }

    @Deprecated
    static class StandardGlyphRender extends GlyphRender {

        @Nullable
        private final BakedGlyph mGlyph;

        public StandardGlyphRender(int stripIndex, float offsetX, float advance, int decoration, @Nullable BakedGlyph glyph) {
            super(stripIndex, offsetX, advance, decoration);
            this.mGlyph = glyph;
        }
    }
}