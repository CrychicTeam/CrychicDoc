package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class MBEncoderHelper {

    public static final void takeSubtract(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, byte[] pred, int blkW, int blkH) {
        if (x + blkW < planeWidth && y + blkH < planeHeight) {
            takeSubtractSafe(planeData, planeWidth, planeHeight, x, y, coeff, pred, blkW, blkH);
        } else {
            takeSubtractUnsafe(planeData, planeWidth, planeHeight, x, y, coeff, pred, blkW, blkH);
        }
    }

    public static final void takeSubtractSafe(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, byte[] pred, int blkW, int blkH) {
        int i = 0;
        int srcOff = y * planeWidth + x;
        for (int dstOff = 0; i < blkH; srcOff += planeWidth) {
            int j = 0;
            for (int srcOff1 = srcOff; j < blkW; srcOff1 += 4) {
                coeff[dstOff] = planeData[srcOff1] - pred[dstOff];
                coeff[dstOff + 1] = planeData[srcOff1 + 1] - pred[dstOff + 1];
                coeff[dstOff + 2] = planeData[srcOff1 + 2] - pred[dstOff + 2];
                coeff[dstOff + 3] = planeData[srcOff1 + 3] - pred[dstOff + 3];
                j += 4;
                dstOff += 4;
            }
            i++;
        }
    }

    public static final void take(byte[] planeData, int planeWidth, int planeHeight, int x, int y, byte[] patch, int blkW, int blkH) {
        if (x + blkW < planeWidth && y + blkH < planeHeight) {
            takeSafe(planeData, planeWidth, planeHeight, x, y, patch, blkW, blkH);
        } else {
            takeExtendBorder(planeData, planeWidth, planeHeight, x, y, patch, blkW, blkH);
        }
    }

    public static final void takeSafe(byte[] planeData, int planeWidth, int planeHeight, int x, int y, byte[] patch, int blkW, int blkH) {
        int i = 0;
        int srcOff = y * planeWidth + x;
        for (int dstOff = 0; i < blkH; srcOff += planeWidth) {
            int j = 0;
            for (int srcOff1 = srcOff; j < blkW; srcOff1++) {
                patch[dstOff] = planeData[srcOff1];
                j++;
                dstOff++;
            }
            i++;
        }
    }

    public static final void takeExtendBorder(byte[] planeData, int planeWidth, int planeHeight, int x, int y, byte[] patch, int blkW, int blkH) {
        int outOff = 0;
        int i;
        for (i = y; i < Math.min(y + blkH, planeHeight); i++) {
            int off = i * planeWidth + Math.min(x, planeWidth);
            int j;
            for (j = x; j < Math.min(x + blkW, planeWidth); off++) {
                patch[outOff] = planeData[off];
                j++;
                outOff++;
            }
            off--;
            while (j < x + blkW) {
                patch[outOff] = planeData[off];
                j++;
                outOff++;
            }
        }
        while (i < y + blkH) {
            int off = planeHeight * planeWidth - planeWidth + Math.min(x, planeWidth);
            int j;
            for (j = x; j < Math.min(x + blkW, planeWidth); off++) {
                patch[outOff] = planeData[off];
                j++;
                outOff++;
            }
            off--;
            while (j < x + blkW) {
                patch[outOff] = planeData[off];
                j++;
                outOff++;
            }
            i++;
        }
    }

    public static final void takeSafe2(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, int blkW, int blkH) {
        int i = 0;
        int srcOff = y * planeWidth + x;
        for (int dstOff = 0; i < blkH; srcOff += planeWidth) {
            int j = 0;
            for (int srcOff1 = srcOff; j < blkW; srcOff1++) {
                coeff[dstOff] = planeData[srcOff1];
                j++;
                dstOff++;
            }
            i++;
        }
    }

    public static final void takeSubtractUnsafe(byte[] planeData, int planeWidth, int planeHeight, int x, int y, int[] coeff, byte[] pred, int blkW, int blkH) {
        int outOff = 0;
        int i;
        for (i = y; i < Math.min(y + blkH, planeHeight); i++) {
            int off = i * planeWidth + Math.min(x, planeWidth);
            int j;
            for (j = x; j < Math.min(x + blkW, planeWidth); off++) {
                coeff[outOff] = planeData[off] - pred[outOff];
                j++;
                outOff++;
            }
            off--;
            while (j < x + blkW) {
                coeff[outOff] = planeData[off] - pred[outOff];
                j++;
                outOff++;
            }
        }
        while (i < y + blkH) {
            int off = planeHeight * planeWidth - planeWidth + Math.min(x, planeWidth);
            int j;
            for (j = x; j < Math.min(x + blkW, planeWidth); off++) {
                coeff[outOff] = planeData[off] - pred[outOff];
                j++;
                outOff++;
            }
            off--;
            while (j < x + blkW) {
                coeff[outOff] = planeData[off] - pred[outOff];
                j++;
                outOff++;
            }
            i++;
        }
    }

    public static final void putBlk(byte[] planeData, int[] block, byte[] pred, int log2stride, int blkX, int blkY, int blkW, int blkH) {
        int stride = 1 << log2stride;
        int line = 0;
        int srcOff = 0;
        for (int dstOff = (blkY << log2stride) + blkX; line < blkH; line++) {
            int dstOff1 = dstOff;
            for (int row = 0; row < blkW; row += 4) {
                planeData[dstOff1] = (byte) MathUtil.clip(block[srcOff] + pred[srcOff], -128, 127);
                planeData[dstOff1 + 1] = (byte) MathUtil.clip(block[srcOff + 1] + pred[srcOff + 1], -128, 127);
                planeData[dstOff1 + 2] = (byte) MathUtil.clip(block[srcOff + 2] + pred[srcOff + 2], -128, 127);
                planeData[dstOff1 + 3] = (byte) MathUtil.clip(block[srcOff + 3] + pred[srcOff + 3], -128, 127);
                srcOff += 4;
                dstOff1 += 4;
            }
            dstOff += stride;
        }
    }

    public static final void putBlkPic(Picture dest, Picture src, int x, int y) {
        if (dest.getColor() != src.getColor()) {
            throw new RuntimeException("Incompatible color");
        } else {
            for (int c = 0; c < dest.getColor().nComp; c++) {
                pubBlkOnePlane(dest.getPlaneData(c), dest.getPlaneWidth(c), src.getPlaneData(c), src.getPlaneWidth(c), src.getPlaneHeight(c), x >> dest.getColor().compWidth[c], y >> dest.getColor().compHeight[c]);
            }
        }
    }

    private static void pubBlkOnePlane(byte[] dest, int destWidth, byte[] src, int srcWidth, int srcHeight, int x, int y) {
        int destOff = y * destWidth + x;
        int srcOff = 0;
        for (int i = 0; i < srcHeight; i++) {
            for (int j = 0; j < srcWidth; srcOff++) {
                dest[destOff] = src[srcOff];
                j++;
                destOff++;
            }
            destOff += destWidth - srcWidth;
        }
    }
}