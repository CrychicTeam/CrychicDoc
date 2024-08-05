package net.minecraft.world.phys.shapes;

import net.minecraft.core.AxisCycle;
import net.minecraft.core.Direction;

public abstract class DiscreteVoxelShape {

    private static final Direction.Axis[] AXIS_VALUES = Direction.Axis.values();

    protected final int xSize;

    protected final int ySize;

    protected final int zSize;

    protected DiscreteVoxelShape(int int0, int int1, int int2) {
        if (int0 >= 0 && int1 >= 0 && int2 >= 0) {
            this.xSize = int0;
            this.ySize = int1;
            this.zSize = int2;
        } else {
            throw new IllegalArgumentException("Need all positive sizes: x: " + int0 + ", y: " + int1 + ", z: " + int2);
        }
    }

    public boolean isFullWide(AxisCycle axisCycle0, int int1, int int2, int int3) {
        return this.isFullWide(axisCycle0.cycle(int1, int2, int3, Direction.Axis.X), axisCycle0.cycle(int1, int2, int3, Direction.Axis.Y), axisCycle0.cycle(int1, int2, int3, Direction.Axis.Z));
    }

    public boolean isFullWide(int int0, int int1, int int2) {
        if (int0 < 0 || int1 < 0 || int2 < 0) {
            return false;
        } else {
            return int0 < this.xSize && int1 < this.ySize && int2 < this.zSize ? this.isFull(int0, int1, int2) : false;
        }
    }

    public boolean isFull(AxisCycle axisCycle0, int int1, int int2, int int3) {
        return this.isFull(axisCycle0.cycle(int1, int2, int3, Direction.Axis.X), axisCycle0.cycle(int1, int2, int3, Direction.Axis.Y), axisCycle0.cycle(int1, int2, int3, Direction.Axis.Z));
    }

    public abstract boolean isFull(int var1, int var2, int var3);

    public abstract void fill(int var1, int var2, int var3);

    public boolean isEmpty() {
        for (Direction.Axis $$0 : AXIS_VALUES) {
            if (this.firstFull($$0) >= this.lastFull($$0)) {
                return true;
            }
        }
        return false;
    }

    public abstract int firstFull(Direction.Axis var1);

    public abstract int lastFull(Direction.Axis var1);

    public int firstFull(Direction.Axis directionAxis0, int int1, int int2) {
        int $$3 = this.getSize(directionAxis0);
        if (int1 >= 0 && int2 >= 0) {
            Direction.Axis $$4 = AxisCycle.FORWARD.cycle(directionAxis0);
            Direction.Axis $$5 = AxisCycle.BACKWARD.cycle(directionAxis0);
            if (int1 < this.getSize($$4) && int2 < this.getSize($$5)) {
                AxisCycle $$6 = AxisCycle.between(Direction.Axis.X, directionAxis0);
                for (int $$7 = 0; $$7 < $$3; $$7++) {
                    if (this.isFull($$6, $$7, int1, int2)) {
                        return $$7;
                    }
                }
                return $$3;
            } else {
                return $$3;
            }
        } else {
            return $$3;
        }
    }

    public int lastFull(Direction.Axis directionAxis0, int int1, int int2) {
        if (int1 >= 0 && int2 >= 0) {
            Direction.Axis $$3 = AxisCycle.FORWARD.cycle(directionAxis0);
            Direction.Axis $$4 = AxisCycle.BACKWARD.cycle(directionAxis0);
            if (int1 < this.getSize($$3) && int2 < this.getSize($$4)) {
                int $$5 = this.getSize(directionAxis0);
                AxisCycle $$6 = AxisCycle.between(Direction.Axis.X, directionAxis0);
                for (int $$7 = $$5 - 1; $$7 >= 0; $$7--) {
                    if (this.isFull($$6, $$7, int1, int2)) {
                        return $$7 + 1;
                    }
                }
                return 0;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public int getSize(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.xSize, this.ySize, this.zSize);
    }

    public int getXSize() {
        return this.getSize(Direction.Axis.X);
    }

    public int getYSize() {
        return this.getSize(Direction.Axis.Y);
    }

    public int getZSize() {
        return this.getSize(Direction.Axis.Z);
    }

    public void forAllEdges(DiscreteVoxelShape.IntLineConsumer discreteVoxelShapeIntLineConsumer0, boolean boolean1) {
        this.forAllAxisEdges(discreteVoxelShapeIntLineConsumer0, AxisCycle.NONE, boolean1);
        this.forAllAxisEdges(discreteVoxelShapeIntLineConsumer0, AxisCycle.FORWARD, boolean1);
        this.forAllAxisEdges(discreteVoxelShapeIntLineConsumer0, AxisCycle.BACKWARD, boolean1);
    }

    private void forAllAxisEdges(DiscreteVoxelShape.IntLineConsumer discreteVoxelShapeIntLineConsumer0, AxisCycle axisCycle1, boolean boolean2) {
        AxisCycle $$3 = axisCycle1.inverse();
        int $$4 = this.getSize($$3.cycle(Direction.Axis.X));
        int $$5 = this.getSize($$3.cycle(Direction.Axis.Y));
        int $$6 = this.getSize($$3.cycle(Direction.Axis.Z));
        for (int $$7 = 0; $$7 <= $$4; $$7++) {
            for (int $$8 = 0; $$8 <= $$5; $$8++) {
                int $$9 = -1;
                for (int $$10 = 0; $$10 <= $$6; $$10++) {
                    int $$11 = 0;
                    int $$12 = 0;
                    for (int $$13 = 0; $$13 <= 1; $$13++) {
                        for (int $$14 = 0; $$14 <= 1; $$14++) {
                            if (this.isFullWide($$3, $$7 + $$13 - 1, $$8 + $$14 - 1, $$10)) {
                                $$11++;
                                $$12 ^= $$13 ^ $$14;
                            }
                        }
                    }
                    if ($$11 == 1 || $$11 == 3 || $$11 == 2 && ($$12 & 1) == 0) {
                        if (boolean2) {
                            if ($$9 == -1) {
                                $$9 = $$10;
                            }
                        } else {
                            discreteVoxelShapeIntLineConsumer0.consume($$3.cycle($$7, $$8, $$10, Direction.Axis.X), $$3.cycle($$7, $$8, $$10, Direction.Axis.Y), $$3.cycle($$7, $$8, $$10, Direction.Axis.Z), $$3.cycle($$7, $$8, $$10 + 1, Direction.Axis.X), $$3.cycle($$7, $$8, $$10 + 1, Direction.Axis.Y), $$3.cycle($$7, $$8, $$10 + 1, Direction.Axis.Z));
                        }
                    } else if ($$9 != -1) {
                        discreteVoxelShapeIntLineConsumer0.consume($$3.cycle($$7, $$8, $$9, Direction.Axis.X), $$3.cycle($$7, $$8, $$9, Direction.Axis.Y), $$3.cycle($$7, $$8, $$9, Direction.Axis.Z), $$3.cycle($$7, $$8, $$10, Direction.Axis.X), $$3.cycle($$7, $$8, $$10, Direction.Axis.Y), $$3.cycle($$7, $$8, $$10, Direction.Axis.Z));
                        $$9 = -1;
                    }
                }
            }
        }
    }

    public void forAllBoxes(DiscreteVoxelShape.IntLineConsumer discreteVoxelShapeIntLineConsumer0, boolean boolean1) {
        BitSetDiscreteVoxelShape.forAllBoxes(this, discreteVoxelShapeIntLineConsumer0, boolean1);
    }

    public void forAllFaces(DiscreteVoxelShape.IntFaceConsumer discreteVoxelShapeIntFaceConsumer0) {
        this.forAllAxisFaces(discreteVoxelShapeIntFaceConsumer0, AxisCycle.NONE);
        this.forAllAxisFaces(discreteVoxelShapeIntFaceConsumer0, AxisCycle.FORWARD);
        this.forAllAxisFaces(discreteVoxelShapeIntFaceConsumer0, AxisCycle.BACKWARD);
    }

    private void forAllAxisFaces(DiscreteVoxelShape.IntFaceConsumer discreteVoxelShapeIntFaceConsumer0, AxisCycle axisCycle1) {
        AxisCycle $$2 = axisCycle1.inverse();
        Direction.Axis $$3 = $$2.cycle(Direction.Axis.Z);
        int $$4 = this.getSize($$2.cycle(Direction.Axis.X));
        int $$5 = this.getSize($$2.cycle(Direction.Axis.Y));
        int $$6 = this.getSize($$3);
        Direction $$7 = Direction.fromAxisAndDirection($$3, Direction.AxisDirection.NEGATIVE);
        Direction $$8 = Direction.fromAxisAndDirection($$3, Direction.AxisDirection.POSITIVE);
        for (int $$9 = 0; $$9 < $$4; $$9++) {
            for (int $$10 = 0; $$10 < $$5; $$10++) {
                boolean $$11 = false;
                for (int $$12 = 0; $$12 <= $$6; $$12++) {
                    boolean $$13 = $$12 != $$6 && this.isFull($$2, $$9, $$10, $$12);
                    if (!$$11 && $$13) {
                        discreteVoxelShapeIntFaceConsumer0.consume($$7, $$2.cycle($$9, $$10, $$12, Direction.Axis.X), $$2.cycle($$9, $$10, $$12, Direction.Axis.Y), $$2.cycle($$9, $$10, $$12, Direction.Axis.Z));
                    }
                    if ($$11 && !$$13) {
                        discreteVoxelShapeIntFaceConsumer0.consume($$8, $$2.cycle($$9, $$10, $$12 - 1, Direction.Axis.X), $$2.cycle($$9, $$10, $$12 - 1, Direction.Axis.Y), $$2.cycle($$9, $$10, $$12 - 1, Direction.Axis.Z));
                    }
                    $$11 = $$13;
                }
            }
        }
    }

    public interface IntFaceConsumer {

        void consume(Direction var1, int var2, int var3, int var4);
    }

    public interface IntLineConsumer {

        void consume(int var1, int var2, int var3, int var4, int var5, int var6);
    }
}