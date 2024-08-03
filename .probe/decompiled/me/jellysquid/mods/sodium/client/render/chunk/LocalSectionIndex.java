package me.jellysquid.mods.sodium.client.render.chunk;

public class LocalSectionIndex {

    private static final int X_BITS = 7;

    private static final int X_OFFSET = 5;

    private static final int X_MASK = 224;

    private static final int Y_BITS = 3;

    private static final int Y_OFFSET = 0;

    private static final int Y_MASK = 3;

    private static final int Z_BITS = 7;

    private static final int Z_OFFSET = 2;

    private static final int Z_MASK = 28;

    public static int pack(int x, int y, int z) {
        return (x & 7) << 5 | (y & 3) << 0 | (z & 7) << 2;
    }

    public static int incX(int idx) {
        return idx & -225 | idx + 32 & 224;
    }

    public static int decX(int idx) {
        return idx & -225 | idx - 32 & 224;
    }

    public static int incY(int idx) {
        return idx & -4 | idx + 1 & 3;
    }

    public static int decY(int idx) {
        return idx & -4 | idx - 1 & 3;
    }

    public static int incZ(int idx) {
        return idx & -29 | idx + 4 & 28;
    }

    public static int decZ(int idx) {
        return idx & -29 | idx - 4 & 28;
    }

    public static int unpackX(int idx) {
        return idx >> 5 & 7;
    }

    public static int unpackY(int idx) {
        return idx >> 0 & 3;
    }

    public static int unpackZ(int idx) {
        return idx >> 2 & 7;
    }
}