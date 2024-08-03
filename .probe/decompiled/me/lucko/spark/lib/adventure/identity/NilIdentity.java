package me.lucko.spark.lib.adventure.identity;

import java.util.UUID;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class NilIdentity implements Identity {

    static final UUID NIL_UUID = new UUID(0L, 0L);

    static final Identity INSTANCE = new NilIdentity();

    @NotNull
    @Override
    public UUID uuid() {
        return NIL_UUID;
    }

    public String toString() {
        return "Identity.nil()";
    }

    public boolean equals(@Nullable final Object that) {
        return this == that;
    }

    public int hashCode() {
        return 0;
    }
}