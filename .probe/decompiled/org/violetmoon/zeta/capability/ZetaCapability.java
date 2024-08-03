package org.violetmoon.zeta.capability;

public record ZetaCapability<T>(String id) {

    public boolean equals(Object that) {
        return this == that;
    }

    public int hashCode() {
        return System.identityHashCode(this);
    }
}