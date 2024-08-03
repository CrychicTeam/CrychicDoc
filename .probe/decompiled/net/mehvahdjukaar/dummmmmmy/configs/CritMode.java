package net.mehvahdjukaar.dummmmmmy.configs;

public enum CritMode {

    OFF, COLOR, COLOR_AND_MULTIPLIER;

    public static double encodeIntFloatToDouble(int integerPart, float floatPart) {
        long combinedValue = (long) Float.floatToIntBits(floatPart) << 32 | (long) integerPart & 4294967295L;
        return Double.longBitsToDouble(combinedValue);
    }

    public static int extractIntegerPart(double encodedDouble) {
        long combinedValue = Double.doubleToLongBits(encodedDouble);
        return (int) (combinedValue & 4294967295L);
    }

    public static float extractFloatPart(double encodedDouble) {
        long combinedValue = Double.doubleToLongBits(encodedDouble);
        int floatBits = (int) (combinedValue >> 32);
        return Float.intBitsToFloat(floatBits);
    }
}