package vazkii.patchouli.client.book.page.abstr;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class PageSimpleProcessingRecipe<T extends Recipe<?>> extends PageDoubleRecipeRegistry<T> {

    public PageSimpleProcessingRecipe(RecipeType<T> recipeType) {
        super(recipeType);
    }

    protected void drawRecipe(GuiGraphics graphics, T recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RenderSystem.enableBlend();
            graphics.blit(this.book.craftingTexture, recipeX, recipeY, 11.0F, 71.0F, 96, 24, 128, 256);
            this.parent.drawCenteredStringNoShadow(graphics, this.getTitle(second).getVisualOrderText(), 58, recipeY - 10, this.book.headerColor);
            this.parent.renderIngredient(graphics, recipeX + 4, recipeY + 4, mouseX, mouseY, recipe.getIngredients().get(0));
            this.parent.renderItemStack(graphics, recipeX + 40, recipeY + 4, mouseX, mouseY, recipe.getToastSymbol());
            this.parent.renderItemStack(graphics, recipeX + 76, recipeY + 4, mouseX, mouseY, recipe.getResultItem(level.registryAccess()));
        }
    }

    protected ItemStack getRecipeOutput(Level level, T recipe) {
        return recipe != null && level != null ? recipe.getResultItem(level.registryAccess()) : ItemStack.EMPTY;
    }

    @Override
    protected int getRecipeHeight() {
        return 45;
    }
}