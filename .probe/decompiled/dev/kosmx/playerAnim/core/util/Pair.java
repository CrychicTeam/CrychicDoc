package dev.kosmx.playerAnim.core.util;

import java.util.Objects;
import javax.annotation.concurrent.Immutable;

@Immutable
public class Pair<L, R> {

    final L left;

    final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return this.left;
    }

    public R getRight() {
        return this.right;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Pair)) {
            return false;
        } else {
            Pair o2 = (Pair) o;
            return Objects.equals(this.left, o2.left) && Objects.equals(this.right, o2.right);
        }
    }

    public int hashCode() {
        int hash = 0;
        hash = hash * 31 + (this.left == null ? 0 : this.left.hashCode());
        return hash * 31 + (this.right == null ? 0 : this.right.hashCode());
    }

    public String toString() {
        return "Pair{left=" + this.left + ", right=" + this.right + '}';
    }
}