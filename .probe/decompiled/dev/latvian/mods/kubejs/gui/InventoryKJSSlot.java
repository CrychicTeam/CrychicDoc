package dev.latvian.mods.kubejs.gui;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryKJSSlot extends Slot {

    public final InventoryKJS inventory;

    public final int invIndex;

    public InventoryKJSSlot(InventoryKJS inventory, int invIndex, int xPosition, int yPosition) {
        super(KubeJSGUI.EMPTY_CONTAINER, invIndex, xPosition, yPosition);
        this.inventory = inventory;
        this.invIndex = invIndex;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.isEmpty() ? false : this.inventory.kjs$isItemValid(this.invIndex, stack);
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        return this.inventory.kjs$getStackInSlot(this.invIndex);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        this.inventory.kjs$setStackInSlot(this.invIndex, stack);
        this.setChanged();
    }

    @Override
    public void onQuickCraft(@NotNull ItemStack oldStackIn, @NotNull ItemStack newStackIn) {
    }

    @Override
    public void setChanged() {
        this.inventory.kjs$setChanged();
    }

    @Override
    public int getMaxStackSize() {
        return this.inventory.kjs$getSlotLimit(this.invIndex);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);
        ItemStack currentStack = this.inventory.kjs$getStackInSlot(this.invIndex);
        if (this.inventory.kjs$isMutable()) {
            this.inventory.kjs$setStackInSlot(this.invIndex, ItemStack.EMPTY);
            ItemStack remainder = this.inventory.kjs$insertItem(this.invIndex, maxAdd, true);
            this.inventory.kjs$setStackInSlot(this.invIndex, currentStack);
            return maxInput - remainder.getCount();
        } else {
            ItemStack remainder = this.inventory.kjs$insertItem(this.invIndex, maxAdd, true);
            int current = currentStack.getCount();
            int added = maxInput - remainder.getCount();
            return current + added;
        }
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return !this.inventory.kjs$extractItem(this.invIndex, 1, true).isEmpty();
    }

    @NotNull
    @Override
    public ItemStack remove(int amount) {
        return this.inventory.kjs$extractItem(this.invIndex, amount, false);
    }
}