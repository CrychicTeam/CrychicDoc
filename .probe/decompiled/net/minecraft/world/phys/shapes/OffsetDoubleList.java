package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;
import it.unimi.dsi.fastutil.doubles.DoubleList;

public class OffsetDoubleList extends AbstractDoubleList {

    private final DoubleList delegate;

    private final double offset;

    public OffsetDoubleList(DoubleList doubleList0, double double1) {
        this.delegate = doubleList0;
        this.offset = double1;
    }

    public double getDouble(int int0) {
        return this.delegate.getDouble(int0) + this.offset;
    }

    public int size() {
        return this.delegate.size();
    }
}