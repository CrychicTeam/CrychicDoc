package dev.latvian.mods.kubejs.core.mixin.forge;

import dev.latvian.mods.kubejs.core.InventoryKJS;
import dev.latvian.mods.kubejs.level.BlockContainerJS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = { IItemHandler.class }, remap = false)
public interface IItemHandlerMixin extends InventoryKJS {

    default IItemHandler kjs$self() {
        return (IItemHandler) this;
    }

    @Override
    default boolean kjs$isMutable() {
        return this.kjs$self() instanceof IItemHandlerModifiable;
    }

    @Override
    default int kjs$getSlots() {
        return this.kjs$self().getSlots();
    }

    @Override
    default ItemStack kjs$getStackInSlot(int i) {
        return this.kjs$self().getStackInSlot(i);
    }

    @Override
    default void kjs$setStackInSlot(int slot, ItemStack stack) {
        if (this.kjs$self() instanceof IItemHandlerModifiable mod) {
            mod.setStackInSlot(slot, stack);
        } else {
            InventoryKJS.super.kjs$setStackInSlot(slot, stack);
        }
    }

    @Override
    default ItemStack kjs$insertItem(int i, ItemStack itemStack, boolean b) {
        return this.kjs$self().insertItem(i, itemStack, b);
    }

    @Override
    default ItemStack kjs$extractItem(int i, int i1, boolean b) {
        return this.kjs$self().extractItem(i, i1, b);
    }

    @Override
    default int kjs$getSlotLimit(int i) {
        return this.kjs$self().getSlotLimit(i);
    }

    @Override
    default boolean kjs$isItemValid(int i, ItemStack itemStack) {
        return this.kjs$self().isItemValid(i, itemStack);
    }

    @Nullable
    @Override
    default BlockContainerJS kjs$getBlock(Level level) {
        return this.kjs$self() instanceof BlockEntity entity ? level.kjs$getBlock(entity) : null;
    }
}