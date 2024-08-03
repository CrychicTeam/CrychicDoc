package me.lucko.spark.common.platform.serverconfig;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ExcludedConfigFilter {

    private final Collection<String> pathsToExclude;

    public ExcludedConfigFilter(Collection<String> pathsToExclude) {
        this.pathsToExclude = pathsToExclude;
    }

    public JsonElement apply(JsonElement json) {
        for (String path : this.pathsToExclude) {
            Deque<String> pathDeque = new LinkedList(Arrays.asList(path.split("\\.")));
            delete(json, pathDeque);
        }
        return json;
    }

    private static void delete(JsonElement json, Deque<String> path) {
        if (!path.isEmpty()) {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                String expected = ((String) path.removeFirst()).replace("<dot>", ".");
                Collection<String> keys;
                if (expected.equals("*")) {
                    keys = (Collection<String>) jsonObject.entrySet().stream().map(Entry::getKey).collect(Collectors.toList());
                } else if (expected.endsWith("*")) {
                    String pattern = expected.substring(0, expected.length() - 1);
                    keys = (Collection<String>) jsonObject.entrySet().stream().map(Entry::getKey).filter(keyx -> keyx.startsWith(pattern)).collect(Collectors.toList());
                } else if (jsonObject.has(expected)) {
                    keys = Collections.singletonList(expected);
                } else {
                    keys = Collections.emptyList();
                }
                for (String key : keys) {
                    if (path.isEmpty()) {
                        jsonObject.remove(key);
                    } else {
                        Deque<String> pathCopy = (Deque<String>) (keys.size() > 1 ? new LinkedList(path) : path);
                        delete(jsonObject.get(key), pathCopy);
                    }
                }
            }
        }
    }
}