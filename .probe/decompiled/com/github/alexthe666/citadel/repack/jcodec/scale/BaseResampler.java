package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Picture;
import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;
import com.github.alexthe666.citadel.repack.jcodec.common.tools.MathUtil;

public abstract class BaseResampler {

    private final ThreadLocal<int[]> tempBuffers;

    private Size toSize;

    private Size fromSize;

    private final double scaleFactorX;

    private final double scaleFactorY;

    public BaseResampler(Size from, Size to) {
        this.toSize = to;
        this.fromSize = from;
        this.scaleFactorX = (double) from.getWidth() / (double) to.getWidth();
        this.scaleFactorY = (double) from.getHeight() / (double) to.getHeight();
        this.tempBuffers = new ThreadLocal();
    }

    private static byte getPel(Picture pic, int plane, int x, int y) {
        if (x < 0) {
            x = 0;
        }
        if (y < 0) {
            y = 0;
        }
        int w = pic.getPlaneWidth(plane);
        if (x > w - 1) {
            x = w - 1;
        }
        int h = pic.getPlaneHeight(plane);
        if (y > h - 1) {
            y = h - 1;
        }
        return pic.getData()[plane][x + y * w];
    }

    protected abstract short[] getTapsX(int var1);

    protected abstract short[] getTapsY(int var1);

    protected abstract int nTaps();

    public static void normalizeAndGenerateFixedPrecision(double[] taps, int precBits, short[] out) {
        double sum = 0.0;
        for (int i = 0; i < taps.length; i++) {
            sum += taps[i];
        }
        int sumFix = 0;
        int precNum = 1 << precBits;
        for (int i = 0; i < taps.length; i++) {
            double d = taps[i] * (double) precNum / sum + (double) precNum;
            int s = (int) d;
            taps[i] = d - (double) s;
            out[i] = (short) (s - precNum);
            sumFix += out[i];
        }
        long tapsTaken = 0L;
        while (sumFix < precNum) {
            int maxI = -1;
            for (int i = 0; i < taps.length; i++) {
                if ((tapsTaken & (long) (1 << i)) == 0L && (maxI == -1 || taps[i] > taps[maxI])) {
                    maxI = i;
                }
            }
            out[maxI]++;
            sumFix++;
            tapsTaken |= (long) (1 << maxI);
        }
        for (int ix = 0; ix < taps.length; ix++) {
            taps[ix] += (double) out[ix];
            if ((tapsTaken & (long) (1 << ix)) != 0L) {
                taps[ix]--;
            }
        }
    }

    public void resample(Picture src, Picture dst) {
        int[] temp = (int[]) this.tempBuffers.get();
        int taps = this.nTaps();
        if (temp == null) {
            temp = new int[this.toSize.getWidth() * (this.fromSize.getHeight() + taps)];
            this.tempBuffers.set(temp);
        }
        for (int p = 0; p < src.getColor().nComp; p++) {
            for (int y = 0; y < src.getPlaneHeight(p) + taps; y++) {
                for (int x = 0; x < dst.getPlaneWidth(p); x++) {
                    short[] tapsXs = this.getTapsX(x);
                    int srcX = (int) (this.scaleFactorX * (double) x) - taps / 2 + 1;
                    int sum = 0;
                    for (int i = 0; i < taps; i++) {
                        sum += (getPel(src, p, srcX + i, y - taps / 2 + 1) + 128) * tapsXs[i];
                    }
                    temp[y * this.toSize.getWidth() + x] = sum;
                }
            }
            for (int y = 0; y < dst.getPlaneHeight(p); y++) {
                for (int x = 0; x < dst.getPlaneWidth(p); x++) {
                    short[] tapsYs = this.getTapsY(y);
                    int srcY = (int) (this.scaleFactorY * (double) y);
                    int sum = 0;
                    for (int i = 0; i < taps; i++) {
                        sum += temp[x + (srcY + i) * this.toSize.getWidth()] * tapsYs[i];
                    }
                    dst.getPlaneData(p)[y * dst.getPlaneWidth(p) + x] = (byte) (MathUtil.clip(sum + 8192 >> 14, 0, 255) - 128);
                }
            }
        }
    }
}