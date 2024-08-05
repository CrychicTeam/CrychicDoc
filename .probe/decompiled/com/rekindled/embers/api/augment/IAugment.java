package com.rekindled.embers.api.augment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IAugment {

    ResourceLocation getName();

    double getCost();

    default boolean countTowardsTotalLevel() {
        return true;
    }

    default boolean canRemove() {
        return true;
    }

    default boolean shouldRenderTooltip() {
        return true;
    }

    default void onApply(ItemStack stack) {
    }

    default void onRemove(ItemStack stack) {
    }
}