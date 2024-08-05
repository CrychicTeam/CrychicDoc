package info.journeymap.shaded.kotlin.spark;

import info.journeymap.shaded.kotlin.spark.utils.Wrapper;

public abstract class FilterImpl implements Filter, Wrapper {

    static final String DEFAULT_ACCEPT_TYPE = "*/*";

    private String path;

    private String acceptType;

    private Filter delegate;

    public FilterImpl withPrefix(String prefix) {
        this.path = prefix + this.path;
        return this;
    }

    static FilterImpl create(String path, Filter filter) {
        return create(path, "*/*", filter);
    }

    static FilterImpl create(String path, String acceptType, final Filter filter) {
        if (acceptType == null) {
            acceptType = "*/*";
        }
        return new FilterImpl(path, acceptType, filter) {

            @Override
            public void handle(Request request, Response response) throws Exception {
                filter.handle(request, response);
            }
        };
    }

    protected FilterImpl(String path, String acceptType) {
        this.path = path;
        this.acceptType = acceptType;
    }

    protected FilterImpl(String path, String acceptType, Filter filter) {
        this(path, acceptType);
        this.delegate = filter;
    }

    @Override
    public abstract void handle(Request var1, Response var2) throws Exception;

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