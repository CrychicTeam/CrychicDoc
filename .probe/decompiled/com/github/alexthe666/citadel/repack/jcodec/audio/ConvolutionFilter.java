package com.github.alexthe666.citadel.repack.jcodec.audio;

import java.nio.FloatBuffer;

public abstract class ConvolutionFilter implements AudioFilter {

    private double[] kernel;

    protected abstract double[] buildKernel();

    @Override
    public void filter(FloatBuffer[] _in, long[] pos, FloatBuffer[] out) {
        if (_in.length != 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " filter is designed to work only on one input");
        } else if (out.length != 1) {
            throw new IllegalArgumentException(this.getClass().getName() + " filter is designed to work only on one output");
        } else {
            FloatBuffer in0 = _in[0];
            FloatBuffer out0 = out[0];
            if (this.kernel == null) {
                this.kernel = this.buildKernel();
            }
            if (out0.remaining() < in0.remaining() - this.kernel.length) {
                throw new IllegalArgumentException("Output buffer is too small");
            } else if (in0.remaining() <= this.kernel.length) {
                throw new IllegalArgumentException("Input buffer should contain > kernel lenght (" + this.kernel.length + ") samples.");
            } else {
                int halfKernel = this.kernel.length / 2;
                int i;
                for (i = in0.position() + halfKernel; i < in0.limit() - halfKernel; i++) {
                    double result = 0.0;
                    for (int j = 0; j < this.kernel.length; j++) {
                        result += this.kernel[j] * (double) in0.get(i + j - halfKernel);
                    }
                    out0.put((float) result);
                }
                in0.position(i - halfKernel);
            }
        }
    }

    @Override
    public int getDelay() {
        if (this.kernel == null) {
            this.kernel = this.buildKernel();
        }
        return this.kernel.length / 2;
    }

    @Override
    public int getNInputs() {
        return 1;
    }

    @Override
    public int getNOutputs() {
        return 1;
    }
}