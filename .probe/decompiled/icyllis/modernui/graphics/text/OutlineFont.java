package icyllis.modernui.graphics.text;

import icyllis.arc3d.core.Strike;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Nullable;
import icyllis.modernui.graphics.Rect;
import icyllis.modernui.util.SparseArray;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public final class OutlineFont implements Font {

    static final Graphics2D[] sGraphics = new Graphics2D[4];

    private static final String[] LOGICAL_FONT_NAMES;

    private final java.awt.Font mFont;

    private final SparseArray<java.awt.Font> mFonts = new SparseArray<>();

    private final boolean mIsLogicalFont;

    public OutlineFont(java.awt.Font font) {
        this.mFont = (java.awt.Font) Objects.requireNonNull(font);
        this.mIsLogicalFont = Arrays.stream(LOGICAL_FONT_NAMES).anyMatch(s -> s.equalsIgnoreCase(font.getName()));
    }

    public static FontRenderContext getFontRenderContext(int renderFlags) {
        return sGraphics[renderFlags].getFontRenderContext();
    }

    @Override
    public int getStyle() {
        return this.mFont.getStyle();
    }

    @Override
    public String getFullName(@NonNull Locale locale) {
        return this.mFont.getFontName(locale);
    }

    @Override
    public String getFamilyName(@NonNull Locale locale) {
        return this.mFont.getFamily(locale);
    }

    @NonNull
    public java.awt.Font chooseFont(int size) {
        if (size <= 1) {
            return this.mFont;
        } else if (size <= 96) {
            java.awt.Font value = this.mFonts.get(size);
            if (value != null) {
                return value;
            } else {
                value = this.mFont.deriveFont((float) size);
                this.mFonts.put(size, value);
                return value;
            }
        } else {
            return this.mFont.deriveFont((float) size);
        }
    }

    @Override
    public int getMetrics(@NonNull FontPaint paint, @Nullable FontMetricsInt fm) {
        if (paint.getFontStyle() != this.getStyle()) {
            throw new IllegalArgumentException();
        } else {
            java.awt.Font font = this.chooseFont(paint.getFontSize());
            FontMetrics metrics = sGraphics[paint.getRenderFlags()].getFontMetrics(font);
            int ascent = metrics.getAscent();
            int descent = metrics.getDescent();
            int leading = metrics.getLeading();
            if (fm != null) {
                fm.extendBy(-ascent, descent, leading);
            }
            return ascent + descent + leading;
        }
    }

    @Override
    public boolean hasGlyph(int ch, int vs) {
        return this.mFont.canDisplay(ch);
    }

    @Override
    public int calcGlyphScore(char[] buf, int start, int limit) {
        int offset = this.mFont.canDisplayUpTo(buf, start, limit);
        return (offset == -1 ? limit : offset) + (this.mIsLogicalFont ? -1 : 0);
    }

    @Override
    public float doSimpleLayout(char[] buf, int start, int limit, FontPaint paint, IntArrayList glyphs, FloatArrayList positions, float x, float y) {
        int style = paint.getFontStyle();
        if (style != this.getStyle()) {
            throw new IllegalArgumentException();
        } else {
            if (start != 0 || limit != buf.length) {
                buf = Arrays.copyOfRange(buf, start, limit);
            }
            GlyphVector vector = this.chooseFont(paint.getFontSize()).createGlyphVector(getFontRenderContext(paint.getRenderFlags()), buf);
            int nGlyphs = vector.getNumGlyphs();
            if (glyphs != null || positions != null) {
                for (int i = 0; i < nGlyphs; i++) {
                    if (glyphs != null) {
                        glyphs.add(vector.getGlyphCode(i));
                    }
                    if (positions != null) {
                        Point2D point = vector.getGlyphPosition(i);
                        positions.add((float) point.getX() + x);
                        positions.add((float) point.getY() + y);
                    }
                }
            }
            return (float) vector.getGlyphPosition(nGlyphs).getX();
        }
    }

    @Override
    public float doComplexLayout(char[] buf, int contextStart, int contextLimit, int layoutStart, int layoutLimit, boolean isRtl, FontPaint paint, IntArrayList glyphs, FloatArrayList positions, float[] advances, int advanceOffset, Rect bounds, float x, float y) {
        int style = paint.getFontStyle();
        if (style != this.getStyle()) {
            throw new IllegalArgumentException();
        } else {
            int layoutFlags = isRtl ? 1 : 0;
            if (layoutStart == contextStart) {
                layoutFlags |= 2;
            }
            if (layoutLimit == contextLimit) {
                layoutFlags |= 4;
            }
            java.awt.Font face = this.chooseFont(paint.getFontSize());
            FontRenderContext frc = getFontRenderContext(paint.getRenderFlags());
            GlyphVector vector = face.layoutGlyphVector(frc, buf, layoutStart, layoutLimit, layoutFlags);
            int nGlyphs = vector.getNumGlyphs();
            if (advances != null) {
                int baseFlags = isRtl ? 1 : 0;
                GraphemeBreak.forTextRun(buf, paint.mLocale, layoutStart, layoutLimit, (clusterStart, clusterLimit) -> {
                    int flags = baseFlags;
                    if (clusterStart == contextStart) {
                        flags = baseFlags | 2;
                    }
                    if (clusterLimit == contextLimit) {
                        flags |= 4;
                    }
                    GlyphVector vec = face.layoutGlyphVector(frc, buf, clusterStart, clusterLimit, flags);
                    advances[clusterStart - advanceOffset] = (float) vec.getGlyphPosition(vec.getNumGlyphs()).getX();
                });
            }
            if (glyphs != null || positions != null) {
                for (int i = 0; i < nGlyphs; i++) {
                    if (glyphs != null) {
                        glyphs.add(vector.getGlyphCode(i));
                    }
                    if (positions != null) {
                        Point2D point = vector.getGlyphPosition(i);
                        positions.add((float) point.getX() + x);
                        positions.add((float) point.getY() + y);
                    }
                }
            }
            if (bounds != null) {
                Rectangle r = vector.getPixelBounds(null, x, y);
                bounds.union(r.x, r.y, r.x + r.width, r.y + r.height);
            }
            return (float) vector.getGlyphPosition(nGlyphs).getX();
        }
    }

    @Override
    public Strike findOrCreateStrike(FontPaint paint) {
        return null;
    }

    static {
        BufferedImage image = new BufferedImage(1, 1, 2);
        for (int mask = 0; mask < 4; mask++) {
            Graphics2D graphics = image.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, (mask & 1) != 0 ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
            graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, (mask & 2) != 0 ? RenderingHints.VALUE_FRACTIONALMETRICS_ON : RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
            sGraphics[mask] = graphics;
        }
        LOGICAL_FONT_NAMES = new String[] { "Dialog", "SansSerif", "Serif", "Monospaced" };
    }
}