package com.simibubi.create.foundation.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class ItemHandlerWrapper implements IItemHandlerModifiable {

    private IItemHandlerModifiable wrapped;

    public ItemHandlerWrapper(IItemHandlerModifiable wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int getSlots() {
        return this.wrapped.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.wrapped.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return this.wrapped.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.wrapped.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.wrapped.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.wrapped.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.wrapped.setStackInSlot(slot, stack);
    }
}