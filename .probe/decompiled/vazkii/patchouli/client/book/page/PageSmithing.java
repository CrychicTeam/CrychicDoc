package vazkii.patchouli.client.book.page;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraft.world.level.Level;
import vazkii.patchouli.client.book.page.abstr.PageDoubleRecipeRegistry;
import vazkii.patchouli.mixin.AccessorSmithingTransformRecipe;
import vazkii.patchouli.mixin.AccessorSmithingTrimRecipe;

public class PageSmithing extends PageDoubleRecipeRegistry<SmithingRecipe> {

    public PageSmithing() {
        super(RecipeType.SMITHING);
    }

    protected void drawRecipe(GuiGraphics graphics, SmithingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RenderSystem.enableBlend();
            graphics.blit(this.book.craftingTexture, recipeX, recipeY, 11.0F, 135.0F, 96, 43, 128, 256);
            this.parent.drawCenteredStringNoShadow(graphics, this.getTitle(second).getVisualOrderText(), 58, recipeY - 10, this.book.headerColor);
            this.parent.renderIngredient(graphics, recipeX + 4, recipeY + 4, mouseX, mouseY, this.getBase(recipe));
            this.parent.renderIngredient(graphics, recipeX + 4, recipeY + 23, mouseX, mouseY, this.getAddition(recipe));
            this.parent.renderItemStack(graphics, recipeX + 40, recipeY + 13, mouseX, mouseY, recipe.getToastSymbol());
            this.parent.renderItemStack(graphics, recipeX + 76, recipeY + 13, mouseX, mouseY, recipe.m_8043_(level.registryAccess()));
        }
    }

    private Ingredient getBase(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTrimRecipe) {
            return ((AccessorSmithingTrimRecipe) recipe).getBase();
        } else {
            return recipe instanceof SmithingTransformRecipe ? ((AccessorSmithingTransformRecipe) recipe).getBase() : Ingredient.EMPTY;
        }
    }

    private Ingredient getAddition(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTrimRecipe) {
            return ((AccessorSmithingTrimRecipe) recipe).getAddition();
        } else {
            return recipe instanceof SmithingTransformRecipe ? ((AccessorSmithingTransformRecipe) recipe).getAddition() : Ingredient.EMPTY;
        }
    }

    private Ingredient getTemplate(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTrimRecipe) {
            return ((AccessorSmithingTrimRecipe) recipe).getTemplate();
        } else {
            return recipe instanceof SmithingTransformRecipe ? ((AccessorSmithingTransformRecipe) recipe).getTemplate() : Ingredient.EMPTY;
        }
    }

    protected ItemStack getRecipeOutput(Level level, SmithingRecipe recipe) {
        return recipe != null && level != null ? recipe.m_8043_(level.registryAccess()) : ItemStack.EMPTY;
    }

    @Override
    protected int getRecipeHeight() {
        return 60;
    }
}