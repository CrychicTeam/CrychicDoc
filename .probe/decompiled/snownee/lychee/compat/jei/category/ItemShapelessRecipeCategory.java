package snownee.lychee.compat.jei.category;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.gui.GuiGraphics;
import snownee.lychee.core.ItemShapelessContext;
import snownee.lychee.core.recipe.ItemShapelessRecipe;
import snownee.lychee.core.recipe.type.LycheeRecipeType;

public abstract class ItemShapelessRecipeCategory<T extends ItemShapelessRecipe<T>> extends BaseJEICategory<ItemShapelessContext, T> {

    public ItemShapelessRecipeCategory(LycheeRecipeType<ItemShapelessContext, T> recipeType) {
        super(recipeType);
    }

    @Override
    public int getWidth() {
        return 169;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        int xCenter = this.getWidth() / 2;
        int y = recipe.getIngredients().size() <= 9 && recipe.showingActionsCount() <= 9 ? 28 : 26;
        this.ingredientGroup(builder, recipe, xCenter - 45, y);
        this.actionGroup(builder, recipe, xCenter + 50, y);
        addBlockIngredients(builder, recipe);
    }

    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.drawInfoBadge(recipe, graphics, mouseX, mouseY);
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(76.0F, 16.0F, 0.0F);
        matrixStack.scale(1.5F, 1.5F, 1.5F);
        this.getIcon().draw(graphics);
        matrixStack.popPose();
    }
}