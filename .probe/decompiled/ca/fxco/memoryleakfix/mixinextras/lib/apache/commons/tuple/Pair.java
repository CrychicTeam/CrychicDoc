package ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.tuple;

import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.ObjectUtils;
import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.builder.CompareToBuilder;
import java.io.Serializable;
import java.util.Map.Entry;

public abstract class Pair<L, R> implements Serializable, Comparable<Pair<L, R>>, Entry<L, R> {

    public static <L, R> Pair<L, R> of(L left, R right) {
        return new ImmutablePair<>(left, right);
    }

    public abstract L getLeft();

    public abstract R getRight();

    public final L getKey() {
        return this.getLeft();
    }

    public R getValue() {
        return this.getRight();
    }

    public int compareTo(Pair<L, R> other) {
        return new CompareToBuilder().append(this.getLeft(), other.getLeft()).append(this.getRight(), other.getRight()).toComparison();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof Entry)) {
            return false;
        } else {
            Entry<?, ?> other = (Entry<?, ?>) obj;
            return ObjectUtils.equals(this.getKey(), other.getKey()) && ObjectUtils.equals(this.getValue(), other.getValue());
        }
    }

    public int hashCode() {
        return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^ (this.getValue() == null ? 0 : this.getValue().hashCode());
    }

    public String toString() {
        return "" + '(' + this.getLeft() + ',' + this.getRight() + ')';
    }
}