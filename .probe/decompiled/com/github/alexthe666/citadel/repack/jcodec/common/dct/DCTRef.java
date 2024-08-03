package com.github.alexthe666.citadel.repack.jcodec.common.dct;

public class DCTRef {

    static double[] coefficients = new double[64];

    public static void fdct(int[] block, int off) {
        double[] out = new double[64];
        for (int i = 0; i < 64; i += 8) {
            for (int j = 0; j < 8; j++) {
                double tmp = 0.0;
                for (int k = 0; k < 8; k++) {
                    tmp += coefficients[i + k] * (double) block[k * 8 + j + off];
                }
                out[i + j] = tmp * 4.0;
            }
        }
        for (int j = 0; j < 8; j++) {
            for (int var8 = 0; var8 < 64; var8 += 8) {
                double tmp = 0.0;
                for (int k = 0; k < 8; k++) {
                    tmp += out[var8 + k] * coefficients[j * 8 + k];
                }
                block[var8 + j + off] = (int) (tmp + 0.499999999999);
            }
        }
    }

    public static void idct(int[] block, int off) {
        double[] out = new double[64];
        for (int i = 0; i < 64; i += 8) {
            for (int j = 0; j < 8; j++) {
                double tmp = 0.0;
                for (int k = 0; k < 8; k++) {
                    tmp += (double) block[i + k] * coefficients[k * 8 + j];
                }
                out[i + j] = tmp;
            }
        }
        for (int var8 = 0; var8 < 8; var8++) {
            for (int j = 0; j < 8; j++) {
                double tmp = 0.0;
                for (int k = 0; k < 64; k += 8) {
                    tmp += coefficients[k + var8] * out[k + j];
                }
                block[var8 * 8 + j] = (int) (tmp + 0.5);
            }
        }
    }

    static {
        for (int j = 0; j < 8; j++) {
            coefficients[j] = Math.sqrt(0.125);
            for (int i = 8; i < 64; i += 8) {
                coefficients[i + j] = 0.5 * Math.cos((double) i * ((double) j + 0.5) * Math.PI / 64.0);
            }
        }
    }
}