package net.caffeinemc.mods.sodium.api.util;

public class ColorARGB implements ColorU8 {

    private static final int ALPHA_COMPONENT_OFFSET = 24;

    private static final int RED_COMPONENT_OFFSET = 16;

    private static final int GREEN_COMPONENT_OFFSET = 8;

    private static final int BLUE_COMPONENT_OFFSET = 0;

    public static int pack(int r, int g, int b, int a) {
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF) << 0;
    }

    public static int pack(int r, int g, int b) {
        return pack(r, g, b, 255);
    }

    public static int unpackAlpha(int color) {
        return color >> 24 & 0xFF;
    }

    public static int unpackRed(int color) {
        return color >> 16 & 0xFF;
    }

    public static int unpackGreen(int color) {
        return color >> 8 & 0xFF;
    }

    public static int unpackBlue(int color) {
        return color >> 0 & 0xFF;
    }

    public static int toABGR(int color, int alpha) {
        return Integer.reverseBytes(color << 8 | alpha);
    }

    public static int toABGR(int color) {
        return Integer.reverseBytes(Integer.rotateLeft(color, 8));
    }

    public static int withAlpha(int rgb, int alpha) {
        return alpha << 24 | rgb & 16777215;
    }
}