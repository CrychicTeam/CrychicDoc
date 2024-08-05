package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import me.lucko.spark.lib.adventure.util.Index;

final class IndexedSerializer<E> extends TypeAdapter<E> {

    private final String name;

    private final Index<String, E> map;

    private final boolean throwOnUnknownKey;

    public static <E> TypeAdapter<E> strict(final String name, final Index<String, E> map) {
        return new IndexedSerializer<>(name, map, true).nullSafe();
    }

    public static <E> TypeAdapter<E> lenient(final String name, final Index<String, E> map) {
        return new IndexedSerializer<>(name, map, false).nullSafe();
    }

    private IndexedSerializer(final String name, final Index<String, E> map, final boolean throwOnUnknownKey) {
        this.name = name;
        this.map = map;
        this.throwOnUnknownKey = throwOnUnknownKey;
    }

    public void write(final JsonWriter out, final E value) throws IOException {
        out.value(this.map.key(value));
    }

    public E read(final JsonReader in) throws IOException {
        String string = in.nextString();
        E value = this.map.value(string);
        if (value != null) {
            return value;
        } else if (this.throwOnUnknownKey) {
            throw new JsonParseException("invalid " + this.name + ":  " + string);
        } else {
            return null;
        }
    }
}