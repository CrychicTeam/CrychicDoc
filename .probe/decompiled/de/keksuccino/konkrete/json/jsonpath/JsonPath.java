package de.keksuccino.konkrete.json.jsonpath;

import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.ParseContextImpl;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import de.keksuccino.konkrete.json.jsonpath.internal.path.PathCompiler;
import de.keksuccino.konkrete.json.jsonpath.spi.json.JsonProvider;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsonPath {

    private final Path path;

    private JsonPath(String jsonPath, Predicate[] filters) {
        Utils.notNull(jsonPath, "path can not be null");
        this.path = PathCompiler.compile(jsonPath, filters);
    }

    public String getPath() {
        return this.path.toString();
    }

    public static boolean isPathDefinite(String path) {
        return compile(path).isDefinite();
    }

    public boolean isDefinite() {
        return this.path.isDefinite();
    }

    public <T> T read(Object jsonObject) {
        return this.read(jsonObject, Configuration.defaultConfiguration());
    }

    public <T> T read(Object jsonObject, Configuration configuration) {
        boolean optAsPathList = configuration.containsOption(Option.AS_PATH_LIST);
        boolean optAlwaysReturnList = configuration.containsOption(Option.ALWAYS_RETURN_LIST);
        boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
        if (this.path.isFunctionPath()) {
            if (!optAsPathList && !optAlwaysReturnList) {
                EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration);
                if (optSuppressExceptions && evaluationContext.getPathList().isEmpty()) {
                    return (T) (this.path.isDefinite() ? null : configuration.jsonProvider().createArray());
                } else {
                    return evaluationContext.getValue(true);
                }
            } else if (optSuppressExceptions) {
                return (T) (this.path.isDefinite() ? null : configuration.jsonProvider().createArray());
            } else {
                throw new JsonPathException("Options " + Option.AS_PATH_LIST + " and " + Option.ALWAYS_RETURN_LIST + " are not allowed when using path functions!");
            }
        } else if (optAsPathList) {
            EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration);
            return (T) (optSuppressExceptions && evaluationContext.getPathList().isEmpty() ? configuration.jsonProvider().createArray() : evaluationContext.getPath());
        } else {
            EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration);
            if (!optSuppressExceptions || !evaluationContext.getPathList().isEmpty()) {
                Object res = evaluationContext.getValue(false);
                if (optAlwaysReturnList && this.path.isDefinite()) {
                    Object array = configuration.jsonProvider().createArray();
                    configuration.jsonProvider().setArrayIndex(array, 0, res);
                    return (T) array;
                } else {
                    return (T) res;
                }
            } else if (optAlwaysReturnList) {
                return (T) configuration.jsonProvider().createArray();
            } else {
                return (T) (this.path.isDefinite() ? null : configuration.jsonProvider().createArray());
            }
        }
    }

    public <T> T set(Object jsonObject, Object newVal, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        if (evaluationContext.getPathList().isEmpty()) {
            boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
            if (optSuppressExceptions) {
                return this.handleMissingPathInContext(configuration);
            } else {
                throw new PathNotFoundException();
            }
        } else {
            for (PathRef updateOperation : evaluationContext.updateOperations()) {
                updateOperation.set(newVal, configuration);
            }
            return this.resultByConfiguration(jsonObject, configuration, evaluationContext);
        }
    }

    public <T> T map(Object jsonObject, MapFunction mapFunction, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        Utils.notNull(mapFunction, "mapFunction can not be null");
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        if (evaluationContext.getPathList().isEmpty()) {
            boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
            if (optSuppressExceptions) {
                return this.handleMissingPathInContext(configuration);
            } else {
                throw new PathNotFoundException();
            }
        } else {
            for (PathRef updateOperation : evaluationContext.updateOperations()) {
                updateOperation.convert(mapFunction, configuration);
            }
            return this.resultByConfiguration(jsonObject, configuration, evaluationContext);
        }
    }

    public <T> T delete(Object jsonObject, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        if (evaluationContext.getPathList().isEmpty()) {
            boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
            if (optSuppressExceptions) {
                return this.handleMissingPathInContext(configuration);
            } else {
                throw new PathNotFoundException();
            }
        } else {
            for (PathRef updateOperation : evaluationContext.updateOperations()) {
                updateOperation.delete(configuration);
            }
            return this.resultByConfiguration(jsonObject, configuration, evaluationContext);
        }
    }

    public <T> T add(Object jsonObject, Object value, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        if (evaluationContext.getPathList().isEmpty()) {
            boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
            if (optSuppressExceptions) {
                return this.handleMissingPathInContext(configuration);
            } else {
                throw new PathNotFoundException();
            }
        } else {
            for (PathRef updateOperation : evaluationContext.updateOperations()) {
                updateOperation.add(value, configuration);
            }
            return this.resultByConfiguration(jsonObject, configuration, evaluationContext);
        }
    }

    public <T> T put(Object jsonObject, String key, Object value, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null");
        Utils.notEmpty((T) key, "key can not be null or empty");
        Utils.notNull(configuration, "configuration can not be null");
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        if (evaluationContext.getPathList().isEmpty()) {
            boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
            if (optSuppressExceptions) {
                return this.handleMissingPathInContext(configuration);
            } else {
                throw new PathNotFoundException();
            }
        } else {
            for (PathRef updateOperation : evaluationContext.updateOperations()) {
                updateOperation.put(key, value, configuration);
            }
            return this.resultByConfiguration(jsonObject, configuration, evaluationContext);
        }
    }

    public <T> T renameKey(Object jsonObject, String oldKeyName, String newKeyName, Configuration configuration) {
        Utils.notNull(jsonObject, "json can not be null");
        Utils.notEmpty((T) newKeyName, "newKeyName can not be null or empty");
        Utils.notNull(configuration, "configuration can not be null");
        EvaluationContext evaluationContext = this.path.evaluate(jsonObject, jsonObject, configuration, true);
        for (PathRef updateOperation : evaluationContext.updateOperations()) {
            boolean optSuppressExceptions = configuration.containsOption(Option.SUPPRESS_EXCEPTIONS);
            try {
                updateOperation.renameKey(oldKeyName, newKeyName, configuration);
            } catch (RuntimeException var10) {
                if (!optSuppressExceptions) {
                    throw var10;
                }
            }
        }
        return this.resultByConfiguration(jsonObject, configuration, evaluationContext);
    }

    public <T> T read(String json) {
        return this.read(json, Configuration.defaultConfiguration());
    }

    public <T> T read(String json, Configuration configuration) {
        Utils.notEmpty((T) json, "json can not be null or empty");
        Utils.notNull(configuration, "jsonProvider can not be null");
        return this.read(configuration.jsonProvider().parse(json), configuration);
    }

    public <T> T read(URL jsonURL) throws IOException {
        return this.read(jsonURL, Configuration.defaultConfiguration());
    }

    public <T> T read(File jsonFile) throws IOException {
        return this.read(jsonFile, Configuration.defaultConfiguration());
    }

    public <T> T read(File jsonFile, Configuration configuration) throws IOException {
        Utils.notNull(jsonFile, "json file can not be null");
        Utils.isTrue(jsonFile.exists(), "json file does not exist");
        Utils.notNull(configuration, "jsonProvider can not be null");
        FileInputStream fis = null;
        Object var4;
        try {
            fis = new FileInputStream(jsonFile);
            var4 = this.read((InputStream) fis, configuration);
        } finally {
            Utils.closeQuietly(fis);
        }
        return (T) var4;
    }

    public <T> T read(InputStream jsonInputStream) throws IOException {
        return this.read(jsonInputStream, Configuration.defaultConfiguration());
    }

    public <T> T read(InputStream jsonInputStream, Configuration configuration) throws IOException {
        Utils.notNull(jsonInputStream, "json input stream can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        return this.read(jsonInputStream, "UTF-8", configuration);
    }

    public <T> T read(InputStream jsonInputStream, String charset, Configuration configuration) throws IOException {
        Utils.notNull(jsonInputStream, "json input stream can not be null");
        Utils.notNull(charset, "charset can not be null");
        Utils.notNull(configuration, "configuration can not be null");
        Object var4;
        try {
            var4 = this.read(configuration.jsonProvider().parse(jsonInputStream, charset), configuration);
        } finally {
            Utils.closeQuietly(jsonInputStream);
        }
        return (T) var4;
    }

    public static JsonPath compile(String jsonPath, Predicate... filters) {
        Utils.notEmpty(jsonPath, "json can not be null or empty");
        return new JsonPath(jsonPath, filters);
    }

    public static <T> T read(Object json, String jsonPath, Predicate... filters) {
        return parse(json).read(jsonPath, filters);
    }

    public static <T> T read(String json, String jsonPath, Predicate... filters) {
        return new ParseContextImpl().parse(json).read(jsonPath, filters);
    }

    @Deprecated
    public static <T> T read(URL jsonURL, String jsonPath, Predicate... filters) throws IOException {
        return new ParseContextImpl().parse(jsonURL).read(jsonPath, filters);
    }

    public static <T> T read(File jsonFile, String jsonPath, Predicate... filters) throws IOException {
        return new ParseContextImpl().parse(jsonFile).read(jsonPath, filters);
    }

    public static <T> T read(InputStream jsonInputStream, String jsonPath, Predicate... filters) throws IOException {
        return new ParseContextImpl().parse(jsonInputStream).read(jsonPath, filters);
    }

    public static ParseContext using(Configuration configuration) {
        return new ParseContextImpl(configuration);
    }

    @Deprecated
    public static ParseContext using(JsonProvider provider) {
        return new ParseContextImpl(Configuration.builder().jsonProvider(provider).build());
    }

    public static DocumentContext parse(Object json) {
        return new ParseContextImpl().parse(json);
    }

    public static DocumentContext parse(String json) {
        return new ParseContextImpl().parse(json);
    }

    public static DocumentContext parse(InputStream json) {
        return new ParseContextImpl().parse(json);
    }

    public static DocumentContext parse(File json) throws IOException {
        return new ParseContextImpl().parse(json);
    }

    @Deprecated
    public static DocumentContext parse(URL json) throws IOException {
        return new ParseContextImpl().parse(json);
    }

    public static DocumentContext parse(Object json, Configuration configuration) {
        return new ParseContextImpl(configuration).parse(json);
    }

    public static DocumentContext parse(String json, Configuration configuration) {
        return new ParseContextImpl(configuration).parse(json);
    }

    public static DocumentContext parse(InputStream json, Configuration configuration) {
        return new ParseContextImpl(configuration).parse(json);
    }

    public static DocumentContext parse(File json, Configuration configuration) throws IOException {
        return new ParseContextImpl(configuration).parse(json);
    }

    @Deprecated
    public static DocumentContext parse(URL json, Configuration configuration) throws IOException {
        return new ParseContextImpl(configuration).parse(json);
    }

    private <T> T resultByConfiguration(Object jsonObject, Configuration configuration, EvaluationContext evaluationContext) {
        return (T) (configuration.containsOption(Option.AS_PATH_LIST) ? evaluationContext.getPathList() : jsonObject);
    }

    private <T> T handleMissingPathInContext(Configuration configuration) {
        boolean optAsPathList = configuration.containsOption(Option.AS_PATH_LIST);
        boolean optAlwaysReturnList = configuration.containsOption(Option.ALWAYS_RETURN_LIST);
        if (optAsPathList) {
            return (T) configuration.jsonProvider().createArray();
        } else if (optAlwaysReturnList) {
            return (T) configuration.jsonProvider().createArray();
        } else {
            return (T) (this.path.isDefinite() ? null : configuration.jsonProvider().createArray());
        }
    }
}