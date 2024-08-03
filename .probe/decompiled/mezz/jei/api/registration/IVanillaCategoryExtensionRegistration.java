package mezz.jei.api.registration;

import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.category.extensions.IExtendableRecipeCategory;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import net.minecraft.world.item.crafting.CraftingRecipe;

public interface IVanillaCategoryExtensionRegistration {

    IJeiHelpers getJeiHelpers();

    IExtendableRecipeCategory<CraftingRecipe, ICraftingCategoryExtension> getCraftingCategory();
}