package dev.latvian.mods.kubejs.recipe.component;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Wrapper;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;

public class RecipeComponentBuilderMap extends AbstractMap<RecipeKey<?>, Object> {

    public static final RecipeComponentBuilderMap EMPTY = new RecipeComponentBuilderMap(RecipeComponentValue.EMPTY_ARRAY);

    public final RecipeComponentValue<?>[] holders;

    private Set<Entry<RecipeKey<?>, Object>> holderSet;

    public boolean hasChanged;

    public RecipeComponentBuilderMap(RecipeComponentBuilder builder) {
        this.holders = new RecipeComponentValue[builder.keys.size()];
        for (int i = 0; i < this.holders.length; i++) {
            this.holders[i] = new RecipeComponentValue((RecipeKey) builder.keys.get(i), i);
        }
        this.hasChanged = false;
    }

    public RecipeComponentBuilderMap(RecipeComponentValue<?>[] holders) {
        this.holders = new RecipeComponentValue[holders.length];
        for (int i = 0; i < holders.length; i++) {
            this.holders[i] = holders[i].copy();
        }
    }

    public RecipeComponentBuilderMap(RecipeKey<?>[] keys) {
        this.holders = new RecipeComponentValue[keys.length];
        for (int i = 0; i < this.holders.length; i++) {
            this.holders[i] = new RecipeComponentValue<>(keys[i], i);
        }
    }

    @NotNull
    public Set<Entry<RecipeKey<?>, Object>> entrySet() {
        if (this.holderSet == null) {
            this.holderSet = UtilsJS.cast(Set.of(this.holders));
        }
        return this.holderSet;
    }

    public Object put(RecipeKey<?> key, Object value) {
        for (RecipeComponentValue<?> holder : this.holders) {
            if (holder.key == key) {
                return ((RecipeComponentValue<Object>) holder).setValue(UtilsJS.cast(Wrapper.unwrapped(value)));
            }
        }
        throw new IllegalArgumentException("Key " + key + " is not in this map!");
    }

    public RecipeComponentValue<?> getHolder(Object key) {
        for (RecipeComponentValue<?> holder : this.holders) {
            if (holder.key == key) {
                return holder;
            }
        }
        return null;
    }

    public Object get(Object key) {
        RecipeComponentValue<?> h = this.getHolder(key);
        return h == null ? null : h.value;
    }

    public Object getOrDefault(Object key, Object defaultValue) {
        Object v = this.get(key);
        return v == null ? defaultValue : v;
    }

    public int hashCode() {
        int i = 1;
        for (RecipeComponentValue<?> holder : this.holders) {
            i = 31 * i + holder.key.hashCode();
            i = 31 * i + Objects.hashCode(holder.value);
        }
        return i;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RecipeComponentBuilderMap map)) {
            return false;
        } else if (this.holders.length != map.holders.length) {
            return false;
        } else {
            for (int i = 0; i < this.holders.length; i++) {
                if (this.holders[i].key != map.holders[i].key || !Objects.equals(this.holders[i].value, map.holders[i].value)) {
                    return false;
                }
            }
            return true;
        }
    }
}