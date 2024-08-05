package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.level.BlockContainerJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface InventoryKJS {

    default boolean kjs$isMutable() {
        return false;
    }

    default int kjs$getSlots() {
        throw new NoMixinException();
    }

    default ItemStack kjs$getStackInSlot(int slot) {
        throw new NoMixinException();
    }

    default void kjs$setStackInSlot(int slot, ItemStack stack) {
        throw new IllegalStateException("This item handler can't be modified directly! Use insertItem or extractItem instead!");
    }

    default ItemStack kjs$insertItem(int slot, ItemStack stack, boolean simulate) {
        throw new NoMixinException();
    }

    default ItemStack kjs$extractItem(int slot, int amount, boolean simulate) {
        throw new NoMixinException();
    }

    default ItemStack kjs$insertItem(ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return stack;
        } else {
            for (int i = 0; i < this.kjs$getSlots(); i++) {
                stack = this.kjs$insertItem(i, stack, simulate);
                if (stack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            return stack;
        }
    }

    default int kjs$getSlotLimit(int slot) {
        throw new NoMixinException();
    }

    default boolean kjs$isItemValid(int slot, ItemStack stack) {
        throw new NoMixinException();
    }

    default int kjs$getWidth() {
        return Math.min(this.kjs$getSlots(), 9);
    }

    default int kjs$getHeight() {
        return (this.kjs$getSlots() + 8) / 9;
    }

    default void kjs$clear() {
        for (int i = this.kjs$getSlots(); i >= 0; i--) {
            if (this.kjs$isMutable()) {
                this.kjs$setStackInSlot(i, ItemStack.EMPTY);
            } else {
                this.kjs$extractItem(i, this.kjs$getStackInSlot(i).getCount(), false);
            }
        }
    }

    default void kjs$clear(Ingredient ingredient) {
        if (ingredient.kjs$isWildcard()) {
            this.kjs$clear();
        }
        for (int i = this.kjs$getSlots(); i >= 0; i--) {
            if (ingredient.test(this.kjs$getStackInSlot(i))) {
                if (this.kjs$isMutable()) {
                    this.kjs$setStackInSlot(i, ItemStack.EMPTY);
                } else {
                    this.kjs$extractItem(i, this.kjs$getStackInSlot(i).getCount(), false);
                }
            }
        }
    }

    default int kjs$find() {
        for (int i = 0; i < this.kjs$getSlots(); i++) {
            ItemStack stack1 = this.kjs$getStackInSlot(i);
            if (!stack1.isEmpty()) {
                return i;
            }
        }
        return -1;
    }

    default int kjs$find(Ingredient ingredient) {
        if (ingredient.kjs$isWildcard()) {
            return this.kjs$find();
        } else {
            for (int i = 0; i < this.kjs$getSlots(); i++) {
                ItemStack stack1 = this.kjs$getStackInSlot(i);
                if (ingredient.test(stack1)) {
                    return i;
                }
            }
            return -1;
        }
    }

    default int kjs$count() {
        int count = 0;
        for (int i = 0; i < this.kjs$getSlots(); i++) {
            count += this.kjs$getStackInSlot(i).getCount();
        }
        return count;
    }

    default int kjs$count(Ingredient ingredient) {
        if (ingredient.kjs$isWildcard()) {
            return this.kjs$count();
        } else {
            int count = 0;
            for (int i = 0; i < this.kjs$getSlots(); i++) {
                ItemStack stack1 = this.kjs$getStackInSlot(i);
                if (ingredient.test(stack1)) {
                    count += stack1.getCount();
                }
            }
            return count;
        }
    }

    default int kjs$countNonEmpty() {
        int count = 0;
        for (int i = 0; i < this.kjs$getSlots(); i++) {
            if (!this.kjs$getStackInSlot(i).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    default int kjs$countNonEmpty(Ingredient ingredient) {
        if (ingredient.kjs$isWildcard()) {
            return this.kjs$countNonEmpty();
        } else {
            int count = 0;
            for (int i = 0; i < this.kjs$getSlots(); i++) {
                ItemStack stack1 = this.kjs$getStackInSlot(i);
                if (ingredient.test(stack1)) {
                    count++;
                }
            }
            return count;
        }
    }

    default boolean kjs$isEmpty() {
        for (int i = 0; i < this.kjs$getSlots(); i++) {
            if (!this.kjs$getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    default void kjs$setChanged() {
    }

    @Nullable
    default BlockContainerJS kjs$getBlock(Level level) {
        return null;
    }

    default List<ItemStack> kjs$getAllItems() {
        ArrayList<ItemStack> list = new ArrayList();
        for (int i = 0; i < this.kjs$getSlots(); i++) {
            ItemStack is = this.kjs$getStackInSlot(i);
            if (!is.isEmpty()) {
                list.add(is);
            }
        }
        return list;
    }

    default Container kjs$asContainer() {
        return null;
    }
}