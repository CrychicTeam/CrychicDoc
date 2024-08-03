package net.minecraft.data.models.blockstates;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Function;

public class VariantProperty<T> {

    final String key;

    final Function<T, JsonElement> serializer;

    public VariantProperty(String string0, Function<T, JsonElement> functionTJsonElement1) {
        this.key = string0;
        this.serializer = functionTJsonElement1;
    }

    public VariantProperty<T>.Value withValue(T t0) {
        return new VariantProperty.Value(t0);
    }

    public String toString() {
        return this.key;
    }

    public class Value {

        private final T value;

        public Value(T t0) {
            this.value = t0;
        }

        public VariantProperty<T> getKey() {
            return VariantProperty.this;
        }

        public void addToVariant(JsonObject jsonObject0) {
            jsonObject0.add(VariantProperty.this.key, (JsonElement) VariantProperty.this.serializer.apply(this.value));
        }

        public String toString() {
            return VariantProperty.this.key + "=" + this.value;
        }
    }
}