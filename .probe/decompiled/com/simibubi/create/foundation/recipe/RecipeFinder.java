package com.simibubi.create.foundation.recipe;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public class RecipeFinder {

    private static Cache<Object, List<Recipe<?>>> cachedSearches = CacheBuilder.newBuilder().build();

    public static final ResourceManagerReloadListener LISTENER = resourceManager -> cachedSearches.invalidateAll();

    public static List<Recipe<?>> get(@Nullable Object cacheKey, Level world, Predicate<Recipe<?>> conditions) {
        if (cacheKey == null) {
            return startSearch(world, conditions);
        } else {
            try {
                return (List<Recipe<?>>) cachedSearches.get(cacheKey, () -> startSearch(world, conditions));
            } catch (ExecutionException var4) {
                var4.printStackTrace();
                return Collections.emptyList();
            }
        }
    }

    private static List<Recipe<?>> startSearch(Level world, Predicate<? super Recipe<?>> conditions) {
        return (List<Recipe<?>>) world.getRecipeManager().getRecipes().stream().filter(conditions).collect(Collectors.toList());
    }
}