package mezz.jei.api.registration;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.IRecipeCategory;

public interface IRecipeCategoryRegistration {

    IJeiHelpers getJeiHelpers();

    void addRecipeCategories(IRecipeCategory<?>... var1);
}