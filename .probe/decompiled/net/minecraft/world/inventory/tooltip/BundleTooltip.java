package net.minecraft.world.inventory.tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class BundleTooltip implements TooltipComponent {

    private final NonNullList<ItemStack> items;

    private final int weight;

    public BundleTooltip(NonNullList<ItemStack> nonNullListItemStack0, int int1) {
        this.items = nonNullListItemStack0;
        this.weight = int1;
    }

    public NonNullList<ItemStack> getItems() {
        return this.items;
    }

    public int getWeight() {
        return this.weight;
    }
}