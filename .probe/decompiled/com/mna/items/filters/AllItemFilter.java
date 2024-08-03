package com.mna.items.filters;

import net.minecraft.world.item.ItemStack;

public class AllItemFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return true;
    }
}