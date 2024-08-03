package mezz.jei.library.plugins.vanilla.crafting;

import java.util.List;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.library.util.RecipeErrorUtil;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CategoryRecipeValidator<T extends Recipe<?>> {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final int INVALID_COUNT = -1;

    private final IRecipeCategory<T> recipeCategory;

    private final IIngredientManager ingredientManager;

    private final int maxInputs;

    public CategoryRecipeValidator(IRecipeCategory<T> recipeCategory, IIngredientManager ingredientManager, int maxInputs) {
        this.recipeCategory = recipeCategory;
        this.ingredientManager = ingredientManager;
        this.maxInputs = maxInputs;
    }

    public boolean isRecipeValid(T recipe) {
        return this.hasValidInputsAndOutputs(recipe);
    }

    public boolean isRecipeHandled(T recipe) {
        return this.recipeCategory.isHandled(recipe);
    }

    private boolean hasValidInputsAndOutputs(T recipe) {
        if (recipe.isSpecial()) {
            return true;
        } else {
            ItemStack recipeOutput = RecipeUtil.getResultItem(recipe);
            if (recipeOutput != null && !recipeOutput.isEmpty()) {
                List<Ingredient> ingredients = recipe.getIngredients();
                if (ingredients == null) {
                    if (LOGGER.isDebugEnabled()) {
                        String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipe, this.recipeCategory, this.ingredientManager);
                        LOGGER.debug("Skipping Recipe because it has no input Ingredients. {}", recipeInfo);
                    }
                    return false;
                } else {
                    int inputCount = getInputCount(ingredients);
                    if (inputCount == -1) {
                        if (LOGGER.isDebugEnabled()) {
                            String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipe, this.recipeCategory, this.ingredientManager);
                            LOGGER.debug("Skipping Recipe because it contains invalid inputs. {}", recipeInfo);
                        }
                        return false;
                    } else if (inputCount > this.maxInputs) {
                        if (LOGGER.isDebugEnabled()) {
                            String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipe, this.recipeCategory, this.ingredientManager);
                            LOGGER.debug("Skipping Recipe because it has too many inputs. {}", recipeInfo);
                        }
                        return false;
                    } else if (inputCount == 0 && this.maxInputs > 0) {
                        if (LOGGER.isDebugEnabled()) {
                            String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipe, this.recipeCategory, this.ingredientManager);
                            LOGGER.debug("Skipping Recipe because it has no inputs. {}", recipeInfo);
                        }
                        return false;
                    } else {
                        return true;
                    }
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipe, this.recipeCategory, this.ingredientManager);
                    LOGGER.debug("Skipping Recipe because it has no output. {}", recipeInfo);
                }
                return false;
            }
        }
    }

    private static int getInputCount(List<Ingredient> ingredientList) {
        int inputCount = 0;
        for (Ingredient ingredient : ingredientList) {
            ItemStack[] input = ingredient.getItems();
            if (input == null) {
                return -1;
            }
            inputCount++;
        }
        return inputCount;
    }
}