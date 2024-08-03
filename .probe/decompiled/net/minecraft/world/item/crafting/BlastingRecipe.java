package net.minecraft.world.item.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

public class BlastingRecipe extends AbstractCookingRecipe {

    public BlastingRecipe(ResourceLocation resourceLocation0, String string1, CookingBookCategory cookingBookCategory2, Ingredient ingredient3, ItemStack itemStack4, float float5, int int6) {
        super(RecipeType.BLASTING, resourceLocation0, string1, cookingBookCategory2, ingredient3, itemStack4, float5, int6);
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.BLAST_FURNACE);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializer.BLASTING_RECIPE;
    }
}