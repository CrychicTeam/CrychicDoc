package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeOptional;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class RecipeKey<T> {

    public final RecipeComponent<T> component;

    public final String name;

    public final Set<String> names;

    public String preferred;

    public RecipeOptional<T> optional;

    public boolean excluded;

    public boolean noBuilders;

    public boolean allowEmpty;

    public boolean alwaysWrite;

    public RecipeKey(RecipeComponent<T> component, String name) {
        this.component = component;
        this.name = name;
        this.names = new LinkedHashSet(1);
        this.names.add(name);
        this.preferred = name;
        this.optional = null;
        this.excluded = false;
        this.noBuilders = false;
        this.allowEmpty = false;
        this.alwaysWrite = false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.name);
        if (this.optional != null) {
            sb.append('?');
        }
        sb.append(':');
        sb.append(this.component);
        return sb.toString();
    }

    public RecipeKey<T> optional(T value) {
        return this.optional(new RecipeOptional.Constant<>(value));
    }

    public RecipeKey<T> optional(RecipeOptional<T> value) {
        this.optional = value;
        return this;
    }

    public RecipeKey<T> defaultOptional() {
        this.optional = UtilsJS.cast(RecipeOptional.DEFAULT);
        return this;
    }

    public boolean optional() {
        return this.optional != null;
    }

    public RecipeKey<T> alt(String name) {
        this.names.add(name);
        return this;
    }

    public RecipeKey<T> alt(String... names) {
        this.names.addAll(List.of(names));
        return this;
    }

    public RecipeKey<T> preferred(String name) {
        this.names.add(name);
        this.preferred = name;
        return this;
    }

    public RecipeKey<T> exclude() {
        this.excluded = true;
        return this;
    }

    public boolean includeInAutoConstructors() {
        return this.optional == null || !this.excluded;
    }

    public RecipeKey<T> noBuilders() {
        this.noBuilders = true;
        return this;
    }

    public RecipeKey<T> allowEmpty() {
        this.allowEmpty = true;
        return this;
    }

    public RecipeKey<T> alwaysWrite() {
        this.alwaysWrite = true;
        return this;
    }
}