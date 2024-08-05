package com.github.alexthe666.citadel.repack.jcodec.scale;

import java.nio.ByteBuffer;

public class ImageConvert {

    private static final int SCALEBITS = 10;

    private static final int ONE_HALF = 512;

    private static final int FIX_0_71414 = FIX(0.71414);

    private static final int FIX_1_772 = FIX(1.772);

    private static final int _FIX_0_34414 = -FIX(0.34414);

    private static final int FIX_1_402 = FIX(1.402);

    private static final int CROP = 1024;

    private static final byte[] cropTable = new byte[2304];

    private static final int[] intCropTable = new int[2304];

    private static final byte[] _y_ccir_to_jpeg = new byte[256];

    private static final byte[] _y_jpeg_to_ccir = new byte[256];

    private static final int FIX(double x) {
        return (int) (x * 1024.0 + 0.5);
    }

    public static final int ycbcr_to_rgb24(int y, int cb, int cr) {
        y <<= 10;
        cb -= 128;
        cr -= 128;
        int add_r = FIX_1_402 * cr + 512;
        int add_g = _FIX_0_34414 * cb - FIX_0_71414 * cr + 512;
        int add_b = FIX_1_772 * cb + 512;
        int r = y + add_r >> 10;
        int g = y + add_g >> 10;
        int b = y + add_b >> 10;
        int var12 = crop(r);
        int var13 = crop(g);
        int var14 = crop(b);
        return (var12 & 0xFF) << 16 | (var13 & 0xFF) << 8 | var14 & 0xFF;
    }

    static final int Y_JPEG_TO_CCIR(int y) {
        return y * FIX(0.8588235294117647) + 16896 >> 10;
    }

    static final int Y_CCIR_TO_JPEG(int y) {
        return y * FIX(1.1643835616438356) + (512 - 16 * FIX(1.1643835616438356)) >> 10;
    }

    public static final int icrop(int i) {
        return intCropTable[i + 1024];
    }

    public static final byte crop(int i) {
        return cropTable[i + 1024];
    }

    public static final byte y_ccir_to_jpeg(byte y) {
        return _y_ccir_to_jpeg[y & 255];
    }

    public static final byte y_jpeg_to_ccir(byte y) {
        return _y_jpeg_to_ccir[y & 255];
    }

    public static void YUV444toRGB888(int y, int u, int v, ByteBuffer rgb) {
        int c = y - 16;
        int d = u - 128;
        int e = v - 128;
        int r = 298 * c + 409 * e + 128 >> 8;
        int g = 298 * c - 100 * d - 208 * e + 128 >> 8;
        int b = 298 * c + 516 * d + 128 >> 8;
        rgb.put(crop(r));
        rgb.put(crop(g));
        rgb.put(crop(b));
    }

    public static void RGB888toYUV444(ByteBuffer rgb, ByteBuffer Y, ByteBuffer U, ByteBuffer V) {
        int r = rgb.get() & 255;
        int g = rgb.get() & 255;
        int b = rgb.get() & 255;
        int y = 66 * r + 129 * g + 25 * b;
        int u = -38 * r - 74 * g + 112 * b;
        int v = 112 * r - 94 * g - 18 * b;
        y = y + 128 >> 8;
        u = u + 128 >> 8;
        v = v + 128 >> 8;
        Y.put(crop(y + 16));
        U.put(crop(u + 128));
        V.put(crop(v + 128));
    }

    public static byte RGB888toY4(int r, int g, int b) {
        int y = 66 * r + 129 * g + 25 * b;
        y = y + 128 >> 8;
        return crop(y + 16);
    }

    public static byte RGB888toU4(int r, int g, int b) {
        int u = -38 * r - 74 * g + 112 * b;
        u = u + 128 >> 8;
        return crop(u + 128);
    }

    public static byte RGB888toV4(int r, int g, int b) {
        int v = 112 * r - 94 * g - 18 * b;
        v = v + 128 >> 8;
        return crop(v + 128);
    }

    static {
        for (int i = -1024; i < 0; i++) {
            cropTable[i + 1024] = 0;
            intCropTable[i + 1024] = 0;
        }
        for (int i = 0; i < 256; intCropTable[i + 1024] = i++) {
            cropTable[i + 1024] = (byte) i;
        }
        for (int i = 256; i < 1024; i++) {
            cropTable[i + 1024] = -1;
            intCropTable[i + 1024] = 255;
        }
        for (int i = 0; i < 256; i++) {
            _y_ccir_to_jpeg[i] = crop(Y_CCIR_TO_JPEG(i));
            _y_jpeg_to_ccir[i] = crop(Y_JPEG_TO_CCIR(i));
        }
    }
}