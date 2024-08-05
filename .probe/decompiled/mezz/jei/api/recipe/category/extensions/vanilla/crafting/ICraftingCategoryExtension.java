package mezz.jei.api.recipe.category.extensions.vanilla.crafting;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface ICraftingCategoryExtension extends IRecipeCategoryExtension {

    void setRecipe(IRecipeLayoutBuilder var1, ICraftingGridHelper var2, IFocusGroup var3);

    @Nullable
    default ResourceLocation getRegistryName() {
        return null;
    }

    default int getWidth() {
        return 0;
    }

    default int getHeight() {
        return 0;
    }
}