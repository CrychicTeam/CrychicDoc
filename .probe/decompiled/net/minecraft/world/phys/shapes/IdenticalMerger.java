package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;

public class IdenticalMerger implements IndexMerger {

    private final DoubleList coords;

    public IdenticalMerger(DoubleList doubleList0) {
        this.coords = doubleList0;
    }

    @Override
    public boolean forMergedIndexes(IndexMerger.IndexConsumer indexMergerIndexConsumer0) {
        int $$1 = this.coords.size() - 1;
        for (int $$2 = 0; $$2 < $$1; $$2++) {
            if (!indexMergerIndexConsumer0.merge($$2, $$2, $$2)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return this.coords.size();
    }

    @Override
    public DoubleList getList() {
        return this.coords;
    }
}