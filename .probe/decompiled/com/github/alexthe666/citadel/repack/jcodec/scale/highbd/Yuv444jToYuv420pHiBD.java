package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv444jToYuv420pHiBD implements TransformHiBD {

    public static int Y_COEFF = 7168;

    @Override
    public void transform(PictureHiBD src, PictureHiBD dst) {
        int[] sy = src.getPlaneData(0);
        int[] dy = dst.getPlaneData(0);
        for (int i = 0; i < src.getPlaneWidth(0) * src.getPlaneHeight(0); i++) {
            dy[i] = (sy[i] * Y_COEFF >> 13) + 16;
        }
        this.copyAvg(src.getPlaneData(1), dst.getPlaneData(1), src.getPlaneWidth(1), src.getPlaneHeight(1));
        this.copyAvg(src.getPlaneData(2), dst.getPlaneData(2), src.getPlaneWidth(2), src.getPlaneHeight(2));
    }

    private void copyAvg(int[] src, int[] dst, int width, int height) {
        int offSrc = 0;
        int offDst = 0;
        for (int y = 0; y < height >> 1; y++) {
            for (int x = 0; x < width; offSrc += 2) {
                int a = ((src[offSrc] - 128) * Y_COEFF >> 13) + 128;
                int b = ((src[offSrc + 1] - 128) * Y_COEFF >> 13) + 128;
                int c = ((src[offSrc + width] - 128) * Y_COEFF >> 13) + 128;
                int d = ((src[offSrc + width + 1] - 128) * Y_COEFF >> 13) + 128;
                dst[offDst] = a + b + c + d + 2 >> 2;
                x += 2;
                offDst++;
            }
            offSrc += width;
        }
    }
}