package pie.ilikepiefoo.compat.jei.impl;

import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.List;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public record CustomRecipeCategoryDecorator<T>(CustomRecipeCategoryDecorator.DrawDecorator<T> draw, CustomRecipeCategoryDecorator.TooltipDecorator<T> tooltip) implements IRecipeCategoryDecorator<T> {

    @Override
    public void draw(T recipe, IRecipeCategory<T> recipeCategory, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        try {
            this.draw.decorate(recipe, recipeCategory, recipeSlotsView, guiGraphics, mouseX, mouseY);
        } catch (Throwable var10) {
            ConsoleJS.CLIENT.error("Error decorating existing draw handler for recipe category: " + recipeCategory.getRecipeType().getUid(), var10);
        }
    }

    @Override
    public List<Component> decorateExistingTooltips(List<Component> tooltips, T recipe, IRecipeCategory<T> recipeCategory, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        try {
            return this.tooltip.decorate(tooltips, recipe, recipeCategory, recipeSlotsView, mouseX, mouseY);
        } catch (Throwable var10) {
            ConsoleJS.CLIENT.error("Error decorating existing tooltips for recipe category: " + recipeCategory.getRecipeType().getUid(), var10);
            return IRecipeCategoryDecorator.super.decorateExistingTooltips(tooltips, recipe, recipeCategory, recipeSlotsView, mouseX, mouseY);
        }
    }

    @FunctionalInterface
    public interface DrawDecorator<R> {

        void decorate(R var1, IRecipeCategory<R> var2, IRecipeSlotsView var3, GuiGraphics var4, double var5, double var7);
    }

    @FunctionalInterface
    public interface TooltipDecorator<R> {

        List<Component> decorate(List<Component> var1, R var2, IRecipeCategory<R> var3, IRecipeSlotsView var4, double var5, double var7);
    }
}