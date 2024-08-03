package net.minecraft.core;

public class Cursor3D {

    public static final int TYPE_INSIDE = 0;

    public static final int TYPE_FACE = 1;

    public static final int TYPE_EDGE = 2;

    public static final int TYPE_CORNER = 3;

    private final int originX;

    private final int originY;

    private final int originZ;

    private final int width;

    private final int height;

    private final int depth;

    private final int end;

    private int index;

    private int x;

    private int y;

    private int z;

    public Cursor3D(int int0, int int1, int int2, int int3, int int4, int int5) {
        this.originX = int0;
        this.originY = int1;
        this.originZ = int2;
        this.width = int3 - int0 + 1;
        this.height = int4 - int1 + 1;
        this.depth = int5 - int2 + 1;
        this.end = this.width * this.height * this.depth;
    }

    public boolean advance() {
        if (this.index == this.end) {
            return false;
        } else {
            this.x = this.index % this.width;
            int $$0 = this.index / this.width;
            this.y = $$0 % this.height;
            this.z = $$0 / this.height;
            this.index++;
            return true;
        }
    }

    public int nextX() {
        return this.originX + this.x;
    }

    public int nextY() {
        return this.originY + this.y;
    }

    public int nextZ() {
        return this.originZ + this.z;
    }

    public int getNextType() {
        int $$0 = 0;
        if (this.x == 0 || this.x == this.width - 1) {
            $$0++;
        }
        if (this.y == 0 || this.y == this.height - 1) {
            $$0++;
        }
        if (this.z == 0 || this.z == this.depth - 1) {
            $$0++;
        }
        return $$0;
    }
}