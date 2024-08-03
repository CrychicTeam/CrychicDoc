package mezz.jei.library.recipes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.runtime.IIngredientVisibility;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.config.RecipeCategorySortingConfig;
import mezz.jei.library.ingredients.IIngredientSupplier;
import mezz.jei.library.recipes.collect.RecipeMap;
import mezz.jei.library.recipes.collect.RecipeTypeData;
import mezz.jei.library.recipes.collect.RecipeTypeDataMap;
import mezz.jei.library.util.IngredientSupplierHelper;
import mezz.jei.library.util.RecipeErrorUtil;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public class RecipeManagerInternal {

    private static final Logger LOGGER = LogManager.getLogger();

    @Unmodifiable
    private final List<IRecipeCategory<?>> recipeCategories;

    private final ImmutableListMultimap<RecipeType<?>, IRecipeCategoryDecorator<?>> recipeCategoryDecorators;

    private final IIngredientManager ingredientManager;

    private final RecipeTypeDataMap recipeTypeDataMap;

    private final Comparator<IRecipeCategory<?>> recipeCategoryComparator;

    private final EnumMap<RecipeIngredientRole, RecipeMap> recipeMaps;

    private final PluginManager pluginManager;

    private final Set<RecipeType<?>> hiddenRecipeTypes = new HashSet();

    private final IIngredientVisibility ingredientVisibility;

    @Nullable
    @Unmodifiable
    private List<IRecipeCategory<?>> recipeCategoriesVisibleCache = null;

    public RecipeManagerInternal(List<IRecipeCategory<?>> recipeCategories, ImmutableListMultimap<ResourceLocation, ITypedIngredient<?>> recipeCatalysts, ImmutableListMultimap<RecipeType<?>, IRecipeCategoryDecorator<?>> recipeCategoryDecorators, IIngredientManager ingredientManager, List<IRecipeManagerPlugin> plugins, RecipeCategorySortingConfig recipeCategorySortingConfig, IIngredientVisibility ingredientVisibility) {
        ErrorUtil.checkNotEmpty(recipeCategories, "recipeCategories");
        this.recipeCategoryDecorators = recipeCategoryDecorators;
        this.ingredientManager = ingredientManager;
        this.ingredientVisibility = ingredientVisibility;
        Collection<RecipeType<?>> recipeTypes = recipeCategories.stream().map(IRecipeCategory::getRecipeType).toList();
        Comparator<RecipeType<?>> recipeTypeComparator = recipeCategorySortingConfig.getComparator(recipeTypes);
        this.recipeMaps = new EnumMap(RecipeIngredientRole.class);
        for (RecipeIngredientRole role : RecipeIngredientRole.values()) {
            RecipeMap recipeMap = new RecipeMap(recipeTypeComparator, ingredientManager, role);
            this.recipeMaps.put(role, recipeMap);
        }
        this.recipeCategoryComparator = Comparator.comparing(IRecipeCategory::getRecipeType, recipeTypeComparator);
        this.recipeCategories = recipeCategories.stream().sorted(this.recipeCategoryComparator).toList();
        RecipeCatalystBuilder recipeCatalystBuilder = new RecipeCatalystBuilder(ingredientManager, (RecipeMap) this.recipeMaps.get(RecipeIngredientRole.CATALYST));
        for (IRecipeCategory<?> recipeCategory : recipeCategories) {
            ResourceLocation recipeCategoryUid = recipeCategory.getRecipeType().getUid();
            if (recipeCatalysts.containsKey(recipeCategoryUid)) {
                List<ITypedIngredient<?>> catalysts = recipeCatalysts.get(recipeCategoryUid);
                recipeCatalystBuilder.addCategoryCatalysts(recipeCategory, catalysts);
            }
        }
        ImmutableListMultimap<IRecipeCategory<?>, ITypedIngredient<?>> recipeCategoryCatalystsMap = recipeCatalystBuilder.buildRecipeCategoryCatalysts();
        this.recipeTypeDataMap = new RecipeTypeDataMap(recipeCategories, recipeCategoryCatalystsMap);
        IRecipeManagerPlugin internalRecipeManagerPlugin = new InternalRecipeManagerPlugin(ingredientManager, this.recipeTypeDataMap, this.recipeMaps);
        this.pluginManager = new PluginManager(internalRecipeManagerPlugin, plugins);
    }

    public <T> void addRecipes(RecipeType<T> recipeType, List<T> recipes) {
        LOGGER.debug("Adding recipes: " + recipeType.getUid());
        RecipeTypeData<T> recipeTypeData = this.recipeTypeDataMap.get(recipes, recipeType);
        this.addRecipes(recipeTypeData, recipes);
    }

    private <T> void addRecipes(RecipeTypeData<T> recipeTypeData, Collection<T> recipes) {
        IRecipeCategory<T> recipeCategory = recipeTypeData.getRecipeCategory();
        Set<T> hiddenRecipes = recipeTypeData.getHiddenRecipes();
        List<T> addedRecipes = recipes.stream().filter(recipe -> {
            if (!hiddenRecipes.contains(recipe) && recipeCategory.isHandled((T) recipe)) {
                IIngredientSupplier ingredientSupplier = IngredientSupplierHelper.getIngredientSupplier(recipe, recipeCategory, this.ingredientManager);
                return ingredientSupplier == null ? false : this.addRecipe(recipeCategory, (T) recipe, ingredientSupplier);
            } else {
                return false;
            }
        }).toList();
        if (!addedRecipes.isEmpty()) {
            recipeTypeData.addRecipes(addedRecipes);
            this.recipeCategoriesVisibleCache = null;
        }
    }

    private <T> boolean addRecipe(IRecipeCategory<T> recipeCategory, T recipe, IIngredientSupplier ingredientSupplier) {
        RecipeType<T> recipeType = recipeCategory.getRecipeType();
        try {
            for (RecipeMap recipeMap : this.recipeMaps.values()) {
                recipeMap.addRecipe(recipeType, recipe, ingredientSupplier);
            }
            return true;
        } catch (LinkageError | RuntimeException var7) {
            String recipeInfo = RecipeErrorUtil.getInfoFromRecipe(recipe, recipeCategory, this.ingredientManager);
            LOGGER.error("Found a broken recipe, failed to addRecipe: {}\n", recipeInfo, var7);
            return false;
        }
    }

    public boolean isCategoryHidden(IRecipeCategory<?> recipeCategory, IFocusGroup focuses) {
        RecipeType<?> recipeType = recipeCategory.getRecipeType();
        if (this.hiddenRecipeTypes.contains(recipeType)) {
            return true;
        } else if (this.getRecipeCatalystStream(recipeType, true).findAny().isPresent() && this.getRecipeCatalystStream(recipeType, false).findAny().isEmpty()) {
            return true;
        } else {
            Stream<?> visibleRecipes = this.getRecipesStream(recipeType, focuses, false);
            return visibleRecipes.findAny().isEmpty();
        }
    }

    public Stream<IRecipeCategory<?>> getRecipeCategoriesForTypes(Collection<RecipeType<?>> recipeTypes, IFocusGroup focuses, boolean includeHidden) {
        List<IRecipeCategory<?>> recipeCategories = recipeTypes.stream().map(this.recipeTypeDataMap::get).map(RecipeTypeData::getRecipeCategory).toList();
        return this.getRecipeCategoriesCached(recipeCategories, focuses, includeHidden);
    }

    private Stream<IRecipeCategory<?>> getRecipeCategoriesCached(Collection<IRecipeCategory<?>> recipeCategories, IFocusGroup focuses, boolean includeHidden) {
        if (recipeCategories.isEmpty() && focuses.isEmpty() && !includeHidden) {
            if (this.recipeCategoriesVisibleCache == null) {
                this.recipeCategoriesVisibleCache = this.getRecipeCategoriesUncached(recipeCategories, focuses, includeHidden).toList();
            }
            return this.recipeCategoriesVisibleCache.stream();
        } else {
            return this.getRecipeCategoriesUncached(recipeCategories, focuses, includeHidden);
        }
    }

    private Stream<IRecipeCategory<?>> getRecipeCategoriesUncached(Collection<IRecipeCategory<?>> recipeCategories, IFocusGroup focuses, boolean includeHidden) {
        Stream<IRecipeCategory<?>> categoryStream;
        if (focuses.isEmpty()) {
            if (recipeCategories.isEmpty()) {
                categoryStream = this.recipeCategories.stream();
            } else {
                categoryStream = recipeCategories.stream().distinct();
            }
        } else {
            categoryStream = this.pluginManager.getRecipeTypes(focuses).map(this.recipeTypeDataMap::get).map(RecipeTypeData::getRecipeCategory);
            if (!recipeCategories.isEmpty()) {
                categoryStream = categoryStream.filter(recipeCategories::contains);
            }
        }
        if (!includeHidden) {
            categoryStream = categoryStream.filter(c -> !this.isCategoryHidden(c, focuses));
        }
        return categoryStream.sorted(this.recipeCategoryComparator);
    }

    public <T> Stream<T> getRecipesStream(RecipeType<T> recipeType, IFocusGroup focuses, boolean includeHidden) {
        RecipeTypeData<T> recipeTypeData = this.recipeTypeDataMap.get(recipeType);
        return this.pluginManager.getRecipes(recipeTypeData, focuses, includeHidden);
    }

    public <T> Stream<ITypedIngredient<?>> getRecipeCatalystStream(RecipeType<T> recipeType, boolean includeHidden) {
        RecipeTypeData<T> recipeTypeData = this.recipeTypeDataMap.get(recipeType);
        List<ITypedIngredient<?>> catalysts = recipeTypeData.getRecipeCategoryCatalysts();
        return includeHidden ? catalysts.stream() : catalysts.stream().filter(this.ingredientVisibility::isIngredientVisible);
    }

    public <T> void hideRecipes(RecipeType<T> recipeType, Collection<T> recipes) {
        RecipeTypeData<T> recipeTypeData = this.recipeTypeDataMap.get(recipes, recipeType);
        Set<T> hiddenRecipes = recipeTypeData.getHiddenRecipes();
        hiddenRecipes.addAll(recipes);
        this.recipeCategoriesVisibleCache = null;
    }

    public <T> void unhideRecipes(RecipeType<T> recipeType, Collection<T> recipes) {
        RecipeTypeData<T> recipeTypeData = this.recipeTypeDataMap.get(recipes, recipeType);
        Set<T> hiddenRecipes = recipeTypeData.getHiddenRecipes();
        hiddenRecipes.removeAll(recipes);
        this.recipeCategoriesVisibleCache = null;
    }

    public void hideRecipeCategory(RecipeType<?> recipeType) {
        this.hiddenRecipeTypes.add(recipeType);
        this.recipeCategoriesVisibleCache = null;
    }

    public void unhideRecipeCategory(RecipeType<?> recipeType) {
        this.recipeTypeDataMap.validate(recipeType);
        this.hiddenRecipeTypes.remove(recipeType);
        this.recipeCategoriesVisibleCache = null;
    }

    public Optional<RecipeType<?>> getRecipeType(ResourceLocation recipeUid) {
        return this.recipeTypeDataMap.getType(recipeUid);
    }

    @Unmodifiable
    public <T> List<IRecipeCategoryDecorator<T>> getRecipeCategoryDecorators(RecipeType<T> recipeType) {
        ImmutableList<IRecipeCategoryDecorator<?>> decorators = this.recipeCategoryDecorators.get(recipeType);
        return (List<IRecipeCategoryDecorator<T>>) decorators;
    }
}