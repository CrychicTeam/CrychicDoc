package snownee.lychee.fragment;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class JsonFragment {

    private static final Pattern PATTERN = Pattern.compile("\\$\\{([^}]+?)}");

    public static void process(JsonElement json, JsonFragment.Context context) {
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            Map<String, JsonElement> toPut = Maps.newLinkedHashMap();
            object.entrySet().forEach(entry -> {
                String key = (String) entry.getKey();
                JsonElement value = (JsonElement) entry.getValue();
                if (value.isJsonPrimitive()) {
                    JsonElement processed = processVars(value, context);
                    if (processed != value) {
                        toPut.put(key, processed);
                    }
                } else {
                    JsonFragment.Anchor anchor = parseAnchor(value, context);
                    if (anchor == null) {
                        process(value, context);
                    } else {
                        anchor.apply(key, object, context, toPut);
                    }
                }
            });
            toPut.forEach((k, v) -> {
                if (v == null) {
                    object.remove(k);
                } else {
                    object.add(k, v);
                }
            });
        } else if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            Map<JsonElement, List<JsonElement>> toReplace = Maps.newHashMap();
            array.forEach(element -> {
                if (element.isJsonPrimitive()) {
                    JsonElement processed = processVars(element, context);
                    if (processed != element) {
                        toReplace.put(element, List.of(processed));
                    }
                } else {
                    JsonFragment.Anchor anchor = parseAnchor(element, context);
                    if (anchor == null) {
                        process(element, context);
                    } else {
                        anchor.apply(array, context, $ -> toReplace.put(element, $));
                    }
                }
            });
            if (!toReplace.isEmpty()) {
                List<JsonElement> newElements = Lists.newArrayList();
                array.forEach(element -> {
                    if (toReplace.containsKey(element)) {
                        newElements.addAll((Collection) toReplace.get(element));
                    } else {
                        newElements.add(element);
                    }
                });
                while (array.size() > 0) {
                    array.remove(0);
                }
                newElements.forEach(array::add);
            }
        }
    }

    private static JsonElement processVars(JsonElement json, JsonFragment.Context context) {
        if (!context.vars.isEmpty() && json instanceof JsonPrimitive primitive && primitive.isString()) {
            String s = primitive.getAsString();
            if (s.startsWith("$")) {
                JsonElement e = (JsonElement) context.vars.get(s.substring(1));
                if (e != null) {
                    return e;
                }
            }
            Matcher matcher = PATTERN.matcher(s);
            if (matcher.find()) {
                s = matcher.replaceAll(result -> {
                    String v = result.group(1);
                    return ((JsonElement) context.vars.getOrDefault(v, JsonNull.INSTANCE)).getAsString();
                });
                return new JsonPrimitive(s);
            }
        }
        return json;
    }

    @Nullable
    private static JsonFragment.Anchor parseAnchor(JsonElement json, JsonFragment.Context context) {
        if (!json.isJsonObject()) {
            return null;
        } else {
            JsonObject object = json.getAsJsonObject();
            boolean spread;
            String key;
            if (object.has("@")) {
                spread = false;
                key = "@";
            } else {
                if (!object.has("...@")) {
                    return null;
                }
                spread = true;
                key = "...@";
            }
            String id = object.get(key).getAsString();
            JsonElement fragment = (JsonElement) context.getter.apply(id);
            if (fragment == null) {
                return null;
            } else {
                Map<String, JsonElement> vars = Maps.newHashMap();
                object.entrySet().forEach(entry -> {
                    String k = (String) entry.getKey();
                    if (!key.equals(k)) {
                        Preconditions.checkArgument(!k.isEmpty(), "Empty variable name");
                        vars.put(k, (JsonElement) entry.getValue());
                    }
                });
                return new JsonFragment.Anchor(fragment, spread, vars);
            }
        }
    }

    static record Anchor(JsonElement fragment, boolean spread, Map<String, JsonElement> vars) {

        public void apply(String key, JsonObject json, JsonFragment.Context context, Map<String, JsonElement> toPut) {
            if (this.spread) {
                toPut.put(key, null);
            }
            context.putVars(this.vars);
            JsonElement fragment = this.fragment().deepCopy();
            JsonFragment.process(fragment, context);
            if (this.spread) {
                Preconditions.checkArgument(fragment.isJsonObject(), "Fragment %s is not an object", fragment);
                fragment.getAsJsonObject().entrySet().forEach(entry -> toPut.put((String) entry.getKey(), (JsonElement) entry.getValue()));
            } else {
                toPut.put(key, fragment);
            }
            context.removeVars(this.vars);
        }

        public void apply(JsonArray json, JsonFragment.Context context, Consumer<List<JsonElement>> callback) {
            context.putVars(this.vars);
            JsonElement fragment = this.fragment().deepCopy();
            JsonFragment.process(fragment, context);
            if (this.spread) {
                Preconditions.checkArgument(fragment.isJsonArray(), "Fragment %s is not an array", fragment);
                callback.accept(Streams.stream(fragment.getAsJsonArray().iterator()).toList());
            } else {
                callback.accept(List.of(fragment));
            }
            context.removeVars(this.vars);
        }
    }

    static record Context(Function<String, JsonElement> getter, Map<String, JsonElement> vars) {

        public void putVars(Map<String, JsonElement> map) {
            map.forEach((k, v) -> {
                Preconditions.checkArgument(!this.vars.containsKey(k), "Duplicate variable %s", k);
                this.vars.put(k, v);
            });
        }

        public void removeVars(Map<String, JsonElement> map) {
            map.forEach((k, v) -> this.vars.remove(k));
        }
    }
}