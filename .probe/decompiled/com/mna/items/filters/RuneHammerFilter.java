package com.mna.items.filters;

import com.mna.items.ItemInit;
import net.minecraft.world.item.ItemStack;

public class RuneHammerFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() == ItemInit.RUNESMITH_HAMMER.get() || stack.getItem() == ItemInit.RUNIC_MALUS.get();
    }
}