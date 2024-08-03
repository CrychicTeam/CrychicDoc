package icyllis.modernui.mc.text;

import com.google.gson.JsonParseException;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.SheetGlyphInfo;
import icyllis.arc3d.core.Strike;
import icyllis.arc3d.engine.GpuResource;
import icyllis.arc3d.opengl.GLBackendFormat;
import icyllis.arc3d.opengl.GLCaps;
import icyllis.arc3d.opengl.GLCore;
import icyllis.arc3d.opengl.GLTexture;
import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.graphics.Bitmap;
import icyllis.modernui.graphics.BitmapFactory;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.graphics.font.GLFontAtlas;
import icyllis.modernui.graphics.font.GlyphManager;
import icyllis.modernui.graphics.text.Font;
import icyllis.modernui.graphics.text.FontMetricsInt;
import icyllis.modernui.graphics.text.FontPaint;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.io.InputStream;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.gui.font.providers.BitmapProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

public class BitmapFont implements Font, AutoCloseable {

    private final ResourceLocation mName;

    private Bitmap mBitmap;

    private final Int2ObjectMap<BitmapFont.Glyph> mGlyphs = new Int2ObjectOpenHashMap();

    private GLTexture mTexture;

    private final int mAscent;

    private final int mDescent;

    private final int mSpriteWidth;

    private final int mSpriteHeight;

    private final float mScaleFactor;

    private BitmapFont(ResourceLocation name, Bitmap bitmap, int[][] grid, int rows, int cols, int height, int ascent) {
        this.mName = name;
        this.mBitmap = bitmap;
        this.mAscent = ascent;
        this.mDescent = height - ascent;
        this.mSpriteWidth = bitmap.getWidth() / cols;
        this.mSpriteHeight = bitmap.getHeight() / rows;
        this.mScaleFactor = (float) height / (float) this.mSpriteHeight;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int ch = grid[r][c];
                if (ch != 0) {
                    int actualWidth = getActualGlyphWidth(bitmap, this.mSpriteWidth, this.mSpriteHeight, c, r);
                    BitmapFont.Glyph glyph = new BitmapFont.Glyph(Math.round((float) actualWidth * this.mScaleFactor) + 1);
                    glyph.x = 0;
                    glyph.y = (short) (-this.mAscent * 8);
                    glyph.width = (short) Math.round((float) this.mSpriteWidth * this.mScaleFactor * 8.0F);
                    glyph.height = (short) Math.round((float) this.mSpriteHeight * this.mScaleFactor * 8.0F);
                    glyph.u1 = (float) (c * this.mSpriteWidth) / (float) bitmap.getWidth();
                    glyph.v1 = (float) (r * this.mSpriteHeight) / (float) bitmap.getHeight();
                    glyph.u2 = (float) (c * this.mSpriteWidth + this.mSpriteWidth) / (float) bitmap.getWidth();
                    glyph.v2 = (float) (r * this.mSpriteHeight + this.mSpriteHeight) / (float) bitmap.getHeight();
                    if (this.mGlyphs.put(ch, glyph) != null) {
                        ModernUI.LOGGER.warn("Codepoint '{}' declared multiple times in {}", Integer.toHexString(ch), this.mName);
                    }
                }
            }
        }
    }

    @Nonnull
    public static BitmapFont create(BitmapProvider.Definition definition, ResourceManager manager) {
        int height = definition.height();
        int ascent = definition.ascent();
        if (ascent > height) {
            throw new JsonParseException("Ascent " + ascent + " higher than height " + height);
        } else {
            int[][] grid = definition.codepointGrid();
            if (grid.length != 0 && grid[0].length != 0) {
                int rows = grid.length;
                int cols = grid[0].length;
                ResourceLocation file = definition.file();
                ResourceLocation location = file.withPrefix("textures/");
                try {
                    InputStream stream = manager.m_215595_(location);
                    BitmapFont var12;
                    try {
                        BitmapFactory.Options opts = new BitmapFactory.Options();
                        opts.inPreferredFormat = Bitmap.Format.RGBA_8888;
                        Bitmap bitmap = BitmapFactory.decodeStream(stream, opts);
                        Objects.requireNonNull(bitmap);
                        var12 = new BitmapFont(file, bitmap, grid, rows, cols, height, ascent);
                    } catch (Throwable var14) {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (Throwable var13) {
                                var14.addSuppressed(var13);
                            }
                        }
                        throw var14;
                    }
                    if (stream != null) {
                        stream.close();
                    }
                    return var12;
                } catch (Exception var15) {
                    throw new RuntimeException(var15);
                }
            } else {
                throw new JsonParseException("Expected to find data in chars, found none.");
            }
        }
    }

    private static int getActualGlyphWidth(Bitmap bitmap, int width, int height, int col, int row) {
        int i;
        for (i = width - 1; i >= 0; i--) {
            int x = col * width + i;
            for (int j = 0; j < height; j++) {
                int y = row * height + j;
                if (bitmap.getPixelARGB(x, y) >>> 24 != 0) {
                    return i + 1;
                }
            }
        }
        return i + 1;
    }

    private void createTextureLazy() {
        try {
            this.mTexture = (GLTexture) Core.requireDirectContext().getResourceProvider().createTexture(this.mBitmap.getWidth(), this.mBitmap.getHeight(), GLBackendFormat.make(32856), 1, 1, this.mBitmap.getColorType(), this.mBitmap.getColorType(), this.mBitmap.getRowStride(), this.mBitmap.getAddress(), this.mName.toString());
            Objects.requireNonNull(this.mTexture, "Failed to create font texture");
            int boundTexture = GLCore.glGetInteger(32873);
            GLCore.glBindTexture(3553, this.mTexture.getHandle());
            GLCore.glTexParameteri(3553, 10240, 9728);
            GLCore.glTexParameteri(3553, 10241, 9728);
            GLCore.glBindTexture(3553, boundTexture);
        } finally {
            this.mBitmap.close();
            this.mBitmap = null;
        }
    }

    public void dumpAtlas(String path) {
        ModernUI.LOGGER.info(GlyphManager.MARKER, "BitmapFont: {}, glyphs: {}, texture: {}", this.mName, this.mGlyphs.size(), this.mTexture);
        if (path != null && this.mTexture != null && Core.isOnRenderThread()) {
            GLFontAtlas.dumpAtlas((GLCaps) Core.requireDirectContext().getCaps(), this.mTexture, Bitmap.Format.RGBA_8888, path);
        }
    }

    @Override
    public int getStyle() {
        return 0;
    }

    @Override
    public String getFullName(@Nonnull Locale locale) {
        return this.mName.toString();
    }

    @Override
    public String getFamilyName(@Nonnull Locale locale) {
        return this.mName.toString();
    }

    @Override
    public int getMetrics(@Nonnull FontPaint paint, FontMetricsInt fm) {
        return 0;
    }

    @Nullable
    public BitmapFont.Glyph getGlyph(int ch) {
        BitmapFont.Glyph glyph = (BitmapFont.Glyph) this.mGlyphs.get(ch);
        if (glyph != null && this.mBitmap != null) {
            this.createTextureLazy();
            assert this.mBitmap == null;
        }
        return glyph;
    }

    @Nullable
    public BitmapFont.Glyph getGlyphInfo(int ch) {
        return (BitmapFont.Glyph) this.mGlyphs.get(ch);
    }

    public int getCurrentTexture() {
        return this.mTexture != null ? this.mTexture.getHandle() : 0;
    }

    public int getAscent() {
        return this.mAscent;
    }

    public int getDescent() {
        return this.mDescent;
    }

    public int getSpriteWidth() {
        return this.mSpriteWidth;
    }

    public int getSpriteHeight() {
        return this.mSpriteHeight;
    }

    public float getScaleFactor() {
        return this.mScaleFactor;
    }

    @Override
    public boolean hasGlyph(int ch, int vs) {
        return this.mGlyphs.containsKey(ch);
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
            BitmapFont.Glyph glyph = this.getGlyphInfo(ch);
            if (glyph != null) {
                float adv = glyph.advance * scaleUp;
                if (advances != null) {
                    advances[i - advanceOffset] = adv;
                }
                if (glyphs != null) {
                    glyphs.add(ch);
                }
                if (positions != null) {
                    positions.add(x + advance + scaleUp * 0.5F);
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

    public int hashCode() {
        int result = 0;
        result = 31 * result + this.mName.hashCode();
        result = 31 * result + this.mAscent;
        result = 31 * result + this.mDescent;
        result = 31 * result + this.mSpriteWidth;
        result = 31 * result + this.mSpriteHeight;
        return 31 * result + Float.hashCode(this.mScaleFactor);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            BitmapFont that = (BitmapFont) o;
            if (this.mAscent != that.mAscent) {
                return false;
            } else if (this.mDescent != that.mDescent) {
                return false;
            } else if (this.mSpriteWidth != that.mSpriteWidth) {
                return false;
            } else if (this.mSpriteHeight != that.mSpriteHeight) {
                return false;
            } else {
                return this.mScaleFactor != that.mScaleFactor ? false : this.mName.equals(that.mName);
            }
        } else {
            return false;
        }
    }

    public void close() {
        if (this.mBitmap != null) {
            this.mBitmap.close();
            this.mBitmap = null;
        }
        this.mTexture = GpuResource.move(this.mTexture);
    }

    public static class Glyph extends icyllis.modernui.graphics.font.BakedGlyph implements GlyphInfo {

        public final float advance;

        public Glyph(int advance) {
            this.advance = (float) advance;
        }

        @Override
        public float getAdvance() {
            return this.advance;
        }

        @Nonnull
        @Override
        public BakedGlyph bake(@Nonnull Function<SheetGlyphInfo, BakedGlyph> function) {
            return EmptyGlyph.INSTANCE;
        }
    }
}