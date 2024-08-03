package mezz.jei.library.plugins.debug;

import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

class DebugCategoryDecorator<T> implements IRecipeCategoryDecorator<T> {

    private static final DebugCategoryDecorator<?> INSTANCE = new DebugCategoryDecorator();

    public static <T> DebugCategoryDecorator<T> getInstance() {
        return (DebugCategoryDecorator<T>) INSTANCE;
    }

    @Override
    public void draw(T recipe, IRecipeCategory<T> recipeCategory, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        ResourceLocation id = recipeCategory.getRegistryName(recipe);
        if (id != null) {
            int posX = recipeCategory.getWidth() / 2;
            int posY = recipeCategory.getHeight();
            Minecraft minecraft = Minecraft.getInstance();
            guiGraphics.drawCenteredString(minecraft.font, "Decorator: " + id, posX, posY, 16777215);
        }
    }
}