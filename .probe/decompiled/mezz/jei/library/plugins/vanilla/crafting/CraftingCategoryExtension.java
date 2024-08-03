package mezz.jei.library.plugins.vanilla.crafting;

import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension;
import mezz.jei.common.platform.IPlatformRecipeHelper;
import mezz.jei.common.platform.Services;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.jetbrains.annotations.Nullable;

public class CraftingCategoryExtension<T extends CraftingRecipe> implements ICraftingCategoryExtension {

    protected final T recipe;

    public CraftingCategoryExtension(T recipe) {
        this.recipe = recipe;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        List<List<ItemStack>> inputs = this.recipe.m_7527_().stream().map(ingredient -> List.of(ingredient.getItems())).toList();
        ItemStack resultItem = RecipeUtil.getResultItem(this.recipe);
        int width = this.getWidth();
        int height = this.getHeight();
        craftingGridHelper.createAndSetOutputs(builder, List.of(resultItem));
        craftingGridHelper.createAndSetInputs(builder, inputs, width, height);
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return this.recipe.m_6423_();
    }

    @Override
    public int getWidth() {
        IPlatformRecipeHelper recipeHelper = Services.PLATFORM.getRecipeHelper();
        return recipeHelper.getWidth(this.recipe);
    }

    @Override
    public int getHeight() {
        IPlatformRecipeHelper recipeHelper = Services.PLATFORM.getRecipeHelper();
        return recipeHelper.getHeight(this.recipe);
    }
}