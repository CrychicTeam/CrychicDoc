package com.simibubi.create.content.contraptions.behaviour.dispenser;

public class DispenseItemLocation {

    private final boolean internal;

    private final int slot;

    public static final DispenseItemLocation NONE = new DispenseItemLocation(false, -1);

    public DispenseItemLocation(boolean internal, int slot) {
        this.internal = internal;
        this.slot = slot;
    }

    public boolean isInternal() {
        return this.internal;
    }

    public int getSlot() {
        return this.slot;
    }

    public boolean isEmpty() {
        return this.slot < 0;
    }
}