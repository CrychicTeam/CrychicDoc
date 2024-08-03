package com.simibubi.create.compat.computercraft.implementation;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaTable;
import dan200.computercraft.api.lua.LuaValues;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateLuaTable implements LuaTable<Object, Object> {

    private final Map<Object, Object> map;

    public CreateLuaTable() {
        this.map = new HashMap();
    }

    public CreateLuaTable(Map<?, ?> map) {
        this.map = new HashMap(map);
    }

    public boolean getBoolean(String key) throws LuaException {
        Object value = this.get(key);
        if (!(value instanceof Boolean)) {
            throw LuaValues.badField(key, "boolean", LuaValues.getType(value));
        } else {
            return (Boolean) value;
        }
    }

    public String getString(String key) throws LuaException {
        Object value = this.get(key);
        if (!(value instanceof String)) {
            throw LuaValues.badField(key, "string", LuaValues.getType(value));
        } else {
            return (String) value;
        }
    }

    public CreateLuaTable getTable(String key) throws LuaException {
        Object value = this.get(key);
        if (!(value instanceof Map)) {
            throw LuaValues.badField(key, "table", LuaValues.getType(value));
        } else {
            return new CreateLuaTable((Map<?, ?>) value);
        }
    }

    public Optional<Boolean> getOptBoolean(String key) throws LuaException {
        Object value = this.get(key);
        if (value == null) {
            return Optional.empty();
        } else if (!(value instanceof Boolean)) {
            throw LuaValues.badField(key, "boolean", LuaValues.getType(value));
        } else {
            return Optional.of((Boolean) value);
        }
    }

    public Set<String> stringKeySet() throws LuaException {
        Set<String> stringSet = new HashSet();
        for (Object key : this.keySet()) {
            if (!(key instanceof String)) {
                throw new LuaException("key " + key + " is not string (got " + LuaValues.getType(key) + ")");
            }
            stringSet.add((String) key);
        }
        return Collections.unmodifiableSet(stringSet);
    }

    public Collection<CreateLuaTable> tableValues() throws LuaException {
        List<CreateLuaTable> tables = new ArrayList();
        for (int i = 1; i <= this.size(); i++) {
            Object value = this.get((double) i);
            if (!(value instanceof Map)) {
                throw new LuaException("value " + value + " is not table (got " + LuaValues.getType(value) + ")");
            }
            tables.add(new CreateLuaTable((Map<?, ?>) value));
        }
        return Collections.unmodifiableList(tables);
    }

    public Map<Object, Object> getMap() {
        return this.map;
    }

    @Nullable
    public Object put(Object key, Object value) {
        return this.map.put(key, value);
    }

    public void putBoolean(String key, boolean value) {
        this.map.put(key, value);
    }

    public void putDouble(String key, double value) {
        this.map.put(key, value);
    }

    public void putString(String key, String value) {
        this.map.put(key, value);
    }

    public void putTable(String key, CreateLuaTable value) {
        this.map.put(key, value);
    }

    public void putTable(int i, CreateLuaTable value) {
        this.map.put(i, value);
    }

    public int size() {
        return this.map.size();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public boolean containsKey(Object o) {
        return this.map.containsKey(o);
    }

    public boolean containsValue(Object o) {
        return this.map.containsValue(o);
    }

    public Object get(Object o) {
        return this.map.get(o);
    }

    @NotNull
    public Set<Object> keySet() {
        return this.map.keySet();
    }

    @NotNull
    public Collection<Object> values() {
        return this.map.values();
    }

    @NotNull
    public Set<Entry<Object, Object>> entrySet() {
        return this.map.entrySet();
    }
}