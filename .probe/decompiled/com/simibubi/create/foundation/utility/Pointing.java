package com.simibubi.create.foundation.utility;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;

public enum Pointing implements StringRepresentable {

    UP(0), LEFT(270), DOWN(180), RIGHT(90);

    private int xRotation;

    private Pointing(int xRotation) {
        this.xRotation = xRotation;
    }

    @Override
    public String getSerializedName() {
        return Lang.asId(this.name());
    }

    public int getXRotation() {
        return this.xRotation;
    }

    public Direction getCombinedDirection(Direction direction) {
        Direction.Axis axis = direction.getAxis();
        Direction top = axis == Direction.Axis.Y ? Direction.SOUTH : Direction.UP;
        int rotations = direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 4 - this.ordinal() : this.ordinal();
        for (int i = 0; i < rotations; i++) {
            top = top.getClockWise(axis);
        }
        return top;
    }
}