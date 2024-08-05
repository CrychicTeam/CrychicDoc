package com.corosus.coroutil.util;

import com.google.common.base.Preconditions;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.StringJoiner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ReflectionHelper {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final Marker REFLECTION = MarkerManager.getMarker("REFLECTION");

    @Nullable
    public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String fieldName) {
        try {
            return (T) findField(classToAccess, fieldName).get(instance);
        } catch (ReflectionHelper.UnableToFindFieldException var4) {
            LOGGER.error(REFLECTION, "Unable to locate field {} ({}) on type {}", fieldName, fieldName, classToAccess.getName(), var4);
            throw var4;
        } catch (IllegalAccessException var5) {
            LOGGER.error(REFLECTION, "Unable to access field {} ({}) on type {}", fieldName, fieldName, classToAccess.getName(), var5);
            throw new ReflectionHelper.UnableToAccessFieldException(var5);
        }
    }

    public static <T, E> void setPrivateValue(@NotNull Class<? super T> classToAccess, @NotNull T instance, @Nullable E value, @NotNull String fieldName) {
        try {
            findField(classToAccess, fieldName).set(instance, value);
        } catch (ReflectionHelper.UnableToFindFieldException var5) {
            LOGGER.error("Unable to locate any field {} on type {}", fieldName, classToAccess.getName(), var5);
            throw var5;
        } catch (IllegalAccessException var6) {
            LOGGER.error("Unable to set any field {} on type {}", fieldName, classToAccess.getName(), var6);
            throw new ReflectionHelper.UnableToAccessFieldException(var6);
        }
    }

    @NotNull
    public static Method findMethod(@NotNull Class<?> clazz, @NotNull String methodName, @NotNull Class<?>... parameterTypes) {
        Preconditions.checkNotNull(clazz, "Class to find method on cannot be null.");
        Preconditions.checkNotNull(methodName, "Name of method to find cannot be null.");
        Preconditions.checkArgument(!methodName.isEmpty(), "Name of method to find cannot be empty.");
        Preconditions.checkNotNull(parameterTypes, "Parameter types of method to find cannot be null.");
        try {
            Method m = clazz.getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m;
        } catch (Exception var4) {
            throw new ReflectionHelper.UnableToFindMethodException(var4);
        }
    }

    @NotNull
    public static <T> Constructor<T> findConstructor(@NotNull Class<T> clazz, @NotNull Class<?>... parameterTypes) {
        Preconditions.checkNotNull(clazz, "Class to find constructor on cannot be null.");
        Preconditions.checkNotNull(parameterTypes, "Parameter types of constructor to find cannot be null.");
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException var9) {
            StringBuilder desc = new StringBuilder();
            desc.append(clazz.getSimpleName());
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (Class<?> type : parameterTypes) {
                joiner.add(type.getSimpleName());
            }
            desc.append(joiner);
            throw new ReflectionHelper.UnknownConstructorException("Could not find constructor '" + desc.toString() + "' in " + clazz);
        }
    }

    @NotNull
    public static <T> Field findField(@NotNull Class<? super T> clazz, @NotNull String fieldName) {
        Preconditions.checkNotNull(clazz, "Class to find field on cannot be null.");
        Preconditions.checkNotNull(fieldName, "Name of field to find cannot be null.");
        Preconditions.checkArgument(!fieldName.isEmpty(), "Name of field to find cannot be empty.");
        try {
            Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (Exception var3) {
            throw new ReflectionHelper.UnableToFindFieldException(var3);
        }
    }

    public static class UnableToAccessFieldException extends RuntimeException {

        private UnableToAccessFieldException(Exception e) {
            super(e);
        }
    }

    public static class UnableToFindFieldException extends RuntimeException {

        private UnableToFindFieldException(Exception e) {
            super(e);
        }
    }

    public static class UnableToFindMethodException extends RuntimeException {

        public UnableToFindMethodException(Throwable failed) {
            super(failed);
        }
    }

    public static class UnknownConstructorException extends RuntimeException {

        public UnknownConstructorException(String message) {
            super(message);
        }
    }
}