package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IGaseousFuelRecipe extends Recipe<FluidHandlerContext> {

    int getBurnTime(FluidHandlerContext var1);

    double getPowerMultiplier(FluidHandlerContext var1);

    int process(FluidHandlerContext var1, int var2);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.WILDFIRE_STIRLING_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.GASEOUS_FUEL.get();
    }

    FluidIngredient getDisplayInput();

    int getDisplayBurnTime();

    double getDisplayMultiplier();

    @Deprecated
    default ItemStack assemble(FluidHandlerContext context, RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default ItemStack getResultItem(RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}