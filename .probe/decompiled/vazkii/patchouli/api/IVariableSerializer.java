package vazkii.patchouli.api;

import com.google.gson.JsonElement;

public interface IVariableSerializer<T> {

    T fromJson(JsonElement var1);

    JsonElement toJson(T var1);
}