package org.violetmoon.quark.addons.oddities.inventory.slot;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class CachedItemHandlerSlot extends SlotItemHandler {

    private ItemStack cached = ItemStack.EMPTY;

    private boolean caching = false;

    public CachedItemHandlerSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @NotNull
    @Override
    public ItemStack getItem() {
        return this.caching ? this.cached : super.getItem();
    }

    @NotNull
    @Override
    public ItemStack remove(int amount) {
        if (this.caching) {
            ItemStack newStack = this.cached.copy();
            int trueAmount = Math.min(amount, this.cached.getCount());
            this.cached.shrink(trueAmount);
            newStack.setCount(trueAmount);
            return newStack;
        } else {
            return super.remove(amount);
        }
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        super.set(stack);
        if (this.caching) {
            this.cached = stack;
        }
    }

    public static void cache(AbstractContainerMenu container) {
        for (Slot slot : container.slots) {
            if (slot instanceof CachedItemHandlerSlot thisSlot) {
                thisSlot.cached = slot.getItem();
                thisSlot.caching = true;
            }
        }
    }

    public static void applyCache(AbstractContainerMenu container) {
        for (Slot slot : container.slots) {
            if (slot instanceof CachedItemHandlerSlot) {
                CachedItemHandlerSlot thisSlot = (CachedItemHandlerSlot) slot;
                if (thisSlot.caching) {
                    slot.set(thisSlot.cached);
                    thisSlot.caching = false;
                }
            }
        }
    }
}