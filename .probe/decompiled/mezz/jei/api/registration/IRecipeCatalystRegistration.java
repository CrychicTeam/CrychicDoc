package mezz.jei.api.registration;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.world.item.ItemStack;

public interface IRecipeCatalystRegistration {

    IIngredientManager getIngredientManager();

    IJeiHelpers getJeiHelpers();

    default void addRecipeCatalyst(ItemStack ingredient, RecipeType<?>... recipeTypes) {
        this.addRecipeCatalyst(VanillaTypes.ITEM_STACK, ingredient, recipeTypes);
    }

    <T> void addRecipeCatalyst(IIngredientType<T> var1, T var2, RecipeType<?>... var3);
}