package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.utils.Wrapper;

public abstract class RouteImpl implements Route, Wrapper {

    static final String DEFAULT_ACCEPT_TYPE = "*/*";

    private String path;

    private String acceptType;

    private Object delegate;

    public RouteImpl withPrefix(String prefix) {
        this.path = prefix + this.path;
        return this;
    }

    public static RouteImpl create(String path, Route route) {
        return create(path, "*/*", route);
    }

    public static RouteImpl create(String path, String acceptType, final Route route) {
        if (acceptType == null) {
            acceptType = "*/*";
        }
        return new RouteImpl(path, acceptType, route) {

            @Override
            public Object handle(Request request, Response response) throws Exception {
                return route.handle(request, response);
            }
        };
    }

    protected RouteImpl(String path) {
        this(path, "*/*");
    }

    protected RouteImpl(String path, String acceptType) {
        this.path = path;
        this.acceptType = acceptType;
    }

    protected RouteImpl(String path, String acceptType, Object route) {
        this(path, acceptType);
        this.delegate = route;
    }

    @Override
    public abstract Object handle(Request var1, Response var2) throws Exception;

    public Object render(Object element) throws Exception {
        return element != null ? element : null;
    }

    public String getAcceptType() {
        return this.acceptType;
    }

    public String getPath() {
        return this.path;
    }

    @Override
    public Object delegate() {
        return this.delegate;
    }
}