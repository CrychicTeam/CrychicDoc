package dev.xkmc.l2backpack.content.bag;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public record BagItemHandler(AbstractBag bag, ItemStack bagStack) implements IItemHandlerModifiable {

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        NonNullList<ItemStack> list = this.bag.getContent(this.bagStack);
        list.set(slot, stack);
        this.bag.setContent(this.bagStack, list);
    }

    @Override
    public int getSlots() {
        return 64;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.bag.getContent(this.bagStack).get(slot);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        NonNullList<ItemStack> list = this.bag.getContent(this.bagStack);
        if (list.get(slot).isEmpty()) {
            if (!simulate) {
                list.set(slot, stack);
                this.bag.setContent(this.bagStack, list);
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
            NonNullList<ItemStack> list = this.bag.getContent(this.bagStack);
            ItemStack ans = list.get(slot);
            if (!ans.isEmpty() && !simulate) {
                list.set(slot, ItemStack.EMPTY);
                this.bag.setContent(this.bagStack, list);
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

    public FastBagItemHandler toFast() {
        return new FastBagItemHandler(this.bag, this.bagStack, this.bag.getContent(this.bagStack));
    }
}