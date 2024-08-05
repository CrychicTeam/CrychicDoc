package mezz.jei.library.recipes.collect;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.core.collect.SetMultiMap;
import mezz.jei.library.ingredients.IIngredientSupplier;
import org.jetbrains.annotations.UnmodifiableView;

public class RecipeMap {

    private final RecipeIngredientTable recipeTable = new RecipeIngredientTable();

    private final SetMultiMap<String, RecipeType<?>> ingredientUidToCategoryMap = new SetMultiMap<>();

    private final SetMultiMap<String, RecipeType<?>> categoryCatalystUidToRecipeCategoryMap = new SetMultiMap<>();

    private final Comparator<RecipeType<?>> recipeTypeComparator;

    private final IIngredientManager ingredientManager;

    private final RecipeIngredientRole role;

    public RecipeMap(Comparator<RecipeType<?>> recipeTypeComparator, IIngredientManager ingredientManager, RecipeIngredientRole role) {
        this.recipeTypeComparator = recipeTypeComparator;
        this.ingredientManager = ingredientManager;
        this.role = role;
    }

    public Stream<RecipeType<?>> getRecipeTypes(String ingredientUid) {
        Collection<RecipeType<?>> recipeCategoryUids = this.ingredientUidToCategoryMap.get(ingredientUid);
        Collection<RecipeType<?>> catalystRecipeCategoryUids = this.categoryCatalystUidToRecipeCategoryMap.get(ingredientUid);
        return Stream.concat(recipeCategoryUids.stream(), catalystRecipeCategoryUids.stream()).sorted(this.recipeTypeComparator);
    }

    public void addCatalystForCategory(RecipeType<?> recipeType, String ingredientUid) {
        this.categoryCatalystUidToRecipeCategoryMap.put(ingredientUid, recipeType);
    }

    @UnmodifiableView
    public <T> List<T> getRecipes(RecipeType<T> recipeType, String ingredientUid) {
        return this.recipeTable.get(recipeType, ingredientUid);
    }

    public <T> boolean isCatalystForRecipeCategory(RecipeType<T> recipeType, String ingredientUid) {
        Collection<RecipeType<?>> catalystCategories = this.categoryCatalystUidToRecipeCategoryMap.get(ingredientUid);
        return catalystCategories.contains(recipeType);
    }

    public <T> void addRecipe(RecipeType<T> recipeType, T recipe, IIngredientSupplier ingredientSupplier) {
        ingredientSupplier.getIngredientTypes(this.role).forEach(ingredientType -> this.addRecipe(recipe, recipeType, ingredientSupplier, ingredientType));
    }

    private <T, V> void addRecipe(T recipe, RecipeType<T> recipeType, IIngredientSupplier ingredientSupplier, IIngredientType<V> ingredientType) {
        IIngredientHelper<V> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredientType);
        List<String> ingredientUids = ingredientSupplier.getIngredientStream(ingredientType, this.role).filter(ingredientHelper::isValidIngredient).map(i -> ingredientHelper.getUniqueId((V) i, UidContext.Recipe)).distinct().toList();
        if (!ingredientUids.isEmpty()) {
            for (String uid : ingredientUids) {
                this.ingredientUidToCategoryMap.put(uid, recipeType);
            }
            this.recipeTable.add(recipe, recipeType, ingredientUids);
        }
    }
}