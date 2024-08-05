package snownee.kiwi.config;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.EnumUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.KiwiModule;
import snownee.kiwi.loader.Platform;
import snownee.kiwi.shadowed.org.yaml.snakeyaml.Yaml;
import snownee.kiwi.util.Util;

public class ConfigHandler {

    public static final String FILE_EXTENSION = ".yaml";

    private final String modId;

    private final String fileName;

    private final KiwiConfig.ConfigType type;

    @Nullable
    private final Class<?> clazz;

    private final Map<String, ConfigHandler.Value<?>> valueMap = Maps.newLinkedHashMap();

    private boolean hasModules;

    public ConfigHandler(String modId, String fileName, KiwiConfig.ConfigType type, Class<?> clazz, boolean hasModules) {
        this.hasModules = hasModules;
        this.modId = modId;
        this.clazz = clazz;
        this.fileName = fileName;
        this.type = type;
        KiwiConfigManager.register(this);
    }

    private static void flatten(Map<String, Object> src, Map<String, Object> dst, String path, Set<String> keys) {
        for (Entry<String, Object> e : src.entrySet()) {
            String key = (String) e.getKey();
            String newPath = path + key;
            if (e.getValue() instanceof Map && !keys.contains(newPath)) {
                flatten((Map<String, Object>) e.getValue(), dst, newPath + ".", keys);
            } else {
                dst.put(newPath, e.getValue());
            }
        }
    }

    static List<String> getPath(Field field) {
        List<String> annotatedPath = getPath((AnnotatedElement) field);
        return annotatedPath == null ? Collections.singletonList(field.getName()) : annotatedPath;
    }

    static List<String> getPath(AnnotatedElement annotatedElement) {
        KiwiConfig.Path path = (KiwiConfig.Path) annotatedElement.getDeclaredAnnotation(KiwiConfig.Path.class);
        if (path != null) {
            return List.of(path.value().split("\\."));
        } else {
            KiwiConfig.AdvancedPath advancedPath = (KiwiConfig.AdvancedPath) annotatedElement.getDeclaredAnnotation(KiwiConfig.AdvancedPath.class);
            return advancedPath != null ? Arrays.asList(advancedPath.value()) : null;
        }
    }

    private static Map<String, Object> getEndMap(Map<String, Object> map, List<String> path) {
        int l = path.size() - 1;
        for (int i = 0; i < l; i++) {
            map = (Map<String, Object>) map.computeIfAbsent((String) path.get(i), $ -> Maps.newHashMap());
        }
        return map;
    }

    private static Object convert(Field field) {
        try {
            Class<?> type = field.getType();
            if (type != int.class && type != long.class && type != double.class && type != float.class && type != boolean.class && type != String.class && !Enum.class.isAssignableFrom(type) && !List.class.isAssignableFrom(type) && !Map.class.isAssignableFrom(type)) {
                return null;
            } else if (type == String.class) {
                String defaultVal = (String) field.get(null);
                if (defaultVal == null) {
                    defaultVal = "";
                }
                return defaultVal;
            } else if (List.class.isAssignableFrom(type)) {
                List<?> defaultVal = (List<?>) field.get(null);
                if (defaultVal == null) {
                    defaultVal = List.of();
                }
                return defaultVal;
            } else if (Map.class.isAssignableFrom(type)) {
                Map<?, ?> defaultVal = (Map<?, ?>) field.get(null);
                if (defaultVal == null) {
                    defaultVal = Map.of();
                }
                return defaultVal;
            } else {
                return field.get(null);
            }
        } catch (Exception var3) {
            return null;
        }
    }

    private static Class<?> toPrimitiveClass(Class<?> clazz) {
        if (clazz == Boolean.class) {
            return boolean.class;
        } else if (clazz == Integer.class) {
            return int.class;
        } else if (clazz == Long.class) {
            return long.class;
        } else if (clazz == Float.class) {
            return float.class;
        } else {
            return clazz == Double.class ? double.class : clazz;
        }
    }

    private Path getConfigPath() {
        return Platform.getConfigDir().resolve(this.fileName + ".yaml");
    }

    public void init() {
        this.build();
        Path configPath = this.getConfigPath();
        if (Files.exists(configPath, new LinkOption[0])) {
            this.refresh();
        }
        this.save();
    }

    public void refresh() {
        Path configPath = this.getConfigPath();
        Map<String, Object> map;
        try {
            FileReader reader = new FileReader(configPath.toFile(), StandardCharsets.UTF_8);
            try {
                map = new Yaml().loadAs(reader, Map.class);
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
            this.save();
            return;
        }
        if (map != null && !map.isEmpty()) {
            Map<String, Object> flatMap = Maps.newHashMap();
            flatten(map, flatMap, "", this.valueMap.keySet());
            for (Entry<String, Object> e : flatMap.entrySet()) {
                ConfigHandler.Value value = (ConfigHandler.Value) this.valueMap.get(e.getKey());
                if (value != null) {
                    Object $ = e.getValue();
                    Class<?> type = value.getType();
                    if (Enum.class.isAssignableFrom(type)) {
                        $ = EnumUtils.getEnumIgnoreCase(type, (String) $, (Enum) type.getEnumConstants()[0]);
                    }
                    value.accept($);
                }
            }
        } else {
            this.save();
        }
    }

    public void save() {
        Path configPath = this.getConfigPath();
        Map<String, Object> map = Maps.newLinkedHashMap();
        for (ConfigHandler.Value<?> value : this.valueMap.values()) {
            List<String> path = List.of(value.path.split("\\."));
            Object v = value.field == null ? value.value : convert(value.field);
            value.accept(v);
            if (v instanceof Enum) {
                v = ((Enum) v).name();
            }
            getEndMap(map, path).put((String) path.get(path.size() - 1), v);
        }
        try {
            FileWriter writer = new FileWriter(configPath.toFile(), StandardCharsets.UTF_8);
            try {
                writer.append("# Use Cloth Config mod for the descriptions.");
                writer.append('\n');
                writer.append("---");
                writer.append('\n');
                Util.dumpYaml(map, writer);
            } catch (Throwable var8) {
                try {
                    writer.close();
                } catch (Throwable var7) {
                    var8.addSuppressed(var7);
                }
                throw var8;
            }
            writer.close();
        } catch (IOException | JsonSyntaxException var9) {
            Kiwi.LOGGER.error("Failed to save config file: %s".formatted(configPath), var9);
        }
    }

    private void build() {
        if (this.hasModules) {
            KiwiConfigManager.defineModules(this.modId, this, !this.fileName.equals(this.modId + "-modules"));
        }
        if (this.clazz != null) {
            Joiner joiner = Joiner.on('.');
            for (Field field : this.clazz.getDeclaredFields()) {
                int mods = field.getModifiers();
                if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && !Modifier.isFinal(mods) && field.getAnnotation(KiwiModule.Skip.class) == null) {
                    List<String> path = getPath(field);
                    String pathKey = joiner.join(path);
                    KiwiConfig.Translation translation = (KiwiConfig.Translation) field.getAnnotation(KiwiConfig.Translation.class);
                    String translationKey;
                    if (translation != null) {
                        translationKey = translation.value();
                    } else {
                        translationKey = "%s.config.%s".formatted(this.modId, pathKey);
                    }
                    Object converted = convert(field);
                    if (converted != null) {
                        ConfigHandler.Value<?> value = this.define(pathKey, converted, field, translationKey);
                        if (field.getAnnotation(KiwiConfig.LevelRestart.class) != null || field.getAnnotation(KiwiConfig.GameRestart.class) != null) {
                            value.requiresRestart = true;
                        }
                        KiwiConfig.Range range = (KiwiConfig.Range) field.getAnnotation(KiwiConfig.Range.class);
                        if (range != null) {
                            value.min = range.min();
                            value.max = range.max();
                        }
                    }
                }
            }
            for (Method method : this.clazz.getDeclaredMethods()) {
                int mods = method.getModifiers();
                if (Modifier.isPublic(mods) && Modifier.isStatic(mods)) {
                    KiwiConfig.Listen[] listens = (KiwiConfig.Listen[]) method.getAnnotationsByType(KiwiConfig.Listen.class);
                    if (listens.length != 0) {
                        if (method.getParameterCount() != 1 || method.getParameterTypes()[0] != String.class) {
                            throw new IllegalArgumentException("Invalid listener method " + method);
                        }
                        for (KiwiConfig.Listen listen : listens) {
                            String pathx = listen.value();
                            ConfigHandler.Value<?> valuex = (ConfigHandler.Value<?>) this.valueMap.get(pathx);
                            if (valuex == null) {
                                throw new IllegalArgumentException("No config value found for path " + pathx);
                            }
                            valuex.listener = method;
                        }
                    }
                }
            }
        }
    }

    public <T> ConfigHandler.Value<T> define(String path, T value, @Nullable Field field, String translation) {
        ConfigHandler.Value<T> v = new ConfigHandler.Value<>(path, field, value, translation);
        this.valueMap.put(path, v);
        return v;
    }

    public void setHasModules(boolean hasModules) {
        this.hasModules = hasModules;
    }

    public boolean hasModules() {
        return this.hasModules;
    }

    public String getModId() {
        return this.modId;
    }

    public KiwiConfig.ConfigType getType() {
        return this.type;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getTranslationKey() {
        if (this.fileName.equals(this.modId + "-" + this.getType().extension())) {
            return this.getType().extension();
        } else {
            return this.fileName.equals(this.modId + "-modules") ? "modules" : this.fileName;
        }
    }

    public Class<?> getClazz() {
        return this.clazz;
    }

    public <T> ConfigHandler.Value<T> get(String path) {
        return (ConfigHandler.Value<T>) this.valueMap.get(path);
    }

    public Map<String, ConfigHandler.Value<?>> getValueMap() {
        return this.valueMap;
    }

    public static class Value<T> {

        @NotNull
        public final T defValue;

        @Nullable
        public Field field;

        @NotNull
        public T value;

        public boolean requiresRestart;

        public String translation;

        public double min = Double.NaN;

        public double max = Double.NaN;

        public final String path;

        @Nullable
        Method listener;

        public Value(String path, @Nullable Field field, T value, String translation) {
            this.path = path;
            this.field = field;
            this.defValue = this.value = value;
            this.translation = translation;
        }

        public T get() {
            return this.value;
        }

        public Class<?> getType() {
            return this.field != null ? this.field.getType() : ConfigHandler.toPrimitiveClass(this.value.getClass());
        }

        public void accept(Object $) {
            try {
                Class<?> type = this.getType();
                if (type == int.class) {
                    int min = Double.isNaN(this.min) ? Integer.MIN_VALUE : (int) this.min;
                    int max = Double.isNaN(this.max) ? Integer.MAX_VALUE : (int) this.max;
                    int value = ((Number) $).intValue();
                    $ = Mth.clamp(value, min, max);
                } else if (type == float.class) {
                    float min = Double.isNaN(this.min) ? Float.MIN_VALUE : (float) this.min;
                    float max = Double.isNaN(this.max) ? Float.MAX_VALUE : (float) this.max;
                    float value = ((Number) $).floatValue();
                    $ = Mth.clamp(value, min, max);
                } else if (type == double.class) {
                    double min = Double.isNaN(this.min) ? Double.MIN_VALUE : this.min;
                    double max = Double.isNaN(this.max) ? Double.MAX_VALUE : this.max;
                    double value = ((Number) $).doubleValue();
                    $ = Mth.clamp(value, min, max);
                } else if (type == long.class) {
                    long min = Double.isNaN(this.min) ? Long.MIN_VALUE : (long) this.min;
                    long max = Double.isNaN(this.max) ? Long.MAX_VALUE : (long) this.max;
                    long value = ((Number) $).longValue();
                    $ = Math.min(Math.max(value, min), max);
                }
                if (this.field != null) {
                    if (type == boolean.class) {
                        this.field.setBoolean(null, (Boolean) $);
                    } else if (type == int.class) {
                        this.field.setInt(null, (Integer) $);
                    } else if (type == float.class) {
                        this.field.setFloat(null, (Float) $);
                    } else if (type == double.class) {
                        this.field.setDouble(null, (Double) $);
                    } else if (type == long.class) {
                        this.field.setLong(null, (Long) $);
                    } else {
                        this.field.set(null, $);
                    }
                }
                boolean changed = !Objects.equals(this.value, $);
                this.value = (T) $;
                if (changed && this.listener != null) {
                    this.listener.invoke(null, this.path);
                }
            } catch (Exception var9) {
                Kiwi.LOGGER.error("Failed to set config value %s: %s".formatted(this.path, $), var9);
            }
        }

        public <A extends Annotation> A getAnnotation(Class<A> clazz) {
            return (A) (this.field == null ? null : this.field.getAnnotation(clazz));
        }
    }
}