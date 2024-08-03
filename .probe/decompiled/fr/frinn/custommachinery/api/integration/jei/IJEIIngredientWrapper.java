package fr.frinn.custommachinery.api.integration.jei;

import fr.frinn.custommachinery.api.guielement.IGuiElement;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.recipe.RecipeIngredientRole;

public interface IJEIIngredientWrapper<T> {

    boolean setupRecipe(IRecipeLayoutBuilder var1, int var2, int var3, IGuiElement var4, IRecipeHelper var5);

    default RecipeIngredientRole roleFromMode(RequirementIOMode mode) {
        return mode == RequirementIOMode.INPUT ? RecipeIngredientRole.INPUT : RecipeIngredientRole.OUTPUT;
    }
}