package mezz.jei.forge.platform;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.platform.IPlatformRecipeHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.SmithingTrimRecipe;
import net.minecraftforge.common.crafting.IShapedRecipe;

public class RecipeHelper implements IPlatformRecipeHelper {

    @Override
    public <T extends CraftingRecipe> int getWidth(T recipe) {
        return recipe instanceof IShapedRecipe<?> shapedRecipe ? shapedRecipe.getRecipeWidth() : 0;
    }

    @Override
    public <T extends CraftingRecipe> int getHeight(T recipe) {
        return recipe instanceof IShapedRecipe<?> shapedRecipe ? shapedRecipe.getRecipeHeight() : 0;
    }

    @Override
    public Ingredient getBase(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.base;
        } else {
            return recipe instanceof SmithingTrimRecipe trimRecipe ? trimRecipe.base : Ingredient.EMPTY;
        }
    }

    @Override
    public Ingredient getAddition(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.addition;
        } else {
            return recipe instanceof SmithingTrimRecipe trimRecipe ? trimRecipe.addition : Ingredient.EMPTY;
        }
    }

    @Override
    public Ingredient getTemplate(SmithingRecipe recipe) {
        if (recipe instanceof SmithingTransformRecipe transformRecipe) {
            return transformRecipe.template;
        } else {
            return recipe instanceof SmithingTrimRecipe trimRecipe ? trimRecipe.template : Ingredient.EMPTY;
        }
    }

    @Override
    public boolean isHandled(SmithingRecipe recipe) {
        return recipe.m_142505_() ? false : recipe instanceof SmithingTransformRecipe || recipe instanceof SmithingTrimRecipe;
    }

    @Override
    public Optional<ResourceLocation> getRegistryNameForRecipe(Recipe<?> recipe) {
        ResourceLocation id = recipe.getId();
        return Optional.ofNullable(id);
    }

    @Override
    public List<IJeiBrewingRecipe> getBrewingRecipes(IIngredientManager ingredientManager, IVanillaRecipeFactory vanillaRecipeFactory) {
        return BrewingRecipeMaker.getBrewingRecipes(ingredientManager, vanillaRecipeFactory);
    }
}