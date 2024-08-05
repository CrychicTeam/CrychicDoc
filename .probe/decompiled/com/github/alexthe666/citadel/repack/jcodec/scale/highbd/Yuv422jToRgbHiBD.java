package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv422jToRgbHiBD implements TransformHiBD {

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
                Yuv420jToRgbHiBD.YUVJtoRGB(y[offLuma], u[offChroma], v[offChroma], data, offLuma * 3);
                Yuv420jToRgbHiBD.YUVJtoRGB(y[offLuma + 1], u[offChroma], v[offChroma], data, (offLuma + 1) * 3);
                offLuma += 2;
                offChroma++;
            }
        }
    }
}