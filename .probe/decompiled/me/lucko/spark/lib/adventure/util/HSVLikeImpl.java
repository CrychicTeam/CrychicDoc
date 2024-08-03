package me.lucko.spark.lib.adventure.util;

import java.util.Objects;
import me.lucko.spark.lib.adventure.internal.Internals;
import org.jetbrains.annotations.Nullable;

final class HSVLikeImpl implements HSVLike {

    private final float h;

    private final float s;

    private final float v;

    HSVLikeImpl(final float h, final float s, final float v) {
        requireInsideRange(h, "h");
        requireInsideRange(s, "s");
        requireInsideRange(v, "v");
        this.h = h;
        this.s = s;
        this.v = v;
    }

    @Override
    public float h() {
        return this.h;
    }

    @Override
    public float s() {
        return this.s;
    }

    @Override
    public float v() {
        return this.v;
    }

    private static void requireInsideRange(final float number, final String name) throws IllegalArgumentException {
        if (number < 0.0F || 1.0F < number) {
            throw new IllegalArgumentException(name + " (" + number + ") is not inside the required range: [0,1]");
        }
    }

    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        } else if (!(other instanceof HSVLikeImpl)) {
            return false;
        } else {
            HSVLikeImpl that = (HSVLikeImpl) other;
            return ShadyPines.equals(that.h, this.h) && ShadyPines.equals(that.s, this.s) && ShadyPines.equals(that.v, this.v);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.h, this.s, this.v });
    }

    public String toString() {
        return Internals.toString(this);
    }
}