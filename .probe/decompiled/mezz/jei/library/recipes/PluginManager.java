package mezz.jei.library.recipes;

import com.google.common.base.Stopwatch;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.library.recipes.collect.RecipeTypeData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Unmodifiable;

public class PluginManager {

    private static final Logger LOGGER = LogManager.getLogger();

    @Unmodifiable
    private List<IRecipeManagerPlugin> plugins = new ArrayList();

    public PluginManager(IRecipeManagerPlugin internalRecipeManagerPlugin, List<IRecipeManagerPlugin> plugins) {
        this.plugins.add(internalRecipeManagerPlugin);
        this.plugins.addAll(plugins);
    }

    public <T> Stream<T> getRecipes(RecipeTypeData<T> recipeTypeData, IFocusGroup focusGroup, boolean includeHidden) {
        IRecipeCategory<T> recipeCategory = recipeTypeData.getRecipeCategory();
        Stream<T> recipes = this.plugins.stream().flatMap(p -> this.getPluginRecipeStream(p, recipeCategory, focusGroup)).distinct();
        if (!includeHidden) {
            Set<T> hiddenRecipes = recipeTypeData.getHiddenRecipes();
            Predicate<T> notHidden = (hiddenRecipes::contains).negate();
            recipes = recipes.filter(notHidden);
        }
        return recipes;
    }

    public Stream<RecipeType<?>> getRecipeTypes(IFocusGroup focusGroup) {
        return this.plugins.stream().flatMap(p -> this.getPluginRecipeTypeStream(p, focusGroup)).distinct();
    }

    private Stream<RecipeType<?>> getPluginRecipeTypeStream(IRecipeManagerPlugin plugin, IFocusGroup focuses) {
        List<IFocus<?>> allFocuses = focuses.getAllFocuses();
        return allFocuses.stream().flatMap(focus -> this.getRecipeTypes(plugin, focus));
    }

    private <T> Stream<T> getPluginRecipeStream(IRecipeManagerPlugin plugin, IRecipeCategory<T> recipeCategory, IFocusGroup focuses) {
        if (!focuses.isEmpty()) {
            List<IFocus<?>> allFocuses = focuses.getAllFocuses();
            return allFocuses.stream().flatMap(focus -> this.getRecipes(plugin, recipeCategory, focus));
        } else {
            return this.getRecipes(plugin, recipeCategory);
        }
    }

    private Stream<RecipeType<?>> getRecipeTypes(IRecipeManagerPlugin plugin, IFocus<?> focus) {
        return this.safeCallPlugin(plugin, () -> plugin.getRecipeTypes(focus).stream(), Stream.of());
    }

    private <T> Stream<T> getRecipes(IRecipeManagerPlugin plugin, IRecipeCategory<T> recipeCategory) {
        return this.safeCallPlugin(plugin, () -> plugin.getRecipes(recipeCategory).stream(), Stream.of());
    }

    private <T> Stream<T> getRecipes(IRecipeManagerPlugin plugin, IRecipeCategory<T> recipeCategory, IFocus<?> focus) {
        return this.safeCallPlugin(plugin, () -> plugin.getRecipes(recipeCategory, focus).stream(), Stream.of());
    }

    private <T> T safeCallPlugin(IRecipeManagerPlugin plugin, Supplier<T> supplier, T defaultValue) {
        Stopwatch stopWatch = Stopwatch.createStarted();
        try {
            T result = (T) supplier.get();
            stopWatch.stop();
            if (stopWatch.elapsed(TimeUnit.MILLISECONDS) > 10L) {
                LOGGER.warn("Recipe registry plugin is slow, took {}. {}", stopWatch, plugin.getClass());
            }
            return result;
        } catch (LinkageError | RuntimeException var6) {
            LOGGER.error("Recipe registry plugin crashed, it is being disabled: {}", plugin.getClass(), var6);
            this.plugins = new ArrayList(this.plugins);
            this.plugins.remove(plugin);
            return defaultValue;
        }
    }
}