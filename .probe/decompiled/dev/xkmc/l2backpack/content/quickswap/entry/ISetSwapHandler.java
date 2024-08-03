package dev.xkmc.l2backpack.content.quickswap.entry;

import net.minecraft.world.item.ItemStack;

public interface ISetSwapHandler {

    ItemStack getStack(int var1);

    void replace(int var1, ItemStack var2);
}