package net.minecraft.stats;

import com.google.common.collect.Sets;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeBook {

    protected final Set<ResourceLocation> known = Sets.newHashSet();

    protected final Set<ResourceLocation> highlight = Sets.newHashSet();

    private final RecipeBookSettings bookSettings = new RecipeBookSettings();

    public void copyOverData(RecipeBook recipeBook0) {
        this.known.clear();
        this.highlight.clear();
        this.bookSettings.replaceFrom(recipeBook0.bookSettings);
        this.known.addAll(recipeBook0.known);
        this.highlight.addAll(recipeBook0.highlight);
    }

    public void add(Recipe<?> recipe0) {
        if (!recipe0.isSpecial()) {
            this.add(recipe0.getId());
        }
    }

    protected void add(ResourceLocation resourceLocation0) {
        this.known.add(resourceLocation0);
    }

    public boolean contains(@Nullable Recipe<?> recipe0) {
        return recipe0 == null ? false : this.known.contains(recipe0.getId());
    }

    public boolean contains(ResourceLocation resourceLocation0) {
        return this.known.contains(resourceLocation0);
    }

    public void remove(Recipe<?> recipe0) {
        this.remove(recipe0.getId());
    }

    protected void remove(ResourceLocation resourceLocation0) {
        this.known.remove(resourceLocation0);
        this.highlight.remove(resourceLocation0);
    }

    public boolean willHighlight(Recipe<?> recipe0) {
        return this.highlight.contains(recipe0.getId());
    }

    public void removeHighlight(Recipe<?> recipe0) {
        this.highlight.remove(recipe0.getId());
    }

    public void addHighlight(Recipe<?> recipe0) {
        this.addHighlight(recipe0.getId());
    }

    protected void addHighlight(ResourceLocation resourceLocation0) {
        this.highlight.add(resourceLocation0);
    }

    public boolean isOpen(RecipeBookType recipeBookType0) {
        return this.bookSettings.isOpen(recipeBookType0);
    }

    public void setOpen(RecipeBookType recipeBookType0, boolean boolean1) {
        this.bookSettings.setOpen(recipeBookType0, boolean1);
    }

    public boolean isFiltering(RecipeBookMenu<?> recipeBookMenu0) {
        return this.isFiltering(recipeBookMenu0.getRecipeBookType());
    }

    public boolean isFiltering(RecipeBookType recipeBookType0) {
        return this.bookSettings.isFiltering(recipeBookType0);
    }

    public void setFiltering(RecipeBookType recipeBookType0, boolean boolean1) {
        this.bookSettings.setFiltering(recipeBookType0, boolean1);
    }

    public void setBookSettings(RecipeBookSettings recipeBookSettings0) {
        this.bookSettings.replaceFrom(recipeBookSettings0);
    }

    public RecipeBookSettings getBookSettings() {
        return this.bookSettings.copy();
    }

    public void setBookSetting(RecipeBookType recipeBookType0, boolean boolean1, boolean boolean2) {
        this.bookSettings.setOpen(recipeBookType0, boolean1);
        this.bookSettings.setFiltering(recipeBookType0, boolean2);
    }
}