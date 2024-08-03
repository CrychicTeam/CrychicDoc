package dev.latvian.mods.kubejs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import dev.latvian.mods.rhino.mod.util.JsonUtils;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.Collection;
import java.util.HexFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class JsonIO {

    @HideFromJS
    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setLenient().create();

    public static JsonElement copy(@Nullable JsonElement element) {
        return JsonUtils.copy(element);
    }

    @Nullable
    public static JsonElement of(@Nullable Object o) {
        if (o instanceof JsonElement) {
            return (JsonElement) o;
        } else if (o instanceof Map || o instanceof CompoundTag) {
            return MapJS.json(o);
        } else if (o instanceof Collection) {
            return ListJS.json(o);
        } else {
            JsonElement e = JsonUtils.of(o);
            return e == JsonNull.INSTANCE ? null : e;
        }
    }

    public static JsonPrimitive primitiveOf(@Nullable Object o) {
        return of(o) instanceof JsonPrimitive p ? p : null;
    }

    @Nullable
    public static Object toObject(@Nullable JsonElement json) {
        return JsonUtils.toObject(json);
    }

    public static String toString(JsonElement json) {
        return JsonUtils.toString(json);
    }

    public static String toPrettyString(JsonElement json) {
        return JsonUtils.toPrettyString(json);
    }

    public static JsonElement parseRaw(@Nullable String string) {
        return JsonUtils.fromString(string);
    }

    public static Object parse(String string) {
        return UtilsJS.wrap(parseRaw(string), JSObjectType.ANY);
    }

    @Nullable
    public static Object toPrimitive(@Nullable JsonElement element) {
        if (element == null || element.isJsonNull()) {
            return null;
        } else if (element.isJsonPrimitive()) {
            JsonPrimitive p = element.getAsJsonPrimitive();
            if (p.isBoolean()) {
                return p.getAsBoolean();
            } else if (p.isNumber()) {
                return p.getAsNumber();
            } else {
                try {
                    Double.parseDouble(p.getAsString());
                    return p.getAsNumber();
                } catch (Exception var3) {
                    return p.getAsString();
                }
            }
        } else {
            return null;
        }
    }

    public static JsonElement readJson(Path path) throws IOException {
        if (!Files.isRegularFile(path, new LinkOption[0])) {
            return null;
        } else {
            BufferedReader fileReader = Files.newBufferedReader(path);
            JsonElement var2;
            try {
                var2 = JsonParser.parseReader(fileReader);
            } catch (Throwable var5) {
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (fileReader != null) {
                fileReader.close();
            }
            return var2;
        }
    }

    public static String readString(Path path) throws IOException {
        return toString(readJson(path));
    }

    @Nullable
    public static Map<?, ?> read(Path path) throws IOException {
        return MapJS.of(readJson(path));
    }

    public static void write(Path path, @Nullable JsonObject json) throws IOException {
        if (json != null && !json.isJsonNull()) {
            Writer fileWriter = Files.newBufferedWriter(path);
            try {
                JsonWriter jsonWriter = new JsonWriter(fileWriter);
                jsonWriter.setIndent("\t");
                jsonWriter.setSerializeNulls(true);
                jsonWriter.setLenient(true);
                Streams.write(json, jsonWriter);
            } catch (Throwable var6) {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }
                }
                throw var6;
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
        } else {
            Files.deleteIfExists(path);
        }
    }

    public static JsonArray toArray(JsonElement element) {
        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        } else {
            JsonArray a = new JsonArray();
            a.add(element);
            return a;
        }
    }

    public static void writeJsonHash(DataOutputStream stream, @Nullable JsonElement element) throws IOException {
        if (element == null || element.isJsonNull()) {
            stream.writeByte(45);
        } else if (element instanceof JsonArray arr) {
            stream.writeByte(91);
            for (JsonElement e : arr) {
                writeJsonHash(stream, e);
            }
        } else if (element instanceof JsonObject obj) {
            stream.writeByte(123);
            for (Entry<String, JsonElement> e : obj.entrySet()) {
                stream.writeBytes((String) e.getKey());
                writeJsonHash(stream, (JsonElement) e.getValue());
            }
        } else if (element instanceof JsonPrimitive primitive) {
            stream.writeByte(61);
            if (primitive.isBoolean()) {
                stream.writeBoolean(element.getAsBoolean());
            } else if (primitive.isNumber()) {
                stream.writeDouble(element.getAsDouble());
            } else {
                stream.writeBytes(element.getAsString());
            }
        } else {
            stream.writeByte(63);
            stream.writeInt(element.hashCode());
        }
    }

    public static byte[] getJsonHashBytes(JsonElement json) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            writeJsonHash(new DataOutputStream(baos), json);
        } catch (IOException var4) {
            var4.printStackTrace();
            int h = json.hashCode();
            return new byte[] { (byte) (h >> 24), (byte) (h >> 16), (byte) (h >> 8), (byte) h };
        }
        return baos.toByteArray();
    }

    public static String getJsonHashString(JsonElement json) {
        try {
            MessageDigest messageDigest = (MessageDigest) Objects.requireNonNull(MessageDigest.getInstance("MD5"));
            return new BigInteger(HexFormat.of().formatHex(messageDigest.digest(getJsonHashBytes(json))), 16).toString(36);
        } catch (Exception var2) {
            return "%08x".formatted(json.hashCode());
        }
    }
}