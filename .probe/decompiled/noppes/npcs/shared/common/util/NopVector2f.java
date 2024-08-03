package noppes.npcs.shared.common.util;

public class NopVector2f {

    public static final NopVector2f ZERO = new NopVector2f(0.0F, 0.0F);

    public static final NopVector2f ONE = new NopVector2f(0.0F, 0.0F);

    public final float x;

    public final float y;

    public NopVector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public NopVector2f(float[] values) {
        this(values[0], values[1]);
    }

    public NopVector2f mul(float mul) {
        return new NopVector2f(this.x * mul, this.y * mul);
    }

    public NopVector2f add(float x, float y, float z) {
        return new NopVector2f(this.x + x, this.y + y);
    }

    public NopVector2f set(float x, float y, float z) {
        return new NopVector2f(x, y);
    }
}