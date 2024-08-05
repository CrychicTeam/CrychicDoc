package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import de.keksuccino.konkrete.json.jsonpath.internal.function.PathFunction;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import java.util.List;

public abstract class PathToken {

    private PathToken prev;

    private PathToken next;

    private Boolean definite = null;

    private Boolean upstreamDefinite = null;

    private int upstreamArrayIndex = -1;

    public void setUpstreamArrayIndex(int idx) {
        this.upstreamArrayIndex = idx;
    }

    PathToken appendTailToken(PathToken next) {
        this.next = next;
        this.next.prev = this;
        return next;
    }

    void handleObjectProperty(String currentPath, Object model, EvaluationContextImpl ctx, List<String> properties) {
        if (properties.size() == 1) {
            String property = (String) properties.get(0);
            String evalPath = Utils.concat(currentPath, "['", property, "']");
            Object propertyVal = readObjectProperty(property, model, ctx);
            if (propertyVal == JsonProvider.UNDEFINED) {
                assert this instanceof PropertyPathToken : "only PropertyPathToken is supported";
                if (!this.isLeaf()) {
                    if ((this.isUpstreamDefinite() && this.isTokenDefinite() || ctx.options().contains(Option.REQUIRE_PROPERTIES)) && !ctx.options().contains(Option.SUPPRESS_EXCEPTIONS)) {
                        throw new PathNotFoundException("Missing property in path " + evalPath);
                    }
                    return;
                }
                if (!ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                    if (!ctx.options().contains(Option.SUPPRESS_EXCEPTIONS) && ctx.options().contains(Option.REQUIRE_PROPERTIES)) {
                        throw new PathNotFoundException("No results for path: " + evalPath);
                    }
                    return;
                }
                propertyVal = null;
            }
            PathRef pathRef = ctx.forUpdate() ? PathRef.create(model, property) : PathRef.NO_OP;
            if (this.isLeaf()) {
                String idx = "[" + this.upstreamArrayIndex + "]";
                if (idx.equals("[-1]") || ctx.getRoot().getTail().prev().getPathFragment().equals(idx)) {
                    ctx.addResult(evalPath, pathRef, propertyVal);
                }
            } else {
                this.next().evaluate(evalPath, pathRef, propertyVal, ctx);
            }
        } else {
            String evalPathx = currentPath + "[" + Utils.join(", ", "'", properties) + "]";
            assert this.isLeaf() : "non-leaf multi props handled elsewhere";
            Object merged = ctx.jsonProvider().createMap();
            for (String propertyx : properties) {
                Object propertyValx;
                if (hasProperty(propertyx, model, ctx)) {
                    propertyValx = readObjectProperty(propertyx, model, ctx);
                    if (propertyValx == JsonProvider.UNDEFINED) {
                        if (!ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                            continue;
                        }
                        propertyValx = null;
                    }
                } else {
                    if (!ctx.options().contains(Option.DEFAULT_PATH_LEAF_TO_NULL)) {
                        if (ctx.options().contains(Option.REQUIRE_PROPERTIES)) {
                            throw new PathNotFoundException("Missing property in path " + evalPathx);
                        }
                        continue;
                    }
                    propertyValx = null;
                }
                ctx.jsonProvider().setProperty(merged, propertyx, propertyValx);
            }
            PathRef pathRef = ctx.forUpdate() ? PathRef.create(model, properties) : PathRef.NO_OP;
            ctx.addResult(evalPathx, pathRef, merged);
        }
    }

    private static boolean hasProperty(String property, Object model, EvaluationContextImpl ctx) {
        return ctx.jsonProvider().getPropertyKeys(model).contains(property);
    }

    private static Object readObjectProperty(String property, Object model, EvaluationContextImpl ctx) {
        return ctx.jsonProvider().getMapValue(model, property);
    }

    protected void handleArrayIndex(int index, String currentPath, Object model, EvaluationContextImpl ctx) {
        String evalPath = Utils.concat(currentPath, "[", String.valueOf(index), "]");
        PathRef pathRef = ctx.forUpdate() ? PathRef.create(model, index) : PathRef.NO_OP;
        int effectiveIndex = index < 0 ? ctx.jsonProvider().length(model) + index : index;
        try {
            Object evalHit = ctx.jsonProvider().getArrayIndex(model, effectiveIndex);
            if (this.isLeaf()) {
                ctx.addResult(evalPath, pathRef, evalHit);
            } else {
                this.next().evaluate(evalPath, pathRef, evalHit, ctx);
            }
        } catch (IndexOutOfBoundsException var9) {
        }
    }

    PathToken prev() {
        return this.prev;
    }

    PathToken next() {
        if (this.isLeaf()) {
            throw new IllegalStateException("Current path token is a leaf");
        } else {
            return this.next;
        }
    }

    boolean isLeaf() {
        return this.next == null;
    }

    boolean isRoot() {
        return this.prev == null;
    }

    boolean isUpstreamDefinite() {
        if (this.upstreamDefinite == null) {
            this.upstreamDefinite = this.isRoot() || this.prev.isTokenDefinite() && this.prev.isUpstreamDefinite();
        }
        return this.upstreamDefinite;
    }

    public int getTokenCount() {
        int cnt = 1;
        for (PathToken token = this; !token.isLeaf(); cnt++) {
            token = token.next();
        }
        return cnt;
    }

    public boolean isPathDefinite() {
        if (this.definite != null) {
            return this.definite;
        } else {
            boolean isDefinite = this.isTokenDefinite();
            if (isDefinite && !this.isLeaf()) {
                isDefinite = this.next.isPathDefinite();
            }
            this.definite = isDefinite;
            return isDefinite;
        }
    }

    public String toString() {
        return this.isLeaf() ? this.getPathFragment() : this.getPathFragment() + this.next().toString();
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void invoke(PathFunction pathFunction, String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        ctx.addResult(currentPath, parent, pathFunction.invoke(currentPath, parent, model, ctx, null));
    }

    public abstract void evaluate(String var1, PathRef var2, Object var3, EvaluationContextImpl var4);

    public abstract boolean isTokenDefinite();

    protected abstract String getPathFragment();

    public void setNext(PathToken next) {
        this.next = next;
    }

    public PathToken getNext() {
        return this.next;
    }
}