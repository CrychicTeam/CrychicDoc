package com.github.alexthe666.citadel.repack.jcodec.audio;

public class SincLowPassFilter extends ConvolutionFilter {

    private int kernelSize;

    private double cutoffFreq;

    public static SincLowPassFilter createSincLowPassFilter(double cutoffFreq) {
        return new SincLowPassFilter(40, cutoffFreq);
    }

    public static SincLowPassFilter createSincLowPassFilter2(int cutoffFreq, int samplingRate) {
        return new SincLowPassFilter(40, (double) cutoffFreq / (double) samplingRate);
    }

    public SincLowPassFilter(int kernelSize, double cutoffFreq) {
        this.kernelSize = kernelSize;
        this.cutoffFreq = cutoffFreq;
    }

    @Override
    protected double[] buildKernel() {
        double[] kernel = new double[this.kernelSize];
        double sum = 0.0;
        for (int i = 0; i < this.kernelSize; i++) {
            int a = i - this.kernelSize / 2;
            if (a != 0) {
                kernel[i] = Math.sin((Math.PI * 2) * this.cutoffFreq * (double) (i - this.kernelSize / 2)) / (double) (i - this.kernelSize / 2) * (0.54 - 0.46 * Math.cos((Math.PI * 2) * (double) i / (double) this.kernelSize));
            } else {
                kernel[i] = (Math.PI * 2) * this.cutoffFreq;
            }
            sum += kernel[i];
        }
        for (int i = 0; i < this.kernelSize; i++) {
            kernel[i] /= sum;
        }
        return kernel;
    }
}