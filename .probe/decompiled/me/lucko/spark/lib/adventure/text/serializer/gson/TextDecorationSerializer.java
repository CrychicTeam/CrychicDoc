package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import me.lucko.spark.lib.adventure.text.format.TextDecoration;

final class TextDecorationSerializer {

    static final TypeAdapter<TextDecoration> INSTANCE = IndexedSerializer.strict("text decoration", TextDecoration.NAMES);

    private TextDecorationSerializer() {
    }
}