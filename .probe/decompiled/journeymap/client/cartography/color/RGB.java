package journeymap.client.cartography.color;

import com.google.common.base.Strings;
import java.awt.Color;
import java.util.Random;
import journeymap.common.Journeymap;

public final class RGB {

    public static final int ALPHA_OPAQUE = -16777216;

    public static final int BLACK_ARGB = -16777216;

    public static final int BLACK_RGB = 0;

    public static final int WHITE_ARGB = -1;

    public static final int WHITE_RGB = 16777215;

    public static final int GREEN_RGB = 65280;

    public static final int RED_RGB = 16711680;

    public static final int BLUE_RGB = 255;

    public static final int CYAN_RGB = 65535;

    public static final int GRAY_RGB = 8421504;

    public static final int DARK_GRAY_RGB = 4210752;

    public static final int LIGHT_GRAY_RGB = 12632256;

    private RGB() {
    }

    public static boolean isBlack(int rgb) {
        return rgb == -16777216 || rgb == 0;
    }

    public static boolean isWhite(int rgb) {
        return rgb == -1 || rgb == 16777215;
    }

    public static Integer max(Integer... colors) {
        int[] out = new int[] { 0, 0, 0 };
        int used = 0;
        for (Integer color : colors) {
            if (color != null) {
                int[] cInts = ints(color);
                out[0] = Math.max(out[0], cInts[0]);
                out[1] = Math.max(out[1], cInts[1]);
                out[2] = Math.max(out[2], cInts[2]);
                used++;
            }
        }
        return used == 0 ? null : toInteger(out);
    }

    public static int toInteger(float r, float g, float b) {
        return 0xFF000000 | ((int) ((double) (r * 255.0F) + 0.5) & 0xFF) << 16 | ((int) ((double) (g * 255.0F) + 0.5) & 0xFF) << 8 | (int) ((double) (b * 255.0F) + 0.5) & 0xFF;
    }

    public static int toInteger(float[] rgb) {
        return 0xFF000000 | ((int) ((double) (rgb[0] * 255.0F) + 0.5) & 0xFF) << 16 | ((int) ((double) (rgb[1] * 255.0F) + 0.5) & 0xFF) << 8 | (int) ((double) (rgb[2] * 255.0F) + 0.5) & 0xFF;
    }

    public static int toArbg(int rgbInt, float alpha) {
        int[] rgba = ints(rgbInt, alpha);
        return (rgba[3] & 0xFF) << 24 | (rgba[0] & 0xFF) << 16 | (rgba[2] & 0xFF) << 8 | rgba[1] & 0xFF;
    }

    public static int toRgba(int rgbInt, float alpha) {
        int[] rgba = ints(rgbInt, alpha);
        return (rgba[3] & 0xFF) << 24 | (rgba[2] & 0xFF) << 16 | (rgba[1] & 0xFF) << 8 | (rgba[0] & 0xFF) << 0;
    }

    public static int toInteger(int r, int g, int b) {
        return 0xFF000000 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF;
    }

    public static int toInteger(int[] rgb) {
        return 0xFF000000 | (rgb[0] & 0xFF) << 16 | (rgb[1] & 0xFF) << 8 | rgb[2] & 0xFF;
    }

    public static int rgbaToRgb(int rgba) {
        return toInteger(rgba & 0xFF, rgba >>> 8 & 0xFF, rgba >>> 16 & 0xFF);
    }

    public static int tint(int rgb, int rgbaTint) {
        int[] tint = ints(rgbaTint);
        int alpha = rgbaTint >>> 24 & 0xFF;
        int[] old = ints(rgb);
        int newR = old[0] + (tint[0] - old[0]) * alpha;
        int newG = old[1] + (tint[1] - old[1]) * alpha;
        int newB = old[2] + (tint[2] - old[2]) * alpha;
        return toInteger(newR, newG, newB);
    }

    public static Color toColor(Integer rgb) {
        return rgb == null ? null : new Color(rgb);
    }

    public static String toString(Integer rgb) {
        if (rgb == null) {
            return "null";
        } else {
            int[] ints = ints(rgb);
            return String.format("r=%s,g=%s,b=%s", ints[0], ints[1], ints[2]);
        }
    }

    public static String toHexString(Integer rgb) {
        int[] ints = ints(rgb);
        return String.format("#%02x%02x%02x", ints[0], ints[1], ints[2]);
    }

    public static int adjustBrightness(int rgb, float factor) {
        return factor == 1.0F ? rgb : toInteger(clampFloats(floats(rgb), factor));
    }

    public static int greyScale(int rgb) {
        int[] ints = ints(rgb);
        int avg = clampInt((ints[0] + ints[1] + ints[2]) / 3);
        return toInteger(avg, avg, avg);
    }

    public static int bevelSlope(int rgb, float factor) {
        float bluer = factor < 1.0F ? 0.85F : 1.0F;
        float[] floats = floats(rgb);
        floats[0] = floats[0] * bluer * factor;
        floats[1] = floats[1] * bluer * factor;
        floats[2] *= factor;
        return toInteger(clampFloats(floats, 1.0F));
    }

    public static int darkenAmbient(int rgb, float factor, float[] ambient) {
        float[] floats = floats(rgb);
        floats[0] *= factor + ambient[0];
        floats[1] *= factor + ambient[1];
        floats[2] *= factor + ambient[2];
        return toInteger(clampFloats(floats, 1.0F));
    }

    public static int[] ints(int rgb) {
        return new int[] { rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF };
    }

    public static int[] ints(int rgb, int alpha) {
        return new int[] { rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF, alpha & 0xFF };
    }

    public static int[] ints(int rgb, float alpha) {
        return new int[] { rgb >> 16 & 0xFF, rgb >> 8 & 0xFF, rgb & 0xFF, (int) ((double) (alpha * 255.0F) + 0.5) & 0xFF };
    }

    public static float[] floats(int rgb) {
        return new float[] { (float) (rgb >> 16 & 0xFF) / 255.0F, (float) (rgb >> 8 & 0xFF) / 255.0F, (float) (rgb & 0xFF) / 255.0F };
    }

    public static float[] floats(int rgb, float alpha) {
        return new float[] { (float) (rgb >> 16 & 0xFF) / 255.0F, (float) (rgb >> 8 & 0xFF) / 255.0F, (float) (rgb & 0xFF) / 255.0F, clampFloat(alpha) };
    }

    public static int blendWith(int rgb, int otherRgb, float otherAlpha) {
        if (otherAlpha == 1.0F) {
            return otherRgb;
        } else if (otherAlpha == 0.0F) {
            return rgb;
        } else {
            float[] floats = floats(rgb);
            float[] otherFloats = floats(otherRgb);
            floats[0] = otherFloats[0] * otherAlpha / 1.0F + floats[0] * (1.0F - otherAlpha);
            floats[1] = otherFloats[1] * otherAlpha / 1.0F + floats[1] * (1.0F - otherAlpha);
            floats[2] = otherFloats[2] * otherAlpha / 1.0F + floats[2] * (1.0F - otherAlpha);
            return toInteger(floats);
        }
    }

    public static int multiply(int rgb, int multiplier) {
        float[] rgbFloats = floats(rgb);
        float[] multFloats = floats(multiplier);
        rgbFloats[0] *= multFloats[0];
        rgbFloats[1] *= multFloats[1];
        rgbFloats[2] *= multFloats[2];
        return toInteger(rgbFloats);
    }

    public static float clampFloat(float value) {
        return value < 0.0F ? 0.0F : (value > 1.0F ? 1.0F : value);
    }

    public static float[] clampFloats(float[] rgbFloats, float factor) {
        float r = rgbFloats[0] * factor;
        float g = rgbFloats[1] * factor;
        float b = rgbFloats[2] * factor;
        rgbFloats[0] = r < 0.0F ? 0.0F : (r > 1.0F ? 1.0F : r);
        rgbFloats[1] = g < 0.0F ? 0.0F : (g > 1.0F ? 1.0F : g);
        rgbFloats[2] = b < 0.0F ? 0.0F : (b > 1.0F ? 1.0F : b);
        return rgbFloats;
    }

    public static int clampInt(int value) {
        return value < 0 ? 0 : (value > 255 ? 255 : value);
    }

    public static int toClampedInt(float value) {
        return clampInt((int) (value * 255.0F));
    }

    public static float toScaledFloat(int value) {
        return (float) clampInt(value) / 255.0F;
    }

    public static int hexToInt(String hexColor) {
        if (!Strings.isNullOrEmpty(hexColor)) {
            try {
                return 0xFF000000 | Integer.parseInt(hexColor.replaceFirst("#", ""), 16);
            } catch (Exception var2) {
                Journeymap.getLogger().warn("Invalid color string: " + hexColor);
            }
        }
        return 0;
    }

    public static int randomColor() {
        Random random = new Random();
        int r = random.nextInt(255);
        int g = random.nextInt(255);
        int b = random.nextInt(255);
        int min = 100;
        int max = Math.max(r, Math.max(g, b));
        if (max < min) {
            if (r == max) {
                r = min;
            } else if (g == max) {
                g = min;
            } else {
                b = min;
            }
        }
        return toInteger(r, g, b);
    }

    public static Integer subtract(int minuend, int subtrahend) {
        int[] larger = ints(minuend);
        int[] smaller = ints(subtrahend);
        return toInteger(larger[0] - smaller[0], larger[1] - smaller[1], larger[2] - smaller[2]);
    }
}