package me.lucko.spark.lib.adventure.text.serializer.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Locale;
import me.lucko.spark.lib.adventure.text.format.NamedTextColor;
import me.lucko.spark.lib.adventure.text.format.TextColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextColorSerializer extends TypeAdapter<TextColor> {

    static final TypeAdapter<TextColor> INSTANCE = new TextColorSerializer(false).nullSafe();

    static final TypeAdapter<TextColor> DOWNSAMPLE_COLOR = new TextColorSerializer(true).nullSafe();

    private final boolean downsampleColor;

    private TextColorSerializer(final boolean downsampleColor) {
        this.downsampleColor = downsampleColor;
    }

    public void write(final JsonWriter out, final TextColor value) throws IOException {
        if (value instanceof NamedTextColor) {
            out.value(NamedTextColor.NAMES.key((NamedTextColor) value));
        } else if (this.downsampleColor) {
            out.value(NamedTextColor.NAMES.key(NamedTextColor.nearestTo(value)));
        } else {
            out.value(asUpperCaseHexString(value));
        }
    }

    private static String asUpperCaseHexString(final TextColor color) {
        return String.format(Locale.ROOT, "#%06X", color.value());
    }

    @Nullable
    public TextColor read(final JsonReader in) throws IOException {
        TextColor color = fromString(in.nextString());
        if (color == null) {
            return null;
        } else {
            return (TextColor) (this.downsampleColor ? NamedTextColor.nearestTo(color) : color);
        }
    }

    @Nullable
    static TextColor fromString(@NotNull final String value) {
        return value.startsWith("#") ? TextColor.fromHexString(value) : NamedTextColor.NAMES.value(value);
    }
}