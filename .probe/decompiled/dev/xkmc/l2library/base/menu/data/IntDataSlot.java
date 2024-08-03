package dev.xkmc.l2library.base.menu.data;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;

public class IntDataSlot {

    private final DataSlot hi;

    private final DataSlot lo;

    public IntDataSlot(AbstractContainerMenu menu) {
        this.hi = menu.addDataSlot(DataSlot.standalone());
        this.lo = menu.addDataSlot(DataSlot.standalone());
    }

    public int get() {
        return this.hi.get() << 16 | Short.toUnsignedInt((short) this.lo.get());
    }

    public void set(int pc) {
        this.lo.set((short) (pc & 65535));
        this.hi.set(pc >> 16);
    }
}