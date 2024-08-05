package snownee.lychee.util.json;

import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Map.Entry;
import org.jetbrains.annotations.Nullable;

public class JsonPatch {

    private static final Gson GSON = new GsonBuilder().registerTypeAdapter(JsonPointer.class, new JsonPointer.Serializer()).create();

    public final JsonPatch.Type op;

    public JsonPointer path;

    @Nullable
    public JsonPointer from;

    @Nullable
    public JsonElement value;

    public JsonPatch(JsonPatch.Type op, JsonPointer path, @Nullable JsonPointer from, @Nullable JsonElement value) {
        this.op = op;
        this.path = path;
        this.from = from;
        this.value = value;
    }

    public static JsonPatch parse(JsonObject jsonObject) {
        try {
            JsonPatch patch = (JsonPatch) GSON.fromJson(jsonObject, JsonPatch.class);
            if (patch.op != null && patch.path != null) {
                return patch;
            }
        } catch (Exception var2) {
        }
        return null;
    }

    public JsonElement apply(JsonElement doc) {
        if (this.op == JsonPatch.Type.add) {
            return add(doc, this.path, this.value.deepCopy());
        } else if (this.op == JsonPatch.Type.remove) {
            return remove(doc, this.path);
        } else if (this.op == JsonPatch.Type.replace) {
            return replace(doc, this.path, this.value.deepCopy());
        } else if (this.op == JsonPatch.Type.copy) {
            return copy(doc, this.path, this.from);
        } else if (this.op == JsonPatch.Type.move) {
            return move(doc, this.path, this.from);
        } else if (this.op == JsonPatch.Type.test) {
            return test(doc, this.path, this.value);
        } else if (this.op == JsonPatch.Type.merge) {
            return merge(doc, this.path, this.value.deepCopy());
        } else if (this.op == JsonPatch.Type.deep_merge) {
            return deepMerge(doc, this.path, this.value.deepCopy());
        } else {
            throw new IllegalArgumentException("Invalid op: " + this.op);
        }
    }

    public JsonObject toJson() {
        return (JsonObject) GSON.toJsonTree(this, JsonPatch.class);
    }

    public static JsonElement add(JsonElement doc, JsonPointer path, JsonElement value) {
        if (path.isRoot()) {
            return value;
        } else {
            JsonElement parent = path.parent().find(doc);
            Preconditions.checkNotNull(parent, "Invalid path: " + path);
            String last = path.getString(-1);
            if (parent.isJsonObject()) {
                parent.getAsJsonObject().add(last, value);
            } else {
                if (!parent.isJsonArray()) {
                    throw new IllegalArgumentException("Invalid path: " + path);
                }
                if ("-".equals(last)) {
                    parent.getAsJsonArray().add(value);
                } else {
                    JsonArray array = parent.getAsJsonArray();
                    int size = array.size();
                    int after = size - Integer.parseInt(last);
                    array.add(JsonNull.INSTANCE);
                    for (int i = 0; i < after; i++) {
                        array.set(size - i, array.get(size - i - 1));
                    }
                    array.set(size - after, value);
                }
            }
            return doc;
        }
    }

    public static JsonElement remove(JsonElement doc, JsonPointer path) {
        if (path.isRoot()) {
            if (doc.isJsonObject()) {
                doc = new JsonObject();
            } else if (doc.isJsonArray()) {
                doc = new JsonArray();
            }
            return doc;
        } else {
            JsonElement parent = path.parent().find(doc);
            Preconditions.checkNotNull(parent, "Invalid path: " + path);
            String last = path.getString(-1);
            if (parent.isJsonObject()) {
                Preconditions.checkArgument(parent.getAsJsonObject().has(last), last);
                parent.getAsJsonObject().remove(last);
            } else {
                if (!parent.isJsonArray()) {
                    throw new IllegalArgumentException("Invalid path: " + path);
                }
                JsonArray array = parent.getAsJsonArray();
                if ("-".equals(last)) {
                    array.remove(array.size() - 1);
                } else {
                    array.remove(Integer.parseInt(last));
                }
            }
            return doc;
        }
    }

    public static JsonElement replace(JsonElement doc, JsonPointer path, JsonElement value) {
        doc = remove(doc, path);
        return add(doc, path, value);
    }

    public static JsonElement move(JsonElement doc, JsonPointer path, JsonPointer from) {
        Preconditions.checkNotNull(from, "from");
        JsonElement value = from.find(doc);
        doc = remove(doc, from);
        return add(doc, path, value);
    }

    public static JsonElement copy(JsonElement doc, JsonPointer path, JsonPointer from) {
        Preconditions.checkNotNull(from, "from");
        JsonElement value = from.find(doc);
        return add(doc, path, value.deepCopy());
    }

    public static JsonElement test(JsonElement doc, JsonPointer path, JsonElement value) {
        Preconditions.checkNotNull(value, "value");
        JsonElement target = path.find(doc);
        Preconditions.checkArgument(Objects.equals(value, target), "Invalid value: " + target);
        return doc;
    }

    public static JsonElement merge(JsonElement doc, JsonPointer path, JsonElement value) {
        Preconditions.checkNotNull(value, "value");
        Preconditions.checkArgument(value.isJsonObject(), "Invalid value: " + value);
        JsonElement target = path.find(doc);
        Preconditions.checkArgument(target != null && target.isJsonObject(), "Invalid path: " + path);
        JsonObject targetObject = target.getAsJsonObject();
        JsonObject valueObject = value.getAsJsonObject();
        for (Entry<String, JsonElement> entry : valueObject.entrySet()) {
            targetObject.add((String) entry.getKey(), (JsonElement) entry.getValue());
        }
        return doc;
    }

    public static JsonElement deepMerge(JsonElement doc, JsonPointer path, JsonElement value) {
        Preconditions.checkNotNull(value, "value");
        JsonElement target = path.find(doc);
        Preconditions.checkNotNull(target, "Invalid path: " + path);
        ArrayDeque<String> tokens = new ArrayDeque(path.tokens);
        deepMergeInternal(tokens, target, value);
        return doc;
    }

    private static JsonElement deepMergeInternal(ArrayDeque<String> tokens, JsonElement target, JsonElement value) {
        if (target.getClass() != value.getClass()) {
            return value;
        } else {
            if (target.isJsonObject()) {
                JsonObject targetObject = target.getAsJsonObject();
                JsonObject valueObject = value.getAsJsonObject();
                for (Entry<String, JsonElement> entry : valueObject.entrySet()) {
                    String key = (String) entry.getKey();
                    if (targetObject.has(key)) {
                        tokens.addLast(key);
                        targetObject.add(key, deepMergeInternal(tokens, targetObject.get(key), (JsonElement) entry.getValue()));
                        tokens.removeLast();
                    } else {
                        targetObject.add(key, (JsonElement) entry.getValue());
                    }
                }
            } else if (target.isJsonArray()) {
                JsonArray targetArray = target.getAsJsonArray();
                JsonArray valueArray = value.getAsJsonArray();
                int size = targetArray.size();
                for (int i = 0; i < size; i++) {
                    if (i < valueArray.size()) {
                        tokens.addLast(String.valueOf(i));
                        targetArray.set(i, deepMergeInternal(tokens, targetArray.get(i), valueArray.get(i)));
                        tokens.removeLast();
                    } else {
                        valueArray.add(targetArray.get(i));
                    }
                }
            }
            return value;
        }
    }

    public static enum Type {

        add,
        remove,
        replace,
        copy,
        move,
        test,
        merge,
        deep_merge
    }
}