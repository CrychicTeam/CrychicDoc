package mezz.jei.api.gui.handlers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;

public interface IGuiClickableArea {

    Rect2i getArea();

    default boolean isTooltipEnabled() {
        return true;
    }

    default List<Component> getTooltipStrings() {
        return Collections.emptyList();
    }

    void onClick(IFocusFactory var1, IRecipesGui var2);

    static IGuiClickableArea createBasic(int xPos, int yPos, int width, int height, RecipeType<?>... recipeTypes) {
        final Rect2i area = new Rect2i(xPos, yPos, width, height);
        final List<RecipeType<?>> recipeTypesList = Arrays.asList(recipeTypes);
        return new IGuiClickableArea() {

            @Override
            public Rect2i getArea() {
                return area;
            }

            @Override
            public void onClick(IFocusFactory focusFactory, IRecipesGui recipesGui) {
                recipesGui.showTypes(recipeTypesList);
            }
        };
    }
}