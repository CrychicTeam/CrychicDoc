package com.mna.items.artifice.curio;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IPreEnchantedItem<T extends Item> {

    default boolean hasEffect(ItemStack stack) {
        return true;
    }

    default boolean isEnchantable(ItemStack stack) {
        return false;
    }
}