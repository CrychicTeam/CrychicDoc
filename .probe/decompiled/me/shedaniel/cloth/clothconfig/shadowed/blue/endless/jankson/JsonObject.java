package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.Marshaller;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.MarshallerImpl;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.serializer.CommentSerializer;

public class JsonObject extends JsonElement implements Map<String, JsonElement> {

    protected Marshaller marshaller = MarshallerImpl.getFallback();

    private List<JsonObject.Entry> entries = new ArrayList();

    @Nullable
    public JsonObject getObject(@Nonnull String name) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(name)) {
                if (entry.value instanceof JsonObject) {
                    return (JsonObject) entry.value;
                }
                return null;
            }
        }
        return null;
    }

    public JsonElement put(@Nonnull String key, @Nonnull JsonElement elem, @Nullable String comment) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(key)) {
                JsonElement result = entry.value;
                entry.value = elem;
                entry.comment = comment;
                return result;
            }
        }
        JsonObject.Entry entryx = new JsonObject.Entry();
        if (elem instanceof JsonObject) {
            ((JsonObject) elem).marshaller = this.marshaller;
        }
        if (elem instanceof JsonArray) {
            ((JsonArray) elem).marshaller = this.marshaller;
        }
        entryx.key = key;
        entryx.value = elem;
        entryx.comment = comment;
        this.entries.add(entryx);
        return null;
    }

    @Nonnull
    public JsonElement putDefault(@Nonnull String key, @Nonnull JsonElement elem, @Nullable String comment) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(key)) {
                return entry.value;
            }
        }
        JsonObject.Entry entryx = new JsonObject.Entry();
        entryx.key = key;
        entryx.value = elem;
        entryx.comment = comment;
        this.entries.add(entryx);
        return elem;
    }

    @Nullable
    public <T> T putDefault(@Nonnull String key, @Nonnull T elem, @Nullable String comment) {
        return this.putDefault(key, elem, elem.getClass(), comment);
    }

    @Nullable
    public <T> T putDefault(@Nonnull String key, @Nonnull T elem, Class<? extends T> clazz, @Nullable String comment) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(key)) {
                return this.marshaller.marshall((Class<T>) clazz, entry.value);
            }
        }
        JsonObject.Entry entryx = new JsonObject.Entry();
        entryx.key = key;
        entryx.value = this.marshaller.serialize(elem);
        if (entryx.value == null) {
            entryx.value = JsonNull.INSTANCE;
        }
        entryx.comment = comment;
        this.entries.add(entryx);
        return elem;
    }

    @Nonnull
    public JsonObject getDelta(@Nonnull JsonObject defaults) {
        JsonObject result = new JsonObject();
        for (JsonObject.Entry entry : this.entries) {
            String key = entry.key;
            JsonElement defaultValue = defaults.get(key);
            if (defaultValue == null) {
                result.put(entry.key, entry.value, entry.comment);
            } else if (entry.value instanceof JsonObject && defaultValue instanceof JsonObject) {
                JsonObject subDelta = ((JsonObject) entry.value).getDelta((JsonObject) defaultValue);
                if (!subDelta.isEmpty()) {
                    result.put(entry.key, subDelta, entry.comment);
                }
            } else if (!entry.value.equals(defaultValue)) {
                result.put(entry.key, entry.value, entry.comment);
            }
        }
        return result;
    }

    @Nullable
    public String getComment(@Nonnull String name) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(name)) {
                return entry.comment;
            }
        }
        return null;
    }

    public void setComment(@Nonnull String name, @Nullable String comment) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(name)) {
                entry.comment = comment;
                return;
            }
        }
    }

    @Override
    public String toJson(boolean comments, boolean newlines, int depth) {
        JsonGrammar grammar = JsonGrammar.builder().withComments(comments).printWhitespace(newlines).build();
        return this.toJson(grammar, depth);
    }

    @Override
    public String toJson(JsonGrammar grammar, int depth) {
        StringBuilder builder = new StringBuilder();
        boolean skipBraces = depth == 0 && grammar.bareRootObject;
        int effectiveDepth = grammar.bareRootObject ? Math.max(depth - 1, 0) : depth;
        int nextDepth = grammar.bareRootObject ? depth : depth + 1;
        if (!skipBraces) {
            builder.append("{");
            if (grammar.printWhitespace && this.entries.size() > 0) {
                builder.append('\n');
            } else {
                builder.append(' ');
            }
        }
        for (int i = 0; i < this.entries.size(); i++) {
            JsonObject.Entry entry = (JsonObject.Entry) this.entries.get(i);
            if (grammar.printWhitespace) {
                for (int j = 0; j < nextDepth; j++) {
                    builder.append("\t");
                }
            }
            CommentSerializer.print(builder, entry.comment, Math.max(effectiveDepth, 0), grammar);
            boolean quoted = !grammar.printUnquotedKeys;
            if (entry.key.contains(" ")) {
                quoted = true;
            }
            if (quoted) {
                builder.append("\"");
            }
            builder.append(entry.key);
            if (quoted) {
                builder.append("\"");
            }
            builder.append(": ");
            builder.append(entry.value.toJson(grammar, depth + 1));
            if (grammar.printCommas) {
                if (i < this.entries.size() - 1 || grammar.printTrailingCommas) {
                    builder.append(",");
                    if (i < this.entries.size() - 1 && !grammar.printWhitespace) {
                        builder.append(' ');
                    }
                }
            } else {
                builder.append(" ");
            }
            if (grammar.printWhitespace) {
                builder.append('\n');
            }
        }
        if (!skipBraces) {
            if (this.entries.size() > 0) {
                if (grammar.printWhitespace) {
                    for (int j = 0; j < effectiveDepth; j++) {
                        builder.append("\t");
                    }
                } else {
                    builder.append(' ');
                }
            }
            builder.append("}");
        }
        return builder.toString();
    }

    public String toString() {
        return this.toJson(true, false, 0);
    }

    public boolean equals(Object other) {
        if (other != null && other instanceof JsonObject) {
            JsonObject otherObject = (JsonObject) other;
            if (this.entries.size() != otherObject.entries.size()) {
                return false;
            } else {
                for (int i = 0; i < this.entries.size(); i++) {
                    JsonObject.Entry a = (JsonObject.Entry) this.entries.get(i);
                    JsonObject.Entry b = (JsonObject.Entry) otherObject.entries.get(i);
                    if (!a.equals(b)) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.entries.hashCode();
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public Marshaller getMarshaller() {
        return this.marshaller;
    }

    @Nullable
    public <E> E get(@Nonnull Class<E> clazz, @Nonnull String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Cannot get from empty key");
        } else {
            JsonElement elem = this.get(key);
            return this.marshaller.marshall(clazz, elem);
        }
    }

    public boolean getBoolean(@Nonnull String key, boolean defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asBoolean(defaultValue) : defaultValue;
    }

    public byte getByte(@Nonnull String key, byte defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asByte(defaultValue) : defaultValue;
    }

    public char getChar(@Nonnull String key, char defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asChar(defaultValue) : defaultValue;
    }

    public short getShort(@Nonnull String key, short defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asShort(defaultValue) : defaultValue;
    }

    public int getInt(@Nonnull String key, int defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asInt(defaultValue) : defaultValue;
    }

    public long getLong(@Nonnull String key, long defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asLong(defaultValue) : defaultValue;
    }

    public float getFloat(@Nonnull String key, float defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asFloat(defaultValue) : defaultValue;
    }

    public double getDouble(@Nonnull String key, double defaultValue) {
        JsonElement elem = this.get(key);
        return elem != null && elem instanceof JsonPrimitive ? ((JsonPrimitive) elem).asDouble(defaultValue) : defaultValue;
    }

    @Nullable
    public <E> E recursiveGet(@Nonnull Class<E> clazz, @Nonnull String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Cannot get from empty key");
        } else {
            String[] parts = key.split("\\.");
            JsonObject cur = this;
            for (int i = 0; i < parts.length; i++) {
                String s = parts[i];
                if (s.isEmpty()) {
                    throw new IllegalArgumentException("Cannot get from broken key '" + key + "'");
                }
                JsonElement elem = cur.get(s);
                if (i >= parts.length - 1) {
                    return this.marshaller.marshall(clazz, elem);
                }
                if (!(elem instanceof JsonObject)) {
                    return null;
                }
                cur = (JsonObject) elem;
            }
            throw new IllegalArgumentException("Cannot get from broken key '" + key + "'");
        }
    }

    public <E extends JsonElement> E recursiveGetOrCreate(@Nonnull Class<E> clazz, @Nonnull String key, @Nonnull E fallback, @Nullable String comment) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Cannot get from empty key");
        } else {
            String[] parts = key.split("\\.");
            JsonObject cur = this;
            for (int i = 0; i < parts.length; i++) {
                String s = parts[i];
                if (s.isEmpty()) {
                    throw new IllegalArgumentException("Cannot get from broken key '" + key + "'");
                }
                JsonElement elem = cur.get(s);
                if (i >= parts.length - 1) {
                    if (elem != null && clazz.isAssignableFrom(elem.getClass())) {
                        return (E) elem;
                    }
                    E result = (E) fallback.clone();
                    cur.put(key, result, comment);
                    return result;
                }
                if (elem instanceof JsonObject) {
                    cur = (JsonObject) elem;
                } else {
                    JsonObject replacement = new JsonObject();
                    cur.put(s, replacement);
                    cur = replacement;
                }
            }
            throw new IllegalArgumentException("Cannot get from broken key '" + key + "'");
        }
    }

    public JsonObject clone() {
        JsonObject result = new JsonObject();
        for (JsonObject.Entry entry : this.entries) {
            result.put(entry.key, entry.value.clone(), entry.comment);
        }
        result.marshaller = this.marshaller;
        return result;
    }

    @Nullable
    public JsonElement put(@Nonnull String key, @Nonnull JsonElement elem) {
        for (JsonObject.Entry entry : this.entries) {
            if (entry.key.equalsIgnoreCase(key)) {
                JsonElement result = entry.value;
                entry.value = elem;
                return result;
            }
        }
        JsonObject.Entry entryx = new JsonObject.Entry();
        entryx.key = key;
        entryx.value = elem;
        this.entries.add(entryx);
        return null;
    }

    public void clear() {
        this.entries.clear();
    }

    public boolean containsKey(@Nullable Object key) {
        if (key == null) {
            return false;
        } else if (!(key instanceof String)) {
            return false;
        } else {
            for (JsonObject.Entry entry : this.entries) {
                if (entry.key.equalsIgnoreCase((String) key)) {
                    return true;
                }
            }
            return false;
        }
    }

    public boolean containsValue(@Nullable Object val) {
        if (val == null) {
            return false;
        } else if (!(val instanceof JsonElement)) {
            return false;
        } else {
            for (JsonObject.Entry entry : this.entries) {
                if (entry.value.equals(val)) {
                    return true;
                }
            }
            return false;
        }
    }

    public Set<java.util.Map.Entry<String, JsonElement>> entrySet() {
        Set<java.util.Map.Entry<String, JsonElement>> result = new LinkedHashSet();
        for (final JsonObject.Entry entry : this.entries) {
            result.add(new java.util.Map.Entry<String, JsonElement>() {

                public String getKey() {
                    return entry.key;
                }

                public JsonElement getValue() {
                    return entry.value;
                }

                public JsonElement setValue(JsonElement value) {
                    JsonElement oldValue = entry.value;
                    entry.value = value;
                    return oldValue;
                }
            });
        }
        return result;
    }

    @Nullable
    public JsonElement get(@Nullable Object key) {
        if (key != null && key instanceof String) {
            for (JsonObject.Entry entry : this.entries) {
                if (entry.key.equalsIgnoreCase((String) key)) {
                    return entry.value;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return this.entries.isEmpty();
    }

    @Nonnull
    public Set<String> keySet() {
        Set<String> keys = new HashSet();
        for (JsonObject.Entry entry : this.entries) {
            keys.add(entry.key);
        }
        return keys;
    }

    public void putAll(Map<? extends String, ? extends JsonElement> map) {
        for (java.util.Map.Entry<? extends String, ? extends JsonElement> entry : map.entrySet()) {
            this.put((String) entry.getKey(), (JsonElement) entry.getValue());
        }
    }

    @Nullable
    public JsonElement remove(@Nullable Object key) {
        if (key != null && key instanceof String) {
            for (int i = 0; i < this.entries.size(); i++) {
                JsonObject.Entry entry = (JsonObject.Entry) this.entries.get(i);
                if (entry.key.equalsIgnoreCase((String) key)) {
                    return ((JsonObject.Entry) this.entries.remove(i)).value;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public int size() {
        return this.entries.size();
    }

    public Collection<JsonElement> values() {
        List<JsonElement> values = new ArrayList();
        for (JsonObject.Entry entry : this.entries) {
            values.add(entry.value);
        }
        return values;
    }

    private static final class Entry {

        protected String comment;

        protected String key;

        protected JsonElement value;

        private Entry() {
        }

        public boolean equals(Object other) {
            if (other != null && other instanceof JsonObject.Entry) {
                JsonObject.Entry o = (JsonObject.Entry) other;
                if (!Objects.equals(this.comment, o.comment)) {
                    return false;
                } else {
                    return !this.key.equals(o.key) ? false : this.value.equals(o.value);
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.comment, this.key, this.value });
        }
    }
}