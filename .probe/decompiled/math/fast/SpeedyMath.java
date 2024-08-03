package math.fast;

public final class SpeedyMath {

    private SpeedyMath() {
        throw new AssertionError();
    }

    public static double cos(double a) {
        return JafamaFastMath.cos(a);
    }

    public static double asin(double a) {
        return JafamaFastMath.asin(a);
    }

    public static double acos(double a) {
        return JafamaFastMath.acos(a);
    }

    public static double atan(double a) {
        return JafamaFastMath.atan(a);
    }

    public static double atan2(double y, double x) {
        return JafamaFastMath.atan2(y, x);
    }

    public static double sinh(double x) {
        return CommonsAccurateMath.sinh(x);
    }

    public static double cosh(double x) {
        return CommonsAccurateMath.cosh(x);
    }

    public static double tanh(double x) {
        return CommonsAccurateMath.tanh(x);
    }

    public static double hypot(double x, double y) {
        return JafamaFastMath.hypot(x, y);
    }

    public static double expm1(double x) {
        return CommonsAccurateMath.expm1(x);
    }
}