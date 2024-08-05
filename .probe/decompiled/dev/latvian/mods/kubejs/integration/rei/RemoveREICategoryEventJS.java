package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.event.EventJS;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry.CategoryConfiguration;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import net.minecraft.resources.ResourceLocation;

public class RemoveREICategoryEventJS extends EventJS {

    private final Set<CategoryIdentifier<?>> categoriesRemoved;

    private final CategoryRegistry registry;

    public RemoveREICategoryEventJS(Set<CategoryIdentifier<?>> categoriesRemoved) {
        this.categoriesRemoved = categoriesRemoved;
        this.registry = CategoryRegistry.getInstance();
    }

    public CategoryRegistry getRegistry() {
        return this.registry;
    }

    public CategoryRegistry getCategories() {
        return this.registry;
    }

    public Collection<ResourceLocation> getCategoryIds() {
        return CollectionUtils.map(this.registry, CategoryConfiguration::getIdentifier);
    }

    public void remove(ResourceLocation... categories) {
        for (ResourceLocation id : categories) {
            this.categoriesRemoved.add(CategoryIdentifier.of(id));
        }
    }

    public void removeIf(Predicate<CategoryConfiguration<?>> filter) {
        this.registry.stream().filter(filter).map(CategoryConfiguration::getIdentifier).forEach(xva$0 -> this.remove(xva$0));
    }
}