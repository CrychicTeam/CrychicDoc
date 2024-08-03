package com.rekindled.embers.util;

import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;

public class WeightedItemStack extends WeightedEntry.IntrusiveBase {

    public static WeightedItemStack EMPTY = new WeightedItemStack(ItemStack.EMPTY, 0);

    ItemStack stack;

    public WeightedItemStack(ItemStack stack, int itemWeightIn) {
        super(itemWeightIn);
        this.stack = stack;
    }

    public ItemStack getStack() {
        return this.stack;
    }
}