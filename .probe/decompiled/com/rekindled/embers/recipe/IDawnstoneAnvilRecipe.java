package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IDawnstoneAnvilRecipe extends Recipe<Container> {

    List<ItemStack> getOutput(Container var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.DAWNSTONE_ANVIL_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.DAWNSTONE_ANVIL_RECIPE.get();
    }

    List<ItemStack> getDisplayInputBottom();

    List<ItemStack> getDisplayInputTop();

    List<ItemStack> getDisplayOutput();

    @Deprecated
    @Override
    default ItemStack assemble(Container context, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}