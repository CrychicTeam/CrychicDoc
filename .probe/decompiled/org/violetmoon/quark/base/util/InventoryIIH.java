package org.violetmoon.quark.base.util;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class InventoryIIH implements IItemHandlerModifiable {

    private final IItemHandlerModifiable iih;

    final ItemStack stack;

    public InventoryIIH(ItemStack stack) {
        this.stack = stack;
        LazyOptional<IItemHandler> opt = stack.getCapability(ForgeCapabilities.ITEM_HANDLER, null);
        if (opt.isPresent()) {
            IItemHandler handler = opt.orElse(null);
            if (handler instanceof IItemHandlerModifiable) {
                this.iih = (IItemHandlerModifiable) handler;
            } else {
                this.iih = null;
            }
        } else {
            this.iih = null;
        }
        if (this.iih == null) {
            throw new RuntimeException("Can't load InventoryIIH without a proper IItemHandlerModifiable");
        }
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.iih.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return this.iih.getSlots();
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.iih.getStackInSlot(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.iih.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.iih.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.iih.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.iih.isItemValid(slot, stack);
    }
}