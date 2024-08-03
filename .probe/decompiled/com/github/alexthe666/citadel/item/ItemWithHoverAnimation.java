package com.github.alexthe666.citadel.item;

import net.minecraft.world.item.ItemStack;

public interface ItemWithHoverAnimation {

    float getMaxHoverOverTime(ItemStack var1);

    boolean canHoverOver(ItemStack var1);
}