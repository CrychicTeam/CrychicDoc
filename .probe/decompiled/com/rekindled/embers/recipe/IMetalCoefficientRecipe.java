package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IMetalCoefficientRecipe extends Recipe<BlockStateContext> {

    double getCoefficient(BlockStateContext var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.PRESSURE_REFINERY_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.METAL_COEFFICIENT.get();
    }

    List<ItemStack> getDisplayInput();

    double getDisplayCoefficient();

    @Deprecated
    @Override
    default ItemStack getResultItem(RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    default ItemStack assemble(BlockStateContext context, RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}