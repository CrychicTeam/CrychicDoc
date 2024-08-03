package yesman.epicfight.api.utils;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;

public class TypeFlexibleHashMap<A extends TypeFlexibleHashMap.TypeKey<?>> extends HashMap<A, Object> {

    final boolean immutable;

    public TypeFlexibleHashMap(boolean immutable) {
        this.immutable = immutable;
    }

    public <T> T put(TypeFlexibleHashMap.TypeKey<T> typeKey, T val) {
        if (this.immutable) {
            throw new UnsupportedOperationException();
        } else {
            return (T) super.put(typeKey, val);
        }
    }

    public <T> T get(TypeFlexibleHashMap.TypeKey<T> typeKey) {
        ImmutableMap.of();
        return (T) super.get(typeKey);
    }

    public <T> T getOrDefault(TypeFlexibleHashMap.TypeKey<T> typeKey) {
        return (T) super.getOrDefault(typeKey, typeKey.defaultValue());
    }

    public interface TypeKey<T> {

        T defaultValue();
    }
}