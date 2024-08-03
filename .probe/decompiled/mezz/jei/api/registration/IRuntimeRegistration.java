package mezz.jei.api.registration;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.api.runtime.IBookmarkOverlay;
import mezz.jei.api.runtime.IEditModeConfig;
import mezz.jei.api.runtime.IIngredientFilter;
import mezz.jei.api.runtime.IIngredientListOverlay;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.api.runtime.IRecipesGui;
import mezz.jei.api.runtime.IScreenHelper;

public interface IRuntimeRegistration {

    void setIngredientListOverlay(IIngredientListOverlay var1);

    void setBookmarkOverlay(IBookmarkOverlay var1);

    void setRecipesGui(IRecipesGui var1);

    void setIngredientFilter(IIngredientFilter var1);

    IRecipeManager getRecipeManager();

    IJeiHelpers getJeiHelpers();

    IIngredientManager getIngredientManager();

    IIngredientVisibility getIngredientVisibility();

    IScreenHelper getScreenHelper();

    IRecipeTransferManager getRecipeTransferManager();

    IEditModeConfig getEditModeConfig();
}