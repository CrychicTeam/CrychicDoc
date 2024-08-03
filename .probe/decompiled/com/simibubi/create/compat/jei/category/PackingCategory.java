package com.simibubi.create.compat.jei.category;

import com.simibubi.create.compat.jei.category.animations.AnimatedBlazeBurner;
import com.simibubi.create.compat.jei.category.animations.AnimatedPress;
import com.simibubi.create.content.processing.basin.BasinRecipe;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.crafting.Ingredient;

@ParametersAreNonnullByDefault
public class PackingCategory extends BasinCategory {

    private final AnimatedPress press = new AnimatedPress(true);

    private final AnimatedBlazeBurner heater = new AnimatedBlazeBurner();

    private final PackingCategory.PackingType type;

    public static PackingCategory standard(CreateRecipeCategory.Info<BasinRecipe> info) {
        return new PackingCategory(info, PackingCategory.PackingType.COMPACTING);
    }

    public static PackingCategory autoSquare(CreateRecipeCategory.Info<BasinRecipe> info) {
        return new PackingCategory(info, PackingCategory.PackingType.AUTO_SQUARE);
    }

    protected PackingCategory(CreateRecipeCategory.Info<BasinRecipe> info, PackingCategory.PackingType type) {
        super(info, type != PackingCategory.PackingType.AUTO_SQUARE);
        this.type = type;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BasinRecipe recipe, IFocusGroup focuses) {
        if (this.type == PackingCategory.PackingType.COMPACTING) {
            super.setRecipe(builder, recipe, focuses);
        } else {
            int i = 0;
            NonNullList<Ingredient> ingredients = recipe.m_7527_();
            int size = ingredients.size();
            for (int rows = size == 4 ? 2 : 3; i < size; i++) {
                Ingredient ingredient = ingredients.get(i);
                builder.addSlot(RecipeIngredientRole.INPUT, (rows == 2 ? 27 : 18) + i % rows * 19, 51 - i / rows * 19).setBackground(getRenderedSlot(), -1, -1).addIngredients(ingredient);
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 51).setBackground(getRenderedSlot(), -1, -1).addItemStack(getResultItem(recipe));
        }
    }

    @Override
    public void draw(BasinRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        if (this.type == PackingCategory.PackingType.COMPACTING) {
            super.draw(recipe, iRecipeSlotsView, graphics, mouseX, mouseY);
        } else {
            AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 136, 32);
            AllGuiTextures.JEI_SHADOW.render(graphics, 81, 68);
        }
        HeatCondition requiredHeat = recipe.getRequiredHeat();
        if (requiredHeat != HeatCondition.NONE) {
            this.heater.withHeat(requiredHeat.visualizeAsBlazeBurner()).draw(graphics, this.getBackground().getWidth() / 2 + 3, 55);
        }
        this.press.draw(graphics, this.getBackground().getWidth() / 2 + 3, 34);
    }

    static enum PackingType {

        COMPACTING, AUTO_SQUARE
    }
}