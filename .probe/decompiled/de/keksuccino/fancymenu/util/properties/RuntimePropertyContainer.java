package de.keksuccino.fancymenu.util.properties;

import java.util.LinkedHashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RuntimePropertyContainer {

    protected final Map<String, RuntimePropertyContainer.RuntimeProperty<?>> properties = new LinkedHashMap();

    public <T> RuntimePropertyContainer putProperty(String key, T value) {
        this.properties.put(key, new RuntimePropertyContainer.RuntimeProperty(value));
        return this;
    }

    public <T> RuntimePropertyContainer putPropertyIfAbsent(String key, T value) {
        if (!this.hasProperty(key)) {
            this.putProperty(key, value);
        }
        return this;
    }

    public <T> T putPropertyIfAbsentAndGet(String key, T value) {
        this.putPropertyIfAbsent(key, value);
        return this.getProperty(key, value.getClass());
    }

    @Nullable
    public Boolean getBooleanProperty(@NotNull String key) {
        return this.getProperty(key, Boolean.class);
    }

    @Nullable
    public String getStringProperty(@NotNull String key) {
        return this.getProperty(key, String.class);
    }

    @Nullable
    public Integer getIntegerProperty(@NotNull String key) {
        return this.getProperty(key, Integer.class);
    }

    @Nullable
    public <T> T getProperty(@NotNull String key, @NotNull Class<? extends T> propertyType) {
        RuntimePropertyContainer.RuntimeProperty<?> p = (RuntimePropertyContainer.RuntimeProperty<?>) this.properties.get(key);
        try {
            if (p != null) {
                return (T) p.value;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }
        return null;
    }

    public boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }

    public RuntimePropertyContainer removeProperty(@NotNull String key) {
        this.properties.remove(key);
        return this;
    }

    public void clear() {
        this.properties.clear();
    }

    public static class RuntimeProperty<T> {

        public T value;

        public RuntimeProperty(T value) {
            this.value = value;
        }
    }
}