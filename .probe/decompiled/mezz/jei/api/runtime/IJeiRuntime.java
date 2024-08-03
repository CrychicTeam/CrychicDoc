package mezz.jei.api.runtime;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.transfer.IRecipeTransferManager;
import mezz.jei.api.runtime.config.IJeiConfigManager;

public interface IJeiRuntime {

    IRecipeManager getRecipeManager();

    IRecipesGui getRecipesGui();

    IIngredientFilter getIngredientFilter();

    IIngredientListOverlay getIngredientListOverlay();

    IBookmarkOverlay getBookmarkOverlay();

    IJeiHelpers getJeiHelpers();

    IIngredientManager getIngredientManager();

    IIngredientVisibility getIngredientVisibility();

    IJeiKeyMappings getKeyMappings();

    IScreenHelper getScreenHelper();

    IRecipeTransferManager getRecipeTransferManager();

    IEditModeConfig getEditModeConfig();

    IJeiConfigManager getConfigManager();
}