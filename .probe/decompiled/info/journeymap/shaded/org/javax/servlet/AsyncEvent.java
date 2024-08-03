package info.journeymap.shaded.org.javax.servlet;

public class AsyncEvent {

    private AsyncContext context;

    private ServletRequest request;

    private ServletResponse response;

    private Throwable throwable;

    public AsyncEvent(AsyncContext context) {
        this(context, context.getRequest(), context.getResponse(), null);
    }

    public AsyncEvent(AsyncContext context, ServletRequest request, ServletResponse response) {
        this(context, request, response, null);
    }

    public AsyncEvent(AsyncContext context, Throwable throwable) {
        this(context, context.getRequest(), context.getResponse(), throwable);
    }

    public AsyncEvent(AsyncContext context, ServletRequest request, ServletResponse response, Throwable throwable) {
        this.context = context;
        this.request = request;
        this.response = response;
        this.throwable = throwable;
    }

    public AsyncContext getAsyncContext() {
        return this.context;
    }

    public ServletRequest getSuppliedRequest() {
        return this.request;
    }

    public ServletResponse getSuppliedResponse() {
        return this.response;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }
}