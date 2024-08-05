package com.mojang.blaze3d.font;

import com.mojang.blaze3d.platform.NativeImage;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Function;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class TrueTypeGlyphProvider implements GlyphProvider {

    @Nullable
    private ByteBuffer fontMemory;

    @Nullable
    private STBTTFontinfo font;

    final float oversample;

    private final IntSet skip = new IntArraySet();

    final float shiftX;

    final float shiftY;

    final float pointScale;

    final float ascent;

    public TrueTypeGlyphProvider(ByteBuffer byteBuffer0, STBTTFontinfo sTBTTFontinfo1, float float2, float float3, float float4, float float5, String string6) {
        this.fontMemory = byteBuffer0;
        this.font = sTBTTFontinfo1;
        this.oversample = float3;
        string6.codePoints().forEach(this.skip::add);
        this.shiftX = float4 * float3;
        this.shiftY = float5 * float3;
        this.pointScale = STBTruetype.stbtt_ScaleForPixelHeight(sTBTTFontinfo1, float2 * float3);
        MemoryStack $$7 = MemoryStack.stackPush();
        try {
            IntBuffer $$8 = $$7.mallocInt(1);
            IntBuffer $$9 = $$7.mallocInt(1);
            IntBuffer $$10 = $$7.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(sTBTTFontinfo1, $$8, $$9, $$10);
            this.ascent = (float) $$8.get(0) * this.pointScale;
        } catch (Throwable var13) {
            if ($$7 != null) {
                try {
                    $$7.close();
                } catch (Throwable var12) {
                    var13.addSuppressed(var12);
                }
            }
            throw var13;
        }
        if ($$7 != null) {
            $$7.close();
        }
    }

    @Nullable
    @Override
    public GlyphInfo getGlyph(int int0) {
        STBTTFontinfo $$1 = this.validateFontOpen();
        if (this.skip.contains(int0)) {
            return null;
        } else {
            MemoryStack $$2 = MemoryStack.stackPush();
            Object var17;
            label61: {
                GlyphInfo var18;
                label62: {
                    try {
                        int $$3 = STBTruetype.stbtt_FindGlyphIndex($$1, int0);
                        if ($$3 == 0) {
                            var17 = null;
                            break label61;
                        }
                        IntBuffer $$4 = $$2.mallocInt(1);
                        IntBuffer $$5 = $$2.mallocInt(1);
                        IntBuffer $$6 = $$2.mallocInt(1);
                        IntBuffer $$7 = $$2.mallocInt(1);
                        IntBuffer $$8 = $$2.mallocInt(1);
                        IntBuffer $$9 = $$2.mallocInt(1);
                        STBTruetype.stbtt_GetGlyphHMetrics($$1, $$3, $$8, $$9);
                        STBTruetype.stbtt_GetGlyphBitmapBoxSubpixel($$1, $$3, this.pointScale, this.pointScale, this.shiftX, this.shiftY, $$4, $$5, $$6, $$7);
                        float $$10 = (float) $$8.get(0) * this.pointScale;
                        int $$11 = $$6.get(0) - $$4.get(0);
                        int $$12 = $$7.get(0) - $$5.get(0);
                        if ($$11 > 0 && $$12 > 0) {
                            var18 = new TrueTypeGlyphProvider.Glyph($$4.get(0), $$6.get(0), -$$5.get(0), -$$7.get(0), $$10, (float) $$9.get(0) * this.pointScale, $$3);
                            break label62;
                        }
                        var18 = (GlyphInfo.SpaceGlyphInfo) () -> $$10 / this.oversample;
                    } catch (Throwable var16) {
                        if ($$2 != null) {
                            try {
                                $$2.close();
                            } catch (Throwable var15) {
                                var16.addSuppressed(var15);
                            }
                        }
                        throw var16;
                    }
                    if ($$2 != null) {
                        $$2.close();
                    }
                    return var18;
                }
                if ($$2 != null) {
                    $$2.close();
                }
                return var18;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return (GlyphInfo) var17;
        }
    }

    STBTTFontinfo validateFontOpen() {
        if (this.fontMemory != null && this.font != null) {
            return this.font;
        } else {
            throw new IllegalArgumentException("Provider already closed");
        }
    }

    @Override
    public void close() {
        if (this.font != null) {
            this.font.free();
            this.font = null;
        }
        MemoryUtil.memFree(this.fontMemory);
        this.fontMemory = null;
    }

    @Override
    public IntSet getSupportedGlyphs() {
        return (IntSet) IntStream.range(0, 65535).filter(p_231118_ -> !this.skip.contains(p_231118_)).collect(IntOpenHashSet::new, IntCollection::add, IntCollection::addAll);
    }

    class Glyph implements GlyphInfo {

        final int width;

        final int height;

        final float bearingX;

        final float bearingY;

        private final float advance;

        final int index;

        Glyph(int int0, int int1, int int2, int int3, float float4, float float5, int int6) {
            this.width = int1 - int0;
            this.height = int2 - int3;
            this.advance = float4 / TrueTypeGlyphProvider.this.oversample;
            this.bearingX = (float5 + (float) int0 + TrueTypeGlyphProvider.this.shiftX) / TrueTypeGlyphProvider.this.oversample;
            this.bearingY = (TrueTypeGlyphProvider.this.ascent - (float) int2 + TrueTypeGlyphProvider.this.shiftY) / TrueTypeGlyphProvider.this.oversample;
            this.index = int6;
        }

        @Override
        public float getAdvance() {
            return this.advance;
        }

        @Override
        public BakedGlyph bake(Function<SheetGlyphInfo, BakedGlyph> functionSheetGlyphInfoBakedGlyph0) {
            return (BakedGlyph) functionSheetGlyphInfoBakedGlyph0.apply(new SheetGlyphInfo() {

                @Override
                public int getPixelWidth() {
                    return Glyph.this.width;
                }

                @Override
                public int getPixelHeight() {
                    return Glyph.this.height;
                }

                @Override
                public float getOversample() {
                    return TrueTypeGlyphProvider.this.oversample;
                }

                @Override
                public float getBearingX() {
                    return Glyph.this.bearingX;
                }

                @Override
                public float getBearingY() {
                    return Glyph.this.bearingY;
                }

                @Override
                public void upload(int p_231126_, int p_231127_) {
                    STBTTFontinfo $$2 = TrueTypeGlyphProvider.this.validateFontOpen();
                    NativeImage $$3 = new NativeImage(NativeImage.Format.LUMINANCE, Glyph.this.width, Glyph.this.height, false);
                    $$3.copyFromFont($$2, Glyph.this.index, Glyph.this.width, Glyph.this.height, TrueTypeGlyphProvider.this.pointScale, TrueTypeGlyphProvider.this.pointScale, TrueTypeGlyphProvider.this.shiftX, TrueTypeGlyphProvider.this.shiftY, 0, 0);
                    $$3.upload(0, p_231126_, p_231127_, 0, 0, Glyph.this.width, Glyph.this.height, false, true);
                }

                @Override
                public boolean isColored() {
                    return false;
                }
            });
        }
    }
}