package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Toml {

    private static final Gson DEFAULT_GSON = new Gson();

    private Map<String, Object> values = new HashMap();

    private final Toml defaults;

    public Toml() {
        this(null);
    }

    public Toml(Toml defaults) {
        this(defaults, new HashMap());
    }

    public Toml read(File file) {
        try {
            return this.read(new InputStreamReader(new FileInputStream(file), "UTF8"));
        } catch (Exception var3) {
            throw new RuntimeException(var3);
        }
    }

    public Toml read(InputStream inputStream) {
        return this.read(new InputStreamReader(inputStream));
    }

    public Toml read(Reader reader) {
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(reader);
            StringBuilder w = new StringBuilder();
            for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                w.append(line).append('\n');
            }
            this.read(w.toString());
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException var11) {
            }
        }
        return this;
    }

    public Toml read(Toml otherToml) {
        this.values = otherToml.values;
        return this;
    }

    public Toml read(String tomlString) throws IllegalStateException {
        Results results = TomlParser.run(tomlString);
        if (results.errors.hasErrors()) {
            throw new IllegalStateException(results.errors.toString());
        } else {
            this.values = results.consume();
            return this;
        }
    }

    public String getString(String key) {
        return (String) this.get(key);
    }

    public String getString(String key, String defaultValue) {
        String val = this.getString(key);
        return val == null ? defaultValue : val;
    }

    public Long getLong(String key) {
        return (Long) this.get(key);
    }

    public Long getLong(String key, Long defaultValue) {
        Long val = this.getLong(key);
        return val == null ? defaultValue : val;
    }

    public <T> List<T> getList(String key) {
        return (List<T>) this.get(key);
    }

    public <T> List<T> getList(String key, List<T> defaultValue) {
        List<T> list = this.getList(key);
        return list != null ? list : defaultValue;
    }

    public Boolean getBoolean(String key) {
        return (Boolean) this.get(key);
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        Boolean val = this.getBoolean(key);
        return val == null ? defaultValue : val;
    }

    public Date getDate(String key) {
        return (Date) this.get(key);
    }

    public Date getDate(String key, Date defaultValue) {
        Date val = this.getDate(key);
        return val == null ? defaultValue : val;
    }

    public Double getDouble(String key) {
        return (Double) this.get(key);
    }

    public Double getDouble(String key, Double defaultValue) {
        Double val = this.getDouble(key);
        return val == null ? defaultValue : val;
    }

    public Toml getTable(String key) {
        Map<String, Object> map = (Map<String, Object>) this.get(key);
        return map != null ? new Toml(null, map) : null;
    }

    public List<Toml> getTables(String key) {
        List<Map<String, Object>> tableArray = (List<Map<String, Object>>) this.get(key);
        if (tableArray == null) {
            return null;
        } else {
            ArrayList<Toml> tables = new ArrayList();
            for (Map<String, Object> table : tableArray) {
                tables.add(new Toml(null, table));
            }
            return tables;
        }
    }

    public boolean contains(String key) {
        return this.get(key) != null;
    }

    public boolean containsPrimitive(String key) {
        Object object = this.get(key);
        return object != null && !(object instanceof Map) && !(object instanceof List);
    }

    public boolean containsTable(String key) {
        Object object = this.get(key);
        return object != null && object instanceof Map;
    }

    public boolean containsTableArray(String key) {
        Object object = this.get(key);
        return object != null && object instanceof List;
    }

    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    public <T> T to(Class<T> targetClass) {
        JsonElement json = DEFAULT_GSON.toJsonTree(this.toMap());
        return (T) (targetClass == JsonElement.class ? targetClass.cast(json) : DEFAULT_GSON.fromJson(json, targetClass));
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> valuesCopy = new HashMap(this.values);
        if (this.defaults != null) {
            for (java.util.Map.Entry<String, Object> entry : this.defaults.values.entrySet()) {
                if (!valuesCopy.containsKey(entry.getKey())) {
                    valuesCopy.put(entry.getKey(), entry.getValue());
                }
            }
        }
        return valuesCopy;
    }

    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        Set<java.util.Map.Entry<String, Object>> entries = new LinkedHashSet();
        for (java.util.Map.Entry<String, Object> entry : this.values.entrySet()) {
            Class<? extends Object> entryClass = entry.getValue().getClass();
            if (Map.class.isAssignableFrom(entryClass)) {
                entries.add(new Toml.Entry((String) entry.getKey(), this.getTable((String) entry.getKey())));
            } else if (List.class.isAssignableFrom(entryClass)) {
                List<?> value = (List<?>) entry.getValue();
                if (!value.isEmpty() && value.get(0) instanceof Map) {
                    entries.add(new Toml.Entry((String) entry.getKey(), this.getTables((String) entry.getKey())));
                } else {
                    entries.add(new Toml.Entry((String) entry.getKey(), value));
                }
            } else {
                entries.add(new Toml.Entry((String) entry.getKey(), entry.getValue()));
            }
        }
        return entries;
    }

    private Object get(String key) {
        if (this.values.containsKey(key)) {
            return this.values.get(key);
        } else {
            Object current = new HashMap(this.values);
            Keys.Key[] keys = Keys.split(key);
            for (Keys.Key k : keys) {
                if (k.index == -1 && current instanceof Map && ((Map) current).containsKey(k.path)) {
                    return ((Map) current).get(k.path);
                }
                current = ((Map) current).get(k.name);
                if (k.index > -1 && current != null) {
                    if (k.index >= ((List) current).size()) {
                        return null;
                    }
                    current = ((List) current).get(k.index);
                }
                if (current == null) {
                    return this.defaults != null ? this.defaults.get(key) : null;
                }
            }
            return current;
        }
    }

    private Toml(Toml defaults, Map<String, Object> values) {
        this.values = values;
        this.defaults = defaults;
    }

    private class Entry implements java.util.Map.Entry<String, Object> {

        private final String key;

        private final Object value;

        public String getKey() {
            return this.key;
        }

        public Object getValue() {
            return this.value;
        }

        public Object setValue(Object value) {
            throw new UnsupportedOperationException("TOML entry values cannot be changed.");
        }

        private Entry(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}