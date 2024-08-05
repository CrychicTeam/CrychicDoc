package snownee.lychee.util;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import java.lang.reflect.Type;

public class GsonContextImpl implements JsonSerializationContext, JsonDeserializationContext {

    private Gson gson;

    public GsonContextImpl(Gson gson) {
        this.gson = gson;
    }

    public JsonElement serialize(Object src) {
        return this.gson.toJsonTree(src);
    }

    public JsonElement serialize(Object src, Type typeOfSrc) {
        return this.gson.toJsonTree(src, typeOfSrc);
    }

    public <R> R deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
        return (R) this.gson.fromJson(json, typeOfT);
    }
}