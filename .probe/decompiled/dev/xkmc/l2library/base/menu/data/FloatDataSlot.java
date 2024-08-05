package dev.xkmc.l2library.base.menu.data;

import net.minecraft.world.inventory.AbstractContainerMenu;

public class FloatDataSlot {

    private final IntDataSlot data;

    public FloatDataSlot(AbstractContainerMenu menu) {
        this.data = new IntDataSlot(menu);
    }

    public float get() {
        return Float.intBitsToFloat(this.data.get());
    }

    public void set(float pc) {
        this.data.set(Float.floatToIntBits(pc));
    }
}