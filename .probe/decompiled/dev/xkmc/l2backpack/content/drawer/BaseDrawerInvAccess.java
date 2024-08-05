package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2backpack.content.capability.PickupConfig;
import dev.xkmc.l2backpack.content.capability.PickupMode;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

public interface BaseDrawerInvAccess extends IItemHandlerModifiable {

    BaseDrawerItem drawerItem();

    ItemStack drawerStack();

    ServerPlayer player();

    int getStoredCount();

    boolean isEmpty();

    default Item getStoredItem() {
        return BaseDrawerItem.getItem(this.drawerStack());
    }

    default ItemStack getStoredStack() {
        return !this.isEmpty() && this.getStoredCount() != 0 ? new ItemStack(this.getStoredItem(), this.getStoredCount()) : ItemStack.EMPTY;
    }

    default void setStoredItem(Item item) {
        this.drawerItem().setItem(this.drawerStack(), item, this.player());
    }

    void setStoredCount(int var1);

    default boolean isItemValid(ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        } else if (stack.hasTag()) {
            return false;
        } else {
            return this.isEmpty() ? true : this.getStoredItem() == stack.getItem();
        }
    }

    @Override
    default int getSlots() {
        return 1;
    }

    @Override
    default int getSlotLimit(int slot) {
        Item item = this.getStoredItem();
        return BaseDrawerItem.getStacking(this.drawerStack()) * item.getMaxStackSize();
    }

    default int getMax(ItemStack stack) {
        return BaseDrawerItem.getStacking(this.drawerStack()) * stack.getMaxStackSize();
    }

    @Override
    default boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.isItemValid(stack);
    }

    @Override
    default void setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.setStoredItem(stack.getItem());
        this.setStoredCount(stack.getCount());
    }

    @NotNull
    @Override
    default ItemStack getStackInSlot(int slot) {
        return this.getStoredStack();
    }

    @NotNull
    @Override
    default ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return stack;
        } else if (!this.isItemValid(stack)) {
            return stack;
        } else {
            boolean empty = this.isEmpty();
            int current = empty ? 0 : this.getStoredCount();
            int input = stack.getCount();
            int allow = Math.min(this.getMax(stack) - current, input);
            if (!simulate) {
                if (empty) {
                    this.setStoredItem(stack.getItem());
                }
                this.setStoredCount(current + allow);
            }
            return input == allow ? ItemStack.EMPTY : ItemHandlerHelper.copyStackWithSize(stack, input - allow);
        }
    }

    @NotNull
    @Override
    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.drawerItem().takeItem(this.drawerStack(), amount, this.player(), simulate);
    }

    default boolean mayStack(BaseDrawerInvAccess inv, int slot, ItemStack stack, PickupConfig config) {
        return config.pickup() == PickupMode.ALL && this.isEmpty() ? !stack.hasTag() : !this.isEmpty() && this.isItemValid(stack);
    }
}