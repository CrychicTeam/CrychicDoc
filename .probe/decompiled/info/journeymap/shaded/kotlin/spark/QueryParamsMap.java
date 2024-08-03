package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.org.javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryParamsMap {

    private static final QueryParamsMap NULL = new QueryParamsMap.NullQueryParamsMap();

    private static final Pattern KEY_PATTERN = Pattern.compile("\\A[\\[\\]]*([^\\[\\]]+)\\]*");

    private Map<String, QueryParamsMap> queryMap = new HashMap();

    private String[] values;

    public QueryParamsMap(HttpServletRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("HttpServletRequest cannot be null.");
        } else {
            this.loadQueryString(request.getParameterMap());
        }
    }

    protected QueryParamsMap() {
    }

    protected QueryParamsMap(String key, String... values) {
        this.loadKeys(key, values);
    }

    protected QueryParamsMap(Map<String, String[]> params) {
        this.loadQueryString(params);
    }

    protected final void loadQueryString(Map<String, String[]> params) {
        for (Entry<String, String[]> param : params.entrySet()) {
            this.loadKeys((String) param.getKey(), (String[]) param.getValue());
        }
    }

    protected final void loadKeys(String key, String[] value) {
        String[] parsed = this.parseKey(key);
        if (parsed != null) {
            if (!this.queryMap.containsKey(parsed[0])) {
                this.queryMap.put(parsed[0], new QueryParamsMap());
            }
            if (!parsed[1].isEmpty()) {
                ((QueryParamsMap) this.queryMap.get(parsed[0])).loadKeys(parsed[1], value);
            } else {
                ((QueryParamsMap) this.queryMap.get(parsed[0])).values = (String[]) value.clone();
            }
        }
    }

    protected final String[] parseKey(String key) {
        Matcher m = KEY_PATTERN.matcher(key);
        return m.find() ? new String[] { cleanKey(m.group()), key.substring(m.end()) } : null;
    }

    protected static final String cleanKey(String group) {
        return group.startsWith("[") ? group.substring(1, group.length() - 1) : group;
    }

    public QueryParamsMap get(String... keys) {
        QueryParamsMap ret = this;
        for (String key : keys) {
            if (ret.queryMap.containsKey(key)) {
                ret = (QueryParamsMap) ret.queryMap.get(key);
            } else {
                ret = NULL;
            }
        }
        return ret;
    }

    public String value() {
        return this.hasValue() ? this.values[0] : null;
    }

    public String value(String... keys) {
        return this.get(keys).value();
    }

    public boolean hasKeys() {
        return !this.queryMap.isEmpty();
    }

    public boolean hasKey(String key) {
        return this.queryMap.containsKey(key);
    }

    public boolean hasValue() {
        return this.values != null && this.values.length > 0;
    }

    public Boolean booleanValue() {
        return this.hasValue() ? Boolean.valueOf(this.value()) : null;
    }

    public Integer integerValue() {
        return this.hasValue() ? Integer.valueOf(this.value()) : null;
    }

    public Long longValue() {
        return this.hasValue() ? Long.valueOf(this.value()) : null;
    }

    public Float floatValue() {
        return this.hasValue() ? Float.valueOf(this.value()) : null;
    }

    public Double doubleValue() {
        return this.hasValue() ? Double.valueOf(this.value()) : null;
    }

    public String[] values() {
        return (String[]) this.values.clone();
    }

    Map<String, QueryParamsMap> getQueryMap() {
        return this.queryMap;
    }

    String[] getValues() {
        return this.values;
    }

    public Map<String, String[]> toMap() {
        Map<String, String[]> map = new HashMap();
        for (Entry<String, QueryParamsMap> key : this.queryMap.entrySet()) {
            map.put(key.getKey(), ((QueryParamsMap) key.getValue()).values);
        }
        return map;
    }

    private static class NullQueryParamsMap extends QueryParamsMap {

        public NullQueryParamsMap() {
        }
    }
}