package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import java.util.ArrayList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public interface IMixingRecipe extends Recipe<MixingContext> {

    FluidStack getOutput(MixingContext var1);

    FluidStack process(MixingContext var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.MIXER_CENTRIFUGE_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.MIXING.get();
    }

    ArrayList<FluidIngredient> getDisplayInputFluids();

    FluidStack getDisplayOutput();

    @Deprecated
    default ItemStack assemble(MixingContext context, RegistryAccess registry) {
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