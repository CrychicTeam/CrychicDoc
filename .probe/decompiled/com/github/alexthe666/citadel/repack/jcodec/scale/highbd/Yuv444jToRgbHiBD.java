package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv444jToRgbHiBD implements TransformHiBD {

    @Override
    public void transform(PictureHiBD src, PictureHiBD dst) {
        int[] y = src.getPlaneData(0);
        int[] u = src.getPlaneData(1);
        int[] v = src.getPlaneData(2);
        int[] data = dst.getPlaneData(0);
        int i = 0;
        int srcOff = 0;
        for (int dstOff = 0; i < dst.getHeight(); i++) {
            for (int j = 0; j < dst.getWidth(); dstOff += 3) {
                Yuv420jToRgbHiBD.YUVJtoRGB(y[srcOff], u[srcOff], v[srcOff], data, dstOff);
                j++;
                srcOff++;
            }
        }
    }
}