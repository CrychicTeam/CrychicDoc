package icyllis.modernui.mc.text;

import com.mojang.blaze3d.vertex.VertexConsumer;
import icyllis.modernui.ModernUI;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

@Deprecated
public class GlyphManagerForge {

    public static final Marker MARKER = MarkerManager.getMarker("Glyph");

    private static GlyphManagerForge instance;

    public static String sPreferredFont;

    public static boolean sAntiAliasing;

    public static boolean sHighPrecision;

    public static boolean sEnableMipmap;

    public static int sMipmapLevel;

    public static int sResolutionLevel;

    @Deprecated
    private static final int TEXTURE_WIDTH = 256;

    @Deprecated
    private static final int TEXTURE_HEIGHT = 256;

    @Deprecated
    private static final int STRING_WIDTH = 256;

    @Deprecated
    private static final int STRING_HEIGHT = 64;

    private static final int GLYPH_BORDER = 1;

    private static final int GLYPH_SPACING = 2;

    public static final float GLYPH_OFFSET = 0.5F;

    private static final Color BG_COLOR = new Color(0, 0, 0, 0);

    public static final int TEXTURE_SIZE = 1024;

    protected final BufferedImage mGlyphImage = new BufferedImage(1024, 1024, 2);

    protected final Graphics2D mGlyphGraphics = this.mGlyphImage.createGraphics();

    protected final Object2IntMap<Font> mFontKeyMap = new Object2IntOpenHashMap();

    @Deprecated
    private BufferedImage tempStringImage;

    @Deprecated
    private Graphics2D tempStringGraphics;

    private final int[] mImageData = new int[8192];

    private final ByteBuffer mUploadBuffer = BufferUtils.createByteBuffer(this.mImageData.length);

    private final List<Font> mSelectedFonts = new ObjectArrayList();

    private final Long2ObjectMap<GlyphManagerForge.VanillaGlyph> mGlyphCache = new Long2ObjectOpenHashMap();

    private final Int2ObjectMap<GlyphManagerForge.VanillaGlyph[]> mDigitsMap = new Int2ObjectArrayMap(4);

    private int mCurrPosX = 2;

    private int mCurrPosY = 2;

    private int mCurrLineHeight = 0;

    private GlyphManagerForge() {
        instance = this;
        this.mGlyphGraphics.setBackground(BG_COLOR);
        this.mGlyphGraphics.setComposite(AlphaComposite.Src);
        this.allocateGlyphTexture();
        this.loadPreferredFonts();
        this.setRenderingHints();
    }

    @Deprecated
    public static GlyphManagerForge getInstance() {
        return instance;
    }

    public void reload() {
        this.mCurrPosX = 2;
        this.mCurrPosY = 2;
        this.mCurrLineHeight = 0;
        this.mFontKeyMap.clear();
        this.mGlyphCache.clear();
        this.mDigitsMap.clear();
        TextRenderType.clear(false);
        this.mSelectedFonts.clear();
        this.allocateGlyphTexture();
        this.loadPreferredFonts();
        this.setRenderingHints();
        ModernUI.LOGGER.debug(MARKER, "Font engine reloaded");
    }

    private void loadPreferredFonts() {
        if (StringUtils.isNotEmpty(sPreferredFont)) {
            String cfgFont = sPreferredFont;
            if (cfgFont.endsWith(".ttf") || cfgFont.endsWith(".otf") || cfgFont.endsWith(".TTF") || cfgFont.endsWith(".OTF")) {
                if (cfgFont.contains(":/") || cfgFont.contains(":\\")) {
                    try {
                        Font f = Font.createFont(0, new File(cfgFont.replaceAll("\\\\", "/")));
                        this.mSelectedFonts.add(f);
                        ModernUI.LOGGER.debug(MARKER, "Preferred font {} was loaded", f.getFamily(Locale.ROOT));
                    } catch (Exception var3) {
                        ModernUI.LOGGER.warn(MARKER, "Preferred font {} failed to load", cfgFont, var3);
                    }
                } else if (!cfgFont.contains(":")) {
                    ModernUI.LOGGER.warn(MARKER, "Preferred font {} is invalid", cfgFont);
                }
            }
        }
    }

    @Nonnull
    public Font lookupFont(int codePoint) {
        for (Font font : this.mSelectedFonts) {
            if (font.canDisplay(codePoint)) {
                return font;
            }
        }
        return (Font) this.mSelectedFonts.get(0);
    }

    public GlyphVector layoutGlyphVector(@Nonnull Font font, char[] text, int start, int limit, int flags) {
        return font.layoutGlyphVector(this.mGlyphGraphics.getFontRenderContext(), text, start, limit, flags);
    }

    @Nonnull
    public Font deriveFont(@Nonnull Font family, int style, int size) {
        family = family.deriveFont(style, (float) size);
        this.mFontKeyMap.putIfAbsent(family, this.mFontKeyMap.size());
        return family;
    }

    public float getResolutionFactor() {
        return (float) sResolutionLevel * 2.0F;
    }

    @Nonnull
    @Deprecated
    private Font lookupFont(int codePoint, int fontStyle, int fontSize) {
        for (Font font : this.mSelectedFonts) {
            if (font.canDisplay(codePoint)) {
                return font.deriveFont(fontStyle, (float) fontSize);
            }
        }
        Font fontx = (Font) this.mSelectedFonts.get(0);
        return fontx.deriveFont(fontStyle, (float) fontSize);
    }

    @Nonnull
    public GlyphManagerForge.VanillaGlyph lookupGlyph(Font font, int glyphCode) {
        long fontKey = (long) this.mFontKeyMap.getInt(font) << 32;
        return (GlyphManagerForge.VanillaGlyph) this.mGlyphCache.computeIfAbsent(fontKey | (long) glyphCode, l -> this.cacheGlyph(font, glyphCode));
    }

    @Nonnull
    @Deprecated
    private GlyphManagerForge.VanillaGlyph lookupGlyph(int codePoint, int fontStyle, int fontSize) {
        return this.lookupGlyph(this.lookupFont(codePoint, fontStyle, fontSize), codePoint);
    }

    @Nonnull
    private GlyphManagerForge.VanillaGlyph cacheGlyph(@Nonnull Font font, int glyphCode) {
        GlyphVector vector = font.createGlyphVector(this.mGlyphGraphics.getFontRenderContext(), new int[] { glyphCode });
        Rectangle renderBounds = vector.getGlyphPixelBounds(0, this.mGlyphGraphics.getFontRenderContext(), 0.0F, 0.0F);
        int renderWidth = (int) renderBounds.getWidth();
        int renderHeight = (int) renderBounds.getHeight();
        if (this.mCurrPosX + renderWidth + 2 >= 1024) {
            this.mCurrPosX = 2;
            this.mCurrPosY = this.mCurrPosY + this.mCurrLineHeight + 4;
            this.mCurrLineHeight = 0;
        }
        if (this.mCurrPosY + renderHeight + 2 >= 1024) {
            this.mCurrPosX = 2;
            this.mCurrPosY = 2;
            this.allocateGlyphTexture();
        }
        int baselineX = (int) renderBounds.getX();
        int baselineY = (int) renderBounds.getY();
        float advance = vector.getGlyphMetrics(0).getAdvanceX();
        this.mGlyphGraphics.setFont(font);
        int x = this.mCurrPosX - 1;
        int y = this.mCurrPosY - 1;
        int width = renderWidth + 2;
        int height = renderHeight + 2;
        this.mGlyphGraphics.drawGlyphVector(vector, (float) (this.mCurrPosX - baselineX), (float) (this.mCurrPosY - baselineY));
        this.uploadTexture(x, y, width, height);
        this.mCurrLineHeight = Math.max(this.mCurrLineHeight, renderHeight);
        this.mCurrPosX += renderWidth + 4;
        float f = this.getResolutionFactor();
        return new GlyphManagerForge.VanillaGlyph(advance / f, (float) baselineX / f, (float) baselineY / f, (float) width / f, (float) height / f, (float) x / 1024.0F, (float) y / 1024.0F, (float) (x + width) / 1024.0F, (float) (y + height) / 1024.0F);
    }

    public GlyphManagerForge.VanillaGlyph[] lookupDigits(Font font) {
        int fontKey = this.mFontKeyMap.getInt(font);
        return (GlyphManagerForge.VanillaGlyph[]) this.mDigitsMap.computeIfAbsent(fontKey, l -> this.cacheDigits(font));
    }

    @Deprecated
    private GlyphManagerForge.VanillaGlyph[] lookupDigits(int fontStyle, int fontSize) {
        return this.lookupDigits(this.lookupFont(48, fontStyle, fontSize));
    }

    @Nonnull
    private GlyphManagerForge.VanillaGlyph[] cacheDigits(@Nonnull Font font) {
        GlyphManagerForge.VanillaGlyph[] digits = new GlyphManagerForge.VanillaGlyph[10];
        char[] chars = new char[1];
        this.mGlyphGraphics.setFont(font);
        float standardAdvance = 0.0F;
        int standardRenderWidth = 0;
        float f = this.getResolutionFactor();
        for (int i = 0; i < 10; i++) {
            chars[0] = (char) (48 + i);
            GlyphVector vector = font.createGlyphVector(this.mGlyphGraphics.getFontRenderContext(), chars);
            Rectangle renderBounds = vector.getGlyphPixelBounds(0, this.mGlyphGraphics.getFontRenderContext(), 0.0F, 0.0F);
            int renderWidth = (int) renderBounds.getWidth();
            int renderHeight = (int) renderBounds.getHeight();
            if (i == 0) {
                if (this.mCurrPosX + renderWidth + 2 >= 1024) {
                    this.mCurrPosX = 2;
                    this.mCurrPosY = this.mCurrPosY + this.mCurrLineHeight + 4;
                    this.mCurrLineHeight = 0;
                }
            } else if (this.mCurrPosX + standardRenderWidth + 2 >= 1024) {
                this.mCurrPosX = 2;
                this.mCurrPosY = this.mCurrPosY + this.mCurrLineHeight + 4;
                this.mCurrLineHeight = 0;
            }
            if (this.mCurrPosY + renderHeight + 2 >= 1024) {
                this.mCurrPosX = 2;
                this.mCurrPosY = 2;
                this.allocateGlyphTexture();
            }
            int baselineX = (int) renderBounds.getX();
            int baselineY = (int) renderBounds.getY();
            if (i == 0) {
                standardAdvance = vector.getGlyphMetrics(0).getAdvanceX();
                standardRenderWidth = renderWidth;
            }
            int x = this.mCurrPosX - 1;
            int y = this.mCurrPosY - 1;
            int width;
            if (i == 0) {
                width = renderWidth + 2;
            } else {
                width = standardRenderWidth + 2;
            }
            int height = renderHeight + 2;
            if (i == 0) {
                this.mGlyphGraphics.drawString(String.valueOf(chars), this.mCurrPosX - baselineX, this.mCurrPosY - baselineY);
            } else {
                int offset = Math.round((standardAdvance - vector.getGlyphMetrics(0).getAdvanceX()) / 2.0F);
                this.mGlyphGraphics.drawString(String.valueOf(chars), this.mCurrPosX + offset - baselineX, this.mCurrPosY - baselineY);
            }
            this.uploadTexture(x, y, width, height);
            this.mCurrLineHeight = Math.max(this.mCurrLineHeight, renderHeight);
            this.mCurrPosX += standardRenderWidth + 4;
            digits[i] = new GlyphManagerForge.VanillaGlyph(standardAdvance / f, (float) baselineX / f, (float) baselineY / f, (float) width / f, (float) height / f, (float) x / 1024.0F, (float) y / 1024.0F, (float) (x + width) / 1024.0F, (float) (y + height) / 1024.0F);
        }
        return digits;
    }

    @Deprecated
    private void cacheGlyphs(Font font, char[] text, int start, int limit, int layoutFlags) {
        GlyphVector vector = this.layoutGlyphVector(font, text, start, limit, layoutFlags);
        Rectangle vectorBounds = null;
        long fontKey = (long) this.mFontKeyMap.get(font).intValue() << 32;
        int numGlyphs = vector.getNumGlyphs();
        Rectangle dirty = null;
        boolean vectorRendered = false;
        for (int index = 0; index < numGlyphs; index++) {
            int glyphCode = vector.getGlyphCode(index);
            if (!this.mGlyphCache.containsKey(fontKey | (long) glyphCode)) {
                if (!vectorRendered) {
                    vectorRendered = true;
                    for (int i = 0; i < numGlyphs; i++) {
                        Point2D pos = vector.getGlyphPosition(i);
                        pos.setLocation(pos.getX() + (double) (2 * i), pos.getY());
                        vector.setGlyphPosition(i, pos);
                    }
                    vectorBounds = vector.getPixelBounds(this.mGlyphGraphics.getFontRenderContext(), 0.0F, 0.0F);
                    if (vectorBounds.width > this.tempStringImage.getWidth() || vectorBounds.height > this.tempStringImage.getHeight()) {
                        int width = Math.max(vectorBounds.width, this.tempStringImage.getWidth());
                        int height = Math.max(vectorBounds.height, this.tempStringImage.getHeight());
                        this.allocateStringImage(width, height);
                    }
                    this.tempStringGraphics.clearRect(0, 0, vectorBounds.width, vectorBounds.height);
                    this.tempStringGraphics.drawGlyphVector(vector, (float) (-vectorBounds.x), (float) (-vectorBounds.y));
                }
                Rectangle rect = vector.getGlyphPixelBounds(index, null, (float) (-vectorBounds.x), (float) (-vectorBounds.y));
                if (this.mCurrPosX + rect.width + 2 > 256) {
                    this.mCurrPosX = 2;
                    this.mCurrPosY = this.mCurrPosY + this.mCurrLineHeight + 2;
                    this.mCurrLineHeight = 0;
                }
                if (this.mCurrPosY + rect.height + 2 > 256) {
                    this.updateTexture(dirty);
                    this.allocateGlyphTexture();
                    this.allocateStringImage(256, 64);
                    this.mCurrPosY = this.mCurrPosX = 2;
                    this.mCurrLineHeight = 0;
                    this.cacheGlyphs(font, text, start + index, limit, layoutFlags);
                    return;
                }
                if (rect.height > this.mCurrLineHeight) {
                    this.mCurrLineHeight = rect.height;
                }
                this.mGlyphGraphics.drawImage(this.tempStringImage, this.mCurrPosX, this.mCurrPosY, this.mCurrPosX + rect.width, this.mCurrPosY + rect.height, rect.x, rect.y, rect.x + rect.width, rect.y + rect.height, null);
                rect.setLocation(this.mCurrPosX, this.mCurrPosY);
                if (dirty == null) {
                    dirty = new Rectangle(this.mCurrPosX, this.mCurrPosY, rect.width, rect.height);
                } else {
                    dirty.add(rect);
                }
                this.mCurrPosX = this.mCurrPosX + rect.width + 2;
            }
        }
        this.updateTexture(dirty);
    }

    @Deprecated
    private void updateTexture(@Nullable Rectangle dirty) {
    }

    private void uploadTexture(int x, int y, int width, int height) {
        this.updateImageBuffer(x, y, width, height);
        if (sEnableMipmap && sMipmapLevel > 0) {
        }
    }

    private void updateImageBuffer(int x, int y, int width, int height) {
        this.mGlyphImage.getRGB(x, y, width, height, this.mImageData, 0, width);
        this.mUploadBuffer.clear();
        int size = width * height;
        for (int i = 0; i < size; i++) {
            this.mUploadBuffer.put((byte) (this.mImageData[i] >>> 24));
        }
        this.mUploadBuffer.flip();
    }

    private void setRenderingHints() {
        this.mGlyphGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (sAntiAliasing) {
            this.mGlyphGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        } else {
            this.mGlyphGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        }
        if (sHighPrecision) {
            this.mGlyphGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        } else {
            this.mGlyphGraphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
        }
    }

    private void allocateGlyphTexture() {
        this.mGlyphGraphics.clearRect(0, 0, 1024, 1024);
        int mipmapLevel = sEnableMipmap ? sMipmapLevel : 0;
        int size = 256;
        for (int i = 0; i < 256; i++) {
            int x = (i & 15) << 6;
            int y = i / 16 << 6;
            this.uploadTexture(x, y, 64, 64);
        }
    }

    @Deprecated
    private void allocateStringImage(int width, int height) {
    }

    @Deprecated
    public static class VanillaGlyph {

        private TextRenderType renderType;

        private TextRenderType seeThroughType;

        private final float advance;

        private final float baselineX;

        private final float baselineY;

        private final float width;

        private final float height;

        private final float u1;

        private final float v1;

        private final float u2;

        private final float v2;

        public VanillaGlyph(float advance, float baselineX, float baselineY, float width, float height, float u1, float v1, float u2, float v2) {
            this.advance = advance;
            this.baselineX = baselineX;
            this.baselineY = baselineY;
            this.width = width;
            this.height = height;
            this.u1 = u1;
            this.v1 = v1;
            this.u2 = u2;
            this.v2 = v2;
        }

        public void drawGlyph(@Nonnull VertexConsumer builder, float x, float y, int r, int g, int b, int a) {
            x += this.baselineX;
            y += this.baselineY;
            builder.vertex((double) x, (double) y, 0.0).color(r, g, b, a).uv(this.u1, this.v1).endVertex();
            builder.vertex((double) x, (double) (y + this.height), 0.0).color(r, g, b, a).uv(this.u1, this.v2).endVertex();
            builder.vertex((double) (x + this.width), (double) (y + this.height), 0.0).color(r, g, b, a).uv(this.u2, this.v2).endVertex();
            builder.vertex((double) (x + this.width), (double) y, 0.0).color(r, g, b, a).uv(this.u2, this.v1).endVertex();
        }

        public void drawGlyph(Matrix4f matrix, @Nonnull MultiBufferSource buffer, float x, float y, int r, int g, int b, int a, boolean seeThrough, int packedLight) {
            VertexConsumer builder = buffer.getBuffer(seeThrough ? this.seeThroughType : this.renderType);
            x += this.baselineX;
            y += this.baselineY;
            builder.vertex(matrix, x, y, 0.0F).color(r, g, b, a).uv(this.u1, this.v1).uv2(packedLight).endVertex();
            builder.vertex(matrix, x, y + this.height, 0.0F).color(r, g, b, a).uv(this.u1, this.v2).uv2(packedLight).endVertex();
            builder.vertex(matrix, x + this.width, y + this.height, 0.0F).color(r, g, b, a).uv(this.u2, this.v2).uv2(packedLight).endVertex();
            builder.vertex(matrix, x + this.width, y, 0.0F).color(r, g, b, a).uv(this.u2, this.v1).uv2(packedLight).endVertex();
        }

        public float getAdvance() {
            return this.advance;
        }
    }
}