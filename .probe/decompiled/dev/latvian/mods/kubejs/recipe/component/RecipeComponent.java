package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.TinyMap;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Nullable;

@Nullable
public interface RecipeComponent<T> {

    static RecipeComponentBuilder builder() {
        return new RecipeComponentBuilder(4);
    }

    static RecipeComponentBuilder builder(RecipeKey<?>... key) {
        RecipeComponentBuilder b = new RecipeComponentBuilder(key.length);
        for (RecipeKey<?> k : key) {
            b.add(k);
        }
        return b;
    }

    default RecipeKey<T> key(String name) {
        return new RecipeKey<>(this, name);
    }

    default ComponentRole role() {
        return ComponentRole.OTHER;
    }

    default String componentType() {
        return "unknown";
    }

    Class<?> componentClass();

    default TypeDescJS constructorDescription(DescriptionContext ctx) {
        return ctx.javaType(this.componentClass());
    }

    JsonElement write(RecipeJS var1, T var2);

    T read(RecipeJS var1, Object var2);

    default void writeToJson(RecipeJS recipe, RecipeComponentValue<T> cv, JsonObject json) {
        if (cv.key.names.size() >= 2) {
            for (String k : cv.key.names) {
                json.remove(k);
            }
        }
        json.add(cv.key.name, this.write(recipe, cv.value));
    }

    default void readFromJson(RecipeJS recipe, RecipeComponentValue<T> cv, JsonObject json) {
        JsonElement v = json.get(cv.key.name);
        if (v != null) {
            cv.value = this.read(recipe, v);
        } else if (cv.key.names.size() >= 2) {
            for (String alt : cv.key.names) {
                v = json.get(alt);
                if (v != null) {
                    cv.value = this.read(recipe, v);
                    return;
                }
            }
        }
    }

    default void readFromMap(RecipeJS recipe, RecipeComponentValue<T> cv, Map<?, ?> map) {
        Object v = map.get(cv.key.name);
        if (v != null) {
            cv.value = this.read(recipe, v);
        } else if (cv.key.names.size() >= 2) {
            for (String alt : cv.key.names) {
                v = map.get(alt);
                if (v != null) {
                    cv.value = this.read(recipe, v);
                    return;
                }
            }
        }
    }

    default boolean hasPriority(RecipeJS recipe, Object from) {
        return false;
    }

    default boolean isInput(RecipeJS recipe, T value, ReplacementMatch match) {
        return false;
    }

    default T replaceInput(RecipeJS recipe, T original, ReplacementMatch match, InputReplacement with) {
        if (original instanceof InputReplacement r && this.isInput(recipe, original, match)) {
            return this.read(recipe, with.replaceInput(recipe, match, r));
        }
        return original;
    }

    default boolean isOutput(RecipeJS recipe, T value, ReplacementMatch match) {
        return false;
    }

    default T replaceOutput(RecipeJS recipe, T original, ReplacementMatch match, OutputReplacement with) {
        if (original instanceof OutputReplacement r && this.isOutput(recipe, original, match)) {
            return this.read(recipe, with.replaceOutput(recipe, match, r));
        }
        return original;
    }

    default String checkEmpty(RecipeKey<T> key, T value) {
        return "";
    }

    default boolean checkValueHasChanged(T oldValue, T newValue) {
        return oldValue != newValue;
    }

    default ArrayRecipeComponent<T> asArray() {
        T[] arr = (T[]) ((Object[]) Array.newInstance(this.componentClass(), 0));
        return new ArrayRecipeComponent<>(this, false, arr.getClass(), arr);
    }

    default ArrayRecipeComponent<T> asArrayOrSelf() {
        T[] arr = (T[]) ((Object[]) Array.newInstance(this.componentClass(), 0));
        return new ArrayRecipeComponent<>(this, true, arr.getClass(), arr);
    }

    default RecipeComponent<T> orSelf() {
        return this;
    }

    default <K> RecipeComponent<TinyMap<K, T>> asMap(RecipeComponent<K> key) {
        return new MapRecipeComponent<>(key, this, false);
    }

    default RecipeComponent<TinyMap<Character, T>> asPatternKey() {
        return new MapRecipeComponent<>(StringComponent.CHARACTER, this, true);
    }

    default <O> OrRecipeComponent<T, O> or(RecipeComponent<O> other) {
        return new OrRecipeComponent<>(this, other);
    }

    default <O> AndRecipeComponent<T, O> and(RecipeComponent<O> other) {
        return new AndRecipeComponent<>(this, other);
    }

    @Info("Returns a new RecipeComponent that applies the mappingTo function to the input before it is passed to this component to be read")
    default MappingRecipeComponent<T> mapIn(UnaryOperator<Object> mappingTo) {
        return this.map(mappingTo, UnaryOperator.identity());
    }

    @Info("Returns a new RecipeComponent that applies the mappingFrom function after the component writes to json, before that json is saved")
    default MappingRecipeComponent<T> mapOut(UnaryOperator<JsonElement> mappingFrom) {
        return this.map(UnaryOperator.identity(), mappingFrom);
    }

    @Info("Returns a new RecipeComponent that applies the mappingTo function to the input before it is passed to this component to be read, and the mappingFrom function after the component writes to json, before that json is saved")
    default MappingRecipeComponent<T> map(UnaryOperator<Object> mappingTo, UnaryOperator<JsonElement> mappingFrom) {
        return new MappingRecipeComponent<>(this, mappingTo, mappingFrom);
    }

    @Info("Returns a new RecipeComponent that maps the keys in a JsonObject according to the provided map, both before the json gets passed to the component and after the component returns a written json object.\nThe mappings should be provided in the format `{recipe: \"component\"}` where recipe is the key as in the recipe, and component is the key as how the RecipeComponent expects it.\nAny keys not included in the provided map will be ignored, and any keys in the provided map that are not in either the input object or output object will be ignored.\nNote that if the input or output is not a JsonObject (ie its an ItemStack, or it is a JsonPrimitive) then that will pass through this without being modified.\nIf you wish to handle those situations use the actual map function")
    default SimpleMappingRecipeComponent<T> simpleMap(Object mappings) {
        return new SimpleMappingRecipeComponent<>(this, mappings);
    }
}