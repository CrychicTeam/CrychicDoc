package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.EvaluationListener;
import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;
import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationAbortException;
import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EvaluationContextImpl implements EvaluationContext {

    private static final EvaluationAbortException ABORT_EVALUATION = new EvaluationAbortException();

    private final Configuration configuration;

    private final Object valueResult;

    private final Object pathResult;

    private final Path path;

    private final Object rootDocument;

    private final List<PathRef> updateOperations;

    private final HashMap<Path, Object> documentEvalCache = new HashMap();

    private final boolean forUpdate;

    private final boolean suppressExceptions;

    private int resultIndex = 0;

    public RootPathToken getRoot() {
        return ((CompiledPath) this.path).getRoot();
    }

    public EvaluationContextImpl(Path path, Object rootDocument, Configuration configuration, boolean forUpdate) {
        Utils.notNull(path, "path can not be null");
        Utils.notNull(rootDocument, "root can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        this.forUpdate = forUpdate;
        this.path = path;
        this.rootDocument = rootDocument;
        this.configuration = configuration;
        this.valueResult = configuration.jsonProvider().createArray();
        this.pathResult = configuration.jsonProvider().createArray();
        this.updateOperations = new ArrayList();
        this.suppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
    }

    public HashMap<Path, Object> documentEvalCache() {
        return this.documentEvalCache;
    }

    public boolean forUpdate() {
        return this.forUpdate;
    }

    public void addResult(String path, PathRef operation, Object model) {
        if (this.forUpdate) {
            this.updateOperations.add(operation);
        }
        this.configuration.jsonProvider().setArrayIndex(this.valueResult, this.resultIndex, model);
        this.configuration.jsonProvider().setArrayIndex(this.pathResult, this.resultIndex, path);
        this.resultIndex++;
        if (!this.configuration().getEvaluationListeners().isEmpty()) {
            int idx = this.resultIndex - 1;
            for (EvaluationListener listener : this.configuration().getEvaluationListeners()) {
                EvaluationListener.EvaluationContinuation continuation = listener.resultFound(new EvaluationContextImpl.FoundResultImpl(idx, path, model));
                if (EvaluationListener.EvaluationContinuation.ABORT == continuation) {
                    throw ABORT_EVALUATION;
                }
            }
        }
    }

    public JsonProvider jsonProvider() {
        return this.configuration.jsonProvider();
    }

    public Set<Option> options() {
        return this.configuration.getOptions();
    }

    @Override
    public Configuration configuration() {
        return this.configuration;
    }

    @Override
    public Object rootDocument() {
        return this.rootDocument;
    }

    @Override
    public Collection<PathRef> updateOperations() {
        Collections.sort(this.updateOperations);
        return Collections.unmodifiableCollection(this.updateOperations);
    }

    @Override
    public <T> T getValue() {
        return this.getValue(true);
    }

    @Override
    public <T> T getValue(boolean unwrap) {
        if (this.path.isDefinite()) {
            if (this.resultIndex == 0) {
                if (this.suppressExceptions) {
                    return null;
                } else {
                    throw new PathNotFoundException("No results for path: " + this.path.toString());
                }
            } else {
                int len = this.jsonProvider().length(this.valueResult);
                Object value = len > 0 ? this.jsonProvider().getArrayIndex(this.valueResult, len - 1) : null;
                if (value != null && unwrap) {
                    value = this.jsonProvider().unwrap(value);
                }
                return (T) value;
            }
        } else {
            return (T) this.valueResult;
        }
    }

    @Override
    public <T> T getPath() {
        if (this.resultIndex == 0) {
            if (this.suppressExceptions) {
                return null;
            } else {
                throw new PathNotFoundException("No results for path: " + this.path.toString());
            }
        } else {
            return (T) this.pathResult;
        }
    }

    @Override
    public List<String> getPathList() {
        List<String> res = new ArrayList();
        if (this.resultIndex > 0) {
            for (Object o : this.configuration.jsonProvider().toIterable(this.pathResult)) {
                res.add((String) o);
            }
        }
        return res;
    }

    private static class FoundResultImpl implements EvaluationListener.FoundResult {

        private final int index;

        private final String path;

        private final Object result;

        private FoundResultImpl(int index, String path, Object result) {
            this.index = index;
            this.path = path;
            this.result = result;
        }

        @Override
        public int index() {
            return this.index;
        }

        @Override
        public String path() {
            return this.path;
        }

        @Override
        public Object result() {
            return this.result;
        }
    }
}