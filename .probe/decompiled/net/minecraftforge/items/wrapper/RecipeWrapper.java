package net.minecraftforge.items.wrapper;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class RecipeWrapper implements Container {

    protected final IItemHandlerModifiable inv;

    public RecipeWrapper(IItemHandlerModifiable inv) {
        this.inv = inv;
    }

    @Override
    public int getContainerSize() {
        return this.inv.getSlots();
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.inv.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = this.inv.getStackInSlot(slot);
        return stack.isEmpty() ? ItemStack.EMPTY : stack.split(count);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.inv.setStackInSlot(slot, stack);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        ItemStack s = this.getItem(index);
        if (s.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.setItem(index, ItemStack.EMPTY);
            return s;
        }
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < this.inv.getSlots(); i++) {
            if (!this.inv.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return this.inv.isItemValid(slot, stack);
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < this.inv.getSlots(); i++) {
            this.inv.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void startOpen(Player player) {
    }

    @Override
    public void stopOpen(Player player) {
    }
}