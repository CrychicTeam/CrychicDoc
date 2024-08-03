package mezz.jei.api.registration;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.api.runtime.IJeiFeatures;

public interface IAdvancedRegistration {

    IJeiHelpers getJeiHelpers();

    void addRecipeManagerPlugin(IRecipeManagerPlugin var1);

    <T> void addRecipeCategoryDecorator(RecipeType<T> var1, IRecipeCategoryDecorator<T> var2);

    IJeiFeatures getJeiFeatures();
}