package de.keksuccino.konkrete.json.jsonpath;

import de.keksuccino.konkrete.json.jsonpath.internal.filter.FilterCompiler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public abstract class Filter implements Predicate {

    public static Filter filter(Predicate predicate) {
        return new Filter.SingleFilter(predicate);
    }

    public static Filter filter(Collection<Predicate> predicates) {
        return new Filter.AndFilter(predicates);
    }

    @Override
    public abstract boolean apply(Predicate.PredicateContext var1);

    public Filter or(Predicate other) {
        return new Filter.OrFilter(this, other);
    }

    public Filter and(Predicate other) {
        return new Filter.AndFilter(this, other);
    }

    public static Filter parse(String filter) {
        return FilterCompiler.compile(filter);
    }

    private static final class AndFilter extends Filter {

        private final Collection<Predicate> predicates;

        private AndFilter(Collection<Predicate> predicates) {
            this.predicates = predicates;
        }

        private AndFilter(Predicate left, Predicate right) {
            this(Arrays.asList(left, right));
        }

        @Override
        public Filter and(Predicate other) {
            Collection<Predicate> newPredicates = new ArrayList(this.predicates);
            newPredicates.add(other);
            return new Filter.AndFilter(newPredicates);
        }

        @Override
        public boolean apply(Predicate.PredicateContext ctx) {
            for (Predicate predicate : this.predicates) {
                if (!predicate.apply(ctx)) {
                    return false;
                }
            }
            return true;
        }

        public String toString() {
            Iterator<Predicate> i = this.predicates.iterator();
            StringBuilder sb = new StringBuilder();
            sb.append("[?(");
            while (i.hasNext()) {
                String p = ((Predicate) i.next()).toString();
                if (p.startsWith("[?(")) {
                    p = p.substring(3, p.length() - 2);
                }
                sb.append(p);
                if (i.hasNext()) {
                    sb.append(" && ");
                }
            }
            sb.append(")]");
            return sb.toString();
        }
    }

    private static final class OrFilter extends Filter {

        private final Predicate left;

        private final Predicate right;

        private OrFilter(Predicate left, Predicate right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public Filter and(Predicate other) {
            return new Filter.OrFilter(this.left, new Filter.AndFilter(this.right, other));
        }

        @Override
        public boolean apply(Predicate.PredicateContext ctx) {
            boolean a = this.left.apply(ctx);
            return a || this.right.apply(ctx);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[?(");
            String l = this.left.toString();
            String r = this.right.toString();
            if (l.startsWith("[?(")) {
                l = l.substring(3, l.length() - 2);
            }
            if (r.startsWith("[?(")) {
                r = r.substring(3, r.length() - 2);
            }
            sb.append(l).append(" || ").append(r);
            sb.append(")]");
            return sb.toString();
        }
    }

    private static final class SingleFilter extends Filter {

        private final Predicate predicate;

        private SingleFilter(Predicate predicate) {
            this.predicate = predicate;
        }

        @Override
        public boolean apply(Predicate.PredicateContext ctx) {
            return this.predicate.apply(ctx);
        }

        public String toString() {
            String predicateString = this.predicate.toString();
            return predicateString.startsWith("(") ? "[?" + predicateString + "]" : "[?(" + predicateString + ")]";
        }
    }
}