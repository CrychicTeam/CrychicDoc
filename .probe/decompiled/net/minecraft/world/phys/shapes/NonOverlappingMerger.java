package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public class NonOverlappingMerger extends AbstractDoubleList implements IndexMerger {

    private final DoubleList lower;

    private final DoubleList upper;

    private final boolean swap;

    protected NonOverlappingMerger(DoubleList doubleList0, DoubleList doubleList1, boolean boolean2) {
        this.lower = doubleList0;
        this.upper = doubleList1;
        this.swap = boolean2;
    }

    @Override
    public int size() {
        return this.lower.size() + this.upper.size();
    }

    @Override
    public boolean forMergedIndexes(IndexMerger.IndexConsumer indexMergerIndexConsumer0) {
        return this.swap ? this.forNonSwappedIndexes((p_83020_, p_83021_, p_83022_) -> indexMergerIndexConsumer0.merge(p_83021_, p_83020_, p_83022_)) : this.forNonSwappedIndexes(indexMergerIndexConsumer0);
    }

    private boolean forNonSwappedIndexes(IndexMerger.IndexConsumer indexMergerIndexConsumer0) {
        int $$1 = this.lower.size();
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            if (!indexMergerIndexConsumer0.merge($$2, -1, $$2)) {
                return false;
            }
        }
        int $$3 = this.upper.size() - 1;
        for (int $$4 = 0; $$4 < $$3; $$4++) {
            if (!indexMergerIndexConsumer0.merge($$1 - 1, $$4, $$1 + $$4)) {
                return false;
            }
        }
        return true;
    }

    public double getDouble(int int0) {
        return int0 < this.lower.size() ? this.lower.getDouble(int0) : this.upper.getDouble(int0 - this.lower.size());
    }

    @Override
    public DoubleList getList() {
        return this;
    }
}