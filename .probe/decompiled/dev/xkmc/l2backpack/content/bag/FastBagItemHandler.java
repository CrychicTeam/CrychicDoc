package dev.xkmc.l2backpack.content.bag;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public record FastBagItemHandler(AbstractBag bag, ItemStack bagStack, NonNullList<ItemStack> list) implements IItemHandlerModifiable {

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.list.set(slot, stack);
        this.bag.setContent(this.bagStack, this.list);
    }

    @Override
    public int getSlots() {
        return 64;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.list.get(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (this.list.get(slot).isEmpty()) {
            if (!simulate) {
                this.list.set(slot, stack);
                this.bag.setContent(this.bagStack, this.list);
            }
            return ItemStack.EMPTY;
        } else {
            return stack;
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack ans = this.list.get(slot);
            if (!ans.isEmpty() && !simulate) {
                this.list.set(slot, ItemStack.EMPTY);
                this.bag.setContent(this.bagStack, this.list);
            }
            return ans;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.bag.isValidContent(stack);
    }
}