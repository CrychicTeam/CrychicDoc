package dev.latvian.mods.kubejs.recipe.filter;

import dev.latvian.mods.kubejs.core.RecipeKJS;

public class GroupFilter implements RecipeFilter {

    private final String group;

    public GroupFilter(String g) {
        this.group = g;
    }

    @Override
    public boolean test(RecipeKJS r) {
        return r.kjs$getGroup().equals(this.group);
    }

    public String toString() {
        return "GroupFilter{group='" + this.group + "'}";
    }
}