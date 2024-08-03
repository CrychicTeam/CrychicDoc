package net.caffeinemc.mods.sodium.api.internal;

import java.lang.reflect.Constructor;

public class DependencyInjection {

    public static <T> T load(Class<T> apiClass, String implClassName) {
        Class<?> implClass;
        try {
            implClass = Class.forName(implClassName);
        } catch (ReflectiveOperationException var8) {
            throw new RuntimeException("Could not find implementation", var8);
        }
        if (!apiClass.isAssignableFrom(implClass)) {
            throw new RuntimeException("Class %s does not implement interface %s".formatted(implClass.getName(), apiClass.getName()));
        } else {
            Constructor<?> implConstructor;
            try {
                implConstructor = implClass.getConstructor();
            } catch (ReflectiveOperationException var7) {
                throw new RuntimeException("Could not find default constructor", var7);
            }
            Object implInstance;
            try {
                implInstance = implConstructor.newInstance();
            } catch (ReflectiveOperationException var6) {
                throw new RuntimeException("Could not instantiate implementation", var6);
            }
            return (T) apiClass.cast(implInstance);
        }
    }
}