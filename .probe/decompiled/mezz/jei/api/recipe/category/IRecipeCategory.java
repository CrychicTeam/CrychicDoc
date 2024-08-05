package mezz.jei.api.recipe.category;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;

public interface IRecipeCategory<T> {

    RecipeType<T> getRecipeType();

    Component getTitle();

    IDrawable getBackground();

    default int getWidth() {
        return this.getBackground().getWidth();
    }

    default int getHeight() {
        return this.getBackground().getHeight();
    }

    IDrawable getIcon();

    void setRecipe(IRecipeLayoutBuilder var1, T var2, IFocusGroup var3);

    default void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    }

    default List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return List.of();
    }

    default boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input) {
        return false;
    }

    default boolean isHandled(T recipe) {
        return true;
    }

    @Nullable
    default ResourceLocation getRegistryName(T recipe) {
        return recipe instanceof Recipe<?> vanillaRecipe ? vanillaRecipe.getId() : null;
    }
}