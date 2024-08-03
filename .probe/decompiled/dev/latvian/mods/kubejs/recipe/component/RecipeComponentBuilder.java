package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.InputReplacement;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.ObjectDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RecipeComponentBuilder implements RecipeComponent<RecipeComponentBuilderMap> {

    public final List<RecipeKey<?>> keys;

    public Predicate<Set<String>> hasPriority;

    public ComponentRole role = ComponentRole.OTHER;

    public RecipeComponentBuilder(int init) {
        this.keys = new ArrayList(init);
    }

    public RecipeComponentBuilder add(RecipeKey<?> key) {
        this.keys.add(key);
        return this;
    }

    public RecipeComponentBuilder hasPriority(Predicate<Set<String>> hasPriority) {
        this.hasPriority = hasPriority;
        return this;
    }

    public RecipeComponentBuilder inputRole() {
        this.role = ComponentRole.INPUT;
        return this;
    }

    public RecipeComponentBuilder outputRole() {
        this.role = ComponentRole.OUTPUT;
        return this;
    }

    public RecipeComponentBuilder createCopy() {
        RecipeComponentBuilder copy = new RecipeComponentBuilder(this.keys.size());
        copy.keys.addAll(this.keys);
        copy.hasPriority = this.hasPriority;
        copy.role = this.role;
        return copy;
    }

    @Override
    public ComponentRole role() {
        return this.role;
    }

    @Override
    public String componentType() {
        return "builder";
    }

    @Override
    public Class<?> componentClass() {
        return RecipeComponentBuilderMap.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        ObjectDescJS obj = TypeDescJS.object(this.keys.size());
        for (RecipeKey<?> key : this.keys) {
            obj.add(key.name, key.component.constructorDescription(ctx), key.optional());
        }
        return obj;
    }

    public JsonElement write(RecipeJS recipe, RecipeComponentBuilderMap value) {
        JsonObject json = new JsonObject();
        for (RecipeComponentValue<?> val : value.holders) {
            if (val.value != null) {
                RecipeComponentValue<?> vc = new RecipeComponentValue<>(val.key, val.getIndex());
                vc.value = UtilsJS.cast(val.value);
                val.key.component.writeToJson(recipe, UtilsJS.cast(vc), json);
            }
        }
        return json;
    }

    public RecipeComponentBuilderMap read(RecipeJS recipe, Object from) {
        RecipeComponentBuilderMap value = new RecipeComponentBuilderMap(this);
        if (from instanceof JsonObject json) {
            for (RecipeComponentValue<?> holder : value.holders) {
                holder.key.component.readFromJson(recipe, UtilsJS.cast(holder), json);
                if (!holder.key.optional() && holder.value == null) {
                    throw new IllegalArgumentException("Missing required key '" + holder.key + "'!");
                }
            }
        } else {
            if (!(from instanceof Map<?, ?> map)) {
                throw new IllegalArgumentException("Expected JSON object!");
            }
            for (RecipeComponentValue<?> holderx : value.holders) {
                holderx.key.component.readFromMap(recipe, UtilsJS.cast(holderx), map);
                if (!holderx.key.optional() && holderx.value == null) {
                    throw new IllegalArgumentException("Missing required key '" + holderx.key + "'!");
                }
            }
        }
        return value;
    }

    @Override
    public boolean hasPriority(RecipeJS recipe, Object from) {
        if (from instanceof Map m) {
            if (this.hasPriority != null) {
                return this.hasPriority.test(m.keySet());
            } else {
                for (RecipeKey<?> key : this.keys) {
                    if (!key.optional() && !m.containsKey(key.name)) {
                        return false;
                    }
                }
                return true;
            }
        } else if (from instanceof JsonObject json) {
            if (this.hasPriority != null) {
                return this.hasPriority.test(json.keySet());
            } else {
                for (RecipeKey<?> keyx : this.keys) {
                    if (!keyx.optional() && !json.has(keyx.name)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean isInput(RecipeJS recipe, RecipeComponentBuilderMap value, ReplacementMatch match) {
        for (RecipeComponentValue<?> e : value.holders) {
            if (e.isInput(recipe, match)) {
                return true;
            }
        }
        return false;
    }

    public RecipeComponentBuilderMap replaceInput(RecipeJS recipe, RecipeComponentBuilderMap original, ReplacementMatch match, InputReplacement with) {
        for (RecipeComponentValue<?> e : original.holders) {
            if (e.replaceInput(recipe, match, with)) {
                original.hasChanged = true;
            }
        }
        return original;
    }

    public boolean isOutput(RecipeJS recipe, RecipeComponentBuilderMap value, ReplacementMatch match) {
        for (RecipeComponentValue<?> e : value.holders) {
            if (e.isOutput(recipe, match)) {
                return true;
            }
        }
        return false;
    }

    public RecipeComponentBuilderMap replaceOutput(RecipeJS recipe, RecipeComponentBuilderMap original, ReplacementMatch match, OutputReplacement with) {
        for (RecipeComponentValue<?> e : original.holders) {
            if (e.replaceOutput(recipe, match, with)) {
                original.hasChanged = true;
            }
        }
        return original;
    }

    public String toString() {
        return (String) this.keys.stream().map(RecipeKey::toString).collect(Collectors.joining(",", "builder{", "}"));
    }

    public boolean checkValueHasChanged(RecipeComponentBuilderMap oldValue, RecipeComponentBuilderMap newValue) {
        return newValue.hasChanged;
    }
}