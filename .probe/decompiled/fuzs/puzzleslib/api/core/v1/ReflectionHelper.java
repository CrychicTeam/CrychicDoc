package fuzs.puzzleslib.api.core.v1;

import com.google.common.base.Defaults;
import com.google.common.collect.Maps;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ReflectionHelper {

    private static final Map<String, Field> FIELDS_CACHE = Maps.newIdentityHashMap();

    private static final Map<String, Method> METHODS_CACHE = Maps.newIdentityHashMap();

    private static final Map<String, Constructor<?>> CONSTRUCTORS_CACHE = Maps.newIdentityHashMap();

    @Nullable
    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, true);
    }

    @Nullable
    public static Field findField(Class<?> clazz, String name, boolean allowCache) {
        return findField(clazz.getTypeName(), name, allowCache);
    }

    @Nullable
    public static Field findField(String typeName, String name, boolean allowCache) {
        Objects.requireNonNull(typeName, "clazz name was null");
        Objects.requireNonNull(name, "field name was null");
        String fieldName = getClassMemberName(typeName, name);
        if (allowCache && FIELDS_CACHE.containsKey(fieldName)) {
            return (Field) FIELDS_CACHE.get(fieldName);
        } else {
            try {
                Field field = Class.forName(typeName).getDeclaredField(name);
                field.setAccessible(true);
                FIELDS_CACHE.put(fieldName, field);
                return field;
            } catch (ClassNotFoundException | NoSuchFieldException var5) {
                PuzzlesLib.LOGGER.warn("Unable to find field {}", fieldName, var5);
                FIELDS_CACHE.put(fieldName, null);
                return null;
            }
        }
    }

    @Nullable
    public static Method findMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        return findMethod(clazz, name, true, parameterTypes);
    }

    @Nullable
    public static Method findMethod(Class<?> clazz, String name, boolean allowCache, Class<?>... parameterTypes) {
        return findMethod(clazz.getTypeName(), name, allowCache, parameterTypes);
    }

    @Nullable
    public static Method findMethod(String typeName, String name, boolean allowCache, Class<?>... parameterTypes) {
        Objects.requireNonNull(typeName, "clazz name was null");
        Objects.requireNonNull(name, "method name was null");
        String methodName = getMethodName(typeName, name, parameterTypes);
        if (allowCache && METHODS_CACHE.containsKey(methodName)) {
            return (Method) METHODS_CACHE.get(methodName);
        } else {
            try {
                Method method = Class.forName(typeName).getDeclaredMethod(name, parameterTypes);
                method.setAccessible(true);
                METHODS_CACHE.put(methodName, method);
                return method;
            } catch (ClassNotFoundException | NoSuchMethodException var6) {
                PuzzlesLib.LOGGER.warn("Unable to find method {}", methodName, var6);
                METHODS_CACHE.put(methodName, null);
                return null;
            }
        }
    }

    @Nullable
    public static <T> Constructor<T> findConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        return findConstructor(clazz, true, parameterTypes);
    }

    @Nullable
    public static <T> Constructor<T> findConstructor(Class<?> clazz, boolean allowCache, Class<?>... parameterTypes) {
        return findConstructor(clazz.getTypeName(), allowCache, parameterTypes);
    }

    @Nullable
    public static <T> Constructor<T> findConstructor(String typeName, boolean allowCache, Class<?>... parameterTypes) {
        Objects.requireNonNull(typeName, "clazz name was null");
        String constructorName = getConstructorName(typeName, parameterTypes);
        if (allowCache && CONSTRUCTORS_CACHE.containsKey(constructorName)) {
            return (Constructor<T>) CONSTRUCTORS_CACHE.get(constructorName);
        } else {
            try {
                Constructor<T> constructor = Class.forName(typeName).getDeclaredConstructor(parameterTypes);
                constructor.setAccessible(true);
                CONSTRUCTORS_CACHE.put(constructorName, constructor);
                return constructor;
            } catch (ClassNotFoundException | NoSuchMethodException var5) {
                PuzzlesLib.LOGGER.warn("Unable to find constructor {}", constructorName, var5);
                CONSTRUCTORS_CACHE.put(constructorName, null);
                return null;
            }
        }
    }

    public static <T, E> Optional<T> getValue(Class<? super E> clazz, String name, E instance) {
        return getValue(findField(clazz, name), instance);
    }

    public static <T, E> Optional<T> getValue(String typeName, String name, E instance) {
        return getValue(findField(typeName, name, true), instance);
    }

    public static <T, E> boolean setValue(Class<? super E> clazz, String name, E instance, T value) {
        return setValue(findField(clazz, name), instance, value);
    }

    public static <T, E> boolean setValue(String typeName, String name, E instance, T value) {
        return setValue(findField(typeName, name, true), instance, value);
    }

    public static <T> Optional<T> getValue(@Nullable Field field, Object instance) {
        if (field != null) {
            try {
                return Optional.ofNullable(field.get(instance));
            } catch (ReflectiveOperationException var3) {
                PuzzlesLib.LOGGER.warn("Unable to get field {}", getFieldName(field), var3);
            }
        }
        return Optional.empty();
    }

    public static <T> boolean setValue(@Nullable Field field, Object instance, T value) {
        if (field != null) {
            try {
                field.set(instance, value);
                return true;
            } catch (ReflectiveOperationException var4) {
                PuzzlesLib.LOGGER.warn("Unable to set field {}", getFieldName(field), var4);
            }
        }
        return false;
    }

    public static <T, E> Optional<T> invokeMethod(Class<? super E> clazz, String name, Class<?>[] parameterTypes, E instance, Object[] args) {
        return invokeMethod(findMethod(clazz, name, parameterTypes), instance, args);
    }

    public static <T> Optional<T> invokeMethod(@Nullable Method method, Object instance, Object... args) {
        if (method != null) {
            try {
                return Optional.ofNullable(method.invoke(instance, args));
            } catch (ReflectiveOperationException var4) {
                PuzzlesLib.LOGGER.warn("Unable to invoke method {}", getMethodName(method), var4);
            }
        }
        return Optional.empty();
    }

    public static <T> Supplier<Optional<T>> newDefaultInstanceFactory(Class<T> clazz) {
        Constructor<?> constructor = null;
        for (Constructor<?> currentConstructor : clazz.getConstructors()) {
            if (constructor == null || currentConstructor.getParameterCount() < constructor.getParameterCount()) {
                constructor = currentConstructor;
            }
        }
        if (constructor != null) {
            Object[] args = Stream.of(constructor.getParameterTypes()).map(Defaults::defaultValue).toArray();
            return newInstanceFactory((Constructor<T>) constructor, args);
        } else {
            return Optional::empty;
        }
    }

    public static <T, E> Optional<T> newInstance(Class<? super E> clazz, Class<?>[] parameterTypes, Object[] args) {
        return newInstance(findConstructor(clazz, parameterTypes), args);
    }

    public static <T> Supplier<Optional<T>> newInstanceFactory(Class<T> clazz, Class<?>[] parameterTypes, Object[] args) {
        return newInstanceFactory(findConstructor(clazz, parameterTypes), args);
    }

    public static <T> Optional<T> newInstance(@Nullable Constructor<T> constructor, Object... args) {
        return (Optional<T>) newInstanceFactory(constructor, args).get();
    }

    public static <T> Supplier<Optional<T>> newInstanceFactory(@Nullable Constructor<T> constructor, Object... args) {
        if (constructor != null) {
            MethodHandle methodHandle;
            try {
                methodHandle = MethodHandles.publicLookup().unreflectConstructor(constructor);
            } catch (IllegalAccessException var4) {
                PuzzlesLib.LOGGER.warn("Unable to access constructor {}", getConstructorName(constructor), var4);
                return Optional::empty;
            }
            return () -> {
                try {
                    return Optional.of((Object) methodHandle.invoke(args));
                } catch (Throwable var4x) {
                    PuzzlesLib.LOGGER.warn("Unable to invoke constructor {}", getConstructorName(constructor), var4x);
                    return Optional.empty();
                }
            };
        } else {
            return Optional::empty;
        }
    }

    private static String getFieldName(@NotNull Field field) {
        Objects.requireNonNull(field, "Cannot get name for null field");
        return getClassMemberName(field.getDeclaringClass(), field.getName());
    }

    private static String getMethodName(@NotNull Method method) {
        Objects.requireNonNull(method, "Cannot get name for null method");
        return getMethodName(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }

    private static String getConstructorName(@NotNull Constructor<?> constructor) {
        Objects.requireNonNull(constructor, "Cannot get name for null constructor");
        return getConstructorName(constructor.getDeclaringClass(), constructor.getParameterTypes());
    }

    private static String getConstructorName(Class<?> clazz, Class<?>... parameterTypes) {
        return getConstructorName(clazz.getTypeName(), parameterTypes);
    }

    private static String getConstructorName(String typeName, Class<?>... parameterTypes) {
        return getMethodName(typeName, "<init>", parameterTypes);
    }

    private static String getMethodName(Class<?> clazz, String method, Class<?>... parameterTypes) {
        return getMethodName(clazz.getTypeName(), method, parameterTypes);
    }

    private static String getMethodName(String typeName, String method, Class<?>... parameterTypes) {
        return getClassMemberName(typeName, toMethodSignature(method, parameterTypes));
    }

    private static String toMethodSignature(String method, Class<?>... parameterTypes) {
        StringJoiner sj = new StringJoiner(",", method + "(", ")");
        for (Class<?> parameterType : parameterTypes) {
            sj.add(parameterType.getTypeName());
        }
        return sj.toString();
    }

    private static String getClassMemberName(Class<?> clazz, String member) {
        return getClassMemberName(clazz.getTypeName(), member);
    }

    private static String getClassMemberName(String typeName, String member) {
        return (typeName + "." + member).intern();
    }
}