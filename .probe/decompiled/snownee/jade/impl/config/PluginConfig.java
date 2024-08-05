package snownee.jade.impl.config;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import snownee.jade.Jade;
import snownee.jade.api.IToggleableProvider;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.impl.config.entry.ConfigEntry;
import snownee.jade.util.CommonProxy;
import snownee.jade.util.JsonConfig;

public class PluginConfig implements IPluginConfig {

    public static final PluginConfig INSTANCE = new PluginConfig();

    public static final String CLIENT_FILE = "jade/plugins.json";

    public static final String SERVER_FILE = "jade/server-plugin-overrides.json";

    private final Map<ResourceLocation, ConfigEntry<Object>> configs = Maps.newHashMap();

    private JsonObject serverConfigs;

    private PluginConfig() {
    }

    public void addConfig(ConfigEntry<?> entry) {
        Preconditions.checkArgument(StringUtils.countMatches(entry.getId().getPath(), '.') <= 1);
        Preconditions.checkArgument(!this.containsKey(entry.getId()), "Duplicate config key: %s", entry.getId());
        Preconditions.checkArgument(entry.isValidValue(entry.getDefaultValue()), "Default value of config %s does not pass value check", entry.getId());
        this.configs.put(entry.getId(), entry);
    }

    @Override
    public Set<ResourceLocation> getKeys(String namespace) {
        return (Set<ResourceLocation>) this.getKeys().stream().filter(id -> id.getNamespace().equals(namespace)).collect(Collectors.toSet());
    }

    @Override
    public Set<ResourceLocation> getKeys() {
        return this.configs.keySet();
    }

    @Override
    public boolean get(IToggleableProvider provider) {
        return provider.isRequired() ? true : this.get(provider.getUid());
    }

    @Override
    public boolean get(ResourceLocation key) {
        return CommonProxy.isPhysicallyClient() ? (Boolean) this.getEntry(key).getValue() : (Boolean) Optional.ofNullable(this.serverConfigs).map($ -> $.getAsJsonObject(key.getNamespace())).map($ -> $.get(key.getPath())).map(JsonElement::getAsBoolean).orElse(false);
    }

    @Override
    public <T extends Enum<T>> T getEnum(ResourceLocation key) {
        return (T) this.getEntry(key).getValue();
    }

    @Override
    public int getInt(ResourceLocation key) {
        return (Integer) this.getEntry(key).getValue();
    }

    @Override
    public float getFloat(ResourceLocation key) {
        return (Float) this.getEntry(key).getValue();
    }

    @Override
    public String getString(ResourceLocation key) {
        return (String) this.getEntry(key).getValue();
    }

    public List<String> getNamespaces() {
        return (List<String>) this.configs.keySet().stream().sorted((o1, o2) -> o1.getNamespace().compareToIgnoreCase(o2.getNamespace())).map(ResourceLocation::m_135827_).distinct().collect(Collectors.toList());
    }

    public ConfigEntry<?> getEntry(ResourceLocation key) {
        return (ConfigEntry<?>) this.configs.get(key);
    }

    public boolean set(ResourceLocation key, Object value) {
        Objects.requireNonNull(value);
        ConfigEntry<?> entry = this.getEntry(key);
        if (entry == null) {
            Jade.LOGGER.warn("Skip setting value for unknown option: {}, {}", key, value);
            return false;
        } else if (!entry.isValidValue(value)) {
            Jade.LOGGER.warn("Skip setting illegal value for option: {}, {}", key, value);
            return false;
        } else {
            entry.setValue(value);
            return true;
        }
    }

    public File getFile() {
        boolean client = CommonProxy.isPhysicallyClient();
        return new File(CommonProxy.getConfigDirectory(), client ? "jade/plugins.json" : "jade/server-plugin-overrides.json");
    }

    public void reload() {
        boolean client = CommonProxy.isPhysicallyClient();
        File configFile = this.getFile();
        if (client) {
            this.configs.values().forEach($ -> $.setSynced(false));
        }
        if (!configFile.exists()) {
            this.writeConfig(configFile, true);
        }
        if (client) {
            Map<String, Map<String, Object>> config;
            try {
                FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8);
                try {
                    config = (Map<String, Map<String, Object>>) JsonConfig.GSON.fromJson(reader, (new TypeToken<Map<String, Map<String, Object>>>() {
                    }).getType());
                } catch (Throwable var13) {
                    try {
                        reader.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                    throw var13;
                }
                reader.close();
            } catch (Exception var14) {
                var14.printStackTrace();
                config = Maps.newHashMap();
            }
            MutableBoolean saveFlag = new MutableBoolean();
            Set<ResourceLocation> found = Sets.newHashSet();
            config.forEach((namespace, subMap) -> subMap.forEach((path, value) -> {
                ResourceLocation idx = new ResourceLocation(namespace, path);
                if (this.configs.containsKey(idx)) {
                    if (!this.set(idx, value)) {
                        saveFlag.setTrue();
                    }
                    found.add(idx);
                }
            }));
            for (ResourceLocation id : this.getKeys()) {
                if (!found.contains(id)) {
                    this.set(id, this.getEntry(id).getDefaultValue());
                    saveFlag.setTrue();
                }
            }
            if (saveFlag.isTrue()) {
                this.save();
            }
        } else {
            try {
                FileReader reader = new FileReader(configFile, StandardCharsets.UTF_8);
                try {
                    this.serverConfigs = (JsonObject) JsonConfig.GSON.fromJson(reader, JsonObject.class);
                } catch (Throwable var10) {
                    try {
                        reader.close();
                    } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                    }
                    throw var10;
                }
                reader.close();
            } catch (Exception var11) {
                var11.printStackTrace();
                this.serverConfigs = null;
            }
        }
    }

    public void save() {
        this.writeConfig(this.getFile(), false);
    }

    private void writeConfig(File file, boolean reset) {
        boolean client = CommonProxy.isPhysicallyClient();
        String json;
        if (client) {
            Map<String, Map<String, Object>> config = Maps.newHashMap();
            this.configs.values().forEach(ex -> {
                Map<String, Object> modConfig = (Map<String, Object>) config.computeIfAbsent(ex.getId().getNamespace(), k -> Maps.newHashMap());
                if (reset) {
                    ex.setValue(ex.getDefaultValue());
                }
                modConfig.put(ex.getId().getPath(), ex.getValue());
            });
            json = JsonConfig.GSON.toJson(config);
        } else {
            json = "{}";
        }
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8);
            try {
                writer.write(json);
            } catch (Throwable var9) {
                try {
                    writer.close();
                } catch (Throwable var8) {
                    var9.addSuppressed(var8);
                }
                throw var9;
            }
            writer.close();
        } catch (IOException var10) {
            var10.printStackTrace();
        }
    }

    public void applyServerConfigs(JsonObject json) {
        json.keySet().forEach(namespace -> json.getAsJsonObject(namespace).entrySet().forEach(entry -> {
            ResourceLocation key = new ResourceLocation(namespace, (String) entry.getKey());
            ConfigEntry<?> configEntry = this.getEntry(key);
            if (configEntry != null) {
                JsonPrimitive primitive = ((JsonElement) entry.getValue()).getAsJsonPrimitive();
                Object v;
                if (primitive.isBoolean()) {
                    v = primitive.getAsBoolean();
                } else if (primitive.isNumber()) {
                    v = primitive.getAsNumber();
                } else {
                    if (!primitive.isString()) {
                        return;
                    }
                    v = primitive.getAsString();
                }
                if (configEntry.isValidValue(v)) {
                    configEntry.setValue(v);
                    configEntry.setSynced(true);
                }
            }
        }));
    }

    public String getServerConfigs() {
        return this.serverConfigs == null ? "" : this.serverConfigs.toString();
    }

    public boolean containsKey(ResourceLocation uid) {
        return this.configs.containsKey(uid);
    }

    public void addConfigListener(ResourceLocation key, Consumer<ResourceLocation> listener) {
        Preconditions.checkArgument(this.containsKey(key));
        ((ConfigEntry) this.configs.get(key)).addListener(listener);
    }
}