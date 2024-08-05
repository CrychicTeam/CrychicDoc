package dev.latvian.mods.kubejs.core.mixin.common;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.item.ItemHandlerUtils;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({ Container.class })
public interface ContainerMixin extends InventoryKJS {

    default Container kjs$self() {
        return (Container) this;
    }

    @Override
    default boolean kjs$isMutable() {
        return true;
    }

    @Override
    default int kjs$getSlots() {
        return this.kjs$self().getContainerSize();
    }

    @NotNull
    @Override
    default ItemStack kjs$getStackInSlot(int slot) {
        return this.kjs$self().getItem(slot);
    }

    @NotNull
    @Override
    default ItemStack kjs$insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stackInSlot = this.kjs$self().getItem(slot);
            if (!stackInSlot.isEmpty()) {
                if (stackInSlot.getCount() >= Math.min(stackInSlot.getMaxStackSize(), this.kjs$getSlotLimit(slot))) {
                    return stack;
                } else if (!ItemHandlerUtils.canItemStacksStack(stack, stackInSlot)) {
                    return stack;
                } else if (!this.kjs$self().canPlaceItem(slot, stack)) {
                    return stack;
                } else {
                    int m = Math.min(stack.getMaxStackSize(), this.kjs$getSlotLimit(slot)) - stackInSlot.getCount();
                    if (stack.getCount() <= m) {
                        if (!simulate) {
                            ItemStack copy = stack.copy();
                            copy.grow(stackInSlot.getCount());
                            this.kjs$self().setItem(slot, copy);
                            this.kjs$self().setChanged();
                        }
                        return ItemStack.EMPTY;
                    } else {
                        stack = stack.copy();
                        if (!simulate) {
                            ItemStack copy = stack.split(m);
                            copy.grow(stackInSlot.getCount());
                            this.kjs$self().setItem(slot, copy);
                            this.kjs$self().setChanged();
                        } else {
                            stack.shrink(m);
                        }
                        return stack;
                    }
                }
            } else if (!this.kjs$self().canPlaceItem(slot, stack)) {
                return stack;
            } else {
                int m = Math.min(stack.getMaxStackSize(), this.kjs$getSlotLimit(slot));
                if (m < stack.getCount()) {
                    stack = stack.copy();
                    if (!simulate) {
                        this.kjs$self().setItem(slot, stack.split(m));
                        this.kjs$self().setChanged();
                        return stack;
                    } else {
                        stack.shrink(m);
                        return stack;
                    }
                } else {
                    if (!simulate) {
                        this.kjs$self().setItem(slot, stack);
                        this.kjs$self().setChanged();
                    }
                    return ItemStack.EMPTY;
                }
            }
        }
    }

    @NotNull
    @Override
    default ItemStack kjs$extractItem(int slot, int amount, boolean simulate) {
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stackInSlot = this.kjs$self().getItem(slot);
            if (stackInSlot.isEmpty()) {
                return ItemStack.EMPTY;
            } else if (simulate) {
                if (stackInSlot.getCount() < amount) {
                    return stackInSlot.copy();
                } else {
                    ItemStack copy = stackInSlot.copy();
                    copy.setCount(amount);
                    return copy;
                }
            } else {
                int m = Math.min(stackInSlot.getCount(), amount);
                ItemStack decrStackSize = this.kjs$self().removeItem(slot, m);
                this.kjs$self().setChanged();
                return decrStackSize;
            }
        }
    }

    @Override
    default void kjs$setStackInSlot(int slot, @NotNull ItemStack stack) {
        this.kjs$self().setItem(slot, stack);
    }

    @Override
    default int kjs$getSlotLimit(int slot) {
        return this.kjs$self().getMaxStackSize();
    }

    @Override
    default boolean kjs$isItemValid(int slot, @NotNull ItemStack stack) {
        return this.kjs$self().canPlaceItem(slot, stack);
    }

    @Override
    default int kjs$getWidth() {
        return this.kjs$self() instanceof CraftingContainer crafter ? crafter.getWidth() : InventoryKJS.super.kjs$getWidth();
    }

    @Override
    default int kjs$getHeight() {
        return this.kjs$self() instanceof CraftingContainer crafter ? crafter.getHeight() : InventoryKJS.super.kjs$getHeight();
    }

    @Override
    default void kjs$clear() {
        this.kjs$self().m_6211_();
    }

    @Override
    default void kjs$setChanged() {
        this.kjs$self().setChanged();
        if (this.kjs$self() instanceof Inventory inv) {
            inv.player.kjs$sendInventoryUpdate();
        }
    }

    @Nullable
    @Override
    default BlockContainerJS kjs$getBlock(Level level) {
        return this.kjs$self() instanceof BlockEntity be ? level.kjs$getBlock(be) : null;
    }

    @Override
    default Container kjs$asContainer() {
        return this.kjs$self();
    }
}