package mezz.jei.library.load.registration;

import com.google.common.collect.ImmutableListMultimap;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryDecorator;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.runtime.IJeiFeatures;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.core.collect.ListMultiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Unmodifiable;

public class AdvancedRegistration implements IAdvancedRegistration {

    private static final Logger LOGGER = LogManager.getLogger();

    private final List<IRecipeManagerPlugin> recipeManagerPlugins = new ArrayList();

    private final ListMultiMap<RecipeType<?>, IRecipeCategoryDecorator<?>> recipeCategoryDecorators = new ListMultiMap<>();

    private final IJeiHelpers jeiHelpers;

    private final IJeiFeatures jeiFeatures;

    public AdvancedRegistration(IJeiHelpers jeiHelpers, IJeiFeatures jeiFeatures) {
        this.jeiHelpers = jeiHelpers;
        this.jeiFeatures = jeiFeatures;
    }

    @Override
    public void addRecipeManagerPlugin(IRecipeManagerPlugin recipeManagerPlugin) {
        ErrorUtil.checkNotNull(recipeManagerPlugin, "recipeManagerPlugin");
        LOGGER.info("Added recipe manager plugin: {}", recipeManagerPlugin.getClass());
        this.recipeManagerPlugins.add(recipeManagerPlugin);
    }

    @Override
    public <T> void addRecipeCategoryDecorator(RecipeType<T> recipeType, IRecipeCategoryDecorator<T> decorator) {
        ErrorUtil.checkNotNull(recipeType, "recipeType");
        ErrorUtil.checkNotNull(decorator, "decorator");
        LOGGER.info("Added recipe category decorator: {} for recipe type: {}", decorator.getClass(), recipeType.getUid());
        this.recipeCategoryDecorators.put(recipeType, decorator);
    }

    @Override
    public IJeiHelpers getJeiHelpers() {
        return this.jeiHelpers;
    }

    @Override
    public IJeiFeatures getJeiFeatures() {
        return this.jeiFeatures;
    }

    @Unmodifiable
    public List<IRecipeManagerPlugin> getRecipeManagerPlugins() {
        return List.copyOf(this.recipeManagerPlugins);
    }

    @Unmodifiable
    public ImmutableListMultimap<RecipeType<?>, IRecipeCategoryDecorator<?>> getRecipeCategoryDecorators() {
        return this.recipeCategoryDecorators.toImmutable();
    }
}