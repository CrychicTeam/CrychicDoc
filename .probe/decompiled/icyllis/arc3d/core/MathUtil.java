package icyllis.arc3d.core;

public class MathUtil {

    private static final boolean USE_SIN_TABLE = false;

    private static final float[] SIN_TABLE = null;

    public static final float PI = (float) Math.PI;

    public static final float PI2 = (float) (Math.PI * 2);

    public static final float PI3 = (float) (Math.PI * 3);

    public static final float PI4 = (float) (Math.PI * 4);

    public static final float PI_O_2 = (float) (Math.PI / 2);

    public static final float PI_O_3 = (float) (Math.PI / 3);

    public static final float PI_O_4 = (float) (Math.PI / 4);

    public static final float PI_O_6 = (float) (Math.PI / 6);

    public static final float EPS = 1.0E-5F;

    public static final float INV_EPS = 100000.0F;

    public static final float DEG_TO_RAD = (float) (Math.PI / 180.0);

    public static final float RAD_TO_DEG = (float) (180.0 / Math.PI);

    public static final float SQRT2 = 1.4142135F;

    public static final float INV_SQRT2 = 0.70710677F;

    private static float fsin(float a) {
        return SIN_TABLE[Math.round(a * 10430.378F) & 65535];
    }

    private static float fcos(float a) {
        return SIN_TABLE[Math.round(a * 10430.378F) + 16384 & 65535];
    }

    public static float sin(float a) {
        return (float) Math.sin((double) a);
    }

    public static float cos(float a) {
        return (float) Math.cos((double) a);
    }

    public static float tan(float a) {
        return (float) Math.tan((double) a);
    }

    public static boolean isApproxZero(float a) {
        return Math.abs(a) <= 1.0E-5F;
    }

    public static boolean isApproxZero(float a, float b) {
        return Math.abs(a) <= 1.0E-5F && Math.abs(b) <= 1.0E-5F;
    }

    public static boolean isApproxZero(float a, float b, float c) {
        return Math.abs(a) <= 1.0E-5F && Math.abs(b) <= 1.0E-5F && Math.abs(c) <= 1.0E-5F;
    }

    public static boolean isApproxZero(float a, float b, float c, float d) {
        return Math.abs(a) <= 1.0E-5F && Math.abs(b) <= 1.0E-5F && Math.abs(c) <= 1.0E-5F && Math.abs(d) <= 1.0E-5F;
    }

    public static boolean isApproxEqual(float a, float b) {
        return Math.abs(b - a) <= 1.0E-5F;
    }

    public static boolean isApproxEqual(float a, float b, float c) {
        return Math.abs(b - a) <= 1.0E-5F && Math.abs(c - a) <= 1.0E-5F;
    }

    public static boolean isApproxEqual(float a, float b, float c, float d) {
        return Math.abs(b - a) <= 1.0E-5F && Math.abs(c - a) <= 1.0E-5F && Math.abs(d - a) <= 1.0E-5F;
    }

    public static boolean isApproxEqual(float a, float b, float c, float d, float e) {
        return Math.abs(b - a) <= 1.0E-5F && Math.abs(c - a) <= 1.0E-5F && Math.abs(d - a) <= 1.0E-5F && Math.abs(e - a) <= 1.0E-5F;
    }

    public static float sqrt(float f) {
        return (float) Math.sqrt((double) f);
    }

    public static float asin(float a) {
        return (float) Math.asin((double) a);
    }

    public static float acos(float a) {
        return (float) Math.acos((double) a);
    }

    public static float atan2(float a, float b) {
        return (float) Math.atan2((double) a, (double) b);
    }

    public static int clamp(int x, int min, int max) {
        return Math.max(min, Math.min(x, max));
    }

    public static long clamp(long x, long min, long max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float clamp(float x, float min, float max) {
        return Math.max(min, Math.min(x, max));
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(min, Math.min(x, max));
    }

    public static float pin(float x, float min, float max) {
        float y = max < x ? max : x;
        return min < y ? y : min;
    }

    public static double pin(double x, double min, double max) {
        double y = max < x ? max : x;
        return min < y ? y : min;
    }

    public static float min(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

    public static float min3(float[] v) {
        return Math.min(Math.min(v[0], v[1]), v[2]);
    }

    public static double min(double a, double b, double c) {
        return Math.min(Math.min(a, b), c);
    }

    public static float min(float a, float b, float c, float d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    public static double min(double a, double b, double c, double d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    public static float max(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }

    public static float max3(float[] v) {
        return Math.max(Math.max(v[0], v[1]), v[2]);
    }

    public static double max(double a, double b, double c) {
        return Math.max(Math.max(a, b), c);
    }

    public static float max(float a, float b, float c, float d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    public static double max(double a, double b, double c, double d) {
        return Math.max(Math.max(a, b), Math.max(c, d));
    }

    public static float median(float a, float b, float c) {
        return clamp(c, Math.min(a, b), Math.max(a, b));
    }

    public static double median(double a, double b, double c) {
        return clamp(c, Math.min(a, b), Math.max(a, b));
    }

    public static float lerp(float a, float b, float t) {
        return (b - a) * t + a;
    }

    public static double lerp(double a, double b, double t) {
        return (b - a) * t + a;
    }

    public static float mix(float a, float b, float t) {
        return a * (1.0F - t) + b * t;
    }

    public static double mix(double a, double b, double t) {
        return a * (1.0 - t) + b * t;
    }

    public static float biLerp(float q00, float q10, float q01, float q11, float tx, float ty) {
        return lerp(lerp(q00, q10, tx), lerp(q01, q11, tx), ty);
    }

    public static double biLerp(double q00, double q10, double q01, double q11, double tx, double ty) {
        return lerp(lerp(q00, q10, tx), lerp(q01, q11, tx), ty);
    }

    public static float triLerp(float c000, float c100, float c010, float c110, float c001, float c101, float c011, float c111, float tx, float ty, float tz) {
        return lerp(biLerp(c000, c100, c010, c110, tx, ty), biLerp(c001, c101, c011, c111, tx, ty), tz);
    }

    public static double triLerp(double c000, double c100, double c010, double c110, double c001, double c101, double c011, double c111, double tx, double ty, double tz) {
        return lerp(biLerp(c000, c100, c010, c110, tx, ty), biLerp(c001, c101, c011, c111, tx, ty), tz);
    }

    public static int align2(int a) {
        assert a >= 0 && a <= 2147483639;
        return a + 1 & -2;
    }

    public static int align4(int a) {
        assert a >= 0 && a <= 2147483639;
        return a + 3 & -4;
    }

    public static int align8(int a) {
        assert a >= 0 && a <= 2147483639;
        return a + 7 & -8;
    }

    public static long align2(long a) {
        assert a >= 0L && a <= 9223372036854775791L;
        return a + 1L & -2L;
    }

    public static long align4(long a) {
        assert a >= 0L && a <= 9223372036854775791L;
        return a + 3L & -4L;
    }

    public static long align8(long a) {
        assert a >= 0L && a <= 9223372036854775791L;
        return a + 7L & -8L;
    }

    public static boolean isAlign2(int a) {
        assert a >= 0;
        return (a & 1) == 0;
    }

    public static boolean isAlign4(int a) {
        assert a >= 0;
        return (a & 3) == 0;
    }

    public static boolean isAlign8(int a) {
        assert a >= 0;
        return (a & 7) == 0;
    }

    public static boolean isAlign2(long a) {
        assert a >= 0L;
        return (a & 1L) == 0L;
    }

    public static boolean isAlign4(long a) {
        assert a >= 0L;
        return (a & 3L) == 0L;
    }

    public static boolean isAlign8(long a) {
        assert a >= 0L;
        return (a & 7L) == 0L;
    }

    public static int alignTo(int a, int alignment) {
        assert alignment > 0 && (alignment & alignment - 1) == 0;
        return a + alignment - 1 & -alignment;
    }

    public static int alignUp(int a, int alignment) {
        assert alignment > 0;
        int r = a % alignment;
        return r == 0 ? a : a + alignment - r;
    }

    public static int alignUpPad(int a, int alignment) {
        assert alignment > 0;
        return (alignment - a % alignment) % alignment;
    }

    public static int alignDown(int a, int alignment) {
        assert alignment > 0;
        return a / alignment * alignment;
    }

    public static boolean isPow2(int a) {
        assert a > 0 : "undefined";
        return (a & a - 1) == 0;
    }

    public static boolean isPow2(long a) {
        assert a > 0L : "undefined";
        return (a & a - 1L) == 0L;
    }

    public static int ceilLog2(int a) {
        assert a > 0 : "undefined";
        return 32 - Integer.numberOfLeadingZeros(a - 1);
    }

    public static int ceilLog2(long a) {
        assert a > 0L : "undefined";
        return 64 - Long.numberOfLeadingZeros(a - 1L);
    }

    public static int ceilPow2(int a) {
        assert a > 0 && a <= 1073741824 : "undefined";
        return 1 << -Integer.numberOfLeadingZeros(a - 1);
    }

    public static long ceilPow2(long a) {
        assert a > 0L && a <= 4611686018427387904L : "undefined";
        return 1L << -Long.numberOfLeadingZeros(a - 1L);
    }

    public static int floorLog2(int a) {
        assert a > 0 : "undefined";
        return 31 - Integer.numberOfLeadingZeros(a);
    }

    public static int floorLog2(long a) {
        assert a > 0L : "undefined";
        return 63 - Long.numberOfLeadingZeros(a);
    }

    public static int floorPow2(int a) {
        assert a > 0 : "undefined";
        return Integer.highestOneBit(a);
    }

    public static long floorPow2(long a) {
        assert a > 0L : "undefined";
        return Long.highestOneBit(a);
    }

    public static int ceilLog2(float v) {
        int exp = (Float.floatToRawIntBits(v) + 8388608 - 1 >> 23) - 127;
        return exp & ~(exp >> 31);
    }

    public static int ceilLog4(float v) {
        return ceilLog2(v) + 1 >> 1;
    }

    public static int ceilLog16(float v) {
        return ceilLog2(v) + 3 >> 2;
    }

    protected MathUtil() {
        throw new UnsupportedOperationException();
    }
}