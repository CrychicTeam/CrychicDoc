package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import java.util.Collection;

public class ScanPathToken extends PathToken {

    private static final ScanPathToken.Predicate FALSE_PREDICATE = new ScanPathToken.Predicate() {

        @Override
        public boolean matches(Object model) {
            return false;
        }
    };

    ScanPathToken() {
    }

    @Override
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        PathToken pt = this.next();
        walk(pt, currentPath, parent, model, ctx, createScanPredicate(pt, ctx));
    }

    public static void walk(PathToken pt, String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx, ScanPathToken.Predicate predicate) {
        if (ctx.jsonProvider().isMap(model)) {
            walkObject(pt, currentPath, parent, model, ctx, predicate);
        } else if (ctx.jsonProvider().isArray(model)) {
            walkArray(pt, currentPath, parent, model, ctx, predicate);
        }
    }

    public static void walkArray(PathToken pt, String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx, ScanPathToken.Predicate predicate) {
        if (predicate.matches(model)) {
            if (pt.isLeaf()) {
                pt.evaluate(currentPath, parent, model, ctx);
            } else {
                PathToken next = pt.next();
                Iterable<?> models = ctx.jsonProvider().toIterable(model);
                int idx = 0;
                for (Object evalModel : models) {
                    String evalPath = currentPath + "[" + idx + "]";
                    next.setUpstreamArrayIndex(idx);
                    next.evaluate(evalPath, parent, evalModel, ctx);
                    idx++;
                }
            }
        }
        Iterable<?> models = ctx.jsonProvider().toIterable(model);
        int idx = 0;
        for (Object evalModel : models) {
            String evalPath = currentPath + "[" + idx + "]";
            walk(pt, evalPath, PathRef.create(model, idx), evalModel, ctx, predicate);
            idx++;
        }
    }

    public static void walkObject(PathToken pt, String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx, ScanPathToken.Predicate predicate) {
        if (predicate.matches(model)) {
            pt.evaluate(currentPath, parent, model, ctx);
        }
        for (String property : ctx.jsonProvider().getPropertyKeys(model)) {
            String evalPath = currentPath + "['" + property + "']";
            Object propertyModel = ctx.jsonProvider().getMapValue(model, property);
            if (propertyModel != JsonProvider.UNDEFINED) {
                walk(pt, evalPath, PathRef.create(model, property), propertyModel, ctx, predicate);
            }
        }
    }

    private static ScanPathToken.Predicate createScanPredicate(PathToken target, EvaluationContextImpl ctx) {
        if (target instanceof PropertyPathToken) {
            return new ScanPathToken.PropertyPathTokenPredicate(target, ctx);
        } else if (target instanceof ArrayPathToken) {
            return new ScanPathToken.ArrayPathTokenPredicate(ctx);
        } else if (target instanceof WildcardPathToken) {
            return new ScanPathToken.WildcardPathTokenPredicate();
        } else {
            return (ScanPathToken.Predicate) (target instanceof PredicatePathToken ? new ScanPathToken.FilterPathTokenPredicate(target, ctx) : FALSE_PREDICATE);
        }
    }

    @Override
    public boolean isTokenDefinite() {
        return false;
    }

    @Override
    public String getPathFragment() {
        return "..";
    }

    private static final class ArrayPathTokenPredicate implements ScanPathToken.Predicate {

        private final EvaluationContextImpl ctx;

        private ArrayPathTokenPredicate(EvaluationContextImpl ctx) {
            this.ctx = ctx;
        }

        @Override
        public boolean matches(Object model) {
            return this.ctx.jsonProvider().isArray(model);
        }
    }

    private static final class FilterPathTokenPredicate implements ScanPathToken.Predicate {

        private final EvaluationContextImpl ctx;

        private PredicatePathToken predicatePathToken;

        private FilterPathTokenPredicate(PathToken target, EvaluationContextImpl ctx) {
            this.ctx = ctx;
            this.predicatePathToken = (PredicatePathToken) target;
        }

        @Override
        public boolean matches(Object model) {
            return this.predicatePathToken.accept(model, this.ctx.rootDocument(), this.ctx.configuration(), this.ctx);
        }
    }

    private interface Predicate {

        boolean matches(Object var1);
    }

    private static final class PropertyPathTokenPredicate implements ScanPathToken.Predicate {

        private final EvaluationContextImpl ctx;

        private PropertyPathToken propertyPathToken;

        private PropertyPathTokenPredicate(PathToken target, EvaluationContextImpl ctx) {
            this.ctx = ctx;
            this.propertyPathToken = (PropertyPathToken) target;
        }

        @Override
        public boolean matches(Object model) {
            if (!this.ctx.jsonProvider().isMap(model)) {
                return false;
            } else if (!this.propertyPathToken.isTokenDefinite()) {
                return true;
            } else if (this.propertyPathToken.isLeaf() && this.ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                return true;
            } else {
                Collection<String> keys = this.ctx.jsonProvider().getPropertyKeys(model);
                return keys.containsAll(this.propertyPathToken.getProperties());
            }
        }
    }

    private static final class WildcardPathTokenPredicate implements ScanPathToken.Predicate {

        @Override
        public boolean matches(Object model) {
            return true;
        }
    }
}