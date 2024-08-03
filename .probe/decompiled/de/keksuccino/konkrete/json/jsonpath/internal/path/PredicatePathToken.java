package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import java.util.Collection;
import java.util.Collections;

public class PredicatePathToken extends PathToken {

    private final Collection<Predicate> predicates;

    PredicatePathToken(Predicate filter) {
        this.predicates = Collections.singletonList(filter);
    }

    PredicatePathToken(Collection<Predicate> predicates) {
        this.predicates = predicates;
    }

    @Override
    public void evaluate(String currentPath, PathRef ref, Object model, EvaluationContextImpl ctx) {
        if (ctx.jsonProvider().isMap(model)) {
            if (this.accept(model, ctx.rootDocument(), ctx.configuration(), ctx)) {
                PathRef op = ctx.forUpdate() ? ref : PathRef.NO_OP;
                if (this.isLeaf()) {
                    ctx.addResult(currentPath, op, model);
                } else {
                    this.next().evaluate(currentPath, op, model, ctx);
                }
            }
        } else if (ctx.jsonProvider().isArray(model)) {
            int idx = 0;
            for (Object idxModel : ctx.jsonProvider().toIterable(model)) {
                if (this.accept(idxModel, ctx.rootDocument(), ctx.configuration(), ctx)) {
                    this.handleArrayIndex(idx, currentPath, model, ctx);
                }
                idx++;
            }
        } else if (this.isUpstreamDefinite()) {
            throw new InvalidPathException(String.format("Filter: %s can not be applied to primitives. Current context is: %s", this.toString(), model));
        }
    }

    public boolean accept(Object obj, Object root, Configuration configuration, EvaluationContextImpl evaluationContext) {
        Predicate.PredicateContext ctx = new PredicateContextImpl(obj, root, configuration, evaluationContext.documentEvalCache());
        for (Predicate predicate : this.predicates) {
            try {
                if (!predicate.apply(ctx)) {
                    return false;
                }
            } catch (InvalidPathException var9) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String getPathFragment() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.predicates.size(); i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append("?");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean isTokenDefinite() {
        return false;
    }
}