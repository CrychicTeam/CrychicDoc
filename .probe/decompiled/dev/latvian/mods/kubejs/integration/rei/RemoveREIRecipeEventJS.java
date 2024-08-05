package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry.CategoryConfiguration;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import net.minecraft.resources.ResourceLocation;

public class RemoveREIRecipeEventJS extends EventJS {

    private final Map<CategoryIdentifier<?>, Collection<ResourceLocation>> recipesRemoved;

    private final CategoryRegistry categories;

    private final DisplayRegistry displays;

    public RemoveREIRecipeEventJS(Map<CategoryIdentifier<?>, Collection<ResourceLocation>> recipesRemoved) {
        this.recipesRemoved = recipesRemoved;
        this.categories = CategoryRegistry.getInstance();
        this.displays = DisplayRegistry.getInstance();
    }

    public CategoryRegistry getCategories() {
        return this.categories;
    }

    public DisplayRegistry getDisplays() {
        return this.displays;
    }

    public List<?> getDisplaysFor(ResourceLocation category) {
        return this.displays.get(CategoryIdentifier.of(category));
    }

    public Collection<ResourceLocation> getCategoryIds() {
        return CollectionUtils.map(this.categories, CategoryConfiguration::getIdentifier);
    }

    public void remove(ResourceLocation category, ResourceLocation... recipesToRemove) {
        CategoryIdentifier<Display> catId = CategoryIdentifier.of(category);
        if (this.categories.tryGet(catId).isEmpty()) {
            KubeJS.LOGGER.warn("Failed to remove recipes for type {}: Category doesn't exist!", category);
            KubeJS.LOGGER.info("Use event.categoryIds to get a list of all categories.");
        } else {
            ((Collection) this.recipesRemoved.computeIfAbsent(catId, _0 -> new HashSet())).addAll(List.of(recipesToRemove));
        }
    }

    public void removeFromAll(ResourceLocation... recipesToRemove) {
        List<ResourceLocation> asList = List.of(recipesToRemove);
        for (CategoryConfiguration<?> catId : this.categories) {
            ((Collection) this.recipesRemoved.computeIfAbsent(catId.getCategoryIdentifier(), _0 -> new HashSet())).addAll(asList);
        }
    }
}