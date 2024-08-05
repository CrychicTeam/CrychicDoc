package vectorwing.farmersdelight.integration.jei;

import mezz.jei.api.recipe.RecipeType;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.integration.jei.resource.DecompositionDummy;

public final class FDRecipeTypes {

    public static final RecipeType<CookingPotRecipe> COOKING = RecipeType.create("farmersdelight", "cooking", CookingPotRecipe.class);

    public static final RecipeType<CuttingBoardRecipe> CUTTING = RecipeType.create("farmersdelight", "cutting", CuttingBoardRecipe.class);

    public static final RecipeType<DecompositionDummy> DECOMPOSITION = RecipeType.create("farmersdelight", "decomposition", DecompositionDummy.class);
}