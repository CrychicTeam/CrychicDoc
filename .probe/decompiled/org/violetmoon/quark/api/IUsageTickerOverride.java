package org.violetmoon.quark.api;

import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;

public interface IUsageTickerOverride {

    default int getUsageTickerCountForItem(ItemStack stack, Predicate<ItemStack> target) {
        return 0;
    }

    default boolean shouldUsageTickerCheckMatchSize(ItemStack stack) {
        return false;
    }

    ItemStack getUsageTickerItem(ItemStack var1);
}