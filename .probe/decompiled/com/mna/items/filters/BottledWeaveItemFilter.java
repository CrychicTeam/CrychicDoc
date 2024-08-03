package com.mna.items.filters;

import com.mna.items.manaweaving.ItemManaweaveBottle;
import net.minecraft.world.item.ItemStack;

public class BottledWeaveItemFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() instanceof ItemManaweaveBottle;
    }
}