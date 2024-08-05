package icyllis.modernui.util;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public final class FP16 {

    public static final int SIZE = 16;

    public static final int BYTES = 2;

    public static final short EPSILON = 5120;

    public static final int MAX_EXPONENT = 15;

    public static final int MIN_EXPONENT = -14;

    public static final short LOWEST_VALUE = -1025;

    public static final short MAX_VALUE = 31743;

    public static final short MIN_NORMAL = 1024;

    public static final short MIN_VALUE = 1;

    public static final short NaN = 32256;

    public static final short NEGATIVE_INFINITY = -1024;

    public static final short NEGATIVE_ZERO = -32768;

    public static final short POSITIVE_INFINITY = 31744;

    public static final short POSITIVE_ZERO = 0;

    public static final int SIGN_SHIFT = 15;

    public static final int EXPONENT_SHIFT = 10;

    public static final int SIGN_MASK = 32768;

    public static final int SHIFTED_EXPONENT_MASK = 31;

    public static final int SIGNIFICAND_MASK = 1023;

    public static final int EXPONENT_SIGNIFICAND_MASK = 32767;

    public static final int EXPONENT_BIAS = 15;

    private static final int FP32_SIGN_SHIFT = 31;

    private static final int FP32_EXPONENT_SHIFT = 23;

    private static final int FP32_SHIFTED_EXPONENT_MASK = 255;

    private static final int FP32_SIGNIFICAND_MASK = 8388607;

    private static final int FP32_EXPONENT_BIAS = 127;

    private static final int FP32_QNAN_MASK = 4194304;

    private static final int FP32_DENORMAL_MAGIC = 1056964608;

    private static final float FP32_DENORMAL_FLOAT = Float.intBitsToFloat(1056964608);

    private FP16() {
    }

    public static int compare(short x, short y) {
        if (less(x, y)) {
            return -1;
        } else if (greater(x, y)) {
            return 1;
        } else {
            short xBits = isNaN(x) ? 32256 : x;
            short yBits = isNaN(y) ? 32256 : y;
            return Short.compare(xBits, yBits);
        }
    }

    public static short rint(short h) {
        int bits = h & '\uffff';
        int abs = bits & 32767;
        int result = bits;
        if (abs < 15360) {
            result = bits & 32768;
            if (abs > 14336) {
                result |= 15360;
            }
        } else if (abs < 25600) {
            int exp = 25 - (abs >> 10);
            int mask = (1 << exp) - 1;
            result = bits + ((1 << exp - 1) - (~(abs >> exp) & 1));
            result &= ~mask;
        }
        if (isNaN((short) result)) {
            result |= 32256;
        }
        return (short) result;
    }

    public static short ceil(short h) {
        int bits = h & '\uffff';
        int abs = bits & 32767;
        int result = bits;
        if (abs < 15360) {
            result = bits & 32768;
            result |= 15360 & -(~(bits >> 15) & (abs != 0 ? 1 : 0));
        } else if (abs < 25600) {
            abs = 25 - (abs >> 10);
            int mask = (1 << abs) - 1;
            result = bits + (mask & (bits >> 15) - 1);
            result &= ~mask;
        }
        if (isNaN((short) result)) {
            result |= 32256;
        }
        return (short) result;
    }

    public static short floor(short h) {
        int bits = h & '\uffff';
        int abs = bits & 32767;
        int result = bits;
        if (abs < 15360) {
            result = bits & 32768;
            result |= 15360 & (bits > 32768 ? '\uffff' : 0);
        } else if (abs < 25600) {
            abs = 25 - (abs >> 10);
            int mask = (1 << abs) - 1;
            result = bits + (mask & -(bits >> 15));
            result &= ~mask;
        }
        if (isNaN((short) result)) {
            result |= 32256;
        }
        return (short) result;
    }

    public static short trunc(short h) {
        int bits = h & '\uffff';
        int abs = bits & 32767;
        int result = bits;
        if (abs < 15360) {
            result = bits & 32768;
        } else if (abs < 25600) {
            abs = 25 - (abs >> 10);
            int mask = (1 << abs) - 1;
            result = bits & ~mask;
        }
        return (short) result;
    }

    public static short min(short x, short y) {
        if (isNaN(x)) {
            return 32256;
        } else if (isNaN(y)) {
            return 32256;
        } else if ((x & 32767) == 0 && (y & 32767) == 0) {
            return (x & '耀') != 0 ? x : y;
        } else {
            return ((x & '耀') != 0 ? 32768 - (x & '\uffff') : x & '\uffff') < ((y & '耀') != 0 ? 32768 - (y & '\uffff') : y & '\uffff') ? x : y;
        }
    }

    public static short max(short x, short y) {
        if (isNaN(x)) {
            return 32256;
        } else if (isNaN(y)) {
            return 32256;
        } else if ((x & 32767) == 0 && (y & 32767) == 0) {
            return (x & '耀') != 0 ? y : x;
        } else {
            return ((x & '耀') != 0 ? 32768 - (x & '\uffff') : x & '\uffff') > ((y & '耀') != 0 ? 32768 - (y & '\uffff') : y & '\uffff') ? x : y;
        }
    }

    public static boolean less(short x, short y) {
        if (isNaN(x)) {
            return false;
        } else {
            return isNaN(y) ? false : ((x & '耀') != 0 ? 32768 - (x & '\uffff') : x & '\uffff') < ((y & '耀') != 0 ? 32768 - (y & '\uffff') : y & '\uffff');
        }
    }

    public static boolean lessEquals(short x, short y) {
        if (isNaN(x)) {
            return false;
        } else {
            return isNaN(y) ? false : ((x & '耀') != 0 ? 32768 - (x & '\uffff') : x & '\uffff') <= ((y & '耀') != 0 ? 32768 - (y & '\uffff') : y & '\uffff');
        }
    }

    public static boolean greater(short x, short y) {
        if (isNaN(x)) {
            return false;
        } else {
            return isNaN(y) ? false : ((x & '耀') != 0 ? 32768 - (x & '\uffff') : x & '\uffff') > ((y & '耀') != 0 ? 32768 - (y & '\uffff') : y & '\uffff');
        }
    }

    public static boolean greaterEquals(short x, short y) {
        if (isNaN(x)) {
            return false;
        } else {
            return isNaN(y) ? false : ((x & '耀') != 0 ? 32768 - (x & '\uffff') : x & '\uffff') >= ((y & '耀') != 0 ? 32768 - (y & '\uffff') : y & '\uffff');
        }
    }

    public static boolean equals(short x, short y) {
        if (isNaN(x)) {
            return false;
        } else {
            return isNaN(y) ? false : x == y || ((x | y) & 32767) == 0;
        }
    }

    public static boolean isInfinite(short h) {
        return (h & 32767) == 31744;
    }

    public static boolean isNaN(short h) {
        return (h & 32767) > 31744;
    }

    public static boolean isNormalized(short h) {
        return (h & 31744) != 0 && (h & 31744) != 31744;
    }

    public static float toFloat(short h) {
        int bits = h & '\uffff';
        int s = bits & 32768;
        int e = bits >>> 10 & 31;
        int m = bits & 1023;
        int outE = 0;
        int outM = 0;
        if (e == 0) {
            if (m != 0) {
                float o = Float.intBitsToFloat(1056964608 + m);
                o -= FP32_DENORMAL_FLOAT;
                return s == 0 ? o : -o;
            }
        } else {
            outM = m << 13;
            if (e == 31) {
                outE = 255;
                if (outM != 0) {
                    outM |= 4194304;
                }
            } else {
                outE = e - 15 + 127;
            }
        }
        int out = s << 16 | outE << 23 | outM;
        return Float.intBitsToFloat(out);
    }

    public static short toHalf(float f) {
        int bits = Float.floatToRawIntBits(f);
        int s = bits >>> 31;
        int e = bits >>> 23 & 0xFF;
        int m = bits & 8388607;
        int outE = 0;
        int outM = 0;
        if (e == 255) {
            outE = 31;
            outM = m != 0 ? 512 : 0;
        } else {
            e = e - 127 + 15;
            if (e >= 31) {
                outE = 31;
            } else if (e <= 0) {
                if (e >= -10) {
                    m |= 8388608;
                    int shift = 14 - e;
                    outM = m >> shift;
                    int lowm = m & (1 << shift) - 1;
                    int hway = 1 << shift - 1;
                    if (lowm + (outM & 1) > hway) {
                        outM++;
                    }
                }
            } else {
                outE = e;
                outM = m >> 13;
                if ((m & 8191) + (outM & 1) > 4096) {
                    outM++;
                }
            }
        }
        return (short) (s << 15 | (outE << 10) + outM);
    }

    public static String toHexString(short h) {
        StringBuilder o = new StringBuilder();
        int bits = h & '\uffff';
        int s = bits >>> 15;
        int e = bits >>> 10 & 31;
        int m = bits & 1023;
        if (e == 31) {
            if (m == 0) {
                if (s != 0) {
                    o.append('-');
                }
                o.append("Infinity");
            } else {
                o.append("NaN");
            }
        } else {
            if (s == 1) {
                o.append('-');
            }
            if (e == 0) {
                if (m == 0) {
                    o.append("0x0.0p0");
                } else {
                    o.append("0x0.");
                    String significand = Integer.toHexString(m);
                    o.append(significand.replaceFirst("0{2,}$", ""));
                    o.append("p-14");
                }
            } else {
                o.append("0x1.");
                String significand = Integer.toHexString(m);
                o.append(significand.replaceFirst("0{2,}$", ""));
                o.append('p');
                o.append(e - 15);
            }
        }
        return o.toString();
    }
}