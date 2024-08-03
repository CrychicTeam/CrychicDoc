package icyllis.modernui.util;

import icyllis.modernui.annotation.NonNull;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public final class Half extends Number implements Comparable<Half> {

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

    private static final MethodHandle FLOAT16_TO_FLOAT = getFloat16ToFloat();

    private static final MethodHandle FLOAT_TO_FLOAT16 = getFloatToFloat16();

    private final short value;

    private static MethodHandle getFloat16ToFloat() {
        try {
            return MethodHandles.lookup().findStatic(Float.class, "float16ToFloat", MethodType.methodType(float.class, short.class));
        } catch (IllegalAccessException | NoSuchMethodException var1) {
            return null;
        }
    }

    private static MethodHandle getFloatToFloat16() {
        try {
            return MethodHandles.lookup().findStatic(Float.class, "floatToFloat16", MethodType.methodType(short.class, float.class));
        } catch (IllegalAccessException | NoSuchMethodException var1) {
            return null;
        }
    }

    public Half(short value) {
        this.value = value;
    }

    public Half(float value) {
        this.value = toHalf(value);
    }

    public Half(double value) {
        this.value = toHalf((float) value);
    }

    public Half(@NonNull String value) throws NumberFormatException {
        this.value = toHalf(Float.parseFloat(value));
    }

    public short halfValue() {
        return this.value;
    }

    public byte byteValue() {
        return (byte) ((int) toFloat(this.value));
    }

    public short shortValue() {
        return (short) ((int) toFloat(this.value));
    }

    public int intValue() {
        return (int) toFloat(this.value);
    }

    public long longValue() {
        return (long) toFloat(this.value);
    }

    public float floatValue() {
        return toFloat(this.value);
    }

    public double doubleValue() {
        return (double) toFloat(this.value);
    }

    public boolean isNaN() {
        return isNaN(this.value);
    }

    public boolean equals(Object o) {
        return o instanceof Half && halfToIntBits(((Half) o).value) == halfToIntBits(this.value);
    }

    public int hashCode() {
        return hashCode(this.value);
    }

    @NonNull
    public String toString() {
        return toString(this.value);
    }

    public int compareTo(@NonNull Half h) {
        return compare(this.value, h.value);
    }

    public static int hashCode(short h) {
        return halfToIntBits(h);
    }

    public static int compare(short x, short y) {
        return FP16.compare(x, y);
    }

    public static short halfToShortBits(short h) {
        return (h & 32767) > 31744 ? 32256 : h;
    }

    public static int halfToIntBits(short h) {
        return (h & 32767) > 31744 ? 32256 : h & 65535;
    }

    public static int halfToRawIntBits(short h) {
        return h & 65535;
    }

    public static short intBitsToHalf(int bits) {
        return (short) (bits & 65535);
    }

    public static short copySign(short magnitude, short sign) {
        return (short) (sign & '耀' | magnitude & 32767);
    }

    public static short abs(short h) {
        return (short) (h & 32767);
    }

    public static short round(short h) {
        return FP16.rint(h);
    }

    public static short ceil(short h) {
        return FP16.ceil(h);
    }

    public static short floor(short h) {
        return FP16.floor(h);
    }

    public static short trunc(short h) {
        return FP16.trunc(h);
    }

    public static short min(short x, short y) {
        return FP16.min(x, y);
    }

    public static short max(short x, short y) {
        return FP16.max(x, y);
    }

    public static boolean less(short x, short y) {
        return FP16.less(x, y);
    }

    public static boolean lessEquals(short x, short y) {
        return FP16.lessEquals(x, y);
    }

    public static boolean greater(short x, short y) {
        return FP16.greater(x, y);
    }

    public static boolean greaterEquals(short x, short y) {
        return FP16.greaterEquals(x, y);
    }

    public static boolean equals(short x, short y) {
        return FP16.equals(x, y);
    }

    public static int getSign(short h) {
        return (h & 32768) == 0 ? 1 : -1;
    }

    public static int getExponent(short h) {
        return (h >>> 10 & 31) - 15;
    }

    public static int getSignificand(short h) {
        return h & 1023;
    }

    public static boolean isInfinite(short h) {
        return FP16.isInfinite(h);
    }

    public static boolean isNaN(short h) {
        return FP16.isNaN(h);
    }

    public static boolean isNormalized(short h) {
        return FP16.isNormalized(h);
    }

    public static float toFloat(short h) {
        if (FLOAT16_TO_FLOAT != null) {
            try {
                return (float) FLOAT16_TO_FLOAT.invokeExact(h);
            } catch (Throwable var2) {
                throw new RuntimeException(var2);
            }
        } else {
            return FP16.toFloat(h);
        }
    }

    public static short toHalf(float f) {
        if (FLOAT_TO_FLOAT16 != null) {
            try {
                return (short) FLOAT_TO_FLOAT16.invokeExact(f);
            } catch (Throwable var2) {
                throw new RuntimeException(var2);
            }
        } else {
            return FP16.toHalf(f);
        }
    }

    @NonNull
    public static Half valueOf(short h) {
        return new Half(h);
    }

    @NonNull
    public static Half valueOf(float f) {
        return new Half(f);
    }

    @NonNull
    public static Half valueOf(@NonNull String s) {
        return new Half(s);
    }

    public static short parseHalf(@NonNull String s) throws NumberFormatException {
        return toHalf(Float.parseFloat(s));
    }

    @NonNull
    public static String toString(short h) {
        return Float.toString(toFloat(h));
    }

    @NonNull
    public static String toHexString(short h) {
        return FP16.toHexString(h);
    }
}