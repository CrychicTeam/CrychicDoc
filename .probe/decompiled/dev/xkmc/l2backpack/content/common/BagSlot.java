package dev.xkmc.l2backpack.content.common;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class BagSlot extends SlotItemHandler {

    private final IItemHandlerModifiable bag;

    private final int index;

    public BagSlot(IItemHandlerModifiable itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.bag = itemHandler;
        this.index = index;
    }

    @Override
    public void setChanged() {
        this.bag.setStackInSlot(this.index, this.m_7993_());
    }
}