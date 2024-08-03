package com.github.alexthe666.citadel.repack.jaad.aac.sbr;

import java.util.Arrays;

class AnalysisFilterbank implements FilterbankTable {

    private float[] x;

    private int x_index;

    private int channels;

    AnalysisFilterbank(int channels) {
        this.channels = channels;
        this.x = new float[2 * channels * 10];
        this.x_index = 0;
    }

    public void reset() {
        Arrays.fill(this.x, 0.0F);
    }

    void sbr_qmf_analysis_32(SBR sbr, float[] input, float[][][] X, int offset, int kx) {
        float[] u = new float[64];
        float[] in_real = new float[32];
        float[] in_imag = new float[32];
        float[] out_real = new float[32];
        float[] out_imag = new float[32];
        int in = 0;
        for (int l = 0; l < sbr.numTimeSlotsRate; l++) {
            for (int n = 31; n >= 0; n--) {
                this.x[this.x_index + n] = this.x[this.x_index + n + 320] = input[in++];
            }
            for (int var14 = 0; var14 < 64; var14++) {
                u[var14] = this.x[this.x_index + var14] * qmf_c[2 * var14] + this.x[this.x_index + var14 + 64] * qmf_c[2 * (var14 + 64)] + this.x[this.x_index + var14 + 128] * qmf_c[2 * (var14 + 128)] + this.x[this.x_index + var14 + 192] * qmf_c[2 * (var14 + 192)] + this.x[this.x_index + var14 + 256] * qmf_c[2 * (var14 + 256)];
            }
            this.x_index -= 32;
            if (this.x_index < 0) {
                this.x_index = 288;
            }
            in_imag[31] = u[1];
            in_real[0] = u[0];
            for (int var15 = 1; var15 < 31; var15++) {
                in_imag[31 - var15] = u[var15 + 1];
                in_real[var15] = -u[64 - var15];
            }
            in_imag[0] = u[32];
            in_real[31] = -u[33];
            DCT.dct4_kernel(in_real, in_imag, out_real, out_imag);
            for (int var16 = 0; var16 < 16; var16++) {
                if (2 * var16 + 1 < kx) {
                    X[l + offset][2 * var16][0] = 2.0F * out_real[var16];
                    X[l + offset][2 * var16][1] = 2.0F * out_imag[var16];
                    X[l + offset][2 * var16 + 1][0] = -2.0F * out_imag[31 - var16];
                    X[l + offset][2 * var16 + 1][1] = -2.0F * out_real[31 - var16];
                } else {
                    if (2 * var16 < kx) {
                        X[l + offset][2 * var16][0] = 2.0F * out_real[var16];
                        X[l + offset][2 * var16][1] = 2.0F * out_imag[var16];
                    } else {
                        X[l + offset][2 * var16][0] = 0.0F;
                        X[l + offset][2 * var16][1] = 0.0F;
                    }
                    X[l + offset][2 * var16 + 1][0] = 0.0F;
                    X[l + offset][2 * var16 + 1][1] = 0.0F;
                }
            }
        }
    }
}