package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.decode;

import com.github.alexthe666.citadel.repack.jcodec.common.Preconditions;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class Intra8x8PredictionBuilder {

    byte[] topBuf = new byte[16];

    byte[] leftBuf = new byte[8];

    byte[] genBuf = new byte[24];

    public void predictWithMode(int mode, int[] residual, boolean leftAvailable, boolean topAvailable, boolean topLeftAvailable, boolean topRightAvailable, byte[] leftRow, byte[] topLine, byte[] topLeft, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        switch(mode) {
            case 0:
                Preconditions.checkState(topAvailable, "");
                this.predictVertical(residual, topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 1:
                Preconditions.checkState(leftAvailable, "");
                this.predictHorizontal(residual, topLeftAvailable, topLeft, leftRow, mbOffX, blkX, blkY, pixOut);
                break;
            case 2:
                this.predictDC(residual, topLeftAvailable, topRightAvailable, leftAvailable, topAvailable, topLeft, leftRow, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 3:
                Preconditions.checkState(topAvailable, "");
                this.predictDiagonalDownLeft(residual, topLeftAvailable, topAvailable, topRightAvailable, topLeft, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 4:
                Preconditions.checkState(topAvailable && leftAvailable && topLeftAvailable, "");
                this.predictDiagonalDownRight(residual, topRightAvailable, topLeft, leftRow, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 5:
                Preconditions.checkState(topAvailable && leftAvailable && topLeftAvailable, "");
                this.predictVerticalRight(residual, topRightAvailable, topLeft, leftRow, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 6:
                Preconditions.checkState(topAvailable && leftAvailable && topLeftAvailable, "");
                this.predictHorizontalDown(residual, topRightAvailable, topLeft, leftRow, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 7:
                Preconditions.checkState(topAvailable, "");
                this.predictVerticalLeft(residual, topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX, blkX, blkY, pixOut);
                break;
            case 8:
                Preconditions.checkState(leftAvailable, "");
                this.predictHorizontalUp(residual, topLeftAvailable, topLeft, leftRow, mbOffX, blkX, blkY, pixOut);
        }
        int oo1 = mbOffX + blkX;
        int off1 = (blkY << 4) + blkX + 7;
        topLeft[blkY >> 2] = topLine[oo1 + 7];
        for (int i = 0; i < 8; i++) {
            leftRow[blkY + i] = pixOut[off1 + (i << 4)];
        }
        int off2 = (blkY << 4) + blkX + 112;
        for (int i = 0; i < 8; i++) {
            topLine[oo1 + i] = pixOut[off2 + i];
        }
        topLeft[(blkY >> 2) + 1] = leftRow[blkY + 3];
    }

    private void interpolateTop(boolean topLeftAvailable, boolean topRightAvailable, byte[] topLeft, byte[] topLine, int blkX, int blkY, byte[] out) {
        int a = topLeftAvailable ? topLeft[blkY >> 2] : topLine[blkX];
        out[0] = (byte) (a + (topLine[blkX] << 1) + topLine[blkX + 1] + 2 >> 2);
        int i;
        for (i = 1; i < 7; i++) {
            out[i] = (byte) (topLine[blkX + i - 1] + (topLine[blkX + i] << 1) + topLine[blkX + i + 1] + 2 >> 2);
        }
        if (!topRightAvailable) {
            out[7] = (byte) (topLine[blkX + 6] + (topLine[blkX + 7] << 1) + topLine[blkX + 7] + 2 >> 2);
            for (int var10 = 8; var10 < 16; var10++) {
                out[var10] = topLine[blkX + 7];
            }
        } else {
            while (i < 15) {
                out[i] = (byte) (topLine[blkX + i - 1] + (topLine[blkX + i] << 1) + topLine[blkX + i + 1] + 2 >> 2);
                i++;
            }
            out[15] = (byte) (topLine[blkX + 14] + (topLine[blkX + 15] << 1) + topLine[blkX + 15] + 2 >> 2);
        }
    }

    private void interpolateLeft(boolean topLeftAvailable, byte[] topLeft, byte[] leftRow, int blkY, byte[] out) {
        int a = topLeftAvailable ? topLeft[blkY >> 2] : leftRow[0];
        out[0] = (byte) (a + (leftRow[blkY] << 1) + leftRow[blkY + 1] + 2 >> 2);
        for (int i = 1; i < 7; i++) {
            out[i] = (byte) (leftRow[blkY + i - 1] + (leftRow[blkY + i] << 1) + leftRow[blkY + i + 1] + 2 >> 2);
        }
        out[7] = (byte) (leftRow[blkY + 6] + (leftRow[blkY + 7] << 1) + leftRow[blkY + 7] + 2 >> 2);
    }

    private int interpolateTopLeft(boolean topAvailable, boolean leftAvailable, byte[] topLeft, byte[] topLine, byte[] leftRow, int mbOffX, int blkX, int blkY) {
        int a = topLeft[blkY >> 2];
        int b = topAvailable ? topLine[mbOffX + blkX] : a;
        int c = leftAvailable ? leftRow[blkY] : a;
        int aa = a << 1;
        return aa + b + c + 2 >> 2;
    }

    public void copyAdd(byte[] pred, int srcOff, int[] residual, int pixOff, int rOff, byte[] out) {
        out[pixOff] = (byte) MathUtil.clip(residual[rOff] + pred[srcOff], -128, 127);
        out[pixOff + 1] = (byte) MathUtil.clip(residual[rOff + 1] + pred[srcOff + 1], -128, 127);
        out[pixOff + 2] = (byte) MathUtil.clip(residual[rOff + 2] + pred[srcOff + 2], -128, 127);
        out[pixOff + 3] = (byte) MathUtil.clip(residual[rOff + 3] + pred[srcOff + 3], -128, 127);
        out[pixOff + 4] = (byte) MathUtil.clip(residual[rOff + 4] + pred[srcOff + 4], -128, 127);
        out[pixOff + 5] = (byte) MathUtil.clip(residual[rOff + 5] + pred[srcOff + 5], -128, 127);
        out[pixOff + 6] = (byte) MathUtil.clip(residual[rOff + 6] + pred[srcOff + 6], -128, 127);
        out[pixOff + 7] = (byte) MathUtil.clip(residual[rOff + 7] + pred[srcOff + 7], -128, 127);
    }

    public void fillAdd(int[] residual, int pixOff, int val, byte[] pixOut) {
        int rOff = 0;
        for (int i = 0; i < 8; i++) {
            pixOut[pixOff] = (byte) MathUtil.clip(residual[rOff] + val, -128, 127);
            pixOut[pixOff + 1] = (byte) MathUtil.clip(residual[rOff + 1] + val, -128, 127);
            pixOut[pixOff + 2] = (byte) MathUtil.clip(residual[rOff + 2] + val, -128, 127);
            pixOut[pixOff + 3] = (byte) MathUtil.clip(residual[rOff + 3] + val, -128, 127);
            pixOut[pixOff + 4] = (byte) MathUtil.clip(residual[rOff + 4] + val, -128, 127);
            pixOut[pixOff + 5] = (byte) MathUtil.clip(residual[rOff + 5] + val, -128, 127);
            pixOut[pixOff + 6] = (byte) MathUtil.clip(residual[rOff + 6] + val, -128, 127);
            pixOut[pixOff + 7] = (byte) MathUtil.clip(residual[rOff + 7] + val, -128, 127);
            pixOff += 16;
            rOff += 8;
        }
    }

    public void predictVertical(int[] residual, boolean topLeftAvailable, boolean topRightAvailable, byte[] topLeft, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateTop(topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
        int pixOff = (blkY << 4) + blkX;
        int rOff = 0;
        for (int i = 0; i < 8; i++) {
            pixOut[pixOff] = (byte) MathUtil.clip(residual[rOff] + this.topBuf[0], -128, 127);
            pixOut[pixOff + 1] = (byte) MathUtil.clip(residual[rOff + 1] + this.topBuf[1], -128, 127);
            pixOut[pixOff + 2] = (byte) MathUtil.clip(residual[rOff + 2] + this.topBuf[2], -128, 127);
            pixOut[pixOff + 3] = (byte) MathUtil.clip(residual[rOff + 3] + this.topBuf[3], -128, 127);
            pixOut[pixOff + 4] = (byte) MathUtil.clip(residual[rOff + 4] + this.topBuf[4], -128, 127);
            pixOut[pixOff + 5] = (byte) MathUtil.clip(residual[rOff + 5] + this.topBuf[5], -128, 127);
            pixOut[pixOff + 6] = (byte) MathUtil.clip(residual[rOff + 6] + this.topBuf[6], -128, 127);
            pixOut[pixOff + 7] = (byte) MathUtil.clip(residual[rOff + 7] + this.topBuf[7], -128, 127);
            pixOff += 16;
            rOff += 8;
        }
    }

    public void predictHorizontal(int[] residual, boolean topLeftAvailable, byte[] topLeft, byte[] leftRow, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateLeft(topLeftAvailable, topLeft, leftRow, blkY, this.leftBuf);
        int pixOff = (blkY << 4) + blkX;
        int rOff = 0;
        for (int i = 0; i < 8; i++) {
            pixOut[pixOff] = (byte) MathUtil.clip(residual[rOff] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 1] = (byte) MathUtil.clip(residual[rOff + 1] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 2] = (byte) MathUtil.clip(residual[rOff + 2] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 3] = (byte) MathUtil.clip(residual[rOff + 3] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 4] = (byte) MathUtil.clip(residual[rOff + 4] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 5] = (byte) MathUtil.clip(residual[rOff + 5] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 6] = (byte) MathUtil.clip(residual[rOff + 6] + this.leftBuf[i], -128, 127);
            pixOut[pixOff + 7] = (byte) MathUtil.clip(residual[rOff + 7] + this.leftBuf[i], -128, 127);
            pixOff += 16;
            rOff += 8;
        }
    }

    public void predictDC(int[] residual, boolean topLeftAvailable, boolean topRightAvailable, boolean leftAvailable, boolean topAvailable, byte[] topLeft, byte[] leftRow, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        if (topAvailable && leftAvailable) {
            this.interpolateTop(topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
            this.interpolateLeft(topLeftAvailable, topLeft, leftRow, blkY, this.leftBuf);
            int sum1 = this.topBuf[0] + this.topBuf[1] + this.topBuf[2] + this.topBuf[3];
            int sum2 = this.topBuf[4] + this.topBuf[5] + this.topBuf[6] + this.topBuf[7];
            int sum3 = this.leftBuf[0] + this.leftBuf[1] + this.leftBuf[2] + this.leftBuf[3];
            int sum4 = this.leftBuf[4] + this.leftBuf[5] + this.leftBuf[6] + this.leftBuf[7];
            this.fillAdd(residual, (blkY << 4) + blkX, sum1 + sum2 + sum3 + sum4 + 8 >> 4, pixOut);
        } else if (leftAvailable) {
            this.interpolateLeft(topLeftAvailable, topLeft, leftRow, blkY, this.leftBuf);
            int sum3 = this.leftBuf[0] + this.leftBuf[1] + this.leftBuf[2] + this.leftBuf[3];
            int sum4 = this.leftBuf[4] + this.leftBuf[5] + this.leftBuf[6] + this.leftBuf[7];
            this.fillAdd(residual, (blkY << 4) + blkX, sum3 + sum4 + 4 >> 3, pixOut);
        } else if (topAvailable) {
            this.interpolateTop(topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
            int sum1 = this.topBuf[0] + this.topBuf[1] + this.topBuf[2] + this.topBuf[3];
            int sum2 = this.topBuf[4] + this.topBuf[5] + this.topBuf[6] + this.topBuf[7];
            this.fillAdd(residual, (blkY << 4) + blkX, sum1 + sum2 + 4 >> 3, pixOut);
        } else {
            this.fillAdd(residual, (blkY << 4) + blkX, 0, pixOut);
        }
    }

    public void predictDiagonalDownLeft(int[] residual, boolean topLeftAvailable, boolean topAvailable, boolean topRightAvailable, byte[] topLeft, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateTop(topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
        this.genBuf[0] = (byte) (this.topBuf[0] + this.topBuf[2] + (this.topBuf[1] << 1) + 2 >> 2);
        this.genBuf[1] = (byte) (this.topBuf[1] + this.topBuf[3] + (this.topBuf[2] << 1) + 2 >> 2);
        this.genBuf[2] = (byte) (this.topBuf[2] + this.topBuf[4] + (this.topBuf[3] << 1) + 2 >> 2);
        this.genBuf[3] = (byte) (this.topBuf[3] + this.topBuf[5] + (this.topBuf[4] << 1) + 2 >> 2);
        this.genBuf[4] = (byte) (this.topBuf[4] + this.topBuf[6] + (this.topBuf[5] << 1) + 2 >> 2);
        this.genBuf[5] = (byte) (this.topBuf[5] + this.topBuf[7] + (this.topBuf[6] << 1) + 2 >> 2);
        this.genBuf[6] = (byte) (this.topBuf[6] + this.topBuf[8] + (this.topBuf[7] << 1) + 2 >> 2);
        this.genBuf[7] = (byte) (this.topBuf[7] + this.topBuf[9] + (this.topBuf[8] << 1) + 2 >> 2);
        this.genBuf[8] = (byte) (this.topBuf[8] + this.topBuf[10] + (this.topBuf[9] << 1) + 2 >> 2);
        this.genBuf[9] = (byte) (this.topBuf[9] + this.topBuf[11] + (this.topBuf[10] << 1) + 2 >> 2);
        this.genBuf[10] = (byte) (this.topBuf[10] + this.topBuf[12] + (this.topBuf[11] << 1) + 2 >> 2);
        this.genBuf[11] = (byte) (this.topBuf[11] + this.topBuf[13] + (this.topBuf[12] << 1) + 2 >> 2);
        this.genBuf[12] = (byte) (this.topBuf[12] + this.topBuf[14] + (this.topBuf[13] << 1) + 2 >> 2);
        this.genBuf[13] = (byte) (this.topBuf[13] + this.topBuf[15] + (this.topBuf[14] << 1) + 2 >> 2);
        this.genBuf[14] = (byte) (this.topBuf[14] + this.topBuf[15] + (this.topBuf[15] << 1) + 2 >> 2);
        int off = (blkY << 4) + blkX;
        this.copyAdd(this.genBuf, 0, residual, off, 0, pixOut);
        this.copyAdd(this.genBuf, 1, residual, off + 16, 8, pixOut);
        this.copyAdd(this.genBuf, 2, residual, off + 32, 16, pixOut);
        this.copyAdd(this.genBuf, 3, residual, off + 48, 24, pixOut);
        this.copyAdd(this.genBuf, 4, residual, off + 64, 32, pixOut);
        this.copyAdd(this.genBuf, 5, residual, off + 80, 40, pixOut);
        this.copyAdd(this.genBuf, 6, residual, off + 96, 48, pixOut);
        this.copyAdd(this.genBuf, 7, residual, off + 112, 56, pixOut);
    }

    public void predictDiagonalDownRight(int[] residual, boolean topRightAvailable, byte[] topLeft, byte[] leftRow, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateTop(true, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
        this.interpolateLeft(true, topLeft, leftRow, blkY, this.leftBuf);
        int tl = this.interpolateTopLeft(true, true, topLeft, topLine, leftRow, mbOffX, blkX, blkY);
        this.genBuf[0] = (byte) (this.leftBuf[7] + this.leftBuf[5] + (this.leftBuf[6] << 1) + 2 >> 2);
        this.genBuf[1] = (byte) (this.leftBuf[6] + this.leftBuf[4] + (this.leftBuf[5] << 1) + 2 >> 2);
        this.genBuf[2] = (byte) (this.leftBuf[5] + this.leftBuf[3] + (this.leftBuf[4] << 1) + 2 >> 2);
        this.genBuf[3] = (byte) (this.leftBuf[4] + this.leftBuf[2] + (this.leftBuf[3] << 1) + 2 >> 2);
        this.genBuf[4] = (byte) (this.leftBuf[3] + this.leftBuf[1] + (this.leftBuf[2] << 1) + 2 >> 2);
        this.genBuf[5] = (byte) (this.leftBuf[2] + this.leftBuf[0] + (this.leftBuf[1] << 1) + 2 >> 2);
        this.genBuf[6] = (byte) (this.leftBuf[1] + tl + (this.leftBuf[0] << 1) + 2 >> 2);
        this.genBuf[7] = (byte) (this.leftBuf[0] + this.topBuf[0] + (tl << 1) + 2 >> 2);
        this.genBuf[8] = (byte) (tl + this.topBuf[1] + (this.topBuf[0] << 1) + 2 >> 2);
        this.genBuf[9] = (byte) (this.topBuf[0] + this.topBuf[2] + (this.topBuf[1] << 1) + 2 >> 2);
        this.genBuf[10] = (byte) (this.topBuf[1] + this.topBuf[3] + (this.topBuf[2] << 1) + 2 >> 2);
        this.genBuf[11] = (byte) (this.topBuf[2] + this.topBuf[4] + (this.topBuf[3] << 1) + 2 >> 2);
        this.genBuf[12] = (byte) (this.topBuf[3] + this.topBuf[5] + (this.topBuf[4] << 1) + 2 >> 2);
        this.genBuf[13] = (byte) (this.topBuf[4] + this.topBuf[6] + (this.topBuf[5] << 1) + 2 >> 2);
        this.genBuf[14] = (byte) (this.topBuf[5] + this.topBuf[7] + (this.topBuf[6] << 1) + 2 >> 2);
        int off = (blkY << 4) + blkX;
        this.copyAdd(this.genBuf, 7, residual, off, 0, pixOut);
        this.copyAdd(this.genBuf, 6, residual, off + 16, 8, pixOut);
        this.copyAdd(this.genBuf, 5, residual, off + 32, 16, pixOut);
        this.copyAdd(this.genBuf, 4, residual, off + 48, 24, pixOut);
        this.copyAdd(this.genBuf, 3, residual, off + 64, 32, pixOut);
        this.copyAdd(this.genBuf, 2, residual, off + 80, 40, pixOut);
        this.copyAdd(this.genBuf, 1, residual, off + 96, 48, pixOut);
        this.copyAdd(this.genBuf, 0, residual, off + 112, 56, pixOut);
    }

    public void predictVerticalRight(int[] residual, boolean topRightAvailable, byte[] topLeft, byte[] leftRow, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateTop(true, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
        this.interpolateLeft(true, topLeft, leftRow, blkY, this.leftBuf);
        int tl = this.interpolateTopLeft(true, true, topLeft, topLine, leftRow, mbOffX, blkX, blkY);
        this.genBuf[0] = (byte) (this.leftBuf[5] + this.leftBuf[3] + (this.leftBuf[4] << 1) + 2 >> 2);
        this.genBuf[1] = (byte) (this.leftBuf[3] + this.leftBuf[1] + (this.leftBuf[2] << 1) + 2 >> 2);
        this.genBuf[2] = (byte) (this.leftBuf[1] + tl + (this.leftBuf[0] << 1) + 2 >> 2);
        this.genBuf[3] = (byte) (tl + this.topBuf[0] + 1 >> 1);
        this.genBuf[4] = (byte) (this.topBuf[0] + this.topBuf[1] + 1 >> 1);
        this.genBuf[5] = (byte) (this.topBuf[1] + this.topBuf[2] + 1 >> 1);
        this.genBuf[6] = (byte) (this.topBuf[2] + this.topBuf[3] + 1 >> 1);
        this.genBuf[7] = (byte) (this.topBuf[3] + this.topBuf[4] + 1 >> 1);
        this.genBuf[8] = (byte) (this.topBuf[4] + this.topBuf[5] + 1 >> 1);
        this.genBuf[9] = (byte) (this.topBuf[5] + this.topBuf[6] + 1 >> 1);
        this.genBuf[10] = (byte) (this.topBuf[6] + this.topBuf[7] + 1 >> 1);
        this.genBuf[11] = (byte) (this.leftBuf[6] + this.leftBuf[4] + (this.leftBuf[5] << 1) + 2 >> 2);
        this.genBuf[12] = (byte) (this.leftBuf[4] + this.leftBuf[2] + (this.leftBuf[3] << 1) + 2 >> 2);
        this.genBuf[13] = (byte) (this.leftBuf[2] + this.leftBuf[0] + (this.leftBuf[1] << 1) + 2 >> 2);
        this.genBuf[14] = (byte) (this.leftBuf[0] + this.topBuf[0] + (tl << 1) + 2 >> 2);
        this.genBuf[15] = (byte) (tl + this.topBuf[1] + (this.topBuf[0] << 1) + 2 >> 2);
        this.genBuf[16] = (byte) (this.topBuf[0] + this.topBuf[2] + (this.topBuf[1] << 1) + 2 >> 2);
        this.genBuf[17] = (byte) (this.topBuf[1] + this.topBuf[3] + (this.topBuf[2] << 1) + 2 >> 2);
        this.genBuf[18] = (byte) (this.topBuf[2] + this.topBuf[4] + (this.topBuf[3] << 1) + 2 >> 2);
        this.genBuf[19] = (byte) (this.topBuf[3] + this.topBuf[5] + (this.topBuf[4] << 1) + 2 >> 2);
        this.genBuf[20] = (byte) (this.topBuf[4] + this.topBuf[6] + (this.topBuf[5] << 1) + 2 >> 2);
        this.genBuf[21] = (byte) (this.topBuf[5] + this.topBuf[7] + (this.topBuf[6] << 1) + 2 >> 2);
        int off = (blkY << 4) + blkX;
        this.copyAdd(this.genBuf, 3, residual, off, 0, pixOut);
        this.copyAdd(this.genBuf, 14, residual, off + 16, 8, pixOut);
        this.copyAdd(this.genBuf, 2, residual, off + 32, 16, pixOut);
        this.copyAdd(this.genBuf, 13, residual, off + 48, 24, pixOut);
        this.copyAdd(this.genBuf, 1, residual, off + 64, 32, pixOut);
        this.copyAdd(this.genBuf, 12, residual, off + 80, 40, pixOut);
        this.copyAdd(this.genBuf, 0, residual, off + 96, 48, pixOut);
        this.copyAdd(this.genBuf, 11, residual, off + 112, 56, pixOut);
    }

    public void predictHorizontalDown(int[] residual, boolean topRightAvailable, byte[] topLeft, byte[] leftRow, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateTop(true, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
        this.interpolateLeft(true, topLeft, leftRow, blkY, this.leftBuf);
        int tl = this.interpolateTopLeft(true, true, topLeft, topLine, leftRow, mbOffX, blkX, blkY);
        this.genBuf[0] = (byte) (this.leftBuf[7] + this.leftBuf[6] + 1 >> 1);
        this.genBuf[1] = (byte) (this.leftBuf[5] + this.leftBuf[7] + (this.leftBuf[6] << 1) + 2 >> 2);
        this.genBuf[2] = (byte) (this.leftBuf[6] + this.leftBuf[5] + 1 >> 1);
        this.genBuf[3] = (byte) (this.leftBuf[4] + this.leftBuf[6] + (this.leftBuf[5] << 1) + 2 >> 2);
        this.genBuf[4] = (byte) (this.leftBuf[5] + this.leftBuf[4] + 1 >> 1);
        this.genBuf[5] = (byte) (this.leftBuf[3] + this.leftBuf[5] + (this.leftBuf[4] << 1) + 2 >> 2);
        this.genBuf[6] = (byte) (this.leftBuf[4] + this.leftBuf[3] + 1 >> 1);
        this.genBuf[7] = (byte) (this.leftBuf[2] + this.leftBuf[4] + (this.leftBuf[3] << 1) + 2 >> 2);
        this.genBuf[8] = (byte) (this.leftBuf[3] + this.leftBuf[2] + 1 >> 1);
        this.genBuf[9] = (byte) (this.leftBuf[1] + this.leftBuf[3] + (this.leftBuf[2] << 1) + 2 >> 2);
        this.genBuf[10] = (byte) (this.leftBuf[2] + this.leftBuf[1] + 1 >> 1);
        this.genBuf[11] = (byte) (this.leftBuf[0] + this.leftBuf[2] + (this.leftBuf[1] << 1) + 2 >> 2);
        this.genBuf[12] = (byte) (this.leftBuf[1] + this.leftBuf[0] + 1 >> 1);
        this.genBuf[13] = (byte) (tl + this.leftBuf[1] + (this.leftBuf[0] << 1) + 2 >> 2);
        this.genBuf[14] = (byte) (this.leftBuf[0] + tl + 1 >> 1);
        this.genBuf[15] = (byte) (this.leftBuf[0] + this.topBuf[0] + (tl << 1) + 2 >> 2);
        this.genBuf[16] = (byte) (tl + this.topBuf[1] + (this.topBuf[0] << 1) + 2 >> 2);
        this.genBuf[17] = (byte) (this.topBuf[0] + this.topBuf[2] + (this.topBuf[1] << 1) + 2 >> 2);
        this.genBuf[18] = (byte) (this.topBuf[1] + this.topBuf[3] + (this.topBuf[2] << 1) + 2 >> 2);
        this.genBuf[19] = (byte) (this.topBuf[2] + this.topBuf[4] + (this.topBuf[3] << 1) + 2 >> 2);
        this.genBuf[20] = (byte) (this.topBuf[3] + this.topBuf[5] + (this.topBuf[4] << 1) + 2 >> 2);
        this.genBuf[21] = (byte) (this.topBuf[4] + this.topBuf[6] + (this.topBuf[5] << 1) + 2 >> 2);
        int off = (blkY << 4) + blkX;
        this.copyAdd(this.genBuf, 14, residual, off, 0, pixOut);
        this.copyAdd(this.genBuf, 12, residual, off + 16, 8, pixOut);
        this.copyAdd(this.genBuf, 10, residual, off + 32, 16, pixOut);
        this.copyAdd(this.genBuf, 8, residual, off + 48, 24, pixOut);
        this.copyAdd(this.genBuf, 6, residual, off + 64, 32, pixOut);
        this.copyAdd(this.genBuf, 4, residual, off + 80, 40, pixOut);
        this.copyAdd(this.genBuf, 2, residual, off + 96, 48, pixOut);
        this.copyAdd(this.genBuf, 0, residual, off + 112, 56, pixOut);
    }

    public void predictVerticalLeft(int[] residual, boolean topLeftAvailable, boolean topRightAvailable, byte[] topLeft, byte[] topLine, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateTop(topLeftAvailable, topRightAvailable, topLeft, topLine, mbOffX + blkX, blkY, this.topBuf);
        this.genBuf[0] = (byte) (this.topBuf[0] + this.topBuf[1] + 1 >> 1);
        this.genBuf[1] = (byte) (this.topBuf[1] + this.topBuf[2] + 1 >> 1);
        this.genBuf[2] = (byte) (this.topBuf[2] + this.topBuf[3] + 1 >> 1);
        this.genBuf[3] = (byte) (this.topBuf[3] + this.topBuf[4] + 1 >> 1);
        this.genBuf[4] = (byte) (this.topBuf[4] + this.topBuf[5] + 1 >> 1);
        this.genBuf[5] = (byte) (this.topBuf[5] + this.topBuf[6] + 1 >> 1);
        this.genBuf[6] = (byte) (this.topBuf[6] + this.topBuf[7] + 1 >> 1);
        this.genBuf[7] = (byte) (this.topBuf[7] + this.topBuf[8] + 1 >> 1);
        this.genBuf[8] = (byte) (this.topBuf[8] + this.topBuf[9] + 1 >> 1);
        this.genBuf[9] = (byte) (this.topBuf[9] + this.topBuf[10] + 1 >> 1);
        this.genBuf[10] = (byte) (this.topBuf[10] + this.topBuf[11] + 1 >> 1);
        this.genBuf[11] = (byte) (this.topBuf[0] + this.topBuf[2] + (this.topBuf[1] << 1) + 2 >> 2);
        this.genBuf[12] = (byte) (this.topBuf[1] + this.topBuf[3] + (this.topBuf[2] << 1) + 2 >> 2);
        this.genBuf[13] = (byte) (this.topBuf[2] + this.topBuf[4] + (this.topBuf[3] << 1) + 2 >> 2);
        this.genBuf[14] = (byte) (this.topBuf[3] + this.topBuf[5] + (this.topBuf[4] << 1) + 2 >> 2);
        this.genBuf[15] = (byte) (this.topBuf[4] + this.topBuf[6] + (this.topBuf[5] << 1) + 2 >> 2);
        this.genBuf[16] = (byte) (this.topBuf[5] + this.topBuf[7] + (this.topBuf[6] << 1) + 2 >> 2);
        this.genBuf[17] = (byte) (this.topBuf[6] + this.topBuf[8] + (this.topBuf[7] << 1) + 2 >> 2);
        this.genBuf[18] = (byte) (this.topBuf[7] + this.topBuf[9] + (this.topBuf[8] << 1) + 2 >> 2);
        this.genBuf[19] = (byte) (this.topBuf[8] + this.topBuf[10] + (this.topBuf[9] << 1) + 2 >> 2);
        this.genBuf[20] = (byte) (this.topBuf[9] + this.topBuf[11] + (this.topBuf[10] << 1) + 2 >> 2);
        this.genBuf[21] = (byte) (this.topBuf[10] + this.topBuf[12] + (this.topBuf[11] << 1) + 2 >> 2);
        int off = (blkY << 4) + blkX;
        this.copyAdd(this.genBuf, 0, residual, off, 0, pixOut);
        this.copyAdd(this.genBuf, 11, residual, off + 16, 8, pixOut);
        this.copyAdd(this.genBuf, 1, residual, off + 32, 16, pixOut);
        this.copyAdd(this.genBuf, 12, residual, off + 48, 24, pixOut);
        this.copyAdd(this.genBuf, 2, residual, off + 64, 32, pixOut);
        this.copyAdd(this.genBuf, 13, residual, off + 80, 40, pixOut);
        this.copyAdd(this.genBuf, 3, residual, off + 96, 48, pixOut);
        this.copyAdd(this.genBuf, 14, residual, off + 112, 56, pixOut);
    }

    public void predictHorizontalUp(int[] residual, boolean topLeftAvailable, byte[] topLeft, byte[] leftRow, int mbOffX, int blkX, int blkY, byte[] pixOut) {
        this.interpolateLeft(topLeftAvailable, topLeft, leftRow, blkY, this.leftBuf);
        this.genBuf[0] = (byte) (this.leftBuf[0] + this.leftBuf[1] + 1 >> 1);
        this.genBuf[1] = (byte) (this.leftBuf[2] + this.leftBuf[0] + (this.leftBuf[1] << 1) + 2 >> 2);
        this.genBuf[2] = (byte) (this.leftBuf[1] + this.leftBuf[2] + 1 >> 1);
        this.genBuf[3] = (byte) (this.leftBuf[3] + this.leftBuf[1] + (this.leftBuf[2] << 1) + 2 >> 2);
        this.genBuf[4] = (byte) (this.leftBuf[2] + this.leftBuf[3] + 1 >> 1);
        this.genBuf[5] = (byte) (this.leftBuf[4] + this.leftBuf[2] + (this.leftBuf[3] << 1) + 2 >> 2);
        this.genBuf[6] = (byte) (this.leftBuf[3] + this.leftBuf[4] + 1 >> 1);
        this.genBuf[7] = (byte) (this.leftBuf[5] + this.leftBuf[3] + (this.leftBuf[4] << 1) + 2 >> 2);
        this.genBuf[8] = (byte) (this.leftBuf[4] + this.leftBuf[5] + 1 >> 1);
        this.genBuf[9] = (byte) (this.leftBuf[6] + this.leftBuf[4] + (this.leftBuf[5] << 1) + 2 >> 2);
        this.genBuf[10] = (byte) (this.leftBuf[5] + this.leftBuf[6] + 1 >> 1);
        this.genBuf[11] = (byte) (this.leftBuf[7] + this.leftBuf[5] + (this.leftBuf[6] << 1) + 2 >> 2);
        this.genBuf[12] = (byte) (this.leftBuf[6] + this.leftBuf[7] + 1 >> 1);
        this.genBuf[13] = (byte) (this.leftBuf[6] + this.leftBuf[7] + (this.leftBuf[7] << 1) + 2 >> 2);
        this.genBuf[14] = this.genBuf[15] = this.genBuf[16] = this.genBuf[17] = this.genBuf[18] = this.genBuf[19] = this.genBuf[20] = this.genBuf[21] = this.leftBuf[7];
        int off = (blkY << 4) + blkX;
        this.copyAdd(this.genBuf, 0, residual, off, 0, pixOut);
        this.copyAdd(this.genBuf, 2, residual, off + 16, 8, pixOut);
        this.copyAdd(this.genBuf, 4, residual, off + 32, 16, pixOut);
        this.copyAdd(this.genBuf, 6, residual, off + 48, 24, pixOut);
        this.copyAdd(this.genBuf, 8, residual, off + 64, 32, pixOut);
        this.copyAdd(this.genBuf, 10, residual, off + 80, 40, pixOut);
        this.copyAdd(this.genBuf, 12, residual, off + 96, 48, pixOut);
        this.copyAdd(this.genBuf, 14, residual, off + 112, 56, pixOut);
    }
}