package net.minecraft.world.ticks;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface ContainerSingleItem extends Container {

    @Override
    default int getContainerSize() {
        return 1;
    }

    @Override
    default boolean isEmpty() {
        return this.getFirstItem().isEmpty();
    }

    @Override
    default void clearContent() {
        this.removeFirstItem();
    }

    default ItemStack getFirstItem() {
        return this.m_8020_(0);
    }

    default ItemStack removeFirstItem() {
        return this.removeItemNoUpdate(0);
    }

    default void setFirstItem(ItemStack itemStack0) {
        this.m_6836_(0, itemStack0);
    }

    @Override
    default ItemStack removeItemNoUpdate(int int0) {
        return this.m_7407_(int0, this.m_6893_());
    }
}