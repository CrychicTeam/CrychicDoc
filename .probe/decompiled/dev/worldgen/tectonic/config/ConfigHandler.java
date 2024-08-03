package dev.worldgen.tectonic.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import dev.worldgen.tectonic.Tectonic;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

public class ConfigHandler {

    private static ConfigCodec LOADED_CONFIG = new ConfigCodec(true, ConfigCodec.Legacy.DEFAULT, ConfigCodec.Features.DEFAULT, ConfigCodec.Experimental.DEFAULT);

    public static ConfigCodec getConfig() {
        return LOADED_CONFIG;
    }

    public static void load(Path path) {
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            write(path);
        }
        try {
            BufferedReader reader = Files.newBufferedReader(path);
            try {
                JsonElement json = JsonParser.parseReader(reader);
                Optional<ConfigCodec> result = ConfigCodec.CODEC.parse(JsonOps.INSTANCE, json).result();
                if (!result.isPresent()) {
                    throw new JsonParseException("Invalid codec");
                }
                LOADED_CONFIG = (ConfigCodec) result.get();
            } catch (Throwable var5) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (reader != null) {
                reader.close();
            }
        } catch (JsonParseException var6) {
            Tectonic.LOGGER.error("Couldn't parse config file, resetting to default config");
        } catch (Exception var7) {
            throw new RuntimeException(var7);
        }
        write(path);
    }

    private static void write(Path path) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(path);
            try {
                JsonElement element = (JsonElement) ConfigCodec.CODEC.encodeStart(JsonOps.INSTANCE, LOADED_CONFIG).result().get();
                StringWriter stringWriter = new StringWriter();
                JsonWriter jsonWriter = new JsonWriter(stringWriter);
                jsonWriter.setIndent("  ");
                writeSortedKeys(jsonWriter, element);
                writer.write(commentHack(stringWriter.toString()));
            } catch (Throwable var6) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (writer != null) {
                writer.close();
            }
        } catch (Exception var7) {
            throw new RuntimeException(var7);
        }
    }

    public static void writeSortedKeys(JsonWriter writer, @Nullable JsonElement json) throws IOException {
        if (json == null || json.isJsonNull()) {
            writer.nullValue();
        } else if (json instanceof JsonPrimitive jsonPrimitive) {
            if (jsonPrimitive.isNumber()) {
                writer.value(jsonPrimitive.getAsNumber());
            } else if (jsonPrimitive.isBoolean()) {
                writer.value(jsonPrimitive.getAsBoolean());
            } else {
                writer.value(jsonPrimitive.getAsString());
            }
        } else if (json.isJsonArray()) {
            writer.beginArray();
            for (JsonElement element : json.getAsJsonArray()) {
                writeSortedKeys(writer, element);
            }
            writer.endArray();
        } else {
            if (!json.isJsonObject()) {
                throw new IllegalArgumentException("Couldn't write " + json.getClass());
            }
            writer.beginObject();
            for (Entry<String, JsonElement> elementEntry : sort(json.getAsJsonObject().entrySet())) {
                writer.name((String) elementEntry.getKey());
                writeSortedKeys(writer, (JsonElement) elementEntry.getValue());
            }
            writer.endObject();
        }
    }

    private static Collection<Entry<String, JsonElement>> sort(Collection<Entry<String, JsonElement>> entries) {
        List<Entry<String, JsonElement>> list = new ArrayList(entries);
        list.sort(Entry.comparingByKey(String::compareTo));
        return list;
    }

    private static String commentHack(String json) {
        return json.replaceAll("\"__.\": \"", "// ").replaceAll("\"...__\": \"", "// ").replace("\",", "");
    }
}