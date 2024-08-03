package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.Configuration;
import de.keksuccino.konkrete.json.jsonpath.DocumentContext;
import de.keksuccino.konkrete.json.jsonpath.EvaluationListener;
import de.keksuccino.konkrete.json.jsonpath.JsonPath;
import de.keksuccino.konkrete.json.jsonpath.MapFunction;
import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.ReadContext;
import de.keksuccino.konkrete.json.jsonpath.TypeRef;
import de.keksuccino.konkrete.json.jsonpath.spi.cache.Cache;
import de.keksuccino.konkrete.json.jsonpath.spi.cache.CacheProvider;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonContext implements DocumentContext {

    private static final Logger logger = LoggerFactory.getLogger(JsonContext.class);

    private final Configuration configuration;

    private final Object json;

    JsonContext(Object json, Configuration configuration) {
        Utils.notNull(json, "json can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        this.configuration = configuration;
        this.json = json;
    }

    @Override
    public Configuration configuration() {
        return this.configuration;
    }

    @Override
    public Object json() {
        return this.json;
    }

    @Override
    public String jsonString() {
        return this.configuration.jsonProvider().toJson(this.json);
    }

    @Override
    public <T> T read(String path, Predicate... filters) {
        Utils.notEmpty((T) path, "path can not be null or empty");
        return this.read(this.pathFromCache(path, filters));
    }

    @Override
    public <T> T read(String path, Class<T> type, Predicate... filters) {
        return this.convert(this.read(path, filters), type, this.configuration);
    }

    @Override
    public <T> T read(JsonPath path) {
        Utils.notNull(path, "path can not be null");
        return path.read(this.json, this.configuration);
    }

    @Override
    public <T> T read(JsonPath path, Class<T> type) {
        return this.convert(this.read(path), type, this.configuration);
    }

    @Override
    public <T> T read(JsonPath path, TypeRef<T> type) {
        return this.convert(this.read(path), type, this.configuration);
    }

    @Override
    public <T> T read(String path, TypeRef<T> type) {
        return this.convert(this.read(path), type, this.configuration);
    }

    @Override
    public ReadContext limit(int maxResults) {
        return this.withListeners(new JsonContext.LimitingEvaluationListener(maxResults));
    }

    @Override
    public ReadContext withListeners(EvaluationListener... listener) {
        return new JsonContext(this.json, this.configuration.setEvaluationListeners(listener));
    }

    private <T> T convert(Object obj, Class<T> targetType, Configuration configuration) {
        return configuration.mappingProvider().map(obj, targetType, configuration);
    }

    private <T> T convert(Object obj, TypeRef<T> targetType, Configuration configuration) {
        return configuration.mappingProvider().map(obj, targetType, configuration);
    }

    @Override
    public DocumentContext set(String path, Object newValue, Predicate... filters) {
        return this.set(this.pathFromCache(path, filters), newValue);
    }

    @Override
    public DocumentContext set(JsonPath path, Object newValue) {
        List<String> modified = path.set(this.json, newValue, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Set path {} new value {}", p, newValue);
            }
        }
        return this;
    }

    @Override
    public DocumentContext map(String path, MapFunction mapFunction, Predicate... filters) {
        this.map(this.pathFromCache(path, filters), mapFunction);
        return this;
    }

    @Override
    public DocumentContext map(JsonPath path, MapFunction mapFunction) {
        Object obj = path.map(this.json, mapFunction, this.configuration);
        return obj == null ? null : this;
    }

    @Override
    public DocumentContext delete(String path, Predicate... filters) {
        return this.delete(this.pathFromCache(path, filters));
    }

    @Override
    public DocumentContext delete(JsonPath path) {
        List<String> modified = path.delete(this.json, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Delete path {}", p);
            }
        }
        return this;
    }

    @Override
    public DocumentContext add(String path, Object value, Predicate... filters) {
        return this.add(this.pathFromCache(path, filters), value);
    }

    @Override
    public DocumentContext add(JsonPath path, Object value) {
        List<String> modified = path.add(this.json, value, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Add path {} new value {}", p, value);
            }
        }
        return this;
    }

    @Override
    public DocumentContext put(String path, String key, Object value, Predicate... filters) {
        return this.put(this.pathFromCache(path, filters), key, value);
    }

    @Override
    public DocumentContext renameKey(String path, String oldKeyName, String newKeyName, Predicate... filters) {
        return this.renameKey(this.pathFromCache(path, filters), oldKeyName, newKeyName);
    }

    @Override
    public DocumentContext renameKey(JsonPath path, String oldKeyName, String newKeyName) {
        List<String> modified = path.renameKey(this.json, oldKeyName, newKeyName, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Rename path {} new value {}", p, newKeyName);
            }
        }
        return this;
    }

    @Override
    public DocumentContext put(JsonPath path, String key, Object value) {
        List<String> modified = path.put(this.json, key, value, this.configuration.addOptions(Option.AS_PATH_LIST));
        if (logger.isDebugEnabled()) {
            for (String p : modified) {
                logger.debug("Put path {} key {} value {}", new Object[] { p, key, value });
            }
        }
        return this;
    }

    private JsonPath pathFromCache(String path, Predicate[] filters) {
        Cache cache = CacheProvider.getCache();
        String cacheKey = filters != null && filters.length != 0 ? Utils.concat(path, Arrays.toString(filters)) : path;
        JsonPath jsonPath = cache.get(cacheKey);
        if (jsonPath == null) {
            jsonPath = JsonPath.compile(path, filters);
            cache.put(cacheKey, jsonPath);
        }
        return jsonPath;
    }

    private static final class LimitingEvaluationListener implements EvaluationListener {

        final int limit;

        private LimitingEvaluationListener(int limit) {
            this.limit = limit;
        }

        @Override
        public EvaluationListener.EvaluationContinuation resultFound(EvaluationListener.FoundResult found) {
            return found.index() == this.limit - 1 ? EvaluationListener.EvaluationContinuation.ABORT : EvaluationListener.EvaluationContinuation.CONTINUE;
        }
    }
}