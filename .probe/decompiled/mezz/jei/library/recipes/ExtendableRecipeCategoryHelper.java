package mezz.jei.library.recipes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import mezz.jei.library.util.RecipeErrorUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ExtendableRecipeCategoryHelper<T, W extends IRecipeCategoryExtension> {

    private static final Logger LOGGER = LogManager.getLogger();

    private final List<ExtendableRecipeCategoryHelper.RecipeHandler<? extends T, ? extends W>> recipeHandlers = new ArrayList();

    private final Set<Class<? extends T>> handledClasses = new HashSet();

    private final Map<T, W> cache = new IdentityHashMap();

    private final Class<? extends T> expectedRecipeClass;

    public ExtendableRecipeCategoryHelper(Class<? extends T> expectedRecipeClass) {
        this.expectedRecipeClass = expectedRecipeClass;
    }

    public <R extends T> void addRecipeExtensionFactory(Class<? extends R> recipeClass, @Nullable Predicate<R> extensionFilter, Function<R, ? extends W> recipeExtensionFactory) {
        if (!this.expectedRecipeClass.isAssignableFrom(recipeClass)) {
            throw new IllegalArgumentException("Recipe handlers must handle a specific class. Needed: " + this.expectedRecipeClass + " Got: " + recipeClass);
        } else if (this.handledClasses.contains(recipeClass)) {
            throw new IllegalArgumentException("A Recipe Extension Factory has already been registered for '" + recipeClass.getName());
        } else {
            this.handledClasses.add(recipeClass);
            this.recipeHandlers.add(new ExtendableRecipeCategoryHelper.RecipeHandler(recipeClass, extensionFilter, (Function<T, W>) recipeExtensionFactory));
        }
    }

    public <R extends T> W getRecipeExtension(R recipe) {
        return (W) this.getOptionalRecipeExtension(recipe).orElseThrow(() -> {
            String recipeName = RecipeErrorUtil.getNameForRecipe(recipe);
            return new RuntimeException("Failed to create recipe extension for recipe: " + recipeName);
        });
    }

    public <R extends T> Optional<W> getOptionalRecipeExtension(R recipe) {
        if (this.cache.containsKey(recipe)) {
            return Optional.ofNullable((IRecipeCategoryExtension) this.cache.get(recipe));
        } else {
            Optional<W> result = this.getBestRecipeHandler(recipe).map(handler -> handler.apply(recipe));
            this.cache.put(recipe, (IRecipeCategoryExtension) result.orElse(null));
            return result;
        }
    }

    private <R> Stream<ExtendableRecipeCategoryHelper.RecipeHandler<R, W>> getRecipeHandlerStream(R recipe) {
        Class<? extends R> recipeClass = recipe.getClass();
        return this.recipeHandlers.stream().filter(recipeHandler -> recipeHandler.getRecipeClass().isAssignableFrom(recipeClass)).map(recipeHandler -> recipeHandler).filter(recipeHandler -> recipeHandler.test(recipe));
    }

    private <R extends T> Optional<ExtendableRecipeCategoryHelper.RecipeHandler<R, W>> getBestRecipeHandler(R recipe) {
        Class<?> recipeClass = recipe.getClass();
        List<ExtendableRecipeCategoryHelper.RecipeHandler<R, W>> assignableHandlers = new ArrayList();
        for (ExtendableRecipeCategoryHelper.RecipeHandler<R, W> recipeHandler : this.getRecipeHandlerStream(recipe).toList()) {
            Class<? extends T> handlerRecipeClass = recipeHandler.getRecipeClass();
            if (handlerRecipeClass.equals(recipeClass)) {
                return Optional.of(recipeHandler);
            }
            assignableHandlers.removeIf(handler -> handler.getRecipeClass().isAssignableFrom(handlerRecipeClass));
            if (assignableHandlers.stream().noneMatch(handler -> handlerRecipeClass.isAssignableFrom(handler.getRecipeClass()))) {
                assignableHandlers.add(recipeHandler);
            }
        }
        if (assignableHandlers.isEmpty()) {
            return Optional.empty();
        } else if (assignableHandlers.size() == 1) {
            return Optional.of((ExtendableRecipeCategoryHelper.RecipeHandler) assignableHandlers.get(0));
        } else {
            Class<?> superClass = recipeClass;
            while (!Object.class.equals(superClass)) {
                superClass = superClass.getSuperclass();
                for (ExtendableRecipeCategoryHelper.RecipeHandler<R, W> recipeHandler : assignableHandlers) {
                    if (recipeHandler.getRecipeClass().equals(superClass)) {
                        return Optional.of(recipeHandler);
                    }
                }
            }
            List<Class<? extends T>> assignableClasses = assignableHandlers.stream().map(ExtendableRecipeCategoryHelper.RecipeHandler::getRecipeClass).toList();
            LOGGER.warn("Found multiple matching recipe handlers for {}: {}", recipeClass, assignableClasses);
            return Optional.of((ExtendableRecipeCategoryHelper.RecipeHandler) assignableHandlers.get(0));
        }
    }

    public static class RecipeHandler<T, W extends IRecipeCategoryExtension> {

        private final Class<? extends T> recipeClass;

        private final Function<T, W> factory;

        @Nullable
        private final Predicate<T> filter;

        public RecipeHandler(Class<? extends T> recipeClass, @Nullable Predicate<T> filter, Function<T, W> factory) {
            this.recipeClass = recipeClass;
            this.factory = factory;
            this.filter = filter;
        }

        public Class<? extends T> getRecipeClass() {
            return this.recipeClass;
        }

        public boolean test(T t) {
            return this.filter == null ? true : this.filter.test(t);
        }

        public W apply(T t) {
            return (W) this.factory.apply(t);
        }
    }
}