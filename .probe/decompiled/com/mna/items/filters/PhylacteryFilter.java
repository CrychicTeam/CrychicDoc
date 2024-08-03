package com.mna.items.filters;

import com.mna.api.items.IPhylacteryItem;
import net.minecraft.world.item.ItemStack;

public class PhylacteryFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() instanceof IPhylacteryItem && ((IPhylacteryItem) stack.getItem()).getFillPct(stack) > 0.0F;
    }
}