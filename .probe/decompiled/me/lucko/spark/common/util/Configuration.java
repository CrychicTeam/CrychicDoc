package me.lucko.spark.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Configuration {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path file;

    private JsonObject root;

    public Configuration(Path file) {
        this.file = file;
        this.load();
    }

    public void load() {
        JsonObject root = null;
        if (Files.exists(this.file, new LinkOption[0])) {
            try {
                BufferedReader reader = Files.newBufferedReader(this.file, StandardCharsets.UTF_8);
                try {
                    root = (JsonObject) GSON.fromJson(reader, JsonObject.class);
                } catch (Throwable var6) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }
                    throw var6;
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException var7) {
                var7.printStackTrace();
            }
        }
        if (root == null) {
            root = new JsonObject();
            root.addProperty("_header", "spark configuration file - https://spark.lucko.me/docs/Configuration");
        }
        this.root = root;
    }

    public void save() {
        try {
            Files.createDirectories(this.file.getParent());
        } catch (IOException var5) {
        }
        try {
            BufferedWriter writer = Files.newBufferedWriter(this.file, StandardCharsets.UTF_8);
            try {
                GSON.toJson(this.root, writer);
            } catch (Throwable var6) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var4) {
                        var6.addSuppressed(var4);
                    }
                }
                throw var6;
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException var7) {
            var7.printStackTrace();
        }
    }

    public String getString(String path, String def) {
        JsonElement el = this.root.get(path);
        return el != null && el.isJsonPrimitive() ? el.getAsJsonPrimitive().getAsString() : def;
    }

    public boolean getBoolean(String path, boolean def) {
        JsonElement el = this.root.get(path);
        if (el != null && el.isJsonPrimitive()) {
            JsonPrimitive val = el.getAsJsonPrimitive();
            return val.isBoolean() ? val.getAsBoolean() : def;
        } else {
            return def;
        }
    }

    public int getInteger(String path, int def) {
        JsonElement el = this.root.get(path);
        if (el != null && el.isJsonPrimitive()) {
            JsonPrimitive val = el.getAsJsonPrimitive();
            return val.isNumber() ? val.getAsInt() : def;
        } else {
            return def;
        }
    }

    public List<String> getStringList(String path) {
        JsonElement el = this.root.get(path);
        if (el != null && el.isJsonArray()) {
            List<String> list = new ArrayList();
            for (JsonElement child : el.getAsJsonArray()) {
                if (child.isJsonPrimitive()) {
                    list.add(child.getAsJsonPrimitive().getAsString());
                }
            }
            return list;
        } else {
            return Collections.emptyList();
        }
    }

    public void setString(String path, String value) {
        this.root.add(path, new JsonPrimitive(value));
    }

    public void setBoolean(String path, boolean value) {
        this.root.add(path, new JsonPrimitive(value));
    }

    public void setInteger(String path, int value) {
        this.root.add(path, new JsonPrimitive(value));
    }

    public void setStringList(String path, List<String> value) {
        JsonArray array = new JsonArray();
        for (String str : value) {
            array.add(str);
        }
        this.root.add(path, array);
    }

    public boolean contains(String path) {
        return this.root.has(path);
    }

    public void remove(String path) {
        this.root.remove(path);
    }
}