package dev.latvian.mods.kubejs.item;

import com.google.common.base.Preconditions;
import dev.latvian.mods.kubejs.core.InventoryKJS;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class RangedWrapper implements InventoryKJS {

    private final InventoryKJS compose;

    private final int minSlot;

    private final int maxSlot;

    public RangedWrapper(InventoryKJS compose, int minSlot, int maxSlotExclusive) {
        Preconditions.checkArgument(maxSlotExclusive > minSlot, "Max slot must be greater than min slot");
        this.compose = compose;
        this.minSlot = minSlot;
        this.maxSlot = maxSlotExclusive;
    }

    @Override
    public boolean kjs$isMutable() {
        return this.compose.kjs$isMutable();
    }

    @Override
    public int kjs$getSlots() {
        return this.maxSlot - this.minSlot;
    }

    @NotNull
    @Override
    public ItemStack kjs$getStackInSlot(int slot) {
        return this.checkSlot(slot) ? this.compose.kjs$getStackInSlot(slot + this.minSlot) : ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack kjs$insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return this.checkSlot(slot) ? this.compose.kjs$insertItem(slot + this.minSlot, stack, simulate) : stack;
    }

    @NotNull
    @Override
    public ItemStack kjs$extractItem(int slot, int amount, boolean simulate) {
        return this.checkSlot(slot) ? this.compose.kjs$extractItem(slot + this.minSlot, amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    public void kjs$setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (this.checkSlot(slot)) {
            this.compose.kjs$setStackInSlot(slot + this.minSlot, stack);
        }
    }

    @Override
    public int kjs$getSlotLimit(int slot) {
        return this.checkSlot(slot) ? this.compose.kjs$getSlotLimit(slot + this.minSlot) : 0;
    }

    @Override
    public boolean kjs$isItemValid(int slot, @NotNull ItemStack stack) {
        return this.checkSlot(slot) ? this.compose.kjs$isItemValid(slot + this.minSlot, stack) : false;
    }

    private boolean checkSlot(int localSlot) {
        return localSlot + this.minSlot < this.maxSlot;
    }
}