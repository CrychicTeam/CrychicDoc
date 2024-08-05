package dev.xkmc.l2library.base.menu.data;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;

public class BoolArrayDataSlot {

    private final DataSlot[] array;

    public BoolArrayDataSlot(AbstractContainerMenu menu, int size) {
        int n = size / 16 + (size % 16 == 0 ? 0 : 1);
        this.array = new DataSlot[n];
        for (int i = 0; i < n; i++) {
            this.array[i] = menu.addDataSlot(DataSlot.standalone());
        }
    }

    public boolean get(int i) {
        return (this.array[i >> 4].get() & 1 << (i & 15)) != 0;
    }

    public void set(boolean pc, int i) {
        int val = this.array[i >> 4].get();
        int mask = 1 << (i & 15);
        boolean old = (val & mask) != 0;
        if (old != pc) {
            val ^= mask;
            this.array[i >> 4].set(val);
        }
    }
}