package dev.latvian.mods.kubejs.recipe.filter;

import dev.latvian.mods.kubejs.core.RecipeKJS;

public class ModFilter implements RecipeFilter {

    private final String mod;

    public ModFilter(String m) {
        this.mod = m;
    }

    @Override
    public boolean test(RecipeKJS r) {
        return r.kjs$getMod().equals(this.mod);
    }

    public String toString() {
        return "ModFilter{mod='" + this.mod + "'}";
    }
}