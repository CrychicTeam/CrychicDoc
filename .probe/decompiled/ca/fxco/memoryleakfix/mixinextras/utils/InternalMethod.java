package ca.fxco.memoryleakfix.mixinextras.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

interface InternalMethod<O, R> {

    R call(O var1, Object... var2);

    static <O, R> InternalMethod<O, R> of(Class<?> clazz, String name, Class<?>... argTypes) {
        Method impl;
        try {
            impl = clazz.getDeclaredMethod(name, argTypes);
        } catch (NoSuchMethodException var5) {
            throw new RuntimeException(String.format("Failed to find method %s::%s(%s)! Please report to LlamaLad7!", clazz, name, Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", "))), var5);
        }
        impl.setAccessible(true);
        return (owner, args) -> {
            try {
                return (R) impl.invoke(owner, args);
            } catch (InvocationTargetException | IllegalAccessException var7) {
                throw new RuntimeException(String.format("Failed to call %s::%s(%s) with args [%s]! Please report to LlamaLad7!", clazz, name, Arrays.stream(argTypes).map(Class::getName).collect(Collectors.joining(", ")), Arrays.stream(args).map(Object::toString).collect(Collectors.joining(", "))), var7);
            }
        };
    }

    static <O, R> InternalMethod<O, R> of(String clazz, String name, Class<?>... argTypes) {
        try {
            return of(Class.forName(clazz), name, argTypes);
        } catch (ClassNotFoundException var4) {
            throw new RuntimeException(String.format("Failed to find class %s! Please report to LlamaLad7!", clazz), var4);
        }
    }
}