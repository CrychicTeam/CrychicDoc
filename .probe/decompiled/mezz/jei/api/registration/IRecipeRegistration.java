package mezz.jei.api.registration;

import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public interface IRecipeRegistration {

    IJeiHelpers getJeiHelpers();

    IIngredientManager getIngredientManager();

    IVanillaRecipeFactory getVanillaRecipeFactory();

    IIngredientVisibility getIngredientVisibility();

    <T> void addRecipes(RecipeType<T> var1, List<T> var2);

    <T> void addIngredientInfo(T var1, IIngredientType<T> var2, Component... var3);

    <T> void addIngredientInfo(List<T> var1, IIngredientType<T> var2, Component... var3);

    default void addItemStackInfo(ItemStack ingredient, Component... descriptionComponents) {
        this.addIngredientInfo(ingredient, VanillaTypes.ITEM_STACK, descriptionComponents);
    }

    default void addItemStackInfo(List<ItemStack> ingredients, Component... descriptionComponents) {
        this.addIngredientInfo(ingredients, VanillaTypes.ITEM_STACK, descriptionComponents);
    }
}