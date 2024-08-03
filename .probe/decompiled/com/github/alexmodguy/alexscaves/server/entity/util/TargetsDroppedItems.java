package com.github.alexmodguy.alexscaves.server.entity.util;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public interface TargetsDroppedItems {

    boolean canTargetItem(ItemStack var1);

    void onGetItem(ItemEntity var1);

    default void onFindTarget(ItemEntity e) {
    }

    default double getMaxDistToItem() {
        return 3.0;
    }
}