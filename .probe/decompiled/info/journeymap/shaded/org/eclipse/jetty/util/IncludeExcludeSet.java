package info.journeymap.shaded.org.eclipse.jetty.util;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

public class IncludeExcludeSet<T, P> implements Predicate<P> {

    private final Set<T> _includes;

    private final Predicate<P> _includePredicate;

    private final Set<T> _excludes;

    private final Predicate<P> _excludePredicate;

    public IncludeExcludeSet() {
        this(HashSet.class);
    }

    public <SET extends Set<T>> IncludeExcludeSet(Class<SET> setClass) {
        try {
            this._includes = (Set<T>) setClass.newInstance();
            this._excludes = (Set<T>) setClass.newInstance();
            if (this._includes instanceof Predicate) {
                this._includePredicate = (Predicate<P>) this._includes;
            } else {
                this._includePredicate = new IncludeExcludeSet.SetContainsPredicate<>((Set<P>) this._includes);
            }
            if (this._excludes instanceof Predicate) {
                this._excludePredicate = (Predicate<P>) this._excludes;
            } else {
                this._excludePredicate = new IncludeExcludeSet.SetContainsPredicate<>((Set<P>) this._excludes);
            }
        } catch (IllegalAccessException | InstantiationException var3) {
            throw new RuntimeException(var3);
        }
    }

    public <SET extends Set<T>> IncludeExcludeSet(Set<T> includeSet, Predicate<P> includePredicate, Set<T> excludeSet, Predicate<P> excludePredicate) {
        Objects.requireNonNull(includeSet, "Include Set");
        Objects.requireNonNull(includePredicate, "Include Predicate");
        Objects.requireNonNull(excludeSet, "Exclude Set");
        Objects.requireNonNull(excludePredicate, "Exclude Predicate");
        this._includes = includeSet;
        this._includePredicate = includePredicate;
        this._excludes = excludeSet;
        this._excludePredicate = excludePredicate;
    }

    public void include(T element) {
        this._includes.add(element);
    }

    public void include(T... element) {
        for (T e : element) {
            this._includes.add(e);
        }
    }

    public void exclude(T element) {
        this._excludes.add(element);
    }

    public void exclude(T... element) {
        for (T e : element) {
            this._excludes.add(e);
        }
    }

    @Deprecated
    public boolean matches(P t) {
        return this.test(t);
    }

    public boolean test(P t) {
        return !this._includes.isEmpty() && !this._includePredicate.test(t) ? false : !this._excludePredicate.test(t);
    }

    public Boolean isIncludedAndNotExcluded(P t) {
        if (this._excludePredicate.test(t)) {
            return Boolean.FALSE;
        } else {
            return this._includePredicate.test(t) ? Boolean.TRUE : null;
        }
    }

    public boolean hasIncludes() {
        return !this._includes.isEmpty();
    }

    public int size() {
        return this._includes.size() + this._excludes.size();
    }

    public Set<T> getIncluded() {
        return this._includes;
    }

    public Set<T> getExcluded() {
        return this._excludes;
    }

    public void clear() {
        this._includes.clear();
        this._excludes.clear();
    }

    public String toString() {
        return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", this.getClass().getSimpleName(), this.hashCode(), this._includes, this._includePredicate, this._excludes, this._excludePredicate);
    }

    public boolean isEmpty() {
        return this._includes.isEmpty() && this._excludes.isEmpty();
    }

    private static class SetContainsPredicate<T> implements Predicate<T> {

        private final Set<T> set;

        public SetContainsPredicate(Set<T> set) {
            this.set = set;
        }

        public boolean test(T item) {
            return this.set.contains(item);
        }
    }
}