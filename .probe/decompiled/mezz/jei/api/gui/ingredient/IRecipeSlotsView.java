package mezz.jei.api.gui.ingredient;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.recipe.RecipeIngredientRole;

public interface IRecipeSlotsView {

    List<IRecipeSlotView> getSlotViews();

    List<IRecipeSlotView> getSlotViews(RecipeIngredientRole var1);

    Optional<IRecipeSlotView> findSlotByName(String var1);
}