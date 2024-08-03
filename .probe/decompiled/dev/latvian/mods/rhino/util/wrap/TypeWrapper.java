package dev.latvian.mods.rhino.util.wrap;

import java.util.function.Predicate;

public class TypeWrapper<T> {

    public static final Predicate<Object> ALWAYS_VALID = o -> true;

    public final Class<T> target;

    public final Predicate<Object> validator;

    public final TypeWrapperFactory<T> factory;

    TypeWrapper(Class<T> t, Predicate<Object> v, TypeWrapperFactory<T> f) {
        this.target = t;
        this.validator = v;
        this.factory = f;
    }
}