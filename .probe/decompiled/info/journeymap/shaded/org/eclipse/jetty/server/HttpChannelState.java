package info.journeymap.shaded.org.eclipse.jetty.server;

import info.journeymap.shaded.org.eclipse.jetty.http.BadMessageException;
import info.journeymap.shaded.org.eclipse.jetty.server.handler.ContextHandler;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Log;
import info.journeymap.shaded.org.eclipse.jetty.util.log.Logger;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Locker;
import info.journeymap.shaded.org.eclipse.jetty.util.thread.Scheduler;
import info.journeymap.shaded.org.javax.servlet.AsyncListener;
import info.journeymap.shaded.org.javax.servlet.ServletContext;
import info.journeymap.shaded.org.javax.servlet.ServletResponse;
import info.journeymap.shaded.org.javax.servlet.UnavailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class HttpChannelState {

    private static final Logger LOG = Log.getLogger(HttpChannelState.class);

    private static final long DEFAULT_TIMEOUT = Long.getLong("info.journeymap.shaded.org.eclipse.jetty.server.HttpChannelState.DEFAULT_TIMEOUT", 30000L);

    private final Locker _locker = new Locker();

    private final HttpChannel _channel;

    private List<AsyncListener> _asyncListeners;

    private HttpChannelState.State _state;

    private HttpChannelState.Async _async;

    private boolean _initial;

    private HttpChannelState.AsyncRead _asyncRead = HttpChannelState.AsyncRead.IDLE;

    private boolean _asyncWritePossible;

    private long _timeoutMs = DEFAULT_TIMEOUT;

    private AsyncContextEvent _event;

    protected HttpChannelState(HttpChannel channel) {
        this._channel = channel;
        this._state = HttpChannelState.State.IDLE;
        this._async = HttpChannelState.Async.NOT_ASYNC;
        this._initial = true;
    }

    public HttpChannelState.State getState() {
        HttpChannelState.State var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state;
        }
        return var3;
    }

    public void addListener(AsyncListener listener) {
        try (Locker.Lock lock = this._locker.lock()) {
            if (this._asyncListeners == null) {
                this._asyncListeners = new ArrayList();
            }
            this._asyncListeners.add(listener);
        }
    }

    public void setTimeout(long ms) {
        try (Locker.Lock lock = this._locker.lock()) {
            this._timeoutMs = ms;
        }
    }

    public long getTimeout() {
        long var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._timeoutMs;
        }
        return var3;
    }

    public AsyncContextEvent getAsyncContextEvent() {
        AsyncContextEvent var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._event;
        }
        return var3;
    }

    public String toString() {
        String var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this.toStringLocked();
        }
        return var3;
    }

    public String toStringLocked() {
        return String.format("%s@%x{s=%s a=%s i=%b r=%s w=%b}", this.getClass().getSimpleName(), this.hashCode(), this._state, this._async, this._initial, this._asyncRead, this._asyncWritePossible);
    }

    private String getStatusStringLocked() {
        return String.format("s=%s i=%b a=%s", this._state, this._initial, this._async);
    }

    public String getStatusString() {
        String var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this.getStatusStringLocked();
        }
        return var3;
    }

    protected HttpChannelState.Action handling() {
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("handling {}", this.toStringLocked());
            }
            switch(this._state) {
                case IDLE:
                    this._initial = true;
                    this._state = HttpChannelState.State.DISPATCHED;
                    return HttpChannelState.Action.DISPATCH;
                case COMPLETING:
                case COMPLETED:
                    return HttpChannelState.Action.TERMINATED;
                case ASYNC_WOKEN:
                    switch(this._asyncRead) {
                        case POSSIBLE:
                            this._state = HttpChannelState.State.ASYNC_IO;
                            this._asyncRead = HttpChannelState.AsyncRead.PRODUCING;
                            return HttpChannelState.Action.READ_PRODUCE;
                        case READY:
                            this._state = HttpChannelState.State.ASYNC_IO;
                            this._asyncRead = HttpChannelState.AsyncRead.IDLE;
                            return HttpChannelState.Action.READ_CALLBACK;
                        case REGISTER:
                        case PRODUCING:
                            throw new IllegalStateException(this.toStringLocked());
                        case IDLE:
                        case REGISTERED:
                        default:
                            if (this._asyncWritePossible) {
                                this._state = HttpChannelState.State.ASYNC_IO;
                                this._asyncWritePossible = false;
                                return HttpChannelState.Action.WRITE_CALLBACK;
                            } else {
                                switch(this._async) {
                                    case COMPLETE:
                                        this._state = HttpChannelState.State.COMPLETING;
                                        return HttpChannelState.Action.COMPLETE;
                                    case DISPATCH:
                                        this._state = HttpChannelState.State.DISPATCHED;
                                        this._async = HttpChannelState.Async.NOT_ASYNC;
                                        return HttpChannelState.Action.ASYNC_DISPATCH;
                                    case EXPIRED:
                                    case ERRORED:
                                        this._state = HttpChannelState.State.DISPATCHED;
                                        this._async = HttpChannelState.Async.NOT_ASYNC;
                                        return HttpChannelState.Action.ERROR_DISPATCH;
                                    case STARTED:
                                    case EXPIRING:
                                    case ERRORING:
                                        return HttpChannelState.Action.WAIT;
                                    case NOT_ASYNC:
                                        return HttpChannelState.Action.WAIT;
                                    default:
                                        throw new IllegalStateException(this.getStatusStringLocked());
                                }
                            }
                    }
                case ASYNC_ERROR:
                    return HttpChannelState.Action.ASYNC_ERROR;
                case ASYNC_IO:
                case ASYNC_WAIT:
                case DISPATCHED:
                case UPGRADED:
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
            }
        }
    }

    public void startAsync(final AsyncContextEvent event) {
        final List<AsyncListener> lastAsyncListeners;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("startAsync {}", this.toStringLocked());
            }
            if (this._state != HttpChannelState.State.DISPATCHED || this._async != HttpChannelState.Async.NOT_ASYNC) {
                throw new IllegalStateException(this.getStatusStringLocked());
            }
            this._async = HttpChannelState.Async.STARTED;
            this._event = event;
            lastAsyncListeners = this._asyncListeners;
            this._asyncListeners = null;
        }
        if (lastAsyncListeners != null) {
            Runnable callback = new Runnable() {

                public void run() {
                    for (AsyncListener listener : lastAsyncListeners) {
                        try {
                            listener.onStartAsync(event);
                        } catch (Throwable var4) {
                            HttpChannelState.LOG.warn(var4);
                        }
                    }
                }

                public String toString() {
                    return "startAsync";
                }
            };
            this.runInContext(event, callback);
        }
    }

    public void asyncError(Throwable failure) {
        AsyncContextEvent event = null;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._state) {
                case IDLE:
                case COMPLETING:
                case COMPLETED:
                case ASYNC_WOKEN:
                case ASYNC_ERROR:
                case ASYNC_IO:
                case DISPATCHED:
                case UPGRADED:
                    break;
                case ASYNC_WAIT:
                    this._event.addThrowable(failure);
                    this._state = HttpChannelState.State.ASYNC_ERROR;
                    event = this._event;
                    break;
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
            }
        }
        if (event != null) {
            this.cancelTimeout(event);
            this.runInContext(event, this._channel);
        }
    }

    protected HttpChannelState.Action unhandle() {
        boolean read_interested = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("unhandle {}", this.toStringLocked());
            }
            switch(this._state) {
                case COMPLETING:
                case COMPLETED:
                    return HttpChannelState.Action.TERMINATED;
                case ASYNC_WOKEN:
                case ASYNC_WAIT:
                case UPGRADED:
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
                case ASYNC_ERROR:
                case ASYNC_IO:
                case DISPATCHED:
                    this._initial = false;
                    switch(this._async) {
                        case COMPLETE:
                            this._state = HttpChannelState.State.COMPLETING;
                            this._async = HttpChannelState.Async.NOT_ASYNC;
                            return HttpChannelState.Action.COMPLETE;
                        case DISPATCH:
                            this._state = HttpChannelState.State.DISPATCHED;
                            this._async = HttpChannelState.Async.NOT_ASYNC;
                            return HttpChannelState.Action.ASYNC_DISPATCH;
                        case EXPIRED:
                            this._state = HttpChannelState.State.DISPATCHED;
                            this._async = HttpChannelState.Async.NOT_ASYNC;
                            return HttpChannelState.Action.ERROR_DISPATCH;
                        case ERRORED:
                            this._state = HttpChannelState.State.DISPATCHED;
                            this._async = HttpChannelState.Async.NOT_ASYNC;
                            return HttpChannelState.Action.ERROR_DISPATCH;
                        case STARTED:
                            switch(this._asyncRead) {
                                case POSSIBLE:
                                    this._state = HttpChannelState.State.ASYNC_IO;
                                    this._asyncRead = HttpChannelState.AsyncRead.PRODUCING;
                                    return HttpChannelState.Action.READ_PRODUCE;
                                case READY:
                                    this._state = HttpChannelState.State.ASYNC_IO;
                                    this._asyncRead = HttpChannelState.AsyncRead.IDLE;
                                    return HttpChannelState.Action.READ_CALLBACK;
                                case REGISTER:
                                case PRODUCING:
                                    this._asyncRead = HttpChannelState.AsyncRead.REGISTERED;
                                    read_interested = true;
                                case IDLE:
                                case REGISTERED:
                            }
                            if (!this._asyncWritePossible) {
                                this._state = HttpChannelState.State.ASYNC_WAIT;
                                Scheduler scheduler = this._channel.getScheduler();
                                if (scheduler != null && this._timeoutMs > 0L && !this._event.hasTimeoutTask()) {
                                    this._event.setTimeoutTask(scheduler.schedule(this._event, this._timeoutMs, TimeUnit.MILLISECONDS));
                                }
                                return HttpChannelState.Action.WAIT;
                            }
                            this._state = HttpChannelState.State.ASYNC_IO;
                            this._asyncWritePossible = false;
                            return HttpChannelState.Action.WRITE_CALLBACK;
                        case EXPIRING:
                            this._state = HttpChannelState.State.ASYNC_WAIT;
                            return HttpChannelState.Action.WAIT;
                        case ERRORING:
                        default:
                            this._state = HttpChannelState.State.COMPLETING;
                            return HttpChannelState.Action.COMPLETE;
                        case NOT_ASYNC:
                            this._state = HttpChannelState.State.COMPLETING;
                            return HttpChannelState.Action.COMPLETE;
                    }
                case THROWN:
                    this._state = HttpChannelState.State.DISPATCHED;
                    return HttpChannelState.Action.ERROR_DISPATCH;
            }
        } finally {
            if (read_interested) {
                this._channel.asyncReadFillInterested();
            }
        }
    }

    public void dispatch(ServletContext context, String path) {
        boolean dispatch = false;
        AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("dispatch {} -> {}", this.toStringLocked(), path);
            }
            boolean started = false;
            event = this._event;
            switch(this._async) {
                case ERRORED:
                case EXPIRING:
                case ERRORING:
                    break;
                case STARTED:
                    started = true;
                    break;
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
            }
            this._async = HttpChannelState.Async.DISPATCH;
            if (context != null) {
                this._event.setDispatchContext(context);
            }
            if (path != null) {
                this._event.setDispatchPath(path);
            }
            if (started) {
                switch(this._state) {
                    case ASYNC_WOKEN:
                    case ASYNC_IO:
                    case DISPATCHED:
                        break;
                    case ASYNC_ERROR:
                    default:
                        LOG.warn("async dispatched when complete {}", this);
                        break;
                    case ASYNC_WAIT:
                        this._state = HttpChannelState.State.ASYNC_WOKEN;
                        dispatch = true;
                }
            }
        }
        this.cancelTimeout(event);
        if (dispatch) {
            this.scheduleDispatch();
        }
    }

    protected void onTimeout() {
        final List<AsyncListener> listeners;
        final AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onTimeout {}", this.toStringLocked());
            }
            if (this._async != HttpChannelState.Async.STARTED) {
                return;
            }
            this._async = HttpChannelState.Async.EXPIRING;
            event = this._event;
            listeners = this._asyncListeners;
        }
        final AtomicReference<Throwable> error = new AtomicReference();
        if (listeners != null) {
            Runnable task = new Runnable() {

                public void run() {
                    for (AsyncListener listener : listeners) {
                        try {
                            listener.onTimeout(event);
                        } catch (Throwable var4) {
                            HttpChannelState.LOG.warn(var4 + " while invoking onTimeout listener " + listener);
                            HttpChannelState.LOG.debug(var4);
                            if (error.get() == null) {
                                error.set(var4);
                            } else {
                                ((Throwable) error.get()).addSuppressed(var4);
                            }
                        }
                    }
                }

                public String toString() {
                    return "onTimeout";
                }
            };
            this.runInContext(event, task);
        }
        Throwable th = (Throwable) error.get();
        boolean dispatch = false;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._async) {
                case COMPLETE:
                case DISPATCH:
                    if (th != null) {
                        LOG.ignore(th);
                        th = null;
                    }
                    break;
                case EXPIRING:
                    this._async = th == null ? HttpChannelState.Async.EXPIRED : HttpChannelState.Async.ERRORING;
                    break;
                default:
                    throw new IllegalStateException();
            }
            if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                this._state = HttpChannelState.State.ASYNC_WOKEN;
                dispatch = true;
            }
        }
        if (th != null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Error after async timeout {}", this, th);
            }
            this.onError(th);
        }
        if (dispatch) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Dispatch after async timeout {}", this);
            }
            this.scheduleDispatch();
        }
    }

    public void complete() {
        boolean handle = false;
        AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("complete {}", this.toStringLocked());
            }
            boolean started = false;
            event = this._event;
            switch(this._async) {
                case COMPLETE:
                    return;
                case DISPATCH:
                case EXPIRED:
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
                case ERRORED:
                case EXPIRING:
                case ERRORING:
                    break;
                case STARTED:
                    started = true;
            }
            this._async = HttpChannelState.Async.COMPLETE;
            if (started && this._state == HttpChannelState.State.ASYNC_WAIT) {
                handle = true;
                this._state = HttpChannelState.State.ASYNC_WOKEN;
            }
        }
        this.cancelTimeout(event);
        if (handle) {
            this.runInContext(event, this._channel);
        }
    }

    public void errorComplete() {
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("error complete {}", this.toStringLocked());
            }
            this._async = HttpChannelState.Async.COMPLETE;
            this._event.setDispatchContext(null);
            this._event.setDispatchPath(null);
        }
        this.cancelTimeout();
    }

    protected void onError(Throwable failure) {
        Request baseRequest = this._channel.getRequest();
        int code = 500;
        String reason = null;
        if (failure instanceof BadMessageException) {
            BadMessageException bme = (BadMessageException) failure;
            code = bme.getCode();
            reason = bme.getReason();
        } else if (failure instanceof UnavailableException) {
            if (((UnavailableException) failure).isPermanent()) {
                code = 404;
            } else {
                code = 503;
            }
        }
        final List<AsyncListener> listeners;
        final AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onError {} {}", this.toStringLocked(), failure);
            }
            if (this._event != null) {
                this._event.addThrowable(failure);
                this._event.getSuppliedRequest().setAttribute("info.journeymap.shaded.org.javax.servlet.error.status_code", code);
                this._event.getSuppliedRequest().setAttribute("info.journeymap.shaded.org.javax.servlet.error.exception", failure);
                this._event.getSuppliedRequest().setAttribute("info.journeymap.shaded.org.javax.servlet.error.exception_type", failure == null ? null : failure.getClass());
                this._event.getSuppliedRequest().setAttribute("info.journeymap.shaded.org.javax.servlet.error.message", reason);
            } else {
                Throwable error = (Throwable) baseRequest.getAttribute("info.journeymap.shaded.org.javax.servlet.error.exception");
                if (error != null) {
                    throw new IllegalStateException("Error already set", error);
                }
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.error.status_code", code);
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.error.exception", failure);
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.error.exception_type", failure == null ? null : failure.getClass());
                baseRequest.setAttribute("info.journeymap.shaded.org.javax.servlet.error.message", reason);
            }
            if (this._async == HttpChannelState.Async.NOT_ASYNC) {
                if (this._state == HttpChannelState.State.DISPATCHED) {
                    this._state = HttpChannelState.State.THROWN;
                    return;
                }
                throw new IllegalStateException(this.getStatusStringLocked());
            }
            this._async = HttpChannelState.Async.ERRORING;
            listeners = this._asyncListeners;
            event = this._event;
        }
        if (listeners != null) {
            Runnable task = new Runnable() {

                public void run() {
                    for (AsyncListener listener : listeners) {
                        try {
                            listener.onError(event);
                        } catch (Throwable var4) {
                            HttpChannelState.LOG.warn(var4 + " while invoking onError listener " + listener);
                            HttpChannelState.LOG.debug(var4);
                        }
                    }
                }

                public String toString() {
                    return "onError";
                }
            };
            this.runInContext(event, task);
        }
        boolean dispatch = false;
        try (Locker.Lock lock = this._locker.lock()) {
            switch(this._async) {
                case COMPLETE:
                case DISPATCH:
                    break;
                case ERRORING:
                    this._async = HttpChannelState.Async.ERRORED;
                    break;
                default:
                    throw new IllegalStateException(this.toString());
            }
            if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                this._state = HttpChannelState.State.ASYNC_WOKEN;
                dispatch = true;
            }
        }
        if (dispatch) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Dispatch after error {}", this);
            }
            this.scheduleDispatch();
        }
    }

    protected void onComplete() {
        final List<AsyncListener> aListeners;
        final AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onComplete {}", this.toStringLocked());
            }
            switch(this._state) {
                case COMPLETING:
                    aListeners = this._asyncListeners;
                    event = this._event;
                    this._state = HttpChannelState.State.COMPLETED;
                    this._async = HttpChannelState.Async.NOT_ASYNC;
                    break;
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
            }
        }
        if (event != null) {
            if (aListeners != null) {
                Runnable callback = new Runnable() {

                    public void run() {
                        for (AsyncListener listener : aListeners) {
                            try {
                                listener.onComplete(event);
                            } catch (Throwable var4) {
                                HttpChannelState.LOG.warn(var4 + " while invoking onComplete listener " + listener);
                                HttpChannelState.LOG.debug(var4);
                            }
                        }
                    }

                    public String toString() {
                        return "onComplete";
                    }
                };
                this.runInContext(event, callback);
            }
            event.completed();
        }
    }

    protected void recycle() {
        this.cancelTimeout();
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("recycle {}", this.toStringLocked());
            }
            switch(this._state) {
                case ASYNC_IO:
                case DISPATCHED:
                    throw new IllegalStateException(this.getStatusStringLocked());
                case ASYNC_WAIT:
                default:
                    this._asyncListeners = null;
                    this._state = HttpChannelState.State.IDLE;
                    this._async = HttpChannelState.Async.NOT_ASYNC;
                    this._initial = true;
                    this._asyncRead = HttpChannelState.AsyncRead.IDLE;
                    this._asyncWritePossible = false;
                    this._timeoutMs = DEFAULT_TIMEOUT;
                    this._event = null;
                    return;
                case UPGRADED:
            }
        }
    }

    public void upgrade() {
        this.cancelTimeout();
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("upgrade {}", this.toStringLocked());
            }
            switch(this._state) {
                case IDLE:
                case COMPLETED:
                    this._asyncListeners = null;
                    this._state = HttpChannelState.State.UPGRADED;
                    this._async = HttpChannelState.Async.NOT_ASYNC;
                    this._initial = true;
                    this._asyncRead = HttpChannelState.AsyncRead.IDLE;
                    this._asyncWritePossible = false;
                    this._timeoutMs = DEFAULT_TIMEOUT;
                    this._event = null;
                    return;
                default:
                    throw new IllegalStateException(this.getStatusStringLocked());
            }
        }
    }

    protected void scheduleDispatch() {
        this._channel.execute(this._channel);
    }

    protected void cancelTimeout() {
        AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            event = this._event;
        }
        this.cancelTimeout(event);
    }

    protected void cancelTimeout(AsyncContextEvent event) {
        if (event != null) {
            event.cancelTimeoutTask();
        }
    }

    public boolean isIdle() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == HttpChannelState.State.IDLE;
        }
        return var3;
    }

    public boolean isExpired() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._async == HttpChannelState.Async.EXPIRED;
        }
        return var3;
    }

    public boolean isInitial() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._initial;
        }
        return var3;
    }

    public boolean isSuspended() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == HttpChannelState.State.ASYNC_WAIT || this._state == HttpChannelState.State.DISPATCHED && this._async == HttpChannelState.Async.STARTED;
        }
        return var3;
    }

    boolean isCompleting() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == HttpChannelState.State.COMPLETING;
        }
        return var3;
    }

    boolean isCompleted() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._state == HttpChannelState.State.COMPLETED;
        }
        return var3;
    }

    public boolean isAsyncStarted() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            if (this._state != HttpChannelState.State.DISPATCHED) {
                return this._async == HttpChannelState.Async.STARTED || this._async == HttpChannelState.Async.EXPIRING;
            }
            var3 = this._async != HttpChannelState.Async.NOT_ASYNC;
        }
        return var3;
    }

    public boolean isAsyncComplete() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = this._async == HttpChannelState.Async.COMPLETE;
        }
        return var3;
    }

    public boolean isAsync() {
        boolean var3;
        try (Locker.Lock lock = this._locker.lock()) {
            var3 = !this._initial || this._async != HttpChannelState.Async.NOT_ASYNC;
        }
        return var3;
    }

    public Request getBaseRequest() {
        return this._channel.getRequest();
    }

    public HttpChannel getHttpChannel() {
        return this._channel;
    }

    public ContextHandler getContextHandler() {
        AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            event = this._event;
        }
        return this.getContextHandler(event);
    }

    ContextHandler getContextHandler(AsyncContextEvent event) {
        if (event != null) {
            ContextHandler.Context context = (ContextHandler.Context) event.getServletContext();
            if (context != null) {
                return context.getContextHandler();
            }
        }
        return null;
    }

    public ServletResponse getServletResponse() {
        AsyncContextEvent event;
        try (Locker.Lock lock = this._locker.lock()) {
            event = this._event;
        }
        return this.getServletResponse(event);
    }

    public ServletResponse getServletResponse(AsyncContextEvent event) {
        return (ServletResponse) (event != null && event.getSuppliedResponse() != null ? event.getSuppliedResponse() : this._channel.getResponse());
    }

    void runInContext(AsyncContextEvent event, Runnable runnable) {
        ContextHandler contextHandler = this.getContextHandler(event);
        if (contextHandler == null) {
            runnable.run();
        } else {
            contextHandler.handle(this._channel.getRequest(), runnable);
        }
    }

    public Object getAttribute(String name) {
        return this._channel.getRequest().getAttribute(name);
    }

    public void removeAttribute(String name) {
        this._channel.getRequest().removeAttribute(name);
    }

    public void setAttribute(String name, Object attribute) {
        this._channel.getRequest().setAttribute(name, attribute);
    }

    public void onReadUnready() {
        boolean interested = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onReadUnready {}", this.toStringLocked());
            }
            switch(this._asyncRead) {
                case POSSIBLE:
                case REGISTER:
                case PRODUCING:
                case REGISTERED:
                default:
                    break;
                case READY:
                case IDLE:
                    if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                        interested = true;
                        this._asyncRead = HttpChannelState.AsyncRead.REGISTERED;
                    } else {
                        this._asyncRead = HttpChannelState.AsyncRead.REGISTER;
                    }
            }
        }
        if (interested) {
            this._channel.asyncReadFillInterested();
        }
    }

    public boolean onContentAdded() {
        boolean woken = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onContentAdded {}", this.toStringLocked());
            }
            switch(this._asyncRead) {
                case POSSIBLE:
                    throw new IllegalStateException(this.toStringLocked());
                case READY:
                case IDLE:
                default:
                    break;
                case REGISTER:
                case REGISTERED:
                    this._asyncRead = HttpChannelState.AsyncRead.READY;
                    if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                        woken = true;
                        this._state = HttpChannelState.State.ASYNC_WOKEN;
                    }
                    break;
                case PRODUCING:
                    this._asyncRead = HttpChannelState.AsyncRead.READY;
            }
        }
        return woken;
    }

    public boolean onReadReady() {
        boolean woken = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onReadReady {}", this.toStringLocked());
            }
            switch(this._asyncRead) {
                case IDLE:
                    this._asyncRead = HttpChannelState.AsyncRead.READY;
                    if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                        woken = true;
                        this._state = HttpChannelState.State.ASYNC_WOKEN;
                    }
                    break;
                default:
                    throw new IllegalStateException(this.toStringLocked());
            }
        }
        return woken;
    }

    public boolean onReadPossible() {
        boolean woken = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onReadPossible {}", this.toStringLocked());
            }
            switch(this._asyncRead) {
                case REGISTERED:
                    this._asyncRead = HttpChannelState.AsyncRead.POSSIBLE;
                    if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                        woken = true;
                        this._state = HttpChannelState.State.ASYNC_WOKEN;
                    }
                    break;
                default:
                    throw new IllegalStateException(this.toStringLocked());
            }
        }
        return woken;
    }

    public boolean onReadEof() {
        boolean woken = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onEof {}", this.toStringLocked());
            }
            this._asyncRead = HttpChannelState.AsyncRead.READY;
            if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                woken = true;
                this._state = HttpChannelState.State.ASYNC_WOKEN;
            }
        }
        return woken;
    }

    public boolean onWritePossible() {
        boolean wake = false;
        try (Locker.Lock lock = this._locker.lock()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("onWritePossible {}", this.toStringLocked());
            }
            this._asyncWritePossible = true;
            if (this._state == HttpChannelState.State.ASYNC_WAIT) {
                this._state = HttpChannelState.State.ASYNC_WOKEN;
                wake = true;
            }
        }
        return wake;
    }

    public static enum Action {

        DISPATCH,
        ASYNC_DISPATCH,
        ERROR_DISPATCH,
        ASYNC_ERROR,
        WRITE_CALLBACK,
        READ_PRODUCE,
        READ_CALLBACK,
        COMPLETE,
        TERMINATED,
        WAIT
    }

    private static enum Async {

        NOT_ASYNC,
        STARTED,
        DISPATCH,
        COMPLETE,
        EXPIRING,
        EXPIRED,
        ERRORING,
        ERRORED
    }

    private static enum AsyncRead {

        IDLE,
        REGISTER,
        REGISTERED,
        POSSIBLE,
        PRODUCING,
        READY
    }

    public static enum State {

        IDLE,
        DISPATCHED,
        THROWN,
        ASYNC_WAIT,
        ASYNC_WOKEN,
        ASYNC_IO,
        ASYNC_ERROR,
        COMPLETING,
        COMPLETED,
        UPGRADED
    }
}