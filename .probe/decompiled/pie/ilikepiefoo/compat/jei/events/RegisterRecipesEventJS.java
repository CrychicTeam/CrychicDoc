package pie.ilikepiefoo.compat.jei.events;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import pie.ilikepiefoo.compat.jei.impl.CustomJSRecipe;

public class RegisterRecipesEventJS extends JEIEventJS {

    public final IRecipeRegistration data;

    public final List<CustomJSRecipe.CustomRecipeListBuilder> customRecipeListBuilders = new ArrayList();

    public RegisterRecipesEventJS(IRecipeRegistration data) {
        this.data = data;
    }

    public <T> void register(RecipeType<T> recipeType, List<T> recipes) {
        this.data.addRecipes(recipeType, recipes);
    }

    public CustomJSRecipe.CustomRecipeListBuilder custom(ResourceLocation recipeType) {
        CustomJSRecipe.CustomRecipeListBuilder recipeListBuilder = new CustomJSRecipe.CustomRecipeListBuilder(getOrCreateCustomRecipeType(recipeType));
        this.customRecipeListBuilders.add(recipeListBuilder);
        return recipeListBuilder;
    }
}