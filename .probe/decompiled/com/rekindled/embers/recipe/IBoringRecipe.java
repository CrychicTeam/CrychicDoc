package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.util.WeightedItemStack;
import java.util.Collection;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public interface IBoringRecipe extends Recipe<BoringContext> {

    WeightedItemStack getOutput(BoringContext var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.EMBER_BORE_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.BORING.get();
    }

    int getMinHeight();

    int getMaxHeight();

    Collection<ResourceLocation> getDimensions();

    Collection<ResourceLocation> getBiomes();

    double getChance();

    WeightedItemStack getDisplayOutput();

    List<ItemStack> getDisplayInput();

    @Deprecated
    default ItemStack assemble(BoringContext context, RegistryAccess registry) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}