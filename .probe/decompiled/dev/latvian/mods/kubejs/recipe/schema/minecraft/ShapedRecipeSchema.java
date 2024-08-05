package dev.latvian.mods.kubejs.recipe.schema.minecraft;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentValueMap;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.MapRecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.util.TinyMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.apache.commons.lang3.StringUtils;

public interface ShapedRecipeSchema {

    RecipeKey<OutputItem> RESULT = ItemComponents.OUTPUT.key("result");

    RecipeKey<String[]> PATTERN = StringComponent.NON_EMPTY.asArray().key("pattern");

    RecipeKey<TinyMap<Character, InputItem>> KEY = MapRecipeComponent.ITEM_PATTERN_KEY.key("key");

    RecipeKey<Boolean> KJS_MIRROR = BooleanComponent.BOOLEAN.key("kubejs:mirror").preferred("kjsMirror").optional(true).exclude();

    RecipeKey<Boolean> KJS_SHRINK = BooleanComponent.BOOLEAN.key("kubejs:shrink").preferred("kjsShrink").optional(true).exclude();

    RecipeKey<InputItem[][]> INGREDIENTS = ItemComponents.INPUT_ARRAY.asArray().key("ingredients");

    RecipeSchema SCHEMA = new RecipeSchema(ShapedRecipeSchema.ShapedRecipeJS.class, ShapedRecipeSchema.ShapedRecipeJS::new, RESULT, PATTERN, KEY, KJS_MIRROR, KJS_SHRINK).constructor(RESULT, PATTERN, KEY).constructor((recipe, schemaType, keys, from) -> ((ShapedRecipeSchema.ShapedRecipeJS) recipe).set2DValues(from), RESULT, INGREDIENTS).uniqueOutputId(RESULT);

    public static class ShapedRecipeJS extends RecipeJS {

        public RecipeJS noMirror() {
            return this.setValue(ShapedRecipeSchema.KJS_MIRROR, Boolean.valueOf(false));
        }

        public RecipeJS noShrink() {
            return this.setValue(ShapedRecipeSchema.KJS_SHRINK, Boolean.valueOf(false));
        }

        private void set2DValues(ComponentValueMap from) {
            this.setValue(ShapedRecipeSchema.RESULT, from.getValue(this, ShapedRecipeSchema.RESULT));
            InputItem[][] vertical = from.getValue(this, ShapedRecipeSchema.INGREDIENTS);
            if (vertical.length == 0) {
                throw new RecipeExceptionJS("Pattern is empty!");
            } else {
                ArrayList<String> pattern = new ArrayList();
                HashMap<Character, InputItem> key = new HashMap();
                StringBuilder horizontalPattern = new StringBuilder();
                int id = 0;
                for (InputItem[] horizontal : vertical) {
                    for (InputItem ingredient : horizontal) {
                        if (!ingredient.isEmpty()) {
                            char currentChar = (char) (65 + id++);
                            horizontalPattern.append(currentChar);
                            key.put(currentChar, ingredient);
                        } else {
                            horizontalPattern.append(' ');
                        }
                    }
                    pattern.add(horizontalPattern.toString());
                    horizontalPattern.setLength(0);
                }
                int maxLength = pattern.stream().mapToInt(String::length).max().getAsInt();
                ListIterator<String> iterator = pattern.listIterator();
                while (iterator.hasNext()) {
                    iterator.set(StringUtils.rightPad((String) iterator.next(), maxLength));
                }
                this.setValue(ShapedRecipeSchema.PATTERN, (String[]) pattern.toArray(new String[0]));
                this.setValue(ShapedRecipeSchema.KEY, TinyMap.ofMap(key));
            }
        }

        @Override
        public void afterLoaded() {
            super.afterLoaded();
            String[] pattern = this.getValue(ShapedRecipeSchema.PATTERN);
            TinyMap<Character, InputItem> key = this.getValue(ShapedRecipeSchema.KEY);
            if (pattern.length == 0) {
                throw new RecipeExceptionJS("Pattern is empty!");
            } else if (key.isEmpty()) {
                throw new RecipeExceptionJS("Key map is empty!");
            } else {
                List<Character> airs = null;
                ArrayList<TinyMap.Entry<Character, InputItem>> entries = new ArrayList(Arrays.asList(key.entries()));
                Iterator<TinyMap.Entry<Character, InputItem>> itr = entries.iterator();
                while (itr.hasNext()) {
                    TinyMap.Entry<Character, InputItem> entry = (TinyMap.Entry<Character, InputItem>) itr.next();
                    if (entry.value() == null || entry.value().isEmpty()) {
                        if (airs == null) {
                            airs = new ArrayList(1);
                        }
                        airs.add(entry.key());
                        itr.remove();
                    }
                }
                if (airs != null) {
                    for (int i = 0; i < pattern.length; i++) {
                        for (Character a : airs) {
                            pattern[i] = pattern[i].replace(a, ' ');
                        }
                    }
                    this.setValue(ShapedRecipeSchema.PATTERN, pattern);
                    this.setValue(ShapedRecipeSchema.KEY, new TinyMap<>(entries));
                }
            }
        }

        @Override
        public RecipeTypeFunction getSerializationTypeFunction() {
            return this.type == this.type.event.shaped && this.type.event.shaped != this.type.event.vanillaShaped && !this.json.has("kubejs:actions") && !this.json.has("kubejs:modify_result") && !this.json.has("kubejs:stage") && !this.json.has("kubejs:mirror") && !this.json.has("kubejs:shrink") ? this.type.event.vanillaShaped : super.getSerializationTypeFunction();
        }
    }
}