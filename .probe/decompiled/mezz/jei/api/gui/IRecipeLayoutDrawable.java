package mezz.jei.api.gui;

import java.util.Optional;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.world.item.ItemStack;

public interface IRecipeLayoutDrawable<R> {

    void setPosition(int var1, int var2);

    void drawRecipe(GuiGraphics var1, int var2, int var3);

    void drawOverlays(GuiGraphics var1, int var2, int var3);

    boolean isMouseOver(double var1, double var3);

    default Optional<ItemStack> getItemStackUnderMouse(int mouseX, int mouseY) {
        return this.getIngredientUnderMouse(mouseX, mouseY, VanillaTypes.ITEM_STACK);
    }

    <T> Optional<T> getIngredientUnderMouse(int var1, int var2, IIngredientType<T> var3);

    Optional<IRecipeSlotDrawable> getRecipeSlotUnderMouse(double var1, double var3);

    Rect2i getRect();

    Rect2i getRecipeTransferButtonArea();

    IRecipeSlotsView getRecipeSlotsView();

    IRecipeCategory<R> getRecipeCategory();

    R getRecipe();
}