package dev.xkmc.l2library.base.menu.data;

import net.minecraft.world.inventory.AbstractContainerMenu;

public class LongDataSlot {

    private final IntDataSlot lo;

    private final IntDataSlot hi;

    public LongDataSlot(AbstractContainerMenu menu) {
        this.lo = new IntDataSlot(menu);
        this.hi = new IntDataSlot(menu);
    }

    public long get() {
        return (long) this.hi.get() << 32 | Integer.toUnsignedLong(this.lo.get());
    }

    public void set(long pc) {
        this.lo.set((int) pc);
        this.hi.set((int) (pc >> 32));
    }
}