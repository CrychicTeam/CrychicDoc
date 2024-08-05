package com.github.alexthe666.citadel.repack.jcodec.codecs.h264.encode;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public class MotionEstimator {

    private int maxSearchRange;

    public MotionEstimator(int maxSearchRange) {
        this.maxSearchRange = maxSearchRange;
    }

    public int[] estimate(Picture ref, byte[] patch, int mbX, int mbY, int mvpx, int mvpy) {
        byte[] searchPatch = new byte[(this.maxSearchRange * 2 + 16) * (this.maxSearchRange * 2 + 16)];
        int startX = mbX << 4;
        int startY = mbY << 4;
        int patchTlX = Math.max(startX - this.maxSearchRange, 0);
        int patchTlY = Math.max(startY - this.maxSearchRange, 0);
        int patchBrX = Math.min(startX + this.maxSearchRange + 16, ref.getPlaneWidth(0));
        int patchBrY = Math.min(startY + this.maxSearchRange + 16, ref.getPlaneHeight(0));
        int centerX = startX - patchTlX;
        int centerY = startY - patchTlY;
        int patchW = patchBrX - patchTlX;
        int patchH = patchBrY - patchTlY;
        MBEncoderHelper.takeSafe(ref.getPlaneData(0), ref.getPlaneWidth(0), ref.getPlaneHeight(0), patchTlX, patchTlY, searchPatch, patchW, patchH);
        int bestMvX = centerX;
        int bestMvY = centerY;
        int bestScore = this.sad(searchPatch, patchW, patch, centerX, centerY);
        for (int i = 0; i < this.maxSearchRange; i++) {
            int score1 = bestMvX > 0 ? this.sad(searchPatch, patchW, patch, bestMvX - 1, bestMvY) : Integer.MAX_VALUE;
            int score2 = bestMvX < patchW - 1 ? this.sad(searchPatch, patchW, patch, bestMvX + 1, bestMvY) : Integer.MAX_VALUE;
            int score3 = bestMvY > 0 ? this.sad(searchPatch, patchW, patch, bestMvX, bestMvY - 1) : Integer.MAX_VALUE;
            int score4 = bestMvY < patchH - 1 ? this.sad(searchPatch, patchW, patch, bestMvX, bestMvY + 1) : Integer.MAX_VALUE;
            int min = Math.min(Math.min(Math.min(score1, score2), score3), score4);
            if (min > bestScore) {
                break;
            }
            bestScore = min;
            if (score1 == min) {
                bestMvX--;
            } else if (score2 == min) {
                bestMvX++;
            } else if (score3 == min) {
                bestMvY--;
            } else {
                bestMvY++;
            }
        }
        return new int[] { bestMvX - centerX << 2, bestMvY - centerY << 2 };
    }

    private int sad(byte[] big, int bigStride, byte[] small, int offX, int offY) {
        int score = 0;
        int bigOff = offY * bigStride + offX;
        int smallOff = 0;
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; smallOff++) {
                score += MathUtil.abs(big[bigOff] - small[smallOff]);
                j++;
                bigOff++;
            }
            bigOff += bigStride - 16;
        }
        return score;
    }
}