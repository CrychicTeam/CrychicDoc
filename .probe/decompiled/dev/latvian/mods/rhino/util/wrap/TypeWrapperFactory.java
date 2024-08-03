package dev.latvian.mods.rhino.util.wrap;

import dev.latvian.mods.rhino.Context;

@FunctionalInterface
public interface TypeWrapperFactory<T> {

    T wrap(Context var1, Object var2);

    public interface Simple<T> extends TypeWrapperFactory<T> {

        T wrapSimple(Object var1);

        @Override
        default T wrap(Context cx, Object o) {
            return this.wrapSimple(o);
        }
    }
}