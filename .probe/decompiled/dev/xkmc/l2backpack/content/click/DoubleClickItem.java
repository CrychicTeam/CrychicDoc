package dev.xkmc.l2backpack.content.click;

import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface DoubleClickItem {

    int remainingSpace(ItemStack var1);

    boolean canAbsorb(Slot var1, ItemStack var2);

    void mergeStack(ItemStack var1, ItemStack var2);
}