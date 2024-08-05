package com.rekindled.embers.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public class FuelItem extends Item {

    public final int burnTime;

    public FuelItem(Item.Properties pProperties, int burnTime) {
        super(pProperties);
        this.burnTime = burnTime;
    }

    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }
}