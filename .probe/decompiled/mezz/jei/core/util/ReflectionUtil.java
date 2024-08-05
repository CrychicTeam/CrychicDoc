package mezz.jei.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.core.collect.Table;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ReflectionUtil {

    private static final Logger LOGGER = LogManager.getLogger();

    private final Table<Class<?>, Class<?>, List<Field>> cache = Table.hashBasedTable();

    public <T> Stream<T> getFieldWithClass(Object object, Class<? extends T> fieldClass) {
        return this.getFieldsCached(object, fieldClass).flatMap(field -> getFieldValue(object, field, fieldClass).stream());
    }

    private static <T> Optional<T> getFieldValue(Object object, Field field, Class<? extends T> fieldClass) {
        Object fieldValue;
        try {
            fieldValue = field.get(object);
        } catch (IllegalAccessException var5) {
            LOGGER.error("Failed to access field '" + field.getName() + "' for class " + object.getClass(), var5);
            return Optional.empty();
        }
        if (fieldClass.isInstance(fieldValue)) {
            T cast = (T) fieldClass.cast(fieldValue);
            return Optional.of(cast);
        } else {
            return Optional.empty();
        }
    }

    private Stream<Field> getFieldsCached(Object object, Class<?> fieldClass) {
        return this.cache.computeIfAbsent(fieldClass, object.getClass(), () -> getFieldUncached(object, fieldClass).toList()).stream();
    }

    private static Stream<Field> getFieldUncached(Object object, Class<?> fieldClass) {
        return getAllFields(object).filter(field -> fieldClass.isAssignableFrom(field.getType())).mapMulti((field, mapper) -> {
            try {
                field.setAccessible(true);
                mapper.accept(field);
            } catch (SecurityException | InaccessibleObjectException var4) {
                LOGGER.error("Failed to access field '" + field.getName() + "' for class " + object.getClass(), var4);
            }
        });
    }

    private static Stream<Field> getAllFields(Object object) {
        Class<?> objectClass = object.getClass();
        List<Class<?>> classes;
        for (classes = new ArrayList(); objectClass != Object.class; objectClass = objectClass.getSuperclass()) {
            classes.add(objectClass);
        }
        return classes.stream().flatMap(c -> {
            try {
                Field[] fields = c.getDeclaredFields();
                return Arrays.stream(fields);
            } catch (SecurityException var3) {
                LOGGER.error("Failed to access fields for class " + object.getClass(), var3);
                return Stream.of();
            }
        });
    }
}