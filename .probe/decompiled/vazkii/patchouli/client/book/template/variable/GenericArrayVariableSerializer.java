package vazkii.patchouli.client.book.template.variable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import vazkii.patchouli.api.IVariableSerializer;

public class GenericArrayVariableSerializer<T> implements IVariableSerializer<T[]> {

    protected final T[] empty;

    private final IVariableSerializer<T> inner;

    public GenericArrayVariableSerializer(IVariableSerializer<T> inner, Class<T> type) {
        this.empty = (T[]) ((Object[]) Array.newInstance(type, 0));
        this.inner = inner;
    }

    public T[] fromJson(JsonElement json) {
        if (!json.isJsonArray()) {
            throw new IllegalArgumentException("Can't create an array of objects from a non-array JSON!");
        } else {
            JsonArray array = json.getAsJsonArray();
            List<T> stacks = new ArrayList();
            for (JsonElement e : array) {
                stacks.add(this.inner.fromJson(e));
            }
            return (T[]) stacks.toArray(this.empty);
        }
    }

    public JsonArray toJson(T[] array) {
        JsonArray result = new JsonArray();
        for (T elem : array) {
            result.add(this.inner.toJson(elem));
        }
        return result;
    }
}