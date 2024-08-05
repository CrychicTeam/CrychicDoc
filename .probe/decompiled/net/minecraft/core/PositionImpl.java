package net.minecraft.core;

public class PositionImpl implements Position {

    protected final double x;

    protected final double y;

    protected final double z;

    public PositionImpl(double double0, double double1, double double2) {
        this.x = double0;
        this.y = double1;
        this.z = double2;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }
}