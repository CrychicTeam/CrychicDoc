package com.github.alexthe666.citadel.repack.jcodec.common.tools;

import com.github.alexthe666.citadel.repack.jcodec.common.model.ColorSpace;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Rect;

public class ImageOP {

    public static void subImageWithFillInt(int[] src, int width, int height, int[] dst, int dstW, int dstH, int offX, int offY) {
        int srcHeight = Math.min(height - offY, dstH);
        int srcWidth = Math.min(width - offX, dstW);
        int srcStride = width;
        int dstOff = 0;
        int srcOff = offY * width + offX;
        int i;
        for (i = 0; i < srcHeight; i++) {
            int j;
            for (j = 0; j < srcWidth; j++) {
                dst[dstOff + j] = src[srcOff + j];
            }
            for (int lastPix = dst[j - 1]; j < dstW; j++) {
                dst[dstOff + j] = lastPix;
            }
            srcOff += srcStride;
            dstOff += dstW;
        }
        for (int lastLine = dstOff - dstW; i < dstH; i++) {
            System.arraycopy(dst, lastLine, dst, dstOff, dstW);
            dstOff += dstW;
        }
    }

    public static void subImageWithFill(byte[] src, int width, int height, byte[] dst, int dstW, int dstH, int offX, int offY) {
        int srcHeight = Math.min(height - offY, dstH);
        int srcWidth = Math.min(width - offX, dstW);
        int srcStride = width;
        int dstOff = 0;
        int srcOff = offY * width + offX;
        int i;
        for (i = 0; i < srcHeight; i++) {
            int j;
            for (j = 0; j < srcWidth; j++) {
                dst[dstOff + j] = src[srcOff + j];
            }
            for (byte lastPix = dst[j - 1]; j < dstW; j++) {
                dst[dstOff + j] = lastPix;
            }
            srcOff += srcStride;
            dstOff += dstW;
        }
        for (int lastLine = dstOff - dstW; i < dstH; i++) {
            System.arraycopy(dst, lastLine, dst, dstOff, dstW);
            dstOff += dstW;
        }
    }

    public static void subImageWithFillPic8(Picture _in, Picture out, Rect rect) {
        int width = _in.getWidth();
        int height = _in.getHeight();
        ColorSpace color = _in.getColor();
        byte[][] data = _in.getData();
        for (int i = 0; i < data.length; i++) {
            subImageWithFill(data[i], width >> color.compWidth[i], height >> color.compHeight[i], out.getPlaneData(i), rect.getWidth() >> color.compWidth[i], rect.getHeight() >> color.compHeight[i], rect.getX() >> color.compWidth[i], rect.getY() >> color.compHeight[i]);
        }
    }
}