package de.keksuccino.fancymenu.util.properties;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PropertyContainer {

    @NotNull
    private String type;

    @NotNull
    private final Map<String, String> entries = new LinkedHashMap();

    public PropertyContainer(@NotNull String type) {
        this.type = (String) Objects.requireNonNull(type);
    }

    public void putProperty(@NotNull String name, @Nullable String value) {
        if (value == null) {
            this.removeProperty(name);
        } else {
            this.entries.put((String) Objects.requireNonNull(name), value);
        }
    }

    @NotNull
    public Map<String, String> getProperties() {
        return this.entries;
    }

    @Nullable
    public String getValue(@NotNull String name) {
        return (String) this.entries.get(Objects.requireNonNull(name));
    }

    public void removeProperty(@NotNull String name) {
        this.entries.remove(Objects.requireNonNull(name));
    }

    public boolean hasProperty(@NotNull String name) {
        return this.entries.containsKey(Objects.requireNonNull(name));
    }

    @NotNull
    public String getType() {
        return this.type;
    }

    public void setType(@NotNull String type) {
        this.type = (String) Objects.requireNonNull(type);
    }

    public String toString() {
        return "PropertyContainer{type='" + this.type + "', entries=" + this.entries + "}";
    }
}