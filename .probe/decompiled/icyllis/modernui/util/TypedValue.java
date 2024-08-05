package icyllis.modernui.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jetbrains.annotations.ApiStatus.Internal;

public class TypedValue {

    public static final int TYPE_NULL = 0;

    public static final int TYPE_REFERENCE = 1;

    public static final int TYPE_ATTRIBUTE = 2;

    public static final int TYPE_STRING = 3;

    public static final int TYPE_FLOAT = 4;

    public static final int TYPE_DIMENSION = 5;

    public static final int TYPE_FRACTION = 6;

    public static final int COMPLEX_UNIT_SHIFT = 0;

    public static final int COMPLEX_UNIT_MASK = 15;

    public static final int COMPLEX_UNIT_PX = 0;

    public static final int COMPLEX_UNIT_DP = 1;

    public static final int COMPLEX_UNIT_SP = 2;

    public static final int COMPLEX_UNIT_PT = 3;

    public static final int COMPLEX_UNIT_IN = 4;

    public static final int COMPLEX_UNIT_MM = 5;

    public static final int COMPLEX_UNIT_FRACTION = 0;

    public static final int COMPLEX_UNIT_FRACTION_PARENT = 1;

    public static final int COMPLEX_RADIX_SHIFT = 4;

    public static final int COMPLEX_RADIX_MASK = 3;

    public static final int COMPLEX_RADIX_23p0 = 0;

    public static final int COMPLEX_RADIX_16p7 = 1;

    public static final int COMPLEX_RADIX_8p15 = 2;

    public static final int COMPLEX_RADIX_0p23 = 3;

    public static final int COMPLEX_MANTISSA_SHIFT = 8;

    public static final int COMPLEX_MANTISSA_MASK = 16777215;

    private static final float MANTISSA_MULT = 0.00390625F;

    private static final float[] RADIX_MULTS = new float[] { 0.00390625F, 3.0517578E-5F, 1.1920929E-7F, 4.656613E-10F };

    public static float complexToFloat(int complex) {
        return (float) (complex & -256) * RADIX_MULTS[complex >> 4 & 3];
    }

    public static float complexToDimension(int data, DisplayMetrics metrics) {
        return applyDimension(data >> 0 & 15, complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelOffset(int data, DisplayMetrics metrics) {
        return (int) applyDimension(data >> 0 & 15, complexToFloat(data), metrics);
    }

    public static int complexToDimensionPixelSize(int data, DisplayMetrics metrics) {
        float value = complexToFloat(data);
        float f = applyDimension(data >> 0 & 15, value, metrics);
        int res = (int) (f >= 0.0F ? f + 0.5F : f - 0.5F);
        if (res != 0) {
            return res;
        } else if (value == 0.0F) {
            return 0;
        } else {
            return value > 0.0F ? 1 : -1;
        }
    }

    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        return switch(unit) {
            case 0 ->
                value;
            case 1 ->
                value * metrics.density;
            case 2 ->
                value * metrics.scaledDensity;
            case 3 ->
                value * metrics.xdpi * 0.013888889F;
            case 4 ->
                value * metrics.xdpi;
            case 5 ->
                value * metrics.xdpi * 0.03937008F;
            default ->
                0.0F;
        };
    }

    @Internal
    public static int intToComplex(int value) {
        if (value >= -8388608 && value < 8388608) {
            return 0 | value << 8;
        } else {
            throw new IllegalArgumentException("Magnitude of the value is too large: " + value);
        }
    }

    @Internal
    public static int floatToComplex(float value) {
        if (!(value < -8388608.0F) && !(value >= 8388607.5F)) {
            int bits = Float.floatToRawIntBits(value) + 192937984;
            long mag = (long) (Float.intBitsToFloat(bits & 2147483647) + 0.5F);
            int radix;
            int shift;
            if ((mag & 8388607L) == 0L) {
                radix = 0;
                shift = 23;
            } else if ((mag & 9223372036846387200L) == 0L) {
                radix = 3;
                shift = 0;
            } else if ((mag & 9223372034707292160L) == 0L) {
                radix = 2;
                shift = 8;
            } else if ((mag & 9223371487098961920L) == 0L) {
                radix = 1;
                shift = 16;
            } else {
                radix = 0;
                shift = 23;
            }
            int mantissa = (int) (mag >>> shift) & 16777215;
            if ((bits & -2147483648) != 0) {
                mantissa = -mantissa;
            }
            assert mantissa >= -8388608 && mantissa < 8388608;
            return radix << 4 | mantissa << 8;
        } else {
            throw new IllegalArgumentException("Magnitude of the value is too large: " + value);
        }
    }

    @Internal
    public static int createComplexDimension(int value, int units) {
        if (units >= 0 && units <= 5) {
            return intToComplex(value) | units;
        } else {
            throw new IllegalArgumentException("Must be a valid COMPLEX_UNIT_*: " + units);
        }
    }

    @Internal
    public static int createComplexDimension(float value, int units) {
        if (units >= 0 && units <= 5) {
            return floatToComplex(value) | units;
        } else {
            throw new IllegalArgumentException("Must be a valid COMPLEX_UNIT_*: " + units);
        }
    }

    public static float complexToFraction(int data, float base, float pbase) {
        return switch(data >> 0 & 15) {
            case 0 ->
                complexToFloat(data) * base;
            case 1 ->
                complexToFloat(data) * pbase;
            default ->
                0.0F;
        };
    }

    @Retention(RetentionPolicy.SOURCE)
    @Internal
    public @interface ComplexDimensionUnit {
    }
}