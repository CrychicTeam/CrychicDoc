package noppes.npcs.shared.common.util;

public class NopVector3i {

    public static NopVector3i ZERO = new NopVector3i(0, 0, 0);

    public final int x;

    public final int y;

    public final int z;

    public NopVector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public NopVector3i(int[] arr) {
        this(arr[0], arr[1], arr[2]);
    }

    public NopVector3i mul(int mul) {
        return this.mul(mul, mul, mul);
    }

    public NopVector3i mul(int mulX, int mulY, int mulZ) {
        return new NopVector3i(this.x * mulX, this.y * mulY, this.z * mulZ);
    }
}