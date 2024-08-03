package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;

public class RootPathToken extends PathToken {

    private PathToken tail;

    private int tokenCount;

    private final String rootToken;

    RootPathToken(char rootToken) {
        this.rootToken = Character.toString(rootToken);
        this.tail = this;
        this.tokenCount = 1;
    }

    public PathToken getTail() {
        return this.tail;
    }

    @Override
    public int getTokenCount() {
        return this.tokenCount;
    }

    public RootPathToken append(PathToken next) {
        this.tail = this.tail.appendTailToken(next);
        this.tokenCount++;
        return this;
    }

    public PathTokenAppender getPathTokenAppender() {
        return new PathTokenAppender() {

            @Override
            public PathTokenAppender appendPathToken(PathToken next) {
                RootPathToken.this.append(next);
                return this;
            }
        };
    }

    @Override
    public void evaluate(String currentPath, PathRef pathRef, Object model, EvaluationContextImpl ctx) {
        if (this.isLeaf()) {
            PathRef op = ctx.forUpdate() ? pathRef : PathRef.NO_OP;
            ctx.addResult(this.rootToken, op, model);
        } else {
            this.next().evaluate(this.rootToken, pathRef, model, ctx);
        }
    }

    @Override
    public String getPathFragment() {
        return this.rootToken;
    }

    @Override
    public boolean isTokenDefinite() {
        return true;
    }

    public boolean isFunctionPath() {
        return this.tail instanceof FunctionPathToken;
    }

    public void setTail(PathToken token) {
        this.tail = token;
    }
}