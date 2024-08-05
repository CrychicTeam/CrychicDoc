package com.github.alexthe666.citadel.repack.jcodec.codecs.vpx;

public class VP8DCT {

    private static final int cospi8sqrt2minus1 = 20091;

    private static final int sinpi8sqrt2 = 35468;

    public static int[] decodeDCT(int[] input) {
        int offset = 0;
        int[] output = new int[16];
        for (int i = 0; i < 4; i++) {
            int a1 = input[offset + 0] + input[offset + 8];
            int b1 = input[offset + 0] - input[offset + 8];
            int temp1 = input[offset + 4] * 35468 >> 16;
            int temp2 = input[offset + 12] + (input[offset + 12] * 20091 >> 16);
            int c1 = temp1 - temp2;
            temp1 = input[offset + 4] + (input[offset + 4] * 20091 >> 16);
            temp2 = input[offset + 12] * 35468 >> 16;
            int d1 = temp1 + temp2;
            output[offset + 0] = a1 + d1;
            output[offset + 12] = a1 - d1;
            output[offset + 4] = b1 + c1;
            output[offset + 8] = b1 - c1;
            offset++;
        }
        offset = 0;
        for (int var10 = 0; var10 < 4; var10++) {
            int a1 = output[offset * 4 + 0] + output[offset * 4 + 2];
            int b1 = output[offset * 4 + 0] - output[offset * 4 + 2];
            int temp1 = output[offset * 4 + 1] * 35468 >> 16;
            int temp2 = output[offset * 4 + 3] + (output[offset * 4 + 3] * 20091 >> 16);
            int c1 = temp1 - temp2;
            temp1 = output[offset * 4 + 1] + (output[offset * 4 + 1] * 20091 >> 16);
            temp2 = output[offset * 4 + 3] * 35468 >> 16;
            int d1 = temp1 + temp2;
            output[offset * 4 + 0] = a1 + d1 + 4 >> 3;
            output[offset * 4 + 3] = a1 - d1 + 4 >> 3;
            output[offset * 4 + 1] = b1 + c1 + 4 >> 3;
            output[offset * 4 + 2] = b1 - c1 + 4 >> 3;
            offset++;
        }
        return output;
    }

    public static int[] encodeDCT(int[] input) {
        int ip = 0;
        int[] output = new int[input.length];
        int op = 0;
        for (int i = 0; i < 4; i++) {
            int a1 = input[ip + 0] + input[ip + 3] << 3;
            int b1 = input[ip + 1] + input[ip + 2] << 3;
            int c1 = input[ip + 1] - input[ip + 2] << 3;
            int d1 = input[ip + 0] - input[ip + 3] << 3;
            output[op + 0] = a1 + b1;
            output[op + 2] = a1 - b1;
            output[op + 1] = c1 * 2217 + d1 * 5352 + 14500 >> 12;
            output[op + 3] = d1 * 2217 - c1 * 5352 + 7500 >> 12;
            ip += 4;
            op += 4;
        }
        ip = 0;
        op = 0;
        for (int var9 = 0; var9 < 4; var9++) {
            int a1 = output[ip + 0] + output[ip + 12];
            int b1 = output[ip + 4] + output[ip + 8];
            int c1 = output[ip + 4] - output[ip + 8];
            int d1 = output[ip + 0] - output[ip + 12];
            output[op + 0] = a1 + b1 + 7 >> 4;
            output[op + 8] = a1 - b1 + 7 >> 4;
            output[op + 4] = (c1 * 2217 + d1 * 5352 + 12000 >> 16) + (d1 != 0 ? 1 : 0);
            output[op + 12] = d1 * 2217 - c1 * 5352 + 51000 >> 16;
            ip++;
            op++;
        }
        return output;
    }

    public static int[] decodeWHT(int[] input) {
        int[] output = new int[16];
        int[][] diff = new int[4][4];
        int offset = 0;
        for (int i = 0; i < 4; i++) {
            int a1 = input[offset + 0] + input[offset + 12];
            int b1 = input[offset + 4] + input[offset + 8];
            int c1 = input[offset + 4] - input[offset + 8];
            int d1 = input[offset + 0] - input[offset + 12];
            output[offset + 0] = a1 + b1;
            output[offset + 4] = c1 + d1;
            output[offset + 8] = a1 - b1;
            output[offset + 12] = d1 - c1;
            offset++;
        }
        int var18 = 0;
        for (int var13 = 0; var13 < 4; var13++) {
            int a1 = output[var18 + 0] + output[var18 + 3];
            int b1 = output[var18 + 1] + output[var18 + 2];
            int c1 = output[var18 + 1] - output[var18 + 2];
            int d1 = output[var18 + 0] - output[var18 + 3];
            int a2 = a1 + b1;
            int b2 = c1 + d1;
            int c2 = a1 - b1;
            int d2 = d1 - c1;
            output[var18 + 0] = a2 + 3 >> 3;
            output[var18 + 1] = b2 + 3 >> 3;
            output[var18 + 2] = c2 + 3 >> 3;
            output[var18 + 3] = d2 + 3 >> 3;
            diff[0][var13] = a2 + 3 >> 3;
            diff[1][var13] = b2 + 3 >> 3;
            diff[2][var13] = c2 + 3 >> 3;
            diff[3][var13] = d2 + 3 >> 3;
            var18 += 4;
        }
        return output;
    }

    public static int[] encodeWHT(int[] input) {
        int inputOffset = 0;
        int outputOffset = 0;
        int[] output = new int[input.length];
        for (int i = 0; i < 4; i++) {
            int a1 = input[inputOffset + 0] + input[inputOffset + 2] << 2;
            int d1 = input[inputOffset + 1] + input[inputOffset + 3] << 2;
            int c1 = input[inputOffset + 1] - input[inputOffset + 3] << 2;
            int b1 = input[inputOffset + 0] - input[inputOffset + 2] << 2;
            output[outputOffset + 0] = a1 + d1 + (a1 != 0 ? 1 : 0);
            output[outputOffset + 1] = b1 + c1;
            output[outputOffset + 2] = b1 - c1;
            output[outputOffset + 3] = a1 - d1;
            inputOffset += 4;
            outputOffset += 4;
        }
        inputOffset = 0;
        outputOffset = 0;
        for (int var13 = 0; var13 < 4; var13++) {
            int a1 = output[inputOffset + 0] + output[inputOffset + 8];
            int d1 = output[inputOffset + 4] + output[inputOffset + 12];
            int c1 = output[inputOffset + 4] - output[inputOffset + 12];
            int b1 = output[inputOffset + 0] - output[inputOffset + 8];
            int a2 = a1 + d1;
            int b2 = b1 + c1;
            int c2 = b1 - c1;
            int d2 = a1 - d1;
            a2 += a2 < 0 ? 1 : 0;
            b2 += b2 < 0 ? 1 : 0;
            c2 += c2 < 0 ? 1 : 0;
            d2 += d2 < 0 ? 1 : 0;
            output[outputOffset + 0] = a2 + 3 >> 3;
            output[outputOffset + 4] = b2 + 3 >> 3;
            output[outputOffset + 8] = c2 + 3 >> 3;
            output[outputOffset + 12] = d2 + 3 >> 3;
            inputOffset++;
            outputOffset++;
        }
        return output;
    }
}