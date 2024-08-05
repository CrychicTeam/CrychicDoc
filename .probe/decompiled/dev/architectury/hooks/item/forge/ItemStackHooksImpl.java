package dev.architectury.hooks.item.forge;

import net.minecraft.world.item.ItemStack;

public class ItemStackHooksImpl {

    public static boolean hasCraftingRemainingItem(ItemStack stack) {
        return stack.hasCraftingRemainingItem();
    }

    public static ItemStack getCraftingRemainingItem(ItemStack stack) {
        return stack.getCraftingRemainingItem();
    }
}