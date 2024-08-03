package info.journeymap.shaded.org.eclipse.jetty.util.security;

public class UnixCrypt {

    private static final byte[] IP = new byte[] { 58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7 };

    private static final byte[] ExpandTr = new byte[] { 32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1 };

    private static final byte[] PC1 = new byte[] { 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4 };

    private static final byte[] Rotates = new byte[] { 1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1 };

    private static final byte[] PC2 = new byte[] { 9, 18, 14, 17, 11, 24, 1, 5, 22, 25, 3, 28, 15, 6, 21, 10, 35, 38, 23, 19, 12, 4, 26, 8, 43, 54, 16, 7, 27, 20, 13, 2, 0, 0, 41, 52, 31, 37, 47, 55, 0, 0, 30, 40, 51, 45, 33, 48, 0, 0, 44, 49, 39, 56, 34, 53, 0, 0, 46, 42, 50, 36, 29, 32 };

    private static final byte[][] S = new byte[][] { { 14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, 0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8, 4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0, 15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13 }, { 15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, 3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5, 0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15, 13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9 }, { 10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, 13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1, 13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7, 1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12 }, { 7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, 13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9, 10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4, 3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14 }, { 2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, 14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6, 4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14, 11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3 }, { 12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, 10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8, 9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6, 4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13 }, { 4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, 13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6, 1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2, 6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12 }, { 13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, 1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2, 7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8, 2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11 } };

    private static final byte[] P32Tr = new byte[] { 16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25 };

    private static final byte[] CIFP = new byte[] { 1, 2, 3, 4, 17, 18, 19, 20, 5, 6, 7, 8, 21, 22, 23, 24, 9, 10, 11, 12, 25, 26, 27, 28, 13, 14, 15, 16, 29, 30, 31, 32, 33, 34, 35, 36, 49, 50, 51, 52, 37, 38, 39, 40, 53, 54, 55, 56, 41, 42, 43, 44, 57, 58, 59, 60, 45, 46, 47, 48, 61, 62, 63, 64 };

    private static final byte[] ITOA64 = new byte[] { 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };

    private static final byte[] A64TOI = new byte[128];

    private static final long[][] PC1ROT = new long[16][16];

    private static final long[][][] PC2ROT = new long[2][16][16];

    private static final long[][] IE3264 = new long[8][16];

    private static final long[][] SPE = new long[8][64];

    private static final long[][] CF6464 = new long[16][16];

    private UnixCrypt() {
    }

    private static int to_six_bit(int num) {
        return num << 26 & -67108864 | num << 12 & 16515072 | num >> 2 & 64512 | num >> 16 & 252;
    }

    private static long to_six_bit(long num) {
        return num << 26 & -288230371923853312L | num << 12 & 70931694147600384L | num >> 2 & 277076930264064L | num >> 16 & 1082331758844L;
    }

    private static long perm6464(long c, long[][] p) {
        long out = 0L;
        int i = 8;
        while (--i >= 0) {
            int t = (int) (255L & c);
            c >>= 8;
            long tp = p[i << 1][t & 15];
            out |= tp;
            tp = p[(i << 1) + 1][t >> 4];
            out |= tp;
        }
        return out;
    }

    private static long perm3264(int c, long[][] p) {
        long out = 0L;
        int i = 4;
        while (--i >= 0) {
            int t = 0xFF & c;
            c >>= 8;
            long tp = p[i << 1][t & 15];
            out |= tp;
            tp = p[(i << 1) + 1][t >> 4];
            out |= tp;
        }
        return out;
    }

    private static long[] des_setkey(long keyword) {
        long K = perm6464(keyword, PC1ROT);
        long[] KS = new long[16];
        KS[0] = K & -217020518463700993L;
        for (int i = 1; i < 16; i++) {
            KS[i] = K;
            K = perm6464(K, PC2ROT[Rotates[i] - 1]);
            KS[i] = K & -217020518463700993L;
        }
        return KS;
    }

    private static long des_cipher(long in, int salt, int num_iter, long[] KS) {
        salt = to_six_bit(salt);
        long L = in & 6148914691236517205L;
        long R = in & -6148914694099828736L | in >> 1 & 1431655765L;
        L = (L << 1 | L << 32) & -4294967296L | (R | R >> 32) & 4294967295L;
        L = perm3264((int) (L >> 32), IE3264);
        R = perm3264((int) (L & -1L), IE3264);
        while (--num_iter >= 0) {
            for (int loop_count = 0; loop_count < 8; loop_count++) {
                long kp = KS[loop_count << 1];
                long k = (R >> 32 ^ R) & (long) salt & 4294967295L;
                k |= k << 32;
                long B = k ^ R ^ kp;
                L ^= SPE[0][(int) (B >> 58 & 63L)] ^ SPE[1][(int) (B >> 50 & 63L)] ^ SPE[2][(int) (B >> 42 & 63L)] ^ SPE[3][(int) (B >> 34 & 63L)] ^ SPE[4][(int) (B >> 26 & 63L)] ^ SPE[5][(int) (B >> 18 & 63L)] ^ SPE[6][(int) (B >> 10 & 63L)] ^ SPE[7][(int) (B >> 2 & 63L)];
                kp = KS[(loop_count << 1) + 1];
                k = (L >> 32 ^ L) & (long) salt & 4294967295L;
                k |= k << 32;
                B = k ^ L ^ kp;
                R ^= SPE[0][(int) (B >> 58 & 63L)] ^ SPE[1][(int) (B >> 50 & 63L)] ^ SPE[2][(int) (B >> 42 & 63L)] ^ SPE[3][(int) (B >> 34 & 63L)] ^ SPE[4][(int) (B >> 26 & 63L)] ^ SPE[5][(int) (B >> 18 & 63L)] ^ SPE[6][(int) (B >> 10 & 63L)] ^ SPE[7][(int) (B >> 2 & 63L)];
            }
            L ^= R;
            R ^= L;
            L ^= R;
        }
        L = (L >> 35 & 252645135L | (L & -1L) << 1 & 4042322160L) << 32 | R >> 35 & 252645135L | (R & -1L) << 1 & 4042322160L;
        return perm6464(L, CF6464);
    }

    private static void init_perm(long[][] perm, byte[] p, int chars_out) {
        for (int k = 0; k < chars_out * 8; k++) {
            int l = p[k] - 1;
            if (l >= 0) {
                int i = l >> 2;
                l = 1 << (l & 3);
                for (int j = 0; j < 16; j++) {
                    int s = (k & 7) + (7 - (k >> 3) << 3);
                    if ((j & l) != 0) {
                        perm[i][j] = perm[i][j] | 1L << s;
                    }
                }
            }
        }
    }

    public static String crypt(String key, String setting) {
        long constdatablock = 0L;
        byte[] cryptresult = new byte[13];
        long keyword = 0L;
        if (key != null && setting != null) {
            int keylen = key.length();
            for (int i = 0; i < 8; i++) {
                keyword = keyword << 8 | (long) (i < keylen ? 2 * key.charAt(i) : 0);
            }
            long[] KS = des_setkey(keyword);
            int salt = 0;
            int i = 2;
            while (--i >= 0) {
                char c = i < setting.length() ? setting.charAt(i) : 46;
                cryptresult[i] = (byte) c;
                salt = salt << 6 | 255 & A64TOI[c];
            }
            long rsltblock = des_cipher(constdatablock, salt, 25, KS);
            cryptresult[12] = ITOA64[(int) rsltblock << 2 & 63];
            long var15 = rsltblock >> 4;
            for (int ix = 12; --ix >= 2; var15 >>= 6) {
                cryptresult[ix] = ITOA64[(int) var15 & 63];
            }
            return new String(cryptresult, 0, 13);
        } else {
            return "*";
        }
    }

    public static void main(String[] arg) {
        if (arg.length != 2) {
            System.err.println("Usage - java org.eclipse.util.UnixCrypt <key> <salt>");
            System.exit(1);
        }
        System.err.println("Crypt=" + crypt(arg[0], arg[1]));
    }

    static {
        byte[] perm = new byte[64];
        byte[] temp = new byte[64];
        for (int i = 0; i < 64; i++) {
            A64TOI[ITOA64[i]] = (byte) i;
        }
        for (int i = 0; i < 64; i++) {
            perm[i] = 0;
        }
        for (int i = 0; i < 64; i++) {
            int k;
            if ((k = PC2[i]) != 0) {
                k += Rotates[0] - 1;
                if (k % 28 < Rotates[0]) {
                    k -= 28;
                }
                k = PC1[k];
                if (k > 0) {
                    k--;
                    k = (k | 7) - (k & 7);
                    k++;
                }
                perm[i] = (byte) k;
            }
        }
        init_perm(PC1ROT, perm, 8);
        for (int j = 0; j < 2; j++) {
            for (int ix = 0; ix < 64; ix++) {
                perm[ix] = temp[ix] = 0;
            }
            for (int ix = 0; ix < 64; ix++) {
                int k;
                if ((k = PC2[ix]) != 0) {
                    temp[k - 1] = (byte) (ix + 1);
                }
            }
            for (int ixx = 0; ixx < 64; ixx++) {
                int k;
                if ((k = PC2[ixx]) != 0) {
                    k += j;
                    if (k % 28 <= j) {
                        k -= 28;
                    }
                    perm[ixx] = temp[k];
                }
            }
            init_perm(PC2ROT[j], perm, 8);
        }
        for (int ixxx = 0; ixxx < 8; ixxx++) {
            for (int j = 0; j < 8; j++) {
                int k = j < 2 ? 0 : IP[ExpandTr[ixxx * 6 + j - 2] - 1];
                if (k > 32) {
                    k -= 32;
                } else if (k > 0) {
                    k--;
                }
                if (k > 0) {
                    k--;
                    k = (k | 7) - (k & 7);
                    k++;
                }
                perm[ixxx * 8 + j] = (byte) k;
            }
        }
        init_perm(IE3264, perm, 8);
        for (int ixxx = 0; ixxx < 64; ixxx++) {
            int kx = IP[CIFP[ixxx] - 1];
            if (kx > 0) {
                kx--;
                kx = (kx | 7) - (kx & 7);
                kx++;
            }
            perm[kx - 1] = (byte) (ixxx + 1);
        }
        init_perm(CF6464, perm, 8);
        for (int ixxx = 0; ixxx < 48; ixxx++) {
            perm[ixxx] = P32Tr[ExpandTr[ixxx] - 1];
        }
        for (int t = 0; t < 8; t++) {
            for (int j = 0; j < 64; j++) {
                int kx = (j >> 0 & 1) << 5 | (j >> 1 & 1) << 3 | (j >> 2 & 1) << 2 | (j >> 3 & 1) << 1 | (j >> 4 & 1) << 0 | (j >> 5 & 1) << 4;
                int var33 = S[t][kx];
                var33 = (var33 >> 3 & 1) << 0 | (var33 >> 2 & 1) << 1 | (var33 >> 1 & 1) << 2 | (var33 >> 0 & 1) << 3;
                for (int ixxx = 0; ixxx < 32; ixxx++) {
                    temp[ixxx] = 0;
                }
                for (int ixxx = 0; ixxx < 4; ixxx++) {
                    temp[4 * t + ixxx] = (byte) (var33 >> ixxx & 1);
                }
                long kk = 0L;
                int ixxx = 24;
                while (--ixxx >= 0) {
                    kk = kk << 1 | (long) temp[perm[ixxx] - 1] << 32 | (long) temp[perm[ixxx + 24] - 1];
                }
                SPE[t][j] = to_six_bit(kk);
            }
        }
    }
}