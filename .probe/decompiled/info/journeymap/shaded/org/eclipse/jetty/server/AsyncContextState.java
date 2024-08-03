package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.javax.servlet.AsyncContext;
import info.journeymap.shaded.org.javax.servlet.AsyncEvent;
import info.journeymap.shaded.org.javax.servlet.AsyncListener;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletException;
import info.journeymap.shaded.org.javax.servlet.ServletRequest;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import java.io.IOException;

public class AsyncContextState implements AsyncContext {

    private final HttpChannel _channel;

    volatile HttpChannelState _state;

    public AsyncContextState(HttpChannelState state) {
        this._state = state;
        this._channel = this._state.getHttpChannel();
    }

    public HttpChannel getHttpChannel() {
        return this._channel;
    }

    HttpChannelState state() {
        HttpChannelState state = this._state;
        if (state == null) {
            throw new IllegalStateException("AsyncContext completed and/or Request lifecycle recycled");
        } else {
            return state;
        }
    }

    @Override
    public void addListener(final AsyncListener listener, final ServletRequest request, final ServletResponse response) {
        AsyncListener wrap = new AsyncListener() {

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                listener.onTimeout(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                listener.onStartAsync(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
            }

            @Override
            public void onError(AsyncEvent event) throws IOException {
                listener.onError(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
            }

            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                listener.onComplete(new AsyncEvent(event.getAsyncContext(), request, response, event.getThrowable()));
            }
        };
        this.state().addListener(wrap);
    }

    @Override
    public void addListener(AsyncListener listener) {
        this.state().addListener(listener);
    }

    @Override
    public void complete() {
        this.state().complete();
    }

    @Override
    public <T extends AsyncListener> T createListener(Class<T> clazz) throws ServletException {
        ContextHandler contextHandler = this.state().getContextHandler();
        if (contextHandler != null) {
            return contextHandler.getServletContext().createListener(clazz);
        } else {
            try {
                return (T) clazz.newInstance();
            } catch (Exception var4) {
                throw new ServletException(var4);
            }
        }
    }

    @Override
    public void dispatch() {
        this.state().dispatch(null, null);
    }

    @Override
    public void dispatch(String path) {
        this.state().dispatch(null, path);
    }

    @Override
    public void dispatch(ServletContext context, String path) {
        this.state().dispatch(context, path);
    }

    @Override
    public ServletRequest getRequest() {
        return this.state().getAsyncContextEvent().getSuppliedRequest();
    }

    @Override
    public ServletResponse getResponse() {
        return this.state().getAsyncContextEvent().getSuppliedResponse();
    }

    @Override
    public long getTimeout() {
        return this.state().getTimeout();
    }

    @Override
    public boolean hasOriginalRequestAndResponse() {
        HttpChannel channel = this.state().getHttpChannel();
        return channel.getRequest() == this.getRequest() && channel.getResponse() == this.getResponse();
    }

    @Override
    public void setTimeout(long arg0) {
        this.state().setTimeout(arg0);
    }

    @Override
    public void start(final Runnable task) {
        final HttpChannel channel = this.state().getHttpChannel();
        channel.execute(new Runnable() {

            public void run() {
                AsyncContextState.this.state().getAsyncContextEvent().getContext().getContextHandler().handle(channel.getRequest(), task);
            }
        });
    }

    public void reset() {
        this._state = null;
    }

    public HttpChannelState getHttpChannelState() {
        return this.state();
    }
}