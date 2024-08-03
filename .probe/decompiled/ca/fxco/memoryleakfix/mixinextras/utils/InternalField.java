package ca.fxco.memoryleakfix.mixinextras.utils;

import java.lang.reflect.Field;

interface InternalField<O, T> {

    T get(O var1);

    void set(O var1, T var2);

    static <O, T> InternalField<O, T> of(Class<?> clazz, String name) {
        final Field impl;
        try {
            impl = clazz.getDeclaredField(name);
        } catch (NoSuchFieldException var4) {
            throw new RuntimeException(String.format("Failed to find field %s::%s! Please report to LlamaLad7!", clazz, name), var4);
        }
        impl.setAccessible(true);
        return new InternalField<O, T>() {

            @Override
            public T get(O owner) {
                try {
                    return (T) impl.get(owner);
                } catch (IllegalAccessException var3) {
                    throw new RuntimeException(String.format("Failed to get %s::%s on %s! Please report to LlamaLad7!", clazz, name, owner), var3);
                }
            }

            @Override
            public void set(O owner, T newValue) {
                try {
                    impl.set(owner, newValue);
                } catch (IllegalAccessException var4) {
                    throw new RuntimeException(String.format("Failed to set %s::%s to %s on %s! Please report to LlamaLad7!", clazz, name, newValue, owner), var4);
                }
            }
        };
    }

    static <O, T> InternalField<O, T> of(String clazz, String name) {
        try {
            return of(Class.forName(clazz), name);
        } catch (ClassNotFoundException var3) {
            throw new RuntimeException(String.format("Failed to find class %s! Please report to LlamaLad7!", clazz), var3);
        }
    }
}