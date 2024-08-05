package com.rekindled.embers.recipe;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.misc.AlchemyResult;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public interface IAlchemyRecipe extends Recipe<AlchemyContext> {

    ArrayList<Ingredient> getCode(long var1);

    boolean matchesCorrect(AlchemyContext var1, Level var2);

    AlchemyResult getResult(AlchemyContext var1);

    @Override
    default ItemStack getToastSymbol() {
        return new ItemStack(RegistryManager.ALCHEMY_TABLET_ITEM.get());
    }

    @Override
    default RecipeType<?> getType() {
        return RegistryManager.ALCHEMY.get();
    }

    @Override
    default ItemStack getResultItem(RegistryAccess registry) {
        return this.getResultItem();
    }

    Ingredient getCenterInput();

    List<Ingredient> getInputs();

    List<Ingredient> getAspects();

    ItemStack getResultItem();

    ItemStack getfailureItem();

    @Deprecated
    @Override
    default boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public static class PedestalContents {

        public ItemStack aspect;

        public ItemStack input;

        public PedestalContents(ItemStack aspect, ItemStack input) {
            this.aspect = aspect;
            this.input = input;
        }
    }
}