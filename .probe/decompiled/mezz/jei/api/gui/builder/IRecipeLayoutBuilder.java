package mezz.jei.api.gui.builder;

import mezz.jei.api.recipe.RecipeIngredientRole;

public interface IRecipeLayoutBuilder {

    IRecipeSlotBuilder addSlot(RecipeIngredientRole var1, int var2, int var3);

    IIngredientAcceptor<?> addInvisibleIngredients(RecipeIngredientRole var1);

    void moveRecipeTransferButton(int var1, int var2);

    void setShapeless();

    void setShapeless(int var1, int var2);

    void createFocusLink(IIngredientAcceptor<?>... var1);
}