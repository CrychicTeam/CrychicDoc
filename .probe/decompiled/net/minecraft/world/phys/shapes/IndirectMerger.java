package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleLists;

public class IndirectMerger implements IndexMerger {

    private static final DoubleList EMPTY = DoubleLists.unmodifiable(DoubleArrayList.wrap(new double[] { 0.0 }));

    private final double[] result;

    private final int[] firstIndices;

    private final int[] secondIndices;

    private final int resultLength;

    public IndirectMerger(DoubleList doubleList0, DoubleList doubleList1, boolean boolean2, boolean boolean3) {
        double $$4 = Double.NaN;
        int $$5 = doubleList0.size();
        int $$6 = doubleList1.size();
        int $$7 = $$5 + $$6;
        this.result = new double[$$7];
        this.firstIndices = new int[$$7];
        this.secondIndices = new int[$$7];
        boolean $$8 = !boolean2;
        boolean $$9 = !boolean3;
        int $$10 = 0;
        int $$11 = 0;
        int $$12 = 0;
        while (true) {
            boolean $$13 = $$11 >= $$5;
            boolean $$14 = $$12 >= $$6;
            if ($$13 && $$14) {
                this.resultLength = Math.max(1, $$10);
                return;
            }
            boolean $$15 = !$$13 && ($$14 || doubleList0.getDouble($$11) < doubleList1.getDouble($$12) + 1.0E-7);
            if ($$15) {
                $$11++;
                if ($$8 && ($$12 == 0 || $$14)) {
                    continue;
                }
            } else {
                $$12++;
                if ($$9 && ($$11 == 0 || $$13)) {
                    continue;
                }
            }
            int $$16 = $$11 - 1;
            int $$17 = $$12 - 1;
            double $$18 = $$15 ? doubleList0.getDouble($$16) : doubleList1.getDouble($$17);
            if (!($$4 >= $$18 - 1.0E-7)) {
                this.firstIndices[$$10] = $$16;
                this.secondIndices[$$10] = $$17;
                this.result[$$10] = $$18;
                $$10++;
                $$4 = $$18;
            } else {
                this.firstIndices[$$10 - 1] = $$16;
                this.secondIndices[$$10 - 1] = $$17;
            }
        }
    }

    @Override
    public boolean forMergedIndexes(IndexMerger.IndexConsumer indexMergerIndexConsumer0) {
        int $$1 = this.resultLength - 1;
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            if (!indexMergerIndexConsumer0.merge(this.firstIndices[$$2], this.secondIndices[$$2], $$2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return this.resultLength;
    }

    @Override
    public DoubleList getList() {
        return (DoubleList) (this.resultLength <= 1 ? EMPTY : DoubleArrayList.wrap(this.result, this.resultLength));
    }
}