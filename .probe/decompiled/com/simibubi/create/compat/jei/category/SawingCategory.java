package com.simibubi.create.compat.jei.category;

import com.simibubi.create.compat.jei.category.animations.AnimatedSaw;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
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
public class SawingCategory extends CreateRecipeCategory<CuttingRecipe> {

    private final AnimatedSaw saw = new AnimatedSaw();

    public SawingCategory(CreateRecipeCategory.Info<CuttingRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, CuttingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 5).setBackground(getRenderedSlot(), -1, -1).addIngredients((Ingredient) recipe.m_7527_().get(0));
        List<ProcessingOutput> results = recipe.getRollableResults();
        int i = 0;
        for (ProcessingOutput output : results) {
            int xOffset = i % 2 == 0 ? 0 : 19;
            int yOffset = i / 2 * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, 118 + xOffset, 48 + yOffset).setBackground(getRenderedSlot(output), -1, -1).addItemStack(output.getStack()).addTooltipCallback(addStochasticTooltip(output));
            i++;
        }
    }

    public void draw(CuttingRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 70, 6);
        AllGuiTextures.JEI_SHADOW.render(graphics, 55, 55);
        this.saw.draw(graphics, 72, 42);
    }
}