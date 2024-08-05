package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class RgbToYuv420pHiBD implements TransformHiBD {

    private int upShift;

    private int downShift;

    private int downShiftChr;

    public RgbToYuv420pHiBD(int upShift, int downShift) {
        this.upShift = upShift;
        this.downShift = downShift;
        this.downShiftChr = downShift + 2;
    }

    @Override
    public void transform(PictureHiBD img, PictureHiBD dst) {
        int[] y = img.getData()[0];
        int[][] dstData = dst.getData();
        int offChr = 0;
        int offLuma = 0;
        int offSrc = 0;
        int strideSrc = img.getWidth() * 3;
        int strideDst = dst.getWidth();
        for (int i = 0; i < img.getHeight() >> 1; i++) {
            for (int j = 0; j < img.getWidth() >> 1; j++) {
                dstData[1][offChr] = 0;
                dstData[2][offChr] = 0;
                rgb2yuv(y[offSrc], y[offSrc + 1], y[offSrc + 2], dstData[0], offLuma, dstData[1], offChr, dstData[2], offChr);
                dstData[0][offLuma] = dstData[0][offLuma] << this.upShift >> this.downShift;
                rgb2yuv(y[offSrc + strideSrc], y[offSrc + strideSrc + 1], y[offSrc + strideSrc + 2], dstData[0], offLuma + strideDst, dstData[1], offChr, dstData[2], offChr);
                dstData[0][offLuma + strideDst] = dstData[0][offLuma + strideDst] << this.upShift >> this.downShift;
                rgb2yuv(y[offSrc + 3], y[offSrc + 4], y[offSrc + 5], dstData[0], ++offLuma, dstData[1], offChr, dstData[2], offChr);
                dstData[0][offLuma] = dstData[0][offLuma] << this.upShift >> this.downShift;
                rgb2yuv(y[offSrc + strideSrc + 3], y[offSrc + strideSrc + 4], y[offSrc + strideSrc + 5], dstData[0], offLuma + strideDst, dstData[1], offChr, dstData[2], offChr);
                dstData[0][offLuma + strideDst] = dstData[0][offLuma + strideDst] << this.upShift >> this.downShift;
                offLuma++;
                dstData[1][offChr] = dstData[1][offChr] << this.upShift >> this.downShiftChr;
                dstData[2][offChr] = dstData[2][offChr] << this.upShift >> this.downShiftChr;
                offChr++;
                offSrc += 6;
            }
            offLuma += strideDst;
            offSrc += strideSrc;
        }
    }

    public static final void rgb2yuv(int r, int g, int b, int[] Y, int offY, int[] U, int offU, int[] V, int offV) {
        int y = 66 * r + 129 * g + 25 * b;
        int u = -38 * r - 74 * g + 112 * b;
        int v = 112 * r - 94 * g - 18 * b;
        y = y + 128 >> 8;
        u = u + 128 >> 8;
        v = v + 128 >> 8;
        Y[offY] = clip(y + 16);
        U[offU] += clip(u + 128);
        V[offV] += clip(v + 128);
    }

    private static final int clip(int val) {
        return val < 0 ? 0 : (val > 255 ? 255 : val);
    }
}