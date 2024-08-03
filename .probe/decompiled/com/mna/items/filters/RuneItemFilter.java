package com.mna.items.filters;

import com.mna.items.runes.ItemStoneRune;
import net.minecraft.world.item.ItemStack;

public class RuneItemFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() instanceof ItemStoneRune;
    }
}