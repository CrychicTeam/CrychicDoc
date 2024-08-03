package io.github.lightman314.lightmanscurrency.common.menus.containers;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SuppliedItemContainer implements Container {

    private final Supplier<SuppliedItemContainer.IItemInteractable> supplier;

    public SuppliedItemContainer(@Nonnull Supplier<SuppliedItemContainer.IItemInteractable> supplier) {
        this.supplier = supplier;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return this.getItem(0).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack getItem(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        } else {
            SuppliedItemContainer.IItemInteractable interactable = (SuppliedItemContainer.IItemInteractable) this.supplier.get();
            return interactable == null ? ItemStack.EMPTY : interactable.getItem();
        }
    }

    @Nonnull
    @Override
    public ItemStack removeItem(int slot, int count) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        } else {
            SuppliedItemContainer.IItemInteractable interactable = (SuppliedItemContainer.IItemInteractable) this.supplier.get();
            return interactable == null ? ItemStack.EMPTY : interactable.getItem().split(count);
        }
    }

    @Nonnull
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot != 0) {
            return ItemStack.EMPTY;
        } else {
            SuppliedItemContainer.IItemInteractable interactable = (SuppliedItemContainer.IItemInteractable) this.supplier.get();
            if (interactable == null) {
                return ItemStack.EMPTY;
            } else {
                ItemStack result = interactable.getItem();
                interactable.setItem(ItemStack.EMPTY);
                return result;
            }
        }
    }

    @Override
    public void setItem(int slot, @Nonnull ItemStack stack) {
        if (slot == 0) {
            SuppliedItemContainer.IItemInteractable interactable = (SuppliedItemContainer.IItemInteractable) this.supplier.get();
            if (interactable != null) {
                interactable.setItem(stack);
            }
        }
    }

    @Override
    public void setChanged() {
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
    }

    @Override
    public void clearContent() {
    }

    public interface IItemInteractable {

        @Nonnull
        ItemStack getItem();

        void setItem(@Nonnull ItemStack var1);
    }
}