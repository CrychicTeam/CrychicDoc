package vectorwing.farmersdelight.integration.jei;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class FDRecipes {

    private final RecipeManager recipeManager;

    public FDRecipes() {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level != null) {
            this.recipeManager = level.getRecipeManager();
        } else {
            throw new NullPointerException("minecraft world must not be null.");
        }
    }

    public List<CookingPotRecipe> getCookingPotRecipes() {
        return this.recipeManager.<RecipeWrapper, CookingPotRecipe>getAllRecipesFor(ModRecipeTypes.COOKING.get()).stream().toList();
    }

    public List<CuttingBoardRecipe> getCuttingBoardRecipes() {
        return this.recipeManager.<RecipeWrapper, CuttingBoardRecipe>getAllRecipesFor(ModRecipeTypes.CUTTING.get()).stream().toList();
    }
}