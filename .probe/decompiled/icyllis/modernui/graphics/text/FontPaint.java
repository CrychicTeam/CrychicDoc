package icyllis.modernui.graphics.text;

import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.graphics.MathUtil;
import icyllis.modernui.graphics.Paint;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public class FontPaint {

    public static final int NORMAL = 0;

    public static final int BOLD = 1;

    public static final int ITALIC = 2;

    public static final int BOLD_ITALIC = 3;

    public static final int FONT_STYLE_MASK = 3;

    public static final int RENDER_FLAG_ANTI_ALIAS = 1;

    public static final int RENDER_FLAG_LINEAR_METRICS = 2;

    public static final int RENDER_FLAG_MASK = 3;

    public static final int RENDER_FLAG_SHIFT = 4;

    FontCollection mFont;

    Locale mLocale;

    int mFlags;

    int mSize;

    @Internal
    public FontPaint() {
    }

    @Internal
    public FontPaint(@Nonnull FontPaint paint) {
        this.mFont = paint.mFont;
        this.mLocale = paint.mLocale;
        this.mFlags = paint.mFlags;
        this.mSize = paint.mSize;
    }

    public void set(@Nonnull FontPaint paint) {
        this.mFont = paint.mFont;
        this.mLocale = paint.mLocale;
        this.mFlags = paint.mFlags;
        this.mSize = paint.mSize;
    }

    public void setFont(@Nonnull FontCollection font) {
        this.mFont = font;
    }

    public FontCollection getFont() {
        return this.mFont;
    }

    public void setLocale(@Nonnull Locale locale) {
        this.mLocale = locale;
    }

    public Locale getLocale() {
        return this.mLocale;
    }

    public void setFontStyle(int fontStyle) {
        this.mFlags = this.mFlags & -4 | fontStyle & 3;
    }

    public int getFontStyle() {
        return this.mFlags & 3;
    }

    public void setFontSize(int fontSize) {
        this.mSize = MathUtil.clamp(fontSize, 8, 96);
    }

    public int getFontSize() {
        return this.mSize;
    }

    public void setRenderFlags(int flags) {
        this.mFlags = this.mFlags & -49 | (flags & 3) << 4;
    }

    public int getRenderFlags() {
        return this.mFlags >> 4 & 3;
    }

    public boolean isMetricAffecting(@Nonnull FontPaint paint) {
        if (this.mSize != paint.mSize) {
            return true;
        } else if (this.mFlags != paint.mFlags) {
            return true;
        } else {
            return !this.mFont.equals(paint.mFont) ? true : !this.mLocale.equals(paint.mLocale);
        }
    }

    public int getFontMetricsInt(@Nullable FontMetricsInt fm) {
        int height = 0;
        for (FontFamily family : this.getFont().getFamilies()) {
            Font font = family.getClosestMatch(this.getFontStyle());
            height = Math.max(height, font.getMetrics(this, fm));
        }
        return height;
    }

    public static int computeRenderFlags(@NonNull Paint paint) {
        return (paint.isTextAntiAlias() ? 1 : 0) | (paint.isLinearText() ? 2 : 0);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            FontPaint that = (FontPaint) o;
            if (this.mSize != that.mSize) {
                return false;
            } else if (this.mFlags != that.mFlags) {
                return false;
            } else {
                return !this.mFont.equals(that.mFont) ? false : this.mLocale.equals(that.mLocale);
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int h = this.mFont.hashCode();
        h = 31 * h + this.mLocale.hashCode();
        h = 31 * h + this.mFlags;
        return 31 * h + this.mSize;
    }

    public String toString() {
        return "FontPaint{font=" + this.mFont + ", locale=" + this.mLocale + ", flags=0x" + Integer.toHexString(this.mFlags) + ", size=" + this.mSize + "}";
    }
}