package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv422pToRgbHiBD implements TransformHiBD {

    private int downShift;

    private int upShift;

    public Yuv422pToRgbHiBD(int downShift, int upShift) {
        this.downShift = downShift;
        this.upShift = upShift;
    }

    @Override
    public void transform(PictureHiBD src, PictureHiBD dst) {
        int[] y = src.getPlaneData(0);
        int[] u = src.getPlaneData(1);
        int[] v = src.getPlaneData(2);
        int[] data = dst.getPlaneData(0);
        int offLuma = 0;
        int offChroma = 0;
        for (int i = 0; i < dst.getHeight(); i++) {
            for (int j = 0; j < dst.getWidth(); j += 2) {
                YUV444toRGB888(y[offLuma] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, offLuma * 3);
                YUV444toRGB888(y[offLuma + 1] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + 1) * 3);
                offLuma += 2;
                offChroma++;
            }
        }
    }

    public static final void YUV444toRGB888(int y, int u, int v, int[] data, int off) {
        int c = y - 16;
        int d = u - 128;
        int e = v - 128;
        int r = 298 * c + 409 * e + 128 >> 8;
        int g = 298 * c - 100 * d - 208 * e + 128 >> 8;
        int b = 298 * c + 516 * d + 128 >> 8;
        data[off] = crop(r);
        data[off + 1] = crop(g);
        data[off + 2] = crop(b);
    }

    private static int crop(int val) {
        return val < 0 ? 0 : (val > 255 ? 255 : val);
    }
}