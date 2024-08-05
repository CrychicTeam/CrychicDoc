package mezz.jei.library.recipes;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotDrawable;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeCatalystLookup;
import mezz.jei.api.recipe.IRecipeCategoriesLookup;
import mezz.jei.api.recipe.IRecipeLookup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.gui.textures.Textures;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.gui.ingredients.RecipeSlot;
import mezz.jei.library.gui.recipes.RecipeLayout;
import net.minecraft.resources.ResourceLocation;

public class RecipeManager implements IRecipeManager {

    private final RecipeManagerInternal internal;

    private final IModIdHelper modIdHelper;

    private final IIngredientManager ingredientManager;

    private final Textures textures;

    private final IIngredientVisibility ingredientVisibility;

    public RecipeManager(RecipeManagerInternal internal, IModIdHelper modIdHelper, IIngredientManager ingredientManager, Textures textures, IIngredientVisibility ingredientVisibility) {
        this.internal = internal;
        this.modIdHelper = modIdHelper;
        this.ingredientManager = ingredientManager;
        this.textures = textures;
        this.ingredientVisibility = ingredientVisibility;
    }

    @Override
    public <R> IRecipeLookup<R> createRecipeLookup(RecipeType<R> recipeType) {
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        return new RecipeLookup<>(recipeType, this.internal, this.ingredientManager);
    }

    @Override
    public IRecipeCategoriesLookup createRecipeCategoryLookup() {
        return new RecipeCategoriesLookup(this.internal, this.ingredientManager);
    }

    @Override
    public IRecipeCatalystLookup createRecipeCatalystLookup(RecipeType<?> recipeType) {
        return new RecipeCatalystLookup(recipeType, this.internal);
    }

    @Override
    public <T> void addRecipes(RecipeType<T> recipeType, List<T> recipes) {
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        ErrorUtil.checkNotNull(recipes, "recipes");
        ErrorUtil.assertMainThread();
        this.internal.addRecipes(recipeType, recipes);
    }

    @Override
    public <T> Optional<IRecipeLayoutDrawable<T>> createRecipeLayoutDrawable(IRecipeCategory<T> recipeCategory, T recipe, IFocusGroup focusGroup) {
        ErrorUtil.checkNotNull(recipeCategory, "recipeCategory");
        ErrorUtil.checkNotNull(recipe, "recipe");
        ErrorUtil.checkNotNull(focusGroup, "focusGroup");
        RecipeType<T> recipeType = recipeCategory.getRecipeType();
        Collection<IRecipeCategoryDecorator<T>> decorators = this.internal.<T>getRecipeCategoryDecorators(recipeType);
        return RecipeLayout.create(recipeCategory, decorators, recipe, focusGroup, this.ingredientManager, this.ingredientVisibility, this.modIdHelper, this.textures);
    }

    @Override
    public IRecipeSlotDrawable createRecipeSlotDrawable(RecipeIngredientRole role, List<Optional<ITypedIngredient<?>>> ingredients, Set<Integer> focusedIngredients, int xPos, int yPos, int ingredientCycleOffset) {
        RecipeSlot recipeSlot = new RecipeSlot(this.ingredientManager, role, xPos, yPos, ingredientCycleOffset);
        recipeSlot.set(ingredients, focusedIngredients, this.ingredientVisibility);
        return recipeSlot;
    }

    @Override
    public <T> void hideRecipes(RecipeType<T> recipeType, Collection<T> recipes) {
        ErrorUtil.checkNotNull(recipes, "recipe");
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        ErrorUtil.assertMainThread();
        this.internal.hideRecipes(recipeType, recipes);
    }

    @Override
    public <T> void unhideRecipes(RecipeType<T> recipeType, Collection<T> recipes) {
        ErrorUtil.checkNotNull(recipes, "recipe");
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        ErrorUtil.assertMainThread();
        this.internal.unhideRecipes(recipeType, recipes);
    }

    @Override
    public void hideRecipeCategory(RecipeType<?> recipeType) {
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        ErrorUtil.assertMainThread();
        this.internal.hideRecipeCategory(recipeType);
    }

    @Override
    public void unhideRecipeCategory(RecipeType<?> recipeType) {
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        ErrorUtil.assertMainThread();
        this.internal.unhideRecipeCategory(recipeType);
    }

    @Override
    public Optional<RecipeType<?>> getRecipeType(ResourceLocation recipeUid) {
        return this.internal.getRecipeType(recipeUid);
    }
}