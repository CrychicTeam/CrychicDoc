package com.mna.items.filters;

import com.mna.items.ItemInit;
import net.minecraft.world.item.ItemStack;

public class VellumFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() == ItemInit.VELLUM.get();
    }
}