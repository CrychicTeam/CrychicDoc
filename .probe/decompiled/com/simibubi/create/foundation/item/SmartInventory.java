package com.simibubi.create.foundation.item;

import com.simibubi.create.foundation.blockEntity.SyncedBlockEntity;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class SmartInventory extends RecipeWrapper implements IItemHandlerModifiable, INBTSerializable<CompoundTag> {

    protected boolean extractionAllowed;

    protected boolean insertionAllowed;

    protected boolean stackNonStackables;

    protected SmartInventory.SyncedStackHandler wrapped;

    protected int stackSize;

    public SmartInventory(int slots, SyncedBlockEntity be) {
        this(slots, be, 64, false);
    }

    public SmartInventory(int slots, SyncedBlockEntity be, int stackSize, boolean stackNonStackables) {
        super(new SmartInventory.SyncedStackHandler(slots, be, stackNonStackables, stackSize));
        this.stackNonStackables = stackNonStackables;
        this.insertionAllowed = true;
        this.extractionAllowed = true;
        this.stackSize = stackSize;
        this.wrapped = (SmartInventory.SyncedStackHandler) this.inv;
    }

    public SmartInventory withMaxStackSize(int maxStackSize) {
        this.stackSize = maxStackSize;
        this.wrapped.stackSize = maxStackSize;
        return this;
    }

    public SmartInventory whenContentsChanged(Consumer<Integer> updateCallback) {
        ((SmartInventory.SyncedStackHandler) this.inv).whenContentsChange(updateCallback);
        return this;
    }

    public SmartInventory allowInsertion() {
        this.insertionAllowed = true;
        return this;
    }

    public SmartInventory allowExtraction() {
        this.extractionAllowed = true;
        return this;
    }

    public SmartInventory forbidInsertion() {
        this.insertionAllowed = false;
        return this;
    }

    public SmartInventory forbidExtraction() {
        this.extractionAllowed = false;
        return this;
    }

    @Override
    public int getSlots() {
        return this.inv.getSlots();
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return !this.insertionAllowed ? stack : this.inv.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!this.extractionAllowed) {
            return ItemStack.EMPTY;
        } else {
            if (this.stackNonStackables) {
                ItemStack extractItem = this.inv.extractItem(slot, amount, true);
                if (!extractItem.isEmpty() && extractItem.getMaxStackSize() < extractItem.getCount()) {
                    amount = extractItem.getMaxStackSize();
                }
            }
            return this.inv.extractItem(slot, amount, simulate);
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return Math.min(this.inv.getSlotLimit(slot), this.stackSize);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return this.inv.isItemValid(slot, stack);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inv.getStackInSlot(slot);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        this.inv.setStackInSlot(slot, stack);
    }

    public int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
    }

    public CompoundTag serializeNBT() {
        return this.getInv().serializeNBT();
    }

    public void deserializeNBT(CompoundTag nbt) {
        this.getInv().deserializeNBT(nbt);
    }

    private SmartInventory.SyncedStackHandler getInv() {
        return (SmartInventory.SyncedStackHandler) this.inv;
    }

    private static class SyncedStackHandler extends ItemStackHandler {

        private SyncedBlockEntity blockEntity;

        private boolean stackNonStackables;

        private int stackSize;

        private Consumer<Integer> updateCallback;

        public SyncedStackHandler(int slots, SyncedBlockEntity be, boolean stackNonStackables, int stackSize) {
            super(slots);
            this.blockEntity = be;
            this.stackNonStackables = stackNonStackables;
            this.stackSize = stackSize;
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            if (this.updateCallback != null) {
                this.updateCallback.accept(slot);
            }
            this.blockEntity.notifyUpdate();
        }

        @Override
        public int getSlotLimit(int slot) {
            return Math.min(this.stackNonStackables ? 64 : super.getSlotLimit(slot), this.stackSize);
        }

        public void whenContentsChange(Consumer<Integer> updateCallback) {
            this.updateCallback = updateCallback;
        }
    }
}