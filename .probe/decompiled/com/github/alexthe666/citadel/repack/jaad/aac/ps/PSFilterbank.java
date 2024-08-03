package com.github.alexthe666.citadel.repack.jaad.aac.ps;

class PSFilterbank implements PSTables {

    private int frame_len;

    private int[] resolution20 = new int[3];

    private int[] resolution34 = new int[5];

    private float[][] work;

    private float[][][] buffer;

    private float[][][] temp;

    PSFilterbank(int numTimeSlotsRate) {
        this.resolution34[0] = 12;
        this.resolution34[1] = 8;
        this.resolution34[2] = 4;
        this.resolution34[3] = 4;
        this.resolution34[4] = 4;
        this.resolution20[0] = 8;
        this.resolution20[1] = 2;
        this.resolution20[2] = 2;
        this.frame_len = numTimeSlotsRate;
        this.work = new float[this.frame_len + 12][2];
        this.buffer = new float[5][2][2];
        this.temp = new float[this.frame_len][12][2];
    }

    void hybrid_analysis(float[][][] X, float[][][] X_hybrid, boolean use34, int numTimeSlotsRate) {
        int offset = 0;
        int qmf_bands = use34 ? 5 : 3;
        int[] resolution = use34 ? this.resolution34 : this.resolution20;
        for (int band = 0; band < qmf_bands; band++) {
            for (int i = 0; i < 12; i++) {
                this.work[i][0] = this.buffer[band][i][0];
                this.work[i][1] = this.buffer[band][i][1];
            }
            for (int n = 0; n < this.frame_len; n++) {
                this.work[12 + n][0] = X[n + 6][band][0];
                this.work[12 + n][0] = X[n + 6][band][0];
            }
            for (int i = 0; i < 12; i++) {
                this.buffer[band][i][0] = this.work[this.frame_len + i][0];
                this.buffer[band][i][1] = this.work[this.frame_len + i][1];
            }
            switch(resolution[band]) {
                case 2:
                    channel_filter2(this.frame_len, p2_13_20, this.work, this.temp);
                    break;
                case 4:
                    channel_filter4(this.frame_len, p4_13_34, this.work, this.temp);
                    break;
                case 8:
                    this.channel_filter8(this.frame_len, use34 ? p8_13_34 : p8_13_20, this.work, this.temp);
                    break;
                case 12:
                    this.channel_filter12(this.frame_len, p12_13_34, this.work, this.temp);
            }
            for (int var12 = 0; var12 < this.frame_len; var12++) {
                for (int k = 0; k < resolution[band]; k++) {
                    X_hybrid[var12][offset + k][0] = this.temp[var12][k][0];
                    X_hybrid[var12][offset + k][1] = this.temp[var12][k][1];
                }
            }
            offset += resolution[band];
        }
        if (!use34) {
            for (int n = 0; n < numTimeSlotsRate; n++) {
                X_hybrid[n][3][0] = X_hybrid[n][3][0] + X_hybrid[n][4][0];
                X_hybrid[n][3][1] = X_hybrid[n][3][1] + X_hybrid[n][4][1];
                X_hybrid[n][4][0] = 0.0F;
                X_hybrid[n][4][1] = 0.0F;
                X_hybrid[n][2][0] = X_hybrid[n][2][0] + X_hybrid[n][5][0];
                X_hybrid[n][2][1] = X_hybrid[n][2][1] + X_hybrid[n][5][1];
                X_hybrid[n][5][0] = 0.0F;
                X_hybrid[n][5][1] = 0.0F;
            }
        }
    }

    static void channel_filter2(int frame_len, float[] filter, float[][] buffer, float[][][] X_hybrid) {
        for (int i = 0; i < frame_len; i++) {
            float r0 = filter[0] * (buffer[0 + i][0] + buffer[12 + i][0]);
            float r1 = filter[1] * (buffer[1 + i][0] + buffer[11 + i][0]);
            float r2 = filter[2] * (buffer[2 + i][0] + buffer[10 + i][0]);
            float r3 = filter[3] * (buffer[3 + i][0] + buffer[9 + i][0]);
            float r4 = filter[4] * (buffer[4 + i][0] + buffer[8 + i][0]);
            float r5 = filter[5] * (buffer[5 + i][0] + buffer[7 + i][0]);
            float r6 = filter[6] * buffer[6 + i][0];
            float i0 = filter[0] * (buffer[0 + i][1] + buffer[12 + i][1]);
            float i1 = filter[1] * (buffer[1 + i][1] + buffer[11 + i][1]);
            float i2 = filter[2] * (buffer[2 + i][1] + buffer[10 + i][1]);
            float i3 = filter[3] * (buffer[3 + i][1] + buffer[9 + i][1]);
            float i4 = filter[4] * (buffer[4 + i][1] + buffer[8 + i][1]);
            float i5 = filter[5] * (buffer[5 + i][1] + buffer[7 + i][1]);
            float i6 = filter[6] * buffer[6 + i][1];
            X_hybrid[i][0][0] = r0 + r1 + r2 + r3 + r4 + r5 + r6;
            X_hybrid[i][0][1] = i0 + i1 + i2 + i3 + i4 + i5 + i6;
            X_hybrid[i][1][0] = r0 - r1 + r2 - r3 + r4 - r5 + r6;
            X_hybrid[i][1][1] = i0 - i1 + i2 - i3 + i4 - i5 + i6;
        }
    }

    static void channel_filter4(int frame_len, float[] filter, float[][] buffer, float[][][] X_hybrid) {
        float[] input_re1 = new float[2];
        float[] input_re2 = new float[2];
        float[] input_im1 = new float[2];
        float[] input_im2 = new float[2];
        for (int i = 0; i < frame_len; i++) {
            input_re1[0] = -(filter[2] * (buffer[i + 2][0] + buffer[i + 10][0])) + filter[6] * buffer[i + 6][0];
            input_re1[1] = -0.70710677F * (filter[1] * (buffer[i + 1][0] + buffer[i + 11][0]) + filter[3] * (buffer[i + 3][0] + buffer[i + 9][0]) - filter[5] * (buffer[i + 5][0] + buffer[i + 7][0]));
            input_im1[0] = filter[0] * (buffer[i + 0][1] - buffer[i + 12][1]) - filter[4] * (buffer[i + 4][1] - buffer[i + 8][1]);
            input_im1[1] = 0.70710677F * (filter[1] * (buffer[i + 1][1] - buffer[i + 11][1]) - filter[3] * (buffer[i + 3][1] - buffer[i + 9][1]) - filter[5] * (buffer[i + 5][1] - buffer[i + 7][1]));
            input_re2[0] = filter[0] * (buffer[i + 0][0] - buffer[i + 12][0]) - filter[4] * (buffer[i + 4][0] - buffer[i + 8][0]);
            input_re2[1] = 0.70710677F * (filter[1] * (buffer[i + 1][0] - buffer[i + 11][0]) - filter[3] * (buffer[i + 3][0] - buffer[i + 9][0]) - filter[5] * (buffer[i + 5][0] - buffer[i + 7][0]));
            input_im2[0] = -(filter[2] * (buffer[i + 2][1] + buffer[i + 10][1])) + filter[6] * buffer[i + 6][1];
            input_im2[1] = -0.70710677F * (filter[1] * (buffer[i + 1][1] + buffer[i + 11][1]) + filter[3] * (buffer[i + 3][1] + buffer[i + 9][1]) - filter[5] * (buffer[i + 5][1] + buffer[i + 7][1]));
            X_hybrid[i][0][0] = input_re1[0] + input_re1[1] + input_im1[0] + input_im1[1];
            X_hybrid[i][0][1] = -input_re2[0] - input_re2[1] + input_im2[0] + input_im2[1];
            X_hybrid[i][1][0] = input_re1[0] - input_re1[1] - input_im1[0] + input_im1[1];
            X_hybrid[i][1][1] = input_re2[0] - input_re2[1] + input_im2[0] - input_im2[1];
            X_hybrid[i][2][0] = input_re1[0] - input_re1[1] + input_im1[0] - input_im1[1];
            X_hybrid[i][2][1] = -input_re2[0] + input_re2[1] + input_im2[0] - input_im2[1];
            X_hybrid[i][3][0] = input_re1[0] + input_re1[1] - input_im1[0] - input_im1[1];
            X_hybrid[i][3][1] = input_re2[0] + input_re2[1] + input_im2[0] + input_im2[1];
        }
    }

    static void DCT3_4_unscaled(float[] y, float[] x) {
        float f0 = x[2] * 0.70710677F;
        float f1 = x[0] - f0;
        float f2 = x[0] + f0;
        float f3 = x[1] + x[3];
        float f4 = x[1] * 1.306563F;
        float f5 = f3 * -0.9238795F;
        float f6 = x[3] * -0.5411961F;
        float f7 = f4 + f5;
        float f8 = f6 - f5;
        y[3] = f2 - f8;
        y[0] = f2 + f8;
        y[2] = f1 - f7;
        y[1] = f1 + f7;
    }

    void channel_filter8(int frame_len, float[] filter, float[][] buffer, float[][][] X_hybrid) {
        float[] input_re1 = new float[4];
        float[] input_re2 = new float[4];
        float[] input_im1 = new float[4];
        float[] input_im2 = new float[4];
        float[] x = new float[4];
        for (int i = 0; i < frame_len; i++) {
            input_re1[0] = filter[6] * buffer[6 + i][0];
            input_re1[1] = filter[5] * (buffer[5 + i][0] + buffer[7 + i][0]);
            input_re1[2] = -(filter[0] * (buffer[0 + i][0] + buffer[12 + i][0])) + filter[4] * (buffer[4 + i][0] + buffer[8 + i][0]);
            input_re1[3] = -(filter[1] * (buffer[1 + i][0] + buffer[11 + i][0])) + filter[3] * (buffer[3 + i][0] + buffer[9 + i][0]);
            input_im1[0] = filter[5] * (buffer[7 + i][1] - buffer[5 + i][1]);
            input_im1[1] = filter[0] * (buffer[12 + i][1] - buffer[0 + i][1]) + filter[4] * (buffer[8 + i][1] - buffer[4 + i][1]);
            input_im1[2] = filter[1] * (buffer[11 + i][1] - buffer[1 + i][1]) + filter[3] * (buffer[9 + i][1] - buffer[3 + i][1]);
            input_im1[3] = filter[2] * (buffer[10 + i][1] - buffer[2 + i][1]);
            for (int n = 0; n < 4; n++) {
                x[n] = input_re1[n] - input_im1[3 - n];
            }
            DCT3_4_unscaled(x, x);
            X_hybrid[i][7][0] = x[0];
            X_hybrid[i][5][0] = x[2];
            X_hybrid[i][3][0] = x[3];
            X_hybrid[i][1][0] = x[1];
            for (int var12 = 0; var12 < 4; var12++) {
                x[var12] = input_re1[var12] + input_im1[3 - var12];
            }
            DCT3_4_unscaled(x, x);
            X_hybrid[i][6][0] = x[1];
            X_hybrid[i][4][0] = x[3];
            X_hybrid[i][2][0] = x[2];
            X_hybrid[i][0][0] = x[0];
            input_im2[0] = filter[6] * buffer[6 + i][1];
            input_im2[1] = filter[5] * (buffer[5 + i][1] + buffer[7 + i][1]);
            input_im2[2] = -(filter[0] * (buffer[0 + i][1] + buffer[12 + i][1])) + filter[4] * (buffer[4 + i][1] + buffer[8 + i][1]);
            input_im2[3] = -(filter[1] * (buffer[1 + i][1] + buffer[11 + i][1])) + filter[3] * (buffer[3 + i][1] + buffer[9 + i][1]);
            input_re2[0] = filter[5] * (buffer[7 + i][0] - buffer[5 + i][0]);
            input_re2[1] = filter[0] * (buffer[12 + i][0] - buffer[0 + i][0]) + filter[4] * (buffer[8 + i][0] - buffer[4 + i][0]);
            input_re2[2] = filter[1] * (buffer[11 + i][0] - buffer[1 + i][0]) + filter[3] * (buffer[9 + i][0] - buffer[3 + i][0]);
            input_re2[3] = filter[2] * (buffer[10 + i][0] - buffer[2 + i][0]);
            for (int var13 = 0; var13 < 4; var13++) {
                x[var13] = input_im2[var13] + input_re2[3 - var13];
            }
            DCT3_4_unscaled(x, x);
            X_hybrid[i][7][1] = x[0];
            X_hybrid[i][5][1] = x[2];
            X_hybrid[i][3][1] = x[3];
            X_hybrid[i][1][1] = x[1];
            for (int var14 = 0; var14 < 4; var14++) {
                x[var14] = input_im2[var14] - input_re2[3 - var14];
            }
            DCT3_4_unscaled(x, x);
            X_hybrid[i][6][1] = x[1];
            X_hybrid[i][4][1] = x[3];
            X_hybrid[i][2][1] = x[2];
            X_hybrid[i][0][1] = x[0];
        }
    }

    void DCT3_6_unscaled(float[] y, float[] x) {
        float f0 = x[3] * 0.70710677F;
        float f1 = x[0] + f0;
        float f2 = x[0] - f0;
        float f3 = (x[1] - x[5]) * 0.70710677F;
        float f4 = x[2] * 0.8660254F + x[4] * 0.5F;
        float f5 = f4 - x[4];
        float f6 = x[1] * 0.9659258F + x[5] * 0.25881904F;
        float f7 = f6 - f3;
        y[0] = f1 + f6 + f4;
        y[1] = f2 + f3 - x[4];
        y[2] = f7 + f2 - f5;
        y[3] = f1 - f7 - f5;
        y[4] = f1 - f3 - x[4];
        y[5] = f2 - f6 + f4;
    }

    void channel_filter12(int frame_len, float[] filter, float[][] buffer, float[][][] X_hybrid) {
        float[] input_re1 = new float[6];
        float[] input_re2 = new float[6];
        float[] input_im1 = new float[6];
        float[] input_im2 = new float[6];
        float[] out_re1 = new float[6];
        float[] out_re2 = new float[6];
        float[] out_im1 = new float[6];
        float[] out_im2 = new float[6];
        for (int i = 0; i < frame_len; i++) {
            for (int n = 0; n < 6; n++) {
                if (n == 0) {
                    input_re1[0] = buffer[6 + i][0] * filter[6];
                    input_re2[0] = buffer[6 + i][1] * filter[6];
                } else {
                    input_re1[6 - n] = (buffer[n + i][0] + buffer[12 - n + i][0]) * filter[n];
                    input_re2[6 - n] = (buffer[n + i][1] + buffer[12 - n + i][1]) * filter[n];
                }
                input_im2[n] = (buffer[n + i][0] - buffer[12 - n + i][0]) * filter[n];
                input_im1[n] = (buffer[n + i][1] - buffer[12 - n + i][1]) * filter[n];
            }
            this.DCT3_6_unscaled(out_re1, input_re1);
            this.DCT3_6_unscaled(out_re2, input_re2);
            this.DCT3_6_unscaled(out_im1, input_im1);
            this.DCT3_6_unscaled(out_im2, input_im2);
            for (int var15 = 0; var15 < 6; var15 += 2) {
                X_hybrid[i][var15][0] = out_re1[var15] - out_im1[var15];
                X_hybrid[i][var15][1] = out_re2[var15] + out_im2[var15];
                X_hybrid[i][var15 + 1][0] = out_re1[var15 + 1] + out_im1[var15 + 1];
                X_hybrid[i][var15 + 1][1] = out_re2[var15 + 1] - out_im2[var15 + 1];
                X_hybrid[i][10 - var15][0] = out_re1[var15 + 1] - out_im1[var15 + 1];
                X_hybrid[i][10 - var15][1] = out_re2[var15 + 1] + out_im2[var15 + 1];
                X_hybrid[i][11 - var15][0] = out_re1[var15] + out_im1[var15];
                X_hybrid[i][11 - var15][1] = out_re2[var15] - out_im2[var15];
            }
        }
    }

    void hybrid_synthesis(float[][][] X, float[][][] X_hybrid, boolean use34, int numTimeSlotsRate) {
        int offset = 0;
        int qmf_bands = use34 ? 5 : 3;
        int[] resolution = use34 ? this.resolution34 : this.resolution20;
        for (int band = 0; band < qmf_bands; band++) {
            for (int n = 0; n < this.frame_len; n++) {
                X[n][band][0] = 0.0F;
                X[n][band][1] = 0.0F;
                for (int k = 0; k < resolution[band]; k++) {
                    X[n][band][0] = X[n][band][0] + X_hybrid[n][offset + k][0];
                    X[n][band][1] = X[n][band][1] + X_hybrid[n][offset + k][1];
                }
            }
            offset += resolution[band];
        }
    }
}