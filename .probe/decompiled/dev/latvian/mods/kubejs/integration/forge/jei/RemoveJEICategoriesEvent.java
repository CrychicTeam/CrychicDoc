package dev.latvian.mods.kubejs.integration.forge.jei;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import mezz.jei.api.recipe.IRecipeCategoriesLookup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;

public class RemoveJEICategoriesEvent extends EventJS {

    private final IJeiRuntime runtime;

    private final Collection<RecipeType<?>> categoriesRemoved;

    private final IRecipeCategoriesLookup categoryLookup;

    public RemoveJEICategoriesEvent(IJeiRuntime r) {
        this.runtime = r;
        this.categoriesRemoved = new HashSet();
        this.categoryLookup = this.runtime.getRecipeManager().createRecipeCategoryLookup();
    }

    public Collection<IRecipeCategory<?>> getCategories() {
        return this.categoryLookup.get().toList();
    }

    public void remove(ResourceLocation... categoriesToYeet) {
        Set<ResourceLocation> idSet = Set.of(categoriesToYeet);
        this.categoryLookup.get().map(IRecipeCategory::getRecipeType).filter(type -> idSet.contains(type.getUid())).forEach(this.categoriesRemoved::add);
    }

    public Collection<ResourceLocation> getCategoryIds() {
        return this.categoryLookup.get().map(IRecipeCategory::getRecipeType).map(RecipeType::getUid).toList();
    }

    public void removeIf(Predicate<IRecipeCategory<?>> filter) {
        this.categoryLookup.get().filter(filter).map(IRecipeCategory::getRecipeType).forEach(this.categoriesRemoved::add);
    }

    @Override
    protected void afterPosted(EventResult result) {
        for (RecipeType<?> category : this.categoriesRemoved) {
            try {
                this.runtime.getRecipeManager().hideRecipeCategory(category);
            } catch (Exception var5) {
                KubeJS.LOGGER.warn("Failed to yeet recipe category {}!", category);
            }
        }
    }
}