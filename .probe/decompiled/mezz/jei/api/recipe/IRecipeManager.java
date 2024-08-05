package mezz.jei.api.recipe;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.resources.ResourceLocation;

public interface IRecipeManager {

    <R> IRecipeLookup<R> createRecipeLookup(RecipeType<R> var1);

    IRecipeCategoriesLookup createRecipeCategoryLookup();

    IRecipeCatalystLookup createRecipeCatalystLookup(RecipeType<?> var1);

    <T> void hideRecipes(RecipeType<T> var1, Collection<T> var2);

    <T> void unhideRecipes(RecipeType<T> var1, Collection<T> var2);

    <T> void addRecipes(RecipeType<T> var1, List<T> var2);

    void hideRecipeCategory(RecipeType<?> var1);

    void unhideRecipeCategory(RecipeType<?> var1);

    <T> Optional<IRecipeLayoutDrawable<T>> createRecipeLayoutDrawable(IRecipeCategory<T> var1, T var2, IFocusGroup var3);

    IRecipeSlotDrawable createRecipeSlotDrawable(RecipeIngredientRole var1, List<Optional<ITypedIngredient<?>>> var2, Set<Integer> var3, int var4, int var5, int var6);

    Optional<RecipeType<?>> getRecipeType(ResourceLocation var1);
}