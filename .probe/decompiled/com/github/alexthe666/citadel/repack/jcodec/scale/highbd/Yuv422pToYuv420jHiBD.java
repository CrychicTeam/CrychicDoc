package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv422pToYuv420jHiBD implements TransformHiBD {

    public static int COEFF = 9362;

    private int shift;

    private int halfSrc;

    private int halfDst;

    public Yuv422pToYuv420jHiBD(int upshift, int downshift) {
        this.shift = downshift + 13 - upshift;
        if (this.shift < 0) {
            throw new IllegalArgumentException("Maximum upshift allowed: " + (downshift + 13));
        } else {
            this.halfSrc = 128 << Math.max(downshift - upshift, 0);
            this.halfDst = 128 << Math.max(upshift - downshift, 0);
        }
    }

    @Override
    public void transform(PictureHiBD src, PictureHiBD dst) {
        int[] sy = src.getPlaneData(0);
        int[] dy = dst.getPlaneData(0);
        for (int i = 0; i < src.getPlaneWidth(0) * src.getPlaneHeight(0); i++) {
            dy[i] = (sy[i] - 16) * COEFF >> this.shift;
        }
        this.copyAvg(src.getPlaneData(1), dst.getPlaneData(1), src.getPlaneWidth(1), src.getPlaneHeight(1));
        this.copyAvg(src.getPlaneData(2), dst.getPlaneData(2), src.getPlaneWidth(2), src.getPlaneHeight(2));
    }

    private void copyAvg(int[] src, int[] dst, int width, int height) {
        int offSrc = 0;
        int offDst = 0;
        for (int y = 0; y < height / 2; y++) {
            for (int x = 0; x < width; offSrc++) {
                int a = ((src[offSrc] - this.halfSrc) * COEFF >> this.shift) + this.halfDst;
                int b = ((src[offSrc + width] - this.halfSrc) * COEFF >> this.shift) + this.halfDst;
                dst[offDst] = a + b + 1 >> 1;
                x++;
                offDst++;
            }
            offSrc += width;
        }
    }
}