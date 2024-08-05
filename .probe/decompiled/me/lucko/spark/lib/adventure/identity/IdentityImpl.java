package me.lucko.spark.lib.adventure.identity;

import java.util.UUID;
import me.lucko.spark.lib.adventure.examination.Examinable;
import me.lucko.spark.lib.adventure.internal.Internals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class IdentityImpl implements Examinable, Identity {

    private final UUID uuid;

    IdentityImpl(final UUID uuid) {
        this.uuid = uuid;
    }

    @NotNull
    @Override
    public UUID uuid() {
        return this.uuid;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof Identity)) {
            return false;
        } else {
            Identity that = (Identity) other;
            return this.uuid.equals(that.uuid());
        }
    }

    public int hashCode() {
        return this.uuid.hashCode();
    }
}