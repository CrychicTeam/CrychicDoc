package com.simibubi.create.content.logistics.tunnel;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;

public class BrassTunnelItemHandler implements IItemHandler {

    private BrassTunnelBlockEntity blockEntity;

    public BrassTunnelItemHandler(BrassTunnelBlockEntity be) {
        this.blockEntity = be;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.blockEntity.stackToDistribute;
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (!this.blockEntity.hasDistributionBehaviour()) {
            LazyOptional<IItemHandler> beltCapability = this.blockEntity.getBeltCapability();
            return !beltCapability.isPresent() ? stack : beltCapability.orElse(null).insertItem(slot, stack, simulate);
        } else if (!this.blockEntity.canTakeItems()) {
            return stack;
        } else {
            if (!simulate) {
                this.blockEntity.setStackToDistribute(stack, null);
            }
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        LazyOptional<IItemHandler> beltCapability = this.blockEntity.getBeltCapability();
        return !beltCapability.isPresent() ? ItemStack.EMPTY : beltCapability.orElse(null).extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.blockEntity.stackToDistribute.isEmpty() ? 64 : this.blockEntity.stackToDistribute.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }
}