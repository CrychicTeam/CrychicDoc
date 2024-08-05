package com.simibubi.create.compat.jei.category;

import com.simibubi.create.compat.jei.category.animations.AnimatedPress;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;

@ParametersAreNonnullByDefault
public class PressingCategory extends CreateRecipeCategory<PressingRecipe> {

    private final AnimatedPress press = new AnimatedPress(false);

    public PressingCategory(CreateRecipeCategory.Info<PressingRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, PressingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 51).setBackground(getRenderedSlot(), -1, -1).addIngredients((Ingredient) recipe.m_7527_().get(0));
        List<ProcessingOutput> results = recipe.getRollableResults();
        int i = 0;
        for (ProcessingOutput output : results) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 131 + 19 * i, 50).setBackground(getRenderedSlot(output), -1, -1).addItemStack(output.getStack()).addTooltipCallback(addStochasticTooltip(output));
            i++;
        }
    }

    public void draw(PressingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_SHADOW.render(graphics, 61, 41);
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, 52, 54);
        this.press.draw(graphics, this.getBackground().getWidth() / 2 - 17, 22);
    }
}