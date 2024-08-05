package dev.latvian.mods.kubejs.recipe.component;

import com.google.gson.JsonElement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Nullable;

public class MappingRecipeComponent<T> implements RecipeComponentWithParent<T> {

    private final RecipeComponent<T> parent;

    private final UnaryOperator<Object> mappingTo;

    private final UnaryOperator<JsonElement> mappingFrom;

    public MappingRecipeComponent(RecipeComponent<T> parent, UnaryOperator<Object> mappingTo, UnaryOperator<JsonElement> mappingFrom) {
        this.parent = parent;
        this.mappingTo = mappingTo;
        this.mappingFrom = mappingFrom;
    }

    @Override
    public String componentType() {
        return "mapping";
    }

    @Override
    public T read(RecipeJS recipe, Object from) {
        return RecipeComponentWithParent.super.read(recipe, this.mappingTo.apply(from));
    }

    @Nullable
    @Override
    public JsonElement write(RecipeJS recipe, T value) {
        return (JsonElement) this.mappingFrom.apply(RecipeComponentWithParent.super.write(recipe, value));
    }

    @Override
    public RecipeComponent<T> parentComponent() {
        return this.parent;
    }
}