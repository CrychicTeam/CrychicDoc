package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class Yuv422pToRgb implements Transform {

    @Override
    public void transform(Picture src, Picture dst) {
        byte[] y = src.getPlaneData(0);
        byte[] u = src.getPlaneData(1);
        byte[] v = src.getPlaneData(2);
        byte[] data = dst.getPlaneData(0);
        int offLuma = 0;
        int offChroma = 0;
        for (int i = 0; i < dst.getHeight(); i++) {
            for (int j = 0; j < dst.getWidth(); j += 2) {
                YUV444toRGB888(y[offLuma], u[offChroma], v[offChroma], data, offLuma * 3);
                YUV444toRGB888(y[offLuma + 1], u[offChroma], v[offChroma], data, (offLuma + 1) * 3);
                offLuma += 2;
                offChroma++;
            }
        }
    }

    public static final void YUV444toRGB888(byte y, byte u, byte v, byte[] data, int off) {
        int c = y + 112;
        int r = 298 * c + 409 * v + 128 >> 8;
        int g = 298 * c - 100 * u - 208 * v + 128 >> 8;
        int b = 298 * c + 516 * u + 128 >> 8;
        data[off] = (byte) (MathUtil.clip(r, 0, 255) - 128);
        data[off + 1] = (byte) (MathUtil.clip(g, 0, 255) - 128);
        data[off + 2] = (byte) (MathUtil.clip(b, 0, 255) - 128);
    }
}