package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;

public class BicubicResampler extends BaseResampler {

    private short[][] horizontalTaps;

    private short[][] verticalTaps;

    private static double alpha = 0.6;

    public BicubicResampler(Size from, Size to) {
        super(from, to);
        this.horizontalTaps = buildFilterTaps(to.getWidth(), from.getWidth());
        this.verticalTaps = buildFilterTaps(to.getHeight(), from.getHeight());
    }

    private static short[][] buildFilterTaps(int to, int from) {
        double[] taps = new double[4];
        short[][] tapsOut = new short[to][4];
        double ratio = (double) from / (double) to;
        double toByFrom = (double) to / (double) from;
        double srcPos = 0.0;
        for (int i = 0; i < to; i++) {
            double fraction = srcPos - (double) ((int) srcPos);
            for (int t = -1; t < 3; t++) {
                double d = (double) t - fraction;
                if (to < from) {
                    d *= toByFrom;
                }
                double x = Math.abs(d);
                double xx = x * x;
                double xxx = xx * x;
                if (d >= -1.0 && d <= 1.0) {
                    taps[t + 1] = (2.0 - alpha) * xxx + (-3.0 + alpha) * xx + 1.0;
                } else if (!(d < -2.0) && !(d > 2.0)) {
                    taps[t + 1] = -alpha * xxx + 5.0 * alpha * xx - 8.0 * alpha * x + 4.0 * alpha;
                } else {
                    taps[t + 1] = 0.0;
                }
            }
            normalizeAndGenerateFixedPrecision(taps, 7, tapsOut[i]);
            srcPos += ratio;
        }
        return tapsOut;
    }

    @Override
    protected short[] getTapsX(int dstX) {
        return this.horizontalTaps[dstX];
    }

    @Override
    protected short[] getTapsY(int dstY) {
        return this.verticalTaps[dstY];
    }

    @Override
    protected int nTaps() {
        return 4;
    }
}