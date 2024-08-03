package mezz.jei.library.load.registration;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.runtime.JeiHelpers;
import org.jetbrains.annotations.Unmodifiable;

public class RecipeCategoryRegistration implements IRecipeCategoryRegistration {

    private final List<IRecipeCategory<?>> recipeCategories = new ArrayList();

    private final Set<RecipeType<?>> recipeTypes = new HashSet();

    private final JeiHelpers jeiHelpers;

    public RecipeCategoryRegistration(JeiHelpers jeiHelpers) {
        this.jeiHelpers = jeiHelpers;
    }

    @Override
    public void addRecipeCategories(IRecipeCategory<?>... recipeCategories) {
        ErrorUtil.checkNotEmpty(recipeCategories, "recipeCategories");
        for (IRecipeCategory<?> recipeCategory : recipeCategories) {
            RecipeType<?> recipeType = recipeCategory.getRecipeType();
            Preconditions.checkNotNull(recipeType, "Recipe type cannot be null %s", recipeCategory);
            if (this.recipeTypes.contains(recipeType)) {
                throw new IllegalArgumentException("A RecipeCategory with type \"" + recipeType.getUid() + "\" has already been registered.");
            }
            this.recipeTypes.add(recipeType);
        }
        Collections.addAll(this.recipeCategories, recipeCategories);
        this.jeiHelpers.setRecipeCategories(Collections.unmodifiableCollection(this.recipeCategories));
    }

    @Override
    public IJeiHelpers getJeiHelpers() {
        return this.jeiHelpers;
    }

    @Unmodifiable
    public List<IRecipeCategory<?>> getRecipeCategories() {
        return List.copyOf(this.recipeCategories);
    }
}