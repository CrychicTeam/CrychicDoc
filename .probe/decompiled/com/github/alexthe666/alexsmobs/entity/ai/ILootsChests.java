package com.github.alexthe666.alexsmobs.entity.ai;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public interface ILootsChests {

    boolean isLootable(Container var1);

    boolean shouldLootItem(ItemStack var1);
}