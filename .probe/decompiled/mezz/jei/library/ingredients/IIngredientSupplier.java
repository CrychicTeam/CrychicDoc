package mezz.jei.library.ingredients;

import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeIngredientRole;

public interface IIngredientSupplier {

    Stream<? extends IIngredientType<?>> getIngredientTypes(RecipeIngredientRole var1);

    <T> Stream<T> getIngredientStream(IIngredientType<T> var1, RecipeIngredientRole var2);
}