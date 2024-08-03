package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.util.TinyMap;
import java.util.Map;

public interface ItemComponents {

    RecipeComponent<InputItem> INPUT = new RecipeComponent<InputItem>() {

        @Override
        public String componentType() {
            return "input_item";
        }

        @Override
        public ComponentRole role() {
            return ComponentRole.INPUT;
        }

        @Override
        public Class<?> componentClass() {
            return InputItem.class;
        }

        @Override
        public boolean hasPriority(RecipeJS recipe, Object from) {
            return recipe.inputItemHasPriority(from);
        }

        public JsonElement write(RecipeJS recipe, InputItem value) {
            return recipe.writeInputItem(value);
        }

        public InputItem read(RecipeJS recipe, Object from) {
            return recipe.readInputItem(from);
        }

        public boolean isInput(RecipeJS recipe, InputItem value, ReplacementMatch match) {
            if (match instanceof ItemMatch m && value.validForMatching() && m.contains(value.ingredient)) {
                return true;
            }
            return false;
        }

        public String checkEmpty(RecipeKey<InputItem> key, InputItem value) {
            return value.isEmpty() ? "Ingredient '" + key.name + "' can't be empty!" : "";
        }

        @Override
        public RecipeComponent<TinyMap<Character, InputItem>> asPatternKey() {
            return MapRecipeComponent.ITEM_PATTERN_KEY;
        }

        public String toString() {
            return this.componentType();
        }
    };

    RecipeComponent<InputItem[]> INPUT_ARRAY = INPUT.asArray();

    RecipeComponent<InputItem[]> UNWRAPPED_INPUT_ARRAY = new RecipeComponentWithParent<InputItem[]>() {

        @Override
        public RecipeComponent<InputItem[]> parentComponent() {
            return ItemComponents.INPUT_ARRAY;
        }

        public JsonElement write(RecipeJS recipe, InputItem[] value) {
            JsonArray json = new JsonArray();
            for (InputItem in : value) {
                for (InputItem in1 : in.unwrap()) {
                    json.add(ItemComponents.INPUT.write(recipe, in1));
                }
            }
            return json;
        }

        public String toString() {
            return this.parentComponent().toString();
        }
    };

    RecipeComponent<OutputItem> OUTPUT = new RecipeComponent<OutputItem>() {

        @Override
        public String componentType() {
            return "output_item";
        }

        @Override
        public ComponentRole role() {
            return ComponentRole.OUTPUT;
        }

        @Override
        public Class<?> componentClass() {
            return OutputItem.class;
        }

        @Override
        public boolean hasPriority(RecipeJS recipe, Object from) {
            return recipe.outputItemHasPriority(from);
        }

        public JsonElement write(RecipeJS recipe, OutputItem value) {
            return recipe.writeOutputItem(value);
        }

        public OutputItem read(RecipeJS recipe, Object from) {
            return recipe.readOutputItem(from);
        }

        public boolean isOutput(RecipeJS recipe, OutputItem value, ReplacementMatch match) {
            if (match instanceof ItemMatch m && !value.isEmpty() && m.contains(value.item)) {
                return true;
            }
            return false;
        }

        public String checkEmpty(RecipeKey<OutputItem> key, OutputItem value) {
            return value.isEmpty() ? "ItemStack '" + key.name + "' can't be empty!" : "";
        }

        public String toString() {
            return this.componentType();
        }
    };

    RecipeComponent<OutputItem[]> OUTPUT_ARRAY = OUTPUT.asArray();

    RecipeComponent<OutputItem> OUTPUT_ID_WITH_COUNT = new RecipeComponentWithParent<OutputItem>() {

        @Override
        public RecipeComponent<OutputItem> parentComponent() {
            return ItemComponents.OUTPUT;
        }

        @Override
        public void writeToJson(RecipeJS recipe, RecipeComponentValue<OutputItem> cv, JsonObject json) {
            json.addProperty(cv.key.name, cv.value.item.kjs$getId());
            json.addProperty("count", cv.value.item.getCount());
        }

        @Override
        public void readFromJson(RecipeJS recipe, RecipeComponentValue<OutputItem> cv, JsonObject json) {
            RecipeComponentWithParent.super.readFromJson(recipe, cv, json);
            if (cv.value != null && json.has("count")) {
                cv.value.item.setCount(json.get("count").getAsInt());
            }
        }

        @Override
        public void readFromMap(RecipeJS recipe, RecipeComponentValue<OutputItem> cv, Map<?, ?> map) {
            RecipeComponentWithParent.super.readFromMap(recipe, cv, map);
            if (cv.value != null && map.containsKey("count")) {
                cv.value.item.setCount(((Number) map.get("count")).intValue());
            }
        }

        public String toString() {
            return this.parentComponent().toString();
        }
    };
}