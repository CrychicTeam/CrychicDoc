package net.minecraftforge.common;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.EnumGetMethod;
import com.electronwill.nightconfig.core.InMemoryFormat;
import com.electronwill.nightconfig.core.UnmodifiableConfig;
import com.electronwill.nightconfig.core.ConfigSpec.CorrectionAction;
import com.electronwill.nightconfig.core.ConfigSpec.CorrectionListener;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.utils.UnmodifiableConfigWrapper;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class ForgeConfigSpec extends UnmodifiableConfigWrapper<UnmodifiableConfig> implements IConfigSpec<ForgeConfigSpec> {

    private final Map<List<String>, String> levelComments;

    private final Map<List<String>, String> levelTranslationKeys;

    private final UnmodifiableConfig values;

    private Config childConfig;

    private boolean isCorrecting = false;

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Pattern WINDOWS_NEWLINE = Pattern.compile("\r\n");

    private static final Joiner LINE_JOINER = Joiner.on("\n");

    private static final Joiner DOT_JOINER = Joiner.on(".");

    private static final Splitter DOT_SPLITTER = Splitter.on(".");

    private ForgeConfigSpec(UnmodifiableConfig storage, UnmodifiableConfig values, Map<List<String>, String> levelComments, Map<List<String>, String> levelTranslationKeys) {
        super(storage);
        this.values = values;
        this.levelComments = levelComments;
        this.levelTranslationKeys = levelTranslationKeys;
    }

    public String getLevelComment(List<String> path) {
        return (String) this.levelComments.get(path);
    }

    public String getLevelTranslationKey(List<String> path) {
        return (String) this.levelTranslationKeys.get(path);
    }

    public void setConfig(CommentedConfig config) {
        this.childConfig = config;
        if (config != null && !this.isCorrect(config)) {
            String configName = config instanceof FileConfig ? ((FileConfig) config).getNioPath().toString() : config.toString();
            LOGGER.warn(Logging.CORE, "Configuration file {} is not correct. Correcting", configName);
            this.correct(config, (action, path, incorrectValue, correctedValue) -> LOGGER.warn(Logging.CORE, "Incorrect key {} was corrected from {} to its default, {}. {}", DOT_JOINER.join(path), incorrectValue, correctedValue, incorrectValue == correctedValue ? "This seems to be an error." : ""), (action, path, incorrectValue, correctedValue) -> LOGGER.debug(Logging.CORE, "The comment on key {} does not match the spec. This may create a backup.", DOT_JOINER.join(path)));
            if (config instanceof FileConfig) {
                ((FileConfig) config).save();
            }
        }
        this.afterReload();
    }

    public void acceptConfig(CommentedConfig data) {
        this.setConfig(data);
    }

    public boolean isCorrecting() {
        return this.isCorrecting;
    }

    public boolean isLoaded() {
        return this.childConfig != null;
    }

    public UnmodifiableConfig getSpec() {
        return this.config;
    }

    public UnmodifiableConfig getValues() {
        return this.values;
    }

    public void afterReload() {
        this.resetCaches(this.getValues().valueMap().values());
    }

    private void resetCaches(Iterable<Object> configValues) {
        configValues.forEach(value -> {
            if (value instanceof ForgeConfigSpec.ConfigValue<?> configValue) {
                configValue.clearCache();
            } else if (value instanceof Config innerConfig) {
                this.resetCaches(innerConfig.valueMap().values());
            }
        });
    }

    public void save() {
        Preconditions.checkNotNull(this.childConfig, "Cannot save config value without assigned Config object present");
        if (this.childConfig instanceof FileConfig) {
            ((FileConfig) this.childConfig).save();
        }
    }

    public synchronized boolean isCorrect(CommentedConfig config) {
        LinkedList<String> parentPath = new LinkedList();
        return this.correct(this.config, config, parentPath, Collections.unmodifiableList(parentPath), (a, b, c, d) -> {
        }, null, true) == 0;
    }

    public int correct(CommentedConfig config) {
        return this.correct(config, (action, path, incorrectValue, correctedValue) -> {
        }, null);
    }

    public synchronized int correct(CommentedConfig config, CorrectionListener listener) {
        return this.correct(config, listener, null);
    }

    public synchronized int correct(CommentedConfig config, CorrectionListener listener, CorrectionListener commentListener) {
        LinkedList<String> parentPath = new LinkedList();
        int ret = -1;
        try {
            this.isCorrecting = true;
            ret = this.correct(this.config, config, parentPath, Collections.unmodifiableList(parentPath), listener, commentListener, false);
        } finally {
            this.isCorrecting = false;
        }
        return ret;
    }

    private int correct(UnmodifiableConfig spec, CommentedConfig config, LinkedList<String> parentPath, List<String> parentPathUnmodifiable, CorrectionListener listener, CorrectionListener commentListener, boolean dryRun) {
        int count = 0;
        Map<String, Object> specMap = spec.valueMap();
        Map<String, Object> configMap = config.valueMap();
        for (Entry<String, Object> specEntry : specMap.entrySet()) {
            String key = (String) specEntry.getKey();
            Object specValue = specEntry.getValue();
            Object configValue = configMap.get(key);
            CorrectionAction action = configValue == null ? CorrectionAction.ADD : CorrectionAction.REPLACE;
            parentPath.addLast(key);
            if (specValue instanceof Config) {
                if (configValue instanceof CommentedConfig) {
                    count += this.correct((Config) specValue, (CommentedConfig) configValue, parentPath, parentPathUnmodifiable, listener, commentListener, dryRun);
                    if (count > 0 && dryRun) {
                        return count;
                    }
                } else {
                    if (dryRun) {
                        return 1;
                    }
                    CommentedConfig newValue = config.createSubConfig();
                    configMap.put(key, newValue);
                    listener.onCorrect(action, parentPathUnmodifiable, configValue, newValue);
                    count = ++count + this.correct((Config) specValue, newValue, parentPath, parentPathUnmodifiable, listener, commentListener, dryRun);
                }
                String newComment = (String) this.levelComments.get(parentPath);
                String oldComment = config.getComment(key);
                if (!this.stringsMatchIgnoringNewlines(oldComment, newComment)) {
                    if (commentListener != null) {
                        commentListener.onCorrect(action, parentPathUnmodifiable, oldComment, newComment);
                    }
                    if (dryRun) {
                        return 1;
                    }
                    config.setComment(key, newComment);
                }
            } else {
                ForgeConfigSpec.ValueSpec valueSpec = (ForgeConfigSpec.ValueSpec) specValue;
                if (!valueSpec.test(configValue)) {
                    if (dryRun) {
                        return 1;
                    }
                    Object newValue = valueSpec.correct(configValue);
                    configMap.put(key, newValue);
                    listener.onCorrect(action, parentPathUnmodifiable, configValue, newValue);
                    count++;
                }
                String oldComment = config.getComment(key);
                if (!this.stringsMatchIgnoringNewlines(oldComment, valueSpec.getComment())) {
                    if (commentListener != null) {
                        commentListener.onCorrect(action, parentPathUnmodifiable, oldComment, valueSpec.getComment());
                    }
                    if (dryRun) {
                        return 1;
                    }
                    config.setComment(key, valueSpec.getComment());
                }
            }
            parentPath.removeLast();
        }
        Iterator<Entry<String, Object>> ittr = configMap.entrySet().iterator();
        while (ittr.hasNext()) {
            Entry<String, Object> entry = (Entry<String, Object>) ittr.next();
            if (!specMap.containsKey(entry.getKey())) {
                if (dryRun) {
                    return 1;
                }
                ittr.remove();
                parentPath.addLast((String) entry.getKey());
                listener.onCorrect(CorrectionAction.REMOVE, parentPathUnmodifiable, entry.getValue(), null);
                parentPath.removeLast();
                count++;
            }
        }
        return count;
    }

    private boolean stringsMatchIgnoringNewlines(@Nullable Object obj1, @Nullable Object obj2) {
        if (obj1 instanceof String string1 && obj2 instanceof String string2 && !string1.isEmpty() && !string2.isEmpty()) {
            return WINDOWS_NEWLINE.matcher(string1).replaceAll("\n").equals(WINDOWS_NEWLINE.matcher(string2).replaceAll("\n"));
        }
        return Objects.equals(obj1, obj2);
    }

    private static List<String> split(String path) {
        return Lists.newArrayList(DOT_SPLITTER.split(path));
    }

    public static class BooleanValue extends ForgeConfigSpec.ConfigValue<Boolean> {

        BooleanValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<Boolean> defaultSupplier) {
            super(parent, path, defaultSupplier);
        }
    }

    public static class Builder {

        private final Config storage = Config.of(LinkedHashMap::new, InMemoryFormat.withUniversalSupport());

        private ForgeConfigSpec.BuilderContext context = new ForgeConfigSpec.BuilderContext();

        private final Map<List<String>, String> levelComments = new HashMap();

        private final Map<List<String>, String> levelTranslationKeys = new HashMap();

        private final List<String> currentPath = new ArrayList();

        private final List<ForgeConfigSpec.ConfigValue<?>> values = new ArrayList();

        public <T> ForgeConfigSpec.ConfigValue<T> define(String path, T defaultValue) {
            return this.define(ForgeConfigSpec.split(path), defaultValue);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, T defaultValue) {
            return this.define(path, defaultValue, o -> o != null && defaultValue.getClass().isAssignableFrom(o.getClass()));
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(String path, T defaultValue, Predicate<Object> validator) {
            return this.define(ForgeConfigSpec.split(path), defaultValue, validator);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, T defaultValue, Predicate<Object> validator) {
            Objects.requireNonNull(defaultValue, "Default value can not be null");
            return this.define(path, (Supplier<T>) (() -> defaultValue), validator);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(String path, Supplier<T> defaultSupplier, Predicate<Object> validator) {
            return this.define(ForgeConfigSpec.split(path), defaultSupplier, validator);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, Supplier<T> defaultSupplier, Predicate<Object> validator) {
            return this.define(path, defaultSupplier, validator, Object.class);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, Supplier<T> defaultSupplier, Predicate<Object> validator, Class<?> clazz) {
            this.context.setClazz(clazz);
            return this.define(path, new ForgeConfigSpec.ValueSpec(defaultSupplier, validator, this.context, path), defaultSupplier);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> define(List<String> path, ForgeConfigSpec.ValueSpec value, Supplier<T> defaultSupplier) {
            if (!this.currentPath.isEmpty()) {
                List<String> tmp = new ArrayList(this.currentPath.size() + path.size());
                tmp.addAll(this.currentPath);
                tmp.addAll(path);
                path = tmp;
            }
            this.storage.set(path, value);
            this.context = new ForgeConfigSpec.BuilderContext();
            return new ForgeConfigSpec.ConfigValue<>(this, path, defaultSupplier);
        }

        public <V extends Comparable<? super V>> ForgeConfigSpec.ConfigValue<V> defineInRange(String path, V defaultValue, V min, V max, Class<V> clazz) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultValue, min, max, clazz);
        }

        public <V extends Comparable<? super V>> ForgeConfigSpec.ConfigValue<V> defineInRange(List<String> path, V defaultValue, V min, V max, Class<V> clazz) {
            return this.defineInRange(path, (Supplier<V>) (() -> defaultValue), min, max, clazz);
        }

        public <V extends Comparable<? super V>> ForgeConfigSpec.ConfigValue<V> defineInRange(String path, Supplier<V> defaultSupplier, V min, V max, Class<V> clazz) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultSupplier, min, max, clazz);
        }

        public <V extends Comparable<? super V>> ForgeConfigSpec.ConfigValue<V> defineInRange(List<String> path, Supplier<V> defaultSupplier, V min, V max, Class<V> clazz) {
            ForgeConfigSpec.Range<V> range = new ForgeConfigSpec.Range<>(clazz, min, max);
            this.context.setRange(range);
            this.comment("Range: " + range.toString());
            if (min.compareTo(max) > 0) {
                throw new IllegalArgumentException("Range min most be less then max.");
            } else {
                return this.define(path, defaultSupplier, range);
            }
        }

        public <T> ForgeConfigSpec.ConfigValue<T> defineInList(String path, T defaultValue, Collection<? extends T> acceptableValues) {
            return this.defineInList(ForgeConfigSpec.split(path), defaultValue, acceptableValues);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> defineInList(String path, Supplier<T> defaultSupplier, Collection<? extends T> acceptableValues) {
            return this.defineInList(ForgeConfigSpec.split(path), defaultSupplier, acceptableValues);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> defineInList(List<String> path, T defaultValue, Collection<? extends T> acceptableValues) {
            return this.defineInList(path, (Supplier<T>) (() -> defaultValue), acceptableValues);
        }

        public <T> ForgeConfigSpec.ConfigValue<T> defineInList(List<String> path, Supplier<T> defaultSupplier, Collection<? extends T> acceptableValues) {
            return this.define(path, defaultSupplier, acceptableValues::contains);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineList(String path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
            return this.defineList(ForgeConfigSpec.split(path), defaultValue, elementValidator);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineList(String path, Supplier<List<? extends T>> defaultSupplier, Predicate<Object> elementValidator) {
            return this.defineList(ForgeConfigSpec.split(path), defaultSupplier, elementValidator);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineList(List<String> path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
            return this.defineList(path, (Supplier<List<? extends T>>) (() -> defaultValue), elementValidator);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineList(final List<String> path, Supplier<List<? extends T>> defaultSupplier, final Predicate<Object> elementValidator) {
            this.context.setClazz(List.class);
            return this.define(path, new ForgeConfigSpec.ValueSpec(defaultSupplier, x -> x instanceof List && ((List) x).stream().allMatch(elementValidator), this.context, path) {

                @Override
                public Object correct(Object value) {
                    if (value != null && value instanceof List && !((List) value).isEmpty()) {
                        List<?> list = new ArrayList((List) value);
                        list.removeIf(elementValidator.negate());
                        if (list.isEmpty()) {
                            ForgeConfigSpec.LOGGER.debug(Logging.CORE, "List on key {} is deemed to need correction. It failed validation.", path.get(path.size() - 1));
                            return this.getDefault();
                        } else {
                            return list;
                        }
                    } else {
                        ForgeConfigSpec.LOGGER.debug(Logging.CORE, "List on key {} is deemed to need correction. It is null, not a list, or an empty list. Modders, consider defineListAllowEmpty?", path.get(path.size() - 1));
                        return this.getDefault();
                    }
                }
            }, defaultSupplier);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineListAllowEmpty(String path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
            return this.defineListAllowEmpty(ForgeConfigSpec.split(path), defaultValue, elementValidator);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineListAllowEmpty(String path, Supplier<List<? extends T>> defaultSupplier, Predicate<Object> elementValidator) {
            return this.defineListAllowEmpty(ForgeConfigSpec.split(path), defaultSupplier, elementValidator);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineListAllowEmpty(List<String> path, List<? extends T> defaultValue, Predicate<Object> elementValidator) {
            return this.defineListAllowEmpty(path, (Supplier<List<? extends T>>) (() -> defaultValue), elementValidator);
        }

        public <T> ForgeConfigSpec.ConfigValue<List<? extends T>> defineListAllowEmpty(final List<String> path, Supplier<List<? extends T>> defaultSupplier, final Predicate<Object> elementValidator) {
            this.context.setClazz(List.class);
            return this.define(path, new ForgeConfigSpec.ValueSpec(defaultSupplier, x -> x instanceof List && ((List) x).stream().allMatch(elementValidator), this.context, path) {

                @Override
                public Object correct(Object value) {
                    if (value != null && value instanceof List) {
                        List<?> list = new ArrayList((List) value);
                        list.removeIf(elementValidator.negate());
                        if (list.isEmpty()) {
                            ForgeConfigSpec.LOGGER.debug(Logging.CORE, "List on key {} is deemed to need correction. It failed validation.", path.get(path.size() - 1));
                            return this.getDefault();
                        } else {
                            return list;
                        }
                    } else {
                        ForgeConfigSpec.LOGGER.debug(Logging.CORE, "List on key {} is deemed to need correction, as it is null or not a list.", path.get(path.size() - 1));
                        return this.getDefault();
                    }
                }
            }, defaultSupplier);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, converter);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue) {
            return this.defineEnum(path, defaultValue, (V[]) defaultValue.getDeclaringClass().getEnumConstants());
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter) {
            return this.defineEnum(path, defaultValue, converter, (V[]) defaultValue.getDeclaringClass().getEnumConstants());
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, V... acceptableValues) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, acceptableValues);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter, V... acceptableValues) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, converter, acceptableValues);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, V... acceptableValues) {
            return this.defineEnum(path, defaultValue, Arrays.asList(acceptableValues));
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter, V... acceptableValues) {
            return this.defineEnum(path, defaultValue, converter, Arrays.asList(acceptableValues));
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, Collection<V> acceptableValues) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, acceptableValues);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter, Collection<V> acceptableValues) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, converter, acceptableValues);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, Collection<V> acceptableValues) {
            return this.defineEnum(path, defaultValue, EnumGetMethod.NAME_IGNORECASE, acceptableValues);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter, Collection<V> acceptableValues) {
            return this.defineEnum(path, defaultValue, converter, (Predicate<Object>) (obj -> {
                if (obj instanceof Enum) {
                    return acceptableValues.contains(obj);
                } else if (obj == null) {
                    return false;
                } else {
                    try {
                        return acceptableValues.contains(converter.get(obj, defaultValue.getDeclaringClass()));
                    } catch (ClassCastException | IllegalArgumentException var5) {
                        return false;
                    }
                }
            }));
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, Predicate<Object> validator) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, validator);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, V defaultValue, EnumGetMethod converter, Predicate<Object> validator) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultValue, converter, validator);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, Predicate<Object> validator) {
            return this.defineEnum(path, () -> defaultValue, validator, defaultValue.getDeclaringClass());
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, V defaultValue, EnumGetMethod converter, Predicate<Object> validator) {
            return this.defineEnum(path, () -> defaultValue, converter, validator, defaultValue.getDeclaringClass());
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, Supplier<V> defaultSupplier, Predicate<Object> validator, Class<V> clazz) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultSupplier, validator, clazz);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(String path, Supplier<V> defaultSupplier, EnumGetMethod converter, Predicate<Object> validator, Class<V> clazz) {
            return this.defineEnum(ForgeConfigSpec.split(path), defaultSupplier, converter, validator, clazz);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, Supplier<V> defaultSupplier, Predicate<Object> validator, Class<V> clazz) {
            return this.defineEnum(path, defaultSupplier, EnumGetMethod.NAME_IGNORECASE, validator, clazz);
        }

        public <V extends Enum<V>> ForgeConfigSpec.EnumValue<V> defineEnum(List<String> path, Supplier<V> defaultSupplier, EnumGetMethod converter, Predicate<Object> validator, Class<V> clazz) {
            this.context.setClazz(clazz);
            V[] allowedValues = (V[]) clazz.getEnumConstants();
            this.comment("Allowed Values: " + (String) Arrays.stream(allowedValues).filter(validator).map(Enum::name).collect(Collectors.joining(", ")));
            return new ForgeConfigSpec.EnumValue<>(this, this.define(path, new ForgeConfigSpec.ValueSpec(defaultSupplier, validator, this.context, path), defaultSupplier).getPath(), defaultSupplier, converter, clazz);
        }

        public ForgeConfigSpec.BooleanValue define(String path, boolean defaultValue) {
            return this.define(ForgeConfigSpec.split(path), defaultValue);
        }

        public ForgeConfigSpec.BooleanValue define(List<String> path, boolean defaultValue) {
            return this.define(path, (Supplier<Boolean>) (() -> defaultValue));
        }

        public ForgeConfigSpec.BooleanValue define(String path, Supplier<Boolean> defaultSupplier) {
            return this.define(ForgeConfigSpec.split(path), defaultSupplier);
        }

        public ForgeConfigSpec.BooleanValue define(List<String> path, Supplier<Boolean> defaultSupplier) {
            return new ForgeConfigSpec.BooleanValue(this, this.define(path, defaultSupplier, o -> !(o instanceof String) ? o instanceof Boolean : ((String) o).equalsIgnoreCase("true") || ((String) o).equalsIgnoreCase("false"), Boolean.class).getPath(), defaultSupplier);
        }

        public ForgeConfigSpec.DoubleValue defineInRange(String path, double defaultValue, double min, double max) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultValue, min, max);
        }

        public ForgeConfigSpec.DoubleValue defineInRange(List<String> path, double defaultValue, double min, double max) {
            return this.defineInRange(path, () -> defaultValue, min, max);
        }

        public ForgeConfigSpec.DoubleValue defineInRange(String path, Supplier<Double> defaultSupplier, double min, double max) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultSupplier, min, max);
        }

        public ForgeConfigSpec.DoubleValue defineInRange(List<String> path, Supplier<Double> defaultSupplier, double min, double max) {
            return new ForgeConfigSpec.DoubleValue(this, this.defineInRange(path, defaultSupplier, min, max, Double.class).getPath(), defaultSupplier);
        }

        public ForgeConfigSpec.IntValue defineInRange(String path, int defaultValue, int min, int max) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultValue, min, max);
        }

        public ForgeConfigSpec.IntValue defineInRange(List<String> path, int defaultValue, int min, int max) {
            return this.defineInRange(path, () -> defaultValue, min, max);
        }

        public ForgeConfigSpec.IntValue defineInRange(String path, Supplier<Integer> defaultSupplier, int min, int max) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultSupplier, min, max);
        }

        public ForgeConfigSpec.IntValue defineInRange(List<String> path, Supplier<Integer> defaultSupplier, int min, int max) {
            return new ForgeConfigSpec.IntValue(this, this.defineInRange(path, defaultSupplier, min, max, Integer.class).getPath(), defaultSupplier);
        }

        public ForgeConfigSpec.LongValue defineInRange(String path, long defaultValue, long min, long max) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultValue, min, max);
        }

        public ForgeConfigSpec.LongValue defineInRange(List<String> path, long defaultValue, long min, long max) {
            return this.defineInRange(path, () -> defaultValue, min, max);
        }

        public ForgeConfigSpec.LongValue defineInRange(String path, Supplier<Long> defaultSupplier, long min, long max) {
            return this.defineInRange(ForgeConfigSpec.split(path), defaultSupplier, min, max);
        }

        public ForgeConfigSpec.LongValue defineInRange(List<String> path, Supplier<Long> defaultSupplier, long min, long max) {
            return new ForgeConfigSpec.LongValue(this, this.defineInRange(path, defaultSupplier, min, max, Long.class).getPath(), defaultSupplier);
        }

        public ForgeConfigSpec.Builder comment(String comment) {
            this.context.addComment(comment);
            return this;
        }

        public ForgeConfigSpec.Builder comment(String... comment) {
            for (int i = 0; i < comment.length; i++) {
                Preconditions.checkNotNull(comment[i], "Comment string at " + i + " is null.");
            }
            for (String s : comment) {
                this.context.addComment(s);
            }
            return this;
        }

        public ForgeConfigSpec.Builder translation(String translationKey) {
            this.context.setTranslationKey(translationKey);
            return this;
        }

        public ForgeConfigSpec.Builder worldRestart() {
            this.context.worldRestart();
            return this;
        }

        public ForgeConfigSpec.Builder push(String path) {
            return this.push(ForgeConfigSpec.split(path));
        }

        public ForgeConfigSpec.Builder push(List<String> path) {
            this.currentPath.addAll(path);
            if (this.context.hasComment()) {
                this.levelComments.put(new ArrayList(this.currentPath), this.context.buildComment(path));
                this.context.clearComment();
            }
            if (this.context.getTranslationKey() != null) {
                this.levelTranslationKeys.put(new ArrayList(this.currentPath), this.context.getTranslationKey());
                this.context.setTranslationKey(null);
            }
            this.context.ensureEmpty();
            return this;
        }

        public ForgeConfigSpec.Builder pop() {
            return this.pop(1);
        }

        public ForgeConfigSpec.Builder pop(int count) {
            if (count > this.currentPath.size()) {
                throw new IllegalArgumentException("Attempted to pop " + count + " elements when we only had: " + this.currentPath);
            } else {
                for (int x = 0; x < count; x++) {
                    this.currentPath.remove(this.currentPath.size() - 1);
                }
                return this;
            }
        }

        public <T> Pair<T, ForgeConfigSpec> configure(Function<ForgeConfigSpec.Builder, T> consumer) {
            T o = (T) consumer.apply(this);
            return Pair.of(o, this.build());
        }

        public ForgeConfigSpec build() {
            this.context.ensureEmpty();
            Config valueCfg = Config.of(Config.getDefaultMapCreator(true, true), InMemoryFormat.withSupport(ForgeConfigSpec.ConfigValue.class::isAssignableFrom));
            this.values.forEach(v -> valueCfg.set(v.getPath(), v));
            ForgeConfigSpec ret = new ForgeConfigSpec(this.storage, valueCfg, this.levelComments, this.levelTranslationKeys);
            this.values.forEach(v -> v.spec = ret);
            return ret;
        }

        public interface BuilderConsumer {

            void accept(ForgeConfigSpec.Builder var1);
        }
    }

    private static class BuilderContext {

        private final List<String> comment = new LinkedList();

        private String langKey;

        private ForgeConfigSpec.Range<?> range;

        private boolean worldRestart = false;

        private Class<?> clazz;

        public void addComment(String value) {
            Preconditions.checkNotNull(value, "Passed in null value for comment");
            this.comment.add(value);
        }

        public void clearComment() {
            this.comment.clear();
        }

        public boolean hasComment() {
            return this.comment.size() > 0;
        }

        public String buildComment() {
            return this.buildComment(List.of("unknown", "unknown"));
        }

        public String buildComment(List<String> path) {
            if (this.comment.stream().allMatch(String::isBlank)) {
                if (FMLEnvironment.production) {
                    ForgeConfigSpec.LOGGER.warn(Logging.CORE, "Detected a comment that is all whitespace for config option {}, which causes obscure bugs in Forge's config system and will cause a crash in the future. Please report this to the mod author.", ForgeConfigSpec.DOT_JOINER.join(path));
                    return "A developer of this mod has defined this config option with a blank comment, which causes obscure bugs in Forge's config system and will cause a crash in the future. Please report this to the mod author.";
                } else {
                    throw new IllegalStateException("Can not build comment for config option " + ForgeConfigSpec.DOT_JOINER.join(path) + " as it comprises entirely of blank lines/whitespace. This is not allowed as it causes a \"constantly correcting config\" bug with NightConfig in Forge's config system.");
                }
            } else {
                return ForgeConfigSpec.LINE_JOINER.join(this.comment);
            }
        }

        public void setTranslationKey(String value) {
            this.langKey = value;
        }

        public String getTranslationKey() {
            return this.langKey;
        }

        public <V extends Comparable<? super V>> void setRange(ForgeConfigSpec.Range<V> value) {
            this.range = value;
            this.setClazz(value.getClazz());
        }

        public <V extends Comparable<? super V>> ForgeConfigSpec.Range<V> getRange() {
            return (ForgeConfigSpec.Range<V>) this.range;
        }

        public void worldRestart() {
            this.worldRestart = true;
        }

        public boolean needsWorldRestart() {
            return this.worldRestart;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getClazz() {
            return this.clazz;
        }

        public void ensureEmpty() {
            this.validate(this.hasComment(), "Non-empty comment when empty expected");
            this.validate(this.langKey, "Non-null translation key when null expected");
            this.validate(this.range, "Non-null range when null expected");
            this.validate(this.worldRestart, "Dangeling world restart value set to true");
        }

        private void validate(Object value, String message) {
            if (value != null) {
                throw new IllegalStateException(message);
            }
        }

        private void validate(boolean value, String message) {
            if (value) {
                throw new IllegalStateException(message);
            }
        }
    }

    public static class ConfigValue<T> implements Supplier<T> {

        private static boolean USE_CACHES = true;

        private final ForgeConfigSpec.Builder parent;

        private final List<String> path;

        private final Supplier<T> defaultSupplier;

        private T cachedValue = (T) null;

        private ForgeConfigSpec spec;

        ConfigValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<T> defaultSupplier) {
            this.parent = parent;
            this.path = path;
            this.defaultSupplier = defaultSupplier;
            this.parent.values.add(this);
        }

        public List<String> getPath() {
            return new ArrayList(this.path);
        }

        public T get() {
            Preconditions.checkNotNull(this.spec, "Cannot get config value before spec is built");
            if (!FMLEnvironment.production) {
                Preconditions.checkState(this.spec.childConfig != null, "Cannot get config value before config is loaded.\nThis error is currently only thrown in the development environment, to avoid breaking published mods.\nIn a future version, this will also throw in the production environment.\n");
            }
            if (this.spec.childConfig == null) {
                return (T) this.defaultSupplier.get();
            } else {
                if (USE_CACHES && this.cachedValue == null) {
                    this.cachedValue = this.getRaw(this.spec.childConfig, this.path, this.defaultSupplier);
                } else if (!USE_CACHES) {
                    return this.getRaw(this.spec.childConfig, this.path, this.defaultSupplier);
                }
                return this.cachedValue;
            }
        }

        protected T getRaw(Config config, List<String> path, Supplier<T> defaultSupplier) {
            return (T) config.getOrElse(path, defaultSupplier);
        }

        public T getDefault() {
            return (T) this.defaultSupplier.get();
        }

        public ForgeConfigSpec.Builder next() {
            return this.parent;
        }

        public void save() {
            Preconditions.checkNotNull(this.spec, "Cannot save config value before spec is built");
            Preconditions.checkNotNull(this.spec.childConfig, "Cannot save config value without assigned Config object present");
            this.spec.save();
        }

        public void set(T value) {
            Preconditions.checkNotNull(this.spec, "Cannot set config value before spec is built");
            Preconditions.checkNotNull(this.spec.childConfig, "Cannot set config value without assigned Config object present");
            this.spec.childConfig.set(this.path, value);
            this.cachedValue = value;
        }

        public void clearCache() {
            this.cachedValue = null;
        }
    }

    public static class DoubleValue extends ForgeConfigSpec.ConfigValue<Double> {

        DoubleValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<Double> defaultSupplier) {
            super(parent, path, defaultSupplier);
        }

        protected Double getRaw(Config config, List<String> path, Supplier<Double> defaultSupplier) {
            Number n = (Number) config.get(path);
            return n == null ? (Double) defaultSupplier.get() : n.doubleValue();
        }
    }

    public static class EnumValue<T extends Enum<T>> extends ForgeConfigSpec.ConfigValue<T> {

        private final EnumGetMethod converter;

        private final Class<T> clazz;

        EnumValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<T> defaultSupplier, EnumGetMethod converter, Class<T> clazz) {
            super(parent, path, defaultSupplier);
            this.converter = converter;
            this.clazz = clazz;
        }

        protected T getRaw(Config config, List<String> path, Supplier<T> defaultSupplier) {
            return (T) config.getEnumOrElse(path, this.clazz, this.converter, defaultSupplier);
        }
    }

    public static class IntValue extends ForgeConfigSpec.ConfigValue<Integer> {

        IntValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<Integer> defaultSupplier) {
            super(parent, path, defaultSupplier);
        }

        protected Integer getRaw(Config config, List<String> path, Supplier<Integer> defaultSupplier) {
            return config.getIntOrElse(path, () -> (Integer) defaultSupplier.get());
        }
    }

    public static class LongValue extends ForgeConfigSpec.ConfigValue<Long> {

        LongValue(ForgeConfigSpec.Builder parent, List<String> path, Supplier<Long> defaultSupplier) {
            super(parent, path, defaultSupplier);
        }

        protected Long getRaw(Config config, List<String> path, Supplier<Long> defaultSupplier) {
            return config.getLongOrElse(path, () -> (Long) defaultSupplier.get());
        }
    }

    public static class Range<V extends Comparable<? super V>> implements Predicate<Object> {

        private final Class<? extends V> clazz;

        private final V min;

        private final V max;

        private Range(Class<V> clazz, V min, V max) {
            this.clazz = clazz;
            this.min = min;
            this.max = max;
        }

        public Class<? extends V> getClazz() {
            return this.clazz;
        }

        public V getMin() {
            return this.min;
        }

        public V getMax() {
            return this.max;
        }

        private boolean isNumber(Object other) {
            return Number.class.isAssignableFrom(this.clazz) && other instanceof Number;
        }

        public boolean test(Object t) {
            if (this.isNumber(t)) {
                Number n = (Number) t;
                boolean result = ((Number) this.min).doubleValue() <= n.doubleValue() && n.doubleValue() <= ((Number) this.max).doubleValue();
                if (!result) {
                    ForgeConfigSpec.LOGGER.debug(Logging.CORE, "Range value {} is not within its bounds {}-{}", n.doubleValue(), ((Number) this.min).doubleValue(), ((Number) this.max).doubleValue());
                }
                return result;
            } else if (!this.clazz.isInstance(t)) {
                return false;
            } else {
                V c = (V) this.clazz.cast(t);
                boolean result = c.compareTo(this.min) >= 0 && c.compareTo(this.max) <= 0;
                if (!result) {
                    ForgeConfigSpec.LOGGER.debug(Logging.CORE, "Range value {} is not within its bounds {}-{}", c, this.min, this.max);
                }
                return result;
            }
        }

        public Object correct(Object value, Object def) {
            if (this.isNumber(value)) {
                Number n = (Number) value;
                return n.doubleValue() < ((Number) this.min).doubleValue() ? this.min : (n.doubleValue() > ((Number) this.max).doubleValue() ? this.max : value);
            } else if (!this.clazz.isInstance(value)) {
                return def;
            } else {
                V c = (V) this.clazz.cast(value);
                return c.compareTo(this.min) < 0 ? this.min : (c.compareTo(this.max) > 0 ? this.max : value);
            }
        }

        public String toString() {
            if (this.clazz == Integer.class) {
                if (this.max.equals(Integer.MAX_VALUE)) {
                    return "> " + this.min;
                }
                if (this.min.equals(Integer.MIN_VALUE)) {
                    return "< " + this.max;
                }
            }
            return this.min + " ~ " + this.max;
        }
    }

    public static class ValueSpec {

        private final String comment;

        private final String langKey;

        private final ForgeConfigSpec.Range<?> range;

        private final boolean worldRestart;

        private final Class<?> clazz;

        private final Supplier<?> supplier;

        private final Predicate<Object> validator;

        private ValueSpec(Supplier<?> supplier, Predicate<Object> validator, ForgeConfigSpec.BuilderContext context, List<String> path) {
            Objects.requireNonNull(supplier, "Default supplier can not be null");
            Objects.requireNonNull(validator, "Validator can not be null");
            this.comment = context.hasComment() ? context.buildComment(path) : null;
            this.langKey = context.getTranslationKey();
            this.range = context.getRange();
            this.worldRestart = context.needsWorldRestart();
            this.clazz = context.getClazz();
            this.supplier = supplier;
            this.validator = validator;
        }

        public String getComment() {
            return this.comment;
        }

        public String getTranslationKey() {
            return this.langKey;
        }

        public <V extends Comparable<? super V>> ForgeConfigSpec.Range<V> getRange() {
            return (ForgeConfigSpec.Range<V>) this.range;
        }

        public boolean needsWorldRestart() {
            return this.worldRestart;
        }

        public Class<?> getClazz() {
            return this.clazz;
        }

        public boolean test(Object value) {
            return this.validator.test(value);
        }

        public Object correct(Object value) {
            return this.range == null ? this.getDefault() : this.range.correct(value, this.getDefault());
        }

        public Object getDefault() {
            return this.supplier.get();
        }
    }
}