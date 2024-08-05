package me.jellysquid.mods.sodium.client.util.color;

import net.caffeinemc.mods.sodium.api.util.ColorARGB;
import net.minecraft.util.Mth;

public class BoxBlur {

    public static void blur(BoxBlur.ColorBuffer buf, BoxBlur.ColorBuffer tmp, int radius) {
        if (buf.width != tmp.width || buf.height != tmp.height) {
            throw new IllegalArgumentException("Color buffers must have same dimensions");
        } else if (!isHomogenous(buf.data)) {
            blurImpl(buf.data, tmp.data, buf.width, buf.height, radius);
            blurImpl(tmp.data, buf.data, buf.width, buf.height, radius);
        }
    }

    @Deprecated
    public static void blur(int[] data, int[] tmp, int width, int height, int radius) {
        if (!isHomogenous(data)) {
            blurImpl(data, tmp, width, height, radius);
            blurImpl(tmp, data, width, height, radius);
        }
    }

    private static void blurImpl(int[] src, int[] dst, int width, int height, int radius) {
        int multiplier = getAveragingMultiplier(radius * 2 + 1);
        for (int y = 0; y < height; y++) {
            int srcRowOffset = BoxBlur.ColorBuffer.getIndex(0, y, width);
            int color = src[srcRowOffset];
            int red = ColorARGB.unpackRed(color);
            int green = ColorARGB.unpackGreen(color);
            int blue = ColorARGB.unpackBlue(color);
            red += red * radius;
            green += green * radius;
            blue += blue * radius;
            for (int x = 1; x <= radius; x++) {
                int colorx = src[srcRowOffset + x];
                red += ColorARGB.unpackRed(colorx);
                green += ColorARGB.unpackGreen(colorx);
                blue += ColorARGB.unpackBlue(colorx);
            }
            for (int x = 0; x < width; x++) {
                dst[BoxBlur.ColorBuffer.getIndex(y, x, width)] = averageRGB(red, green, blue, multiplier);
                int colorx = src[srcRowOffset + Math.max(0, x - radius)];
                red -= ColorARGB.unpackRed(colorx);
                green -= ColorARGB.unpackGreen(colorx);
                blue -= ColorARGB.unpackBlue(colorx);
                colorx = src[srcRowOffset + Math.min(width - 1, x + radius + 1)];
                red += ColorARGB.unpackRed(colorx);
                green += ColorARGB.unpackGreen(colorx);
                blue += ColorARGB.unpackBlue(colorx);
            }
        }
    }

    private static int getAveragingMultiplier(int size) {
        return Mth.ceil(1.6777216E7 / (double) size);
    }

    public static int averageRGB(int red, int green, int blue, int multiplier) {
        int value = -16777216;
        value |= blue * multiplier >>> 24 << 0;
        value |= green * multiplier >>> 24 << 8;
        return value | red * multiplier >>> 24 << 16;
    }

    private static boolean isHomogenous(int[] array) {
        int first = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] != first) {
                return false;
            }
        }
        return true;
    }

    public static class ColorBuffer {

        protected final int[] data;

        protected final int width;

        protected final int height;

        public ColorBuffer(int width, int height) {
            this.data = new int[width * height];
            this.width = width;
            this.height = height;
        }

        public void set(int x, int y, int color) {
            this.data[getIndex(x, y, this.width)] = color;
        }

        public int get(int x, int y) {
            return this.data[getIndex(x, y, this.width)];
        }

        public static int getIndex(int x, int y, int width) {
            return y * width + x;
        }
    }
}