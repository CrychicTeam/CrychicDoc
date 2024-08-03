package com.simibubi.create.content.kinetics.belt.transport;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ItemHandlerBeltSegment implements IItemHandler {

    private final BeltInventory beltInventory;

    int offset;

    public ItemHandlerBeltSegment(BeltInventory beltInventory, int offset) {
        this.beltInventory = beltInventory;
        this.offset = offset;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        TransportedItemStack stackAtOffset = this.beltInventory.getStackAtOffset(this.offset);
        return stackAtOffset == null ? ItemStack.EMPTY : stackAtOffset.stack;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (this.beltInventory.canInsertAt(this.offset)) {
            if (!simulate) {
                TransportedItemStack newStack = new TransportedItemStack(stack);
                newStack.insertedAt = this.offset;
                newStack.beltPosition = (float) this.offset + 0.5F + (float) (this.beltInventory.beltMovementPositive ? -1 : 1) / 16.0F;
                newStack.prevBeltPosition = newStack.beltPosition;
                this.beltInventory.addItem(newStack);
                this.beltInventory.belt.m_6596_();
                this.beltInventory.belt.sendData();
            }
            return ItemStack.EMPTY;
        } else {
            return stack;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        TransportedItemStack transported = this.beltInventory.getStackAtOffset(this.offset);
        if (transported == null) {
            return ItemStack.EMPTY;
        } else {
            amount = Math.min(amount, transported.stack.getCount());
            ItemStack extracted = simulate ? transported.stack.copy().split(amount) : transported.stack.split(amount);
            if (!simulate) {
                if (transported.stack.isEmpty()) {
                    this.beltInventory.toRemove.add(transported);
                }
                this.beltInventory.belt.m_6596_();
                this.beltInventory.belt.sendData();
            }
            return extracted;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return Math.min(this.getStackInSlot(slot).getMaxStackSize(), 64);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}