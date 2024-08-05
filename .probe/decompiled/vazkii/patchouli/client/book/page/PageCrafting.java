package vazkii.patchouli.client.book.page;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;

public class PageCrafting extends PageDoubleRecipeRegistry<Recipe<?>> {

    public PageCrafting() {
        super(RecipeType.CRAFTING);
    }

    protected void drawRecipe(GuiGraphics graphics, Recipe<?> recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RenderSystem.enableBlend();
            graphics.blit(this.book.craftingTexture, recipeX - 2, recipeY - 2, 0.0F, 0.0F, 100, 62, 128, 256);
            boolean shaped = recipe instanceof ShapedRecipe;
            if (!shaped) {
                int iconX = recipeX + 62;
                int iconY = recipeY + 2;
                graphics.blit(this.book.craftingTexture, iconX, iconY, 0.0F, 64.0F, 11, 11, 128, 256);
                if (this.parent.isMouseInRelativeRange((double) mouseX, (double) mouseY, iconX, iconY, 11, 11)) {
                    this.parent.setTooltip(new Component[] { Component.translatable("patchouli.gui.lexicon.shapeless") });
                }
            }
            this.parent.drawCenteredStringNoShadow(graphics, this.getTitle(second).getVisualOrderText(), 58, recipeY - 10, this.book.headerColor);
            this.parent.renderItemStack(graphics, recipeX + 79, recipeY + 22, mouseX, mouseY, recipe.getResultItem(level.registryAccess()));
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            int wrap = 3;
            if (shaped) {
                wrap = ((ShapedRecipe) recipe).getWidth();
            }
            for (int i = 0; i < ingredients.size(); i++) {
                this.parent.renderIngredient(graphics, recipeX + i % wrap * 19 + 3, recipeY + i / wrap * 19 + 3, mouseX, mouseY, ingredients.get(i));
            }
            this.parent.renderItemStack(graphics, recipeX + 79, recipeY + 41, mouseX, mouseY, recipe.getToastSymbol());
        }
    }

    @Override
    protected int getRecipeHeight() {
        return 78;
    }

    protected ItemStack getRecipeOutput(Level level, Recipe<?> recipe) {
        return recipe != null && level != null ? recipe.getResultItem(level.registryAccess()) : ItemStack.EMPTY;
    }
}