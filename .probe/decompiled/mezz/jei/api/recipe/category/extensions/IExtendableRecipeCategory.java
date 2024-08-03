package mezz.jei.api.recipe.category.extensions;

import java.util.function.Function;
import java.util.function.Predicate;
import mezz.jei.api.recipe.category.IRecipeCategory;

public interface IExtendableRecipeCategory<T, W extends IRecipeCategoryExtension> extends IRecipeCategory<T> {

    <R extends T> void addCategoryExtension(Class<? extends R> var1, Function<R, ? extends W> var2);

    <R extends T> void addCategoryExtension(Class<? extends R> var1, Predicate<R> var2, Function<R, ? extends W> var3);
}