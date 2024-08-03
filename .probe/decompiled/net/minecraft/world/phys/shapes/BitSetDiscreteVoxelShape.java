package net.minecraft.world.phys.shapes;

import java.util.BitSet;
import net.minecraft.core.Direction;

public final class BitSetDiscreteVoxelShape extends DiscreteVoxelShape {

    private final BitSet storage;

    private int xMin;

    private int yMin;

    private int zMin;

    private int xMax;

    private int yMax;

    private int zMax;

    public BitSetDiscreteVoxelShape(int int0, int int1, int int2) {
        super(int0, int1, int2);
        this.storage = new BitSet(int0 * int1 * int2);
        this.xMin = int0;
        this.yMin = int1;
        this.zMin = int2;
    }

    public static BitSetDiscreteVoxelShape withFilledBounds(int int0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, int int8) {
        BitSetDiscreteVoxelShape $$9 = new BitSetDiscreteVoxelShape(int0, int1, int2);
        $$9.xMin = int3;
        $$9.yMin = int4;
        $$9.zMin = int5;
        $$9.xMax = int6;
        $$9.yMax = int7;
        $$9.zMax = int8;
        for (int $$10 = int3; $$10 < int6; $$10++) {
            for (int $$11 = int4; $$11 < int7; $$11++) {
                for (int $$12 = int5; $$12 < int8; $$12++) {
                    $$9.fillUpdateBounds($$10, $$11, $$12, false);
                }
            }
        }
        return $$9;
    }

    public BitSetDiscreteVoxelShape(DiscreteVoxelShape discreteVoxelShape0) {
        super(discreteVoxelShape0.xSize, discreteVoxelShape0.ySize, discreteVoxelShape0.zSize);
        if (discreteVoxelShape0 instanceof BitSetDiscreteVoxelShape) {
            this.storage = (BitSet) ((BitSetDiscreteVoxelShape) discreteVoxelShape0).storage.clone();
        } else {
            this.storage = new BitSet(this.f_82781_ * this.f_82782_ * this.f_82783_);
            for (int $$1 = 0; $$1 < this.f_82781_; $$1++) {
                for (int $$2 = 0; $$2 < this.f_82782_; $$2++) {
                    for (int $$3 = 0; $$3 < this.f_82783_; $$3++) {
                        if (discreteVoxelShape0.isFull($$1, $$2, $$3)) {
                            this.storage.set(this.getIndex($$1, $$2, $$3));
                        }
                    }
                }
            }
        }
        this.xMin = discreteVoxelShape0.firstFull(Direction.Axis.X);
        this.yMin = discreteVoxelShape0.firstFull(Direction.Axis.Y);
        this.zMin = discreteVoxelShape0.firstFull(Direction.Axis.Z);
        this.xMax = discreteVoxelShape0.lastFull(Direction.Axis.X);
        this.yMax = discreteVoxelShape0.lastFull(Direction.Axis.Y);
        this.zMax = discreteVoxelShape0.lastFull(Direction.Axis.Z);
    }

    protected int getIndex(int int0, int int1, int int2) {
        return (int0 * this.f_82782_ + int1) * this.f_82783_ + int2;
    }

    @Override
    public boolean isFull(int int0, int int1, int int2) {
        return this.storage.get(this.getIndex(int0, int1, int2));
    }

    private void fillUpdateBounds(int int0, int int1, int int2, boolean boolean3) {
        this.storage.set(this.getIndex(int0, int1, int2));
        if (boolean3) {
            this.xMin = Math.min(this.xMin, int0);
            this.yMin = Math.min(this.yMin, int1);
            this.zMin = Math.min(this.zMin, int2);
            this.xMax = Math.max(this.xMax, int0 + 1);
            this.yMax = Math.max(this.yMax, int1 + 1);
            this.zMax = Math.max(this.zMax, int2 + 1);
        }
    }

    @Override
    public void fill(int int0, int int1, int int2) {
        this.fillUpdateBounds(int0, int1, int2, true);
    }

    @Override
    public boolean isEmpty() {
        return this.storage.isEmpty();
    }

    @Override
    public int firstFull(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.xMin, this.yMin, this.zMin);
    }

    @Override
    public int lastFull(Direction.Axis directionAxis0) {
        return directionAxis0.choose(this.xMax, this.yMax, this.zMax);
    }

    static BitSetDiscreteVoxelShape join(DiscreteVoxelShape discreteVoxelShape0, DiscreteVoxelShape discreteVoxelShape1, IndexMerger indexMerger2, IndexMerger indexMerger3, IndexMerger indexMerger4, BooleanOp booleanOp5) {
        BitSetDiscreteVoxelShape $$6 = new BitSetDiscreteVoxelShape(indexMerger2.size() - 1, indexMerger3.size() - 1, indexMerger4.size() - 1);
        int[] $$7 = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE };
        indexMerger2.forMergedIndexes((p_82670_, p_82671_, p_82672_) -> {
            boolean[] $$10 = new boolean[] { false };
            indexMerger3.forMergedIndexes((p_165978_, p_165979_, p_165980_) -> {
                boolean[] $$13 = new boolean[] { false };
                indexMerger4.forMergedIndexes((p_165960_, p_165961_, p_165962_) -> {
                    if (booleanOp5.apply(discreteVoxelShape0.isFullWide(p_82670_, p_165978_, p_165960_), discreteVoxelShape1.isFullWide(p_82671_, p_165979_, p_165961_))) {
                        $$6.storage.set($$6.getIndex(p_82672_, p_165980_, p_165962_));
                        $$7[2] = Math.min($$7[2], p_165962_);
                        $$7[5] = Math.max($$7[5], p_165962_);
                        $$13[0] = true;
                    }
                    return true;
                });
                if ($$13[0]) {
                    $$7[1] = Math.min($$7[1], p_165980_);
                    $$7[4] = Math.max($$7[4], p_165980_);
                    $$10[0] = true;
                }
                return true;
            });
            if ($$10[0]) {
                $$7[0] = Math.min($$7[0], p_82672_);
                $$7[3] = Math.max($$7[3], p_82672_);
            }
            return true;
        });
        $$6.xMin = $$7[0];
        $$6.yMin = $$7[1];
        $$6.zMin = $$7[2];
        $$6.xMax = $$7[3] + 1;
        $$6.yMax = $$7[4] + 1;
        $$6.zMax = $$7[5] + 1;
        return $$6;
    }

    protected static void forAllBoxes(DiscreteVoxelShape discreteVoxelShape0, DiscreteVoxelShape.IntLineConsumer discreteVoxelShapeIntLineConsumer1, boolean boolean2) {
        BitSetDiscreteVoxelShape $$3 = new BitSetDiscreteVoxelShape(discreteVoxelShape0);
        for (int $$4 = 0; $$4 < $$3.f_82782_; $$4++) {
            for (int $$5 = 0; $$5 < $$3.f_82781_; $$5++) {
                int $$6 = -1;
                for (int $$7 = 0; $$7 <= $$3.f_82783_; $$7++) {
                    if ($$3.m_82846_($$5, $$4, $$7)) {
                        if (boolean2) {
                            if ($$6 == -1) {
                                $$6 = $$7;
                            }
                        } else {
                            discreteVoxelShapeIntLineConsumer1.consume($$5, $$4, $$7, $$5 + 1, $$4 + 1, $$7 + 1);
                        }
                    } else if ($$6 != -1) {
                        int $$8 = $$5;
                        int $$9 = $$4;
                        $$3.clearZStrip($$6, $$7, $$5, $$4);
                        while ($$3.isZStripFull($$6, $$7, $$8 + 1, $$4)) {
                            $$3.clearZStrip($$6, $$7, $$8 + 1, $$4);
                            $$8++;
                        }
                        while ($$3.isXZRectangleFull($$5, $$8 + 1, $$6, $$7, $$9 + 1)) {
                            for (int $$10 = $$5; $$10 <= $$8; $$10++) {
                                $$3.clearZStrip($$6, $$7, $$10, $$9 + 1);
                            }
                            $$9++;
                        }
                        discreteVoxelShapeIntLineConsumer1.consume($$5, $$4, $$6, $$8 + 1, $$9 + 1, $$7);
                        $$6 = -1;
                    }
                }
            }
        }
    }

    private boolean isZStripFull(int int0, int int1, int int2, int int3) {
        return int2 < this.f_82781_ && int3 < this.f_82782_ ? this.storage.nextClearBit(this.getIndex(int2, int3, int0)) >= this.getIndex(int2, int3, int1) : false;
    }

    private boolean isXZRectangleFull(int int0, int int1, int int2, int int3, int int4) {
        for (int $$5 = int0; $$5 < int1; $$5++) {
            if (!this.isZStripFull(int2, int3, $$5, int4)) {
                return false;
            }
        }
        return true;
    }

    private void clearZStrip(int int0, int int1, int int2, int int3) {
        this.storage.clear(this.getIndex(int2, int3, int0), this.getIndex(int2, int3, int1));
    }
}