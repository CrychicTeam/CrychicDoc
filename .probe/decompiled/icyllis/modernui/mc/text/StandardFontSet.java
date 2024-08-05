package icyllis.modernui.mc.text;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.SheetGlyphInfo;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.FontCollection;
import icyllis.modernui.graphics.text.FontFamily;
import icyllis.modernui.graphics.text.FontPaint;
import icyllis.modernui.graphics.text.OutlineFont;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.font.CodepointMap;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.gui.font.glyphs.SpecialGlyphs;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Unmodifiable;

public class StandardFontSet extends FontSet {

    @Unmodifiable
    private List<FontFamily> mFamilies = Collections.emptyList();

    private CodepointMap<BakedGlyph> mGlyphs;

    private final IntFunction<BakedGlyph> mCacheGlyph = this::cacheGlyph;

    private CodepointMap<GlyphInfo> mGlyphInfos;

    private final IntFunction<GlyphInfo> mCacheGlyphInfo = this::cacheGlyphInfo;

    private float mResLevel = 2.0F;

    private final FontPaint mStandardPaint = new FontPaint();

    public StandardFontSet(@Nonnull TextureManager texMgr, @Nonnull ResourceLocation fontName) {
        super(texMgr, fontName);
        this.mStandardPaint.setFontStyle(0);
        this.mStandardPaint.setLocale(Locale.ROOT);
    }

    public void reload(@Nonnull FontCollection fontCollection, int newResLevel) {
        super.reload(Collections.emptyList());
        this.mFamilies = fontCollection.getFamilies();
        this.invalidateCache(newResLevel);
    }

    public void invalidateCache(int newResLevel) {
        if (this.mGlyphs != null) {
            this.mGlyphs.clear();
        }
        if (this.mGlyphInfos != null) {
            this.mGlyphInfos.clear();
        }
        int fontSize = TextLayoutProcessor.computeFontSize((float) newResLevel);
        this.mStandardPaint.setFontSize(fontSize);
        this.mResLevel = (float) newResLevel;
    }

    @Nonnull
    private GlyphInfo cacheGlyphInfo(int codePoint) {
        for (FontFamily family : this.mFamilies) {
            if (family.hasGlyph(codePoint)) {
                Font font = family.getClosestMatch(0);
                if (font instanceof BitmapFont bitmapFont) {
                    BitmapFont.Glyph glyph = bitmapFont.getGlyphInfo(codePoint);
                    if (glyph != null) {
                        return glyph;
                    }
                } else if (font instanceof SpaceFont spaceFont) {
                    float adv = spaceFont.getAdvance(codePoint);
                    if (!Float.isNaN(adv)) {
                        return (GlyphInfo.SpaceGlyphInfo) () -> adv;
                    }
                } else if (font instanceof OutlineFont outlineFont && outlineFont.hasGlyph(codePoint, 0)) {
                    char[] chars = Character.toChars(codePoint);
                    float adv = outlineFont.doSimpleLayout(chars, 0, chars.length, this.mStandardPaint, null, null, 0.0F, 0.0F);
                    return new StandardFontSet.StandardGlyphInfo((int) (adv / this.mResLevel + 0.95F));
                }
            }
        }
        return SpecialGlyphs.MISSING;
    }

    @Nonnull
    @Override
    public GlyphInfo getGlyphInfo(int codePoint, boolean notFishy) {
        if (this.mGlyphInfos == null) {
            this.mGlyphInfos = new CodepointMap<>(GlyphInfo[]::new, GlyphInfo[][]::new);
        }
        return this.mGlyphInfos.computeIfAbsent(codePoint, this.mCacheGlyphInfo);
    }

    @Nonnull
    private BakedGlyph cacheGlyph(int codePoint) {
        for (FontFamily family : this.mFamilies) {
            if (family.hasGlyph(codePoint)) {
                Font font = family.getClosestMatch(0);
                if (font instanceof BitmapFont bitmapFont) {
                    BitmapFont.Glyph glyph = bitmapFont.getGlyph(codePoint);
                    if (glyph != null) {
                        float up = 3.0F + (float) bitmapFont.getAscent() + (float) glyph.y / 8.0F;
                        float left = (float) glyph.x / 8.0F;
                        float right = (float) (glyph.x + glyph.width) / 8.0F;
                        float down = up + (float) glyph.height / 8.0F;
                        return new StandardFontSet.StandardBakedGlyph(bitmapFont::getCurrentTexture, glyph.u1, glyph.u2, glyph.v1, glyph.v2, left, right, up, down);
                    }
                } else {
                    if (font instanceof SpaceFont) {
                        return EmptyGlyph.INSTANCE;
                    }
                    if (font instanceof OutlineFont) {
                        OutlineFont outlineFont = (OutlineFont) font;
                        char[] chars = Character.toChars(codePoint);
                        IntArrayList glyphs = new IntArrayList(1);
                        float adv = outlineFont.doSimpleLayout(chars, 0, chars.length, this.mStandardPaint, glyphs, null, 0.0F, 0.0F);
                        if (glyphs.size() == 1 && glyphs.getInt(0) != 0) {
                            icyllis.modernui.graphics.font.BakedGlyph glyph = TextLayoutEngine.getInstance().lookupGlyph(outlineFont, this.mStandardPaint.getFontSize(), glyphs.getInt(0));
                            if (glyph != null) {
                                float up = 10.0F + (float) glyph.y / this.mResLevel;
                                float left = (float) glyph.x / this.mResLevel;
                                float right = (float) (glyph.x + glyph.width) / this.mResLevel;
                                float down = up + (float) glyph.height / this.mResLevel;
                                return new StandardFontSet.StandardBakedGlyph(() -> TextLayoutEngine.getInstance().getStandardTexture(), glyph.u1, glyph.u2, glyph.v1, glyph.v2, left, right, up, down);
                            }
                        }
                        if (adv > 0.0F) {
                            return EmptyGlyph.INSTANCE;
                        }
                    }
                }
            }
        }
        return super.getGlyph(codePoint);
    }

    @Nonnull
    @Override
    public BakedGlyph getGlyph(int codePoint) {
        if (this.mGlyphs == null) {
            this.mGlyphs = new CodepointMap<>(BakedGlyph[]::new, BakedGlyph[][]::new);
        }
        return this.mGlyphs.computeIfAbsent(codePoint, this.mCacheGlyph);
    }

    public static class StandardBakedGlyph extends BakedGlyph {

        private static final GlyphRenderTypes EMPTY_TYPES = GlyphRenderTypes.createForColorTexture(new ResourceLocation(""));

        private final IntSupplier mCurrentTexture;

        public StandardBakedGlyph(IntSupplier currentTexture, float u0, float u1, float v0, float v1, float left, float right, float up, float down) {
            super(EMPTY_TYPES, u0, u1, v0, v1, left, right, up, down);
            this.mCurrentTexture = currentTexture;
        }

        @Nonnull
        @Override
        public RenderType renderType(@Nonnull net.minecraft.client.gui.Font.DisplayMode mode) {
            return TextRenderType.getOrCreate(this.mCurrentTexture.getAsInt(), mode);
        }
    }

    public static class StandardGlyphInfo implements GlyphInfo {

        private final float mAdvance;

        public StandardGlyphInfo(int advance) {
            this.mAdvance = (float) advance;
        }

        @Override
        public float getAdvance() {
            return this.mAdvance;
        }

        @Override
        public float getBoldOffset() {
            return 0.5F;
        }

        @Override
        public float getShadowOffset() {
            return ModernTextRenderer.sShadowOffset;
        }

        @Nonnull
        @Override
        public BakedGlyph bake(@Nonnull Function<SheetGlyphInfo, BakedGlyph> function) {
            return EmptyGlyph.INSTANCE;
        }
    }
}