package fuzs.puzzleslib.impl.config;

import com.google.common.base.CaseFormat;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.ObjectArrays;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.Predicate;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnnotatedConfigBuilder {

    public static <T extends ConfigCore> void serialize(ForgeConfigSpec.Builder builder, ConfigDataHolderImpl<?> context, @NotNull T target) {
        serialize(builder, context, target.getClass(), target);
    }

    public static <T extends ConfigCore> void serialize(ForgeConfigSpec.Builder builder, ConfigDataHolderImpl<?> context, Class<? extends T> target) {
        serialize(builder, context, target, null);
    }

    public static <T extends ConfigCore> void serialize(ForgeConfigSpec.Builder builder, ConfigDataHolderImpl<?> context, Class<? extends T> target, @Nullable T instance) {
        Map<List<String>, Collection<Field>> pathToFields = setupFields(target);
        for (Entry<List<String>, Collection<Field>> entry : pathToFields.entrySet()) {
            List<String> path = (List<String>) entry.getKey();
            if (!path.isEmpty()) {
                for (String category : path) {
                    builder.push(category);
                }
            }
            for (Field field : (Collection) entry.getValue()) {
                field.setAccessible(true);
                boolean isStatic = Modifier.isStatic(field.getModifiers());
                if (!isStatic) {
                    Objects.requireNonNull(instance, "Null instance for non-static field");
                }
                buildConfig(builder, context, isStatic ? null : instance, field, (Config) field.getDeclaredAnnotation(Config.class));
            }
            if (!path.isEmpty()) {
                builder.pop(path.size());
            }
        }
        if (instance != null) {
            instance.addToBuilder(builder, context);
            context.acceptValueCallback(instance::afterConfigReload);
        }
    }

    private static Map<List<String>, Collection<Field>> setupFields(Class<?> target) {
        Multimap<List<String>, Field> pathToField = HashMultimap.create();
        for (Field field : getAllFieldsRecursive(target)) {
            Config annotation = (Config) field.getDeclaredAnnotation(Config.class);
            if (annotation != null) {
                pathToField.put(Lists.newArrayList(annotation.category()), field);
            }
        }
        return pathToField.asMap();
    }

    private static List<Field> getAllFieldsRecursive(Class<?> clazz) {
        List<Field> list = new LinkedList();
        while (clazz != Object.class) {
            list.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    private static void buildConfig(ForgeConfigSpec.Builder builder, ConfigDataHolderImpl<?> context, @Nullable Object instance, Field field, Config annotation) {
        String name = annotation.name();
        if (StringUtils.isBlank(name)) {
            name = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, field.getName());
        }
        Class<?> type = field.getType();
        Object defaultValue;
        try {
            defaultValue = field.get(instance);
        } catch (IllegalAccessException var14) {
            throw new RuntimeException(var14);
        }
        String[] description = annotation.description();
        if (description.length != 0) {
            builder.comment(description);
        }
        if (ConfigCore.class.isAssignableFrom(type)) {
            builder.push(name);
            serialize(builder, context, type, (ConfigCore) defaultValue);
            builder.pop();
        } else if (Modifier.isFinal(field.getModifiers())) {
            throw new RuntimeException("Field may not be final");
        } else {
            if (annotation.worldRestart()) {
                builder.worldRestart();
            }
            if (type == boolean.class) {
                addCallback(context, builder.define(name, ((Boolean) defaultValue).booleanValue()), field, instance);
            } else if (type == int.class) {
                int min = Integer.MIN_VALUE;
                int max = Integer.MAX_VALUE;
                Config.IntRange intRange = (Config.IntRange) field.getDeclaredAnnotation(Config.IntRange.class);
                if (intRange != null) {
                    min = intRange.min();
                    max = intRange.max();
                }
                addCallback(context, builder.defineInRange(name, (Integer) defaultValue, min, max), field, instance);
            } else if (type == long.class) {
                long min = Long.MIN_VALUE;
                long max = Long.MAX_VALUE;
                Config.LongRange longRange = (Config.LongRange) field.getDeclaredAnnotation(Config.LongRange.class);
                if (longRange != null) {
                    min = longRange.min();
                    max = longRange.max();
                }
                addCallback(context, builder.defineInRange(name, (Long) defaultValue, min, max), field, instance);
            } else if (type == double.class) {
                double min = Double.MIN_VALUE;
                double max = Double.MAX_VALUE;
                Config.DoubleRange doubleRange = (Config.DoubleRange) field.getDeclaredAnnotation(Config.DoubleRange.class);
                if (doubleRange != null) {
                    min = doubleRange.min();
                    max = doubleRange.max();
                }
                addCallback(context, builder.defineInRange(name, (Double) defaultValue, min, max), field, instance);
            } else if (type == String.class) {
                Config.AllowedValues allowedValues = (Config.AllowedValues) field.getDeclaredAnnotation(Config.AllowedValues.class);
                if (allowedValues != null && allowedValues.values().length != 0) {
                    builder.comment((String[]) ObjectArrays.concat(description, String.format("Allowed Values: %s", String.join(", ", allowedValues.values()))));
                    addCallback(context, builder.define(name, (String) defaultValue, o -> testAllowedValues(allowedValues.values(), o)), field, instance);
                } else {
                    addCallback(context, builder.define(name, (String) defaultValue), field, instance);
                }
            } else if (type.isEnum()) {
                Config.AllowedValues allowedValues = (Config.AllowedValues) field.getDeclaredAnnotation(Config.AllowedValues.class);
                if (allowedValues != null && allowedValues.values().length != 0) {
                    addCallback(context, builder.defineEnum(name, (Enum) defaultValue, (Predicate<Object>) (o -> testAllowedValues(allowedValues.values(), o))), field, instance);
                } else {
                    addCallback(context, builder.defineEnum(name, (Enum) defaultValue), field, instance);
                }
            } else {
                if (type != List.class) {
                    throw new IllegalArgumentException(String.format("Unsupported config value type: %s", type));
                }
                Config.AllowedValues allowedValues = (Config.AllowedValues) field.getDeclaredAnnotation(Config.AllowedValues.class);
                if (allowedValues != null && allowedValues.values().length != 0) {
                    builder.comment((String[]) ObjectArrays.concat(description, String.format("Allowed Values: %s", String.join(", ", allowedValues.values()))));
                    addCallback(context, builder.defineList(name, (List) defaultValue, o -> testAllowedValues(allowedValues.values(), o)), field, instance);
                } else {
                    addCallback(context, builder.defineList(name, (List) defaultValue, o -> true), field, instance);
                }
            }
        }
    }

    private static boolean testAllowedValues(String[] allowedValues, @Nullable Object o) {
        if (o != null) {
            String value = o instanceof Enum ? ((Enum) o).name() : o.toString();
            for (String allowedValue : allowedValues) {
                if (allowedValue.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void addCallback(ValueCallback context, ForgeConfigSpec.ConfigValue<?> configValue, Field field, @Nullable Object instance) {
        context.accept(configValue, value -> {
            try {
                MethodHandles.lookup().unreflectSetter(field).invoke(instance, configValue.get());
            } catch (Throwable var5) {
                throw new RuntimeException(var5);
            }
        });
    }
}