package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonArray;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonElement;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonGrammar;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonNull;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonObject;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Deserializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.SerializedName;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.DeserializationException;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.Marshaller;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.serializer.DeserializerFunctionPool;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.serializer.InternalDeserializerFunction;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.magic.TypeMagic;

public class POJODeserializer {

    public static void unpackObject(Object target, JsonObject source) {
        try {
            unpackObject(target, source, false);
        } catch (Throwable var3) {
        }
    }

    public static void unpackObject(Object target, JsonObject source, boolean failFast) throws DeserializationException {
        JsonObject work = source.clone();
        for (Field f : target.getClass().getDeclaredFields()) {
            int modifiers = f.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                unpackField(target, f, work, failFast);
            }
        }
        for (Field fx : target.getClass().getFields()) {
            int modifiers = fx.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                unpackField(target, fx, work, failFast);
            }
        }
        if (!work.isEmpty() && failFast) {
            throw new DeserializationException("There was data that couldn't be applied to the destination object: " + work.toJson(JsonGrammar.STRICT));
        }
    }

    public static void unpackField(Object parent, Field f, JsonObject source, boolean failFast) throws DeserializationException {
        String fieldName = f.getName();
        SerializedName nameAnnotation = (SerializedName) f.getAnnotation(SerializedName.class);
        if (nameAnnotation != null) {
            fieldName = nameAnnotation.value();
        }
        if (source.containsKey(fieldName)) {
            JsonElement elem = source.get(fieldName);
            source.remove(fieldName);
            if (elem != null && elem != JsonNull.INSTANCE) {
                try {
                    unpackFieldData(parent, f, elem, source.getMarshaller());
                } catch (Throwable var9) {
                    if (failFast) {
                        throw new DeserializationException("There was a problem unpacking field " + f.getName() + " of class " + parent.getClass().getCanonicalName(), var9);
                    }
                }
            } else {
                boolean accessible = f.isAccessible();
                if (!accessible) {
                    f.setAccessible(true);
                }
                try {
                    f.set(parent, null);
                    if (!accessible) {
                        f.setAccessible(false);
                    }
                } catch (IllegalAccessException | IllegalArgumentException var10) {
                    if (failFast) {
                        throw new DeserializationException("Couldn't set field \"" + f.getName() + "\" of class \"" + parent.getClass().getCanonicalName() + "\"", var10);
                    }
                }
            }
        }
    }

    @Nullable
    public static Object unpack(Type t, JsonElement elem, Marshaller marshaller) {
        Class<?> rawClass = TypeMagic.classForType(t);
        if (rawClass.isPrimitive()) {
            return null;
        } else {
            return elem == null ? null : null;
        }
    }

    public static boolean unpackFieldData(Object parent, Field field, JsonElement elem, Marshaller marshaller) throws Throwable {
        if (elem == null) {
            return true;
        } else {
            try {
                field.setAccessible(true);
            } catch (Throwable var6) {
                return false;
            }
            if (elem == JsonNull.INSTANCE) {
                field.set(parent, null);
                return true;
            } else {
                Class<?> fieldClass = field.getType();
                if (!isCollections(fieldClass)) {
                    Object result = marshaller.marshallCarefully(fieldClass, elem);
                    field.set(parent, result);
                    return true;
                } else {
                    if (field.get(parent) == null) {
                        Object fieldValue = TypeMagic.createAndCast(field.getGenericType());
                        if (fieldValue == null) {
                            return false;
                        }
                        field.set(parent, fieldValue);
                    }
                    if (Map.class.isAssignableFrom(fieldClass)) {
                        Type[] parameters = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
                        unpackMap((Map<Object, Object>) field.get(parent), parameters[0], parameters[1], elem, marshaller);
                        return true;
                    } else if (Collection.class.isAssignableFrom(fieldClass)) {
                        Type elementType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        unpackCollection((Collection<Object>) field.get(parent), elementType, elem, marshaller);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    private static boolean isCollections(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz) || Collection.class.isAssignableFrom(clazz);
    }

    public static void unpackMap(Map<Object, Object> map, Type keyType, Type valueType, JsonElement elem, Marshaller marshaller) throws DeserializationException {
        if (!(elem instanceof JsonObject)) {
            throw new DeserializationException("Cannot deserialize a " + elem.getClass().getSimpleName() + " into a Map - expected a JsonObject!");
        } else {
            JsonObject object = (JsonObject) elem;
            for (Entry<String, JsonElement> entry : object.entrySet()) {
                try {
                    Object k = marshaller.marshall(keyType, new JsonPrimitive(entry.getKey()));
                    Object v = marshaller.marshall(valueType, (JsonElement) entry.getValue());
                    if (k != null && v != null) {
                        map.put(k, v);
                    }
                } catch (Throwable var10) {
                }
            }
        }
    }

    public static void unpackCollection(Collection<Object> collection, Type elementType, JsonElement elem, Marshaller marshaller) throws DeserializationException {
        if (!(elem instanceof JsonArray)) {
            throw new DeserializationException("Cannot deserialize a " + elem.getClass().getSimpleName() + " into a Set - expected a JsonArray!");
        } else {
            for (JsonElement arrayElem : (JsonArray) elem) {
                Object o = marshaller.marshall(elementType, arrayElem);
                if (o != null) {
                    collection.add(o);
                }
            }
        }
    }

    protected static <B> DeserializerFunctionPool<B> deserializersFor(Class<B> targetClass) {
        DeserializerFunctionPool<B> pool = new DeserializerFunctionPool<>(targetClass);
        for (Method m : targetClass.getDeclaredMethods()) {
            if (m.getAnnotation(Deserializer.class) != null && Modifier.isStatic(m.getModifiers()) && m.getReturnType().equals(targetClass)) {
                Parameter[] params = m.getParameters();
                if (params.length >= 1) {
                    Class<?> sourceClass = params[0].getType();
                    InternalDeserializerFunction<B> deserializer = makeDeserializer(m, sourceClass, targetClass);
                    if (deserializer != null) {
                        pool.registerUnsafe(sourceClass, deserializer);
                    }
                }
            }
        }
        return pool;
    }

    @Nullable
    private static <A, B> InternalDeserializerFunction<B> makeDeserializer(@Nonnull Method m, @Nonnull Class<A> sourceClass, @Nonnull Class<B> targetClass) {
        if (!m.getReturnType().equals(targetClass)) {
            return null;
        } else {
            Parameter[] params = m.getParameters();
            if (params.length == 1) {
                return (o, marshaller) -> {
                    try {
                        return (B) m.invoke(null, o);
                    } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var4) {
                        throw new DeserializationException(var4);
                    }
                };
            } else if (params.length == 2) {
                return params[1].getClass().equals(Marshaller.class) ? (o, marshaller) -> {
                    try {
                        return (B) m.invoke(null, o, marshaller);
                    } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException var4) {
                        throw new DeserializationException(var4);
                    }
                } : null;
            } else {
                return null;
            }
        }
    }
}