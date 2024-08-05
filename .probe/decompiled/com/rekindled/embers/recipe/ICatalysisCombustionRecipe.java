package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface ICatalysisCombustionRecipe extends Recipe<CatalysisCombustionContext> {

    int getBurnTIme(CatalysisCombustionContext var1);

    double getmultiplier(CatalysisCombustionContext var1);

    int process(CatalysisCombustionContext var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.IGNEM_REACTOR_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.CATALYSIS_COMBUSTION.get();
    }

    Ingredient getDisplayInput();

    Ingredient getDisplayMachine();

    int getDisplayTime();

    double getDisplayMultiplier();

    @Deprecated
    @Override
    default ItemStack getResultItem(RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    default ItemStack assemble(CatalysisCombustionContext context, RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}