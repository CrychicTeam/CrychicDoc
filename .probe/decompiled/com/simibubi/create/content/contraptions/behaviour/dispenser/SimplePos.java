package com.simibubi.create.content.contraptions.behaviour.dispenser;

import net.minecraft.core.Position;

public class SimplePos implements Position {

    private final double x;

    private final double y;

    private final double z;

    public SimplePos(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
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