package com.simibubi.create.foundation.config.ui;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.simibubi.create.foundation.utility.Pair;
import com.simibubi.create.infrastructure.config.AllConfigs;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class ConfigHelper {

    public static final Pattern unitPattern = Pattern.compile("\\[(in .*)]");

    public static final Pattern annotationPattern = Pattern.compile("\\[@cui:([^:]*)(?::(.*))?]");

    public static final Map<String, ConfigHelper.ConfigChange> changes = new HashMap();

    private static final LoadingCache<String, EnumMap<Type, ModConfig>> configCache = CacheBuilder.newBuilder().expireAfterAccess(5L, TimeUnit.MINUTES).build(new CacheLoader<String, EnumMap<Type, ModConfig>>() {

        public EnumMap<Type, ModConfig> load(@Nonnull String key) {
            return ConfigHelper.findModConfigsUncached(key);
        }
    });

    private static EnumMap<Type, ModConfig> findModConfigsUncached(String modID) {
        ModContainer modContainer = (ModContainer) ModList.get().getModContainerById(modID).orElseThrow(() -> new IllegalArgumentException("Unable to find ModContainer for id: " + modID));
        EnumMap<Type, ModConfig> configs = (EnumMap<Type, ModConfig>) ObfuscationReflectionHelper.getPrivateValue(ModContainer.class, modContainer, "configs");
        return (EnumMap<Type, ModConfig>) Objects.requireNonNull(configs);
    }

    public static IConfigSpec<?> findConfigSpecFor(Type type, String modID) {
        return (IConfigSpec<?>) (!modID.equals("create") ? ((ModConfig) ((EnumMap) configCache.getUnchecked(modID)).get(type)).getSpec() : AllConfigs.byType(type).specification);
    }

    @Nullable
    public static ForgeConfigSpec findForgeConfigSpecFor(Type type, String modID) {
        IConfigSpec<?> spec = findConfigSpecFor(type, modID);
        return spec instanceof ForgeConfigSpec ? (ForgeConfigSpec) spec : null;
    }

    public static boolean hasAnyConfig(String modID) {
        return !modID.equals("create") ? !((EnumMap) configCache.getUnchecked(modID)).isEmpty() : true;
    }

    public static boolean hasAnyForgeConfig(String modID) {
        return !modID.equals("create") ? ((EnumMap) configCache.getUnchecked(modID)).values().stream().anyMatch(config -> config.getSpec() instanceof ForgeConfigSpec) : true;
    }

    public static <T> void setConfigValue(ConfigHelper.ConfigPath path, String value) throws ConfigHelper.InvalidValueException {
        ForgeConfigSpec spec = findForgeConfigSpecFor(path.getType(), path.getModID());
        if (spec != null) {
            List<String> pathList = Arrays.asList(path.getPath());
            ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) spec.getRaw(pathList);
            ForgeConfigSpec.ConfigValue<T> configValue = (ForgeConfigSpec.ConfigValue<T>) spec.getValues().get(pathList);
            T v = (T) CConfigureConfigPacket.deserialize(configValue.get(), value);
            if (!valueSpec.test(v)) {
                throw new ConfigHelper.InvalidValueException();
            } else {
                configValue.set(v);
            }
        }
    }

    public static <T> void setValue(String path, ForgeConfigSpec.ConfigValue<T> configValue, T value, @Nullable Map<String, String> annotations) {
        if (value.equals(configValue.get())) {
            changes.remove(path);
        } else {
            changes.put(path, annotations == null ? new ConfigHelper.ConfigChange(value) : new ConfigHelper.ConfigChange(value, annotations));
        }
    }

    public static <T> T getValue(String path, ForgeConfigSpec.ConfigValue<T> configValue) {
        ConfigHelper.ConfigChange configChange = (ConfigHelper.ConfigChange) changes.get(path);
        return (T) (configChange != null ? configChange.value : configValue.get());
    }

    public static Pair<String, Map<String, String>> readMetadataFromComment(List<String> commentLines) {
        AtomicReference<String> unit = new AtomicReference();
        Map<String, String> annotations = new HashMap();
        commentLines.removeIf(line -> {
            if (line.trim().isEmpty()) {
                return true;
            } else {
                Matcher matcher = annotationPattern.matcher(line);
                if (matcher.matches()) {
                    String annotation = matcher.group(1);
                    String aValue = matcher.group(2);
                    annotations.putIfAbsent(annotation, aValue);
                    return true;
                } else {
                    matcher = unitPattern.matcher(line);
                    if (matcher.matches()) {
                        unit.set(matcher.group(1));
                    }
                    return false;
                }
            }
        });
        return Pair.of((String) unit.get(), annotations);
    }

    public static class ConfigChange {

        Object value;

        Map<String, String> annotations;

        ConfigChange(Object value) {
            this.value = value;
        }

        ConfigChange(Object value, Map<String, String> annotations) {
            this(value);
            this.annotations = new HashMap();
            this.annotations.putAll(annotations);
        }
    }

    public static class ConfigPath {

        private String modID = "create";

        private Type type = Type.CLIENT;

        private String[] path;

        public static ConfigHelper.ConfigPath parse(String string) {
            ConfigHelper.ConfigPath cp = new ConfigHelper.ConfigPath();
            String p = string;
            int index = string.indexOf(":");
            if (index >= 0) {
                p = string.substring(index + 1);
                if (index >= 1) {
                    cp.modID = string.substring(0, index);
                }
            }
            String[] split = p.split("\\.");
            try {
                cp.type = Type.valueOf(split[0].toUpperCase(Locale.ROOT));
            } catch (Exception var6) {
                throw new IllegalArgumentException("path must start with either 'client.', 'common.' or 'server.'");
            }
            cp.path = new String[split.length - 1];
            System.arraycopy(split, 1, cp.path, 0, cp.path.length);
            return cp;
        }

        public ConfigHelper.ConfigPath setID(String modID) {
            this.modID = modID;
            return this;
        }

        public ConfigHelper.ConfigPath setType(Type type) {
            this.type = type;
            return this;
        }

        public ConfigHelper.ConfigPath setPath(String[] path) {
            this.path = path;
            return this;
        }

        public String getModID() {
            return this.modID;
        }

        public Type getType() {
            return this.type;
        }

        public String[] getPath() {
            return this.path;
        }
    }

    public static class InvalidValueException extends Exception {

        private static final long serialVersionUID = 1L;
    }
}