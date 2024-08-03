package net.minecraft.data.models.blockstates;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Variant implements Supplier<JsonElement> {

    private final Map<VariantProperty<?>, VariantProperty<?>.Value> values = Maps.newLinkedHashMap();

    public <T> Variant with(VariantProperty<T> variantPropertyT0, T t1) {
        VariantProperty<?>.Value $$2 = (VariantProperty.Value) this.values.put(variantPropertyT0, variantPropertyT0.withValue(t1));
        if ($$2 != null) {
            throw new IllegalStateException("Replacing value of " + $$2 + " with " + t1);
        } else {
            return this;
        }
    }

    public static Variant variant() {
        return new Variant();
    }

    public static Variant merge(Variant variant0, Variant variant1) {
        Variant $$2 = new Variant();
        $$2.values.putAll(variant0.values);
        $$2.values.putAll(variant1.values);
        return $$2;
    }

    public JsonElement get() {
        JsonObject $$0 = new JsonObject();
        this.values.values().forEach(p_125507_ -> p_125507_.addToVariant($$0));
        return $$0;
    }

    public static JsonElement convertList(List<Variant> listVariant0) {
        if (listVariant0.size() == 1) {
            return ((Variant) listVariant0.get(0)).get();
        } else {
            JsonArray $$1 = new JsonArray();
            listVariant0.forEach(p_125504_ -> $$1.add(p_125504_.get()));
            return $$1;
        }
    }
}