package com.github.alexthe666.alexsmobs.entity;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

public interface ITargetsDroppedItems {

    boolean canTargetItem(ItemStack var1);

    void onGetItem(ItemEntity var1);

    default void onFindTarget(ItemEntity e) {
    }

    default double getMaxDistToItem() {
        return 2.0;
    }

    default void setItemFlag(boolean itemAIFlag) {
    }

    default void peck() {
    }

    default void setFlying(boolean flying) {
    }

    default boolean isFlying() {
        return false;
    }
}