package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.javax.servlet.AsyncContext;
import info.journeymap.shaded.org.javax.servlet.AsyncEvent;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;

public class AsyncContextEvent extends AsyncEvent implements Runnable {

    private final ContextHandler.Context _context;

    private final AsyncContextState _asyncContext;

    private volatile HttpChannelState _state;

    private ServletContext _dispatchContext;

    private String _dispatchPath;

    private volatile Scheduler.Task _timeoutTask;

    private Throwable _throwable;

    public AsyncContextEvent(ContextHandler.Context context, AsyncContextState asyncContext, HttpChannelState state, Request baseRequest, ServletRequest request, ServletResponse response) {
        super(null, request, response, null);
        this._context = context;
        this._asyncContext = asyncContext;
        this._state = state;
        if (baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.async.request_uri") == null) {
            String uri = (String) baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.forward.request_uri");
            if (uri != null) {
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.request_uri", uri);
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.context_path", baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.forward.context_path"));
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.servlet_path", baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.forward.servlet_path"));
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.path_info", baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.forward.path_info"));
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.query_string", baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.forward.query_string"));
            } else {
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.request_uri", baseRequest.getRequestURI());
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.context_path", baseRequest.getContextPath());
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.servlet_path", baseRequest.getServletPath());
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.path_info", baseRequest.getPathInfo());
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.async.query_string", baseRequest.getQueryString());
            }
        }
    }

    public ServletContext getSuspendedContext() {
        return this._context;
    }

    public ContextHandler.Context getContext() {
        return this._context;
    }

    public ServletContext getDispatchContext() {
        return this._dispatchContext;
    }

    public ServletContext getServletContext() {
        return (ServletContext) (this._dispatchContext == null ? this._context : this._dispatchContext);
    }

    public String getPath() {
        return this._dispatchPath;
    }

    public void setTimeoutTask(Scheduler.Task task) {
        this._timeoutTask = task;
    }

    public boolean hasTimeoutTask() {
        return this._timeoutTask != null;
    }

    public void cancelTimeoutTask() {
        Scheduler.Task task = this._timeoutTask;
        this._timeoutTask = null;
        if (task != null) {
            task.cancel();
        }
    }

    @Override
    public AsyncContext getAsyncContext() {
        return this._asyncContext;
    }

    @Override
    public Throwable getThrowable() {
        return this._throwable;
    }

    public void setDispatchContext(ServletContext context) {
        this._dispatchContext = context;
    }

    public void setDispatchPath(String path) {
        this._dispatchPath = path;
    }

    public void completed() {
        this._timeoutTask = null;
        this._asyncContext.reset();
    }

    public HttpChannelState getHttpChannelState() {
        return this._state;
    }

    public void run() {
        Scheduler.Task task = this._timeoutTask;
        this._timeoutTask = null;
        if (task != null) {
            this._state.getHttpChannel().execute(() -> this._state.onTimeout());
        }
    }

    public void addThrowable(Throwable e) {
        if (this._throwable == null) {
            this._throwable = e;
        } else if (this._throwable != e) {
            this._throwable.addSuppressed(e);
        }
    }
}