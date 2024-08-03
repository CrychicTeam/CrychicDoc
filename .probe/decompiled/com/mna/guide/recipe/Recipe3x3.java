package com.mna.guide.recipe;

import com.mna.api.guidebook.RecipeRendererBase;
import com.mna.gui.GuiTextures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;

public class Recipe3x3 extends RecipeRendererBase implements ICyclingRecipeRenderer<Recipe3x3> {

    private CraftingRecipe[] recipes;

    static final int POINT_RENDER_SIZE = 13;

    public Recipe3x3(int x, int y) {
        super(x, y);
    }

    @Override
    public void init_internal(ResourceLocation recipeLocation) {
        Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(recipeLocation);
        if (pattern.isPresent()) {
            this.recipes = new CraftingRecipe[] { (CraftingRecipe) pattern.get() };
        }
    }

    @Override
    protected ResourceLocation backgroundTexture() {
        return GuiTextures.Recipe.CRAFT_3X3;
    }

    @Override
    protected void drawForeground(GuiGraphics pGuiGraphics, int x, int y, int mouseX, int mouseY, float partialTicks) {
        if (this.recipes.length != 0) {
            Level level = this.minecraft.level;
            if (level != null) {
                RegistryAccess access = level.registryAccess();
                if (access != null) {
                    CraftingRecipe recipe = this.recipes[this.getIndex()];
                    if (recipe instanceof ShapedRecipe) {
                        this.renderShapedRecipe(pGuiGraphics, x, y, (ShapedRecipe) recipe, access);
                    } else if (recipe instanceof ShapelessRecipe) {
                        this.renderShapelessRecipe(pGuiGraphics, x, y, (ShapelessRecipe) recipe, access);
                    }
                }
            }
        }
    }

    private void renderShapedRecipe(GuiGraphics pGuiGraphics, int x, int y, ShapedRecipe recipe, RegistryAccess access) {
        if (recipe.getRecipeWidth() <= 3 && recipe.getRecipeHeight() <= 3) {
            int startX = (int) ((float) this.m_252754_() / this.scale + 35.0F);
            int startY = (int) ((float) this.m_252907_() / this.scale + 90.0F);
            int pointSizeX = 54;
            int pointSizeY = 50;
            int padding = 6;
            for (int j = 0; j < recipe.getRecipeHeight(); j++) {
                for (int i = 0; i < recipe.getRecipeWidth(); i++) {
                    int ingredientIndex = j * recipe.getRecipeWidth() + i;
                    if (ingredientIndex < recipe.getIngredients().size()) {
                        Ingredient ingredient = recipe.getIngredients().get(ingredientIndex);
                        List<ItemStack> stacks = Arrays.asList(ingredient.getItems());
                        if (stacks.size() > 0) {
                            this.renderItemStack(pGuiGraphics, stacks, startX + (pointSizeX + padding) * i, startY + (pointSizeY + padding) * j, 2.0F);
                        }
                    }
                }
            }
            this.renderItemStack(pGuiGraphics, recipe.getResultItem(access), (int) ((float) this.m_252754_() / this.scale + 93.0F), (int) ((float) this.m_252907_() / this.scale + 23.0F), 2.0F);
        }
    }

    private void renderShapelessRecipe(GuiGraphics pGuiGraphics, int x, int y, ShapelessRecipe recipe, RegistryAccess access) {
        if (recipe.getIngredients().size() <= 9) {
            int startX = (int) ((float) this.m_252754_() / this.scale + 35.0F);
            int startY = (int) ((float) this.m_252907_() / this.scale + 90.0F);
            int pointSizeX = 54;
            int pointSizeY = 50;
            int padding = 6;
            int count = 0;
            for (Ingredient ingredient : recipe.getIngredients()) {
                ItemStack[] stacks = ingredient.getItems();
                if (stacks.length > 0) {
                    this.renderItemStack(pGuiGraphics, Arrays.asList(stacks), startX + (pointSizeX + padding) * Math.floorDiv(count, 3), startY + (pointSizeY + padding) * (count % 3), 2.0F);
                }
                count++;
            }
            this.renderItemStack(pGuiGraphics, recipe.getResultItem(access), (int) ((float) this.m_252754_() / this.scale + 93.0F), (int) ((float) this.m_252907_() / this.scale + 23.0F), 2.0F);
            Font fr = this.minecraft.font;
            String lineFormatted = I18n.get("gui.mna.shapeless");
            pGuiGraphics.drawString(fr, lineFormatted, this.m_252754_() + this.f_93618_ / 2 - fr.width(lineFormatted) / 2, this.m_252907_() + 67, 4210752, false);
        }
    }

    @Override
    public int getTier() {
        return 1;
    }

    @Override
    public void init_cycling(ResourceLocation[] rLocs) {
        ArrayList<CraftingRecipe> recipes = new ArrayList();
        for (ResourceLocation rLoc : rLocs) {
            Optional<? extends Recipe<?>> pattern = this.minecraft.level.getRecipeManager().byKey(rLoc);
            if (pattern.isPresent()) {
                recipes.add((CraftingRecipe) pattern.get());
            }
        }
        this.recipes = (CraftingRecipe[]) recipes.toArray(new CraftingRecipe[0]);
    }

    @Override
    public int countRecipes() {
        return this.recipes.length;
    }
}