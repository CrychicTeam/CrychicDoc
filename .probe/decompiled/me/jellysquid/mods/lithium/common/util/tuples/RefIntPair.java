package me.jellysquid.mods.lithium.common.util.tuples;

import java.util.Objects;

public record RefIntPair<A>(A left, int right) {

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj != null && obj.getClass() == this.getClass()) {
            RefIntPair<?> that = (RefIntPair<?>) obj;
            return this.left == that.left && this.right == that.right;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { System.identityHashCode(this.left), this.right });
    }

    public String toString() {
        return "RefIntPair[left=" + this.left + ", right=" + this.right + "]";
    }
}