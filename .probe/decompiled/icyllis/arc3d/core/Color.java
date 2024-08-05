package icyllis.arc3d.core;

import javax.annotation.Nonnull;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class Color {

    @ColorInt
    public static final int TRANSPARENT = 0;

    @ColorInt
    public static final int BLACK = -16777216;

    @ColorInt
    public static final int DKGRAY = -12303292;

    @ColorInt
    public static final int GRAY = -7829368;

    @ColorInt
    public static final int LTGRAY = -3355444;

    @ColorInt
    public static final int WHITE = -1;

    @ColorInt
    public static final int RED = -65536;

    @ColorInt
    public static final int GREEN = -16711936;

    @ColorInt
    public static final int BLUE = -16776961;

    @ColorInt
    public static final int YELLOW = -256;

    @ColorInt
    public static final int CYAN = -16711681;

    @ColorInt
    public static final int MAGENTA = -65281;

    public static final int COLOR_CHANNEL_R = 0;

    public static final int COLOR_CHANNEL_G = 1;

    public static final int COLOR_CHANNEL_B = 2;

    public static final int COLOR_CHANNEL_A = 3;

    public static final int COLOR_CHANNEL_FLAG_RED = 1;

    public static final int COLOR_CHANNEL_FLAG_GREEN = 2;

    public static final int COLOR_CHANNEL_FLAG_BLUE = 4;

    public static final int COLOR_CHANNEL_FLAG_ALPHA = 8;

    public static final int COLOR_CHANNEL_FLAG_GRAY = 16;

    public static final int COLOR_CHANNEL_FLAGS_RG = 3;

    public static final int COLOR_CHANNEL_FLAGS_RGB = 7;

    public static final int COLOR_CHANNEL_FLAGS_RGBA = 15;

    @Internal
    public float mR;

    @Internal
    public float mG;

    @Internal
    public float mB;

    @Internal
    public float mA;

    public static int alpha(@ColorInt int color) {
        return color >>> 24;
    }

    public static int red(@ColorInt int color) {
        return color >> 16 & 0xFF;
    }

    public static int green(@ColorInt int color) {
        return color >> 8 & 0xFF;
    }

    public static int blue(@ColorInt int color) {
        return color & 0xFF;
    }

    @ColorInt
    public static int alpha(@ColorInt int color, int alpha) {
        return color & 16777215 | alpha << 24;
    }

    @ColorInt
    public static int rgb(int red, int green, int blue) {
        return 0xFF000000 | red << 16 | green << 8 | blue;
    }

    @ColorInt
    public static int rgb(float red, float green, float blue) {
        return 0xFF000000 | (int) (red * 255.0F + 0.5F) << 16 | (int) (green * 255.0F + 0.5F) << 8 | (int) (blue * 255.0F + 0.5F);
    }

    @ColorInt
    public static int argb(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    @ColorInt
    public static int argb(float alpha, float red, float green, float blue) {
        return (int) (alpha * 255.0F + 0.5F) << 24 | (int) (red * 255.0F + 0.5F) << 16 | (int) (green * 255.0F + 0.5F) << 8 | (int) (blue * 255.0F + 0.5F);
    }

    public Color() {
    }

    public Color(@ColorInt int color) {
        this.mR = (float) (color >> 16 & 0xFF) / 255.0F;
        this.mG = (float) (color >> 8 & 0xFF) / 255.0F;
        this.mB = (float) (color & 0xFF) / 255.0F;
        this.mA = (float) (color >>> 24) / 255.0F;
    }

    public Color(float r, float g, float b, float a) {
        this.mR = r;
        this.mG = g;
        this.mB = b;
        this.mA = a;
    }

    public Color(Color color) {
        this.mR = color.mR;
        this.mG = color.mG;
        this.mB = color.mB;
        this.mA = color.mA;
    }

    public void set(@ColorInt int color) {
        this.mR = (float) (color >> 16 & 0xFF) / 255.0F;
        this.mG = (float) (color >> 8 & 0xFF) / 255.0F;
        this.mB = (float) (color & 0xFF) / 255.0F;
        this.mA = (float) (color >>> 24) / 255.0F;
    }

    public void set(float r, float g, float b, float a) {
        this.mR = r;
        this.mG = g;
        this.mB = b;
        this.mA = a;
    }

    public void set(Color color) {
        this.mR = color.mR;
        this.mG = color.mG;
        this.mB = color.mB;
        this.mA = color.mA;
    }

    @ColorInt
    public int toArgb() {
        return (int) (this.mA * 255.0F + 0.5F) << 24 | (int) (this.mR * 255.0F + 0.5F) << 16 | (int) (this.mG * 255.0F + 0.5F) << 8 | (int) (this.mB * 255.0F + 0.5F);
    }

    public float red() {
        return this.mR;
    }

    public float green() {
        return this.mG;
    }

    public float blue() {
        return this.mB;
    }

    public float alpha() {
        return this.mA;
    }

    public void red(float red) {
        this.mR = red;
    }

    public void green(float green) {
        this.mG = green;
    }

    public void blue(float blue) {
        this.mB = blue;
    }

    public void alpha(float alpha) {
        this.mA = alpha;
    }

    public boolean isOpaque() {
        return this.mA == 1.0F;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Color color = (Color) o;
            return Float.floatToIntBits(color.mR) == Float.floatToIntBits(this.mR) && Float.floatToIntBits(color.mG) == Float.floatToIntBits(this.mG) && Float.floatToIntBits(color.mB) == Float.floatToIntBits(this.mB) && Float.floatToIntBits(color.mA) == Float.floatToIntBits(this.mA);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = Float.floatToIntBits(this.mR);
        result = 31 * result + Float.floatToIntBits(this.mG);
        result = 31 * result + Float.floatToIntBits(this.mB);
        return 31 * result + Float.floatToIntBits(this.mA);
    }

    @Nonnull
    public String toString() {
        return "Color(" + this.mR + ", " + this.mG + ", " + this.mB + ", " + this.mA + ")";
    }

    @ColorInt
    public static int parseColor(@Nonnull String s) {
        if (s.charAt(0) == '#') {
            int color = Integer.parseUnsignedInt(s.substring(1), 16);
            if (s.length() == 7) {
                color |= -16777216;
            } else if (s.length() != 9) {
                throw new IllegalArgumentException("Unknown color: " + s);
            }
            return color;
        } else if (s.startsWith("0x")) {
            int color = Integer.parseUnsignedInt(s.substring(2), 16);
            if (s.length() == 8) {
                color |= -16777216;
            } else if (s.length() != 10) {
                throw new IllegalArgumentException("Unknown color: " + s);
            }
            return color;
        } else {
            throw new IllegalArgumentException("Unknown color prefix: " + s);
        }
    }

    public static void RGBToHSV(int r, int g, int b, float[] hsv) {
        int max = Math.max(r, Math.max(g, b));
        int min = Math.min(r, Math.min(g, b));
        int delta = max - min;
        if (delta == 0) {
            hsv[0] = 0.0F;
            hsv[1] = 0.0F;
            hsv[2] = (float) max / 255.0F;
        } else {
            float h;
            if (max == r) {
                h = (float) (g - b) / (float) delta;
            } else if (max == g) {
                h = 2.0F + (float) (b - r) / (float) delta;
            } else {
                h = 4.0F + (float) (r - g) / (float) delta;
            }
            h *= 60.0F;
            if (h < 0.0F) {
                h += 360.0F;
            }
            hsv[0] = h;
            hsv[1] = (float) delta / (float) max;
            hsv[2] = (float) max / 255.0F;
        }
    }

    public static void RGBToHSV(int color, float[] hsv) {
        RGBToHSV(red(color), green(color), blue(color), hsv);
    }

    public static int HSVToColor(float h, float s, float v) {
        s = MathUtil.clamp(s, 0.0F, 1.0F);
        v = MathUtil.clamp(v, 0.0F, 1.0F);
        if (s <= 9.765625E-4F) {
            int i = (int) (v * 255.0F + 0.5F);
            return i << 16 | i << 8 | i;
        } else {
            float hx = !(h < 0.0F) && !(h >= 360.0F) ? h / 60.0F : 0.0F;
            int w = (int) hx;
            float f = hx - (float) w;
            float p = v * (1.0F - s);
            float q = v * (1.0F - s * f);
            float t = v * (1.0F - s * (1.0F - f));
            float r;
            float g;
            float b;
            switch(w) {
                case 0:
                    r = v;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = v;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = v;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = v;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = v;
                    break;
                default:
                    r = v;
                    g = p;
                    b = q;
            }
            return (int) (r * 255.0F + 0.5F) << 16 | (int) (g * 255.0F + 0.5F) << 8 | (int) (b * 255.0F + 0.5F);
        }
    }

    public static int HSVToColor(float[] hsv) {
        return HSVToColor(hsv[0], hsv[1], hsv[2]);
    }

    public static float GammaToLinear(float x) {
        return (double) x < 0.04045 ? x / 12.92F : (float) Math.pow(((double) x + 0.055) / 1.055, 2.4);
    }

    public static float LinearToGamma(float x) {
        return (double) x < 0.0031308049535603713 ? x * 12.92F : (float) Math.pow((double) x, 0.4166666666666667) * 1.055F - 0.055F;
    }

    public static void GammaToLinear(float[] col) {
        col[0] = GammaToLinear(col[0]);
        col[1] = GammaToLinear(col[1]);
        col[2] = GammaToLinear(col[2]);
    }

    public static void LinearToGamma(float[] col) {
        col[0] = LinearToGamma(col[0]);
        col[1] = LinearToGamma(col[1]);
        col[2] = LinearToGamma(col[2]);
    }

    public static float luminance(float r, float g, float b) {
        return 0.2126F * r + 0.7152F * g + 0.0722F * b;
    }

    public static float luminance(float[] col) {
        return luminance(col[0], col[1], col[2]);
    }

    public static float lightness(float lum) {
        return (double) lum <= 0.008856451679035631 ? lum * 24389.0F / 27.0F : (float) Math.pow((double) lum, 0.3333333333333333) * 116.0F - 16.0F;
    }

    @ColorInt
    public static int blend(@Nonnull BlendMode mode, @ColorInt int src, @ColorInt int dst) {
        switch(mode) {
            case CLEAR:
                return 0;
            case SRC:
                return src;
            case DST:
                return dst;
            default:
                float[] src4 = load_and_premul(src);
                float[] dst4 = load_and_premul(dst);
                mode.apply(src4, dst4, dst4);
                float a = MathUtil.clamp(dst4[3], 0.0F, 1.0F);
                if (a == 0.0F) {
                    return 0;
                } else {
                    int result = (int) (a * 255.0F + 0.5F) << 24;
                    a = 255.0F / a;
                    for (int i = 0; i < 3; i++) {
                        result |= (int) MathUtil.clamp(dst4[2 - i] * a + 0.5F, 0.0F, 255.0F) << (i << 3);
                    }
                    return result;
                }
        }
    }

    @Nonnull
    public static float[] load_and_premul(int col) {
        float[] col4 = new float[4];
        float a = (col4[3] = (float) (col >>> 24) * 0.003921569F) * 0.003921569F;
        col4[0] = (float) (col >> 16 & 0xFF) * a;
        col4[1] = (float) (col >> 8 & 0xFF) * a;
        col4[2] = (float) (col & 0xFF) * a;
        return col4;
    }
}