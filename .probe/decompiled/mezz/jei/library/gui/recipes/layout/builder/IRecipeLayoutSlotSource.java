package mezz.jei.library.gui.recipes.layout.builder;

import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.library.gui.ingredients.RecipeSlots;

public interface IRecipeLayoutSlotSource {

    RecipeIngredientRole getRole();

    void setRecipeSlots(RecipeSlots var1, IntSet var2, IIngredientVisibility var3);

    <T> Stream<T> getIngredients(IIngredientType<T> var1);

    Stream<IIngredientType<?>> getIngredientTypes();

    IntSet getMatches(IFocusGroup var1);

    int getIngredientCount();
}