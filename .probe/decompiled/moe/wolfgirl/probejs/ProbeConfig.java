package moe.wolfgirl.probejs;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import moe.wolfgirl.probejs.utils.JsonUtils;

public class ProbeConfig {

    public static ProbeConfig INSTANCE = new ProbeConfig();

    public ProbeConfig.ConfigEntry<Boolean> enabled = new ProbeConfig.ConfigEntry<>("enabled", true);

    public ProbeConfig.ConfigEntry<Boolean> enableDecompiler = new ProbeConfig.ConfigEntry<>("enableDecompiler", false);

    public ProbeConfig.ConfigEntry<Boolean> aggressive = new ProbeConfig.ConfigEntry<>("aggressive", false);

    public ProbeConfig.ConfigEntry<Boolean> interactive = new ProbeConfig.ConfigEntry<>("interactive", false);

    public ProbeConfig.ConfigEntry<Integer> interactivePort = new ProbeConfig.ConfigEntry<>("interactivePort", 7796);

    public ProbeConfig.ConfigEntry<Long> modHash = new ProbeConfig.ConfigEntry<>("modHash", -1L);

    public ProbeConfig.ConfigEntry<Long> registryHash = new ProbeConfig.ConfigEntry<>("registryHash", -1L);

    public ProbeConfig.ConfigEntry<Boolean> isolatedScopes = new ProbeConfig.ConfigEntry<>("isolatedScope", true);

    private static void writeConfigEntry(ProbeConfig.ConfigEntry<?> configEntry) throws IOException {
        String name = configEntry.name;
        Object value = configEntry.value;
        JsonObject current = new JsonObject();
        if (Files.exists(ProbePaths.SETTINGS_JSON, new LinkOption[0])) {
            BufferedReader reader = Files.newBufferedReader(ProbePaths.SETTINGS_JSON);
            try {
                String content = (String) reader.lines().collect(Collectors.joining("\n"));
                current = (JsonObject) ProbeJS.GSON.fromJson(JsonUtils.stripSussyJson5Stuffs(content), JsonObject.class);
            } catch (Throwable var10) {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable var8) {
                        var10.addSuppressed(var8);
                    }
                }
                throw var10;
            }
            if (reader != null) {
                reader.close();
            }
        }
        current.add("%s.%s".formatted(configEntry.namespace, name), JsonUtils.parseObject(value));
        BufferedWriter writer = Files.newBufferedWriter(ProbePaths.SETTINGS_JSON);
        try {
            JsonWriter jsonWriter = ProbeJS.GSON_WRITER.newJsonWriter(writer);
            jsonWriter.setIndent("    ");
            ProbeJS.GSON_WRITER.toJson(current, JsonObject.class, jsonWriter);
        } catch (Throwable var9) {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Throwable var7) {
                    var9.addSuppressed(var7);
                }
            }
            throw var9;
        }
        if (writer != null) {
            writer.close();
        }
    }

    private static Object getConfigEntry(ProbeConfig.ConfigEntry<?> configEntry) throws IOException {
        JsonObject current = new JsonObject();
        if (Files.exists(ProbePaths.SETTINGS_JSON, new LinkOption[0])) {
            BufferedReader reader = Files.newBufferedReader(ProbePaths.SETTINGS_JSON);
            try {
                String content = (String) reader.lines().collect(Collectors.joining("\n"));
                current = (JsonObject) ProbeJS.GSON.fromJson(JsonUtils.stripSussyJson5Stuffs(content), JsonObject.class);
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
        }
        return JsonUtils.deserializeObject(current) instanceof Map<?, ?> map ? map.get("%s.%s".formatted(configEntry.namespace, configEntry.name)) : null;
    }

    public static class ConfigEntry<T> {

        public final String name;

        public final T defaultValue;

        private T value;

        private final String namespace;

        ConfigEntry(String name, @Nonnull T defaultValue) {
            this(name, defaultValue, "probejs");
        }

        ConfigEntry(String name, @Nonnull T defaultValue, String namespace) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.namespace = namespace;
        }

        public void set(T value) {
            if (value == null) {
                value = this.defaultValue;
            }
            if (!Objects.equals(this.value, value)) {
                this.value = value;
                try {
                    ProbeConfig.writeConfigEntry(this);
                } catch (IOException var3) {
                }
            }
        }

        public T get() {
            try {
                this.fromSetting();
            } catch (IOException var2) {
                return this.defaultValue;
            }
            return this.value == null ? this.defaultValue : this.value;
        }

        private void fromSetting() throws IOException {
            Class<?> typeClass = this.defaultValue.getClass();
            Object configValue = ProbeConfig.getConfigEntry(this);
            if (configValue == null) {
                this.value = null;
            } else if (configValue instanceof Number number) {
                if (typeClass == Integer.class) {
                    configValue = number.intValue();
                }
                if (typeClass == Float.class) {
                    configValue = number.floatValue();
                }
                if (typeClass == Long.class) {
                    configValue = number.longValue();
                }
                if (typeClass == Byte.class) {
                    configValue = number.byteValue();
                }
                if (typeClass == Double.class) {
                    configValue = number.doubleValue();
                }
                if (typeClass == Short.class) {
                    configValue = number.shortValue();
                }
                this.value = (T) configValue;
            } else if (typeClass.isInstance(configValue)) {
                this.value = (T) configValue;
            }
        }
    }
}