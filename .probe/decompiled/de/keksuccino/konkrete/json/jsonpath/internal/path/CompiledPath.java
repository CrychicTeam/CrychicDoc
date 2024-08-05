package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationAbortException;
import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.function.ParamType;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompiledPath implements Path {

    private static final Logger logger = LoggerFactory.getLogger(CompiledPath.class);

    private final RootPathToken root;

    private final boolean isRootPath;

    public CompiledPath(RootPathToken root, boolean isRootPath) {
        this.root = this.invertScannerFunctionRelationship(root);
        this.isRootPath = isRootPath;
    }

    @Override
    public boolean isRootPath() {
        return this.isRootPath;
    }

    private RootPathToken invertScannerFunctionRelationship(RootPathToken path) {
        if (path.isFunctionPath() && path.next() instanceof ScanPathToken) {
            PathToken token = path;
            PathToken prior = null;
            while (null != (token = token.next()) && !(token instanceof FunctionPathToken)) {
                prior = token;
            }
            if (token instanceof FunctionPathToken) {
                prior.setNext(null);
                path.setTail(prior);
                Parameter parameter = new Parameter();
                parameter.setPath(new CompiledPath(path, true));
                parameter.setType(ParamType.PATH);
                ((FunctionPathToken) token).setParameters(Arrays.asList(parameter));
                RootPathToken functionRoot = new RootPathToken('$');
                functionRoot.setTail(token);
                functionRoot.setNext(token);
                return functionRoot;
            }
        }
        return path;
    }

    @Override
    public EvaluationContext evaluate(Object document, Object rootDocument, Configuration configuration, boolean forUpdate) {
        if (logger.isDebugEnabled()) {
            logger.debug("Evaluating path: {}", this.toString());
        }
        EvaluationContextImpl ctx = new EvaluationContextImpl(this, rootDocument, configuration, forUpdate);
        try {
            PathRef op = ctx.forUpdate() ? PathRef.createRoot(rootDocument) : PathRef.NO_OP;
            this.root.evaluate("", op, document, ctx);
        } catch (EvaluationAbortException var7) {
        }
        return ctx;
    }

    @Override
    public EvaluationContext evaluate(Object document, Object rootDocument, Configuration configuration) {
        return this.evaluate(document, rootDocument, configuration, false);
    }

    @Override
    public boolean isDefinite() {
        return this.root.isPathDefinite();
    }

    @Override
    public boolean isFunctionPath() {
        return this.root.isFunctionPath();
    }

    public String toString() {
        return this.root.toString();
    }

    public RootPathToken getRoot() {
        return this.root;
    }
}