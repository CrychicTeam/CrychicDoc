package icyllis.arc3d.core;

import java.util.Objects;
import java.util.Map.Entry;
import javax.annotation.Nonnull;

public class Pair<L, R> implements Entry<L, R> {

    L left;

    R right;

    public Pair() {
    }

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    @Nonnull
    public static <L, R> Pair<L, R> of(L left, R right) {
        return new Pair<>(left, right);
    }

    @Nonnull
    public static <L, R> Pair<L, R> of(Entry<L, R> entry) {
        return entry == null ? new Pair<>() : new Pair<>((L) entry.getKey(), (R) entry.getValue());
    }

    public final L getKey() {
        return this.left;
    }

    public final R getValue() {
        return this.right;
    }

    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }

    public final L getLeft() {
        return this.left;
    }

    public final R getRight() {
        return this.right;
    }

    public final L getFirst() {
        return this.left;
    }

    public final R getSecond() {
        return this.right;
    }

    public int hashCode() {
        return Objects.hashCode(this.left) ^ Objects.hashCode(this.right);
    }

    public boolean equals(Object o) {
        return !(o instanceof Entry<?, ?> e) ? false : Objects.equals(this.left, e.getKey()) && Objects.equals(this.right, e.getValue());
    }

    public String toString() {
        return "(" + this.left + "," + this.right + ")";
    }
}