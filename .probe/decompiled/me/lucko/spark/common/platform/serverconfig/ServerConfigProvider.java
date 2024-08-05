package me.lucko.spark.common.platform.serverconfig;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import me.lucko.spark.common.platform.MetadataProvider;

public abstract class ServerConfigProvider implements MetadataProvider {

    private final Map<String, ConfigParser> files;

    private final ExcludedConfigFilter hiddenPathFilters;

    protected ServerConfigProvider(Map<String, ConfigParser> files, Collection<String> hiddenPaths) {
        this.files = files;
        this.hiddenPathFilters = new ExcludedConfigFilter(hiddenPaths);
    }

    @Override
    public final Map<String, JsonElement> get() {
        Builder<String, JsonElement> builder = ImmutableMap.builder();
        this.files.forEach((path, parser) -> {
            try {
                JsonElement json = parser.load(path, this.hiddenPathFilters);
                if (json == null) {
                    return;
                }
                builder.put(path, json);
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        });
        return builder.build();
    }

    protected static List<String> getSystemPropertyList(String property) {
        String value = System.getProperty(property);
        return value == null ? Collections.emptyList() : Arrays.asList(value.split(","));
    }
}