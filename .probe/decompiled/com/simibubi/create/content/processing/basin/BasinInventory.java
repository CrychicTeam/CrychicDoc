package com.simibubi.create.content.processing.basin;

import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public class BasinInventory extends SmartInventory {

    private BasinBlockEntity blockEntity;

    public BasinInventory(int slots, BasinBlockEntity be) {
        super(slots, be, 16, true);
        this.blockEntity = be;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        for (int i = 0; i < this.getSlots(); i++) {
            if (i != slot && ItemHandlerHelper.canItemStacksStack(stack, this.inv.getStackInSlot(i))) {
                return stack;
            }
        }
        return super.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extractItem = super.extractItem(slot, amount, simulate);
        if (!simulate && !extractItem.isEmpty()) {
            this.blockEntity.notifyChangeOfContents();
        }
        return extractItem;
    }
}