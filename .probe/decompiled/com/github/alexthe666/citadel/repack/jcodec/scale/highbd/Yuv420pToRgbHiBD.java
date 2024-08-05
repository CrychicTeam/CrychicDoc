package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv420pToRgbHiBD implements TransformHiBD {

    private final int downShift;

    private final int upShift;

    public Yuv420pToRgbHiBD(int upShift, int downShift) {
        this.upShift = upShift;
        this.downShift = downShift;
    }

    @Override
    public final void transform(PictureHiBD src, PictureHiBD dst) {
        int[] y = src.getPlaneData(0);
        int[] u = src.getPlaneData(1);
        int[] v = src.getPlaneData(2);
        int[] data = dst.getPlaneData(0);
        int offLuma = 0;
        int offChroma = 0;
        int stride = dst.getWidth();
        for (int i = 0; i < dst.getHeight() >> 1; i++) {
            for (int k = 0; k < dst.getWidth() >> 1; k++) {
                int j = k << 1;
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j) * 3);
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j + 1] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j + 1) * 3);
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j + stride] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j + stride) * 3);
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j + stride + 1] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j + stride + 1) * 3);
                offChroma++;
            }
            if ((dst.getWidth() & 1) != 0) {
                int j = dst.getWidth() - 1;
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j) * 3);
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j + stride] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j + stride) * 3);
                offChroma++;
            }
            offLuma += 2 * stride;
        }
        if ((dst.getHeight() & 1) != 0) {
            for (int k = 0; k < dst.getWidth() >> 1; k++) {
                int j = k << 1;
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j) * 3);
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j + 1] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j + 1) * 3);
                offChroma++;
            }
            if ((dst.getWidth() & 1) != 0) {
                int j = dst.getWidth() - 1;
                Yuv422pToRgbHiBD.YUV444toRGB888(y[offLuma + j] << this.upShift >> this.downShift, u[offChroma] << this.upShift >> this.downShift, v[offChroma] << this.upShift >> this.downShift, data, (offLuma + j) * 3);
                offChroma++;
            }
        }
    }
}