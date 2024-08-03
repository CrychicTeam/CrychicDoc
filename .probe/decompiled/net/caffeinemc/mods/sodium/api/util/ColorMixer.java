package net.caffeinemc.mods.sodium.api.util;

public class ColorMixer {

    private static final int CHANNEL_MASK = 16711935;

    public static int mix(int aColor, int bColor, float ratio) {
        int aRatio = (int) (256.0F * ratio);
        int bRatio = 256 - aRatio;
        int a1 = aColor >> 0 & 16711935;
        int b1 = bColor >> 0 & 16711935;
        int a2 = aColor >> 8 & 16711935;
        int b2 = bColor >> 8 & 16711935;
        int c1 = a1 * aRatio + b1 * bRatio >> 8 & 16711935;
        int c2 = a2 * aRatio + b2 * bRatio >> 8 & 16711935;
        return c1 << 0 | c2 << 8;
    }

    public static int mul(int a, int b) {
        int c0 = (a >> 0 & 0xFF) * (b >> 0 & 0xFF) >> 8;
        int c1 = (a >> 8 & 0xFF) * (b >> 8 & 0xFF) >> 8;
        int c2 = (a >> 16 & 0xFF) * (b >> 16 & 0xFF) >> 8;
        int c3 = (a >> 24 & 0xFF) * (b >> 24 & 0xFF) >> 8;
        return c0 << 0 | c1 << 8 | c2 << 16 | c3 << 24;
    }

    public static int mulSingleWithoutAlpha(int a, int b) {
        b &= 255;
        int c0 = (a >> 0 & 0xFF) * b >> 8;
        int c1 = (a >> 8 & 0xFF) * b >> 8;
        int c2 = (a >> 16 & 0xFF) * b >> 8;
        int c3 = a >> 24 & 0xFF;
        return c0 << 0 | c1 << 8 | c2 << 16 | c3 << 24;
    }
}