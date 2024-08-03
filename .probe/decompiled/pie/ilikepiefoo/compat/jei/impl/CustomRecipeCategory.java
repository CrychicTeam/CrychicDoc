package pie.ilikepiefoo.compat.jei.impl;

import com.mojang.blaze3d.platform.InputConstants;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pie.ilikepiefoo.compat.jei.builder.RecipeCategoryBuilder;

public class CustomRecipeCategory<T> implements IRecipeCategory<T> {

    private final RecipeCategoryBuilder<T> builder;

    public CustomRecipeCategory(RecipeCategoryBuilder<T> builder) {
        this.builder = builder;
    }

    @NotNull
    @Override
    public RecipeType<T> getRecipeType() {
        return this.builder.getRecipeType();
    }

    @NotNull
    @Override
    public Component getTitle() {
        return this.builder.getCategoryTitle();
    }

    @NotNull
    @Override
    public IDrawable getBackground() {
        return this.builder.getCategoryBackground();
    }

    @Override
    public int getWidth() {
        return this.builder.getWidth();
    }

    @Override
    public int getHeight() {
        return this.builder.getHeight();
    }

    @NotNull
    @Override
    public IDrawable getIcon() {
        return this.builder.getCategoryIcon();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        if (this.builder.getSetRecipeHandler() != null) {
            try {
                this.builder.getSetRecipeHandler().setRecipe(builder, recipe, focuses);
            } catch (Throwable var5) {
                ConsoleJS.CLIENT.error("Error setting recipe for recipe category: " + this.builder.getRecipeType().getUid(), var5);
            }
        }
    }

    @Override
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        try {
            if (this.builder.getDrawHandler() != null) {
                this.builder.getDrawHandler().draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
            }
        } catch (Throwable var9) {
            ConsoleJS.CLIENT.error("Error drawing recipe category: " + this.builder.getRecipeType().getUid(), var9);
        }
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @NotNull
    @Override
    public List<Component> getTooltipStrings(T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        try {
            if (this.builder.getTooltipHandler() != null) {
                return this.builder.getTooltipHandler().getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
            }
        } catch (Throwable var8) {
            ConsoleJS.CLIENT.error("Error getting tooltip strings for recipe category: " + this.builder.getRecipeType().getUid(), var8);
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public boolean handleInput(T recipe, double mouseX, double mouseY, InputConstants.Key input) {
        try {
            if (this.builder.getInputHandler() != null) {
                return this.builder.getInputHandler().handleInput(recipe, mouseX, mouseY, input);
            }
        } catch (Throwable var8) {
            ConsoleJS.CLIENT.error("Error handling input for recipe category: " + this.builder.getRecipeType().getUid(), var8);
        }
        return IRecipeCategory.super.handleInput(recipe, mouseX, mouseY, input);
    }

    @Override
    public boolean isHandled(T recipe) {
        try {
            if (this.builder.getIsRecipeHandledByCategory() != null) {
                return this.builder.getIsRecipeHandledByCategory().isHandled(recipe);
            }
        } catch (Throwable var3) {
            ConsoleJS.CLIENT.error("Error checking if recipe is handled by category: " + this.builder.getRecipeType().getUid(), var3);
        }
        return IRecipeCategory.super.isHandled(recipe);
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName(T recipe) {
        try {
            if (this.builder.getGetRegisterName() != null) {
                return this.builder.getGetRegisterName().getRegistryName(recipe);
            }
        } catch (Throwable var3) {
            ConsoleJS.CLIENT.error("Error getting registry name for recipe category: " + this.builder.getRecipeType().getUid(), var3);
        }
        return IRecipeCategory.super.getRegistryName(recipe);
    }
}