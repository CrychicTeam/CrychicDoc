package net.minecraft.world.phys.shapes;

import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public final class SubShape extends DiscreteVoxelShape {

    private final DiscreteVoxelShape parent;

    private final int startX;

    private final int startY;

    private final int startZ;

    private final int endX;

    private final int endY;

    private final int endZ;

    protected SubShape(DiscreteVoxelShape discreteVoxelShape0, int int1, int int2, int int3, int int4, int int5, int int6) {
        super(int4 - int1, int5 - int2, int6 - int3);
        this.parent = discreteVoxelShape0;
        this.startX = int1;
        this.startY = int2;
        this.startZ = int3;
        this.endX = int4;
        this.endY = int5;
        this.endZ = int6;
    }

    @Override
    public boolean isFull(int int0, int int1, int int2) {
        return this.parent.isFull(this.startX + int0, this.startY + int1, this.startZ + int2);
    }

    @Override
    public void fill(int int0, int int1, int int2) {
        this.parent.fill(this.startX + int0, this.startY + int1, this.startZ + int2);
    }

    @Override
    public int firstFull(Direction.Axis directionAxis0) {
        return this.clampToShape(directionAxis0, this.parent.firstFull(directionAxis0));
    }

    @Override
    public int lastFull(Direction.Axis directionAxis0) {
        return this.clampToShape(directionAxis0, this.parent.lastFull(directionAxis0));
    }

    private int clampToShape(Direction.Axis directionAxis0, int int1) {
        int $$2 = directionAxis0.choose(this.startX, this.startY, this.startZ);
        int $$3 = directionAxis0.choose(this.endX, this.endY, this.endZ);
        return Mth.clamp(int1, $$2, $$3) - $$2;
    }
}