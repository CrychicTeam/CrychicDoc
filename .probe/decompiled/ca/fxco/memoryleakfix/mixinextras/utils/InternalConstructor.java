package ca.fxco.memoryleakfix.mixinextras.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;

interface InternalConstructor<T> {

    T newInstance(Object... var1);

    static <T> InternalConstructor<T> of(Class<?> clazz, Class<?>... argTypes) {
        Constructor<T> impl;
        try {
            impl = clazz.getDeclaredConstructor(argTypes);
        } catch (NoSuchMethodException var4) {
            throw new RuntimeException(String.format("Failed to find constructor %s(%s)! Please report to LlamaLad7!", clazz, Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", "))), var4);
        }
        impl.setAccessible(true);
        return args -> {
            try {
                return (T) impl.newInstance(args);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException var5) {
                throw new RuntimeException(String.format("Failed to construct %s(%s) with args [%s]! Please report to LlamaLad7!", clazz, Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", ")), Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "))), var5);
            }
        };
    }

    static <T> InternalConstructor<T> of(String clazz, Class<?>... argTypes) {
        try {
            return of(Class.forName(clazz), argTypes);
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException(String.format("Failed to find class %s! Please report to LlamaLad7!", clazz), var3);
        }
    }
}