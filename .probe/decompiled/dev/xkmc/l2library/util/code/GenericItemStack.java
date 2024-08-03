package dev.xkmc.l2library.util.code;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record GenericItemStack<I extends Item>(I item, ItemStack stack) {

    public static <T extends Item> GenericItemStack<T> of(ItemStack stack) {
        return new GenericItemStack<>((T) stack.getItem(), stack);
    }

    public static <T extends Item> GenericItemStack<T> from(T item) {
        return new GenericItemStack<>(item, item.getDefaultInstance());
    }
}