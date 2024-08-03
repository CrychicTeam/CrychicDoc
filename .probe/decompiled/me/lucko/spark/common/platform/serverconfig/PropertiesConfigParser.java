package me.lucko.spark.common.platform.serverconfig;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public enum PropertiesConfigParser implements ConfigParser {

    INSTANCE;

    private static final Gson GSON = new Gson();

    @Override
    public JsonElement load(String file, ExcludedConfigFilter filter) throws IOException {
        Map<String, Object> values = this.parse(Paths.get(file));
        return values == null ? null : filter.apply(GSON.toJsonTree(values));
    }

    @Override
    public Map<String, Object> parse(BufferedReader reader) throws IOException {
        Properties properties = new Properties();
        properties.load(reader);
        Map<String, Object> values = new HashMap();
        properties.forEach((k, v) -> {
            String key = k.toString();
            String value = v.toString();
            if ("true".equals(value) || "false".equals(value)) {
                values.put(key, Boolean.parseBoolean(value));
            } else if (value.matches("\\d+")) {
                try {
                    values.put(key, Long.parseLong(value));
                } catch (NumberFormatException var6) {
                    values.put(key, value);
                }
            } else {
                values.put(key, value);
            }
        });
        return values;
    }
}