package com.github.alexthe666.citadel.repack.jcodec.scale;

import com.github.alexthe666.citadel.repack.jcodec.common.model.Size;

public class LanczosResampler extends BaseResampler {

    private static final int _nTaps = 6;

    private int precision = 256;

    private short[][] tapsXs;

    private short[][] tapsYs;

    private double _scaleFactorX;

    private double _scaleFactorY;

    public LanczosResampler(Size from, Size to) {
        super(from, to);
        this._scaleFactorX = (double) to.getWidth() / (double) from.getWidth();
        this._scaleFactorY = (double) to.getHeight() / (double) from.getHeight();
        this.tapsXs = new short[this.precision][6];
        this.tapsYs = new short[this.precision][6];
        buildTaps(6, this.precision, this._scaleFactorX, this.tapsXs);
        buildTaps(6, this.precision, this._scaleFactorY, this.tapsYs);
    }

    private static double sinc(double x) {
        return x == 0.0 ? 1.0 : Math.sin(x) / x;
    }

    private static void buildTaps(int nTaps, int precision, double scaleFactor, short[][] tapsOut) {
        double[] taps = new double[nTaps];
        for (int i = 0; i < precision; i++) {
            double o = (double) i / (double) precision;
            int j = -nTaps / 2 + 1;
            for (int t = 0; j < nTaps / 2 + 1; t++) {
                double x = -o + (double) j;
                double sinc_val = scaleFactor * sinc(scaleFactor * x * Math.PI);
                double wnd_val = Math.sin(x * Math.PI / (double) (nTaps - 1) + (Math.PI / 2));
                taps[t] = sinc_val * wnd_val;
                j++;
            }
            normalizeAndGenerateFixedPrecision(taps, 7, tapsOut[i]);
        }
    }

    @Override
    protected short[] getTapsX(int dstX) {
        int oi = (int) ((double) ((float) (dstX * this.precision)) / this._scaleFactorX);
        int sub_pel = oi % this.precision;
        return this.tapsXs[sub_pel];
    }

    @Override
    protected short[] getTapsY(int dstY) {
        int oy = (int) ((double) ((float) (dstY * this.precision)) / this._scaleFactorY);
        int sub_pel = oy % this.precision;
        return this.tapsYs[sub_pel];
    }

    @Override
    protected int nTaps() {
        return 6;
    }
}