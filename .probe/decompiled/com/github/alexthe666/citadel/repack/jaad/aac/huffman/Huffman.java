package com.github.alexthe666.citadel.repack.jaad.aac.huffman;

import com.github.alexthe666.citadel.repack.jaad.aac.AACException;
import com.github.alexthe666.citadel.repack.jaad.aac.syntax.BitStream;

public class Huffman implements Codebooks {

    private static final boolean[] UNSIGNED = new boolean[] { false, false, true, true, false, false, true, true, true, true, true };

    private static final int QUAD_LEN = 4;

    private static final int PAIR_LEN = 2;

    private Huffman() {
    }

    private static int findOffset(BitStream in, int[][] table) throws AACException {
        int off = 0;
        int len = table[off][0];
        int cw = in.readBits(len);
        while (cw != table[off][1]) {
            off++;
            int j = table[off][0] - len;
            len = table[off][0];
            cw <<= j;
            cw |= in.readBits(j);
        }
        return off;
    }

    private static void signValues(BitStream in, int[] data, int off, int len) throws AACException {
        for (int i = off; i < off + len; i++) {
            if (data[i] != 0 && in.readBool()) {
                data[i] = -data[i];
            }
        }
    }

    private static int getEscape(BitStream in, int s) throws AACException {
        boolean neg = s < 0;
        int i = 4;
        while (in.readBool()) {
            i++;
        }
        int j = in.readBits(i) | 1 << i;
        return neg ? -j : j;
    }

    public static int decodeScaleFactor(BitStream in) throws AACException {
        int offset = findOffset(in, HCB_SF);
        return HCB_SF[offset][2];
    }

    public static void decodeSpectralData(BitStream in, int cb, int[] data, int off) throws AACException {
        int[][] HCB = CODEBOOKS[cb - 1];
        int offset = findOffset(in, HCB);
        data[off] = HCB[offset][2];
        data[off + 1] = HCB[offset][3];
        if (cb < 5) {
            data[off + 2] = HCB[offset][4];
            data[off + 3] = HCB[offset][5];
        }
        if (cb < 11) {
            if (UNSIGNED[cb - 1]) {
                signValues(in, data, off, cb < 5 ? 4 : 2);
            }
        } else {
            if (cb != 11 && cb <= 15) {
                throw new AACException("Huffman: unknown spectral codebook: " + cb);
            }
            signValues(in, data, off, cb < 5 ? 4 : 2);
            if (Math.abs(data[off]) == 16) {
                data[off] = getEscape(in, data[off]);
            }
            if (Math.abs(data[off + 1]) == 16) {
                data[off + 1] = getEscape(in, data[off + 1]);
            }
        }
    }
}