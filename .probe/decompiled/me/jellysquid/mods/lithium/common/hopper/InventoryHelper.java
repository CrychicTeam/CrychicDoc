package me.jellysquid.mods.lithium.common.hopper;

import me.jellysquid.mods.lithium.api.inventory.LithiumInventory;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class InventoryHelper {

    public static LithiumStackList getLithiumStackList(LithiumInventory inventory) {
        NonNullList<ItemStack> stackList = inventory.getInventoryLithium();
        return stackList instanceof LithiumStackList ? (LithiumStackList) stackList : upgradeToLithiumStackList(inventory);
    }

    public static LithiumStackList getLithiumStackListOrNull(LithiumInventory inventory) {
        NonNullList<ItemStack> stackList = inventory.getInventoryLithium();
        return stackList instanceof LithiumStackList ? (LithiumStackList) stackList : null;
    }

    private static LithiumStackList upgradeToLithiumStackList(LithiumInventory inventory) {
        inventory.generateLootLithium();
        NonNullList<ItemStack> stackList = inventory.getInventoryLithium();
        LithiumStackList lithiumStackList = new LithiumStackList(stackList, inventory.m_6893_());
        inventory.setInventoryLithium(lithiumStackList);
        return lithiumStackList;
    }
}