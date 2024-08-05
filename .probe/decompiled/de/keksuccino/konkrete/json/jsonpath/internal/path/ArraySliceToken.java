package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArraySliceToken extends ArrayPathToken {

    private static final Logger logger = LoggerFactory.getLogger(ArraySliceToken.class);

    private final ArraySliceOperation operation;

    ArraySliceToken(ArraySliceOperation operation) {
        this.operation = operation;
    }

    @Override
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (this.checkArrayModel(currentPath, model, ctx)) {
            switch(this.operation.operation()) {
                case SLICE_FROM:
                    this.sliceFrom(currentPath, parent, model, ctx);
                    break;
                case SLICE_BETWEEN:
                    this.sliceBetween(currentPath, parent, model, ctx);
                    break;
                case SLICE_TO:
                    this.sliceTo(currentPath, parent, model, ctx);
            }
        }
    }

    private void sliceFrom(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        int length = ctx.jsonProvider().length(model);
        int from = this.operation.from();
        if (from < 0) {
            from += length;
        }
        from = Math.max(0, from);
        logger.debug("Slice from index on array with length: {}. From index: {} to: {}. Input: {}", new Object[] { length, from, length - 1, this.toString() });
        if (length != 0 && from < length) {
            for (int i = from; i < length; i++) {
                this.handleArrayIndex(i, currentPath, model, ctx);
            }
        }
    }

    private void sliceBetween(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        int length = ctx.jsonProvider().length(model);
        int from = this.operation.from();
        int to = this.operation.to();
        to = Math.min(length, to);
        if (from < to && length != 0) {
            logger.debug("Slice between indexes on array with length: {}. From index: {} to: {}. Input: {}", new Object[] { length, from, to, this.toString() });
            for (int i = from; i < to; i++) {
                this.handleArrayIndex(i, currentPath, model, ctx);
            }
        }
    }

    private void sliceTo(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        int length = ctx.jsonProvider().length(model);
        if (length != 0) {
            int to = this.operation.to();
            if (to < 0) {
                to += length;
            }
            to = Math.min(length, to);
            logger.debug("Slice to index on array with length: {}. From index: 0 to: {}. Input: {}", new Object[] { length, to, this.toString() });
            for (int i = 0; i < to; i++) {
                this.handleArrayIndex(i, currentPath, model, ctx);
            }
        }
    }

    @Override
    public String getPathFragment() {
        return this.operation.toString();
    }

    @Override
    public boolean isTokenDefinite() {
        return false;
    }
}