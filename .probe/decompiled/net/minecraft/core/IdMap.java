package net.minecraft.core;

import javax.annotation.Nullable;

public interface IdMap<T> extends Iterable<T> {

    int DEFAULT = -1;

    int getId(T var1);

    @Nullable
    T byId(int var1);

    default T byIdOrThrow(int int0) {
        T $$1 = this.byId(int0);
        if ($$1 == null) {
            throw new IllegalArgumentException("No value with id " + int0);
        } else {
            return $$1;
        }
    }

    int size();
}