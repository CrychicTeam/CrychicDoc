package net.minecraftforge.items.wrapper;

import java.util.function.IntUnaryOperator;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BrewingStandBlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SidedInvWrapper implements IItemHandlerModifiable {

    protected final WorldlyContainer inv;

    @Nullable
    protected final Direction side;

    private final IntUnaryOperator slotLimit;

    private final SidedInvWrapper.InsertLimit newStackInsertLimit;

    public static LazyOptional<IItemHandlerModifiable>[] create(WorldlyContainer inv, Direction... sides) {
        LazyOptional<IItemHandlerModifiable>[] ret = new LazyOptional[sides.length];
        for (int x = 0; x < sides.length; x++) {
            Direction side = sides[x];
            ret[x] = LazyOptional.of(() -> new SidedInvWrapper(inv, side));
        }
        return ret;
    }

    public SidedInvWrapper(WorldlyContainer inv, @Nullable Direction side) {
        this.inv = inv;
        this.side = side;
        if (inv instanceof BrewingStandBlockEntity) {
            this.slotLimit = wrapperSlot -> getSlot(inv, wrapperSlot, side) < 3 ? 1 : inv.m_6893_();
        } else {
            this.slotLimit = wrapperSlot -> inv.m_6893_();
        }
        if (inv instanceof AbstractFurnaceBlockEntity) {
            this.newStackInsertLimit = (wrapperSlot, invSlot, stack) -> invSlot == 1 && stack.is(Items.BUCKET) ? 1 : Math.min(stack.getMaxStackSize(), this.getSlotLimit(wrapperSlot));
        } else {
            this.newStackInsertLimit = (wrapperSlot, invSlot, stack) -> Math.min(stack.getMaxStackSize(), this.getSlotLimit(wrapperSlot));
        }
    }

    public static int getSlot(WorldlyContainer inv, int slot, @Nullable Direction side) {
        int[] slots = inv.getSlotsForFace(side);
        return slot < slots.length ? slots[slot] : -1;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            SidedInvWrapper that = (SidedInvWrapper) o;
            return this.inv.equals(that.inv) && this.side == that.side;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.inv.hashCode();
        return 31 * result + (this.side == null ? 0 : this.side.hashCode());
    }

    @Override
    public int getSlots() {
        return this.inv.getSlotsForFace(this.side).length;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        int i = getSlot(this.inv, slot, this.side);
        return i == -1 ? ItemStack.EMPTY : this.inv.m_8020_(i);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            int slot1 = getSlot(this.inv, slot, this.side);
            if (slot1 == -1) {
                return stack;
            } else {
                ItemStack stackInSlot = this.inv.m_8020_(slot1);
                if (!stackInSlot.isEmpty()) {
                    if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), this.getSlotLimit(slot))) {
                        return stack;
                    } else if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
                        return stack;
                    } else if (this.inv.canPlaceItemThroughFace(slot1, stack, this.side) && this.inv.m_7013_(slot1, stack)) {
                        int m = Math.min(stack.getMaxStackSize(), this.getSlotLimit(slot)) - stackInSlot.getCount();
                        if (stack.getCount() <= m) {
                            if (!simulate) {
                                ItemStack copy = stack.copy();
                                copy.grow(stackInSlot.getCount());
                                this.setInventorySlotContents(slot1, copy);
                            }
                            return ItemStack.EMPTY;
                        } else {
                            stack = stack.copy();
                            if (!simulate) {
                                ItemStack copy = stack.split(m);
                                copy.grow(stackInSlot.getCount());
                                this.setInventorySlotContents(slot1, copy);
                                return stack;
                            } else {
                                stack.shrink(m);
                                return stack;
                            }
                        }
                    } else {
                        return stack;
                    }
                } else if (this.inv.canPlaceItemThroughFace(slot1, stack, this.side) && this.inv.m_7013_(slot1, stack)) {
                    int m = this.newStackInsertLimit.limitInsert(slot, slot1, stack);
                    if (m < stack.getCount()) {
                        stack = stack.copy();
                        if (!simulate) {
                            this.setInventorySlotContents(slot1, stack.split(m));
                            return stack;
                        } else {
                            stack.shrink(m);
                            return stack;
                        }
                    } else {
                        if (!simulate) {
                            this.setInventorySlotContents(slot1, stack);
                        }
                        return ItemStack.EMPTY;
                    }
                } else {
                    return stack;
                }
            }
        }
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        int slot1 = getSlot(this.inv, slot, this.side);
        if (slot1 != -1) {
            this.setInventorySlotContents(slot1, stack);
        }
    }

    private void setInventorySlotContents(int slot, ItemStack stack) {
        this.inv.m_6596_();
        this.inv.m_6836_(slot, stack);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            int slot1 = getSlot(this.inv, slot, this.side);
            if (slot1 == -1) {
                return ItemStack.EMPTY;
            } else {
                ItemStack stackInSlot = this.inv.m_8020_(slot1);
                if (stackInSlot.isEmpty()) {
                    return ItemStack.EMPTY;
                } else if (!this.inv.canTakeItemThroughFace(slot1, stackInSlot, this.side)) {
                    return ItemStack.EMPTY;
                } else if (simulate) {
                    if (stackInSlot.getCount() < amount) {
                        return stackInSlot.copy();
                    } else {
                        ItemStack copy = stackInSlot.copy();
                        copy.setCount(amount);
                        return copy;
                    }
                } else {
                    int m = Math.min(stackInSlot.getCount(), amount);
                    ItemStack ret = this.inv.m_7407_(slot1, m);
                    this.inv.m_6596_();
                    return ret;
                }
            }
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.slotLimit.applyAsInt(slot);
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        int slot1 = getSlot(this.inv, slot, this.side);
        return slot1 == -1 ? false : this.inv.m_7013_(slot1, stack);
    }

    private interface InsertLimit {

        int limitInsert(int var1, int var2, ItemStack var3);
    }
}