package dev.xkmc.modulargolems.content.menu.ghost;

import net.minecraft.world.item.ItemStack;

public interface IGhostContainer extends ReadOnlyContainer {

    @Override
    ItemStack getItem(int var1);

    boolean internalMatch(ItemStack var1);

    void set(int var1, ItemStack var2);

    int listSize();

    @Override
    int getContainerSize();

    @Override
    default boolean isEmpty() {
        return this.listSize() == 0;
    }
}