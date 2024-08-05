package dev.latvian.mods.kubejs.recipe.filter;

import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;

public class OutputFilter implements RecipeFilter {

    private final ReplacementMatch match;

    public OutputFilter(ReplacementMatch match) {
        this.match = match;
    }

    @Override
    public boolean test(RecipeKJS r) {
        return r.hasOutput(this.match);
    }

    public String toString() {
        return "OutputFilter{" + this.match + "}";
    }
}