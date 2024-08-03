package me.shedaniel.autoconfig.util;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import me.shedaniel.autoconfig.util.forge.UtilsImpl;

public class Utils {

    private Utils() {
    }

    @ExpectPlatform
    @Transformed
    public static Path getConfigFolder() {
        return UtilsImpl.getConfigFolder();
    }

    public static <V> V constructUnsafely(Class<V> cls) {
        try {
            Constructor<V> constructor = cls.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (V) constructor.newInstance();
        } catch (ReflectiveOperationException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static <V> V getUnsafely(Field field, Object obj) {
        if (obj == null) {
            return null;
        } else {
            try {
                field.setAccessible(true);
                return (V) field.get(obj);
            } catch (ReflectiveOperationException var3) {
                throw new RuntimeException(var3);
            }
        }
    }

    public static <V> V getUnsafely(Field field, Object obj, V defaultValue) {
        V ret = getUnsafely(field, obj);
        if (ret == null) {
            ret = defaultValue;
        }
        return ret;
    }

    public static void setUnsafely(Field field, Object obj, Object newValue) {
        if (obj != null) {
            try {
                field.setAccessible(true);
                field.set(obj, newValue);
            } catch (ReflectiveOperationException var4) {
                throw new RuntimeException(var4);
            }
        }
    }

    public static <T, K, U> Collector<T, ?, Map<K, U>> toLinkedMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends U> valueMapper) {
        return Collectors.toMap(keyMapper, valueMapper, (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, LinkedHashMap::new);
    }
}