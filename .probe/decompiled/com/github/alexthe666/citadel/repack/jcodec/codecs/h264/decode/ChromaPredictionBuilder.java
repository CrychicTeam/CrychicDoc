package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.codecs.h264.H264Const;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class ChromaPredictionBuilder {

    public static void predictWithMode(int[][] residual, int chromaMode, int mbX, boolean leftAvailable, boolean topAvailable, byte[] leftRow, byte[] topLine, byte[] topLeft, byte[] pixOut) {
        switch(chromaMode) {
            case 0:
                predictDC(residual, mbX, leftAvailable, topAvailable, leftRow, topLine, pixOut);
                break;
            case 1:
                predictHorizontal(residual, mbX, leftAvailable, leftRow, pixOut);
                break;
            case 2:
                predictVertical(residual, mbX, topAvailable, topLine, pixOut);
                break;
            case 3:
                predictPlane(residual, mbX, leftAvailable, topAvailable, leftRow, topLine, topLeft, pixOut);
        }
    }

    public static void predictDC(int[][] planeData, int mbX, boolean leftAvailable, boolean topAvailable, byte[] leftRow, byte[] topLine, byte[] pixOut) {
        predictDCInside(planeData, 0, 0, mbX, leftAvailable, topAvailable, leftRow, topLine, pixOut);
        predictDCTopBorder(planeData, 1, 0, mbX, leftAvailable, topAvailable, leftRow, topLine, pixOut);
        predictDCLeftBorder(planeData, 0, 1, mbX, leftAvailable, topAvailable, leftRow, topLine, pixOut);
        predictDCInside(planeData, 1, 1, mbX, leftAvailable, topAvailable, leftRow, topLine, pixOut);
    }

    public static void predictVertical(int[][] residual, int mbX, boolean topAvailable, byte[] topLine, byte[] pixOut) {
        int off = 0;
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; off++) {
                pixOut[off] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off]][H264Const.CHROMA_POS_LUT[off]] + topLine[(mbX << 3) + i], -128, 127);
                i++;
            }
        }
    }

    public static void predictHorizontal(int[][] residual, int mbX, boolean leftAvailable, byte[] leftRow, byte[] pixOut) {
        int off = 0;
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; off++) {
                pixOut[off] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off]][H264Const.CHROMA_POS_LUT[off]] + leftRow[j], -128, 127);
                i++;
            }
        }
    }

    public static void predictDCInside(int[][] residual, int blkX, int blkY, int mbX, boolean leftAvailable, boolean topAvailable, byte[] leftRow, byte[] topLine, byte[] pixOut) {
        int blkOffX = (blkX << 2) + (mbX << 3);
        int blkOffY = blkY << 2;
        int s0;
        if (leftAvailable && topAvailable) {
            s0 = 0;
            for (int i = 0; i < 4; i++) {
                s0 += leftRow[i + blkOffY];
            }
            for (int i = 0; i < 4; i++) {
                s0 += topLine[blkOffX + i];
            }
            s0 = s0 + 4 >> 3;
        } else if (leftAvailable) {
            s0 = 0;
            for (int i = 0; i < 4; i++) {
                s0 += leftRow[blkOffY + i];
            }
            s0 = s0 + 2 >> 2;
        } else if (topAvailable) {
            s0 = 0;
            for (int i = 0; i < 4; i++) {
                s0 += topLine[blkOffX + i];
            }
            s0 = s0 + 2 >> 2;
        } else {
            s0 = 0;
        }
        int off = (blkY << 5) + (blkX << 2);
        for (int j = 0; j < 4; off += 8) {
            pixOut[off] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off]][H264Const.CHROMA_POS_LUT[off]] + s0, -128, 127);
            pixOut[off + 1] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 1]][H264Const.CHROMA_POS_LUT[off + 1]] + s0, -128, 127);
            pixOut[off + 2] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 2]][H264Const.CHROMA_POS_LUT[off + 2]] + s0, -128, 127);
            pixOut[off + 3] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 3]][H264Const.CHROMA_POS_LUT[off + 3]] + s0, -128, 127);
            j++;
        }
    }

    public static void predictDCTopBorder(int[][] residual, int blkX, int blkY, int mbX, boolean leftAvailable, boolean topAvailable, byte[] leftRow, byte[] topLine, byte[] pixOut) {
        int blkOffX = (blkX << 2) + (mbX << 3);
        int blkOffY = blkY << 2;
        int s1;
        if (topAvailable) {
            s1 = 0;
            for (int i = 0; i < 4; i++) {
                s1 += topLine[blkOffX + i];
            }
            s1 = s1 + 2 >> 2;
        } else if (leftAvailable) {
            s1 = 0;
            for (int i = 0; i < 4; i++) {
                s1 += leftRow[blkOffY + i];
            }
            s1 = s1 + 2 >> 2;
        } else {
            s1 = 0;
        }
        int off = (blkY << 5) + (blkX << 2);
        for (int j = 0; j < 4; off += 8) {
            pixOut[off] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off]][H264Const.CHROMA_POS_LUT[off]] + s1, -128, 127);
            pixOut[off + 1] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 1]][H264Const.CHROMA_POS_LUT[off + 1]] + s1, -128, 127);
            pixOut[off + 2] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 2]][H264Const.CHROMA_POS_LUT[off + 2]] + s1, -128, 127);
            pixOut[off + 3] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 3]][H264Const.CHROMA_POS_LUT[off + 3]] + s1, -128, 127);
            j++;
        }
    }

    public static void predictDCLeftBorder(int[][] residual, int blkX, int blkY, int mbX, boolean leftAvailable, boolean topAvailable, byte[] leftRow, byte[] topLine, byte[] pixOut) {
        int blkOffX = (blkX << 2) + (mbX << 3);
        int blkOffY = blkY << 2;
        int s2;
        if (leftAvailable) {
            s2 = 0;
            for (int i = 0; i < 4; i++) {
                s2 += leftRow[blkOffY + i];
            }
            s2 = s2 + 2 >> 2;
        } else if (topAvailable) {
            s2 = 0;
            for (int i = 0; i < 4; i++) {
                s2 += topLine[blkOffX + i];
            }
            s2 = s2 + 2 >> 2;
        } else {
            s2 = 0;
        }
        int off = (blkY << 5) + (blkX << 2);
        for (int j = 0; j < 4; off += 8) {
            pixOut[off] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off]][H264Const.CHROMA_POS_LUT[off]] + s2, -128, 127);
            pixOut[off + 1] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 1]][H264Const.CHROMA_POS_LUT[off + 1]] + s2, -128, 127);
            pixOut[off + 2] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 2]][H264Const.CHROMA_POS_LUT[off + 2]] + s2, -128, 127);
            pixOut[off + 3] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off + 3]][H264Const.CHROMA_POS_LUT[off + 3]] + s2, -128, 127);
            j++;
        }
    }

    public static void predictPlane(int[][] residual, int mbX, boolean leftAvailable, boolean topAvailable, byte[] leftRow, byte[] topLine, byte[] topLeft, byte[] pixOut) {
        int H = 0;
        int blkOffX = mbX << 3;
        for (int i = 0; i < 3; i++) {
            H += (i + 1) * (topLine[blkOffX + 4 + i] - topLine[blkOffX + 2 - i]);
        }
        H += 4 * (topLine[blkOffX + 7] - topLeft[0]);
        int V = 0;
        for (int j = 0; j < 3; j++) {
            V += (j + 1) * (leftRow[4 + j] - leftRow[2 - j]);
        }
        V += 4 * (leftRow[7] - topLeft[0]);
        int c = 34 * V + 32 >> 6;
        int b = 34 * H + 32 >> 6;
        int a = 16 * (leftRow[7] + topLine[blkOffX + 7]);
        int off = 0;
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; off++) {
                int val = a + b * (i - 3) + c * (j - 3) + 16 >> 5;
                pixOut[off] = (byte) MathUtil.clip(residual[H264Const.CHROMA_BLOCK_LUT[off]][H264Const.CHROMA_POS_LUT[off]] + MathUtil.clip(val, -128, 127), -128, 127);
                i++;
            }
        }
    }
}