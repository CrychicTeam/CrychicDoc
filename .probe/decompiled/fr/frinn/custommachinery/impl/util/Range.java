package fr.frinn.custommachinery.impl.util;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Range<T extends Comparable<T>> {

    private final List<Restriction<T>> restrictions;

    public Range(List<Restriction<T>> restrictions) {
        this.restrictions = restrictions;
    }

    public List<Restriction<T>> getRestrictions() {
        return this.restrictions;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        Iterator<Restriction<T>> i = this.restrictions.iterator();
        while (i.hasNext()) {
            Restriction<?> r = (Restriction<?>) i.next();
            buf.append(r.toString());
            if (i.hasNext()) {
                buf.append(',');
            }
        }
        return buf.toString();
    }

    public T match(List<T> things) {
        T matched = null;
        for (T thing : things) {
            if (this.contains(thing) && (matched == null || thing.compareTo(matched) > 0)) {
                matched = thing;
            }
        }
        return matched;
    }

    public boolean contains(T thing) {
        for (Restriction<T> restriction : this.restrictions) {
            if (restriction.contains(thing)) {
                return true;
            }
        }
        return false;
    }

    public String toFormattedString() {
        StringBuilder buf = new StringBuilder();
        Iterator<Restriction<T>> i = this.restrictions.iterator();
        while (i.hasNext()) {
            Restriction<?> r = (Restriction<?>) i.next();
            buf.append(r.toFormattedString());
            if (i.hasNext()) {
                buf.append(" or ");
            }
        }
        return buf.toString();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else {
            return obj instanceof Range<?> range ? Objects.equals(this.restrictions, range.restrictions) : false;
        }
    }

    public int hashCode() {
        int hash = 7;
        return 31 * hash + (this.restrictions == null ? 0 : this.restrictions.hashCode());
    }
}