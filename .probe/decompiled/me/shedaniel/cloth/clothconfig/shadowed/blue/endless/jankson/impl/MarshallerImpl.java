package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonArray;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonElement;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonNull;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonObject;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.JsonPrimitive;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.SerializedName;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.DeserializationException;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.DeserializerFunction;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.api.Marshaller;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.impl.serializer.DeserializerFunctionPool;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.magic.TypeMagic;

@Deprecated
public class MarshallerImpl implements Marshaller {

    private static MarshallerImpl INSTANCE = new MarshallerImpl();

    private Map<Class<?>, Function<Object, ?>> primitiveMarshallers = new HashMap();

    Map<Class<?>, Function<JsonObject, ?>> typeAdapters = new HashMap();

    private Map<Class<?>, BiFunction<Object, Marshaller, JsonElement>> serializers = new HashMap();

    private Map<Class<?>, DeserializerFunctionPool<?>> deserializers = new HashMap();

    private Map<Class<?>, Supplier<?>> typeFactories = new HashMap();

    public static Marshaller getFallback() {
        return INSTANCE;
    }

    public <T> void register(Class<T> clazz, Function<Object, T> marshaller) {
        this.primitiveMarshallers.put(clazz, marshaller);
    }

    public <T> void registerTypeAdapter(Class<T> clazz, Function<JsonObject, T> adapter) {
        this.typeAdapters.put(clazz, adapter);
    }

    public <T> void registerSerializer(Class<T> clazz, Function<T, JsonElement> serializer) {
        this.serializers.put(clazz, (BiFunction) (it, marshaller) -> (JsonElement) serializer.apply(it));
    }

    public <T> void registerSerializer(Class<T> clazz, BiFunction<T, Marshaller, JsonElement> serializer) {
        this.serializers.put(clazz, serializer);
    }

    public <T> void registerTypeFactory(Class<T> clazz, Supplier<T> supplier) {
        this.typeFactories.put(clazz, supplier);
    }

    public <A, B> void registerDeserializer(Class<A> sourceClass, Class<B> targetClass, DeserializerFunction<A, B> function) {
        DeserializerFunctionPool<B> pool = (DeserializerFunctionPool<B>) this.deserializers.get(targetClass);
        if (pool == null) {
            pool = new DeserializerFunctionPool<>(targetClass);
            this.deserializers.put(targetClass, pool);
        }
        pool.registerUnsafe(sourceClass, function);
    }

    public MarshallerImpl() {
        this.register(Void.class, it -> null);
        this.register(String.class, it -> it instanceof String ? (String) it : it.toString());
        this.register(Byte.class, it -> it instanceof Number ? ((Number) it).byteValue() : null);
        this.register(Character.class, it -> it instanceof Number ? (char) ((Number) it).shortValue() : it.toString().charAt(0));
        this.register(Short.class, it -> it instanceof Number ? ((Number) it).shortValue() : null);
        this.register(Integer.class, it -> it instanceof Number ? ((Number) it).intValue() : null);
        this.register(Long.class, it -> it instanceof Number ? ((Number) it).longValue() : null);
        this.register(Float.class, it -> it instanceof Number ? ((Number) it).floatValue() : null);
        this.register(Double.class, it -> it instanceof Number ? ((Number) it).doubleValue() : null);
        this.register(Boolean.class, it -> it instanceof Boolean ? (Boolean) it : null);
        this.register(void.class, it -> null);
        this.register(byte.class, it -> it instanceof Number ? ((Number) it).byteValue() : null);
        this.register(char.class, it -> it instanceof Number ? (char) ((Number) it).shortValue() : it.toString().charAt(0));
        this.register(short.class, it -> it instanceof Number ? ((Number) it).shortValue() : null);
        this.register(int.class, it -> it instanceof Number ? ((Number) it).intValue() : null);
        this.register(long.class, it -> it instanceof Number ? ((Number) it).longValue() : null);
        this.register(float.class, it -> it instanceof Number ? ((Number) it).floatValue() : null);
        this.register(double.class, it -> it instanceof Number ? ((Number) it).doubleValue() : null);
        this.register(boolean.class, it -> it instanceof Boolean ? (Boolean) it : null);
        this.registerSerializer(Void.class, (Function) (it -> JsonNull.INSTANCE));
        this.registerSerializer(Character.class, (Function) (it -> new JsonPrimitive("" + it)));
        this.registerSerializer(String.class, JsonPrimitive::new);
        this.registerSerializer(Byte.class, (Function) (it -> new JsonPrimitive((long) it.byteValue())));
        this.registerSerializer(Short.class, (Function) (it -> new JsonPrimitive((long) it.shortValue())));
        this.registerSerializer(Integer.class, (Function) (it -> new JsonPrimitive((long) it.intValue())));
        this.registerSerializer(Long.class, JsonPrimitive::new);
        this.registerSerializer(Float.class, (Function) (it -> new JsonPrimitive((double) it.floatValue())));
        this.registerSerializer(Double.class, JsonPrimitive::new);
        this.registerSerializer(Boolean.class, JsonPrimitive::new);
        this.registerSerializer(void.class, (Function) (it -> JsonNull.INSTANCE));
        this.registerSerializer(char.class, (Function) (it -> new JsonPrimitive("" + it)));
        this.registerSerializer(byte.class, (Function) (it -> new JsonPrimitive((long) it.byteValue())));
        this.registerSerializer(short.class, (Function) (it -> new JsonPrimitive((long) it.shortValue())));
        this.registerSerializer(int.class, (Function) (it -> new JsonPrimitive((long) it.intValue())));
        this.registerSerializer(long.class, JsonPrimitive::new);
        this.registerSerializer(float.class, (Function) (it -> new JsonPrimitive((double) it.floatValue())));
        this.registerSerializer(double.class, JsonPrimitive::new);
        this.registerSerializer(boolean.class, JsonPrimitive::new);
    }

    @Nullable
    @Override
    public <T> T marshall(Type type, JsonElement elem) {
        if (elem == null) {
            return null;
        } else if (elem == JsonNull.INSTANCE) {
            return null;
        } else if (type instanceof Class) {
            try {
                return this.marshall((Class<T>) type, elem);
            } catch (ClassCastException var4) {
                return null;
            }
        } else if (type instanceof ParameterizedType) {
            try {
                Class<T> clazz = (Class<T>) TypeMagic.classForType(type);
                return this.marshall(clazz, elem);
            } catch (ClassCastException var5) {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public <T> T marshall(Class<T> clazz, JsonElement elem) {
        try {
            return this.marshall(clazz, elem, false);
        } catch (Throwable var4) {
            return null;
        }
    }

    @Override
    public <T> T marshallCarefully(Class<T> clazz, JsonElement elem) throws DeserializationException {
        return this.marshall(clazz, elem, true);
    }

    @Nullable
    public <T> T marshall(Class<T> clazz, JsonElement elem, boolean failFast) throws DeserializationException {
        if (elem == null) {
            return null;
        } else if (elem == JsonNull.INSTANCE) {
            return null;
        } else if (clazz.isAssignableFrom(elem.getClass())) {
            return (T) elem;
        } else {
            DeserializerFunctionPool<T> pool = (DeserializerFunctionPool<T>) this.deserializers.get(clazz);
            if (pool != null) {
                try {
                    return pool.apply(elem, this);
                } catch (DeserializerFunctionPool.FunctionMatchFailedException var15) {
                }
            }
            pool = POJODeserializer.deserializersFor(clazz);
            try {
                return pool.apply(elem, this);
            } catch (DeserializerFunctionPool.FunctionMatchFailedException var14) {
                if (Enum.class.isAssignableFrom(clazz)) {
                    if (!(elem instanceof JsonPrimitive)) {
                        return null;
                    }
                    String name = ((JsonPrimitive) elem).getValue().toString();
                    T[] constants = (T[]) clazz.getEnumConstants();
                    if (constants == null) {
                        return null;
                    }
                    for (T t : constants) {
                        if (((Enum) t).name().equals(name)) {
                            return t;
                        }
                    }
                }
                if (clazz.equals(String.class)) {
                    if (elem instanceof JsonObject) {
                        return (T) ((JsonObject) elem).toJson(false, false);
                    } else if (elem instanceof JsonArray) {
                        return (T) ((JsonArray) elem).toJson(false, false);
                    } else if (elem instanceof JsonPrimitive) {
                        ((JsonPrimitive) elem).getValue();
                        return (T) ((JsonPrimitive) elem).asString();
                    } else if (elem instanceof JsonNull) {
                        return (T) "null";
                    } else if (failFast) {
                        throw new DeserializationException("Encountered unexpected JsonElement type while deserializing to string: " + elem.getClass().getCanonicalName());
                    } else {
                        return null;
                    }
                } else if (elem instanceof JsonPrimitive) {
                    Function<Object, ?> func = (Function<Object, ?>) this.primitiveMarshallers.get(clazz);
                    if (func != null) {
                        return (T) func.apply(((JsonPrimitive) elem).getValue());
                    } else if (failFast) {
                        throw new DeserializationException("Don't know how to unpack value '" + elem.toString() + "' into target type '" + clazz.getCanonicalName() + "'");
                    } else {
                        return null;
                    }
                } else if (elem instanceof JsonObject) {
                    if (clazz.isPrimitive()) {
                        throw new DeserializationException("Can't marshall json object into primitive type " + clazz.getCanonicalName());
                    } else if (JsonPrimitive.class.isAssignableFrom(clazz)) {
                        if (failFast) {
                            throw new DeserializationException("Can't marshall json object into a json primitive");
                        } else {
                            return null;
                        }
                    } else {
                        JsonObject obj = (JsonObject) elem;
                        obj.setMarshaller(this);
                        if (this.typeAdapters.containsKey(clazz)) {
                            return (T) ((Function) this.typeAdapters.get(clazz)).apply((JsonObject) elem);
                        } else if (this.typeFactories.containsKey(clazz)) {
                            T result = (T) ((Supplier) this.typeFactories.get(clazz)).get();
                            try {
                                POJODeserializer.unpackObject(result, obj, failFast);
                                return result;
                            } catch (Throwable var13) {
                                if (failFast) {
                                    throw var13;
                                } else {
                                    return null;
                                }
                            }
                        } else {
                            try {
                                T result = TypeMagic.createAndCast(clazz, failFast);
                                POJODeserializer.unpackObject(result, obj, failFast);
                                return result;
                            } catch (Throwable var12) {
                                if (failFast) {
                                    throw var12;
                                } else {
                                    return null;
                                }
                            }
                        }
                    }
                } else {
                    if (elem instanceof JsonArray) {
                        if (clazz.isPrimitive()) {
                            return null;
                        }
                        if (clazz.isArray()) {
                            Class<?> componentType = clazz.getComponentType();
                            JsonArray array = (JsonArray) elem;
                            T result = (T) Array.newInstance(componentType, array.size());
                            for (int i = 0; i < array.size(); i++) {
                                Array.set(result, i, this.marshall((Class<T>) componentType, array.get(i)));
                            }
                            return result;
                        }
                    }
                    return null;
                }
            }
        }
    }

    @Override
    public JsonElement serialize(Object obj) {
        if (obj == null) {
            return JsonNull.INSTANCE;
        } else {
            BiFunction<Object, Marshaller, JsonElement> serializer = (BiFunction<Object, Marshaller, JsonElement>) this.serializers.get(obj.getClass());
            if (serializer != null) {
                JsonElement result = (JsonElement) serializer.apply(obj, this);
                if (result instanceof JsonObject) {
                    ((JsonObject) result).setMarshaller(this);
                }
                if (result instanceof JsonArray) {
                    ((JsonArray) result).setMarshaller(this);
                }
                return result;
            } else {
                for (Entry<Class<?>, BiFunction<Object, Marshaller, JsonElement>> entry : this.serializers.entrySet()) {
                    if (((Class) entry.getKey()).isAssignableFrom(obj.getClass())) {
                        JsonElement resultx = (JsonElement) ((BiFunction) entry.getValue()).apply(obj, this);
                        if (resultx instanceof JsonObject) {
                            ((JsonObject) resultx).setMarshaller(this);
                        }
                        if (resultx instanceof JsonArray) {
                            ((JsonArray) resultx).setMarshaller(this);
                        }
                        return resultx;
                    }
                }
                if (obj instanceof Enum) {
                    return new JsonPrimitive(((Enum) obj).name());
                } else if (obj.getClass().isArray()) {
                    JsonArray array = new JsonArray();
                    array.setMarshaller(this);
                    for (int i = 0; i < Array.getLength(obj); i++) {
                        Object elem = Array.get(obj, i);
                        JsonElement parsed = this.serialize(elem);
                        array.add(parsed);
                    }
                    return array;
                } else if (obj instanceof Collection) {
                    JsonArray array = new JsonArray();
                    array.setMarshaller(this);
                    for (Object elem : (Collection) obj) {
                        JsonElement parsed = this.serialize(elem);
                        array.add(parsed);
                    }
                    return array;
                } else if (!(obj instanceof Map)) {
                    JsonObject resultxx = new JsonObject();
                    for (Field f : obj.getClass().getFields()) {
                        if (!Modifier.isStatic(f.getModifiers()) && !Modifier.isTransient(f.getModifiers())) {
                            f.setAccessible(true);
                            try {
                                Object child = f.get(obj);
                                String name = f.getName();
                                SerializedName nameAnnotation = (SerializedName) f.getAnnotation(SerializedName.class);
                                if (nameAnnotation != null) {
                                    name = nameAnnotation.value();
                                }
                                Comment comment = (Comment) f.getAnnotation(Comment.class);
                                if (comment == null) {
                                    resultxx.put(name, this.serialize(child));
                                } else {
                                    resultxx.put(name, this.serialize(child), comment.value());
                                }
                            } catch (IllegalAccessException | IllegalArgumentException var13) {
                            }
                        }
                    }
                    for (Field fx : obj.getClass().getDeclaredFields()) {
                        if (!Modifier.isPublic(fx.getModifiers()) && !Modifier.isStatic(fx.getModifiers()) && !Modifier.isTransient(fx.getModifiers())) {
                            fx.setAccessible(true);
                            try {
                                Object childx = fx.get(obj);
                                String namex = fx.getName();
                                SerializedName nameAnnotationx = (SerializedName) fx.getAnnotation(SerializedName.class);
                                if (nameAnnotationx != null) {
                                    namex = nameAnnotationx.value();
                                }
                                Comment comment = (Comment) fx.getAnnotation(Comment.class);
                                if (comment == null) {
                                    resultxx.put(namex, this.serialize(childx));
                                } else {
                                    resultxx.put(namex, this.serialize(childx), comment.value());
                                }
                            } catch (IllegalAccessException | IllegalArgumentException var12) {
                            }
                        }
                    }
                    return resultxx;
                } else {
                    JsonObject resultxx = new JsonObject();
                    for (Entry<?, ?> entryx : ((Map) obj).entrySet()) {
                        String k = entryx.getKey().toString();
                        Object v = entryx.getValue();
                        resultxx.put(k, this.serialize(v));
                    }
                    return resultxx;
                }
            }
        }
    }
}