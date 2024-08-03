package net.mehvahdjukaar.moonlight.api.resources.recipe;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.set.BlockType;
import net.minecraft.data.recipes.CraftingRecipeBuilder;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

public interface IRecipeTemplate<R extends FinishedRecipe> {

    <T extends BlockType> R createSimilar(T var1, T var2, Item var3, @Nullable String var4);

    @Nullable
    default <T extends BlockType> R createSimilar(T originalMat, T destinationMat, Item unlockItem) {
        return this.createSimilar(originalMat, destinationMat, unlockItem, null);
    }

    void addCondition(Object var1);

    List<Object> getConditions();

    @Nullable
    static <T extends BlockType> Ingredient convertIngredients(T originalMat, T destinationMat, Ingredient ing) {
        for (ItemStack in : ing.getItems()) {
            Item it = in.getItem();
            if (it != Items.BARRIER) {
                ItemLike i = BlockType.changeItemType(it, originalMat, destinationMat);
                if (i != null) {
                    return Ingredient.of(i);
                }
            }
        }
        return null;
    }

    default RecipeCategory determineBookCategory(CraftingBookCategory recipeCategory) {
        for (RecipeCategory v : RecipeCategory.values()) {
            if (recipeCategory == CraftingRecipeBuilder.determineBookCategory(v)) {
                return v;
            }
        }
        return RecipeCategory.MISC;
    }
}