package dev.xkmc.l2backpack.content.quickswap.entry;

import net.minecraft.world.item.ItemStack;

public interface ISingleSwapHandler {

    ItemStack getStack();

    void replace(ItemStack var1);
}