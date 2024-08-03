package me.lucko.spark.lib.adventure.text.format;

import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TextDecorationAndStateImpl implements TextDecorationAndState {

    private final TextDecoration decoration;

    private final TextDecoration.State state;

    TextDecorationAndStateImpl(final TextDecoration decoration, final TextDecoration.State state) {
        this.decoration = decoration;
        this.state = (TextDecoration.State) Objects.requireNonNull(state, "state");
    }

    @NotNull
    @Override
    public TextDecoration decoration() {
        return this.decoration;
    }

    @NotNull
    @Override
    public TextDecoration.State state() {
        return this.state;
    }

    public String toString() {
        return Internals.toString(this);
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (other != null && this.getClass() == other.getClass()) {
            TextDecorationAndStateImpl that = (TextDecorationAndStateImpl) other;
            return this.decoration == that.decoration && this.state == that.state;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.decoration.hashCode();
        return 31 * result + this.state.hashCode();
    }
}