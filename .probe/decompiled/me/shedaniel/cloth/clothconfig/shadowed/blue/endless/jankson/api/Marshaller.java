package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api;

import java.lang.reflect.Type;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonElement;

public interface Marshaller {

    JsonElement serialize(Object var1);

    <E> E marshall(Class<E> var1, JsonElement var2);

    <E> E marshall(Type var1, JsonElement var2);

    <E> E marshallCarefully(Class<E> var1, JsonElement var2) throws DeserializationException;
}