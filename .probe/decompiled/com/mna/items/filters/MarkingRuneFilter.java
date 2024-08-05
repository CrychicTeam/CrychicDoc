package com.mna.items.filters;

import com.mna.items.ItemInit;
import net.minecraft.world.item.ItemStack;

public class MarkingRuneFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() == ItemInit.RUNE_MARKING.get() && ItemInit.RUNE_MARKING.get().getLocation(stack) != null;
    }
}