package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonArray;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import java.util.Iterator;
import org.apache.commons.lang3.tuple.Pair;

public record AndRecipeComponent<A, B>(RecipeComponent<A> a, RecipeComponent<B> b) implements RecipeComponent<Pair<A, B>> {

    @Override
    public String componentType() {
        return "and";
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.fixedArray(this.a.constructorDescription(ctx), this.b.constructorDescription(ctx));
    }

    @Override
    public ComponentRole role() {
        return this.a.role().isOther() ? this.b.role() : this.a.role();
    }

    @Override
    public Class<?> componentClass() {
        return Pair.class;
    }

    public JsonArray write(RecipeJS recipe, Pair<A, B> value) {
        JsonArray json = new JsonArray();
        json.add(this.a.write(recipe, (A) value.getLeft()));
        json.add(this.b.write(recipe, (B) value.getRight()));
        return json;
    }

    public Pair<A, B> read(RecipeJS recipe, Object from) {
        if (from instanceof Iterable<?> iterable) {
            Iterator<?> itr = iterable.iterator();
            return Pair.of(this.a.read(recipe, itr.next()), this.b.read(recipe, itr.next()));
        } else {
            throw new IllegalArgumentException("Expected JSON array!");
        }
    }

    public String toString() {
        return "{" + this.a + "&" + this.b + "}";
    }
}