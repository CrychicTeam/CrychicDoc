package snownee.lychee.util.json;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public class JsonPointer {

    public final List<String> tokens;

    public JsonPointer(Collection<String> tokens) {
        this.tokens = List.copyOf(tokens);
    }

    public JsonPointer(String path) {
        if (path.isEmpty()) {
            this.tokens = List.of();
        } else {
            if (path.codePointAt(0) != 47) {
                throw new IllegalArgumentException(path);
            }
            this.tokens = List.of(path.substring(1).split("/"));
        }
    }

    public String toString() {
        return this.isRoot() ? "" : "/" + Joiner.on('/').join(this.tokens);
    }

    @Nullable
    public JsonElement find(JsonElement doc) {
        if (this.isRoot()) {
            return doc;
        } else {
            JsonElement element = doc;
            try {
                for (String token : this.tokens) {
                    if (element.isJsonArray()) {
                        element = element.getAsJsonArray().get(Integer.parseInt(token));
                    } else {
                        if (!element.isJsonObject()) {
                            throw new IllegalArgumentException();
                        }
                        element = element.getAsJsonObject().get(token);
                    }
                }
                return element;
            } catch (Exception var5) {
                return null;
            }
        }
    }

    public int size() {
        return this.tokens.size();
    }

    public String getString(int index) {
        if (index < 0) {
            index += this.tokens.size();
        }
        return (String) this.tokens.get(index);
    }

    public int getInt(int index) {
        return Integer.parseInt(this.getString(index));
    }

    public boolean isRoot() {
        return this.tokens.isEmpty();
    }

    public JsonPointer parent() {
        List<String> list = Lists.newArrayList(this.tokens);
        list.remove(list.size() - 1);
        return new JsonPointer(list);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof JsonPointer ? this.tokens.equals(((JsonPointer) obj).tokens) : false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.tokens });
    }

    public JsonPointer append(String token) {
        return new JsonPointer(this + "/" + token);
    }

    public boolean isSelfOrParentOf(List<String> tokens1) {
        if (this.tokens.size() < tokens1.size()) {
            return false;
        } else {
            for (int i = 0; i < tokens1.size(); i++) {
                if (!Objects.equals(this.tokens.get(i), tokens1.get(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static class Serializer implements JsonDeserializer<JsonPointer>, JsonSerializer<JsonPointer> {

        public JsonElement serialize(JsonPointer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }

        public JsonPointer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new JsonPointer(json.getAsString());
        }
    }
}