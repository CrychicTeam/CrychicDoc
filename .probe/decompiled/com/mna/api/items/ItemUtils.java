package com.mna.api.items;

import net.minecraft.world.item.ItemStack;

public class ItemUtils {

    private static final String KEY_CHARGES = "charges";

    private static final String KEY_MAX_CHARGES = "max_charges";

    public static void writeMaxCharges(ItemStack stack, int charges) {
        stack.getOrCreateTag().putInt("max_charges", charges);
    }

    public static void writeCharges(ItemStack stack, int charges) {
        stack.getOrCreateTag().putInt("charges", charges);
    }

    public static int getCharges(ItemStack stack) {
        return hasCharges(stack) ? stack.getOrCreateTag().getInt("charges") : 0;
    }

    public static int getMaxCharges(ItemStack stack) {
        return hasCharges(stack) ? stack.getOrCreateTag().getInt("max_charges") : 0;
    }

    public static boolean hasCharges(ItemStack stack) {
        return stack.getOrCreateTag().contains("charges");
    }
}