package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public interface IBoilingRecipe extends Recipe<FluidHandlerContext> {

    FluidStack getOutput(FluidHandlerContext var1);

    FluidStack process(FluidHandlerContext var1, int var2);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.MINI_BOILER_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.BOILING.get();
    }

    FluidIngredient getDisplayInput();

    FluidStack getDisplayOutput();

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