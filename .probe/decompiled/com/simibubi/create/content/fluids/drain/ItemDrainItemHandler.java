package com.simibubi.create.content.fluids.drain;

import com.simibubi.create.content.fluids.transfer.GenericItemEmptying;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class ItemDrainItemHandler implements IItemHandler {

    private ItemDrainBlockEntity blockEntity;

    private Direction side;

    public ItemDrainItemHandler(ItemDrainBlockEntity be, Direction side) {
        this.blockEntity = be;
        this.side = side;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.blockEntity.getHeldItemStack();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!this.blockEntity.getHeldItemStack().isEmpty()) {
            return stack;
        } else {
            ItemStack returned = ItemStack.EMPTY;
            if (stack.getCount() > 1 && GenericItemEmptying.canItemBeEmptied(this.blockEntity.m_58904_(), stack)) {
                returned = ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - 1);
                stack = ItemHandlerHelper.copyStackWithSize(stack, 1);
            }
            if (!simulate) {
                TransportedItemStack heldItem = new TransportedItemStack(stack);
                heldItem.prevBeltPosition = 0.0F;
                this.blockEntity.setHeldItem(heldItem, this.side.getOpposite());
                this.blockEntity.notifyUpdate();
            }
            return returned;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        TransportedItemStack held = this.blockEntity.heldItem;
        if (held == null) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = held.stack.copy();
            ItemStack extracted = stack.split(amount);
            if (!simulate) {
                this.blockEntity.heldItem.stack = stack;
                if (stack.isEmpty()) {
                    this.blockEntity.heldItem = null;
                }
                this.blockEntity.notifyUpdate();
            }
            return extracted;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}