package com.mna.items.filters;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;

public class FilledBucketFilter extends ItemFilter {

    @Override
    public boolean IsValidItem(ItemStack stack) {
        return stack.getItem() instanceof BucketItem && ((BucketItem) stack.getItem()).getFluid() != null;
    }
}