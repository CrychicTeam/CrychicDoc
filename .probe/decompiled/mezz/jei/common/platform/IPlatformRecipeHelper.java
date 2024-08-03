package mezz.jei.common.platform;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.SmithingRecipe;

public interface IPlatformRecipeHelper {

    <T extends CraftingRecipe> int getWidth(T var1);

    <T extends CraftingRecipe> int getHeight(T var1);

    Ingredient getBase(SmithingRecipe var1);

    Ingredient getAddition(SmithingRecipe var1);

    Ingredient getTemplate(SmithingRecipe var1);

    boolean isHandled(SmithingRecipe var1);

    Optional<ResourceLocation> getRegistryNameForRecipe(Recipe<?> var1);

    List<IJeiBrewingRecipe> getBrewingRecipes(IIngredientManager var1, IVanillaRecipeFactory var2);
}