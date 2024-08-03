package com.github.alexthe666.citadel.repack.jcodec.common.tools;

public class MathUtil {

    private static final int[] logTab = new int[] { 0, 0, 1, 1, 2, 2, 2, 2, 3, 3, 3, 3, 3, 3, 3, 3, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };

    private static final int[] reverseTab = new int[] { 0, 128, 64, 192, 32, 160, 96, 224, 16, 144, 80, 208, 48, 176, 112, 240, 8, 136, 72, 200, 40, 168, 104, 232, 24, 152, 88, 216, 56, 184, 120, 248, 4, 132, 68, 196, 36, 164, 100, 228, 20, 148, 84, 212, 52, 180, 116, 244, 12, 140, 76, 204, 44, 172, 108, 236, 28, 156, 92, 220, 60, 188, 124, 252, 2, 130, 66, 194, 34, 162, 98, 226, 18, 146, 82, 210, 50, 178, 114, 242, 10, 138, 74, 202, 42, 170, 106, 234, 26, 154, 90, 218, 58, 186, 122, 250, 6, 134, 70, 198, 38, 166, 102, 230, 22, 150, 86, 214, 54, 182, 118, 246, 14, 142, 78, 206, 46, 174, 110, 238, 30, 158, 94, 222, 62, 190, 126, 254, 1, 129, 65, 193, 33, 161, 97, 225, 17, 145, 81, 209, 49, 177, 113, 241, 9, 137, 73, 201, 41, 169, 105, 233, 25, 153, 89, 217, 57, 185, 121, 249, 5, 133, 69, 197, 37, 165, 101, 229, 21, 149, 85, 213, 53, 181, 117, 245, 13, 141, 77, 205, 45, 173, 109, 237, 29, 157, 93, 221, 61, 189, 125, 253, 3, 131, 67, 195, 35, 163, 99, 227, 19, 147, 83, 211, 51, 179, 115, 243, 11, 139, 75, 203, 43, 171, 107, 235, 27, 155, 91, 219, 59, 187, 123, 251, 7, 135, 71, 199, 39, 167, 103, 231, 23, 151, 87, 215, 55, 183, 119, 247, 15, 143, 79, 207, 47, 175, 111, 239, 31, 159, 95, 223, 63, 191, 127, 255 };

    public static int log2(int v) {
        int n = 0;
        if ((v & -65536) != 0) {
            v >>= 16;
            n += 16;
        }
        if ((v & 0xFF00) != 0) {
            v >>= 8;
            n += 8;
        }
        return n + logTab[v];
    }

    public static int log2l(long v) {
        int n = 0;
        if ((v & -4294967296L) != 0L) {
            v >>= 32;
            n += 32;
        }
        if ((v & 4294901760L) != 0L) {
            v >>= 16;
            n += 16;
        }
        if ((v & 65280L) != 0L) {
            v >>= 8;
            n += 8;
        }
        return n + logTab[(int) v];
    }

    public static int log2Slow(int val) {
        int i;
        for (i = 0; (val & -2147483648) == 0; i++) {
            val <<= 1;
        }
        return 31 - i;
    }

    public static int gcd(int a, int b) {
        return b != 0 ? gcd(b, a % b) : a;
    }

    public static long gcdLong(long a, long b) {
        return b != 0L ? gcdLong(b, a % b) : a;
    }

    public static final int clip(int val, int from, int to) {
        return val < from ? from : (val > to ? to : val);
    }

    public static final int clipMax(int val, int max) {
        return val < max ? val : max;
    }

    public static int cubeRoot(int n) {
        return 0;
    }

    public static final int reverse(int b) {
        return reverseTab[b & 0xFF];
    }

    public static int nextPowerOfTwo(int n) {
        n = --n | n >> 1;
        n |= n >> 2;
        n |= n >> 4;
        n |= n >> 8;
        n |= n >> 16;
        return n + 1;
    }

    public static final int abs(int val) {
        int sign = val >> 31;
        return (val ^ sign) - sign;
    }

    public static final int golomb(int signedLevel) {
        return signedLevel == 0 ? 0 : (abs(signedLevel) << 1) - (~signedLevel >>> 31);
    }

    public static final int toSigned(int val, int sign) {
        return (val ^ sign) - sign;
    }

    public static final int sign(int val) {
        return -(val >> 31);
    }

    public static int wrap(int picNo, int maxFrames) {
        return picNo < 0 ? picNo + maxFrames : (picNo >= maxFrames ? picNo - maxFrames : picNo);
    }

    public static int max3(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }
}