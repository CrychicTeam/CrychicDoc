package info.journeymap.shaded.kotlin.spark.route;

import java.util.HashMap;

public enum HttpMethod {

    get,
    post,
    put,
    patch,
    delete,
    head,
    trace,
    connect,
    options,
    before,
    after,
    afterafter,
    unsupported;

    private static HashMap<String, HttpMethod> methods = new HashMap();

    public static HttpMethod get(String methodStr) {
        HttpMethod method = (HttpMethod) methods.get(methodStr);
        return method != null ? method : unsupported;
    }

    static {
        for (HttpMethod method : values()) {
            methods.put(method.toString(), method);
        }
    }
}