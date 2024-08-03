package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import net.minecraft.world.item.crafting.Ingredient;

public interface ReplacementMatch {

    ReplacementMatch NONE = new ReplacementMatch() {

        public String toString() {
            return "NONE";
        }
    };

    static ReplacementMatch of(Object o) {
        if (o == null) {
            return NONE;
        } else if (o instanceof ReplacementMatch) {
            return (ReplacementMatch) o;
        } else {
            Ingredient in = IngredientJS.of(o);
            if (in.isEmpty()) {
                return NONE;
            } else {
                return (ReplacementMatch) (in.getItems().length == 1 ? new SingleItemMatch(in.getItems()[0]) : new IngredientMatch(in, false));
            }
        }
    }
}