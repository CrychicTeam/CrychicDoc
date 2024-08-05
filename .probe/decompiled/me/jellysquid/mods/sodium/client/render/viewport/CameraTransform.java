package me.jellysquid.mods.sodium.client.render.viewport;

public class CameraTransform {

    private static final float PRECISION_MODIFIER = 128.0F;

    public final int intX;

    public final int intY;

    public final int intZ;

    public final float fracX;

    public final float fracY;

    public final float fracZ;

    public final double x;

    public final double y;

    public final double z;

    public CameraTransform(double x, double y, double z) {
        this.intX = integral(x);
        this.intY = integral(y);
        this.intZ = integral(z);
        this.fracX = fractional(x);
        this.fracY = fractional(y);
        this.fracZ = fractional(z);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    private static int integral(double value) {
        return (int) value;
    }

    private static float fractional(double value) {
        float fullPrecision = (float) (value - (double) integral(value));
        float modifier = Math.copySign(128.0F, fullPrecision);
        return fullPrecision + modifier - modifier;
    }
}