package dev.latvian.mods.kubejs.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public record SingleItemMatch(ItemStack stack) implements ItemMatch {

    @Override
    public boolean contains(ItemStack item) {
        return this.stack.getItem() == item.getItem();
    }

    @Override
    public boolean contains(Ingredient in) {
        return in.test(this.stack);
    }

    @Override
    public boolean contains(ItemLike itemLike) {
        return this.stack.getItem() == itemLike.asItem();
    }

    public String toString() {
        return this.stack.getItem().kjs$getId();
    }
}