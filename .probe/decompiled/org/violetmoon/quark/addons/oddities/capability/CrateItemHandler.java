package org.violetmoon.quark.addons.oddities.capability;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.addons.oddities.module.CrateModule;
import org.violetmoon.quark.base.handler.SortingHandler;

public class CrateItemHandler extends ItemStackHandler {

    private boolean needsUpdate = false;

    private final int maxItems;

    public int displayTotal = 0;

    public int displaySlots = 0;

    private int cachedTotal = -1;

    public CrateItemHandler() {
        super(CrateModule.maxItems);
        this.maxItems = CrateModule.maxItems;
    }

    private int getTotal() {
        if (this.cachedTotal != -1) {
            return this.cachedTotal;
        } else {
            int items = 0;
            for (ItemStack stack : this.stacks) {
                items += stack.getCount();
            }
            this.cachedTotal = items;
            return items;
        }
    }

    private void changeTotal(ItemStack oldStack, ItemStack newStack) {
        int diff = newStack.getCount() - oldStack.getCount();
        if (diff != 0) {
            this.changeTotal(diff);
        }
    }

    private void changeTotal(int change) {
        this.cachedTotal = this.getTotal() + change;
    }

    public void recalculate() {
        if (this.needsUpdate) {
            this.needsUpdate = false;
            this.displayTotal = 0;
            this.displaySlots = 0;
            NonNullList<ItemStack> newStacks = NonNullList.withSize(this.maxItems, ItemStack.EMPTY);
            int idx = 0;
            for (ItemStack stack : this.stacks) {
                if (!stack.isEmpty()) {
                    newStacks.set(idx, stack);
                    this.displayTotal = this.displayTotal + stack.getCount();
                    this.displaySlots++;
                    idx++;
                }
            }
            this.stacks = newStacks;
            this.cachedTotal = -1;
        }
    }

    public void clear() {
        this.needsUpdate = false;
        this.stacks = NonNullList.withSize(this.maxItems, ItemStack.EMPTY);
        this.displayTotal = 0;
        this.displaySlots = 0;
    }

    public boolean isEmpty() {
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void spill(Level level, BlockPos worldPosition) {
        List<ItemStack> stacks = new ArrayList(this.stacks);
        SortingHandler.mergeStacks(stacks);
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty()) {
                Containers.dropItemStack(level, (double) worldPosition.m_123341_(), (double) worldPosition.m_123342_(), (double) worldPosition.m_123343_(), stack);
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        ItemStack stackInSlot = this.getStackInSlot(slot);
        int total = this.getTotal();
        return Mth.clamp(stackInSlot.getCount() + this.maxItems - total, 0, 64);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        ItemStack oldStack = this.stacks.get(slot).copy();
        super.setStackInSlot(slot, stack);
        ItemStack newStack = this.stacks.get(slot).copy();
        this.changeTotal(oldStack, newStack);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        ItemStack oldStack = this.stacks.get(slot).copy();
        ItemStack retStack = super.insertItem(slot, stack, simulate);
        ItemStack newStack = this.stacks.get(slot).copy();
        if (!simulate) {
            this.changeTotal(oldStack, newStack);
        }
        return retStack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack oldStack = this.stacks.get(slot).copy();
        ItemStack retStack = super.extractItem(slot, amount, simulate);
        ItemStack newStack = this.stacks.get(slot).copy();
        if (!simulate) {
            this.changeTotal(oldStack, newStack);
        }
        return retStack;
    }

    @Override
    public void onContentsChanged(int slot) {
        this.needsUpdate = true;
    }

    @Override
    protected void onLoad() {
        this.needsUpdate = true;
        this.recalculate();
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag items = new ListTag();
        for (ItemStack stack : this.stacks) {
            if (!stack.isEmpty()) {
                items.add(stack.save(new CompoundTag()));
            }
        }
        CompoundTag nbt = new CompoundTag();
        nbt.put("stacks", items);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.stacks = NonNullList.withSize(this.maxItems, ItemStack.EMPTY);
        ListTag items = nbt.getList("stacks", 10);
        for (int i = 0; i < items.size(); i++) {
            this.stacks.set(i, ItemStack.of(items.getCompound(i)));
        }
        this.onLoad();
    }
}