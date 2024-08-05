package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class Yuv420jToRgb implements Transform {

    private static final int SCALEBITS = 10;

    private static final int ONE_HALF = 512;

    private static final int FIX_0_71414 = FIX(0.71414);

    private static final int FIX_1_772 = FIX(1.772);

    private static final int _FIX_0_34414 = -FIX(0.34414);

    private static final int FIX_1_402 = FIX(1.402);

    @Override
    public final void transform(Picture src, Picture dst) {
        byte[] y = src.getPlaneData(0);
        byte[] u = src.getPlaneData(1);
        byte[] v = src.getPlaneData(2);
        byte[] data = dst.getPlaneData(0);
        int offLuma = 0;
        int offChroma = 0;
        int stride = dst.getWidth();
        for (int i = 0; i < dst.getHeight() >> 1; i++) {
            for (int k = 0; k < dst.getWidth() >> 1; k++) {
                int j = k << 1;
                YUVJtoRGB(y[offLuma + j], u[offChroma], v[offChroma], data, (offLuma + j) * 3);
                YUVJtoRGB(y[offLuma + j + 1], u[offChroma], v[offChroma], data, (offLuma + j + 1) * 3);
                YUVJtoRGB(y[offLuma + j + stride], u[offChroma], v[offChroma], data, (offLuma + j + stride) * 3);
                YUVJtoRGB(y[offLuma + j + stride + 1], u[offChroma], v[offChroma], data, (offLuma + j + stride + 1) * 3);
                offChroma++;
            }
            if ((dst.getWidth() & 1) != 0) {
                int j = dst.getWidth() - 1;
                YUVJtoRGB(y[offLuma + j], u[offChroma], v[offChroma], data, (offLuma + j) * 3);
                YUVJtoRGB(y[offLuma + j + stride], u[offChroma], v[offChroma], data, (offLuma + j + stride) * 3);
                offChroma++;
            }
            offLuma += 2 * stride;
        }
        if ((dst.getHeight() & 1) != 0) {
            for (int k = 0; k < dst.getWidth() >> 1; k++) {
                int j = k << 1;
                YUVJtoRGB(y[offLuma + j], u[offChroma], v[offChroma], data, (offLuma + j) * 3);
                YUVJtoRGB(y[offLuma + j + 1], u[offChroma], v[offChroma], data, (offLuma + j + 1) * 3);
                offChroma++;
            }
            if ((dst.getWidth() & 1) != 0) {
                int j = dst.getWidth() - 1;
                YUVJtoRGB(y[offLuma + j], u[offChroma], v[offChroma], data, (offLuma + j) * 3);
                offChroma++;
            }
        }
    }

    private static final int FIX(double x) {
        return (int) (x * 1024.0 + 0.5);
    }

    public static final void YUVJtoRGB(byte y, byte cb, byte cr, byte[] data, int off) {
        int y_ = y + 128 << 10;
        int add_r = FIX_1_402 * cr + 512;
        int add_g = _FIX_0_34414 * cb - FIX_0_71414 * cr + 512;
        int add_b = FIX_1_772 * cb + 512;
        int r = y_ + add_r >> 10;
        int g = y_ + add_g >> 10;
        int b = y_ + add_b >> 10;
        data[off] = (byte) MathUtil.clip(r - 128, -128, 127);
        data[off + 1] = (byte) MathUtil.clip(g - 128, -128, 127);
        data[off + 2] = (byte) MathUtil.clip(b - 128, -128, 127);
    }
}