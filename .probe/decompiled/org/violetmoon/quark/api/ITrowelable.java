package org.violetmoon.quark.api;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;

public interface ITrowelable {

    default boolean canBeTroweled(ItemStack stack, UseOnContext context) {
        return true;
    }
}