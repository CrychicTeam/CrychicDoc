package icyllis.modernui.mc.text;

import com.mojang.blaze3d.font.SpaceProvider;
import icyllis.arc3d.core.Strike;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.FontPaint;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.util.Locale;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public class SpaceFont implements Font {

    private final String mFontName;

    private final Int2FloatOpenHashMap mAdvances;

    private SpaceFont(String fontName, Int2FloatOpenHashMap advances) {
        this.mFontName = fontName;
        advances.defaultReturnValue(Float.NaN);
        this.mAdvances = advances;
    }

    @Nonnull
    public static SpaceFont create(ResourceLocation fontName, SpaceProvider.Definition definition) {
        return new SpaceFont(fontName.toString() + " / minecraft:space", new Int2FloatOpenHashMap(definition.advances()));
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public String getFullName(@Nonnull Locale locale) {
        return this.mFontName;
    }

    @Override
    public String getFamilyName(@Nonnull Locale locale) {
        return this.mFontName;
    }

    @Override
    public int getMetrics(@Nonnull FontPaint paint, FontMetricsInt fm) {
        return 0;
    }

    @Override
    public boolean hasGlyph(int ch, int vs) {
        return this.mAdvances.containsKey(ch);
    }

    @Override
    public float doSimpleLayout(char[] buf, int start, int limit, FontPaint paint, IntArrayList glyphs, FloatArrayList positions, float x, float y) {
        return this.doComplexLayout(buf, start, limit, start, limit, false, paint, glyphs, positions, null, 0, null, x, y);
    }

    @Override
    public float doComplexLayout(char[] buf, int contextStart, int contextLimit, int layoutStart, int layoutLimit, boolean isRtl, FontPaint paint, IntArrayList glyphs, FloatArrayList positions, float[] advances, int advanceOffset, Rect bounds, float x, float y) {
        float scaleUp = (float) ((int) ((double) ((float) paint.getFontSize() / TextLayoutProcessor.sBaseFontSize) + 0.5));
        float advance = 0.0F;
        for (int index = layoutStart; index < layoutLimit; index++) {
            int i = index;
            char _c1 = buf[index];
            int ch;
            if (Character.isHighSurrogate(_c1) && index + 1 < layoutLimit) {
                char _c2 = buf[index + 1];
                if (Character.isLowSurrogate(_c2)) {
                    ch = Character.toCodePoint(_c1, _c2);
                    index++;
                } else {
                    ch = _c1;
                }
            } else {
                ch = _c1;
            }
            float adv = this.getAdvance(ch);
            if (!Float.isNaN(adv)) {
                adv *= scaleUp;
                if (advances != null) {
                    advances[i - advanceOffset] = adv;
                }
                if (glyphs != null) {
                    glyphs.add(ch);
                }
                if (positions != null) {
                    positions.add(x + advance);
                    positions.add(y);
                }
                advance += adv;
            }
        }
        return advance;
    }

    @Override
    public Strike findOrCreateStrike(FontPaint paint) {
        return null;
    }

    public float getAdvance(int ch) {
        return this.mAdvances.get(ch);
    }
}