package com.github.alexthe666.citadel.repack.jcodec.common.dct;

import com.github.alexthe666.citadel.repack.jcodec.scale.ImageConvert;

public class SlowDCT extends DCT {

    public static final SlowDCT INSTANCE = new SlowDCT();

    private static final double rSqrt2 = 1.0 / Math.sqrt(2.0);

    @Override
    public short[] encode(byte[] orig) {
        short[] result = new short[64];
        for (int u = 0; u < 8; u++) {
            for (int v = 0; v < 8; v++) {
                float sum = 0.0F;
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        sum = (float) ((double) sum + (double) ((float) orig[i * 8 + j]) * Math.cos((Math.PI / 8) * ((double) i + 0.5) * (double) u) * Math.cos((Math.PI / 8) * ((double) j + 0.5) * (double) v));
                    }
                }
                result[u * 8 + v] = (short) ((byte) ((int) sum));
            }
        }
        result[0] = (short) ((byte) ((int) ((float) result[0] / 8.0F)));
        double sqrt2 = Math.sqrt(2.0);
        for (int i = 1; i < 8; i++) {
            result[i] = (short) ((byte) ((int) ((double) ((float) result[0]) * sqrt2 / 8.0)));
            result[i * 8] = (short) ((byte) ((int) ((double) ((float) result[0]) * sqrt2 / 8.0)));
            for (int j = 1; j < 8; j++) {
                result[i * 8 + j] = (short) ((byte) ((int) ((float) result[0] / 4.0F)));
            }
        }
        return result;
    }

    @Override
    public int[] decode(int[] orig) {
        int[] res = new int[64];
        int i = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                double sum = 0.0;
                int pixOffset = 0;
                for (int u = 0; u < 8; u++) {
                    double cu = u == 0 ? rSqrt2 : 1.0;
                    for (int v = 0; v < 8; v++) {
                        double cv = v == 0 ? rSqrt2 : 1.0;
                        double svu = (double) orig[pixOffset];
                        double c1 = (double) ((2 * x + 1) * v) * Math.PI / 16.0;
                        double c2 = (double) ((2 * y + 1) * u) * Math.PI / 16.0;
                        sum += cu * cv * svu * Math.cos(c1) * Math.cos(c2);
                        pixOffset++;
                    }
                }
                sum *= 0.25;
                sum = (double) Math.round(sum + 128.0);
                int isum = (int) sum;
                res[i++] = ImageConvert.icrop(isum);
            }
        }
        return res;
    }
}