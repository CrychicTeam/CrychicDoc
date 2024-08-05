package net.minecraftforge.client.model.geometry;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public interface IGeometryLoader<T extends IUnbakedGeometry<T>> {

    T read(JsonObject var1, JsonDeserializationContext var2) throws JsonParseException;
}