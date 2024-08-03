package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IEmberActivationRecipe extends Recipe<Container> {

    int getOutput(Container var1);

    int process(Container var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.EMBER_ACTIVATOR_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.EMBER_ACTIVATION.get();
    }

    Ingredient getDisplayInput();

    int getDisplayOutput();

    @Deprecated
    @Override
    default ItemStack getResultItem(RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default ItemStack assemble(Container context, RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}