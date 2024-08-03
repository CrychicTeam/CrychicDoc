package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

public class RemoveJEIRecipesEvent extends EventJS {

    private final IJeiRuntime runtime;

    private final Map<IRecipeCategory, Collection<ResourceLocation>> recipesRemoved;

    private final Map<ResourceLocation, IRecipeCategory> categoryById;

    public RemoveJEIRecipesEvent(IJeiRuntime r) {
        this.runtime = r;
        this.recipesRemoved = new HashMap();
        this.categoryById = (Map<ResourceLocation, IRecipeCategory>) this.runtime.getRecipeManager().createRecipeCategoryLookup().get().collect(Collectors.toMap(cat -> cat.getRecipeType().getUid(), Function.identity()));
    }

    public Collection<IRecipeCategory> getCategories() {
        return this.categoryById.values();
    }

    public Collection<ResourceLocation> getCategoryIds() {
        return this.categoryById.keySet();
    }

    public void remove(ResourceLocation category, ResourceLocation[] recipesToRemove) {
        for (ResourceLocation toRemove : recipesToRemove) {
            if (!this.categoryById.containsKey(category)) {
                KubeJS.LOGGER.warn("Failed to remove recipes for type {}: Category doesn't exist!", category);
                KubeJS.LOGGER.info("Use event.categoryIds to get a list of all categories.");
            } else {
                ((Collection) this.recipesRemoved.computeIfAbsent((IRecipeCategory) this.categoryById.get(category), _0 -> new HashSet())).add(toRemove);
            }
        }
    }

    @Override
    protected void afterPosted(EventResult result) {
        IRecipeManager rm = this.runtime.getRecipeManager();
        for (IRecipeCategory cat : this.recipesRemoved.keySet()) {
            RecipeType type = cat.getRecipeType();
            List allRecipes = rm.createRecipeLookup(cat.getRecipeType()).get().toList();
            Collection<ResourceLocation> ids = (Collection<ResourceLocation>) this.recipesRemoved.get(cat);
            HashSet<Object> recipesHidden = new HashSet(ids.size());
            for (ResourceLocation id : ids) {
                boolean found = false;
                for (Object recipe : allRecipes) {
                    ResourceLocation recipeId = cat.getRegistryName(recipe);
                    if (recipeId == null) {
                        KubeJS.LOGGER.warn("Failed to remove recipe {} for type {}: Category does not support removal by id!", id, type);
                        break;
                    }
                    if (recipeId.equals(id)) {
                        found = true;
                        recipesHidden.add(recipe);
                        break;
                    }
                }
                if (!found) {
                    KubeJS.LOGGER.warn("Failed to remove recipe {} for type {}: Recipe doesn't exist!", id, type);
                }
            }
            rm.hideRecipes(type, recipesHidden);
        }
    }
}