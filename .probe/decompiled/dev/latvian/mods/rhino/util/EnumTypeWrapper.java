package dev.latvian.mods.rhino.util;

import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.util.wrap.TypeWrapperFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EnumTypeWrapper<T> implements TypeWrapperFactory<T> {

    private static final Map<Class<?>, EnumTypeWrapper<?>> WRAPPERS = new HashMap();

    public final Class<T> enumType;

    public final T[] indexValues;

    public final Map<String, T> nameValues;

    public final Map<T, String> valueNames;

    public static <T> EnumTypeWrapper<T> get(Class<T> enumType) {
        if (!enumType.isEnum()) {
            throw new IllegalArgumentException("Class " + enumType.getName() + " is not an enum!");
        } else {
            return (EnumTypeWrapper<T>) WRAPPERS.computeIfAbsent(enumType, EnumTypeWrapper::new);
        }
    }

    public static String getName(Class<?> enumType, Enum<?> e, boolean cache) {
        if (cache) {
            return (String) get((Class<T>) enumType).valueNames.getOrDefault(e, e.name());
        } else {
            String name = e.name();
            if (e instanceof RemappedEnumConstant c) {
                String s = c.getRemappedEnumConstantName();
                if (!s.isEmpty()) {
                    return s;
                }
            }
            return name;
        }
    }

    private EnumTypeWrapper(Class<T> enumType) {
        this.enumType = enumType;
        this.indexValues = (T[]) enumType.getEnumConstants();
        this.nameValues = new HashMap();
        this.valueNames = new HashMap();
        for (T t : this.indexValues) {
            String name = getName(enumType, (Enum<?>) t, false).toLowerCase();
            this.nameValues.put(name, t);
            this.valueNames.put(t, name);
        }
    }

    @Override
    public T wrap(Context cx, Object o) {
        if (o instanceof CharSequence) {
            String s = o.toString().toLowerCase();
            if (s.isEmpty()) {
                return null;
            } else {
                T t = (T) this.nameValues.get(s);
                if (t == null) {
                    throw new IllegalArgumentException("'" + s + "' is not a valid enum constant! Valid values are: " + (String) this.nameValues.keySet().stream().map(s1 -> "'" + s1 + "'").collect(Collectors.joining(", ")));
                } else {
                    return t;
                }
            }
        } else if (o instanceof Number) {
            int index = ((Number) o).intValue();
            if (index >= 0 && index < this.indexValues.length) {
                return this.indexValues[index];
            } else {
                throw new IllegalArgumentException(index + " is not a valid enum index! Valid values are: 0 - " + (this.indexValues.length - 1));
            }
        } else {
            return (T) o;
        }
    }
}