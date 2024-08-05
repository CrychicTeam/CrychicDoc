package me.lucko.spark.common.platform;

import com.google.gson.JsonElement;
import java.util.LinkedHashMap;
import java.util.Map;

@FunctionalInterface
public interface MetadataProvider {

    Map<String, JsonElement> get();

    default Map<String, String> export() {
        Map<String, String> map = new LinkedHashMap();
        this.get().forEach((key, value) -> map.put(key, value.toString()));
        return map;
    }
}