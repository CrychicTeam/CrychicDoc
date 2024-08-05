package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.core.RecipeKJS;

@FunctionalInterface
public interface InputReplacementTransformer {

    Object transform(RecipeKJS var1, ReplacementMatch var2, InputReplacement var3, InputReplacement var4);

    public static record Replacement(InputReplacement with, InputReplacementTransformer transformer) implements InputReplacement {

        public String toString() {
            return this.with + " [transformed]";
        }
    }
}