package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.AbstractDoubleList;

public class CubePointRange extends AbstractDoubleList {

    private final int parts;

    CubePointRange(int int0) {
        if (int0 <= 0) {
            throw new IllegalArgumentException("Need at least 1 part");
        } else {
            this.parts = int0;
        }
    }

    public double getDouble(int int0) {
        return (double) int0 / (double) this.parts;
    }

    public int size() {
        return this.parts + 1;
    }
}