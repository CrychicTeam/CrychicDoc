package mezz.jei.gui.ingredients;

import com.google.common.base.Preconditions;
import java.util.List;
import mezz.jei.api.recipe.IFocusFactory;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.gui.recipes.FocusedRecipes;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class IngredientLookupState {

    private final IRecipeManager recipeManager;

    private final IFocusGroup focuses;

    @Unmodifiable
    private final List<IRecipeCategory<?>> recipeCategories;

    private int recipeCategoryIndex;

    private int recipeIndex;

    private int recipesPerPage;

    @Nullable
    private FocusedRecipes<?> focusedRecipes;

    public static IngredientLookupState createWithFocus(IRecipeManager recipeManager, IFocusGroup focuses) {
        List<IRecipeCategory<?>> recipeCategories = recipeManager.createRecipeCategoryLookup().limitFocus(focuses.getAllFocuses()).get().toList();
        return new IngredientLookupState(recipeManager, focuses, recipeCategories);
    }

    public static IngredientLookupState createWithCategories(IRecipeManager recipeManager, IFocusFactory focusFactory, List<IRecipeCategory<?>> recipeCategories) {
        return new IngredientLookupState(recipeManager, focusFactory.getEmptyFocusGroup(), recipeCategories);
    }

    private IngredientLookupState(IRecipeManager recipeManager, IFocusGroup focuses, List<IRecipeCategory<?>> recipeCategories) {
        this.recipeManager = recipeManager;
        this.focuses = focuses;
        this.recipeCategories = List.copyOf(recipeCategories);
    }

    public IFocusGroup getFocuses() {
        return this.focuses;
    }

    @Unmodifiable
    public List<IRecipeCategory<?>> getRecipeCategories() {
        return this.recipeCategories;
    }

    public int getRecipeCategoryIndex() {
        return this.recipeCategoryIndex;
    }

    public boolean setRecipeCategory(IRecipeCategory<?> recipeCategory) {
        int recipeCategoryIndex = this.recipeCategories.indexOf(recipeCategory);
        if (recipeCategoryIndex >= 0) {
            this.setRecipeCategoryIndex(recipeCategoryIndex);
            return true;
        } else {
            return false;
        }
    }

    public void setRecipeCategoryIndex(int recipeCategoryIndex) {
        Preconditions.checkArgument(recipeCategoryIndex >= 0, "Recipe category index cannot be negative.");
        this.recipeCategoryIndex = recipeCategoryIndex;
        this.recipeIndex = 0;
        this.focusedRecipes = null;
    }

    public void nextRecipeCategory() {
        int recipesTypesCount = this.getRecipeCategories().size();
        this.setRecipeCategoryIndex((this.getRecipeCategoryIndex() + 1) % recipesTypesCount);
    }

    public void previousRecipeCategory() {
        int recipesTypesCount = this.getRecipeCategories().size();
        this.setRecipeCategoryIndex((recipesTypesCount + this.getRecipeCategoryIndex() - 1) % recipesTypesCount);
    }

    public int getRecipeIndex() {
        return this.recipeIndex;
    }

    public void setRecipeIndex(int recipeIndex) {
        this.recipeIndex = recipeIndex;
    }

    public int getRecipesPerPage() {
        return this.recipesPerPage;
    }

    public void setRecipesPerPage(int recipesPerPage) {
        this.recipesPerPage = recipesPerPage;
    }

    public FocusedRecipes<?> getFocusedRecipes() {
        if (this.focusedRecipes == null) {
            IRecipeCategory<?> recipeCategory = (IRecipeCategory<?>) this.recipeCategories.get(this.recipeCategoryIndex);
            this.focusedRecipes = FocusedRecipes.create(this.focuses, this.recipeManager, recipeCategory);
        }
        return this.focusedRecipes;
    }
}