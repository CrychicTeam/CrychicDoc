package dev.xkmc.l2backpack.content.remote.drawer;

import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import dev.xkmc.l2backpack.content.drawer.IDrawerHandler;
import dev.xkmc.l2backpack.content.remote.common.DrawerAccess;
import dev.xkmc.l2backpack.init.advancement.BackpackTriggers;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public record EnderDrawerItemHandler(DrawerAccess access, boolean logistics) implements IDrawerHandler {

    @Override
    public int getSlots() {
        return 2;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 1 ? ItemStack.EMPTY : new ItemStack(this.access.item(), this.access.getCount());
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (!stack.hasTag() && stack.getItem() == this.access.item()) {
            int count = this.access.getCount();
            int max = this.access.item().getMaxStackSize() * BaseDrawerItem.getStacking();
            int insert = Math.min(max - count, stack.getCount());
            if (!simulate) {
                this.access.setCount(count + insert);
                if (this.logistics && insert > 0) {
                    this.access.getOwner().ifPresent(BackpackTriggers.REMOTE::trigger);
                }
            }
            return insert == stack.getCount() ? ItemStack.EMPTY : new ItemStack(this.access.item(), stack.getCount() - insert);
        } else {
            return stack;
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        int count = this.access.getCount();
        int take = Math.min(count, amount);
        if (!simulate) {
            this.access.setCount(count - take);
            if (this.logistics && take > 0) {
                this.access.getOwner().ifPresent(BackpackTriggers.REMOTE::trigger);
            }
        }
        return new ItemStack(this.access.item(), take);
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64 * this.access.item().getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return !stack.hasTag() && stack.getItem() == this.access.item();
    }
}