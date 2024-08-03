package info.journeymap.shaded.org.javax.servlet;

public interface AsyncContext {

    String ASYNC_REQUEST_URI = "info.journeymap.shaded.org.javax.servlet.async.request_uri";

    String ASYNC_CONTEXT_PATH = "info.journeymap.shaded.org.javax.servlet.async.context_path";

    String ASYNC_MAPPING = "info.journeymap.shaded.org.javax.servlet.async.mapping";

    String ASYNC_PATH_INFO = "info.journeymap.shaded.org.javax.servlet.async.path_info";

    String ASYNC_SERVLET_PATH = "info.journeymap.shaded.org.javax.servlet.async.servlet_path";

    String ASYNC_QUERY_STRING = "info.journeymap.shaded.org.javax.servlet.async.query_string";

    ServletRequest getRequest();

    ServletResponse getResponse();

    boolean hasOriginalRequestAndResponse();

    void dispatch();

    void dispatch(String var1);

    void dispatch(ServletContext var1, String var2);

    void complete();

    void start(Runnable var1);

    void addListener(AsyncListener var1);

    void addListener(AsyncListener var1, ServletRequest var2, ServletResponse var3);

    <T extends AsyncListener> T createListener(Class<T> var1) throws ServletException;

    void setTimeout(long var1);

    long getTimeout();
}