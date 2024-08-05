package com.simibubi.create.compat.jei.category;

import com.simibubi.create.compat.jei.category.animations.AnimatedSaw;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.item.ItemHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.StonecutterRecipe;

@ParametersAreNonnullByDefault
public class BlockCuttingCategory extends CreateRecipeCategory<BlockCuttingCategory.CondensedBlockCuttingRecipe> {

    private final AnimatedSaw saw = new AnimatedSaw();

    public BlockCuttingCategory(CreateRecipeCategory.Info<BlockCuttingCategory.CondensedBlockCuttingRecipe> info) {
        super(info);
    }

    public void setRecipe(IRecipeLayoutBuilder builder, BlockCuttingCategory.CondensedBlockCuttingRecipe recipe, IFocusGroup focuses) {
        List<List<ItemStack>> results = recipe.getCondensedOutputs();
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5).setBackground(getRenderedSlot(), -1, -1).addItemStacks(Arrays.asList(((Ingredient) recipe.m_7527_().get(0)).getItems()));
        int i = 0;
        for (List<ItemStack> itemStacks : results) {
            int xPos = 78 + i % 5 * 19;
            int yPos = 48 + i / 5 * -19;
            builder.addSlot(RecipeIngredientRole.OUTPUT, xPos, yPos).setBackground(getRenderedSlot(), -1, -1).addItemStacks(itemStacks);
            i++;
        }
    }

    public void draw(BlockCuttingCategory.CondensedBlockCuttingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        AllGuiTextures.JEI_DOWN_ARROW.render(graphics, 31, 6);
        AllGuiTextures.JEI_SHADOW.render(graphics, 16, 50);
        this.saw.draw(graphics, 33, 37);
    }

    public static List<BlockCuttingCategory.CondensedBlockCuttingRecipe> condenseRecipes(List<Recipe<?>> stoneCuttingRecipes) {
        List<BlockCuttingCategory.CondensedBlockCuttingRecipe> condensed = new ArrayList();
        label23: for (Recipe<?> recipe : stoneCuttingRecipes) {
            Ingredient i1 = recipe.getIngredients().get(0);
            for (BlockCuttingCategory.CondensedBlockCuttingRecipe condensedRecipe : condensed) {
                if (ItemHelper.matchIngredients(i1, (Ingredient) condensedRecipe.m_7527_().get(0))) {
                    condensedRecipe.addOutput(getResultItem(recipe));
                    continue label23;
                }
            }
            BlockCuttingCategory.CondensedBlockCuttingRecipe cr = new BlockCuttingCategory.CondensedBlockCuttingRecipe(i1);
            cr.addOutput(getResultItem(recipe));
            condensed.add(cr);
        }
        return condensed;
    }

    public static class CondensedBlockCuttingRecipe extends StonecutterRecipe {

        List<ItemStack> outputs = new ArrayList();

        public CondensedBlockCuttingRecipe(Ingredient ingredient) {
            super(new ResourceLocation(""), "", ingredient, ItemStack.EMPTY);
        }

        public void addOutput(ItemStack stack) {
            this.outputs.add(stack);
        }

        public List<ItemStack> getOutputs() {
            return this.outputs;
        }

        public List<List<ItemStack>> getCondensedOutputs() {
            List<List<ItemStack>> result = new ArrayList();
            int index = 0;
            boolean firstPass = true;
            for (ItemStack itemStack : this.outputs) {
                if (firstPass) {
                    result.add(new ArrayList());
                }
                ((List) result.get(index)).add(itemStack);
                if (++index >= 15) {
                    index = 0;
                    firstPass = false;
                }
            }
            return result;
        }

        @Override
        public boolean isSpecial() {
            return true;
        }
    }
}