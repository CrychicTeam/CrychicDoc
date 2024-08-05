package com.github.alexthe666.citadel.repack.jcodec.scale.highbd;

import com.github.alexthe666.citadel.repack.jcodec.common.model.PictureHiBD;

public class Yuv422pToYuv420pHiBD implements TransformHiBD {

    private int shiftUp;

    private int shiftDown;

    public Yuv422pToYuv420pHiBD(int shiftUp, int shiftDown) {
        this.shiftUp = shiftUp;
        this.shiftDown = shiftDown;
    }

    @Override
    public void transform(PictureHiBD src, PictureHiBD dst) {
        int lumaSize = src.getWidth() * src.getHeight();
        System.arraycopy(src.getPlaneData(0), 0, dst.getPlaneData(0), 0, lumaSize);
        this.copyAvg(src.getPlaneData(1), dst.getPlaneData(1), src.getPlaneWidth(1), src.getPlaneHeight(1));
        this.copyAvg(src.getPlaneData(2), dst.getPlaneData(2), src.getPlaneWidth(2), src.getPlaneHeight(2));
        if (this.shiftUp > this.shiftDown) {
            this.up(dst.getPlaneData(0), this.shiftUp - this.shiftDown);
            this.up(dst.getPlaneData(1), this.shiftUp - this.shiftDown);
            this.up(dst.getPlaneData(2), this.shiftUp - this.shiftDown);
        } else if (this.shiftDown > this.shiftUp) {
            this.down(dst.getPlaneData(0), this.shiftDown - this.shiftUp);
            this.down(dst.getPlaneData(1), this.shiftDown - this.shiftUp);
            this.down(dst.getPlaneData(2), this.shiftDown - this.shiftUp);
        }
    }

    private void down(int[] dst, int down) {
        for (int i = 0; i < dst.length; i++) {
            dst[i] >>= down;
        }
    }

    private void up(int[] dst, int up) {
        for (int i = 0; i < dst.length; i++) {
            dst[i] <<= up;
        }
    }

    private void copyAvg(int[] src, int[] dst, int width, int height) {
        int offSrc = 0;
        int offDst = 0;
        for (int y = 0; y < height / 2; y++) {
            for (int x = 0; x < width; offSrc++) {
                dst[offDst] = src[offSrc] + src[offSrc + width] + 1 >> 1;
                x++;
                offDst++;
            }
            offSrc += width;
        }
    }
}