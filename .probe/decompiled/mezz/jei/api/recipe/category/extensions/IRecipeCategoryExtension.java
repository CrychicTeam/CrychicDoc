package mezz.jei.api.recipe.category.extensions;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public interface IRecipeCategoryExtension {

    default void drawInfo(int recipeWidth, int recipeHeight, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }

    default List<Component> getTooltipStrings(double mouseX, double mouseY) {
        return Collections.emptyList();
    }

    default boolean handleInput(double mouseX, double mouseY, InputConstants.Key input) {
        return false;
    }
}