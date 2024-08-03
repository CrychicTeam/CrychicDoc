package fr.frinn.custommachinery.api.integration.jei;

import fr.frinn.custommachinery.api.crafting.IMachineRecipe;
import java.util.List;

public interface IJEIIngredientRequirement<T> {

    List<IJEIIngredientWrapper<T>> getJEIIngredientWrappers(IMachineRecipe var1);
}