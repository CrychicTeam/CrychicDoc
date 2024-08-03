package com.github.alexthe666.citadel.repack.jaad.aac.gain;

class IPQF implements GCConstants, PQFTables {

    private final float[] buf = new float[4];

    private final float[][] tmp1 = new float[2][24];

    private final float[][] tmp2 = new float[2][24];

    void process(float[][] in, int frameLen, int maxBand, float[] out) {
        for (int i = 0; i < frameLen; i++) {
            out[i] = 0.0F;
        }
        for (int var7 = 0; var7 < frameLen / 4; var7++) {
            for (int j = 0; j < 4; j++) {
                this.buf[j] = in[j][var7];
            }
            this.performSynthesis(this.buf, out, var7 * 4);
        }
    }

    private void performSynthesis(float[] in, float[] out, int outOff) {
        int kk = 12;
        for (int n = 0; n < 2; n++) {
            for (int k = 0; k < 23; k++) {
                this.tmp1[n][k] = this.tmp1[n][k + 1];
                this.tmp2[n][k] = this.tmp2[n][k + 1];
            }
        }
        for (int var10 = 0; var10 < 2; var10++) {
            float acc = 0.0F;
            for (int i = 0; i < 4; i++) {
                acc += COEFS_Q0[var10][i] * in[i];
            }
            this.tmp1[var10][23] = acc;
            acc = 0.0F;
            for (int var9 = 0; var9 < 4; var9++) {
                acc += COEFS_Q1[var10][var9] * in[var9];
            }
            this.tmp2[var10][23] = acc;
        }
        for (int var11 = 0; var11 < 2; var11++) {
            float acc = 0.0F;
            for (int k = 0; k < 12; k++) {
                acc += COEFS_T0[var11][k] * this.tmp1[var11][23 - 2 * k];
            }
            for (int var13 = 0; var13 < 12; var13++) {
                acc += COEFS_T1[var11][var13] * this.tmp2[var11][22 - 2 * var13];
            }
            out[outOff + var11] = acc;
            acc = 0.0F;
            for (int var14 = 0; var14 < 12; var14++) {
                acc += COEFS_T0[3 - var11][var14] * this.tmp1[var11][23 - 2 * var14];
            }
            for (int var15 = 0; var15 < 12; var15++) {
                acc -= COEFS_T1[3 - var11][var15] * this.tmp2[var11][22 - 2 * var15];
            }
            out[outOff + 4 - 1 - var11] = acc;
        }
    }
}