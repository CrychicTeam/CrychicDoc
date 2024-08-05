package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public interface IStampingRecipe extends Recipe<StampingContext> {

    ItemStack getOutput(RecipeWrapper var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.STAMPER_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.STAMPING.get();
    }

    @Override
    default ItemStack getResultItem(RegistryAccess registry) {
        return this.getResultItem();
    }

    ItemStack getResultItem();

    FluidIngredient getDisplayInputFluid();

    Ingredient getDisplayInput();

    Ingredient getDisplayStamp();

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}