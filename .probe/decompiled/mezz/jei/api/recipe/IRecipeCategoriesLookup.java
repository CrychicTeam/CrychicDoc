package mezz.jei.api.recipe;

import java.util.Collection;
import java.util.stream.Stream;
import mezz.jei.api.recipe.category.IRecipeCategory;

public interface IRecipeCategoriesLookup {

    IRecipeCategoriesLookup limitTypes(Collection<RecipeType<?>> var1);

    IRecipeCategoriesLookup limitFocus(Collection<? extends IFocus<?>> var1);

    IRecipeCategoriesLookup includeHidden();

    Stream<IRecipeCategory<?>> get();
}