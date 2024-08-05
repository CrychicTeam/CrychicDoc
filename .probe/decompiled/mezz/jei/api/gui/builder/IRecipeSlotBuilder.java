package mezz.jei.api.gui.builder;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;

public interface IRecipeSlotBuilder extends IIngredientAcceptor<IRecipeSlotBuilder> {

    IRecipeSlotBuilder addTooltipCallback(IRecipeSlotTooltipCallback var1);

    IRecipeSlotBuilder setSlotName(String var1);

    IRecipeSlotBuilder setBackground(IDrawable var1, int var2, int var3);

    IRecipeSlotBuilder setOverlay(IDrawable var1, int var2, int var3);

    IRecipeSlotBuilder setFluidRenderer(long var1, boolean var3, int var4, int var5);

    <T> IRecipeSlotBuilder setCustomRenderer(IIngredientType<T> var1, IIngredientRenderer<T> var2);
}