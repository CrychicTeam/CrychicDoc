package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class RgbToYuv422pHiBD implements TransformHiBD {

    private int upShift;

    private int downShift;

    private int downShiftChr;

    public RgbToYuv422pHiBD(int upShift, int downShift) {
        this.upShift = upShift;
        this.downShift = downShift;
        this.downShiftChr = downShift + 1;
    }

    @Override
    public void transform(PictureHiBD img, PictureHiBD dst) {
        int[] y = img.getData()[0];
        int[][] dstData = dst.getData();
        int off = 0;
        int offSrc = 0;
        for (int i = 0; i < img.getHeight(); i++) {
            for (int j = 0; j < img.getWidth() >> 1; j++) {
                dstData[1][off] = 0;
                dstData[2][off] = 0;
                int offY = off << 1;
                RgbToYuv420pHiBD.rgb2yuv(y[offSrc++], y[offSrc++], y[offSrc++], dstData[0], offY, dstData[1], off, dstData[2], off);
                dstData[0][offY] = dstData[0][offY] << this.upShift >> this.downShift;
                RgbToYuv420pHiBD.rgb2yuv(y[offSrc++], y[offSrc++], y[offSrc++], dstData[0], offY + 1, dstData[1], off, dstData[2], off);
                dstData[0][offY + 1] = dstData[0][offY + 1] << this.upShift >> this.downShift;
                dstData[1][off] = dstData[1][off] << this.upShift >> this.downShiftChr;
                dstData[2][off] = dstData[2][off] << this.upShift >> this.downShiftChr;
                off++;
            }
        }
    }
}