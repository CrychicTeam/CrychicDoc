package noppes.npcs.shared.common.util;

public class NopVector2i {

    public static NopVector2i ZERO = new NopVector2i(0, 0);

    public final int x;

    public final int y;

    public NopVector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public NopVector2i(int[] arr) {
        this(arr[0], arr[1]);
    }

    public NopVector2i mul(int mul) {
        return this.mul(mul, mul);
    }

    public NopVector2i mul(int mulX, int mulY) {
        return new NopVector2i(this.x * mulX, this.y * mulY);
    }
}