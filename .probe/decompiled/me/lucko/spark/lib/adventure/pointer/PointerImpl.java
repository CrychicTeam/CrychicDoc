package me.lucko.spark.lib.adventure.pointer;

import me.lucko.spark.lib.adventure.internal.Internals;
import me.lucko.spark.lib.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class PointerImpl<T> implements Pointer<T> {

    private final Class<T> type;

    private final Key key;

    PointerImpl(final Class<T> type, final Key key) {
        this.type = type;
        this.key = key;
    }

    @NotNull
    @Override
    public Class<T> type() {
        return this.type;
    }

    @NotNull
    @Override
    public Key key() {
        return this.key;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            PointerImpl<?> that = (PointerImpl<?>) other;
            return this.type.equals(that.type) && this.key.equals(that.key);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.type.hashCode();
        return 31 * result + this.key.hashCode();
    }
}