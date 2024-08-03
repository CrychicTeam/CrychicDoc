package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.recipe.filter.ConstantFilter;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class AfterRecipesLoadedEventJS extends EventJS {

    private final Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> recipeMap;

    private final Map<ResourceLocation, Recipe<?>> recipeIdMap;

    private List<RecipeKJS> originalRecipes;

    public AfterRecipesLoadedEventJS(Map<RecipeType<?>, Map<ResourceLocation, Recipe<?>>> r, Map<ResourceLocation, Recipe<?>> n) {
        this.recipeMap = r;
        this.recipeIdMap = n;
    }

    private List<RecipeKJS> getOriginalRecipes() {
        if (this.originalRecipes == null) {
            this.originalRecipes = new ArrayList();
            for (Map<ResourceLocation, Recipe<?>> map : this.recipeMap.values()) {
                for (Entry<ResourceLocation, Recipe<?>> entry : map.entrySet()) {
                    this.originalRecipes.add((RecipeKJS) entry.getValue());
                }
            }
        }
        return this.originalRecipes;
    }

    public void forEachRecipe(RecipeFilter filter, Consumer<RecipeKJS> consumer) {
        if (filter == ConstantFilter.TRUE) {
            this.getOriginalRecipes().forEach(consumer);
        } else if (filter != ConstantFilter.FALSE) {
            this.getOriginalRecipes().stream().filter(filter).forEach(consumer);
        }
    }

    public int countRecipes(RecipeFilter filter) {
        if (filter == ConstantFilter.TRUE) {
            return this.getOriginalRecipes().size();
        } else {
            return filter != ConstantFilter.FALSE ? (int) this.getOriginalRecipes().stream().filter(filter).count() : 0;
        }
    }

    public int remove(RecipeFilter filter) {
        int count = 0;
        Iterator<RecipeKJS> itr = this.getOriginalRecipes().iterator();
        while (itr.hasNext()) {
            RecipeKJS r = (RecipeKJS) itr.next();
            if (filter.test(r)) {
                Map<ResourceLocation, Recipe<?>> map = (Map<ResourceLocation, Recipe<?>>) this.recipeMap.get(((Recipe) r).getType());
                if (map != null) {
                    map.remove(r.kjs$getOrCreateId());
                }
                this.recipeIdMap.remove(r.kjs$getOrCreateId());
                itr.remove();
                count++;
                if (DevProperties.get().logRemovedRecipes) {
                    ConsoleJS.SERVER.info("- " + r);
                } else if (ConsoleJS.SERVER.shouldPrintDebug()) {
                    ConsoleJS.SERVER.debug("- " + r);
                }
            }
        }
        return count;
    }

    @Override
    protected void afterPosted(EventResult isCanceled) {
        this.recipeMap.values().removeIf(Map::isEmpty);
    }
}