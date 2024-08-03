package mezz.jei.library.recipes;

import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IRecipeCatalystLookup;
import mezz.jei.api.recipe.RecipeType;

public class RecipeCatalystLookup implements IRecipeCatalystLookup {

    private final RecipeType<?> recipeType;

    private final RecipeManagerInternal recipeManager;

    private boolean includeHidden;

    public RecipeCatalystLookup(RecipeType<?> recipeType, RecipeManagerInternal recipeManager) {
        this.recipeType = recipeType;
        this.recipeManager = recipeManager;
    }

    @Override
    public IRecipeCatalystLookup includeHidden() {
        this.includeHidden = true;
        return this;
    }

    @Override
    public Stream<ITypedIngredient<?>> get() {
        return this.recipeManager.getRecipeCatalystStream(this.recipeType, this.includeHidden);
    }

    @Override
    public <V> Stream<V> get(IIngredientType<V> ingredientType) {
        return this.get().map(i -> i.getIngredient(ingredientType)).flatMap(Optional::stream);
    }
}