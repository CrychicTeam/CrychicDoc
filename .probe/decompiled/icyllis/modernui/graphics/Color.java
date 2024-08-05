package icyllis.modernui.graphics;

import icyllis.modernui.annotation.ColorInt;
import icyllis.modernui.annotation.NonNull;
import icyllis.modernui.annotation.Size;
import org.jetbrains.annotations.ApiStatus.Internal;

public class Color {

    @ColorInt
    public static final int TRANSPARENT = 0;

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

    @ColorInt
    public static int parseColor(@NonNull String colorString) {
        int index;
        if (colorString.startsWith("#")) {
            index = 1;
        } else {
            if (!colorString.startsWith("0x") && !colorString.startsWith("0X")) {
                throw new IllegalArgumentException("Unknown color prefix: " + colorString);
            }
            index = 2;
        }
        int length = colorString.length() - index;
        if (length != 6 && length != 8) {
            throw new IllegalArgumentException("Unknown color: " + colorString);
        } else {
            int color = Integer.parseUnsignedInt(colorString, index, index + length, 16);
            if (length == 6) {
                color |= -16777216;
            }
            return color;
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

    @Internal
    @ColorInt
    public static int blend(@NonNull BlendMode mode, @ColorInt int src, @ColorInt int dst) {
        return icyllis.arc3d.core.Color.blend(mode.nativeBlendMode(), src, dst);
    }

    @Internal
    public static boolean equals_within_tolerance(@Size(4L) float[] colA, @Size(4L) float[] colB, float tol) {
        for (int i = 0; i < 4; i++) {
            if (!(Math.abs(colA[i] - colB[i]) <= tol)) {
                return false;
            }
        }
        return true;
    }
}