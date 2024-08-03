package com.simibubi.create.content.logistics.chute;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ChuteItemHandler implements IItemHandler {

    private ChuteBlockEntity blockEntity;

    public ChuteItemHandler(ChuteBlockEntity be) {
        this.blockEntity = be;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.blockEntity.item;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!this.blockEntity.canAcceptItem(stack)) {
            return stack;
        } else {
            if (!simulate) {
                this.blockEntity.setItem(stack);
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack remainder = this.blockEntity.item.copy();
        ItemStack split = remainder.split(amount);
        if (!simulate) {
            this.blockEntity.setItem(remainder);
        }
        return split;
    }

    @Override
    public int getSlotLimit(int slot) {
        return Math.min(64, this.getStackInSlot(slot).getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}