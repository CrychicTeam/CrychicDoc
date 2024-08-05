package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.core.RecipeKJS;

@FunctionalInterface
public interface OutputReplacementTransformer {

    Object transform(RecipeKJS var1, ReplacementMatch var2, OutputReplacement var3, OutputReplacement var4);

    public static record Replacement(OutputReplacement with, OutputReplacementTransformer transformer) implements OutputReplacement {

        public String toString() {
            return this.with + " [transformed]";
        }

        @Override
        public Object replaceOutput(RecipeJS recipe, ReplacementMatch match, OutputReplacement original) {
            return this.transformer.transform(recipe, match, original, this.with);
        }
    }
}