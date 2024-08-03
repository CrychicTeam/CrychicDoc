package mezz.jei.gui.recipes;

import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.gui.IRecipeLayoutDrawable;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import org.jetbrains.annotations.Unmodifiable;

public interface IRecipeGuiLogic {

    String getPageString();

    void setRecipesPerPage(int var1);

    boolean hasMultipleCategories();

    boolean hasAllCategories();

    void previousRecipeCategory();

    void nextRecipeCategory();

    void setRecipeCategory(IRecipeCategory<?> var1);

    boolean hasMultiplePages();

    void previousPage();

    void nextPage();

    boolean setFocus(IFocusGroup var1);

    boolean back();

    void clearHistory();

    boolean setCategoryFocus();

    boolean setCategoryFocus(List<RecipeType<?>> var1);

    IRecipeCategory<?> getSelectedRecipeCategory();

    @Unmodifiable
    List<IRecipeCategory<?>> getRecipeCategories();

    Stream<ITypedIngredient<?>> getRecipeCatalysts();

    Stream<ITypedIngredient<?>> getRecipeCatalysts(IRecipeCategory<?> var1);

    List<IRecipeLayoutDrawable<?>> getRecipeLayouts();
}