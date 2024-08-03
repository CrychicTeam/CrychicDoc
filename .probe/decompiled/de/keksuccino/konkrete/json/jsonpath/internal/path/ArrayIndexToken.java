package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;

public class ArrayIndexToken extends ArrayPathToken {

    private final ArrayIndexOperation arrayIndexOperation;

    ArrayIndexToken(ArrayIndexOperation arrayIndexOperation) {
        this.arrayIndexOperation = arrayIndexOperation;
    }

    @Override
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (this.checkArrayModel(currentPath, model, ctx)) {
            if (this.arrayIndexOperation.isSingleIndexOperation()) {
                this.handleArrayIndex((Integer) this.arrayIndexOperation.indexes().get(0), currentPath, model, ctx);
            } else {
                for (Integer index : this.arrayIndexOperation.indexes()) {
                    this.handleArrayIndex(index, currentPath, model, ctx);
                }
            }
        }
    }

    @Override
    public String getPathFragment() {
        return this.arrayIndexOperation.toString();
    }

    @Override
    public boolean isTokenDefinite() {
        return this.arrayIndexOperation.isSingleIndexOperation();
    }
}