package com.simibubi.create.compat.jei.category;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.compat.jei.ConversionRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingOutput;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.Ingredient;

@ParametersAreNonnullByDefault
public class MysteriousItemConversionCategory extends CreateRecipeCategory<ConversionRecipe> {

    public static final List<ConversionRecipe> RECIPES = new ArrayList();

    public MysteriousItemConversionCategory(CreateRecipeCategory.Info<ConversionRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ConversionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 27, 17).setBackground(getRenderedSlot(), -1, -1).addIngredients((Ingredient) recipe.m_7527_().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 132, 17).setBackground(getRenderedSlot(), -1, -1).addItemStack(((ProcessingOutput) recipe.getRollableResults().get(0)).getStack());
    }

    public void draw(ConversionRecipe recipe, IRecipeSlotsView iRecipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_LONG_ARROW.render(graphics, 52, 20);
        AllGuiTextures.JEI_QUESTION_MARK.render(graphics, 77, 5);
    }

    static {
        RECIPES.add(ConversionRecipe.create(AllItems.EMPTY_BLAZE_BURNER.asStack(), AllBlocks.BLAZE_BURNER.asStack()));
        RECIPES.add(ConversionRecipe.create(AllBlocks.PECULIAR_BELL.asStack(), AllBlocks.HAUNTED_BELL.asStack()));
    }
}