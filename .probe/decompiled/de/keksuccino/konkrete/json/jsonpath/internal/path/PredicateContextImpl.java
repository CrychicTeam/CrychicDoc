package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.spi.mapper.MappingException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PredicateContextImpl implements Predicate.PredicateContext {

    private static final Logger logger = LoggerFactory.getLogger(PredicateContextImpl.class);

    private final Object contextDocument;

    private final Object rootDocument;

    private final Configuration configuration;

    private final HashMap<Path, Object> documentPathCache;

    public PredicateContextImpl(Object contextDocument, Object rootDocument, Configuration configuration, HashMap<Path, Object> documentPathCache) {
        this.contextDocument = contextDocument;
        this.rootDocument = rootDocument;
        this.configuration = configuration;
        this.documentPathCache = documentPathCache;
    }

    public Object evaluate(Path path) {
        Object result;
        if (path.isRootPath()) {
            if (this.documentPathCache.containsKey(path)) {
                logger.debug("Using cached result for root path: " + path.toString());
                result = this.documentPathCache.get(path);
            } else {
                result = path.evaluate(this.rootDocument, this.rootDocument, this.configuration).getValue();
                this.documentPathCache.put(path, result);
            }
        } else {
            result = path.evaluate(this.contextDocument, this.rootDocument, this.configuration).getValue();
        }
        return result;
    }

    public HashMap<Path, Object> documentPathCache() {
        return this.documentPathCache;
    }

    @Override
    public Object item() {
        return this.contextDocument;
    }

    @Override
    public <T> T item(Class<T> clazz) throws MappingException {
        return this.configuration().mappingProvider().map(this.contextDocument, clazz, this.configuration);
    }

    @Override
    public Object root() {
        return this.rootDocument;
    }

    @Override
    public Configuration configuration() {
        return this.configuration;
    }
}