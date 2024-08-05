package com.mna.inventory.stack_extension;

import com.mna.gui.containers.IExtendedItemHandler;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nonnull;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class AbstractItemHandler implements IExtendedItemHandler {

    protected NonNullList<ItemStackEntry> stacks;

    protected Set<IInventoryListener> listeners = new HashSet();

    public AbstractItemHandler() {
        this(1);
    }

    public AbstractItemHandler(int size) {
        this.stacks = NonNullList.withSize(size, ItemStackEntry.EMPTY);
    }

    public AbstractItemHandler(NonNullList<ItemStackEntry> stacks) {
        this.stacks = stacks;
    }

    @Override
    public void enlarge(int size) {
        if (size < this.stacks.size()) {
            throw new RuntimeException("Cannot reduce the size of an AbstractItemHandler, currently contains: " + this.stacks.size() + " slots, cannot reduce to " + size + " slots");
        } else if (size != this.stacks.size()) {
            NonNullList<ItemStackEntry> newList = NonNullList.withSize(size, ItemStackEntry.EMPTY);
            for (int i = 0; i < this.stacks.size(); i++) {
                newList.set(i, this.stacks.get(i));
            }
            this.stacks = newList;
        }
    }

    @Override
    public abstract int getSlotLimit(int var1);

    public int getSlotLimit(int slot, ItemStack stack) {
        return this.getSlotLimit(slot);
    }

    @Override
    public int size() {
        return this.stacks.size();
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.validateSlotIndex(slot);
        this.stacks.set(slot, new ItemStackEntry(stack));
        this.onContentsChanged(slot);
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.stacks.get(slot).getStackOriginal();
    }

    @Override
    public long getCountInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.stacks.get(slot).getCount();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (!this.isItemValid(slot, stack)) {
            return stack;
        } else {
            this.validateSlotIndex(slot);
            ItemStackEntry entry = this.stacks.get(slot);
            ItemStack existing = entry.getStackOriginal();
            int limit = this.getSlotLimit(slot, stack);
            if (!existing.isEmpty()) {
                if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
                    return stack;
                }
                limit -= existing.getCount();
            }
            if (limit <= 0) {
                return stack;
            } else {
                boolean reachedLimit = stack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        if (reachedLimit) {
                            entry = new ItemStackEntry(stack.copy());
                            entry.setCount((long) limit);
                            this.stacks.set(slot, entry);
                        } else {
                            entry = new ItemStackEntry(stack);
                            this.stacks.set(slot, entry);
                        }
                    } else {
                        entry.grow((long) stack.getCount());
                    }
                    this.onContentsChanged(slot);
                }
                return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStackEntry entry = this.stacks.get(slot);
            ItemStack existing = entry.getStackOriginal();
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxStackSize());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        this.stacks.set(slot, ItemStackEntry.EMPTY);
                        this.onContentsChanged(slot);
                        return existing;
                    } else {
                        return existing.copy();
                    }
                } else {
                    if (!simulate) {
                        entry = new ItemStackEntry(ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                        this.stacks.set(slot, entry);
                        this.onContentsChanged(slot);
                    }
                    return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
                }
            }
        }
    }

    @Override
    public abstract boolean isItemValid(int var1, @Nonnull ItemStack var2);

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.stacks.size()) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.stacks.size() + ")");
        }
    }

    public void onContentsChanged(int slot) {
        for (IInventoryListener listener : this.listeners) {
            listener.inventoryChanged(this, slot);
        }
    }

    @Override
    public void addListener(IInventoryListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public CompoundTag serialize() {
        CompoundTag result = new CompoundTag();
        result.putInt("slots", this.size());
        for (int i = 0; i < this.size(); i++) {
            ItemStackEntry entry = this.stacks.get(i);
            if (!entry.isEmpty()) {
                result.put(i + "", entry.serialize());
            }
        }
        return result;
    }

    @Override
    public void deserialize(CompoundTag result) {
        int size = result.getInt("slots");
        this.enlarge(Math.max(size, this.size()));
        for (int i = 0; i < size; i++) {
            ItemStackEntry entry = ItemStackEntry.deserialize(result.get(i + ""));
            this.stacks.set(i, entry);
        }
    }

    public boolean isEmpty() {
        for (int i = 0; i < this.size(); i++) {
            if (!this.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}