package org.violetmoon.quark.base.handler;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class ProxiedItemStackHandler implements IItemHandler, IItemHandlerModifiable, ICapabilityProvider {

    protected final ItemStack stack;

    protected final String key;

    protected final int size;

    public ProxiedItemStackHandler(ItemStack stack) {
        this(stack, "Inventory", 1);
    }

    public ProxiedItemStackHandler(ItemStack stack, String key) {
        this(stack, key, 1);
    }

    public ProxiedItemStackHandler(ItemStack stack, int size) {
        this(stack, "Inventory", size);
    }

    public ProxiedItemStackHandler(ItemStack stack, String key, int size) {
        this.stack = stack;
        this.key = key;
        this.size = size;
    }

    private ListTag getStackList() {
        ListTag list = ItemNBTHelper.getList(this.stack, this.key, 10, true);
        if (list == null) {
            ItemNBTHelper.setList(this.stack, this.key, list = new ListTag());
        }
        while (list.size() < this.size) {
            list.add(new CompoundTag());
        }
        return list;
    }

    private void writeStack(int index, @NotNull ItemStack stack) {
        this.getStackList().set(index, (Tag) stack.serializeNBT());
        this.onContentsChanged(index);
    }

    private ItemStack readStack(int index) {
        return ItemStack.of(this.getStackList().getCompound(index));
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.validateSlotIndex(slot);
        this.writeStack(slot, stack);
        this.onContentsChanged(slot);
    }

    @Override
    public int getSlots() {
        return this.size;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        this.validateSlotIndex(slot);
        return this.readStack(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.readStack(slot);
            int limit = this.getStackLimit(slot, stack);
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
                    this.writeStack(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
                }
                return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
            }
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            this.validateSlotIndex(slot);
            ItemStack existing = this.readStack(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxStackSize());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        this.writeStack(slot, ItemStack.EMPTY);
                    }
                    return existing;
                } else {
                    if (!simulate) {
                        this.writeStack(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
                    }
                    return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
                }
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    protected int getStackLimit(int slot, @NotNull ItemStack stack) {
        return Math.min(this.getSlotLimit(slot), stack.getMaxStackSize());
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    protected void validateSlotIndex(int slot) {
        if (slot < 0 || slot >= this.size) {
            throw new RuntimeException("Slot " + slot + " not in valid range - [0," + this.size + ")");
        }
    }

    protected void onContentsChanged(int slot) {
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction facing) {
        return ForgeCapabilities.ITEM_HANDLER.orEmpty(capability, LazyOptional.of(() -> this));
    }
}